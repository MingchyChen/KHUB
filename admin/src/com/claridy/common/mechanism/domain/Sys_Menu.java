package com.claridy.common.mechanism.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_MENU")
public class Sys_Menu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This attribute maps to the column FUNC_NO in the SYS_MENU table.
	 */
	protected String func_no;

	/**
	 * This attribute maps to the column FUNC_NAME in the SYS_MENU table.
	 */
	protected String func_name;

	/**
	 * This attribute maps to the column PARENT in the SYS_MENU table.
	 */
	protected String parent;

	/**
	 * This attribute maps to the column FUNC_URL in the SYS_MENU table.
	 */
	protected String func_url;

	/**
	 * This attribute maps to the column SEQ in the SYS_MENU table.
	 */
	protected String seq;

	protected String function_class;

	@Id
	public String getFunc_no() {
		return func_no;
	}

	public void setFunc_no(String func_no) {
		this.func_no = func_no;
	}

	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getFunc_url() {
		return func_url;
	}

	public void setFunc_url(String func_url) {
		this.func_url = func_url;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	/**
	 * @return the function_class
	 */
	public String getFunction_class() {
		return function_class;
	}

	/**
	 * @param function_class
	 *            the function_class to set
	 */
	public void setFunction_class(String function_class) {
		this.function_class = function_class;
	}

}
