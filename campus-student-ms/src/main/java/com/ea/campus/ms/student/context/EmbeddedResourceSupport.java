package com.ea.campus.ms.student.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedResourceSupport extends ResourceSupport {
	private Map<String, Object> embedded = new HashMap();

	public EmbeddedResourceSupport() {
	}

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("_embedded")
	@XmlElement(name = "_embedded", namespace = "http://www.w3.org/2005/Atom")
	public Map<String, Object> getEmbeddedResources() {
		return this.embedded;
	}

	public <T extends ResourceSupport> void put(String relationship, T resource) {
		Assert.notNull(relationship, "Relationship must not be null!");
		Assert.notNull(resource, "Resources must not be null!");
		this.embedded.put(relationship, resource);
	}

	public void put(String relationship, Set<? extends ResourceSupport> resources) {
		Assert.notNull(relationship, "Relationship must not be null!");
		Assert.notNull(resources, "Resources must not be null!");
		this.embedded.put(relationship, resources);
	}

	public boolean hasEmbeddedResources() {
		return !this.embedded.isEmpty();
	}

	public boolean hasEmbeddedResources(String relationship) {
		return this.getEmbeddedResourcesByRel(relationship) != null;
	}

	public void removeEmbeddedResources() {
		this.embedded.clear();
	}

	public ResourceSupport getEmbeddedResourceByRel(String rel) {
		return (ResourceSupport) this.embedded.get(rel);
	}

	public Set<ResourceSupport> getEmbeddedResourcesByRel(String rel) {
		return (Set) this.embedded.get(rel);
	}

	private void set_embedded(Map<String, Object> embedded) {
		this.embedded = embedded;
	}
}
