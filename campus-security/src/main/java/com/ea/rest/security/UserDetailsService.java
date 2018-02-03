package com.ea.rest.security;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
	protected static final Logger LOG = LoggerFactory.getLogger(UserDetailsService.class);
	@Autowired
	private ApplicationStateHolder applicationStateHolder;

	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		String username = (String) token.getPrincipal();
		String password = "";
		Collection<? extends GrantedAuthority> authorities = ((EAWebAuthenticationDetails) token.getDetails()).getGrantedAuthorities(); 
		Map<String, Object> grantedStates = ((EAWebAuthenticationDetails) token.getDetails()).getGrantedStates();
		return new EAUser(username, password, authorities, grantedStates); 
	}

	public ApplicationStateHolder getApplicationStateHolder() {
		return applicationStateHolder;
	}

	public void setApplicationStateHolder(ApplicationStateHolder applicationStateHolder) {
		this.applicationStateHolder = applicationStateHolder;
	}
}
