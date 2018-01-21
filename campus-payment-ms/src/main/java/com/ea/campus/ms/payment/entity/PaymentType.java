package com.ea.campus.ms.payment.entity;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PaymentType {

	OK, NOTOK, NEUTRAL;

	private static final Map<String, PaymentType> MAP = Stream.of(PaymentType.values()).collect(Collectors.toMap(Enum::name, Function.identity()));

	public static PaymentType fromName(String name) {
		return MAP.get(name);
	}
}
