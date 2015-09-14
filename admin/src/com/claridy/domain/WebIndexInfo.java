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
@Table(name="webindexinfo")
public class WebIndexInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4619880682897401841L;
	
	private String uuid;
	private String matterZhTw;//版面名稱
	private int isDisplay;//是否顯示1表是、0表否
	private String uploadNewsFile;//上傳檔案名稱
	private String newsContentZhTw;//版面內容
	private int isDataEffid;//資料是否有	效1表有效、0表無效
	private String latelyChangedUser;//異動者
	private Date latelyChangedDate;//異動時間
	private WebEmployee webEmployee;
	//private String dataOwner;//建立者
	private Date createDate;//建立時間
	private String dataOwnerGroup;//建立者群組
	private String uploadFileDisplayName;
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="matter_zh_tw")
	public String getMatterZhTw() {
		return matterZhTw;
	}
	public void setMatterZhTw(String matterZhTw) {
		this.matterZhTw = matterZhTw;
	}
	@Column(name="isdisplay")
	public int getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
	
	@Column(name="uploadfiledisplayname")
	public String getUploadFileDisplayName() {
		return uploadFileDisplayName;
	}
	public void setUploadFileDisplayName(String uploadFileDisplayName) {
		this.uploadFileDisplayName = uploadFileDisplayName;
	}
	@Column(name="uploadnewsfile")
	public String getUploadNewsFile() {
		return uploadNewsFile;
	}
	public void setUploadNewsFile(String uploadNewsFile) {
		this.uploadNewsFile = uploadNewsFile;
	}
	@Column(name="newscontent_zh_tw")
	public String getNewsContentZhTw() {
		return newsContentZhTw;
	}
	public void setNewsContentZhTw(String newsContentZhTw) {
		this.newsContentZhTw = newsContentZhTw;
	}
	@Column(name="isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
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
