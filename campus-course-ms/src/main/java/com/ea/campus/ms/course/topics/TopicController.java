package com.ea.campus.ms.course.topics;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {

	private static final transient Logger log = LoggerFactory.getLogger(TopicController.class);

	@Autowired
	private TopicService topicService;

	@RequestMapping("/topics")
	public List<TopicDTO> getAllTopics() {
		List<TopicDTO> topics = topicService.getAllTopics();
		log.info("Tot topics found in DB: " + topics.size());
		return topics;
	}

	@RequestMapping("/topics/{id}")
	public TopicDTO getTopic(@PathVariable String id) {
		TopicDTO topic = topicService.getTopic(id);
		log.info("Found in DB: " + topic);
		return topic;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/topics")
	public void addTopic(@RequestBody TopicDTO topic) {
		topicService.addTopic(topic);
		log.info("Saved in DB: " + topic);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/topics")
	public void updateTopic(@RequestBody TopicDTO topic) {
		topicService.updateTopic(topic);
		log.info("Updated in DB: " + topic);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/topics/{id}")
	public void deleteTopic(@PathVariable String id) {
		topicService.deleteTopic(id);
		log.info("Deleted from DB topic with id: " + id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/topics/deleteall")
	public void deleteAllTopics() {
		topicService.deleteAll();
		log.info("Deleted ALL topics from DB");
	}
}
