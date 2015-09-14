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
@Table(name = "webnews")
public class ErmNews implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3606881308596734452L;
	private String uuid;// PK
	private String matterZhTw;// 標題
	private Integer isTop;// 是否置頂 1是 0否
	private String upLoadNewFile;// 上傳檔案名稱
	private String contentZhTw;// 內容
	private Integer clickNum;// 點擊數
	private Integer sortNum;// 排序
	private Date startDate;// 發佈日期
	private Date closeDate;// 下架日期
	private String strUrl;// 參考網址
	private String latelyChangedUser;// 移動者
	// private String dataOwner; //建立者
	private WebEmployee webEmployee;
	private String dataOwnerGroup;// 建立者群組
	private Date createDate;// 建立時間
	private int isDataEffid;// 資料是否有效1表有效、0表無效
	private Date latelyChangedDate;// 移動時間
	private String displayName;
	@Id
	@Column(name="uuid")
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
	@Column(name="istop")
	public Integer getIsTop() {
		return isTop;
	}
	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	@Column(name="uploadnewsfile")
	public String getUpLoadNewFile() {
		return upLoadNewFile;
	}
	public void setUpLoadNewFile(String upLoadNewFile) {
		this.upLoadNewFile = upLoadNewFile;
	}
	@Column(name="content_zh_tw")
	public String getContentZhTw() {
		return contentZhTw;
	}
	public void setContentZhTw(String contentZhTw) {
		this.contentZhTw = contentZhTw;
	}
	@Column(name="clicknum")
	public Integer getClickNum() {
		return clickNum;
	}
	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}
	@Column(name="sortnum")
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	@Column(name="startdate")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name="closedate")
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	@Column(name="strurl")
	public String getStrUrl() {
		return strUrl;
	}
	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}
	@Column(name="latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}
	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}
	@ManyToOne(fetch = FetchType.LAZY,  cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
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
	@Column(name="displayname")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
}
