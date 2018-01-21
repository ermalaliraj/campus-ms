package com.ea.campus;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ea.campus.ms.student.StudentApp;
import com.ea.campus.ms.student.dto.StudentDTO;
import com.ea.campus.ms.student.entity.StudentEntity;
import com.ea.campus.ms.student.service.StudentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StudentApp.class)
public class StudentServiceTest {

	private static final transient Logger log = LoggerFactory.getLogger(StudentServiceTest.class);

	@Autowired
	private StudentService studentService;

	@Before
	public void init() {
		studentService.deleteAll();
	}

	@Test
	public void student_CRUD() {
		// 1. Insert
		StudentEntity student = new StudentEntity("ermal", "ermal", "aliraj");
		studentService.addStudent(student);

		// 2. Get
		StudentEntity studentDB = studentService.getStudent(student.getId());
		log.debug("studentDB: " + studentDB);
		then(studentDB).isNotNull();
		then(studentDB).isEqualTo(student);

		// 3. Get list
		List<StudentEntity> list = studentService.getAllStudents();
		log.debug("topic list: " + list);
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		student.setSurname("aliraj UPDATED");
		studentService.updateStudent(student);
		studentDB = studentService.getStudent(student.getId());
		log.debug("studentDB: " + studentDB);
		then(studentDB).isNotNull();
		then(studentDB).isEqualTo(student);

		// 5. Delete
		studentService.deleteStudent(student.getId());
		studentDB = studentService.getStudent(student.getId());
		log.debug("studentDB: " + studentDB);
		then(studentDB).isNull();
	}

}
