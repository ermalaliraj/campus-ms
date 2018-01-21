package com.ea.campus.ms.student.fetch;

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
import org.springframework.core.ParameterizedTypeReference;
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

import com.ea.campus.ms.student.context.MSLogger;
import com.ea.campus.ms.student.discovery.TraversonUtil;
import com.ea.campus.ms.student.exception.ErrorResource;
import com.ea.campus.ms.student.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

@Service
public abstract class AbstractRestClientService {
	
	protected static boolean mustDecodeURLs = mustDecodeURL();
	protected MSLogger logger;
	protected RestTemplate restTemplate = new RestTemplate(getMessageConverters());
	protected ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	@Autowired
	protected TraversonUtil traversonUtil;
	@Value("${restClient.maxJsonLogSize:5000}")
	int maxJsonLogSize;
	@Value("${restClient.requestLog.ms:10000}")
	int requestLogThreshold;

	public AbstractRestClientService() {
	}

	@PostConstruct
	public void init() {
		logger = new MSLogger(getClass(), maxJsonLogSize);
		// StubbingPropagateInterceptor interceptor = new StubbingPropagateInterceptor();
		// interceptor.setAbstractRestClientService(this);
		// restTemplate.setInterceptors(Collections.singletonList(interceptor));
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

	protected ResponseEntity<Void> headEntity(String url, Object... parameters) {
		return executeEntity(url, HttpMethod.HEAD, (HttpHeaders) null, (Object) null, Void.class, parameters);
	}

	protected ResponseEntity<Void> headEntity(String url, HttpHeaders headers, Object... parameters) {
		return executeEntity(url, HttpMethod.HEAD, headers, (Object) null, Void.class, parameters);
	}

	protected <T> T get(String url, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, responseType, parameters);
	}

