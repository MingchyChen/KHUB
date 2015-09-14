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
@Table(name="webadwall")
public class WebAdwall  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7568280317918625828L;
	private String uuid;  
	private String adnameZhTw;  
	private Integer isblank;
	private Integer isdisplay;
	private Integer sortnum;
	private Integer clicknum;
	private String strurl;
	private String filellink;
	private Date startdate;
	private Date enddate;
	private String descZhTw;
	private String descEnUs;
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;

	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name = "adname_zh_tw")
	public String getAdnameZhTw() {
		return adnameZhTw;
	}
	public void setAdnameZhTw(String adnameZhTw) {
		this.adnameZhTw = adnameZhTw;
	}
	public Integer getIsblank() {
		return isblank;
	}
	public void setIsblank(Integer isblank) {
		this.isblank = isblank;
	}
	public Integer getIsdisplay() {
		return isdisplay;
	}
	public void setIsdisplay(Integer isdisplay) {
		this.isdisplay = isdisplay;
	}
	public Integer getSortnum() {
		return sortnum;
	}
	public void setSortnum(Integer sortnum) {
		this.sortnum = sortnum;
	}
	public Integer getClicknum() {
		return clicknum;
	}
	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}
	public String getStrurl() {
		return strurl;
	}
	public void setStrurl(String strurl) {
		this.strurl = strurl;
	}
	public String getFilellink() {
		return filellink;
	}
	public void setFilellink(String filellink) {
		this.filellink = filellink;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	@Column(name = "desc_zh_tw")
	public String getDescZhTw() {
		return descZhTw;
	}
	public void setDescZhTw(String descZhTw) {
		this.descZhTw = descZhTw;
	}
	@Column(name = "desc_en_us")
	public String getDescEnUs() {
		return descEnUs;
	}
	public void setDescEnUs(String descEnUs) {
		this.descEnUs = descEnUs;
	}
	@ManyToOne(fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	@Column(name = "dataownergroup")
	public String getDataOwnerGroup() {
		return dataOwnerGroup;
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

	@Column(name = "latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}

	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
	}

	@Column(name = "latelychangeddate")
	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}

	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}

	@Column(name = "isdataeffid")
	public Integer getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(Integer isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
}
