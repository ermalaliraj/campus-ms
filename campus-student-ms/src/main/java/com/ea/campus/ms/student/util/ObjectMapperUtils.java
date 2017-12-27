package com.ea.campus.ms.student.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public final class ObjectMapperUtils {

	private ObjectMapperUtils() {
	}

	public static ObjectMapper getObjectMapper() {
		return initializeMapper(new ObjectMapper());
	}

	/**
	 * Initializes the specified mapper in order that it will contain some optimizations for the object transfer.
	 */
	public static ObjectMapper initializeMapper(ObjectMapper objectMapper) {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final SimpleModule module = new SimpleModule();		
		objectMapper.registerModule(module);

		return objectMapper;
	}
}
