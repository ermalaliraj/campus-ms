package com.ea.campus.ms.restutil.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriTemplate;

public class MSLogger implements Logger {

	private final int maxJsonLength;
	private Logger logger;

	public MSLogger(Class<?> clazz, int maxJsonLength) {
		logger = LoggerFactory.getLogger(clazz);
		this.maxJsonLength = maxJsonLength;
	}

	public MSLogger(Class<?> clazz) {
		this(clazz, Integer.MAX_VALUE);
	}

	public void logMicroServiceCallDebug(String url, HttpMethod method, String inputMessage, String outputMessage
			, long elapsedTime, HttpStatus statusCode, Object... parameters) {
		logMicroServiceCall(url, method, inputMessage, outputMessage, elapsedTime, statusCode, Level.DEBUG, parameters);
	}

	public void logMicroServiceCallInfo(String url, HttpMethod method, String inputMessage, String outputMessage
			, long elapsedTime, HttpStatus statusCode, Object... parameters) {
		logMicroServiceCall(url, method, inputMessage, outputMessage, elapsedTime, statusCode, Level.INFO, parameters);
	}

	public void logMicroServiceCallError(String url, HttpMethod method, String inputMessage, String outputMessage
			, long elapsedTime, HttpStatus statusCode, Object... parameters) {
		logMicroServiceCall(url, method, inputMessage, outputMessage, elapsedTime, statusCode, Level.ERROR, parameters);
	}

	private void logMicroServiceCall(String url, HttpMethod method, String inputMessage, String outputMessage
			, long elapsedTime, HttpStatus statusCode, Level logLevel, Object... parameters) {
		String message = getMicroServiceMessage(url, method, inputMessage, outputMessage, elapsedTime, statusCode, parameters);
		if (Level.INFO.equals(logLevel)) {
			logger.info(message);
		} else if (Level.DEBUG.equals(logLevel)) {
			logger.debug(message);
		} else {
			logger.error(message);
		}
	}

	private String getMicroServiceMessage(String url, HttpMethod method, String inputMessage, String outputMessage, long elapsedTime
			, HttpStatus statusCode, Object... parameters) {
		String fullUrl = url;
		if (parameters != null && parameters.length > 0) {
			fullUrl = new UriTemplate(url).expand(parameters).toString();
		}
		StringBuilder sb = new StringBuilder("url=").append(fullUrl);
		sb.append("\t method=").append(method);
		sb.append("\t status=").append((statusCode == null ? "unknown" : statusCode));
		sb.append("\t totalTime(ms)=").append(elapsedTime);

		if (inputMessage != null) {
			sb.append("\n   InputMessage : ").append(StringUtils.substring(inputMessage, 0, maxJsonLength));
		}
		if (outputMessage != null) {
			sb.append("\n   OutputMessage: ").append(StringUtils.substring(outputMessage, 0, maxJsonLength));
		}
		return sb.toString();
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String s) {
		if (logger.isTraceEnabled()) {
			logger.trace(s);
		}
	}

	@Override
	public void trace(String s, Object o) {
		if (logger.isTraceEnabled()) {
			logger.trace(s, o);
		}
	}

	@Override
	public void trace(String s, Object o, Object o1) {
		if (logger.isTraceEnabled()) {
			logger.trace(s, o, o1);
		}
	}

	@Override
	public void trace(String s, Object... objects) {
		if (logger.isTraceEnabled()) {
			logger.trace(s, objects);
		}
	}

