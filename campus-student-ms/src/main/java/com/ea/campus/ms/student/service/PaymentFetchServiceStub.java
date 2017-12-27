package com.ea.campus.ms.student.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;
import com.ea.campus.ms.student.util.StubFileReader;
import com.ea.campus.ms.student.util.StubFileException;

@Service
@ConditionalOnProperty(name = "stub.payment-ms", havingValue = "true")
public class PaymentFetchServiceStub implements PaymentFetchService {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentFetchServiceStub.class);
	private static final String STUB_PREFIX = "stubs/payment/";
	private static final String STUB_SUFFIX = ".json";

	public PaymentTypeDTO getPaymentForStudent(String name) {
		PaymentTypeDTO dto = null; 
		try {
			dto = StubFileReader.createResourceFromFile(STUB_PREFIX + name + STUB_SUFFIX, PaymentTypeDTO.class);
			log.info("PaymentTypeDTO for student '" + name + "' is " + dto);
		} catch (StubFileException e) {
			log.error("Error reading payment-stub for student '" + name + "', returning the default value PaymentType.NEUTRAL");
			dto = new PaymentTypeDTO(PaymentType.NEUTRAL);
		}
		return dto;
	}
}
