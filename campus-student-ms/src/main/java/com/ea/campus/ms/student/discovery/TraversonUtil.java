package com.ea.campus.ms.student.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.ea.campus.ms.student.exception.TechnicalException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class TraversonUtil extends DiscoveryClientUtil {

	private static final Logger log = LoggerFactory.getLogger(TraversonUtil.class);
	@Value("${traverson.cache.enabled:true}")
	Boolean cacheEnabled;
	@Value("${traverson.cache.timeout:600000}") // 10 minutes
	int cacheTimeout;
	private Cache cache;

	public Link getLink(String url, String rel) {
		return getLink(url, rel, null);
	}

	public Link getLink(String url, String rel, Map<String, String> params) {
		Link link = getTraversonLink(URI.create(url), rel);
		if (params != null) {
			link = link.expand(params);
		}
		return link;
	}

	public Link getLinkFromEureka(String serviceId, String rel) {
		return getLinkFromEureka(serviceId, rel, null);
	}

	public Link getLinkFromEureka(String serviceId, String rel, Map<String, ? extends Object> arguments) {
		Link link = getTraversonLink(getServiceUrl(serviceId), rel);
		if (arguments != null) {
			link = link.expand(arguments);
		}
		return link;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Link getTraversonLink(final URI url, final String rel) {
		try {
			return (Link) getCache().get(url.toString() + "|" + rel, new Callable() {
				public Object call() throws URISyntaxException {
					return TraversonUtil.this.createTraversonObject(url).follow(new String[] { rel }).asTemplatedLink();
				}
			});
		} catch (ExecutionException e) {
			if ((e.getCause() instanceof RuntimeException)) {
				throw ((RuntimeException) e.getCause());
			}
			throw new RuntimeException(e.getCause());
		}
	}

	public Traverson createTraversonObject(URI uri) {
		return new Traverson(uri, new MediaType[] { MediaTypes.HAL_JSON });
	}

	/**
	 * Cache the calls to Traverson
	 */
	private Cache getCache() {
		if (cache == null) {
			createCache();
		}
		return cache;
	}

	private void createCache() {
		if (cacheEnabled.booleanValue()) {
			log.info("Traverson link caching is enabled");
			cache = CacheBuilder.newBuilder().expireAfterWrite(cacheTimeout, TimeUnit.MILLISECONDS).maximumSize(1000L).build();
		} else {
			log.info("Traverson link caching is disabled");
			cache = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.MICROSECONDS).maximumSize(0L).build();
		}
	}
}
