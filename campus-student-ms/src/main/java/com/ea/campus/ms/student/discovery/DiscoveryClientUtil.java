package com.ea.campus.ms.student.discovery;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.client.Traverson.TraversalBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscoveryClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(DiscoveryClientUtil.class);

	@Autowired
	private LoadBalancerClient loadBalancer;
	@Autowired
	private Environment env;
	private RestTemplate restTemplate;

	@Value("${discovery.connectTimeout:1000}")
	private int connectTimeout;
	@Value("${discovery.readTimeout:5000}")
	private int readTimeout;

	public DiscoveryClientUtil() {
	}

	@PostConstruct
	public void postConstruct() {
		SimpleClientHttpRequestFactory httpFactory = new SimpleClientHttpRequestFactory();
		httpFactory.setConnectTimeout(connectTimeout);
		httpFactory.setReadTimeout(readTimeout);
		restTemplate = new RestTemplate(httpFactory);
		restTemplate.setMessageConverters(Traverson.getDefaultMessageConverters(new MediaType[] { MediaTypes.HAL_JSON }));
	}

	public String getLinkUrl(String serviceId) {
		return getLinkUrl(serviceId, null);
	}

	public String getLinkUrl(String serviceId, String rel) {
		return getLinkUrl(serviceId, rel, new HashMap<String, Object>());
	}
	
	public String getLinkUrl(String serviceId, String rel, Map<String, Object> params) {
		return getLinkUrl(serviceId, rel, params, null);
	}
	
	public String getLinkUrl(String service, String rel, HttpHeaders filterHeaders) {
		return getLinkUrl(service, rel, Collections.emptyMap(), filterHeaders);
	}

	public String getLinkUrl(String serviceId, String rel, Map<String, Object> params, HttpHeaders headers) {
		logger.debug("Get link URL for serviceId: " + serviceId + ", rel: " + rel + ", params: " + params + ", headers: " + headers + ".");
		String url = getDirectUrlIfPresent(serviceId);
		if (url != null) {
			if (rel != null) {
				logger.debug("Using Traverson with configured URL: " + url + ".");
				return fixTripleEncoding(getLink(getTraversonFromURL(url), rel, params, (HttpHeaders) headers).getHref());
			} else {
				logger.debug("Returning configured URL: " + url + ".");
				return fixTripleEncoding(url);
			}
		} else {
			if (rel != null) {
				logger.debug("Using Traverson with URL fetched from Eureka.");
				return fixTripleEncoding(getLink(getTraverson(serviceId), rel, params, (HttpHeaders) headers).getHref());
			} else {
				logger.debug("Getting URL from Eureka.");
				return fixTripleEncoding(getServiceUrl(serviceId).toString());
			}
		}
	}

	private String getDirectUrlIfPresent(String serviceId) {
		return env.getProperty("rest." + serviceId + ".url");
	}
	
	private String fixTripleEncoding(String url) {
		if (url == null) {
			return null;
		} else {
			try {
				return URLDecoder.decode(URLDecoder.decode(url, "UTF-8"), "UTF-8");
			} catch (UnsupportedEncodingException var3) {
				logger.warn("URL decoding failed. Restoring the string!");
				return url.replaceAll("%252520", " ").replaceAll("%2520", " ").replaceAll("%20", " ");
			}
		}
	}
	
	private Link getLink(Traverson traverson, String rel, Map<String, ? extends Object> arguments, HttpHeaders headers) {
		TraversalBuilder tb = traverson.follow(new String[] { rel });
		if (headers != null) {
			tb.withHeaders(getForwardedHeaders(headers));
		}
		return tb.asTemplatedLink().expand(arguments);
	}
	
	public Traverson getTraverson(String serviceId) {
		return getTraversonFromURI(getServiceUrl(serviceId));
	}
	
	public Traverson getTraversonFromURI(URI uri) {
		Traverson traverson = new Traverson(uri, new MediaType[] { MediaTypes.HAL_JSON });
		traverson.setRestOperations(restTemplate);
		return traverson;
	}	
	
	public URI getServiceUrl(String serviceId) {
		ServiceInstance instance = loadBalancer.choose(serviceId);
		if (instance == null) {
			throw new ServerInstanceNotFoundException(serviceId, "Loadbalancer could not find a server instance for service id '" + serviceId + "'.");
		} else {
			return createServiceUri(instance.getHost(), instance.getPort(), serviceId);
		}
	}

	private URI createServiceUri(String host, int port, String serviceId) {
		return URI.create(String.format("http://%s:%s/%s", host, port, serviceId.toLowerCase()));
	}

	public Traverson getTraversonFromURL(String url) {
		try {
			URI uri = new URI(url);
			return getTraversonFromURI(uri);
		} catch (URISyntaxException var4) {
			throw new RuntimeException(var4);
		}
	}

	private HttpHeaders getForwardedHeaders(HttpHeaders headers) {
		HttpHeaders forwardedHeaders = new HttpHeaders();
		Set<String> headersSet = headers.keySet();
		headersSet.stream().forEach((key) -> {
			if (key.toLowerCase().startsWith("x-forwarded") || ConstantsUtils.getSecurityHeaders().contains(key)) {
				forwardedHeaders.add(key, headers.getFirst(key));
			}
		});
		return forwardedHeaders;
	}
}
