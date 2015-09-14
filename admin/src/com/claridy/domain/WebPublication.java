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
@Table(name="webpublication")
public class WebPublication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7535886386001538623L;
	private String uuid;
	private String titleZhTw;//書名
	private String isbn;//isbn
	private String issn;//issn
	private String authorZhTw;//作者
	private int isDisplay;//是否顯示1顯示，0不顯示  
	private String publisherZhTw;//出版者
	private Date pyear;//出版年
	private String img;//上傳檔案名
	private String desZhTw;//內容
	private String latelyChangedUser;//異動輒
	private WebEmployee webEmployee;//建立者
	private String dataOwnerGroup;//建立者群組
	private Date createDate;//建立時間
	private int isDataEffid;//資料是否有效1表有效，0表無效
	private Date latelyChangedDate;//異動時間
	private int clickNum;//點擊數
	private String strurl;//url
	private String ebookurl;//電子書URL
	private String pdfurl;//PDF URL
	
	public String getIssn() {
		return issn; 
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="title_zh_tw")
	public String getTitleZhTw() {
		return titleZhTw;
	}
	public void setTitleZhTw(String titleZhTw) {
		this.titleZhTw = titleZhTw;
	}
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@Column(name="author_zh_tw")
	public String getAuthorZhTw() {
		return authorZhTw;
	}
	public void setAuthorZhTw(String authorZhTw) {
		this.authorZhTw = authorZhTw;
	}
	
	@Column(name="isdisplay")
	public int getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
	public Date getPyear() {
		return pyear;
	}
	public void setPyear(Date pyear) {
		this.pyear = pyear;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	@Column(name="des_zh_tw")
	public String getDesZhTw() {
		return desZhTw;
	}
	public void setDesZhTw(String desZhTw) {
		this.desZhTw = desZhTw;
	}
	
	@Column(name="latelychangeduser")
	public String getLatelyChangedUser() {
		return latelyChangedUser;
	}
	public void setLatelyChangedUser(String latelyChangedUser) {
		this.latelyChangedUser = latelyChangedUser;
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
	
	@Column(name="clicknum")
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	
	@Column(name="publisher_zh_tw")
	public String getPublisherZhTw() {
		return publisherZhTw;
	}
	public void setPublisherZhTw(String publisherZhTw) {
		this.publisherZhTw = publisherZhTw;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="dataowner")
	public WebEmployee getWebEmployee() {
		return webEmployee;
	}
	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}
	public String getStrurl() {
		return strurl;
	}
	public void setStrurl(String strurl) {
		this.strurl = strurl;
	}
	public String getEbookurl() {
		return ebookurl;
	}
	public void setEbookurl(String ebookurl) {
		this.ebookurl = ebookurl;
	}
	public String getPdfurl() {
		return pdfurl;
	}
	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}
	
	
	
	
}
