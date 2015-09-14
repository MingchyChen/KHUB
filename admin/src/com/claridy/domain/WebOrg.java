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
@Table(name="weborg")
public class WebOrg implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1352293120469514336L;
	private String orgId;
	private String orgName;
	private String orgIdParent;
	private String latelyChangedUser;
	private WebEmployee webEmployee;
	//private String dataOwner;
	private String dataOwnerGroup;
	private Date createDate;
	private int isDataEffid;
	private Date latelyChangedDate;
	private int isAuth;
	private int sort;
	
	@Id
	@Column(name="orgid")
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name="orgname")
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Column(name="orgid_parent")
	public String getOrgIdParent() {
		return orgIdParent;
	}
	public void setOrgIdParent(String orgIdParent) {
		this.orgIdParent = orgIdParent;
	}
	
	@Column(name="latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}
	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}
	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}
	/*@Column(name="dataowner")
	public String getDataOwner() {
		return dataOwner;
	}
	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}*/
	
	@Column(name="dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}
	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}
	
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@Column(name="isauth")
	public int getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(int isAuth) {
		this.isAuth = isAuth;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
