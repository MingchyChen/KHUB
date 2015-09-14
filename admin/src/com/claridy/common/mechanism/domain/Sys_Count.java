package com.claridy.common.mechanism.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_COUNT")
public class Sys_Count implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * This attribute maps to the column COUNT in the SYS_COUNT table.
	 */
	protected Long count;

	/** 
	 * This attribute maps to the column OBJ_PK in the SYS_COUNT table.
	 */
	@Id
	protected String obj_pk;

	/** 
	 * This attribute maps to the column OBJ_NAME in the SYS_COUNT table.
	 */
	@Id
	protected String obj_name;
	
	@Id
	protected String obj_time;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getObj_pk() {
		return obj_pk;
	}

	public void setObj_pk(String obj_pk) {
		this.obj_pk = obj_pk;
	}

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	public String getObj_time() {
		return obj_time;
	}

	public void setObj_time(String obj_time) {
		this.obj_time = obj_time;
	}
}