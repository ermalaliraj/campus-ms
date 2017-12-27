package com.ea.campus.ms.course.courses;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {
	
	public List<CourseEntity> findByName(String name);//impl from framework

}
