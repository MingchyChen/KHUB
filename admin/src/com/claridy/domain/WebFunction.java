package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webfunction")
public class WebFunction  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7568280317918625828L;
	private String uuid;
	private String mutiFuncId;
	private String funcName;
	private String funcParent;
	private int seq;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private String creator;
	private Date createDate;
	private String functionClass;
	private String url;
	private String languageArea;
	private int isDataEffid;
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="muti_func_id")
	public String getMutiFuncId() {
		return mutiFuncId;
	}
	public void setMutiFuncId(String mutiFuncId) {
		this.mutiFuncId = mutiFuncId;
	}
	@Column(name="func_name")
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	@Column(name="func_parent")
	public String getFuncParent() {
		return funcParent;
	}
	public void setFuncParent(String funcParent) {
		this.funcParent = funcParent;
	}
	
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	@Column(name="latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}
	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}
	
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@Column(name="function_class")
	public String getFunctionClass() {
		return functionClass;
	}
	public void setFunctionClass(String functionClass) {
		this.functionClass = functionClass;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name="languagearea")
	public String getLanguageArea() {
		return languageArea;
	}
	public void setLanguageArea(String languageArea) {
		this.languageArea = languageArea;
	}
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	
}
