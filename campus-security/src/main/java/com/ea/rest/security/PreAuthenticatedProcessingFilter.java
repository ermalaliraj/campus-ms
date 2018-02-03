package com.ea.rest.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class PreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return request.getHeader("userid");
	}

	protected String getPreAuthenticatedCredentials(HttpServletRequest request) {
		return "N/A";
	}
}
