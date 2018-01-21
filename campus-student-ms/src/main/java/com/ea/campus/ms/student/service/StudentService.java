package com.ea.campus.ms.student.service;

import java.util.List;

import com.ea.campus.ms.student.dto.StudentDTO;
import com.ea.campus.ms.student.entity.StudentEntity;

public interface StudentService {

	public List<StudentEntity> getAllStudents();

	public StudentEntity getStudent(String id);
	
	public StudentDTO getStudentDTOWithPayment(String id);
	
	public void addStudent(StudentEntity student);

	public void updateStudent(StudentEntity student);

	public void deleteStudent(String id);
	
	public void deleteAll();
	
}
