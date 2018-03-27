package com.ea.campus.ms.student.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.StudentDTO;
import com.ea.campus.ms.student.dto.external.CurieExternal;
import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;
import com.ea.campus.ms.student.entity.StudentEntity;
import com.ea.campus.ms.student.repository.StudentRepository;
import com.ea.campus.ms.student.service.fetch.PaymentFetchService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
		// for each check the paymentType
		return students;
	}

	@Override
	public StudentEntity getStudent(String id) {
		StudentEntity entity = paymentRepository.findOne(id);
		return entity;
	}

	@HystrixCommand(groupKey = CurieExternal.PAYMENT_SERVICE, fallbackMethod = "getPaymentForStudentFallback")
	@Override
	public StudentDTO getStudentDTOWithPayment(String id) {
		StudentEntity entity = paymentRepository.findOne(id);
		StudentDTO dto = null;
		if (entity != null) {
			log.debug("Check if is a good payer from the external service");
			PaymentTypeDTO paymentTypeDTO = paymentFetchService.getPaymentForStudent(id);
			log.debug("External service payment-ms for student " + id + " replied " + paymentTypeDTO);
			PaymentType paymentType = null;
			if (paymentTypeDTO != null) {
				paymentType = paymentTypeDTO.getPaymentType();
			}
			dto = new StudentDTO(entity.getId(), entity.getName(), entity.getSurname(), entity.getJobTitle(), paymentType);
		}
		return dto;
	}

	public StudentDTO getPaymentForStudentFallback(String studentId, Throwable e) {
		log.info("No response from payment-ms. Default paymentType UNKNOWN for student " + studentId);
		StudentDTO dto = new StudentDTO(studentId, "fallback", "fallback", "fallback", PaymentType.UNKNOWN);
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
