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

@Entity
@Table( name = "erm_resources_main_ejeb")
public class ErmResourcesMainEjeb implements Serializable {

	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//資源編號EJ或EB+9碼流水號
	private String title;//題名
	private String brief1;//資源簡述摘要
	private String brief2;//資源簡述摘要(英文)
	private String languageId;//語言ERM_CODE_GENERALCODE,ITEM=’DBLAN’＊電子資料庫（中文資料庫、日文資料庫、西文資料庫、簡體中文資料庫）ERM_CODE_GENERALCODE,ITEM=’RELAN’＊其他（取自於ISO-639-2表）
	private String core;//核心N否,Y是採購時,優先採購,計算經費時,需先將此資源列為優先考量.
	private String cn;//電子書分類號EB
	private String calln;//電子書索書號EB
	private String imgurl;//圖片URLDB,EJ,EB, WS儲存路徑FOR每月推薦用
	private String author;//作者EB
	private String typeId;//資源類型
	private String jcr;//JCR指數
	private String jcrReportLink;//JCR報表連結URL
	private String issnprinted;//ISSN(PRINTED)EJ
	private String issnonline;//ISSN(ONLINE)EJ
	private String isbnprinted;//ISBN(PRINTED)EB
	private String isbnonline;//ISBN(ONLINE)
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
	
	@Id
	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}
	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getCalln() {
		return calln;
	}
	public void setCalln(String calln) {
		this.calln = calln;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getJcr() {
		return jcr;
	}
	public void setJcr(String jcr) {
		this.jcr = jcr;
	}
	@Column(name = "jcrreportlink")
	public String getJcrReportLink() {
		return jcrReportLink;
	}
	public void setJcrReportLink(String jcrReportLink) {
		this.jcrReportLink = jcrReportLink;
	}
	public String getIssnprinted() {
		return issnprinted;
	}
	public void setIssnprinted(String issnprinted) {
		this.issnprinted = issnprinted;
	}
	public String getIssnonline() {
		return issnonline;
	}
	public void setIssnonline(String issnonline) {
		this.issnonline = issnonline;
	}
	public String getIsbnprinted() {
		return isbnprinted;
	}
	public void setIsbnprinted(String isbnprinted) {
		this.isbnprinted = isbnprinted;
	}
	public String getIsbnonline() {
		return isbnonline;
	}
	public void setIsbnonline(String isbnonline) {
		this.isbnonline = isbnonline;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBrief1() {
		return brief1;
	}
	public void setBrief1(String brief1) {
		this.brief1 = brief1;
	}
	public String getBrief2() {
		return brief2;
	}
	public void setBrief2(String brief2) {
		this.brief2 = brief2;
	}
	@Column(name = "language_id")
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	@Column(name = "type_id")
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Column(name = "isdataeffid")
	public Integer getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(Integer isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
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
}
