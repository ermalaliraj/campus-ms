package com.ea.campus.ms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ea.campus.ms.course.courses.repository.CourseRepository;
import com.ea.campus.ms.course.courses.service.CourseService;
import com.ea.campus.ms.course.topics.TopicRepository;
import com.ea.campus.ms.course.topics.TopicService;

/**
 * Not working class
 * Not used class.
 * 
 * This class is needed when Test class do not use @SpringBootTest.
 * 
 * In this case we need to initialize Beans.
 * @ContextConfiguration(classes = ContextConfigurationCampus.class, loader = AnnotationConfigContextLoader.class)
 * 
 * see:
 * https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring/29622382
 */
@Configuration
public class ContextConfigurationCampus {
	
	@Autowired
    private TopicRepository topicRepository;
	@Autowired
    private CourseRepository courseRepository;
	
	  @Bean
      public TopicService topicService() {
      	TopicService bean = new TopicService();
          return bean;
      }
	  
	  @Bean
      public TopicRepository topicRepository() {
		  return topicRepository;
	  }
	  
	  @Bean
      public CourseService courseService() {
		  CourseService bean = new CourseService();
          return bean;
      }
	  
	  @Bean
      public CourseRepository courseRepository() {
		  return courseRepository;
	  }

}
