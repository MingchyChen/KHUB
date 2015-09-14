package com.claridy.common.mechanism.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_PARAM")
public class Sys_Param  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * This attribute maps to the column FUNC_NO in the SYS_PARAM table.
	 */
	@Id
	protected String func_no;

	/** 
	 * This attribute maps to the column FUNC_NAME in the SYS_PARAM table.
	 */
	protected String func_name;

	/** 
	 * This attribute maps to the column PARENT in the SYS_PARAM table.
	 */
	protected String parent;

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
}
