package com.ea.campus.ms.course.courses;

public class CourseMapper {

    public static CourseDTO toDTO(CourseEntity entity) {
        CourseDTO dto = null;
        if (entity != null) {
            dto = new CourseDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setMaxSubscriptions(entity.getMaxSubscriptions());
            dto.setStillAvailableSubscriptions(entity.getStillAvailableSubscriptions());
        }
        return dto;
    }

    public static CourseEntity toEntity(CourseDTO dto) {
        CourseEntity entity = null;
        if (dto != null) {
            entity = new CourseEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setMaxSubscriptions(dto.getMaxSubscriptions());
            entity.setStillAvailableSubscriptions(dto.getStillAvailableSubscriptions());
        }
        return entity;
    }
}
