package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webresearchreportsclick")
public class WebResearchReportsClick implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7174510342665856058L;
	private String clickUuid;
	private Date visitTime;
	private String visitIp;
	private int issIp;
	private String uuid;
	private String webaccountUuid;
	
	@Id
	@Column(name="clickuuid")
	public String getClickUuid() {
		return clickUuid;
	}
	public void setClickUuid(String clickUuid) {
		this.clickUuid = clickUuid;
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
	
	@Column(name="webaccountuuid")
	public String getWebaccountUuid() {
		return webaccountUuid;
	}
	public void setWebaccountUuid(String webaccountUuid) {
		this.webaccountUuid = webaccountUuid;
	}
	
	
	
}
