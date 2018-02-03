package com.ea.campus.ms.payment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

import com.ea.campus.ms.payment.constants.Curie;

@Configuration
public class WebConfiguration {

	@Bean
	@Autowired
	public CurieProvider curieProvider(@Value("${server.contextPath}") String contextPath) {
		return new DefaultCurieProvider(Curie.NAMESPACE, new UriTemplate("/docs/api-guide.html#resources-{rel}"));
	}

	@Bean
	@Autowired
	public FilterRegistrationBean shallowEtagHeaderFilter(ShallowEtagHeaderExclHystrixFilterRegistrationBean shallowEtagHeaderExclHystrixFilterRegistrationBean) {
		return shallowEtagHeaderExclHystrixFilterRegistrationBean.register();
	}

	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
		final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);

		return filterRegistrationBean;
	}

}
