package com.ea.campus.ms.payment.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.payment.constants.Curie;
import com.ea.campus.ms.payment.context.PaymentResourceSupport;

/**
 * Main {@link RestController}.
 * Hateoas curie generation
 */
@RestController
public class IndexController {

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = { "application/hal+json" })
	public HttpEntity<PaymentResourceSupport> getIndex() throws Exception {
		final PaymentResourceSupport resource = new PaymentResourceSupport();
		resource.setServiceName(Curie.SERVICE_NAME);
		resource.setServiceDescription(Curie.SERVICE_NAME);
		resource.setServiceDescription(Curie.SERVICE_DESC);
		
		Object endPoint = methodOn(PaymentController.class).getPaymentForStudent("");
		resource.add(linkTo(endPoint).withRel(Curie.GET_PAYMENT_FOR_STUDENT));		
		resource.add(linkTo(methodOn(PaymentController.class).getAllPaymentsStudents()).withRel(Curie.GET_ALL_PAYMENTS_STUDENT));
		//resource.add(linkTo(methodOn(PaymentController.class).deletePaymentStudent("")).withRel(Curie.GET_PAYMENT_FOR_STUDENT));
		
		return ok().body(resource);
	}
}
