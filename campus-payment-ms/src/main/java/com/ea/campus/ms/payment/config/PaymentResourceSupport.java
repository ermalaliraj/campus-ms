package com.ea.campus.ms.payment.config;

import org.springframework.hateoas.ResourceSupport;

public class PaymentResourceSupport extends ResourceSupport {

	private String serviceName;
	private String serviceDescription;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

}
