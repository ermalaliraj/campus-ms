package com.ea.campus.ms.student.service;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<StudentEntity> getAllStudents() {
		List<StudentEntity> students = new ArrayList<>();
		paymentRepository.findAll().forEach(students::add);
		//for each check the paymentType
		return students;
	}

	@Override
	public StudentEntity getStudent(String id) {
		StudentEntity entity = paymentRepository.findOne(id);
		return entity;
	}
	
	@Override
	public StudentDTO getStudentDTOWithPayment(String id) {
		StudentEntity entity = paymentRepository.findOne(id);
		StudentDTO dto = null;
		if (entity != null) {
			// check if is a good payer
			log.debug("Check if is a good payer.");
			PaymentTypeDTO paymentTypeDTO = paymentFetchService.getPaymentForStudent(id);
			PaymentType paymentType = PaymentType.NEUTRAL;
			if(paymentTypeDTO != null){
				paymentType = paymentTypeDTO.getPaymentType();	
			}
			dto = new StudentDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getJobTitle(), paymentType);
		}
		return dto;
	}

	@Override
	public void addStudent(StudentEntity student) {
		paymentRepository.save(student);
	}

	@Override
	public void updateStudent(StudentEntity student) {
		paymentRepository.save(student);
	}

	@Override
	public void deleteStudent(String id) {
		paymentRepository.delete(id);
	}

	@Override
	public void deleteAll() {
		paymentRepository.deleteAll();
	}

}
