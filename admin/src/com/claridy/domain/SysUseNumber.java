package com.claridy.domain;

import java.io.Serializable;

public class SysUseNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private String parentOrgName;//所屬單位名稱
	
	private String parentId;//所屬單位Id
	
	private String orgName;//組室名稱
	
	private String orgId;//組室Id
	
	private int parentSort;//單位排序
	
	private Integer newsNumber;//最新消息
	
	private Integer eduNumber;//教育訓練
	
	private Integer farmNumber;//農業新聞
	
	private Integer reportNumber;//研究報告
	
	private Integer pubNumber;//農業出版品

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public int getParentSort() {
		return parentSort;
	}

	public void setParentSort(int parentSort) {
		this.parentSort = parentSort;
	}

	public Integer getNewsNumber() {
		return newsNumber;
	}

	public void setNewsNumber(Integer newsNumber) {
		this.newsNumber = newsNumber;
	}

	public Integer getEduNumber() {
		return eduNumber;
	}

	public void setEduNumber(Integer eduNumber) {
		this.eduNumber = eduNumber;
	}

	public Integer getFarmNumber() {
		return farmNumber;
	}

	public void setFarmNumber(Integer farmNumber) {
		this.farmNumber = farmNumber;
	}

	public Integer getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(Integer reportNumber) {
		this.reportNumber = reportNumber;
	}

	public Integer getPubNumber() {
		return pubNumber;
	}

	public void setPubNumber(Integer pubNumber) {
		this.pubNumber = pubNumber;
	}
}
