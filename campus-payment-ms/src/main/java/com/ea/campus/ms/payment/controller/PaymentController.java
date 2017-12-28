package com.ea.campus.ms.payment.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@RequestMapping("/ping")
	public String ping() {
		return "Date: " + new Date();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public List<PaymentStudentEntity> getAllTopics() {
		List<PaymentStudentEntity> paymentStudents = paymentService.getAllPaymentStudents();
		log.info("Tot paymentStudents found in DB: " + paymentStudents.size());
		return paymentStudents;
	}

	@RequestMapping("/student/{id}")
	public PaymentStudentEntity getPaymentForStudent(@PathVariable String id) {
		PaymentStudentEntity ret = paymentService.getPaymentStudent(id);
		if (ret == null) {
			ret = new PaymentStudentEntity(id, PaymentType.NEUTRAL);
		}
		return ret;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/student")
	public void addPaymentForStudent(@RequestBody PaymentStudentEntity paymentStudentEntity) {
		paymentService.addPaymentStudent(paymentStudentEntity);
		log.info("Saved in DB: " + paymentStudentEntity);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/student/{id}")
	public void deletePaymentStudent(@PathVariable String id) {
		paymentService.deletePaymentStudent(id);
		log.info("Deleted from DB topic with id: " + id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/student/deleteall")
	public void deleteAllPaymentStudents() {
		paymentService.deleteAll();
		log.info("Deleted ALL PaymentStudents from DB");
	}

}
