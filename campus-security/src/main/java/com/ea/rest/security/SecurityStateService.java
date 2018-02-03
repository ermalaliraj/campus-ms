package com.ea.rest.security;

import java.lang.reflect.Method;

public abstract interface SecurityStateService {
	public abstract Object getSecurityState(Method paramMethod, Object[] paramArrayOfObject);
}
