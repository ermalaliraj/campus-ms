package com.ea.campus.ms.payment.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class ShallowEtagHeaderExclHystrixFilterRegistrationBean {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	public FilterRegistrationBean register() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ShallowEtagHeaderFilter());
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		Map<RequestMappingInfo, HandlerMethod> mappingsMap = this.requestMappingHandlerMapping.getHandlerMethods();
		Set<String> urlMappings = new HashSet();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : mappingsMap.entrySet()) {
			Set<String> urlPattern = ((RequestMappingInfo) entry.getKey()).getPatternsCondition().getPatterns();
			urlMappings.addAll(urlPattern);
		}
		Object replacedUrlMappings = replacePathVariablesWithWildCard(urlMappings);
		registration.setUrlPatterns((Collection) replacedUrlMappings);
		return registration;
	}

	private Collection<String> replacePathVariablesWithWildCard(Collection<String> urlPatterns) {
		Collection<String> replacedUrls = new ArrayList();
		for (String pattern : urlPatterns) {
			String pathBeforePathVariable = pattern.split("\\{([^}]*?)\\}")[0];
			if (pathBeforePathVariable.endsWith("/")) {
				replacedUrls.add(pathBeforePathVariable + "*");
			} else {
				replacedUrls.add(pathBeforePathVariable + "/*");
			}
		}
		replacedUrls.remove("/*");
		replacedUrls.add("/");
		return replacedUrls;
	}
}
