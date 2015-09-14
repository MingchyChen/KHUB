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
@Table(name="webaccount")
public class WebAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2889570145714332793L;
	private String uuid;//主鍵
	private String accountId;//帳號
	private String parentorgid;//單位別web.orgid
	private String parentOrgName; //单位别名字
	private String orgid;//組室web.orgid
	private String orgName; //組室名
	private String nameZhTw;//姓名
	private String pwd;//密碼
	private String email;//E-Mail
	private String tel;//電話
	private String titleZhTw;//職稱
	private int status;//狀態1為啟用、0為停用、
	private int isDataEffid;//資料是否有效1表有效、0表無效
	private int type;//身分別1農委會管理者、2為各所屬單位管理者、3為一般使用者(研究人員)
	private String accountPic;//大頭照檔名
	private String approvePeople;//審核人
	private Date approveDate;//審核日期
	
	private String latelyChangedUser;//異動者
	private Date latelyChangedDate;//異動時間
	private WebEmployee webEmployee;//建立者
	private Date createDate;//建立時間
	private String dataOwnerGroup;//建立者群組
	private Date loginDate;//最後上線時間
	private int isCheck;//是否審核0審核中， 1審核通過、2駁回
	private Integer isRegister;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="accountid")
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	@Column(name="approvepeople")
	public String getApprovePeople() {
		return approvePeople;
	}
	public void setApprovePeople(String approvePeople) {
		this.approvePeople = approvePeople;
	}
	
	@Column(name="approveDate")
	public Date getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	@Transient
	public String getParentOrgName() {
		return parentOrgName;
	}
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	@Transient
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getParentorgid() {
		return parentorgid;
	}
	public void setParentorgid(String parentorgid) {
		this.parentorgid = parentorgid;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getPwd() {
		return pwd;
	}
	
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	@Column(name="title_zh_tw")
	public String getTitleZhTw() {
		return titleZhTw;
	}
	public void setTitleZhTw(String titleZhTw) {
		this.titleZhTw = titleZhTw;
	}
	
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	@Column(name="accountpic")	
	public String getAccountPic() {
		return accountPic;
	}
	public void setAccountPic(String accountPic) {
		this.accountPic = accountPic;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	@Column(name="ischeck")
	public int getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	
	@Column(name="logindate")
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} ) 
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}
	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}
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
	@Column(name="name_zh_tw")
	public String getNameZhTw() {
		return nameZhTw;
	}
	public void setNameZhTw(String nameZhTw) {
		this.nameZhTw = nameZhTw;
	}
	
	@Column(name="isregister")
	public Integer getIsRegister() {
		return isRegister;
	}
	public void setIsRegister(Integer isRegister) {
		this.isRegister = isRegister;
	}
	
	
	
	
}
