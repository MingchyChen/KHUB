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
@Table(name="webnoticetemplates")
public class WebNoticeTemplates implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2889570145714332793L;
	private String uuid;//主鍵
	private String latelyChangedUser;//異動者
	private Date latelyChangedDate;//異動時間
	private WebEmployee webEmployee;
	//private String dataOwner;//建立者
	private String dataOwnerGroup;//建立者群組
	private Date createDate;//建立時間
	private String nameZhTw;//通知單名稱(繁體中文)
	private int isopen;//通知單預設 - 0:關閉 1:開啟
	private String mailsubjectZhTw;//通知單主題(繁體中文)
	private String contentZhTw;//通知單內容(繁體中文)
	private String  remarks;//備註
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	@Column(name="name_zh_tw")
	public String getNameZhTw() {
		return nameZhTw;
	}
	public void setNameZhTw(String nameZhTw) {
		this.nameZhTw = nameZhTw;
	}
	public int getIsopen() {
		return isopen;
	}
	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}
	@Column(name="mailsubject_zh_tw")
	public String getMailsubjectZhTw() {
		return mailsubjectZhTw;
	}
	public void setMailsubjectZhTw(String mailsubjectZhTw) {
		this.mailsubjectZhTw = mailsubjectZhTw;
	}
	@Column(name="content_zh_tw")
	public String getContentZhTw() {
		return contentZhTw;
	}
	public void setContentZhTw(String contentZhTw) {
		this.contentZhTw = contentZhTw;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
