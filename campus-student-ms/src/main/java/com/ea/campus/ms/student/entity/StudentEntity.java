package com.ea.campus.ms.student.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
public class StudentEntity {

	@Id
	@GeneratedValue
	private String id;
	private String name;
	private String surname;
	private String jobTitle;

	public StudentEntity() {
	}

	public StudentEntity(String id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public StudentEntity(String name, String surname) {
		this(null, name, surname);
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
				.append("id", id)
				.append("name", name)
				.append("surname", surname)
				.append("jobTitle", jobTitle)
				.toString();
	}
}
