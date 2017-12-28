package com.ea.campus.ms.payment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.repository.PaymentRepository;

@Component
public class DataLoader implements ApplicationRunner {

	private static final transient Logger log = LoggerFactory.getLogger(DataLoader.class);
	private PaymentRepository paymentRepository;

	@Autowired
	public DataLoader(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	public void run(ApplicationArguments args) {
		log.debug("*** Populating DB with test data ***");
		//populateDB();
		log.debug("*** End population DB with test data ***");
	}

	private void populateDB() {		
		PaymentStudentEntity e = new PaymentStudentEntity("ermal", PaymentType.OK);
		paymentRepository.save(e);
		log.debug("Inserted test data: " + e);
		
		e = new PaymentStudentEntity("ermal_notok", PaymentType.NOTOK);
		paymentRepository.save(e);
		log.debug("Inserted test data: " + e);		
	}
}