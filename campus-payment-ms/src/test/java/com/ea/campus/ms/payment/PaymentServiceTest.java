package com.ea.campus.ms.payment;

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

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;
import com.ea.campus.ms.payment.entity.PaymentType;
import com.ea.campus.ms.payment.service.PaymentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PaymentServiceTest {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentServiceTest.class);

	@Autowired
	private PaymentService paymentService;

	@Before
	public void init() {
		paymentService.deleteAll();
	}

	@Test
	public void paymentStudent_CRUD() {
		// 1. Insert
		PaymentStudentEntity paymentStudent = new PaymentStudentEntity("ermal", PaymentType.OK);
		paymentService.addPaymentStudent(paymentStudent);

		// 2. Get
		PaymentStudentEntity paymentStudentDB = paymentService.getPaymentStudent(paymentStudent.getId());
		log.debug("paymentStudentDB: " + paymentStudentDB);
		then(paymentStudentDB).isNotNull();
		then(paymentStudentDB).isEqualTo(paymentStudent);

		// 3. insert other 4
		paymentService.addPaymentStudent(new PaymentStudentEntity("ermal2", PaymentType.OK));
		paymentService.addPaymentStudent(new PaymentStudentEntity("ermal2_notok", PaymentType.NOTOK));
		paymentService.addPaymentStudent(new PaymentStudentEntity("ermal3", PaymentType.OK));
		paymentService.addPaymentStudent(new PaymentStudentEntity("ermal3_notok", PaymentType.NOTOK));

		// 4. Get list
		List<PaymentStudentEntity> list = paymentService.getAllPaymentsStudents();
		then(list).isNotNull();
		then(list.size()).isEqualTo(5);

		// 5. Delete ermal
		paymentService.deletePaymentStudent("ermal");
		paymentStudentDB = paymentService.getPaymentStudent("ermal");
		log.debug("paymentStudentDB: " + paymentStudentDB);
		then(paymentStudentDB).isNull();
		list = paymentService.getAllPaymentsStudents();
		then(list).isNotNull();
		then(list.size()).isEqualTo(4);
		
		// 6. delete all
		paymentService.deleteAll();
		list = paymentService.getAllPaymentsStudents();
		then(list).isNotNull();
		then(list.size()).isEqualTo(0);
	}

}
