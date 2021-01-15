package com.ea.campus.ms.course.topics;

import com.ea.campus.ms.course.courses.CourseMapper;

public class TopicMapper {

    public static TopicDTO toDTO(TopicEntity entity) {
        TopicDTO dto = null;
        if (entity != null) {
            dto = new TopicDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setCourse(CourseMapper.toDTO(entity.getCourse()));
        }
        return dto;
    }

    public static TopicEntity toEntity(TopicDTO dto) {
        TopicEntity entity = null;
        if (dto != null) {
            entity = new TopicEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setCourse(CourseMapper.toEntity(dto.getCourse()));
        }
        return entity;
    }
}
