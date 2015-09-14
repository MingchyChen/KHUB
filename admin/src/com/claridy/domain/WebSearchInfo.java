package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "websearchinfo")
public class WebSearchInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4854823015071568379L;

	private String uuid;
	private String nameZhTw;// 主題名稱
	private int isDisplay;// 是否開啟
	private int sortNum;// 排序
	private int clickNum;// 點擊數
	private String contentZhTw;// 內容
	private int isDataEffid;// 是否有效
	private String latelyChangedUser;// 異動者
	private Date latelyChangedDate;// 異動時間
	// private String dataOwner;// 建立者
	private WebEmployee webEmployee;
	private Date createDate;// 建立時間

	private String dataOwnerGroup;// 建立者群組

	@Id
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "name_zh_tw")
	public String getNameZhTw() {
		return nameZhTw;
	}

	public void setNameZhTw(String nameZhTw) {
		this.nameZhTw = nameZhTw;
	}

	@Column(name = "isdisplay")
	public int getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}

	@Column(name = "sortnum")
	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	@Column(name = "clicknum")
	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}

	@Column(name = "content_zh_tw")
	public String getContentZhTw() {
		return contentZhTw;
	}

	public void setContentZhTw(String contentZhTw) {
		this.contentZhTw = contentZhTw;
	}

	@Column(name = "isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
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

	/*
	 * @Column(name = "dataowner") public String getDataOwner() { return
	 * dataOwner; }
	 * 
	 * public void setDataOwner(String dataOwner) { this.dataOwner = dataOwner;
	 * }
	 */
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	@Column(name = "createdate")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
