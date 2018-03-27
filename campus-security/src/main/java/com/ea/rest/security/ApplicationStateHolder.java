package com.ea.rest.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * Keep a map of the classes present to handle specific headers. 
 */
@Component
public class ApplicationStateHolder {
	private static final Logger log = LoggerFactory.getLogger(ApplicationStateHolder.class);
	private Map<String, Pair<String, String>> applicationStates = new HashMap<>();

	@PostConstruct
	public void init() {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

		scanner.addIncludeFilter(new AnnotationTypeFilter(SecurityState.class));
		for (BeanDefinition bd : scanner.findCandidateComponents("com.ea")) {
			Map<String, Object> annotationAttributes = ((ScannedGenericBeanDefinition) bd).getMetadata().getAnnotationAttributes("com.ea.rest.security.SecurityState");
			String headerName = ((String) annotationAttributes.get("headerName")).toLowerCase();
			String securityName = null;
			try {
				securityName = Class.forName(bd.getBeanClassName()).getSimpleName();
			} catch (ClassNotFoundException localClassNotFoundException) {
			}
			String className = bd.getBeanClassName();

			log.info("Adding in applicationStates for header: " + headerName + ", securityName: " + securityName + ", class: " + className + ")");
			this.applicationStates.put(headerName, new ImmutablePair<String, String>(securityName, className));
		}
	}

	public String getSecurityName(String header) {
		return this.applicationStates.containsKey(header) ? (String) this.applicationStates.get(header).getLeft() : null;
	}

	public String getStateClassName(String header) {
		return this.applicationStates.containsKey(header) ? (String) this.applicationStates.get(header).getRight() : null;
	}

	public Collection<String> getHeaderNames() {
		return this.applicationStates.keySet();
	}
}
