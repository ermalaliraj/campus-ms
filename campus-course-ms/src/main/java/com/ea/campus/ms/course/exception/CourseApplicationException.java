package com.ea.campus.ms.course.exception;

public class CourseApplicationException extends Exception {

    public CourseApplicationException(String message) {
        super(message);
    }

    public CourseApplicationException(String message, Throwable e) {
        super(message, e);
    }
}
