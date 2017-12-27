package com.ea.campus.ms.course.courses;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ea.campus.ms.course.topics.TopicEntity;

@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	@RequestMapping("/ping")
	public String ping() {
		return "Date " + new Date();
	}

	@RequestMapping("/courses")
	public List<CourseEntity> getAllCourses() {
		return courseService.getAllCourses();
	}

	@RequestMapping("/courses/{id}")
	public CourseEntity getCourse(@PathVariable String id) {
		return courseService.getCourse(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/courses")
	public void addCourse(@RequestBody CourseEntity course) {
		courseService.addCourse(course);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/courses/{id}")
	public void updateCourse(@RequestBody CourseEntity course, @PathVariable String id) {
		courseService.updateCourse(course);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/courses/{id}")
	public void deleteCourse(@PathVariable String id) {
		courseService.deleteCourse(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/courses/{courseId}/topic")
	public void addTopicForCourse(@RequestBody TopicEntity topic, @PathVariable String courseId) {
		courseService.addTopicForCourse(topic, courseId);
	}
}
