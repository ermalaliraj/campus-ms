package com.ea.campus.ms.payment.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.service.PaymentService;

@RestController
public class PaymentController {
	
	@Autowired
	private PaymentService studentService;

	@RequestMapping("/ping")
	public String getPaymentForStudent() {
		return "Date: "+new Date();
	}
	
	@RequestMapping("/payment/student/{name}")
	public PaymentStudentEntity getPaymentForStudent(@PathVariable String name) {
		PaymentStudentEntity ret = studentService.getByName(name);
		if(ret == null){
			ret = new PaymentStudentEntity();
			ret.setName(name);
			ret.setPaymentType(PaymentType.NEUTRAL);
		}
		return ret; 
	}

}
