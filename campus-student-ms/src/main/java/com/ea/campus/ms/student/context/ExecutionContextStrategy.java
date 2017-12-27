package com.ea.campus.ms.student.context;

public interface ExecutionContextStrategy {
	/**
	 * Gets the execution context. Initializes it if needed.
	 */
	ServiceExecutionContext getContext();

	/**
	 * Resets the current context and returns it.
	 */
	ServiceExecutionContext resetContext();

	/**
	 * Makes the execution context immutable.
	 */
	void fixContext();

	/**
	 * Removes the {@link ThreadLocal} related to the ExecutionContextStrategy. This should be only called when all the job of the server
	 * side has been done!
	 */
	void removeContext();
}