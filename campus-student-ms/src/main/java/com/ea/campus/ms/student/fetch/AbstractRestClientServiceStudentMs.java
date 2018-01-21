package com.ea.campus.ms.student.fetch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import com.ea.campus.ms.student.context.ExecutionContextAccessor;

public abstract class AbstractRestClientServiceStudentMs extends AbstractRestClientService {

	private static final List<String> HEADERS_TO_KEEP = Arrays.asList("header1", "header2");

	@Override
	protected String microservicePropertyName() {
		return "student-ms";
	}

	@Override
	public HttpHeaders getCurrentHeaders() {
		HttpHeaders currentHeaders = new HttpHeaders();
		Map<String, String> headers = ExecutionContextAccessor.getExecutionContext().getHeaders();
		for (Map.Entry<String, String> header : headers.entrySet()) {
			currentHeaders.add(header.getKey(), header.getValue());
		}
		return currentHeaders;
	}

	public static HttpHeaders filterHeaders(HttpHeaders httpHeaders) {
		if (httpHeaders == null) {
			return null;
		}
		HttpHeaders forwardedHeaders = new HttpHeaders();
		for (String key : httpHeaders.keySet()) {
			if (HEADERS_TO_KEEP.contains(key.toLowerCase())) {
				forwardedHeaders.add(key, httpHeaders.getFirst(key));
			}
		}
		return forwardedHeaders;
	}

	@Override
	protected String getLinkUrl(String service, String rel, Map<String, Object> params) {
		return super.getLinkUrl(service, rel, params, filterHeaders(getCurrentHeaders()));
	}

	@Override
	protected String getLinkUrl(String service, String rel) {
		return super.getLinkUrl(service, rel, Collections.emptyMap(), filterHeaders(getCurrentHeaders()));
	}

	@Override
	protected String getLinkUrl(String service, String rel, Map<String, Object> params, HttpHeaders headers) {
		headers.putAll(filterHeaders(getCurrentHeaders()).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		return super.getLinkUrl(service, rel, params, headers);
	}
}
