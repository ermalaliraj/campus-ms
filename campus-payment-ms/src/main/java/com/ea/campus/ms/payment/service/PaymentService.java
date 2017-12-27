package com.ea.campus.ms.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	public PaymentStudentEntity getByName(String name) {
		return paymentRepository.findByName(name);
	}

}
