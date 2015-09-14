/**
 * ERMSystemSetting.java
 *
 * @author RMB
 */

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
import javax.persistence.Transient;

@Entity
@Table(name = "webfaq")
public class WebFaq implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private String titleZhTw;
	private String titleEnUs;
	private Integer isdisplay;
	private Integer sortnum;
	private String contextlobEnUs;
	private String contextlobZhTw;
	private String answerEnUs;
	private String answerZhTw;
	private String uploadnewsfile;
	private Integer clicknum;
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
	private WebFaqType webFaqType;
	
	/**
	 * @return the item_id
	 */
	@Id
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name = "title_zh_tw")
	public String getTitleZhTw() {
		return titleZhTw;
	}

	public void setTitleZhTw(String titleZhTw) {
		this.titleZhTw = titleZhTw;
	}
	@Column(name = "title_en_us")
	public String getTitleEnUs() {
		return titleEnUs;
	}

	public void setTitleEnUs(String titleEnUs) {
		this.titleEnUs = titleEnUs;
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
	@Column(name = "contextlob_en_us")
	public String getContextlobEnUs() {
		return contextlobEnUs;
	}

	public void setContextlobEnUs(String contextlobEnUs) {
		this.contextlobEnUs = contextlobEnUs;
	}
	@Column(name = "contextlob_zh_tw")
	public String getContextlobZhTw() {
		return contextlobZhTw;
	}

	public void setContextlobZhTw(String contextlobZhTw) {
		this.contextlobZhTw = contextlobZhTw;
	}
	@Column(name = "answer_en_us")
	public String getAnswerEnUs() {
		return answerEnUs;
	}

	public void setAnswerEnUs(String answerEnUs) {
		this.answerEnUs = answerEnUs;
	}
	@Column(name = "answer_zh_tw")
	public String getAnswerZhTw() {
		return answerZhTw;
	}

	public void setAnswerZhTw(String answerZhTw) {
		this.answerZhTw = answerZhTw;
	}

	public String getUploadnewsfile() {
		return uploadnewsfile;
	}

	public void setUploadnewsfile(String uploadnewsfile) {
		this.uploadnewsfile = uploadnewsfile;
	}

	public Integer getClicknum() {
		return clicknum;
	}

	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}

	@ManyToOne(fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}
	

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "faqtypeuuid")
	public WebFaqType getWebFaqType() {
		return webFaqType;
	}

	public void setWebFaqType(WebFaqType webFaqType) {
		this.webFaqType = webFaqType;
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
