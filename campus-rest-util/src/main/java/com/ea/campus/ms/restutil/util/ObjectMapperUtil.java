package com.ea.campus.ms.restutil.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperUtil {
	
	@SuppressWarnings("unused")
	private static final transient Logger log = LoggerFactory.getLogger(ObjectMapperUtil.class);
	
	public static Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.failOnUnknownProperties(false);
		builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
		SimpleModule module = new SimpleModule();
		module.setDeserializerModifier(new BeanDeserializerModifier() {
			public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
				return (JsonDeserializer<?>) (deserializer instanceof BeanDeserializer ? new ObjectMapperUtil.EmptyCollectionDeserializer(deserializer) : deserializer);
			}
		});
		builder.modules(module);
		return builder;
	}

	public static ObjectMapper getDefaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		configure(objectMapper);
		return objectMapper;
	}

	public static void configure(ObjectMapper objectMapper) {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
		
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		final SimpleModule module = new SimpleModule();

		module.setDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
				if (deserializer instanceof BeanDeserializer) {
					return new EmptyCollectionDeserializer(deserializer);
				}

				return deserializer;
			}
		});

		objectMapper.registerModule(module);
	}

	@SuppressWarnings("serial")
	private static class EmptyCollectionDeserializer extends BeanDeserializer {
		public EmptyCollectionDeserializer(JsonDeserializer<?> defaultDeserializer) {
			super((BeanDeserializer) defaultDeserializer);
		}

		@Override
		public Object deserialize(JsonParser jp, DeserializationContext context) throws IOException {
			final Object bean = super.deserialize(jp, context);

			ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					field.setAccessible(true);
					if (field.get(bean) == null) {
						if (List.class.equals(field.getType())) {
							field.set(bean, new ArrayList<>());
						} else if (Map.class.equals(field.getType())) {
							field.set(bean, new HashMap<>());
						} else if (Set.class.equals(field.getType())) {
							field.set(bean, new HashSet<>());
						}
					}
				}
			});

			return bean;
		}
	}

}
