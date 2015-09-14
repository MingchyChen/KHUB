package com.claridy.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Subselect("select uuid,matter_zh_tw,startdate as showDate,startdate,clicknum,isdataeffid,latelychangeddate,content_zh_tw,dataownergroup, 0 as typeOfErmNewEduTrinng,strurl,uploadnewsfile,displayname from webnews where isdataeffid=1 and startdate<=GETDATE() and (closedate>=GETDATE() or closedate is null) union all select uuid,matter_zh_tw,trainingdate as showDate,startdate,clicknum,isdataeffid,latelychangeddate,content_zh_tw,dataownergroup , 1 as typeOfErmNewEduTrinng,strurl,uploadnewsfile,displayname from webedutraining where isdataeffid=1")
@Synchronize({ "webnews", "webedutraining" })
public class ErmNewEduTrinng {
	private String uuid;// PK
	private String matterZhTw;// 標題
	private Date startDate;// 發佈日期
	private Date showDate;
	private Integer clickNum;// 點擊數
	private Integer isDataEffid;
	private Date latelychangeddate;
	private String contentZhTw;
	private WebOrg webOrg;
	private String typeOfErmNewEduTrinng;
	private String strUrl;
	private String upLoadNewsFile;
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

	@Column(name = "showDate")
	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	@Column(name = "clicknum")
	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	@Column(name = "isdataeffid")
	public Integer getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(Integer isDataEffid) {
		this.isDataEffid = isDataEffid;
	}

	@Column(name = "latelychangeddate")
	public Date getLatelychangeddate() {
		return latelychangeddate;
	}

	public void setLatelychangeddate(Date latelychangeddate) {
		this.latelychangeddate = latelychangeddate;
	}

	@Column(name = "content_zh_tw")
	public String getContentZhTw() {
		return contentZhTw;
	}

	public void setContentZhTw(String contentZhTw) {
		this.contentZhTw = contentZhTw;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "dataownergroup")
	public WebOrg getWebOrg() {
		return webOrg;
	}

	public void setWebOrg(WebOrg webOrg) {
		this.webOrg = webOrg;
	}

	/**
	 * @return the typeOfErmNewEduTrinng
	 */
	@Column(name = "typeOfErmNewEduTrinng")
	public String getTypeOfErmNewEduTrinng() {
		return typeOfErmNewEduTrinng;
	}

	/**
	 * @param typeOfErmNewEduTrinng
	 *            the typeOfErmNewEduTrinng to set
	 */
	public void setTypeOfErmNewEduTrinng(String typeOfErmNewEduTrinng) {
		this.typeOfErmNewEduTrinng = typeOfErmNewEduTrinng;
	}

	@Column(name = "strurl")
	public String getStrUrl() {
		return strUrl;
	}

	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}

	@Column(name = "uploadnewsfile")
	public String getUpLoadNewsFile() {
		return upLoadNewsFile;
	}

	public void setUpLoadNewsFile(String upLoadNewsFile) {
		this.upLoadNewsFile = upLoadNewsFile;
	}

	@Column(name = "startdate")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "displayname")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
