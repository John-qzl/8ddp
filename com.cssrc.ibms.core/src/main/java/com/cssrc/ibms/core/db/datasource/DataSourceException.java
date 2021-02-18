package com.cssrc.ibms.core.db.datasource;

public class DataSourceException extends RuntimeException {
	private static final long serialVersionUID = 3148019938789322656L;

	public DataSourceException(String msg) {
		super(msg);
	}

	public DataSourceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
