package com.ea.campus.ms.student.exception;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.ResourceSupport;

import com.ea.campus.ms.student.context.EmbeddedResourceSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ErrorResource extends EmbeddedResourceSupport {

	private String uuid;
	private String exceptionType;
	private String message;
	private String code;
	private String command;
	private String stackTrace;
	private boolean isOriginException;

	public ErrorResource() {
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void addError(ResourceSupport error) {
		if (hasEmbeddedResources("errors")) {
			getEmbeddedResourcesByRel("errors").add(error);
		} else {
			Set<ResourceSupport> errors = new HashSet<ResourceSupport>();
			errors.add(error);
			put("errors", errors);
		}
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@JsonIgnore
	public boolean isOriginException() {
		return isOriginException;
	}

	public void setOriginException(boolean isOriginException) {
		this.isOriginException = isOriginException;
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(exceptionType)
				.append(message)
				.append(code)
				.append(stackTrace)
				.append(command)
				.append(isOriginException)
				.append(getLinks()).toHashCode();
	}

	public boolean equals(Object o) {		
		if (!(o instanceof ErrorResource))
			return false;

		ErrorResource other = (ErrorResource) o;
		return new EqualsBuilder()
				.append(exceptionType, other.exceptionType)
				.append(message, other.message)
				.append(code, other.code)
				.append(stackTrace, other.stackTrace)
				.append(command, other.command)
				.append(isOriginException, other.isOriginException)
				.append(getLinks(), other.getLinks()).isEquals();
		
	}
}
