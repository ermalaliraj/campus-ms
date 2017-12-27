package com.ea.campus.ms.course.topics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

	@Autowired
	private TopicRepository topicRepository;

	public List<TopicEntity> getAllTopics() {
		List<TopicEntity> topics = new ArrayList<>();
		topicRepository.findAll()
			.forEach(topics::add);
		return topics;
	}

	public TopicEntity getTopic(String id) {
		return topicRepository.findOne(id);
	}

	public void addTopic(TopicEntity topic) {
		topicRepository.save(topic);
	}
	
	public void updateTopic(TopicEntity topic) {
		topicRepository.save(topic);
	}

	public void deleteTopic(String id) {
		topicRepository.delete(id);
	}

	public void deleteAll() {
		topicRepository.deleteAll();
	}
}
