package com.ea.campus.ms.course.courses.controller;

import java.util.Date;
import java.util.List;

import com.ea.campus.ms.course.courses.dto.CourseDTO;
import com.ea.campus.ms.course.courses.service.CourseService;
import com.ea.campus.ms.course.topics.TopicDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

	private static final transient Logger log = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService courseService;

	@RequestMapping("/ping")
	public String ping() {
		return "Date " + new Date();
	}

	@RequestMapping("/courses")
	public List<CourseDTO> getAllCourses() {
		List<CourseDTO> courses = courseService.getAllCourses();
		log.info("Tot courses found in DB: " + courses.size());
		return courses;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/courses/{id}")
	public CourseDTO getCourse(@PathVariable String id) {
		CourseDTO course = courseService.getCourse(id);
		log.info("Found in DB: " + course);
		return course;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/courses")
	public void addCourse(@RequestBody CourseDTO course) {
		courseService.addCourse(course);
		log.info("Saved in DB: " + course);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/courses")
	public void updateCourse(@RequestBody CourseDTO course) {
		courseService.updateCourse(course);
		log.info("Updated in DB: " + course);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/courses/{id}")
	public void deleteCourse(@PathVariable String id) {
		courseService.deleteCourse(id);
		log.info("Deleted from DB course with id: " + id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/courses/deleteall")
	public void deleteAllCourses() {
		courseService.deleteAll();
		log.info("Deleted ALL courses from DB");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/courses/{courseId}/topics")
	public List<TopicDTO> getAllTopicsForCourse(@PathVariable String courseId) {
		List<TopicDTO> topics = courseService.getAllTopicsForCourse(courseId);
		log.info("For course " + courseId + " found " + topics.size() + " topics");
		return topics;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/courses/{courseId}/topics")
	public void addTopicForCourse(@RequestBody TopicDTO topic, @PathVariable String courseId) {
		courseService.addTopicForCourse(topic, courseId);
		log.info("For course " + courseId + " added topic " + topic);
	}

}
