package com.ea.campus.ms.student.service.fetch;

import com.ea.campus.ms.student.dto.external.PaymentTypeDTO;

public interface PaymentFetchService {

	public PaymentTypeDTO getPaymentForStudent(String id);
	
}