	protected <T> T get(String url, HttpHeaders headers, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.GET, headers, (Object) null, responseType, parameters);
	}

	protected <T> ResponseEntity<T> getEntity(String url, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, responseType, parameters);
	}

	protected <T> ResponseEntity<T> getEntity(String url, HttpHeaders headers, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.GET, headers, (Object) null, responseType, parameters);
	}

	protected <T> T post(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.POST, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T post(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.POST, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> postEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.POST, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> postEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.POST, headers, payload, responseType, parameters);
	}

	protected <T> T put(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PUT, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T put(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PUT, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> putEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PUT, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> putEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PUT, headers, payload, responseType, parameters);
	}

	protected <T> T patch(String url, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PATCH, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> T patch(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return execute(url, HttpMethod.PATCH, headers, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> patchEntity(String url, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PATCH, (HttpHeaders) null, payload, responseType, parameters);
	}

	protected <T> ResponseEntity<T> patchEntity(String url, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntity(url, HttpMethod.PATCH, headers, payload, responseType, parameters);
	}

	protected <T> T getGeneric(String url, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, parameterizedTypeReference, parameters);
	}

	protected <T> T getGeneric(String url, HttpHeaders headers, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.GET, headers, (Object) null, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> getEntityGeneric(String url, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.GET, (HttpHeaders) null, (Object) null, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> getEntityGeneric(String url, HttpHeaders headers, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.GET, headers, (Object) null, parameterizedTypeReference, parameters);
	}

	protected <T> T postGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.POST, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> T postGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.POST, headers, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> postEntityGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.POST, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> postEntityGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.POST, headers, payload, parameterizedTypeReference, parameters);
	}

	protected <T> T putGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.PUT, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> T putGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.PUT, headers, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> putEntityGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.PUT, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> putEntityGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.PUT, headers, payload, parameterizedTypeReference, parameters);
	}

	protected <T> T patchGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.PATCH, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> T patchGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeGeneric(url, HttpMethod.PATCH, headers, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> patchEntityGeneric(String url, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.PATCH, (HttpHeaders) null, payload, parameterizedTypeReference, parameters);
	}

	protected <T> ResponseEntity<T> patchEntityGeneric(String url, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityGeneric(url, HttpMethod.PATCH, headers, payload, parameterizedTypeReference, parameters);
	}

	<T> T execute(String url, HttpMethod method, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		ResponseEntity<T> responseEntity = executeEntity(url, method, headers, payload, responseType, parameters);
		return responseEntity.getBody();
	}

	<T> ResponseEntity<T> executeEntity(String url, HttpMethod method, HttpHeaders headers, Object payload, Class<T> responseType, Object... parameters) {
		return executeEntityObject(url, method, headers, payload, responseType, parameters);
	}

	<T> T executeGeneric(String url, HttpMethod method, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		ResponseEntity<T> responseEntity = executeEntityGeneric(url, method, headers, payload, parameterizedTypeReference, parameters);
		return responseEntity.getBody();
	}

	<T> ResponseEntity<T> executeEntityGeneric(String url, HttpMethod method, HttpHeaders headers, Object payload, ParameterizedTypeReference parameterizedTypeReference, Object... parameters) {
		return executeEntityObject(url, method, headers, payload, parameterizedTypeReference, parameters);
	}

	<T> ResponseEntity<T> executeEntityObject(String url, HttpMethod method, HttpHeaders headers, Object payload, Object type, Object... parameters) {
		StopWatch watch = new StopWatch();
		watch.start();
		HttpEntity requestEntity = new HttpEntity(payload, headers);
		ResponseEntity responseEntity = null;

		try {
			if (type instanceof Class) {
				if (parameters != null && parameters.length > 0) {
					responseEntity = restTemplate.exchange(url, method, requestEntity, (Class) type, parameters);
				} else {
					responseEntity = restTemplate.exchange(url, method, requestEntity, (Class) type, new Object[0]);
				}
			} else {
				if (!(type instanceof ParameterizedTypeReference)) {
					throw new IllegalArgumentException("The object type is not the right type : " + type.getClass());
				}

				if (parameters != null && parameters.length > 0) {
					responseEntity = restTemplate.exchange(url, method, requestEntity, (ParameterizedTypeReference) type, parameters);
				} else {
					responseEntity = restTemplate.exchange(url, method, requestEntity, (ParameterizedTypeReference) type, new Object[0]);
				}
			}

			watch.stop();
			if (watch.getTime() > (long) requestLogThreshold) {
				logger.logMicroServiceCallInfo(url, method, convertObjectToJsonString(payload),
						responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(),
						responseEntity != null ? responseEntity.getStatusCode() : null, parameters);
			} else if (logger.isDebugEnabled()) {
				logger.logMicroServiceCallDebug(url, method, convertObjectToJsonString(payload),
						responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(),
						responseEntity != null ? responseEntity.getStatusCode() : null, parameters);
			}

			return responseEntity;
		} catch (HttpServerErrorException | HttpClientErrorException var11) {
			watch.stop();
			logger.logMicroServiceCallError(url, method, convertObjectToJsonString(payload),
					responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(), var11.getStatusCode(), parameters);
			throw var11;
		} catch (RestClientException var12) {
			watch.stop();
			logger.logMicroServiceCallError(url, method, convertObjectToJsonString(payload),
					responseEntity != null ? convertObjectToJsonString(responseEntity.getBody()) : null, watch.getTime(), (HttpStatus) null, parameters);
			throw var12;
		}
	}

	public ErrorResource extractErrorResource(RestClientException e) throws IOException {
		return e instanceof HttpClientErrorException
				? (ErrorResource) ObjectMapperUtils.getDefaultObjectMapper().readValue(((HttpClientErrorException) e).getResponseBodyAsString(), ErrorResource.class)
				: (ErrorResource) ObjectMapperUtils.getDefaultObjectMapper().readValue(((HttpServerErrorException) e).getResponseBodyAsString(), ErrorResource.class);
	}

	public Boolean checkService(String service) {
		ClientHttpResponse httpResponse = null;

		Boolean var8;
		try {
			Boolean var4;
			try {
				String url = getLinkUrl(service);
				if (url == null) {
					var4 = false;
					return var4;
				}

				url = url + "/health";
				httpResponse = httpExecute(HttpMethod.GET, url, (Map) null);
				String response = IOUtils.toString(httpResponse.getBody());
				Configuration configuration = Configuration.defaultConfiguration().mappingProvider(new JacksonMappingProvider());
				ReadContext context = JsonPath.parse(response, configuration);
				String status = (String) context.read("$.status.status", String.class, new Predicate[0]);
				var8 = status != null && status.equals("UP");
			} catch (URISyntaxException | IOException var12) {
				var4 = false;
				return var4;
			}
		} finally {
			if (httpResponse != null) {
				httpResponse.close();
			}
		}

		return var8;
	}

	protected ClientHttpResponse httpGet(String service, String rel, Map<String, Object> params, Map<String, String> headersMap) throws URISyntaxException, IOException {
		if (params == null) {
			params = new HashMap<>();
		}

		String url;
		if (CollectionUtils.isEmpty((Map) params)) {
			url = getLinkUrl(service, rel);
		} else {
			url = getLinkUrl(service, rel, (Map) params);
		}

		return httpExecute(HttpMethod.GET, url, headersMap);
	}

	ClientHttpResponse httpExecute(HttpMethod method, String url, Map<String, String> headersMap) throws URISyntaxException, IOException {
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
		return traversonUtil.getLinkUrl(service, rel, params, headers);
	}

	protected String getLinkUrl(String service, String rel, Map<String, Object> params) {
		return traversonUtil.getLinkUrl(service, rel, params);
	}

	protected String getLinkUrl(String service, String rel) {
		return traversonUtil.getLinkUrl(service, rel);
	}

	protected String getLinkUrl(String service) {
		return traversonUtil.getLinkUrl(service);
	}

	protected ObjectMapper createMapper() {
		return ObjectMapperUtils.getDefaultObjectMapper();
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
			return null;
		} else {
			ObjectMapper mapper = createMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				mapper.writeValue(out, object);
				return new String(out.toByteArray());
			} catch (IOException var5) {
				logger.error("Error when converting object to json.", var5);
				return null;
			}
		}
	}

	public void setTraversonUtil(TraversonUtil traversonUtil) {
		this.traversonUtil = traversonUtil;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
