package com.ea.campus.ms.restutil.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
public @interface SecurityState {
	String headerName() default "";

	String serviceRef() default "";
}
