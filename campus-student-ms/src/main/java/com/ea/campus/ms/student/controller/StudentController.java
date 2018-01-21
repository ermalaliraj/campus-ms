package com.ea.campus.ms.student.controller;

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

import com.ea.campus.ms.student.dto.StudentDTO;
import com.ea.campus.ms.student.entity.StudentEntity;
import com.ea.campus.ms.student.exception.CampusException;
import com.ea.campus.ms.student.service.StudentService;

@RestController
public class StudentController {

	private static final transient Logger log = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@RequestMapping("/ping")
	public String ping() {
		return "Date: " + new Date();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/students")
	public List<StudentEntity> getAllStudents() {
		List<StudentEntity> list = null;
		try {
			list = studentService.getAllStudents();
			log.info("Tot students found in DB: " + list.size());
		} catch (Exception e) {
			log.error("Error retriving ALL students", e);
			throw new CampusException("Error retriving ALL students", e);
		}
		return list;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/students/{id}")
	public StudentDTO getStudent(@PathVariable String id) {
		StudentDTO ret = null;
		try {
			ret = studentService.getStudentDTOWithPayment(id);
			log.info("Found in DB: " + ret);
		} catch (Exception e) {
			log.error("Error retriving student " + id, e);
			throw new CampusException("Error retriving student " + id, e);
		}
		return ret;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/students")
	public void addStudent(@RequestBody StudentEntity student) {
		try {
			studentService.addStudent(student);
			log.info("Added in DB: " + student);
		} catch (Exception e) {
			log.error("Error adding student " + student, e);
			throw new CampusException("Error adding student " + student, e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/students")
	public void updateStudent(@RequestBody StudentEntity student) {
		try {
			studentService.addStudent(student);
			log.info("Updated in DB: " + student);
		} catch (Exception e) {
			log.error("Error updating student " + student, e);
			throw new CampusException("Error updating student " + student, e);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/students/{id}")
	public void deleteStudent(@PathVariable String id) {
		try {
			studentService.deleteStudent(id);
			log.info("Deleted from DB student with id: " + id);
		} catch (Exception e) {
			log.error("Error deleting from DB student with id: " + id, e);
			throw new CampusException("Error deleting from DB student with id: " + id, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/students/deleteall")
	public void deleteAll() {
		try {
			studentService.deleteAll();
			log.info("Deleted ALL students from DB");
		} catch (Exception e) {
			log.error("Error deleting ALL students from DB", e);
			throw new CampusException("Error deleting ALL students from DB", e);
		}
	}

}
