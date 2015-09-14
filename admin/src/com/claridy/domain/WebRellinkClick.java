package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="webrellinkclick")
public class WebRellinkClick implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1862800034680746526L;
	
	private String clickuuid;//主鍵
	private Date visitTime; //點擊時間
	private String visitIp;//訪問IP
	private int issIp;//是否為館內IP、0表館外、1表館內
	private String uuid;//記錄webrellink.uuid
	private String webAccountUuid;//
	
	@Id
	public String getClickuuid() {
		return clickuuid;
	}
	public void setClickuuid(String clickuuid) {
		this.clickuuid = clickuuid;
	}
	@Column(name="visittime")
	public Date getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}
	@Column(name="visitip")
	public String getVisitIp() {
		return visitIp;
	}
	public void setVisitIp(String visitIp) {
		this.visitIp = visitIp;
	}
	@Column(name="issip")
	public int getIssIp() {
		return issIp;
	}
	public void setIssIp(int issIp) {
		this.issIp = issIp;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="account_uuid")	
	public String getWebAccountUuid() {
		return webAccountUuid;
	}
	public void setWebAccountUuid(String webAccountUuid) {
		this.webAccountUuid = webAccountUuid;
	}
	

}
