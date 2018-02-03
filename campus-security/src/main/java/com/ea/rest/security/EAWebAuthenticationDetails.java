package com.ea.rest.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.ea.campus.ms.restutil.converter.JsonToObjectConverter;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class EAWebAuthenticationDetails extends WebAuthenticationDetails implements GrantedAuthoritiesContainer {
	private static final long serialVersionUID = 6096251153655783616L;
	private final List<GrantedAuthority> authorities;
	private Map<String, Object> grantedStates = Maps.newHashMap();

	public EAWebAuthenticationDetails(HttpServletRequest request, ApplicationStateHolder applicationStateHolder) {
		super(request);

		this.authorities = getProfilesFromHeader(request);
		this.grantedStates = extract(request, applicationStateHolder);
	}

	private List<GrantedAuthority> getProfilesFromHeader(HttpServletRequest request) {
		return new ArrayList<>();
	}

	private Map<String, Object> extract(HttpServletRequest request, ApplicationStateHolder applicationStateHolder) {
		Map<String, Object> items = CollectionHelper.newHashMap();
		Collection<String> securityHeaderKeys = extractRelevantKeys(request.getHeaderNames(), applicationStateHolder.getHeaderNames());
		for (String key : securityHeaderKeys) {
			try {
				if (!StringUtils.isEmpty(request.getHeader(key))) {
					items.put(applicationStateHolder.getSecurityName(key),
							new JsonToObjectConverter<>(Class.forName(applicationStateHolder.getStateClassName(key))).convert(request.getHeader(key)));
				}
			} catch (ClassNotFoundException localClassNotFoundException) {
			}
		}
		return items;
	}

	private Collection<String> extractRelevantKeys(Enumeration<String> requestHeaderNames, final Collection<String> securityKeys) {
		return Collections2.filter(Collections.list(requestHeaderNames), new Predicate<String>() {
			public boolean apply(String input) {
				return securityKeys.contains(input.toLowerCase());
			}
		});
	}

	public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
		return this.authorities;
	}

	public Map<String, Object> getGrantedStates() {
		return this.grantedStates;
	}
}
