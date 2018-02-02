package com.ea.campus.ms.restutil.context;

import java.util.HashMap;
import java.util.Map;

public class ServiceExecutionContext {

	private Map<String, String> headers = new HashMap<>();
	private Map<Class<?>, Object> jwts = new HashMap<>();

	ServiceExecutionContext() {
	}

	ServiceExecutionContext(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeader(String key, String value) {
		headers.put(key, value);
	}

	public void setHeaders(final Map<String, String> headers) {
		this.headers = headers;
	}

	public boolean addJwt(Object token) {
		return addJwt(token, false);
	}

	public boolean addJwt(Object token, boolean force) {
		if (!token.getClass().isAnnotationPresent(SecurityState.class)) {
			throw new IllegalArgumentException("Token is not annotated with SecurityState!");
		}

		if (!force) {
			if (jwts.containsKey(token.getClass())) {
				return false;
			}
		}
		jwts.put(token.getClass(), token);
		return true;
	}

	public Object getJwt(Class<?> clazz) {
		return jwts.get(clazz);
	}

	public void removeJwt(Class<?> clazz) {
		jwts.remove(clazz);
	}

	public Map<Class<?>, Object> getJwts() {
		return jwts;
	}

	public void setJwts(Map<Class<?>, Object> jwts) {
		this.jwts = jwts;
	}
}
