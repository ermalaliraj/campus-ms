package com.ea.campus.ms.student.context;

import java.util.Map;

public class ImmutableServiceExecutionContext extends ServiceExecutionContext {

	private static final String IMMUTABLE = "Cannot modify immutable object.";

	ImmutableServiceExecutionContext(final ServiceExecutionContext serviceExecutionContext) {
		super(serviceExecutionContext.getHeaders());
	}

	@Override
	public void setHeaders(final Map<String, String> headers) {
		throw new UnsupportedOperationException(IMMUTABLE);
	}
}
