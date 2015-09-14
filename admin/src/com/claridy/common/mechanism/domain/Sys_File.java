package com.claridy.common.mechanism.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYS_FILE")
public class Sys_File  implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 
	 * This attribute maps to the column FILE_PK in the SYS_FILE table.
	 */
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	protected Long file_pk;

	/** 
	 * This attribute maps to the column OBJ_PK in the SYS_FILE table.
	 */
	protected String obj_pk;

	/** 
	 * This attribute maps to the column OBJ_NAME in the SYS_FILE table.
	 */
	protected String obj_name;

	/** 
	 * This attribute maps to the column FILE_NAME in the SYS_FILE table.
	 */
	protected String file_name;

	/** 
	 * This attribute maps to the column REMARKS in the SYS_FILE table.
	 */
	protected String remarks;

	/** 
	 * This attribute maps to the column CREATORACCT in the SYS_FILE table.
	 */
	protected String creatoracct;

	/** 
	 * This attribute maps to the column DISPLAY_FILE_NAME in the SYS_FILE table.
	 */
	protected String display_file_name;

	/** 
	 * This attribute maps to the column FILE_TYPE in the SYS_FILE table.
	 */
	protected String file_type;

	/** 
	 * This attribute maps to the column CREATEDDATE in the SYS_FILE table.
	 */
	protected Date createddate;

	/** 
	 * This attribute maps to the column ORDERCONDITION in the SYS_FILE table.
	 */
	protected String othercondition;
	
	protected boolean isdelete;

	public Long getFile_pk() {
		return file_pk;
	}

	public void setFile_pk(Long file_pk) {
		this.file_pk = file_pk;
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

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatoracct() {
		return creatoracct;
	}

	public void setCreatoracct(String creatoracct) {
		this.creatoracct = creatoracct;
	}

	public String getDisplay_file_name() {
		return display_file_name;
	}

	public void setDisplay_file_name(String display_file_name) {
		this.display_file_name = display_file_name;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public String getOthercondition() {
		return othercondition;
	}

	public void setOthercondition(String othercondition) {
		this.othercondition = othercondition;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
}
