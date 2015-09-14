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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="webemployee")
public class WebEmployee implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -973189718264239730L;
	private String employeesn;//ID
	private String employeeName;//姓名s
	private String employeeId;//帳號
	private WebOrg parentWebOrg; //單位別
	private WebOrg weborg;//組室
	private String tel;//聯絡電話
	private String email;//e-mail
	private String pwd; //密碼
	private String dataOwner;//建立者
	private String dataOwnerGroup;//建立者群組
	private Date createDate;//建立時間
	private Date latelyChangedDate;//異動時間
	private int isLock;//是否停用1表停用0表啟用
	private int isDataEffid;//資料是否有效1表有效、0表無效
	private String latelyChangeDuser;//異動者
	private int idType;//身分別1表農委會管理者2表各所屬單位管理者3表一般使用者(研究人員)
	private int isManager;//是否為部門主管1表是0表否
	private Date loginDate;//最後登入時間
	private Integer isnewstop;//是否有最新消息置頂權限1表示是、0表否
	
	
	
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	@JoinColumn(name="parentorgid")
	@NotFound(action=NotFoundAction.IGNORE)
	public WebOrg getParentWebOrg() {
		return parentWebOrg;
	}

	public void setParentWebOrg(WebOrg parentWebOrg) {
		this.parentWebOrg = parentWebOrg;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} ) 
	@JoinColumn(name="orgid")
	@NotFound(action=NotFoundAction.IGNORE)
	public WebOrg getWeborg() {
		return weborg;
	}

	public void setWeborg(WebOrg weborg) {
		this.weborg = weborg;
	}
	@Id
	public String getEmployeesn() {
		return employeesn;
	}
	
	

	public void setEmployeesn(String employeesn) {
		this.employeesn = employeesn;
	}
	
	@Column(name="employeename")
	public String getEmployeeName() {
		return employeeName;
	}
	
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	@Column(name="employeeid")
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
	
	@Column(name="dataowner")
	public String getDataOwner() {
		return dataOwner;
	}
	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
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
	
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@Column(name="islock")
	public int getIsLock() {
		return isLock;
	}
	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}
	
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	
	@Column(name="latelychangeduser")
	public String getLatelyChangeDuser() {
		return latelyChangeDuser;
	}
	public void setLatelyChangeDuser(String latelyChangeDuser) {
		this.latelyChangeDuser = latelyChangeDuser;
	}
	
	@Column(name="idtype")
	public int getIdType() {
		return idType;
	}
	public void setIdType(int idType) {
		this.idType = idType;
	}
	
	@Column(name="ismanager")
	public int getIsManager() {
		return isManager;
	}
	public void setIsManager(int isManager) {
		this.isManager = isManager;
	}
	
	@Column(name="logindate")
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Integer getIsnewstop() {
		return isnewstop;
	}

	public void setIsnewstop(Integer isnewstop) {
		this.isnewstop = isnewstop;
	}
}
