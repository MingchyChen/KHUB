package com.claridy.common.mechanism.dao;

import org.springframework.core.NestedRuntimeException;

public class DataAccessException extends NestedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param msg String
	 */
	public DataAccessException(String msg) {
		
		super(msg);
	}

	/**
	 * @param msg
	 * @param e
	 */
	public DataAccessException(String msg, Throwable e) {
		
		super(msg, e);
	}

}
