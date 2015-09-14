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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "weberwsourceunit")
public class WebErwSourceUnit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7118986327816005383L;
	@Id
	private String uuid;// id
	@ManyToOne()
	@JoinColumn(name = "weberwsourceuuid", referencedColumnName = "uuid")
	private WebErwSource webErwSource;// 資料庫Weberwsource.uuid資料庫key
	@ManyToOne()
	@JoinColumn(name = "weborgorgid", referencedColumnName = "orgId")
	private WebOrg webOrgOrg;// 採購單位Weborg.orgid單位key
	@ManyToOne()
	@JoinColumn(name = "webemployeesn", referencedColumnName = "employeesn")
	@Fetch(FetchMode.SELECT)
	@Where(clause="webEmployeesn.isDataEffid = 1")
	private WebEmployee webEmployeesn;// 處理人員Webemployee.employees管理者key
	@Column(name = "iscooperation")
	private int isCooperation;// 是否提供館核1表是、0表否
	@Column(name = "roundnum")
	private int roundNum;// 處理順序
	@Column(name = "isdataeffid")
	private int isDataEffid;// 資料是否有效1表有效、0表無效
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="latelychangeduser")
	private WebEmployee latelyChangeDuser;// 異動者
	@Column(name = "latelychangeddate")
	private Date latelyChangedDate;// 異動時間
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="dataowner")
	private WebEmployee dataOwner;// 建立者
	@Column(name = "createdate")
	private Date createDate;// 建立時間
	@Column(name = "dataownergroup")
	private String dataOwnerGroup;// 建立者群組

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public WebErwSource getWebErwSource() {
		return webErwSource;
	}

	public void setWebErwSource(WebErwSource webErwSource) {
		this.webErwSource = webErwSource;
	}

	public WebOrg getWebOrgOrg() {
		return webOrgOrg;
	}

	public void setWebOrgOrg(WebOrg webOrgOrg) {
		this.webOrgOrg = webOrgOrg;
	}

	public WebEmployee getWebEmployeesn() {
		return webEmployeesn;
	}

	public void setWebEmployeesn(WebEmployee webEmployeesn) {
		this.webEmployeesn = webEmployeesn;
	}

	public int getIsCooperation() {
		return isCooperation;
	}

	public void setIsCooperation(int isCooperation) {
		this.isCooperation = isCooperation;
	}

	public int getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}

	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}

	public WebEmployee getLatelyChangeDuser() {
		return latelyChangeDuser;
	}

	public void setLatelyChangeDuser(WebEmployee latelyChangeDuser) {
		this.latelyChangeDuser = latelyChangeDuser;
	}

	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}

	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}

	public WebEmployee getDataOwner() {
		return dataOwner;
	}

	public void setDataOwner(WebEmployee dataOwner) {
		this.dataOwner = dataOwner;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

}
