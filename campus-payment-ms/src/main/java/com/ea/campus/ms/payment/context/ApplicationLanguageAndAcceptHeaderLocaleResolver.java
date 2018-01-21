package com.ea.campus.ms.payment.context;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class ApplicationLanguageAndAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

	private static final String X_APPLICATION_LANGUAGE = "x-application-language";

	public ApplicationLanguageAndAcceptHeaderLocaleResolver() {
	}

	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader(X_APPLICATION_LANGUAGE);
		if (StringUtils.isEmpty(language)) {
			language = "en";
		}
		return new Locale(language);
	}
}
