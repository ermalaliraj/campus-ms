package com.ea.campus.ms.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
public class PaymentStudentEntity {
	
	@Id
	@GeneratedValue
	private String id;
	private String name;
	private PaymentType paymentType;
	
	public PaymentStudentEntity() {
	}
	public PaymentStudentEntity(String id, String name, PaymentType paymentType) {	
		this.id = id;
		this.name = name;
		this.paymentType = paymentType;
	}
	public PaymentStudentEntity(String name, PaymentType paymentType) {
		this(null, name, paymentType);
	}
	public PaymentStudentEntity(String name) {
		this(null, name, PaymentType.NEUTRAL);
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
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        	.appendSuper(super.toString())
        	//.append("id", id)
        	.append("name", name)
        	.append("paymentType", paymentType)
        	.toString();
    }
}

