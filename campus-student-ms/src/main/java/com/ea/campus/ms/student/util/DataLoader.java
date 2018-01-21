package com.ea.campus.ms.student.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ea.campus.ms.student.entity.StudentEntity;
import com.ea.campus.ms.student.repository.StudentRepository;

@Component
public class DataLoader implements ApplicationRunner {

	private static final transient Logger log = LoggerFactory.getLogger(DataLoader.class);
	private StudentRepository paymentRepository;

	@Autowired
	public DataLoader(StudentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	public void run(ApplicationArguments args) {
		log.debug("*** Populating DB with test data ***");
		populateDB();
		log.debug("*** End population DB with test data ***");
	}

	private void populateDB() {		
		StudentEntity e = new StudentEntity("ermal", "ermal", "");
		paymentRepository.save(e);
		log.debug("Inserted test data: " + e);
		
		e = new StudentEntity("ermal_notok", "ermal_notok", "");
		paymentRepository.save(e);
		log.debug("Inserted test data: " + e);
		
		e = new StudentEntity("ermal_neutral", "ermal_neutral", ""); //not present in /payment
		paymentRepository.save(e);
		log.debug("Inserted test data: " + e);
	}
}