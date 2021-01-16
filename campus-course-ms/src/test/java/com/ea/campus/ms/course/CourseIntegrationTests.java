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
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.ea.campus.ms.course.courses.dto.CourseDTO;
import com.ea.campus.ms.course.topics.TopicDTO;

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
		List<TopicDTO> list = (List<TopicDTO>) resp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test_topic_CRUD() {
		final String url = host + port + "/topics/";
		final String id = "1";

		// 1. Insert
		TopicDTO topic = new TopicDTO(id, "Arrays", "Arrays in Java");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(url, topic, Void.class, topic);
		log.debug("postResp: " + postResp);

		// 2. Get
		ResponseEntity<TopicDTO> resp = testRestTemplate.getForEntity(url + id, TopicDTO.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(resp.getBody()).isEqualTo(topic);

		// 3. Get list
		ResponseEntity<List> respList = testRestTemplate.getForEntity(url, List.class);
		log.debug("getResp as List: " + respList.getBody());
		then(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<TopicDTO> list = (List<TopicDTO>) respList.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		topic.setDescription("Arrays in Java UPDATED");
		testRestTemplate.put(url, topic);
		log.debug("Topic updated");
		// check updated
		resp = testRestTemplate.getForEntity(url + id, TopicDTO.class);
		log.debug("resp Updated: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(resp.getBody()).isEqualTo(topic);

		// 5. Delete
		testRestTemplate.delete(url + id, topic);
		log.debug("Topic deleted");
		// check deleted
		resp = testRestTemplate.getForEntity(url + id, TopicDTO.class);
		log.debug("resp Deleted: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_course_exchange() {
		final String url = host + port + "/courses/";
		final String id = "1";

		// 1. Insert
		CourseDTO course = new CourseDTO(id, "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(url, course, Void.class);
		log.debug("postResp: " + postResp);

		// 2. Update
		course.setDescription("Java 7 course UPDATED");
		HttpHeaders httpHeaders = testRestTemplate.headForHeaders(url);
		HttpEntity<CourseDTO> requestUpdate = new HttpEntity<>(course, httpHeaders);
		testRestTemplate.exchange(url, HttpMethod.PUT, requestUpdate, Void.class);
		log.debug("Course updated");

		// check updated
		ResponseEntity<CourseDTO> resp = testRestTemplate.getForEntity(url + id, CourseDTO.class);
		log.debug("resp Updated: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(resp.getBody()).isEqualTo(course);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_course_CRUD() {
		final String url = host + port + "/courses/";
		final String id = "1";

		// 1. Insert
		CourseDTO course = new CourseDTO(id, "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(url, course, Void.class);
		log.debug("postResp: " + postResp);

		// 2. Get
		ResponseEntity<CourseDTO> resp = testRestTemplate.getForEntity(url + id, CourseDTO.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(resp.getBody()).isEqualTo(course);

		// 3. Get list
		ResponseEntity<List> respList = testRestTemplate.getForEntity(url, List.class);
		log.debug("getResp as List: " + respList.getBody());
		then(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<CourseDTO> list = (List<CourseDTO>) respList.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);

		// 4. Update
		course.setDescription("Java 7 course UPDATED");
		testRestTemplate.put(url, course);
		log.debug("Course updated");

		// check updated
		resp = testRestTemplate.getForEntity(url + id, CourseDTO.class);
		log.debug("resp Updated: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then(resp.getBody()).isEqualTo(course);

		// 5. Delete
		testRestTemplate.delete(url + id, course);
		log.debug("Course deleted");
		// check deleted
		resp = testRestTemplate.getForEntity(url + id, CourseDTO.class);
		log.debug("resp Deleted: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNull();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_CourseAndTopic() throws Exception {
		// 1. Insert Course
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", course, Void.class);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Insert Topic, addTopicForCourse()
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		ResponseEntity<Void> postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);

		// checks if topic is associated with Course
		ResponseEntity<TopicDTO> topicDB = testRestTemplate.getForEntity(host + port + "/topics/" + topic.getId(), TopicDTO.class);
		log.debug("getResp: " + topicDB.getBody());
		log.debug("topic  : " + topic);
		then(topicDB.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicDB.getBody()).isNotNull();
		then(topicDB.getBody().getCourse()).isEqualTo(course);

		// getAllTopicsForCourse
		ResponseEntity<List> topicListResp = testRestTemplate.getForEntity(host + port + "/courses/" + course.getId() + "/topics", List.class);
		log.debug("topicListResp: " + topicListResp.getBody());
		then(topicListResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicListResp.getBody()).isNotNull();
		List<TopicDTO> list = (List<TopicDTO>) topicListResp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(1);
		// then((TopicDTO)list.get(0)).isEqualTo(topicDB.getBody()); List<TopicDTO> not taking effect. .get(0) is of type LinkedHashMap
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test_CourseAndNTopics() throws Exception {
		// 1. Insert Course
		CourseDTO course = new CourseDTO("1", "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", course, Void.class);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Insert 3 Topics, addTopicForCourse()
		List<TopicDTO> topicsList = new ArrayList<>();
		TopicDTO topic = new TopicDTO("1", "Arrays", "Arrays in Java");
		ResponseEntity<Void> postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());
		topic = new TopicDTO("2", "Strings", "Strings in Java");
		postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());
		topic = new TopicDTO("3", "Multithreading", "Multithreading in Java");
		postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topics", topic, Void.class);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);
		topicsList.add(topic.clone());

		// getAllTopicsForCourse
		ResponseEntity<List> topicListResp = testRestTemplate.getForEntity(host + port + "/courses/" + course.getId() + "/topics", List.class);
		log.debug("topicListResp: " + topicListResp.getBody());
		then(topicListResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicListResp.getBody()).isNotNull();
		List<TopicDTO> list = (List<TopicDTO>) topicListResp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(topicsList.size());
	}

}
