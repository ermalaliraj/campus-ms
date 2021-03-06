package com.ea.campus.ms.course.courses.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class CourseDTO implements Serializable {

	private static final long serialVersionUID = 1;

	private String id;
	private String name;
	private String description;
	private int maxSubscriptions;
	private int stillAvailableSubscriptions;

	public CourseDTO(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public CourseDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxSubscriptions() {
		return maxSubscriptions;
	}

	public void setMaxSubscriptions(int maxSubscriptions) {
		this.maxSubscriptions = maxSubscriptions;
	}

	public int getStillAvailableSubscriptions() {
		return stillAvailableSubscriptions;
	}

	public void setStillAvailableSubscriptions(int stillAvailableSubscriptions) {
		this.stillAvailableSubscriptions = stillAvailableSubscriptions;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof CourseDTO))
			return false;
		CourseDTO o = (CourseDTO) other;

		return new EqualsBuilder()
				.append(id, o.id)
				.append(name, o.name)
				.append(description, o.description)
				.append(maxSubscriptions, o.maxSubscriptions)
				.append(stillAvailableSubscriptions, o.stillAvailableSubscriptions)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(description)
				.append(maxSubscriptions)
				.append(stillAvailableSubscriptions)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
				.append("id", id)
				.append("name", name)
				.append("description", description)
				.append("maxSubscriptions", maxSubscriptions)
				.append("stillAvailableSubscriptions", stillAvailableSubscriptions)
				.toString();
	}

}
