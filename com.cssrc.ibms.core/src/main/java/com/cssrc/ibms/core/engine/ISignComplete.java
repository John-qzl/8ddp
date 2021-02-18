package com.cssrc.ibms.core.engine;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;


public interface ISignComplete {
	public static final String SIGN_RESULT_PASS = "pass";
	public static final String SIGN_RESULT_REFUSE = "refuse";
	public static final String SIGN_RESULT_RECOVER = "recover";
	public static final String SIGN_RESULT_BACK = "reject";
	public static final String SIGN_RESULT_TOSTART = "rejectToStart";

	public abstract boolean isComplete(ActivityExecution paramActivityExecution);
}
