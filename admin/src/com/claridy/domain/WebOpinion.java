/**
 * ERMSystemSetting.java
 *
 * @author RMB
 */

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
import javax.persistence.Transient;

@Entity
@Table(name = "webopinion")
public class WebOpinion implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private WebAccount webAccount;//申請人IDwebaccount.accountid
	private String titleZhTw;//標題
	private String contentZhTw;//內容
	private String isfaq;//是否轉入FAQ
	private String dataowner;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private int isDataEffid;//資料是否有效1表有效、0表無效
	private String type;//是否回覆
	private String createUser;
	/**
	 * @return the item_id
	 */
	@Id
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "accountid")
	public WebAccount getWebAccount() {
		return webAccount;
	}

	public void setWebAccount(WebAccount webAccount) {
		this.webAccount = webAccount;
	}

	@Column(name = "title_zh_tw")
	public String getTitleZhTw() {
		return titleZhTw;
	}

	public void setTitleZhTw(String titleZhTw) {
		this.titleZhTw = titleZhTw;
	}
	@Column(name = "content_zh_tw")
	public String getContentZhTw() {
		return contentZhTw;
	}

	public void setContentZhTw(String contentZhTw) {
		this.contentZhTw = contentZhTw;
	}

	public String getIsfaq() {
		return isfaq;
	}

	public void setIsfaq(String isfaq) {
		this.isfaq = isfaq;
	}

	public String getDataowner() {
		return dataowner;
	}

	public void setDataowner(String dataowner) {
		this.dataowner = dataowner;
	}

	@Column(name = "dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

	@Column(name = "createdate")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}

	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}

	@Column(name = "latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}

	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}

	@Column(name = "isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	
	@Transient
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Transient
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
}
