package com.ea.campus.ms.restutil.context;

public interface ExecutionContextStrategy {
	/**
	 * Initialize and gets the execution context. 
	 */
	ServiceExecutionContext getContext();

	/**
	 * Resets the execution context and returns it.
	 */
	ServiceExecutionContext resetContext();

	/**
	 * Makes the execution context immutable.
	 */
	void fixContext();

	/**
	 * Removes the {@link ThreadLocal} related to the ExecutionContextStrategy. 
	 * This is called when all the job of the server side has completed.
	 */
	void removeContext();
}