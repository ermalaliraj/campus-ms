package com.ea.campus.ms.student.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class StubFileReader {
	
	private static final ObjectMapper OBJECT_MAPPER = ObjectMapperUtils.getDefaultObjectMapper();

	private StubFileReader() {
	}
	
	public static <T> T createResourceFromFile(String path, Class<T> clazz) throws StubFileException {
		String response = readFile(path);

		try {
			return OBJECT_MAPPER.readValue(response, clazz);
		} catch (IOException e) {
			throw new StubFileException("Error parsing " + path + " to " + clazz.getCanonicalName(), e);
		}
	}
	
	public static String readFile(String path) throws StubFileException {
		return readFile(path, StubFileReader.class);
	}
	
	public static String readFile(String path, Class<?> clazz) throws StubFileException {
		InputStream resourceAsStream = clazz.getClassLoader().getResourceAsStream(path);
		if (resourceAsStream == null) {
			throw new StubFileException("Cannot read file " + path);
		}
		try {
			return IOUtils.toString(resourceAsStream);
		} catch (IOException e) {
			throw new StubFileException("IOException while reading " + path, e);
		}
	}

}
