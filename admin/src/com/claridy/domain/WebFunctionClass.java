package com.claridy.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webfunction_class")
public class WebFunctionClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4772773750187607750L;
	private String uuid;
	private String classId;
	private String className;
	private String classRemark;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="class_id")
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	@Column(name="class_name")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Column(name="class_remark")
	public String getClassRemark() {
		return classRemark;
	}
	public void setClassRemark(String classRemark) {
		this.classRemark = classRemark;
	}
	
	
}
