package com.ea.campus.ms.student.dto.external;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.ea.campus.ms.student.dto.PaymentType;

public class PaymentTypeDTO implements Serializable {

	private static final long serialVersionUID = -3800589901872116196L;
	
	private String id;
	private PaymentType paymentType;

	public PaymentTypeDTO() {
		this.paymentType = PaymentType.NEUTRAL;
	}
	public PaymentTypeDTO(PaymentType paymentType) {
		this.paymentType = paymentType;
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

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString())
				.append("paymentType", paymentType)
				.toString();
	}
}
