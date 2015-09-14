package com.claridy.domain;

import java.io.Serializable;

public class SelUseNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private String parentOrgName;//所屬單位名稱
	
	private Integer parentSort;//單位排序
	
	private Integer titleNumber;//全部
	
	private Integer summonNumber;//文獻探索
	
	private Integer museNumber;//整合查詢
	
	private Integer ermNumber;//電子資源

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public Integer getTitleNumber() {
		return titleNumber;
	}

	public void setTitleNumber(Integer titleNumber) {
		this.titleNumber = titleNumber;
	}

	public Integer getSummonNumber() {
		return summonNumber;
	}

	public Integer getParentSort() {
		return parentSort;
	}

	public void setParentSort(Integer parentSort) {
		this.parentSort = parentSort;
	}

	public void setSummonNumber(Integer summonNumber) {
		this.summonNumber = summonNumber;
	}

	public Integer getMuseNumber() {
		return museNumber;
	}

	public void setMuseNumber(Integer museNumber) {
		this.museNumber = museNumber;
	}

	public Integer getErmNumber() {
		return ermNumber;
	}

	public void setErmNumber(Integer ermNumber) {
		this.ermNumber = ermNumber;
	}

}
