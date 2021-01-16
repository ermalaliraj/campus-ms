package com.ea.campus.ms.course.exception;

public class CourseSystemException extends RuntimeException {

    public CourseSystemException(String message) {
        super(message);
    }

    public CourseSystemException(String message, Throwable e) {
        super(message, e);
    }
}
