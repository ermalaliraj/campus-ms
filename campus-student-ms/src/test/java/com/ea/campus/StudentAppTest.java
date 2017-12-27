package com.ea.campus;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ea.campus.ms.student.StudentApp;
import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.StudentDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StudentApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class StudentAppTest {

	private static final transient Logger log = LoggerFactory.getLogger(StudentAppTest.class);

	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	private String host = "http://localhost:";

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void hp_ping() throws Exception {
		ResponseEntity<String> entity = testRestTemplate.getForEntity(host + port + "/ping", String.class);
		log.debug("[MS Response]: " + entity.getBody());

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(entity.getBody()).isNotNull();
	}

	@Test
	public void hp_student_paymentTypeOK() throws Exception {
		String studentName = "ermal";
		ResponseEntity<StudentDTO> resp = testRestTemplate.getForEntity(host + port + "/student/" + studentName, StudentDTO.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((StudentDTO) resp.getBody()).getName()).isEqualTo(studentName);
		then(((StudentDTO) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.OK);
	}

	@Test
	public void hp_student_paymentTypeNeutral() throws Exception {
		String studentName = "ermal_neutral";
		ResponseEntity<StudentDTO> resp = testRestTemplate.getForEntity(host + port + "/student/" + studentName, StudentDTO.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((StudentDTO) resp.getBody()).getName()).isEqualTo(studentName);
		then(((StudentDTO) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.NEUTRAL);
	}

	@Test
	public void hp_student_paymentTypeNOTOK() throws Exception {
		String studentName = "ermal_notok";
		ResponseEntity<StudentDTO> resp = testRestTemplate.getForEntity(host + port + "/student/" + studentName, StudentDTO.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((StudentDTO) resp.getBody()).getName()).isEqualTo(studentName);
		then(((StudentDTO) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.NOTOK);
	}
	
	@Test
	public void cl_student_notfound() throws Exception {
		String studentName = "notpresent";
		ResponseEntity<StudentDTO> resp = testRestTemplate.getForEntity(host + port + "/student/" + studentName, StudentDTO.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

}
