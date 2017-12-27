package com.ea.campus.ms.course;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

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

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cl_emptyTopicsList() throws Exception {
		ResponseEntity<List> resp = testRestTemplate.getForEntity(host + port + "/topics", List.class);
		log.debug("[MS Response]: " + resp.getBody());

		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		List<TopicEntity> list = (List<TopicEntity>) resp.getBody();
		then(list).isNotNull();
		then(list.size()).isEqualTo(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void hp_topic_CRUD() throws Exception {
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
	public void hp_course_CRUD() throws Exception {
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

	@Test
	public void hp_topicCourse_CRUD() throws Exception {
		// 1. Insert
		CourseEntity course = new CourseEntity("1", "Java 7", "Java 7 course");
		ResponseEntity<Void> postResp = testRestTemplate.postForEntity(host + port + "/courses", course, Void.class);
		log.debug("postResp: " + postResp);
		then(postResp.getStatusCode()).isEqualTo(HttpStatus.OK);

		TopicEntity topic = new TopicEntity("1", "Arrays", "Arrays in Java");
		ResponseEntity<Void> postRespTopic = testRestTemplate.postForEntity(host + port + "/courses/" + course.getId() + "/topic", topic, Void.class);
		log.debug("postRespTopic: " + postRespTopic);
		then(postRespTopic.getStatusCode()).isEqualTo(HttpStatus.OK);

		// 2. Get
		ResponseEntity<CourseEntity> resp = testRestTemplate.getForEntity(host + port + "/courses/" + course.getId(), CourseEntity.class);
		log.debug("getResp: " + resp.getBody());
		then(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(resp.getBody()).isNotNull();
		then((CourseEntity) resp.getBody()).isEqualTo(course);
//		then(((CourseEntity) resp.getBody()).getTopics()).isNotNull();
//		then(((CourseEntity) resp.getBody()).getTopics().size()).isEqualTo(1);
//		then(((CourseEntity) resp.getBody()).getTopics().get(0)).isEqualTo(topic);

		// 2. Get
		ResponseEntity<TopicEntity> topicDB = testRestTemplate.getForEntity(host + port + "/topics/" + topic.getId(), TopicEntity.class);
		log.debug("getResp: " + topicDB.getBody());
		then(topicDB.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(topicDB.getBody()).isNotNull();
		topic.setCourse(course);
		then(((TopicEntity) topicDB.getBody())).isEqualTo(topic);

	}
}
