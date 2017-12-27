package com.ea.campus.ms.student.repository;

import org.springframework.data.repository.CrudRepository;

import com.ea.campus.ms.student.entity.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, String> {
	
	public StudentEntity findByName(String name);//impl from framework
	
}