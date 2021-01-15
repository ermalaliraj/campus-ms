package com.ea.campus.ms.course.topics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

	@Autowired
	private TopicRepository topicRepository;

	public List<TopicDTO> getAllTopics() {
		List<TopicDTO> topics = new ArrayList<>();
		topicRepository.findAll()
				.forEach(entity -> topics.add(TopicMapper.toDTO(entity)));
		return topics;
	}

	public TopicDTO getTopic(String id) {
		return TopicMapper.toDTO(topicRepository.findOne(id));
	}

	public void addTopic(TopicDTO topic) {
		topicRepository.save(TopicMapper.toEntity(topic));
	}
	
	public void updateTopic(TopicDTO topic) {
		topicRepository.save(TopicMapper.toEntity(topic));
	}

	public void deleteTopic(String id) {
		topicRepository.delete(id);
	}

	public void deleteAll() {
		topicRepository.deleteAll();
	}
}
