package com.ea.campus.ms.restutil.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.cxf.helpers.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ea.campus.ms.restutil.discovery.DiscoveryClientUtil;
import com.ea.campus.ms.restutil.exception.ErrorResource;
import com.ea.campus.ms.restutil.logger.MSLogger;
import com.ea.campus.ms.restutil.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public abstract class AbstractRestClientService {
	
	protected static boolean mustDecodeURLs = mustDecodeURL();
	protected MSLogger logger;
	protected RestTemplate restTemplate = new RestTemplate(getMessageConverters());
	protected ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	@Autowired
	protected DiscoveryClientUtil discoveryClientUtil;
	@Value("${restClient.maxJsonLogSize:5000}")
	int maxJsonLogSize;

	public AbstractRestClientService() {
	}

	@PostConstruct
	public void init() {
		logger = new MSLogger(getClass(), maxJsonLogSize);
	}

	private static boolean mustDecodeURL() {
		UriTemplate uriTemplate = new UriTemplate("http://localhost:8080/{param}");
		Map<String, String> params = new HashMap<>();
		params.put("param", " ");
		String encoded = uriTemplate.expand(params).toString();
		return encoded.contains("%2520");
	}

	protected abstract String microservicePropertyName();

	public abstract HttpHeaders getCurrentHeaders();

	protected <T> T get(String url, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, responseType, parameters);
	}

	protected <T> T get(String url, HttpHeaders headers, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.GET, headers, (Object) null, responseType, parameters);
	}

	protected <T> T post(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.POST, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T post(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.POST, headers, payload, responseType, parameters);
	}

	protected <T> T put(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PUT, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T put(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PUT, headers, payload, responseType, parameters);
	}

	protected <T> T patch(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PATCH, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T patch(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PATCH, headers, payload, responseType, parameters);
	}
	
	protected ResponseEntity<Void> headEntity(String url, Object... parameters) {
		return executeEntity(url, HttpMethod.HEAD, (HttpHeaders) null, (Object) null, Void.class, parameters);
	}

	protected ResponseEntity<Void> headEntity(String url, HttpHeaders headers, Object... parameters) {
		return executeEntity(url, HttpMethod.HEAD, headers, (Object) null, Void.class, parameters);
	}	
	
	protected <T> ResponseEntity<T> getEntity(String url, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, responseType, parameters);
	}

	protected <T> ResponseEntity<T> getEntity(String url, HttpHeaders headers, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.GET, headers, (Object) null, responseType, parameters);
	}
	
	protected <T> ResponseEntity<T> postEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.POST, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> postEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.POST, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> putEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PUT, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> putEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PUT, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> patchEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PATCH, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> patchEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PATCH, headers, payload, responseType, parameters);
	}

	protected <T> T execute(String url, HttpMethod method, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		ResponseEntity<T> responseEntity = executeEntity(url, method, headers, payload, responseType, parameters);
		return responseEntity.getBody();
	}
	
	protected <T> ResponseEntity<T> executeEntity(String url, HttpMethod method, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntityLogic(url, method, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> executeEntityLogic(String url, HttpMethod method, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		StopWatch watch = new StopWatch();
		watch.start();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(payload, headers);
		ResponseEntity<T> responseEntity = null;

		try {
			if (parameters != null && parameters.length > 0) {
				responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, parameters);
			} else {
				responseEntity = restTemplate.exchange(url, method, requestEntity, responseType, new Object[0]);
			}

			watch.stop();
			if (logger.isDebugEnabled()) {
				logger.logMicroServiceCallDebug(url, method, convertObjectToJsonString(payload)
						, responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : ""
						, watch.getTime()
						, responseEntity != null ? responseEntity.getStatusCode() : null, parameters);
			}
			return responseEntity;
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			watch.stop();
			logger.logMicroServiceCallError(url, method, convertObjectToJsonString(payload), responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(), e.getStatusCode(), parameters);
			throw e;
		} catch (RestClientException e) {
			watch.stop();
			logger.logMicroServiceCallError(url, method, convertObjectToJsonString(payload), responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(), (HttpStatus) null, parameters);
			throw e;
		}
	}
	
	protected ClientHttpResponse httpGet(String service, String rel, Map<String, Object> params, Map<String, String> headersMap) throws URISyntaxException, IOException {
		if (params == null) {
			params = new HashMap<>();
		}
		String url;
		if (CollectionUtils.isEmpty((Map<String, Object>) params)) {
			url = getLinkUrl(service, rel);
		} else {
			url = getLinkUrl(service, rel, (Map<String, Object>) params);
		}
		return httpExecute(HttpMethod.GET, url, headersMap);
	}

	private ClientHttpResponse httpExecute(HttpMethod method, String url, Map<String, String> headersMap) throws URISyntaxException, IOException {
		DateTime dateTime = new DateTime();
		URI uri = new URI(url);
		AbstractClientHttpRequest httpRequest = (AbstractClientHttpRequest) requestFactory.createRequest(uri, method);
		if (headersMap != null && !headersMap.isEmpty()) {
			HttpHeaders headers = httpRequest.getHeaders();
			headers.setAll(headersMap);
		}

		ClientHttpResponse response = httpRequest.execute();
		long time = (new DateTime()).getMillis() - dateTime.getMillis();
		if (response.getStatusCode().is2xxSuccessful()) {
			logger.logMicroServiceCallDebug(url, method, (String) null, (String) null, time, response.getStatusCode(), new Object[0]);
		} else {
			String outboundMessage = null;
			if (response.getBody() != null) {
				outboundMessage = IOUtils.toString(response.getBody());
			}

			logger.logMicroServiceCallError(url, method, (String) null, outboundMessage, time, response.getStatusCode(), new Object[0]);
		}

		return response;
	}

	protected String getLinkUrl(String service, String rel, Map<String, Object> params, HttpHeaders headers) {
		return discoveryClientUtil.getLinkUrl(service, rel, params, headers);
	}

	protected String getLinkUrl(String service, String rel, Map<String, Object> params) {
		return discoveryClientUtil.getLinkUrl(service, rel, params);
	}
	
	protected String getLinkUrl(String service, String rel, HttpHeaders filterHeaders) {
		return discoveryClientUtil.getLinkUrl(service, rel, filterHeaders);
	}

	protected String getLinkUrl(String service, String rel) {
		return discoveryClientUtil.getLinkUrl(service, rel);
	}

	protected String getLinkUrl(String service) {
		return discoveryClientUtil.getLinkUrl(service);
	}

	protected ObjectMapper createMapper() {
		return ObjectMapperUtil.getDefaultObjectMapper();
	}

	protected List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(createMapper());
		converters.add(converter);
		converters.add(new Jaxb2RootElementHttpMessageConverter());
		return converters;
	}

	private String convertObjectToJsonString(Object object) {
		if (object == null) {
			return "{}";
		} else {
			ObjectMapper mapper = createMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				mapper.writeValue(out, object);
				return new String(out.toByteArray());
			} catch (IOException e) {
				logger.error("Error when converting object to json.", e);
				return null;
			}
		}
	}
	
	public ErrorResource extractErrorResource(RestClientException e) throws IOException {
		return e instanceof HttpClientErrorException
				? ObjectMapperUtil.getDefaultObjectMapper().readValue(((HttpClientErrorException) e).getResponseBodyAsString(), ErrorResource.class)
				: ObjectMapperUtil.getDefaultObjectMapper().readValue(((HttpServerErrorException) e).getResponseBodyAsString(), ErrorResource.class);
	}

	public DiscoveryClientUtil getDiscoveryClientUtil() {
		return discoveryClientUtil;
	}

	public void setDiscoveryClientUtil(DiscoveryClientUtil discoveryClientUtil) {
		this.discoveryClientUtil = discoveryClientUtil;
	}
	
}
