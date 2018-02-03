package com.ea.rest.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;

public class SecurityStateAffirmativeBased extends AffirmativeBased {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityStateAffirmativeBased.class);
	private static final String PRD = "prd";
	private ApplicationContext applicationContext;
	private GrantedAuthoritiesWebAuthenticationDetailsSource grantedAuthoritiesWebAuthenticationDetailsSource;

	public SecurityStateAffirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
		super(decisionVoters);
	}

	private GrantedAuthoritiesWebAuthenticationDetailsSource getAuthenticationDetailsSource() {
		if (this.grantedAuthoritiesWebAuthenticationDetailsSource == null) {
			this.grantedAuthoritiesWebAuthenticationDetailsSource = new GrantedAuthoritiesWebAuthenticationDetailsSource();
			this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this.grantedAuthoritiesWebAuthenticationDetailsSource);
		}
		return this.grantedAuthoritiesWebAuthenticationDetailsSource;
	}

	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
		EAUser eaUser = tryConvert(authentication);
		if (!(authentication.getDetails() instanceof EAWebAuthenticationDetails)) {
			((AbstractAuthenticationToken) authentication)
					.setDetails(getAuthenticationDetailsSource().buildDetails(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
		}
		retrieveAndAddMissingSecurityStates(object, eaUser);
		try {
			super.decide(authentication, object, configAttributes);
		} catch (AccessDeniedException e) {
			addDetailsToMessageAndRethrowAccessDeniedException(e, configAttributes, object, eaUser);
		}
	}

	private EAUser tryConvert(Authentication authentication) throws IllegalArgumentException {
		Object principal = authentication.getPrincipal();
		if ((principal instanceof EAUser)) {
			return (EAUser) principal;
		}
		if ((principal instanceof UserDetails)) {
			String password = ((UserDetails) principal).getPassword() == null ? "" : ((UserDetails) principal).getPassword();
			return new EAUser(((UserDetails) principal).getUsername(), password, ((UserDetails) principal).getAuthorities(), new HashMap<String, Object>());
		}
		if ((principal instanceof String)) {
			return new EAUser((String) principal, "", authentication.getAuthorities(), new HashMap<String, Object>());
		}
		throw new IllegalArgumentException("Could not convert Principal into EAUser");
	}

	protected void addDetailsToMessageAndRethrowAccessDeniedException(AccessDeniedException e, Collection<ConfigAttribute> configAttributes, Object object,
			EAUser eaUser) {
		String originalWithUUID = e.getMessage() + " (" + UUID.randomUUID().toString() + ") ";
		String detailedMessage = addDetailsToAccessDeniedMessage(originalWithUUID, configAttributes, object, eaUser);
		if (isProduction()) {
			LOG.error(detailedMessage);
			throw new AccessDeniedException(originalWithUUID, e);
		}
		throw new AccessDeniedException(detailedMessage, e);
	}

	private String addDetailsToAccessDeniedMessage(String message, Collection<ConfigAttribute> configAttributes, Object object, EAUser eaUser) {
		StringBuilder stringBuilder = new StringBuilder(message);
		stringBuilder.append(" ( ");

		Iterator<ConfigAttribute> it = configAttributes.iterator();
		if (it.hasNext()) {
			ConfigAttribute configAttribute = it.next();
			stringBuilder.append("#ATTR: " + configAttribute);
		}
		ParameterNameDiscoverer discoverer;
		if ((object instanceof MethodInvocation)) {
			MethodInvocation methodInvocation = (MethodInvocation) object;
			discoverer = new LocalVariableTableParameterNameDiscoverer();
			String[] parameterNames = discoverer.getParameterNames(methodInvocation.getMethod());
			Object[] arguments = methodInvocation.getArguments();

			stringBuilder.append(" #PARAMS: ");
			for (int i = 0; i < parameterNames.length; i++) {
				stringBuilder.append(parameterNames[i] + ": " + arguments[i] + " ");
			}
		}
		Map<String, Object> states = eaUser.getGrantedStates();
		if ((states != null) && (states.size() > 0)) {
			stringBuilder.append(" #STATES: ");
			for (String key : states.keySet()) {
				stringBuilder.append(states.get(key) + " ");
			}
		}
		stringBuilder.append(" ) ");
		return stringBuilder.toString();
	}

	protected void retrieveAndAddMissingSecurityStates(Object object, EAUser user) {
		List<Object> states = Lists.newArrayList();
		if ((object instanceof MethodInvocation)) {
			MethodInvocation methodInvocation = (MethodInvocation) object;

			SecurityStates securityStates = methodInvocation.getMethod().getAnnotation(SecurityStates.class);
			if (securityStates != null) {
				for (Class<?> securityStateClazz : securityStates.states()) {
					if (!user.hasGrantedState(securityStateClazz)) {
						SecurityState securityState = securityStateClazz.getAnnotation(SecurityState.class);
						try {
							SecurityStateService securityStateService = this.applicationContext.getBean(securityState.serviceRef(),
									SecurityStateService.class);
							states.add(securityStateService.getSecurityState(methodInvocation.getMethod(), methodInvocation.getArguments()));
						} catch (Exception e) {
							try {
								states.add(Class.forName(securityStateClazz.getName()).newInstance());
							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException localClassNotFoundException) {
							}
							break;
						}
					}
				}
			}
		}
		user.addGrantedStates(states);
	}

	private boolean isProduction() {
		for (String profile : this.applicationContext.getEnvironment().getActiveProfiles()) {
			if (PRD.equals(profile.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
