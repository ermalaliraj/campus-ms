package com.ea.rest.security.interceptor;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ea.campus.ms.restutil.context.ExecutionContextAccessor;
import com.ea.campus.ms.restutil.context.ServiceExecutionContext;
import com.ea.campus.ms.restutil.util.ObjectMapperUtil;
import com.ea.rest.security.ApplicationStateHolder;
import com.ea.rest.security.SecurityState;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

	@Autowired
	private ApplicationStateHolder applicationStateHolder;
	private ObjectMapper objectMapper = ObjectMapperUtil.getDefaultObjectMapper();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ServiceExecutionContext executionContext = ExecutionContextAccessor.getExecutionContext();

		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			String className = applicationStateHolder.getStateClassName(headerName);
			if (!StringUtils.isEmpty(className)) {
				String header = request.getHeader(headerName);
				Object token = objectMapper.readValue(header, Class.forName(className));
				executionContext.addJwt(token);
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		ServiceExecutionContext executionContext = ExecutionContextAccessor.getExecutionContext();

		for (Object token : executionContext.getJwts().values()) {
			if (!token.getClass().isAnnotationPresent(SecurityState.class)) {
				log.warn("Token '{}' is not annotated with SecurityState!", token.getClass());
				continue;
			}

			String header = token.getClass().getAnnotation(SecurityState.class).headerName();
			response.addHeader(header, objectMapper.writeValueAsString(token));
		}
	}
}