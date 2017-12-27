package com.ea.campus.ms.student.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.StudentDTO;
import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;
import com.ea.campus.ms.student.entity.StudentEntity;
import com.ea.campus.ms.student.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	private static final transient Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Autowired
	private StudentRepository paymentRepository;
	@Autowired
	private PaymentFetchService paymentFetchService;

	public StudentDTO getByName(String name) {
		StudentEntity entity = paymentRepository.findByName(name);
		log.info("For student '" + name + "' found: " + entity);
		StudentDTO dto = null;
		if (entity != null) {
			// check if is a good payer
			PaymentTypeDTO paymentTypeDTO = paymentFetchService.getPaymentForStudent(name);
			PaymentType paymentType = paymentTypeDTO.getPaymentType(); 
			dto = new StudentDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getJobTitle(), paymentType);
		}
		return dto;
	}

}
