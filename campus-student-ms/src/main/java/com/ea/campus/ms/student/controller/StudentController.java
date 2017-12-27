package com.ea.campus.ms.student.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.student.dto.StudentDTO;
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

	@RequestMapping("/student/{name}")
	public StudentDTO getStudent(@PathVariable String name) {
		StudentDTO ret = null;
		try {
			ret = studentService.getByName(name);
		} catch (Exception e) {
			log.error("Error retriving student " + name, e);
		}
		return ret;
	}

}
