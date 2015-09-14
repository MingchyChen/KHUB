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
@Table(name = "webrellink")
public class WebRellink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6879833770296358331L;

	private String uuid;
	private String nameZhTw;// 連結名稱
	private int isDisplay;// 是否開啟1表是、0表否
	private int isBlank;// 連結開啟方式1:表開新視窗、0表本頁開啟
	private int sortNum;// 排序
	private String strurl;// 連結網址
	private int clickNum;// 點擊數
	private int isDataEffid;// 資料是否有效1表有效、0表無效
	private String latelyChangedUser;// 異動者
	private Date latelyChangedDate;// 異動時間
	// private String dataOwner;//建立者
	private WebEmployee webEmployee;
	private Date createdate;// 建立時間
	private String dataOwnerGroup;// 建立者群組
	private Integer menuType;// 選單樣式
	private Integer isezproxy;//是否使用ezproxy
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

	@Column(name = "isblank")
	public int getIsBlank() {
		return isBlank;
	}

	public void setIsBlank(int isBlank) {
		this.isBlank = isBlank;
	}

	@Column(name = "sortnum")
	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}

	public String getStrurl() {
		return strurl;
	}

	public void setStrurl(String strurl) {
		this.strurl = strurl;
	}

	@Column(name = "clicknum")
	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
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
	 * @Column(name="dataowner") public String getDataOwner() { return
	 * dataOwner; } public void setDataOwner(String dataOwner) { this.dataOwner
	 * = dataOwner; }
	 */
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	@Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(name = "dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

	@Column(name = "menutype")
	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getIsezproxy() {
		return isezproxy;
	}

	public void setIsezproxy(Integer isezproxy) {
		this.isezproxy = isezproxy;
	}

}
