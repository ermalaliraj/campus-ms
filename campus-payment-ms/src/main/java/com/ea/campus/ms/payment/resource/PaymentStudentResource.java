package com.ea.campus.ms.payment.resource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentStudentResource extends ResourceSupport {
	
	@JsonProperty("name")
	private String name;
	@JsonProperty("paymentType")
	private String paymentType;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof PaymentStudentResource))
			return false;
		PaymentStudentResource o = (PaymentStudentResource) other;

		return new EqualsBuilder()
				.append(name, o.name)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.toHashCode();
	}

	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        	.appendSuper(super.toString())
        	.append("name", name)
        	.append("paymentType", paymentType)
        	.toString();
    }
}

