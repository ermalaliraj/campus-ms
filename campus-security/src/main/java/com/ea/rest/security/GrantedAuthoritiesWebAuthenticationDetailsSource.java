package com.ea.rest.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class GrantedAuthoritiesWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {
	@Autowired
	ApplicationStateHolder applicationStateHolder;

	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new EAWebAuthenticationDetails(context, this.applicationStateHolder);
	}
}
