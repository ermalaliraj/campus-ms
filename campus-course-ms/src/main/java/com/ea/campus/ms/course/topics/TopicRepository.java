package com.ea.campus.ms.course.topics;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<TopicEntity, String> {
	
	public List<TopicEntity> findByCourseId(String courseId);//impl from framework
	
}
