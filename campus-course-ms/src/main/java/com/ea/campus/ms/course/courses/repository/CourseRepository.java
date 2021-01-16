package com.ea.campus.ms.course.courses.repository;

import com.ea.campus.ms.course.courses.entity.CourseEntity;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<CourseEntity, String> {
	
}
