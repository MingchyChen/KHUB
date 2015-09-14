package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "webpublicationsclick")
public class WebPublicationsClick  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4619880682897401841L;

	private String clickuuid;// 主鍵
	private Date visitTime; // 點擊時間
	private String visitIp;// 訪問IP
	private int issIp;// 是否為館內IP、0表館外、1表館內
	private String uuid;// 記錄webpublication.uuid
	private String webAccountUuid;//

	@Id
	public String getClickuuid() {
		return clickuuid;
	}

	@Column(name = "visittime")
	public Date getVisitTime() {
		return visitTime;
	}

	@Column(name = "visitip")
	public String getVisitIp() {
		return visitIp;
	}

	@Column(name = "issip")
	public int getIssIp() {
		return issIp;
	}

	public String getUuid() {
		return uuid;
	}

	@Column(name = "account_uuid")
	public String getWebAccountUuid() {
		return webAccountUuid;
	}

	/**
	 * @param clickuuid the clickuuid to set
	 */
	public void setClickuuid(String clickuuid) {
		this.clickuuid = clickuuid;
	}

	/**
	 * @param visitTime the visitTime to set
	 */
	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	/**
	 * @param visitIp the visitIp to set
	 */
	public void setVisitIp(String visitIp) {
		this.visitIp = visitIp;
	}

	/**
	 * @param issIp the issIp to set
	 */
	public void setIssIp(int issIp) {
		this.issIp = issIp;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @param webAccountUuid the webAccountUuid to set
	 */
	public void setWebAccountUuid(String webAccountUuid) {
		this.webAccountUuid = webAccountUuid;
	}

	 
}
