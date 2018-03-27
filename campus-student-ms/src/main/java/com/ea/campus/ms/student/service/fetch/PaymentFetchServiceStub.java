package com.ea.campus.ms.student.service.fetch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.restutil.exception.StubFileException;
import com.ea.campus.ms.restutil.util.StubFileReader;
import com.ea.campus.ms.student.dto.PaymentType;
import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;

@Service
@ConditionalOnProperty(name = "stub.payment-ms", havingValue = "true")
public class PaymentFetchServiceStub implements PaymentFetchService {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentFetchServiceStub.class);
	private static final String STUB_PREFIX = "stubs/payment/";
	private static final String STUB_SUFFIX = ".json";

	public PaymentTypeDTO getPaymentForStudent(String id) {
		PaymentTypeDTO dto = null; 
		try {
			dto = StubFileReader.createResourceFromFile(STUB_PREFIX + id + STUB_SUFFIX, PaymentTypeDTO.class);
			log.info("PaymentTypeDTO for student id: " + id + " is " + dto);
		} catch (StubFileException e) {
			log.error("Error reading payment-stub for student id: " + id + ", returning the default value PaymentType.NEUTRAL");
			dto = new PaymentTypeDTO(PaymentType.UNKNOWN);
		}
		return dto;
	}
}
