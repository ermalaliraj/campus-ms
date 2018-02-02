package com.ea.campus.ms.restutil.context;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;

@Service
public final class ExecutionContextAccessor {

	private static ExecutionContextStrategy strategy = new ThreadLocalExecutionContext();
	
	private ExecutionContextAccessor() {
	}
	
	public static ServiceExecutionContext getExecutionContext() {
		return strategy.getContext();
	}

	public static ServiceExecutionContext resetContext() {
		return strategy.resetContext();
	}

	public static void fixContext() {
		strategy.fixContext();
	}

	public static void removeContext() {
		strategy.removeContext();
	}

	@VisibleForTesting
	public static ExecutionContextStrategy getStrategy() {
		return strategy;
	}
}
