package com.ea.campus.ms.payment.assembler;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.resource.PaymentStudentResource;

@Component
public class PaymentStudentAssembler extends ResourceAssemblerSupport<PaymentStudentEntity, PaymentStudentResource> {

	public PaymentStudentAssembler() {
		super(PaymentStudentEntity.class, PaymentStudentResource.class);
	}

	public PaymentStudentResource toResource(PaymentStudentEntity entity) {
		PaymentStudentResource resource = new PaymentStudentResource();
		resource.setName(entity.getId());
		resource.setPaymentType(entity.getPaymentType().name());
		return resource;
	}

	public PaymentStudentEntity toEntity(PaymentStudentResource resource) {
		PaymentStudentEntity entity = new PaymentStudentEntity();
		entity.setId(resource.getName());
		entity.setPaymentType(PaymentType.fromName(resource.getPaymentType()));
		return entity;
	}

}
