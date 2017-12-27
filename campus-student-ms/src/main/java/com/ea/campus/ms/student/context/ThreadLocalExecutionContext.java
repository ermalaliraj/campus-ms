package com.ea.campus.ms.student.context;

public class ThreadLocalExecutionContext implements ExecutionContextStrategy {

	private static final ThreadLocal<ServiceExecutionContext> EXECUTION_CONTEXT = ThreadLocal.withInitial(ServiceExecutionContext::new);

	@Override
	public ServiceExecutionContext getContext() {
		return EXECUTION_CONTEXT.get();
	}

	@Override
	public ServiceExecutionContext resetContext() {
		removeContext();
		return getContext();
	}

	@Override
	public void fixContext() {
		EXECUTION_CONTEXT.set(new ImmutableServiceExecutionContext(EXECUTION_CONTEXT.get()));
	}

	@Override
	public void removeContext() {
		EXECUTION_CONTEXT.remove();
	}
}
