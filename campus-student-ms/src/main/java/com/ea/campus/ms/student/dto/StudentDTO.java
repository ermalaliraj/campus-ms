package com.ea.campus.ms.student.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class StudentDTO implements Serializable {

	private static final long serialVersionUID = -2399911255673089404L;
	private String id;
	private String name;
	private String surname;
	private String jobTitle;
	private PaymentType paymentType;

	public StudentDTO() {
	}
	public StudentDTO(String id, String name, String surname, String jobTitle, PaymentType paymentType) {	
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.paymentType = paymentType;
//		if(paymentType == null) {
//			this.paymentType = PaymentType.NEUTRAL;
//		} else {
//			this.paymentType = paymentType;
//		}
	}
	public StudentDTO(String id, String name, String surname, String jobTitle) {
		this(null, name, surname, jobTitle, PaymentType.NEUTRAL);
	}
	
	public StudentDTO(String name, String surname, PaymentType paymentType) {
		this(null, name, surname, null, paymentType);
	}
	public StudentDTO(String name, String surname) {
		this(null, name, surname, null, PaymentType.NEUTRAL);
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
	
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
				.append("id", id)
				.append("name", name)
				.append("surname", surname)
				.append("jobTitle", jobTitle)
				.append("paymentType", paymentType)
				.toString();
	}
}
