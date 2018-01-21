package org.springframework.hateoas.mvc;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.core.AnnotationAttribute;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.hateoas.core.MethodParameters;
import org.springframework.hateoas.core.DummyInvocationUtils.LastInvocationAware;
import org.springframework.hateoas.core.DummyInvocationUtils.MethodInvocation;
import org.springframework.hateoas.mvc.AnnotatedParametersParameterAccessor.BoundMethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

public class ParameterizedControllerLinkBuilder extends ControllerLinkBuilder {
	protected static final MappingDiscoverer DISCOVERER = new AnnotationMappingDiscoverer(RequestMapping.class);
	protected static final AnnotatedParametersParameterAccessor REQUEST_PARAM_ACCESSOR = new ParameterizedControllerLinkBuilder.RequestParamParameterAccessor();
	private final UriComponentsBuilder builder;
	private List<TemplateVariable> optionalParameters = new ArrayList();

	public ParameterizedControllerLinkBuilder(UriComponentsBuilder builder) {
		super(builder);
		this.builder = builder;
	}

	public ParameterizedControllerLinkBuilder(UriComponentsBuilder builder, List<TemplateVariable> optionalParameters) {
		super(builder);
		this.builder = builder;
		this.optionalParameters = optionalParameters;
	}

	public static ControllerLinkBuilder linkTo(Object invocationValue) {
		List<TemplateVariable> optionalParameters = new ArrayList();
		Assert.isInstanceOf(LastInvocationAware.class, invocationValue);
		LastInvocationAware invocations = (LastInvocationAware) invocationValue;
		MethodInvocation invocation = invocations.getLastInvocation();
		Method method = invocation.getMethod();
		String mapping = DISCOVERER.getMapping(invocation.getTargetType(), method);
		UriComponentsBuilder builder = ControllerLinkBuilder.getBuilder().path(mapping);
		List<BoundMethodParameter> parameters = REQUEST_PARAM_ACCESSOR.getBoundParameters(invocation);

		for (int i = 0; i < parameters.size(); ++i) {
			bindRequestParameters(builder, (ParameterizedControllerLinkBuilder.RequestParamParameterAccessor.PublicBoundMethodParameter) parameters.get(i),
					optionalParameters);
		}

		return new ParameterizedControllerLinkBuilder(builder, optionalParameters);
	}

