package com.ea.campus.ms.restutil.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class JsonToObjectConverter<T> implements Converter<String, T> {
	private static final Logger LOG = LoggerFactory.getLogger(JsonToObjectConverter.class);
	private static final ObjectReader OBJECT_READER = new ObjectMapper().reader();
	private Class<T> type;

	public JsonToObjectConverter(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public T convert(String objectAsString) {
		try {
			if (StringUtils.isEmpty(objectAsString)) {
				return null;
			}
			return (T) OBJECT_READER.forType(this.type).readValue(objectAsString);
		} catch (IOException e) {
			LOG.error("unable to read value", e);
			throw new IllegalArgumentException(objectAsString, e);
		}
	}
}
