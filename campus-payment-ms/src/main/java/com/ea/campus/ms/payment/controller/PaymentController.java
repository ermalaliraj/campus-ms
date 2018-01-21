package com.ea.campus.ms.payment.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.payment.assembler.PaymentStudentAssembler;
import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.resource.PaymentStudentResource;
import com.ea.campus.ms.payment.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@Autowired
	PaymentStudentAssembler paymentStudentAssembler;
	
	@RequestMapping("/ping")
	public String ping() {
		return "Date: " + new Date();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/students")
	public ResponseEntity<List<PaymentStudentEntity>> getAllPaymentsStudents() {
		List<PaymentStudentEntity> paymentsStudents = paymentService.getAllPaymentsStudents();
		log.info("Tot paymentsStudents found in DB: " + paymentsStudents.size());
		return ResponseEntity.ok().body(paymentsStudents);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<PaymentStudentEntity> getPaymentForStudent(@RequestParam(value = "id") String id) {
		PaymentStudentEntity ret = paymentService.getPaymentStudent(id);
		log.info("For id: '" + id + "', found in DB: " + ret);
		return ResponseEntity.ok().body(ret);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/student")
	public void addPaymentForStudent(@RequestBody PaymentStudentResource paymentStudentResource) {
		PaymentStudentEntity paymentStudentEntity = paymentStudentAssembler.toEntity(paymentStudentResource);
		paymentService.addPaymentStudent(paymentStudentEntity);
		log.info("Saved in DB: " + paymentStudentEntity);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/student/{id}")
	public ResponseEntity<String> deletePaymentStudent(@PathVariable String id) {
		paymentService.deletePaymentStudent(id);
		log.info("Deleted from DB topic with id: " + id);
		return ResponseEntity.ok().body(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/student/deleteall")
	public void deleteAllPaymentsStudents() {
		paymentService.deleteAll();
		log.info("Deleted ALL PaymentStudents from DB");
	}

}