	public static boolean typeOf(Class<?> clazz, Class<?> expected) {
		Assert.notNull(clazz);
		Assert.notNull(expected);
		if (clazz.equals(expected)) {
			return true;
		} else {
			Class[] var3 = clazz.getInterfaces();
			int var4 = var3.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				Class<?> i = var3[var5];
				if (i.equals(expected)) {
					return true;
				}

				boolean found = typeOf(i, expected);
				if (found) {
					return true;
				}
			}

			Class<?> s = clazz.getSuperclass();
			if (s != null) {
				if (s.equals(expected)) {
					return true;
				} else {
					return typeOf(s, expected);
				}
			} else {
				return false;
			}
		}
	}

	protected static void bindRequestParameters(UriComponentsBuilder builder,
			ParameterizedControllerLinkBuilder.RequestParamParameterAccessor.PublicBoundMethodParameter parameter, List<TemplateVariable> optionalParameters) {
		Object value = parameter.getValue();
		String key = parameter.getVariableName();
		Class<?> parameterType = parameter.getParameter().getParameterType();
		RequestParam annotation = (RequestParam) parameter.getParameter().getParameterAnnotation(RequestParam.class);
		if (typeOf(parameterType, Map.class)) {
			Map<String, Object> requestParams = (Map) value;
			Iterator var8;
			String k;
			if (annotation.required()) {
				var8 = requestParams.keySet().iterator();

				while (var8.hasNext()) {
					k = (String) var8.next();
					builder.queryParam(k, new Object[] { "{" + k + "}" });
				}
			} else {
				var8 = requestParams.keySet().iterator();

				while (var8.hasNext()) {
					k = (String) var8.next();
					boolean append = !optionalParameters.isEmpty();
					VariableType type = append ? VariableType.REQUEST_PARAM_CONTINUED : VariableType.REQUEST_PARAM;
					optionalParameters.add(new TemplateVariable(k, type));
				}
			}
		} else {
			boolean append;
			VariableType type;
			if (typeOf(parameterType, Collection.class)) {
				if (annotation != null) {
					if (annotation.required()) {
						builder.queryParam(key, new Object[] { "{" + key + "}" });
					} else {
						append = !optionalParameters.isEmpty();
						type = append ? VariableType.REQUEST_PARAM_CONTINUED : VariableType.REQUEST_PARAM;
						optionalParameters.add(new TemplateVariable(key, type));
					}
				} else {
					builder.queryParam(key, new Object[] { "{" + key + "}" });
				}
			} else if (annotation != null) {
				if (annotation.required()) {
					builder.queryParam(key, new Object[] { "{" + key + "}" });
				} else {
					append = !optionalParameters.isEmpty();
					type = append ? VariableType.REQUEST_PARAM_CONTINUED : VariableType.REQUEST_PARAM;
					optionalParameters.add(new TemplateVariable(key, type));
				}
			} else {
				builder.queryParam(key, new Object[] { "{" + key + "}" });
			}
		}

	}

	protected static UriComponentsBuilder getUriBuilderAndBindParameters(MethodInvocation invocation, Method method, List<TemplateVariable> optionalParameters) {
		String mapping = DISCOVERER.getMapping(invocation.getTargetType(), method);
		UriComponentsBuilder builder = ControllerLinkBuilder.getBuilder().path(mapping);
		List<BoundMethodParameter> parameters = REQUEST_PARAM_ACCESSOR.getBoundParameters(invocation);

		for (int i = 0; i < parameters.size(); ++i) {
			bindRequestParameters(builder, (ParameterizedControllerLinkBuilder.RequestParamParameterAccessor.PublicBoundMethodParameter) parameters.get(i),
					optionalParameters);
		}

		return builder;
	}

	protected static MethodInvocation getLastMethodInvocation(Object invocationValue) {
		Assert.isInstanceOf(LastInvocationAware.class, invocationValue);
		LastInvocationAware invocations = (LastInvocationAware) invocationValue;
		return invocations.getLastInvocation();
	}

	public Link withRel(String rel) {
		return new Link((new UriTemplate(this.builder.build().toString())).with(new TemplateVariables(this.optionalParameters)), rel);
	}

	public String toString() {
		try {
			return URLDecoder.decode(this.toUri().normalize().toASCIIString(), "UTF-8");
		} catch (UnsupportedEncodingException var2) {
			throw new RuntimeException("Cannot decode Link path to UTF-8", var2);
		}
	}

	protected static class RequestParamParameterAccessor extends AnnotatedParametersParameterAccessor {
		private final AnnotationAttribute attribute = new AnnotationAttribute(RequestParam.class);

		public RequestParamParameterAccessor() {
			super(new AnnotationAttribute(RequestParam.class));
		}

		protected Object verifyParameterValue(MethodParameter parameter, Object value) {
			return ParameterizedControllerLinkBuilder.typeOf(parameter.getParameterType(), Map.class) ? super.verifyParameterValue(parameter, value) : value;
		}

		public List<BoundMethodParameter> getBoundParameters(MethodInvocation invocation) {
			Assert.notNull(invocation, "MethodInvocation must not be null!");
			MethodParameters parameters = new MethodParameters(invocation.getMethod());
			Object[] arguments = invocation.getArguments();
			List<BoundMethodParameter> result = new ArrayList();
			Iterator var5 = parameters.getParametersWith(this.attribute.getAnnotationType()).iterator();

			while (var5.hasNext()) {
				MethodParameter parameter = (MethodParameter) var5.next();
				Object value = arguments[parameter.getParameterIndex()];
				Object verifiedValue = this.verifyParameterValue(parameter, value);
				if (verifiedValue != null) {
					result.add(new ParameterizedControllerLinkBuilder.RequestParamParameterAccessor.PublicBoundMethodParameter(parameter, value, this.attribute));
				} else {
					result.add(new ParameterizedControllerLinkBuilder.RequestParamParameterAccessor.PublicBoundMethodParameter(parameter, new Object(), this.attribute));
				}
			}

			return result;
		}

		public static class PublicBoundMethodParameter extends BoundMethodParameter {
			MethodParameter parameter;

			public PublicBoundMethodParameter(MethodParameter parameter, Object value, AnnotationAttribute attribute) {
				super(parameter, value, attribute);
				this.parameter = parameter;
			}

			public MethodParameter getParameter() {
				return this.parameter;
			}
		}
	}
}
