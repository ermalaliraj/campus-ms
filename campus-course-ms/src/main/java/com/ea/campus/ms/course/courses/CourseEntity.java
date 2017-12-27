package com.ea.campus.ms.course.courses;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "COURSE")
public class CourseEntity implements Serializable {

	private static final long serialVersionUID = 4907661782394670944L;

	@Id
	@Column(name = "COURSE_ID")
	private String id;
	@Column(name = "COURSE_NAME")
	private String name;
	@Column(name = "COURSE_DESC")
	private String description;
	@Column(name = "MAX_SUBSCRIPTIONS")
	private int maxSubscriptions;
	@Column(name = "AVAILABLE_SUBSCRIPTIONS")
	private int stillAvailableSubscriptions;

	public CourseEntity() {
	}

	public CourseEntity(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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
		if (!(other instanceof CourseEntity))
			return false;
		CourseEntity o = (CourseEntity) other;

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
