package com.ea.campus.ms.student.fetch;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;

public abstract class AbstractRestClientService {

	protected abstract String microservicePropertyName();

	public abstract HttpHeaders getCurrentHeaders();

	protected String getLinkUrl(String service, String rel, Map<String, Object> params) {
		return null;
	}

	protected String getLinkUrl(String service, String rel) {
		return null;
	}

	protected String getLinkUrl(String service, String rel, Map<String, Object> params, HttpHeaders headers) {
		return null;
	}

	protected PaymentTypeDTO get(String url, Class clazz) {
		return null;
	}

}
