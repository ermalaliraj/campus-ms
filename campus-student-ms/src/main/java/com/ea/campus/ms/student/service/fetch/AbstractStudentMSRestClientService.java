package com.ea.campus.ms.student.service.fetch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;

import com.ea.campus.ms.restutil.context.ExecutionContextAccessor;
import com.ea.campus.ms.restutil.context.ServiceExecutionContext;
import com.ea.campus.ms.restutil.rest.AbstractRestClientService;

public abstract class AbstractStudentMSRestClientService extends AbstractRestClientService {

	private static final List<String> HEADERS_TO_KEEP = Arrays.asList("header1", "header2");
	
	private ServiceExecutionContext serviceExecutionContext = ExecutionContextAccessor.getExecutionContext();

	@Override
	protected String microservicePropertyName() {
		return "student-ms";
	}

	/**
	 * Get from the context the headers present.
	 */
	@Override
	public HttpHeaders getCurrentHeaders() {
		HttpHeaders currentHeaders = new HttpHeaders();
		Map<String, String> headers = serviceExecutionContext.getHeaders();
		for (Map.Entry<String, String> header : headers.entrySet()) {
			currentHeaders.add(header.getKey(), header.getValue());
		}
		return currentHeaders;
	}

	/**
	 * Filter the headers keeping only the ones present in HEADERS_TO_KEEP
	 * @return Filtered Headers
	 */
	public static HttpHeaders filterHeaders(HttpHeaders httpHeaders) {
		if (httpHeaders == null) {
			return null;
		}
		HttpHeaders filteredHeaders = new HttpHeaders();
		for (String key : httpHeaders.keySet()) {
			if (HEADERS_TO_KEEP.contains(key.toLowerCase())) {
				filteredHeaders.add(key, httpHeaders.getFirst(key));
			}
		}
		return filteredHeaders;
	}

	@Override
	protected String getLinkUrl(String service, String rel, Map<String, Object> params) {
		return super.getLinkUrl(service, rel, params, filterHeaders(getCurrentHeaders()));
	}

	@Override
	protected String getLinkUrl(String service, String rel) {
		return super.getLinkUrl(service, rel, filterHeaders(getCurrentHeaders()));
	}

	@Override
	protected String getLinkUrl(String service, String rel, Map<String, Object> params, HttpHeaders headers) {
		headers.putAll(filterHeaders(getCurrentHeaders()).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		return super.getLinkUrl(service, rel, params, headers);
	}
}
