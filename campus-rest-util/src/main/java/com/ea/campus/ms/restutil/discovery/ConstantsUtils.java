package com.ea.campus.ms.restutil.discovery;

import java.util.Arrays;
import java.util.List;

public class ConstantsUtils {
	public static final List<String> getSecurityHeaders() {
		return Arrays.asList(new String[] {"x-forwarded-host", "x-forwarded-port" });
	}
}
