package com.ea.campus.ms.course.courses;

import java.util.ArrayList;
import java.util.List;

import com.ea.campus.ms.course.topics.TopicDTO;
import com.ea.campus.ms.course.topics.TopicEntity;
import com.ea.campus.ms.course.topics.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ea.campus.ms.course.exception.CourseNotFoundException;
import com.ea.campus.ms.course.exception.TopicAssociatedToCourseException;
import com.ea.campus.ms.course.topics.TopicRepository;

import static com.ea.campus.ms.course.courses.CourseMapper.toDTO;
import static com.ea.campus.ms.course.courses.CourseMapper.toEntity;

@Service
public class CourseService {

	@SuppressWarnings("unused")
	private static final transient Logger log = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TopicRepository topicRepository;

	public List<CourseDTO> getAllCourses() {
		List<CourseDTO> courses = new ArrayList<>();
		courseRepository.findAll().forEach(entity -> courses.add(toDTO(entity)));
		return courses;
	}

	public CourseDTO getCourse(String courseId) {
		CourseDTO entity = toDTO(courseRepository.findOne(courseId));
		return entity;
	}

	public void addCourse(CourseDTO courseDto) {
		courseRepository.save(toEntity(courseDto));
	}

	public void updateCourse(CourseDTO courseDto) {
		courseRepository.save(toEntity(courseDto));
	}

	public void deleteCourse(String courseId) {
		try {
			courseRepository.delete(courseId);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new TopicAssociatedToCourseException("Course " + courseId + " contains a Topic so, cannot be deleted.");
		}
	}

	public void deleteAll() {
		try {
			courseRepository.deleteAll();
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new TopicAssociatedToCourseException("One of the Courses contains a Topic. Cannot delete ALL courses.");
		}
	}

	public void addTopicForCourse(TopicDTO topicDto, String courseId) {
		TopicEntity topicEntity = TopicMapper.toEntity(topicDto);
		CourseEntity courseEntity = courseRepository.findOne(courseId);
		if (courseEntity == null) {
			throw new CourseNotFoundException("Course '" + courseId + "' not present in DB");
		}
		topicEntity.setCourse(courseEntity);
		topicRepository.save(topicEntity);
	}

	public List<TopicDTO> getAllTopicsForCourse(String courseId) {
		List<TopicDTO> topics = new ArrayList<>();
		topicRepository.findByCourseId(courseId).forEach(entity -> topics.add(TopicMapper.toDTO(entity)));
		return topics;
	}

}
