package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "webresearchreports")
public class WebReSearchReports implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1437197334261872417L;

	private String uuid;
	private String categoryId;//分類id
	private String subjectZhTw;//中文篇名
	private String author;//作者中文
	private Date onlineDate;//公佈日期
	private String keyWords;//關鍵字
	private String publisher;//出版單位
	private String groupSreadingIninter;//閱讀分眾導覽(前端入口網)
	private String description;//中文摘要
	private String journalName;//刊物名稱
	private String foreignSubject;//英文名稱
	private String doMain;//專業領域
	private String editors;//編輯者中文
	private String foreignEdites;//編輯者英文
	private String foreignAuthor;//作者英文
	private String publishDate;//出版年月
	private String foreginDescription;//英文摘要
	private String fileDisplayName;//顯示檔案名稱
	private String fileUrl;//檔案下載網址
	private int clickNum;
	
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="categoryid")
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@Column(name="subject_zh_tw")
	public String getSubjectZhTw() {
		return subjectZhTw;
	}
	public void setSubjectZhTw(String subjectZhTw) {
		this.subjectZhTw = subjectZhTw;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Column(name="onlinedate")
	public Date getOnlineDate() {
		return onlineDate;
	}
	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}
	
	@Column(name="keywords")
	public String getKeyWords() {
		return keyWords;
	}
	
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	@Column(name="groupsreadingininter")
	public String getGroupSreadingIninter() {
		return groupSreadingIninter;
	}
	public void setGroupSreadingIninter(String groupSreadingIninter) {
		this.groupSreadingIninter = groupSreadingIninter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="journalname")
	public String getJournalName() {
		return journalName;
	}
	public void setJournalName(String journalName) {
		this.journalName = journalName;
	}
	
	@Column(name="foreignsubject")
	public String getForeignSubject() {
		return foreignSubject;
	}
	public void setForeignSubject(String foreignSubject) {
		this.foreignSubject = foreignSubject;
	}
	
	@Column(name="domain")
	public String getDoMain() {
		return doMain;
	}
	public void setDoMain(String doMain) {
		this.doMain = doMain;
	}
	public String getEditors() {
		return editors;
	}
	public void setEditors(String editors) {
		this.editors = editors;
	}
	
	@Column(name="foreigneditors")
	public String getForeignEdites() {
		return foreignEdites;
	}
	public void setForeignEdites(String foreignEdites) {
		this.foreignEdites = foreignEdites;
	}
	
	@Column(name="foreignauthor")
	public String getForeignAuthor() {
		return foreignAuthor;
	}
	public void setForeignAuthor(String foreignAuthor) {
		this.foreignAuthor = foreignAuthor;
	}
	
	@Column(name="publishdate")
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	
	@Column(name="foreigndescription")
	public String getForeginDescription() {
		return foreginDescription;
	}
	
	public void setForeginDescription(String foreginDescription) {
		this.foreginDescription = foreginDescription;
	}
	
	@Column(name="filedisplayname")
	public String getFileDisplayName() {
		return fileDisplayName;
	}
	public void setFileDisplayName(String fileDisplayName) {
		this.fileDisplayName = fileDisplayName;
	}
	
	@Column(name="fileurl")
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	@Column(name="clicknum")
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	
	
	
	

}
