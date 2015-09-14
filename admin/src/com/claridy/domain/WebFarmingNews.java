package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webfarmingnews")
public class WebFarmingNews implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3185951318716527270L;
	private String uuid;
	private String ownUnitId;//單位ID
	private String ownUnitName;//單位名稱
	private Date postDate;//發佈日期
	private String subjectId;//主題ID
	private String subjectName;//主題名稱
	private String title;//標題
	private String url;//url
	private Integer clickNum;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="ownunitid")
	public String getOwnUnitId() {
		return ownUnitId;
	}
	public void setOwnUnitId(String ownUnitId) {
		this.ownUnitId = ownUnitId;
	}
	
	@Column(name="ownunitname")
	public String getOwnUnitName() {
		return ownUnitName;
	}
	public void setOwnUnitName(String ownUnitName) {
		this.ownUnitName = ownUnitName;
	}
	
	@Column(name="postdate")
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	
	@Column(name="subjectid")
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	@Column(name="subjectname")
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="clicknum")
	public Integer getClickNum() {
		return clickNum;
	}
	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}
	
	
	
	
	
}
