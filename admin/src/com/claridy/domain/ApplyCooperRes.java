package com.claridy.domain;

import java.io.Serializable;

public class ApplyCooperRes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private String parentOrgName;//提供單位名稱
	
	private String parentId;//提供單位ID
	
	private int parentSort;//提供單位排序
	
	private String dbName;//資料庫名稱
	
	private String dbId;//資料庫ID
	
	private Integer applyNumber;//申請次數
	
	private Integer nuclearNumber;//核可次數
	
	private Integer rejectedNumber;//駁回次數
	
	private Integer untreatedNumber;//未處理次數

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getParentSort() {
		return parentSort;
	}

	public void setParentSort(int parentSort) {
		this.parentSort = parentSort;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public Integer getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(Integer applyNumber) {
		this.applyNumber = applyNumber;
	}

	public Integer getNuclearNumber() {
		return nuclearNumber;
	}

	public void setNuclearNumber(Integer nuclearNumber) {
		this.nuclearNumber = nuclearNumber;
	}

	public Integer getRejectedNumber() {
		return rejectedNumber;
	}

	public void setRejectedNumber(Integer rejectedNumber) {
		this.rejectedNumber = rejectedNumber;
	}

	public Integer getUntreatedNumber() {
		return untreatedNumber;
	}

	public void setUntreatedNumber(Integer untreatedNumber) {
		this.untreatedNumber = untreatedNumber;
	}

}
