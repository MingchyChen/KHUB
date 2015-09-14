package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="webphrase")
public class WebPhrase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8984427264846525079L;
	private String uuid;
	private String phraseZhTw;
	private int isDataEffid;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private WebEmployee dataOwnerWeb;
	private Date createDate;
	private String dataOwnerGroup;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="phrase_zh_tw")
	public String getPhraseZhTw() {
		return phraseZhTw;
	}
	public void setPhraseZhTw(String phraseZhTw) {
		this.phraseZhTw = phraseZhTw;
	}
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	@Column(name="latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}
	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="dataowner")
	public WebEmployee getDataOwnerWeb() {
		return dataOwnerWeb;
	}
	public void setDataOwnerWeb(WebEmployee dataOwnerWeb) {
		this.dataOwnerWeb = dataOwnerWeb;
	}
	
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}
	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}
	
	
	
	
}
