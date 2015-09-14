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
@Table(name = "webedutraining")
public class WebEduTraining implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2501975847336310575L;

	private String uuid;
	private String matterZhTw;// 標題
	private String uploadNewsFile;// 上傳檔案
	private String conTextZhTw;// 內容
	private int clickNum;// 點擊數
	private Date startDate;// 發佈時間
	private Date closeDate;//下架日期
	private String strurl;// 參考網址
	private String latelyChangedUser;// 異動者
	private WebEmployee webEmployee;// 建立者
	private String dataOwnerGroup;// 建立者群組
	private Date createDate;// 建立時間
	private int isDataEffid;// 資料是否有效1表有效，0表示無效
	private Date latelyChangedDate;// 異動時間
	private Date trainingDate;
	private String displayName; 
	
	@Id
	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "matter_zh_tw")
	public String getMatterZhTw() {
		return matterZhTw;
	}

	public void setMatterZhTw(String matterZhTw) {
		this.matterZhTw = matterZhTw;
	}

	@Column(name = "uploadnewsfile")
	public String getUploadNewsFile() {
		return uploadNewsFile;
	}

	public void setUploadNewsFile(String uploadNewsFile) {
		this.uploadNewsFile = uploadNewsFile;
	}

	@Column(name = "content_zh_tw")
	public String getConTextZhTw() {
		return conTextZhTw;
	}

	public void setConTextZhTw(String conTextZhTw) {
		this.conTextZhTw = conTextZhTw;
	}

	@Column(name = "clicknum")
	public int getClickNum() {
		return clickNum;
	}

	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}

	@Column(name = "startdate")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name = "closedate")
	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getStrurl() {
		return strurl;
	}

	public void setStrurl(String strurl) {
		this.strurl = strurl;
	}

	@Column(name = "latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}

	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}

	@Column(name = "dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

	@Column(name = "createdate")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "isdataeffid")
	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}

	@Column(name = "latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}

	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}

	@Column(name = "trainingdate")
	public Date getTrainingDate() {
		return trainingDate;
	}

	public void setTrainingDate(Date trainingDate) {
		this.trainingDate = trainingDate;
	}
	@Column(name = "displayname")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
