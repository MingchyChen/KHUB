package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

public class SysUseDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4690743029115352175L;

	private Date createDate;//時間
	private String accountId;//使用者帳號
	private String accountName;//使用者姓名
	private String accountIp;//使用者IP
	private String title;//申請單位組室
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountIp() {
		return accountIp;
	}
	public void setAccountIp(String accountIp) {
		this.accountIp = accountIp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
