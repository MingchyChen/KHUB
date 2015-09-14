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
@Table(name="webcooperation")
public class WebCooperation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1352293120469514336L;
	private String uuid;//申請案件單號
	private String dbid;//資料庫ID
	private String titleZhTw;//期刊名
	private String issn;//issn
	private Date publishDate;//出版日期
	private String volume;//卷號
	private String issue;//期號
	private String spage;//頁面起
	private String epage;//頁面迄
	private String atitle;//篇名
	private String pid;//作者
	private WebAccount applyAccount;//申請人
	private WebEmployee acceptEmployee;//處理者
	private int status;//處理狀態
	private String doi;//DOI
	private int isDataEffid;//資料是否有效
	private String latelyChangedUser;//異動者
	private Date latelyChangedDate;//異動時間
	private WebEmployee webEmployee;//建立者
	private Date createDate;//建立時間
	private String dataOwnerGroup;//建立者群組
	private String rejectReason;//駁回原因
	private String uploadFile;//上傳附件檔名
	private String downoadDbid;//下載資料庫
	private String displayName;
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	
	
	@Column(name="uploadfiledisplayname")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Column(name="title_zh_tw")
	public String getTitleZhTw() {
		return titleZhTw;
	}
	
	
	public void setTitleZhTw(String titleZhTw) {
		this.titleZhTw = titleZhTw;
	}
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	@Column(name="publishdate")
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	
	
	public String getSpage() {
		return spage;
	}
	public void setSpage(String spage) {
		this.spage = spage;
	}
	public String getEpage() {
		return epage;
	}
	public void setEpage(String epage) {
		this.epage = epage;
	}
	public String getAtitle() {
		return atitle;
	}
	public void setAtitle(String atitle) {
		this.atitle = atitle;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="accountid")
	public WebAccount getApplyAccount() {
		return applyAccount;
	}
	public void setApplyAccount(WebAccount applyAccount) {
		this.applyAccount = applyAccount;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="employeesn")
	public WebEmployee getAcceptEmployee() {
		return acceptEmployee;
	}
	public void setAcceptEmployee(WebEmployee acceptEmployee) {
		this.acceptEmployee = acceptEmployee;
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
	
	@ManyToOne(fetch = FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE})
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
	
	
	@Column(name="rejectreason")
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	
	
	@Column(name="uploadfile")
	public String getUploadFile() {
		return uploadFile;
	}
	
	
	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}
	
	@Column(name="downoaddbid")
	public String getDownoadDbid() {
		return downoadDbid;
	}
	public void setDownoadDbid(String downoadDbid) {
		this.downoadDbid = downoadDbid;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	
	
	
	
}
