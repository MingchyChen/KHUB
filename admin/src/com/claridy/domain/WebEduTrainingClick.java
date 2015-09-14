package com.claridy.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "webedutrainingclick")
public class WebEduTrainingClick {
	private String clickUuid;
	private Date visitTime;// 點擊時間
	private String visitIp;// 訪問IP
	private int issip;// 是否為館內的IP
	private String uuid;// 記錄
	private String webAccountUuid;

	@Id
	@Column(name = "clickuuid")
	public String getClickUuid() {
		return clickUuid;
	}

	public void setClickUuid(String clickUuid) {
		this.clickUuid = clickUuid;
	}

	@Column(name = "visittime")
	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	@Column(name = "visitip")
	public String getVisitIp() {
		return visitIp;
	}

	public void setVisitIp(String visitIp) {
		this.visitIp = visitIp;
	}

	@Column(name = "issip")
	public int getIssip() {
		return issip;
	}

	public void setIssip(int issip) {
		this.issip = issip;
	}

	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "webaccountuuid")
	public String getWebAccountUuid() {
		return webAccountUuid;
	}

	public void setWebAccountUuid(String webAccountUuid) {
		this.webAccountUuid = webAccountUuid;
	}

}
