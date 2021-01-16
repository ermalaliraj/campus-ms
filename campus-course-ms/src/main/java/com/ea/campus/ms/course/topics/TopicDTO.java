package com.ea.campus.ms.course.topics;

import com.ea.campus.ms.course.courses.dto.CourseDTO;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class TopicDTO implements Serializable {

    private static final long serialVersionUID = 7773225503124016766L;

    private String id;
    private String name;
    private String description;

    private CourseDTO course;

    public TopicDTO(String id, String name, String description, CourseDTO course) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.course = course;
    }

    public TopicDTO(String id, String name, String description) {
        this(id, name, description, null);
    }

    public TopicDTO() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public boolean equals(final Object other) {
        if (!(other instanceof TopicDTO)) {
            return false;
        }
        TopicDTO o = (TopicDTO) other;

        return new EqualsBuilder()
                .append(id, o.id)
                .append(name, o.name)
                .append(description, o.description)
//                .append(course, o.course)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .append(description)
                .append(course)
                .toHashCode();
    }

    public TopicDTO clone() {
        TopicDTO clone = new TopicDTO(id, name, description, course);
        return clone;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("id", id)
                .append("name", name)
                .append("description", description)
                .append("course", course)
                .toString();
    }
}
