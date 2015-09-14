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

@Entity
@Table(name = "webpersonalizefavorites")
public class webPersonalizeFavorites implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private WebAccount webAccount;//申請人IDwebaccount.accountid
	private String classId;//分類編號:申請人帳號+日期+4碼流水號PK
	private String classIdZhTw;//分類資料夾名稱
	private String classIdParent;//父階分類編號
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private int isDataEffid;//資料是否有效1表有效、0表無效

	/**
	 * @return the item_id
	 */
	@Id
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "accountid")
	public WebAccount getWebAccount() {
		return webAccount;
	}

	public void setWebAccount(WebAccount webAccount) {
		this.webAccount = webAccount;
	}
	@Id
	@Column(name = "classid")
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	@Column(name = "classid_zh_tw")
	public String getClassIdZhTw() {
		return classIdZhTw;
	}

	public void setClassIdZhTw(String classIdZhTw) {
		this.classIdZhTw = classIdZhTw;
	}
	@Column(name = "classid_parent")
	public String getClassIdParent() {
		return classIdParent;
	}

	public void setClassIdParent(String classIdParent) {
		this.classIdParent = classIdParent;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
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
}
