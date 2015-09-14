package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

public class AccountApplyDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private Date createDate;//時間
	private String applyAccountId;//申請帳號
	private String applyAccountName;//申請姓名
	private String applyParentOrgName;//申請單位
	private String applyOrgName;//申請單位組室
	private String checkResult;//審核結果
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getApplyAccountId() {
		return applyAccountId;
	}
	public void setApplyAccountId(String applyAccountId) {
		this.applyAccountId = applyAccountId;
	}
	public String getApplyAccountName() {
		return applyAccountName;
	}
	public void setApplyAccountName(String applyAccountName) {
		this.applyAccountName = applyAccountName;
	}
	public String getApplyParentOrgName() {
		return applyParentOrgName;
	}
	public void setApplyParentOrgName(String applyParentOrgName) {
		this.applyParentOrgName = applyParentOrgName;
	}
	public String getApplyOrgName() {
		return applyOrgName;
	}
	public void setApplyOrgName(String applyOrgName) {
		this.applyOrgName = applyOrgName;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
}
