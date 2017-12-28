package com.ea.campus.ms.payment.repository;

import org.springframework.data.repository.CrudRepository;

import com.ea.campus.ms.payment.entity.PaymentStudentEntity;

public interface PaymentRepository extends CrudRepository<PaymentStudentEntity, String> {

}
