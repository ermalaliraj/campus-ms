package com.ea.campus.ms.student.discovery;

import java.util.Arrays;
import java.util.List;

public class ConstantsUtils {
	public static final List<String> getSecurityHeaders() {
		return Arrays.asList(new String[] {"X-Forwarded-Host", "x-forwarded-port" });
	}
}