	@Override
	public void trace(String s, Throwable throwable) {
		if (logger.isTraceEnabled()) {
			logger.trace(s, throwable);
		}
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	@Override
	public void trace(Marker marker, String s) {
		if (logger.isTraceEnabled(marker)) {
			logger.trace(marker, s);
		}
	}

	@Override
	public void trace(Marker marker, String s, Object o) {
		if (logger.isTraceEnabled(marker)) {
			logger.trace(marker, s, o);
		}
	}

	@Override
	public void trace(Marker marker, String s, Object o, Object o1) {
		if (logger.isTraceEnabled(marker)) {
			logger.trace(marker, s, o, o1);
		}
	}

	@Override
	public void trace(Marker marker, String s, Object... objects) {
		if (logger.isTraceEnabled(marker)) {
			logger.trace(marker, s, objects);
		}
	}

	@Override
	public void trace(Marker marker, String s, Throwable throwable) {
		if (logger.isTraceEnabled(marker)) {
			logger.trace(marker, s, throwable);
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String s) {
		if (logger.isDebugEnabled()) {
			logger.debug(s);
		}
	}

	@Override
	public void debug(String s, Object o) {
		if (logger.isDebugEnabled()) {
			logger.debug(s, o);
		}
	}

	@Override
	public void debug(String s, Object o, Object o1) {
		if (logger.isDebugEnabled()) {
			logger.debug(s, o, o1);
		}
	}

	@Override
	public void debug(String s, Object... objects) {
		if (logger.isDebugEnabled()) {
			logger.debug(s, objects);
		}
	}

	@Override
	public void debug(String s, Throwable throwable) {
		if (logger.isDebugEnabled()) {
			logger.debug(s, throwable);
		}
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	@Override
	public void debug(Marker marker, String s) {
		if (logger.isDebugEnabled(marker)) {
			logger.debug(marker, s);
		}
	}

	@Override
	public void debug(Marker marker, String s, Object o) {
		if (logger.isDebugEnabled(marker)) {
			logger.debug(marker, s, o);
		}
	}

	@Override
	public void debug(Marker marker, String s, Object o, Object o1) {
		if (logger.isDebugEnabled(marker)) {
			logger.debug(marker, s, o, o1);
		}
	}

	@Override
	public void debug(Marker marker, String s, Object... objects) {
		if (logger.isDebugEnabled(marker)) {
			logger.debug(marker, s, objects);
		}
	}

	@Override
	public void debug(Marker marker, String s, Throwable throwable) {
		if (logger.isDebugEnabled(marker)) {
			logger.debug(marker, s, throwable);
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String s) {
		if (logger.isInfoEnabled()) {
			logger.info(s);
		}
	}

	@Override
	public void info(String s, Object o) {
		if (logger.isInfoEnabled()) {
			logger.info(s, o);
		}
	}

	@Override
	public void info(String s, Object o, Object o1) {
		if (logger.isInfoEnabled()) {
			logger.info(s, o, o1);
		}
	}

	@Override
	public void info(String s, Object... objects) {
		if (logger.isInfoEnabled()) {
			logger.info(s, objects);
		}
	}

	@Override
	public void info(String s, Throwable throwable) {
		if (logger.isInfoEnabled()) {
			logger.info(s, throwable);
		}
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	@Override
	public void info(Marker marker, String s) {
		if (logger.isInfoEnabled(marker)) {
			logger.info(marker, s);
		}
	}

	@Override
	public void info(Marker marker, String s, Object o) {
		if (logger.isInfoEnabled(marker)) {
			logger.info(marker, s, o);
		}
	}

	@Override
	public void info(Marker marker, String s, Object o, Object o1) {
		if (logger.isInfoEnabled(marker)) {
			logger.info(marker, s, o, o1);
		}
	}

	@Override
	public void info(Marker marker, String s, Object... objects) {
		if (logger.isInfoEnabled(marker)) {
			logger.info(marker, s, objects);
		}
	}

	@Override
	public void info(Marker marker, String s, Throwable throwable) {
		if (logger.isInfoEnabled(marker)) {
			logger.info(marker, s, throwable);
		}
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String s) {
		if (logger.isWarnEnabled()) {
			logger.warn(s);
		}
	}

	@Override
	public void warn(String s, Object o) {
		if (logger.isWarnEnabled()) {
			logger.warn(s, o);
		}
	}

	@Override
	public void warn(String s, Object... objects) {
		if (logger.isWarnEnabled()) {
			logger.warn(s, objects);
		}
	}

	@Override
	public void warn(String s, Object o, Object o1) {
		if (logger.isWarnEnabled()) {
			logger.warn(s, o, o1);
		}
	}

	@Override
	public void warn(String s, Throwable throwable) {
		if (logger.isWarnEnabled()) {
			logger.warn(s, throwable);
		}
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void warn(Marker marker, String s) {
		if (logger.isWarnEnabled(marker)) {
			logger.warn(marker, s);
		}
	}

	@Override
	public void warn(Marker marker, String s, Object o) {
		if (logger.isWarnEnabled(marker)) {
			logger.warn(marker, s, o);
		}
	}

	@Override
	public void warn(Marker marker, String s, Object o, Object o1) {
		if (logger.isWarnEnabled(marker)) {
			logger.warn(marker, s, o, o1);
		}
	}

	@Override
	public void warn(Marker marker, String s, Object... objects) {
		if (logger.isWarnEnabled(marker)) {
			logger.warn(marker, s, objects);
		}
	}

	@Override
	public void warn(Marker marker, String s, Throwable throwable) {
		if (logger.isWarnEnabled(marker)) {
			logger.warn(marker, s, throwable);
		}
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String s) {
		if (logger.isErrorEnabled()) {
			logger.error(s);
		}
	}

	@Override
	public void error(String s, Object o) {
		if (logger.isErrorEnabled()) {
			logger.error(s, o);
		}
	}

	@Override
	public void error(String s, Object o, Object o1) {
		if (logger.isErrorEnabled()) {
			logger.error(s, o, o1);
		}
	}

	@Override
	public void error(String s, Object... objects) {
		if (logger.isErrorEnabled()) {
			logger.error(s, objects);
		}
	}

	@Override
	public void error(String s, Throwable throwable) {
		if (logger.isErrorEnabled()) {
			logger.error(s, throwable);
		}
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	@Override
	public void error(Marker marker, String s) {
		if (logger.isErrorEnabled(marker)) {
			logger.error(marker, s);
		}
	}

	@Override
	public void error(Marker marker, String s, Object o) {
		if (logger.isErrorEnabled(marker)) {
			logger.error(marker, s, o);
		}
	}

	@Override
	public void error(Marker marker, String s, Object o, Object o1) {
		if (logger.isErrorEnabled(marker)) {
			logger.error(marker, s, o, o1);
		}
	}

	@Override
	public void error(Marker marker, String s, Object... objects) {
		if (logger.isErrorEnabled()) {
			logger.error(marker, s, objects);
		}
	}

	@Override
	public void error(Marker marker, String s, Throwable throwable) {
		if (logger.isErrorEnabled()) {
			logger.error(marker, s, throwable);
		}
	}
}
