package com.ea.campus.ms.payment;

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

import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.entity.PaymentStudentEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PaymentApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class PaymentAppTest {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentAppTest.class);

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
	public void hp_PaymentTypeOK() throws Exception {
		String studentName = "ermal";
		ResponseEntity<PaymentStudentEntity> resp = testRestTemplate.getForEntity(host + port + "/payment/student/" + studentName, PaymentStudentEntity.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((PaymentStudentEntity) resp.getBody()).getName()).isEqualTo(studentName);
		then(((PaymentStudentEntity) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.OK);
	}

	@Test
	public void hp_PaymentTypeNOTOK() throws Exception {
		String studentName = "ermal_notok";
		ResponseEntity<PaymentStudentEntity> resp = testRestTemplate.getForEntity(host + port + "/payment/student/" + studentName, PaymentStudentEntity.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((PaymentStudentEntity) resp.getBody()).getName()).isEqualTo(studentName);
		then(((PaymentStudentEntity) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.NOTOK);
	}

	@Test
	public void cl_PaymentTypeNEUTRAL() throws Exception {
		String studentName = "ermal_not_present";
		ResponseEntity<PaymentStudentEntity> resp = testRestTemplate.getForEntity(host + port + "/payment/student/" + studentName, PaymentStudentEntity.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((PaymentStudentEntity) resp.getBody()).getName()).isEqualTo(studentName);
		then(((PaymentStudentEntity) resp.getBody()).getPaymentType()).isEqualTo(PaymentType.NEUTRAL);
	}

}
