package com.ea.campus.ms.course;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ea.campus.ms.course.courses.CourseEntity;
import com.ea.campus.ms.course.topics.TopicEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CourseApp.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
public class CourseIntegrationTests {

	private static final transient Logger log = LoggerFactory.getLogger(CourseIntegrationTests.class);

	@LocalServerPort
	private int port;
	@Value("${local.management.port}")
	private int mgt;
	private String host = "http://localhost:";
	@Autowired
	private TestRestTemplate testRestTemplate;

	@After
	public void afterEachTest() {
		testRestTemplate.getForEntity(host + port + "/topics/deleteall", Void.class);
		testRestTemplate.getForEntity(host + port + "/courses/deleteall", Void.class);
	}

	@Test
	public void testPing() throws Exception {
		ResponseEntity<String> resp = testRestTemplate.getForEntity(host + port + "/ping", String.class);
		log.debug("Date from MS: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testEmptyTopicsList() throws Exception {
		ResponseEntity<List> resp = testRestTemplate.getForEntity(host + port + "/topics", List.class);
		log.debug("resp: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<TopicEntity> list = (List<TopicEntity>) resp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test_topic_CRUD() throws Exception {
		String id = "1";

		// 1. Insert
		TopicEntity entity = new TopicEntity(id, "Arrays", "Arrays in Java");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/topics", entity, Void.class, entity);
		log.debug("postResp: " + postResp);

		// 2. Get
		ResponseEntity<TopicEntity> resp = testRestTemplate.getForEntity(host + port + "/topics/" + id, TopicEntity.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((TopicEntity) resp.getBody())).isEqualTo(entity);

		// 3. Get list
		ResponseEntity<List> respList = testRestTemplate.getForEntity(host + port + "/topics", List.class);
		log.debug("getResp as List: " + respList.getBody());
		then(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<TopicEntity> list = (List<TopicEntity>) respList.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		entity.setDescription("Arrays in Java UPDATED");
		testRestTemplate.put(host + port + "/topics/" + id, entity);
		log.debug("Topic updated");
		// check updated
		resp = testRestTemplate.getForEntity(host + port + "/topics/" + id, TopicEntity.class);
		log.debug("resp Updated: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((TopicEntity) resp.getBody())).isEqualTo(entity);

		// 5. Delete
		testRestTemplate.delete(host + port + "/topics/" + id, entity);
		log.debug("Topic deleted");
		// check deleted
		resp = testRestTemplate.getForEntity(host + port + "/topics/" + id, TopicEntity.class);
		log.debug("resp Deleted: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_course_CRUD() throws Exception {
		String id = "1";

		// 1. Insert
		CourseEntity entity = new CourseEntity(id, "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", entity, Void.class);
		log.debug("postResp: " + postResp);

		// 2. Get
		ResponseEntity<CourseEntity> resp = testRestTemplate.getForEntity(host + port + "/courses/" + id, CourseEntity.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((CourseEntity) resp.getBody())).isEqualTo(entity);

		// 3. Get list
		ResponseEntity<List> respList = testRestTemplate.getForEntity(host + port + "/courses", List.class);
		log.debug("getResp as List: " + respList.getBody());
		then(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<CourseEntity> list = (List<CourseEntity>) respList.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		entity.setDescription("Java 7 course UPDATED");
		testRestTemplate.put(host + port + "/courses/" + id, entity);
		log.debug("Course updated");
		// check updated
		resp = testRestTemplate.getForEntity(host + port + "/courses/" + id, CourseEntity.class);
		log.debug("resp Updated: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(((CourseEntity) resp.getBody())).isEqualTo(entity);

		// 5. Delete
		testRestTemplate.delete(host + port + "/courses/" + id, entity);
		log.debug("Course deleted");
		// check deleted
		resp = testRestTemplate.getForEntity(host + port + "/courses/" + id, CourseEntity.class);
		log.debug("resp Deleted: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_CourseAndTopic() throws Exception {
		// 1. Insert Course
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", course, Void.class);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Insert Topic, addTopicForCourse()
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		ResponseEntity<Void> postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);

		// checks if topic is associated with Course
		ResponseEntity<TopicEntity> topicDB = testRestTemplate.getForEntity(host + port + "/topics/" + topic.getId(), TopicEntity.class);
		log.debug("getResp: " + topicDB.getBody());
		log.debug("topic  : " + topic);
		then(topicDB.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicDB.getBody()).isNotNull();
		then(((TopicEntity) topicDB.getBody()).getCourse()).isEqualTo(course);

		// getAllTopicsForCourse
		ResponseEntity<List> topicListResp = testRestTemplate.getForEntity(host + port + "/courses/" + course.getId() + "/topics", List.class);
		log.debug("topicListResp: " + topicListResp.getBody());
		then(topicListResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicListResp.getBody()).isNotNull();
		List<TopicEntity> list = (List<TopicEntity>) topicListResp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);
		// then((TopicEntity)list.get(0)).isEqualTo(topicDB.getBody()); List<TopicEntity> not taking effect. .get(0) is of type LinkedHashMap
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_CourseAndNTopics() throws Exception {
		// 1. Insert Course
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", course, Void.class);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Insert 3 Topics, addTopicForCourse()
		List<TopicEntity> topicsList = new ArrayList<>();
		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		ResponseEntity<Void> postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());
		topic = new TopicEntity("2", "Strings", "Strings in Java");
		postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());
		topic = new TopicEntity("3", "Multithreading", "Multithreading in Java");
		postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());

		// getAllTopicsForCourse
		ResponseEntity<List> topicListResp = testRestTemplate.getForEntity(host + port + "/courses/" + course.getId() + "/topics", List.class);
		log.debug("topicListResp: " + topicListResp.getBody());
		then(topicListResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicListResp.getBody()).isNotNull();
		List<TopicEntity> list = (List<TopicEntity>) topicListResp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(topicsList.size());
	}

}
