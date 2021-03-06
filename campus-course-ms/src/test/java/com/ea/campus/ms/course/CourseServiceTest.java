package com.ea.campus.ms.course;

import static com.ea.campus.ms.course.TestUtil.checkListEquality;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ea.campus.ms.course.courses.dto.CourseDTO;
import com.ea.campus.ms.course.courses.service.CourseService;
import com.ea.campus.ms.course.exception.TopicAssociatedToCourseException;
import com.ea.campus.ms.course.topics.TopicDTO;
import com.ea.campus.ms.course.topics.TopicService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CourseServiceTest {

	private static final transient Logger log = LoggerFactory.getLogger(CourseServiceTest.class);

	@Autowired
	private TopicService topicService;
	@Autowired
	private CourseService courseService;

	@Before
	public void init() {
		topicService.deleteAll();
		courseService.deleteAll();
	}

	@Test
	public void topic_CRUD() {
		// 1. Insert
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		topicService.addTopic(topic);

		// 2. Get
		TopicDTO topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB).isEqualTo(topic);

		// 3. Get list
		List<TopicDTO> list = topicService.getAllTopics();
		log.debug("topic list: " + list);
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		topic.setDescription("Arrays in Java UPDATED");
		topicService.updateTopic(topic);
		topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB).isEqualTo(topic);

		// 5. Delete
		topicService.deleteTopic(topic.getId());
		topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNull();
	}

	@Test
	public void course_CRUD() {
		// 1. Insert
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 full course");
		courseService.addCourse(course);

		// 2. Get
		CourseDTO courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);

		// 3. Get list
		List<CourseDTO> list = courseService.getAllCourses();
		log.debug("courses list: " + list);
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		course.setDescription("Java 7 full course UPDATED");
		courseService.updateCourse(course);
		courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);

		// 5. Delete
		courseService.deleteCourse(course.getId());
		courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNull();
	}

	@Test
	public void insertParent_Course_thenChild_Topic() {
		// 1. insert Course, db parent
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. insert Topic, db child
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);

		// checks if topic is associated with Course
		TopicDTO topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB.getCourse()).isEqualTo(course);
		then(topicDB).isEqualTo(topic);

		// checks if topics list of the Course contains the inserted topic
		List<TopicDTO> list = courseService.getAllTopicsForCourse(course.getId());
		checkListEquality(list, Arrays.asList(topic));
	}

	@Test
	public void insertParent_Course_thenNChilds_Topic() {
		// 1. insert Course (db parent)
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. Insert 3 Topics for the course "1" (db child)
		List<TopicDTO> topicsList = new ArrayList<>();
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());
		topic = new TopicDTO("2", "Strings", "Strings in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());
		topic = new TopicDTO("3", "Multithreading", "Multithreading in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());

		// Checks if topics list of the Course contains the inserted topics
		List<TopicDTO> list = courseService.getAllTopicsForCourse(course.getId());
		checkListEquality(list, topicsList);
		
		// Checks if all topics present in DB are the one we inserted. (each junit , db starts empty)
		List<TopicDTO> topicsDB = topicService.getAllTopics();
		log.debug("topicsDB: " + topicsDB);
		then(topicsDB).isNotNull();
		checkListEquality(topicsDB, topicsList);
	}

	@Test
	public void addTopicForCourse() {
		// 1. Insert Course
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. Insert Topic
		TopicDTO topic1 = new TopicDTO("1", "Arrays", "Arrays in Java");
		courseService.addTopicForCourse(topic1, course.getId());

		// 3. Checks, New topic is associated to same Course
		TopicDTO topicDB = topicService.getTopic(topic1.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB.getCourse()).isEqualTo(course);
		
		// 4. List for course 1 should be 1
		List<TopicDTO> topicList = courseService.getAllTopicsForCourse(course.getId());
		log.debug("topicList: " + topicList);
		then(topicList).isNotNull();
		then(topicList.size()).isEqualTo(1);
		then(topicList).isEqualTo(Arrays.asList(topic1));
		
		TopicDTO topic2 = new TopicDTO("2", "Primitives", "Primitives in Java");
		courseService.addTopicForCourse(topic2, course.getId());
		
		topicList = courseService.getAllTopicsForCourse(course.getId());
		log.debug("topicList: " + topicList);
		then(topicList).isNotNull();
		then(topicList.size()).isEqualTo(2);
		then(topicList).isEqualTo(Arrays.asList(topic1, topic2));
	}

	@Test
	public void insertChild_Topic_thenParent_Course_thenUpdateReference() {
		// 1. add Topic, child db table (N records here)
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		topicService.addTopic(topic);

		// 2. add Course, parent db table (1 records here)
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 3. update reference for parent record
		topic.setCourse(course);
		topicService.updateTopic(topic);

		// checks
		TopicDTO topicDB = topicService.getTopic(topic.getId());
		then(topicDB).isNotNull();
		then(topicDB.getCourse()).isEqualTo(course);
		then(topicDB).isEqualTo(topic);
	}
	
	@Test (expected=TopicAssociatedToCourseException.class)
	public void ex_deleteAllCourse_whenTopicAssociated() {
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);
		TopicDTO topic1 = new TopicDTO("1", "Arrays", "Arrays in Java");
		courseService.addTopicForCourse(topic1, course.getId());

		courseService.deleteAll();		
	}
	
	@Test (expected=TopicAssociatedToCourseException.class)
	public void ex_deleteCourse_whenTopicAssociated() {
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);
		TopicDTO topic1 = new TopicDTO("1", "Arrays", "Arrays in Java");
		courseService.addTopicForCourse(topic1, course.getId());

		courseService.deleteCourse(course.getId());		
	}

}
