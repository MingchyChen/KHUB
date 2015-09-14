package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webfunction_employee")
public class WebFunctionEmployee implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6583994995170217977L;
	private String uuid;
	private String funcUuid;
	private String employeesn;
	private String latelyChangeDuser;
	private Date latelyChangedDate;
	private String dataOwner;
	private Date createDate;
	private String dataOwnerGroup;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="func_uuid")
	public String getFuncUuid() {
		return funcUuid;
	}
	public void setFuncUuid(String funcUuid) {
		this.funcUuid = funcUuid;
	}
	
	
	public String getEmployeesn() {
		return employeesn;
	}
	public void setEmployeesn(String employeesn) {
		this.employeesn = employeesn;
	}
	
	@Column(name="latelychangeduser")
	public String getLatelyChangeDuser() {
		return latelyChangeDuser;
	}
	public void setLatelyChangeDuser(String latelyChangeDuser) {
		this.latelyChangeDuser = latelyChangeDuser;
	}
	
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@Column(name="dataowner")
	public String getDataOwner() {
		return dataOwner;
	}
	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
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
