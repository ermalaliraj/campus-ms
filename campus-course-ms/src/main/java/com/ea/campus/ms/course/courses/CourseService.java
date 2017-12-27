package com.ea.campus.ms.course.courses;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.course.exception.CourseNotFoundException;
import com.ea.campus.ms.course.topics.TopicEntity;
import com.ea.campus.ms.course.topics.TopicRepository;

@Service
public class CourseService {

	private static final transient Logger log = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TopicRepository topicRepository;

	public List<CourseEntity> getAllCourses() {
		List<CourseEntity> courses = new ArrayList<>();
		courseRepository.findAll().forEach(courses::add);
		return courses;
	}

	public CourseEntity getCourse(String id) {
		return courseRepository.findOne(id);
	}

	public void addCourse(CourseEntity course) {
		courseRepository.save(course);
	}

	public void updateCourse(CourseEntity course) {
		courseRepository.save(course);
	}

	public void deleteCourse(String id) {
		courseRepository.delete(id);
	}

	public void deleteAll() {
		courseRepository.deleteAll();
	}

	public void addTopicForCourse(TopicEntity topic, String courseId) {
		CourseEntity course = courseRepository.findOne(courseId);
		if(course == null){
			throw new CourseNotFoundException("Course '"+courseId+"' not present in DB");
		}
		topic.setCourse(course);
		log.debug("Topic to add, assocciated with course: " + topic);
		topicRepository.save(topic);
	}

	public List<TopicEntity> getAllTopicsForCourse(String courseId) {
		List<TopicEntity> topics = new ArrayList<>();
		topicRepository.findByCourseId(courseId)
			.forEach(topics::add);
		return topics;
	}

}
