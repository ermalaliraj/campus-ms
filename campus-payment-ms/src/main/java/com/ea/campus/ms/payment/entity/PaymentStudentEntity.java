package com.ea.campus.ms.payment.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
public class PaymentStudentEntity {
	
	@Id
	private String id;
	private PaymentType paymentType;
	
	public PaymentStudentEntity() {
	}
	public PaymentStudentEntity(String id, PaymentType paymentType) {	
		this.id = id;
		this.paymentType = paymentType;
	}
	public PaymentStudentEntity(String id) {
		this(id,  PaymentType.NEUTRAL);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	public boolean equals(final Object other) {
		if (!(other instanceof PaymentStudentEntity))
			return false;
		PaymentStudentEntity o = (PaymentStudentEntity) other;

		return new EqualsBuilder()
				.append(id, o.id)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        	.appendSuper(super.toString())
        	.append("id", id)
        	.append("paymentType", paymentType)
        	.toString();
    }
}

