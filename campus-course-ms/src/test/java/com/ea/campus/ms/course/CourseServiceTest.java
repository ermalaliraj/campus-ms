package com.ea.campus.ms.course;

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

import com.ea.campus.ms.course.courses.CourseEntity;
import com.ea.campus.ms.course.courses.CourseService;
import com.ea.campus.ms.course.topics.TopicEntity;
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
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		topicService.addTopic(topic);

		// 2. Get
		TopicEntity topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB).isEqualTo(topic);

		// 3. Get list
		List<TopicEntity> list = topicService.getAllTopics();
		log.debug("topic list: " + topicDB);
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
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 full course");
		courseService.addCourse(course);

		// 2. Get
		CourseEntity courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);

		// 3. Get list
		List<CourseEntity> list = courseService.getAllCourses();
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
	public void insertChild_Course_thenTopic() {
		// 1. insert Course, db child
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. insert Topic, db parent
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);

		// checks
		TopicEntity topicDB = topicService.getTopic(topic.getId());
		log.debug("topicDB: " + topicDB);
		then(topicDB).isNotNull();
		then(topicDB).isEqualTo(topic);

		CourseEntity courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);

		List<TopicEntity> list = courseService.getAllTopicsForCourse(course.getId());
		checkListEquality(list, Arrays.asList(topic));
	}

	@Test
	public void insertChild_Course_thenNParents_Topic() {
		// 1. insert Course (db child)
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. Insert 3 Topics for the course "1" (db parent)
		List<TopicEntity> topicsList = new ArrayList<>();
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());
		topic = new TopicEntity("2", "Strings", "Strings in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());
		topic = new TopicEntity("3", "Multithreading", "Multithreading in Java");
		topic.setCourse(course);
		topicService.addTopic(topic);
		topicsList.add(topic.clone());

		// Checks
		CourseEntity courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);
		// checkListEquality(courseDB.getTopics(), topicsList);

		List<TopicEntity> topicsDB = topicService.getAllTopics();
		log.debug("courseDB: " + courseDB);
		then(topicsDB).isNotNull();
		checkListEquality(topicsDB, topicsList);
	}

	@Test
	public void insertChild_Course_thenNParents() {
		// 1. insert Course (db child)
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 2. Insert 3 Topics for the course "1" (db parent)
		List<TopicEntity> topicsList = new ArrayList<>();
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		// course.addTopic(topic);
		topicService.addTopic(topic);
		courseService.addCourse(course);
		topicsList.add(topic.clone());
		topic = new TopicEntity("2", "Strings", "Strings in Java");
		// course.addTopic(topic);
		topicService.addTopic(topic);
		courseService.addCourse(course);
		topicsList.add(topic.clone());
		topic = new TopicEntity("3", "Multithreading", "Multithreading in Java");
		// course.addTopic(topic);
		topicService.addTopic(topic);
		courseService.addCourse(course);
		topicsList.add(topic.clone());

		// Checks
		CourseEntity courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);
		// checkListEquality(courseDB.getTopics(), topicsList);

		List<TopicEntity> topicsDB = topicService.getAllTopics();
		log.debug("courseDB: " + courseDB);
		then(topicsDB).isNotNull();
		checkListEquality(topicsDB, topicsList);
	}

	@Test
	public void addTopicForCourse() {
		// 1. insert Course (db child)
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		courseService.addTopicForCourse(topic, course.getId());

		// Checks
		CourseEntity courseDB = courseService.getCourse(course.getId());
		log.debug("courseDB: " + courseDB);
		then(courseDB).isNotNull();
		List<TopicEntity> list = courseService.getAllTopicsForCourse(course.getId());
		checkListEquality(list, Arrays.asList(topic));
	}

	@Test
	public void insertParentFirst_Topic_thenCourse_thenUpdateReference() {
		// 1. add Topic, parent db table (1 records here)
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		topicService.addTopic(topic);

		// 2. add Course, child db table (N records here)
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		courseService.addCourse(course);

		// 3. update reference for parent record
		topic.setCourse(course);
		topicService.updateTopic(topic);

		// checks
		CourseEntity courseDB = courseService.getCourse(course.getId());
		then(courseDB).isNotNull();
		then(courseDB).isEqualTo(course);
		List<TopicEntity> list = courseService.getAllTopicsForCourse(course.getId());
		checkListEquality(list, Arrays.asList(topic));

		TopicEntity topicDB = topicService.getTopic(topic.getId());
		then(topicDB).isNotNull();
		then(topicDB).isEqualTo(topic);
	}

	private <T> void checkListEquality(List<T> list1, List<T> list2) {
		then(list1).isNotNull();
		then(list2).isNotNull();
		then(list1.size()).isEqualTo(list2.size());

		for (int i = 0; i < list1.size(); i++) {
			then(list1.get(i)).isEqualTo(list2.get(i));
		}
	}

}
