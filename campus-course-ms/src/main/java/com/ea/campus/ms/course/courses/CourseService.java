package com.ea.campus.ms.course.courses;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.course.exception.CourseNotFoundException;
import com.ea.campus.ms.course.exception.TopicAssociatedToCourseException;
import com.ea.campus.ms.course.topics.TopicEntity;
import com.ea.campus.ms.course.topics.TopicRepository;

@Service
public class CourseService {

	@SuppressWarnings("unused")
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
		CourseEntity entity = courseRepository.findOne(id);
		return entity;
	}

	public void addCourse(CourseEntity course) {
		courseRepository.save(course);
	}

	public void updateCourse(CourseEntity course) {
		courseRepository.save(course);
	}

	public void deleteCourse(String id) {
		try {
			courseRepository.delete(id);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new TopicAssociatedToCourseException("Course " + id + " contains a Topic so, cannot be deleted.");
		}
	}

	public void deleteAll() {
		try {
			courseRepository.deleteAll();
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new TopicAssociatedToCourseException("One of the Courses contains a Topic. Cannot delete ALL courses.");
		}
	}

	public void addTopicForCourse(TopicEntity topic, String courseId) {
		CourseEntity course = courseRepository.findOne(courseId);
		if (course == null) {
			throw new CourseNotFoundException("Course '" + courseId + "' not present in DB");
		}
		topic.setCourse(course);
		topicRepository.save(topic);
	}

	public List<TopicEntity> getAllTopicsForCourse(String courseId) {
		List<TopicEntity> topics = new ArrayList<>();
		topicRepository.findByCourseId(courseId).forEach(topics::add);
		return topics;
	}

}
