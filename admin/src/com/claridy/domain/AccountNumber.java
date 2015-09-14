package com.claridy.domain;

import java.io.Serializable;

public class AccountNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private String parentOrgName;// 所屬單位名稱

	private String pUrl;// 所屬單位URL

	private Integer loginNumber;// 登入量

	private String lUrl;// 登入量URL

	private Integer cooperNumber;// 館合申請量

	private String cUrl;// 館合申請量URL

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getpUrl() {
		return pUrl;
	}

	public void setpUrl(String pUrl) {
		this.pUrl = pUrl;
	}

	public Integer getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(Integer loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getlUrl() {
		return lUrl;
	}

	public void setlUrl(String lUrl) {
		this.lUrl = lUrl;
	}

	public Integer getCooperNumber() {
		return cooperNumber;
	}

	public void setCooperNumber(Integer cooperNumber) {
		this.cooperNumber = cooperNumber;
	}

	public String getcUrl() {
		return cUrl;
	}

	public void setcUrl(String cUrl) {
		this.cUrl = cUrl;
	}

}
