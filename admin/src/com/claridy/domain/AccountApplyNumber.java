package com.claridy.domain;

import java.io.Serializable;

public class AccountApplyNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private String parentOrgName;//所屬單位名稱
	
	private String parentId;//所屬單位Id
	
	private String orgName;//組室名稱
	
	private String orgId;//組室Id
	
	private int parentSort;//單位排序
	
	private Integer applyNumber;//帳號申請量

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

	public Integer getApplyNumber() {
		return applyNumber;
	}

	public void setApplyNumber(Integer applyNumber) {
		this.applyNumber = applyNumber;
	}

	public int getParentSort() {
		return parentSort;
	}

	public void setParentSort(int parentSort) {
		this.parentSort = parentSort;
	}

}
