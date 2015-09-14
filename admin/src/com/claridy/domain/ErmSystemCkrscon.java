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


@Table
@Entity(name="erm_system_ckrscon")
public class ErmSystemCkrscon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838965918332867988L;
	
	private String checkItem;//每日進行偵測時間 00:00~23:30 每30分鐘為一單位
	private String gourpId;//收件群組 SYS_GROUP 農委會為weborg.orgid
	private String uuid;
	private Date latelyChangedDate;
	private WebEmployee webEmployee;
	private Date createDate;
	private String dataOwnerGroup;
	private Integer isdataEffid;
	
	@Column(name="checkitem")
	public String getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	
	@Column(name="group_id")
	public String getGourpId() {
		return gourpId;
	}
	
	public void setGourpId(String gourpId) {
		this.gourpId = gourpId;
	}
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}
	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}
	
	@ManyToOne( fetch =FetchType.LAZY , cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}
	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
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
	
	@Column(name="isdataeffid")
	public Integer getIsdataEffid() {
		return isdataEffid;
	}
	public void setIsdataEffid(Integer isdataEffid) {
		this.isdataEffid = isdataEffid;
	}
	
	

}
