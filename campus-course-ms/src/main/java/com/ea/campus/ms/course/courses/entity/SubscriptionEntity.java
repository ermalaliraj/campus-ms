package com.ea.campus.ms.course.courses.entity;

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

@Entity
@Table(name = "SUBSCRIPTION")
public class SubscriptionEntity {

	@Id
	@Column(name = "STUDENT_NAME")
	private String studentName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COURSE_ID")
	private CourseEntity course;
	
	public SubscriptionEntity() {
	}

	public SubscriptionEntity(String studentName) {
		this.studentName = studentName;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof SubscriptionEntity))
			return false;
		SubscriptionEntity o = (SubscriptionEntity) other;

		return new EqualsBuilder()
				.append(studentName, o.studentName)
				.append(course, o.course)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(studentName)
				.append(course)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
				.append("course", studentName)
				.append("course", course)
				.toString();
	}

}
