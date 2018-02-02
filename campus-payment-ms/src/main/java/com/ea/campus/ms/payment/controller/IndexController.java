package com.ea.campus.ms.payment.controller;

import static org.springframework.hateoas.mvc.ParameterizedControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
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
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class IndexController {


	@RequestMapping(value = "/", method = RequestMethod.GET, produces = { "application/hal+json" })
	public HttpEntity<PaymentResourceSupport> getIndex() throws Exception {
		final PaymentResourceSupport resource = new PaymentResourceSupport();
		resource.setServiceName(Curie.SERVICE_NAME);
		resource.setServiceDescription(Curie.SERVICE_NAME);
		resource.setServiceDescription(Curie.SERVICE_DESC);
		
		resource.add(linkTo(methodOn(PaymentController.class).getPaymentForStudent(null)).withRel(Curie.GET_PAYMENT_FOR_STUDENT));		
		resource.add(linkTo(methodOn(PaymentController.class).getAllPaymentsStudents()).withRel(Curie.GET_ALL_PAYMENTS_STUDENT));
		//resource.add(linkTo(methodOn(PaymentController.class).deletePaymentStudent("")).withRel(Curie.GET_PAYMENT_FOR_STUDENT));
		
		String content = "{'_links' :  { 'foo' : { 'href' : '/foo/bar/{1}' }}}";
		LinkDiscoverer discoverer = new HalLinkDiscoverer();
		Link link = discoverer.findLinkWithRel("foo", content);
		resource.add(link);
		
		return ok().body(resource);
	}
	
}
