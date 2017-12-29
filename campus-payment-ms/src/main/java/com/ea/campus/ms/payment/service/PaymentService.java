package com.ea.campus.ms.payment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	public PaymentRepository paymentRepository;

	public PaymentStudentEntity getPaymentStudent(String id) {
		return paymentRepository.findOne(id);
	}

	public void addPaymentStudent(PaymentStudentEntity paymentStudent) {
		paymentRepository.save(paymentStudent);
	}

	public List<PaymentStudentEntity> getAllPaymentsStudents() {
		List<PaymentStudentEntity> paymentStudents = new ArrayList<>();
		paymentRepository.findAll().forEach(paymentStudents::add);
		return paymentStudents;
	}

	public void deletePaymentStudent(String id) {
		paymentRepository.delete(id);
	}
	
	public void deleteAll() {
		paymentRepository.deleteAll();
	}

}
