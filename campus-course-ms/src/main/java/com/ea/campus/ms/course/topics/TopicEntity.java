package com.ea.campus.ms.course.topics;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.ea.campus.ms.course.courses.CourseEntity;

@Entity
@Table(name = "TOPIC")
public class TopicEntity implements Serializable {

	private static final long serialVersionUID = 7773225503124016766L;

	@Id
	@Column(name = "TOPIC_ID")
	private String id;
	@Column(name = "TOPIC_NAME")
	private String name;
	@Column(name = "TOPIC_DESC")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COURSE_ID")
	private CourseEntity course;

	public TopicEntity() {
	}

	public TopicEntity(String id, String name, String description, CourseEntity course) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.course = course;
	}

	public TopicEntity(String id, String name, String description) {
		this(id, name, description, null);
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

	public CourseEntity getCourse() {
		return course;
	}

	public void setCourse(CourseEntity course) {
		this.course = course;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof TopicEntity))
			return false;
		TopicEntity o = (TopicEntity) other;

		return new EqualsBuilder().append(id, o.id).append(name, o.name).append(description, o.description).append(course, o.course).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).append(name).append(description).append(course).toHashCode();
	}

	public TopicEntity clone() {
		TopicEntity clone = new TopicEntity(id, name, description, course);
		return clone;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("id", id).append("name", name)
				.append("description", description).append("course", course).toString();
	}
}
