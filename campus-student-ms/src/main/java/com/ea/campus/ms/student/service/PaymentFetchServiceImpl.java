package com.ea.campus.ms.student.service;

import static com.ea.campus.ms.student.dto.external.CurieExternal.PAYMENT_SERVICE;
import static com.ea.campus.ms.student.dto.external.CurieExternal.PAYMENT_TAG;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;
import com.ea.campus.ms.student.fetch.AbstractRestClientServiceStudentMs;

@Service
@ConditionalOnProperty(name = "stub.payment-ms", havingValue = "false", matchIfMissing = true)
public class PaymentFetchServiceImpl extends AbstractRestClientServiceStudentMs implements PaymentFetchService {

	private static final transient Logger log = LoggerFactory.getLogger(PaymentFetchServiceImpl.class);

	@Override
	public PaymentTypeDTO getPaymentForStudent(String name) {
		Map<String, Object> input = new HashMap<>();
		input.put("id", name);
		
		String url = getLinkUrl(PAYMENT_SERVICE, PAYMENT_TAG, input);
		log.debug("Fetching data from: " + url);
		return get(url, PaymentTypeDTO.class, input);
	}
	
}
