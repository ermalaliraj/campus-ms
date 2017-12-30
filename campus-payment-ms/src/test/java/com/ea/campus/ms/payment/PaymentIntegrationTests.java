package com.ea.campus.ms.payment;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.junit.After;
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

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.entity.PaymentType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PaymentApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class PaymentIntegrationTests {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentIntegrationTests.class);

	@LocalServerPort
	private int port;
	@Value("${local.management.port}")
	private int mgt;
	private String host = "http://localhost:";
	@Autowired
	private TestRestTemplate testRestTemplate;

	@After
	public void afterEachTest() {
		testRestTemplate.getForEntity(host + port + "/payments/student/deleteall", Void.class);
	}

	@Test
	public void testPing() throws Exception {
		ResponseEntity<String> resp = testRestTemplate.getForEntity(host + port + "/payments/ping", String.class);
		log.debug("Date from MS: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test_topic_CRUD() throws Exception {
		String id = "ermal";
		// 1. Insert
		PaymentStudentEntity entity = new PaymentStudentEntity(id, PaymentType.OK);
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/payments/student", entity, Void.class, entity);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Get
		ResponseEntity<PaymentStudentEntity> resp = testRestTemplate.getForEntity(host + port + "/payments/student/" + id, PaymentStudentEntity.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((PaymentStudentEntity) resp.getBody())).isEqualTo(entity);

		// 3. Get list
		ResponseEntity<List> respList = testRestTemplate.getForEntity(host + port + "/payments/student", List.class);
		log.debug("getResp as List: " + respList.getBody());
		then(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<PaymentStudentEntity> list = (List<PaymentStudentEntity>) respList.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 5. Delete
		testRestTemplate.delete(host + port + "/payments/student/" + id);
		log.debug("PaymentStudent deleted");
		resp = testRestTemplate.getForEntity(host + port + "/payments/student/" + id, PaymentStudentEntity.class);
		log.debug("resp Deleted: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

}
