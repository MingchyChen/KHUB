/**
 * ERMSystemSetting.java
 *
 * @author RMB
 */

package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table( name = "erm_resources_mainfile_v")
public class ErmResourcesMainfileV implements Serializable {

	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//編號
	private String title;//題名
	private String typeId;//資源類型
	private String remarkId;//採購註記
	private String brief1;//資源簡述摘要
	private String dbId;//所屬資料庫
	private String agentedId;//資料庫代理商
	private String url1;//url
	private String url2;//相關url
	private String regllyIp;//合法ip
	private String idpwd;//帳號與密碼
	private String others;//其他資訊
	private String brief2;//資源簡述摘要(英文)
	private String frenquency;//更新頻率/刊期
	private String intro;//使用說明
	private String concur;//同時上線人數
	private String pubPlace;//出版地
	private String publisherUrl;//出版商首頁此欄位農委會不需要，公圖版要
	private String issnprinted;//ISSN(PRINTED)EJ
	private String issnonline;//ISSN(ONLINE)EJ
	private String isbnprinted;//ISBN(PRINTED)EB
	private String isbnonline;//ISBN(ONLINE)
	private String embargo;//EMBARGO
	private String calln;//電子書索書號EB
	private String cn;//電子書分類號EB
	private String eholdings;//館藏資訊
	private String libaryMoney;//圖書館經費N否,Y是此欄位農委會不需要，公圖版要
	private String author;//作者EB
	private String version;//版本
	private String domain;//domain name
	private String core;//核心N否,Y是採購時,優先採購,計算經費時,需先將此資源列為優先考量.
	private String imgurl;//圖片
	private String languageId;//語言
	private Date starOrderDate;//起訂開始日期
	private Date endOrderDate;//迄訂結束日期
	private String state;//狀態
	private String connectId;//連線方式
	private String publisherId;//資料庫出版商
	private String jcr;//JCR指數
	private String jcrReportLink;//JCR報表連結URL
	private String coverage;//收錄年代
	private String placeId;//存放地點
	private String history;//刪除註記
	private String dataowner;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
	private String languageCn;//清單列顯示的語言類型
	private String remarkCn;//顯示的採購註記類型
	private String dbNumber;//是否有多個所屬資料庫1表示多個0表示1個
	private String createName;//創建人員
	@Id
	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}
	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	@Column(name = "remark_id")
	public String getRemarkId() {
		return remarkId;
	}
	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}
	@Column(name = "starorderdate")
	public Date getStarOrderDate() {
		return starOrderDate;
	}
	public void setStarOrderDate(Date starOrderDate) {
		this.starOrderDate = starOrderDate;
	}
	@Column(name = "endorderdate")
	public Date getEndOrderDate() {
		return endOrderDate;
	}
	public void setEndOrderDate(Date endOrderDate) {
		this.endOrderDate = endOrderDate;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	@Column(name = "regllyip")
	public String getRegllyIp() {
		return regllyIp;
	}
	public void setRegllyIp(String regllyIp) {
		this.regllyIp = regllyIp;
	}
	public String getIdpwd() {
		return idpwd;
	}
	public void setIdpwd(String idpwd) {
		this.idpwd = idpwd;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
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
	@Column(name = "agented_id")
	public String getAgentedId() {
		return agentedId;
	}
	public void setAgentedId(String agentedId) {
		this.agentedId = agentedId;
	}
	@Column(name = "publisher_id")
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getFrenquency() {
		return frenquency;
	}
	public void setFrenquency(String frenquency) {
		this.frenquency = frenquency;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getConcur() {
		return concur;
	}
	public void setConcur(String concur) {
		this.concur = concur;
	}
	@Column(name = "connect_id")
	public String getConnectId() {
		return connectId;
	}
	public void setConnectId(String connectId) {
		this.connectId = connectId;
	}
	@Column(name = "libarymoney")
	public String getLibaryMoney() {
		return libaryMoney;
	}
	public void setLibaryMoney(String libaryMoney) {
		this.libaryMoney = libaryMoney;
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
	@Column(name = "place_id")
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	@Column(name = "isdataeffid")
	public Integer getIsDataEffid() {
		return isDataEffid;
	}
	public void setIsDataEffid(Integer isDataEffid) {
		this.isDataEffid = isDataEffid;
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
	@Id
	@Column(name="db_id")
	public String getDbId() {
		return dbId; 
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	@Column(name="pubplace")
	public String getPubPlace() {
		return pubPlace;
	}
	public void setPubPlace(String pubPlace) {
		this.pubPlace = pubPlace;
	}
	@Column(name="publisherurl")
	public String getPublisherUrl() {
		return publisherUrl;
	}
	public void setPublisherUrl(String publisherUrl) {
		this.publisherUrl = publisherUrl;
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
	public String getEmbargo() {
		return embargo;
	}
	public void setEmbargo(String embargo) {
		this.embargo = embargo;
	}
	public String getCalln() {
		return calln;
	}
	public void setCalln(String calln) {
		this.calln = calln;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getEholdings() {
		return eholdings;
	}
	public void setEholdings(String eholdings) {
		this.eholdings = eholdings;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
	public String getJcr() {
		return jcr;
	}
	public void setJcr(String jcr) {
		this.jcr = jcr;
	}
	@Column(name="jcrreportlink")
	public String getJcrReportLink() {
		return jcrReportLink;
	}
	public void setJcrReportLink(String jcrReportLink) {
		this.jcrReportLink = jcrReportLink;
	}
	public String getDataowner() {
		return dataowner;
	}
	public void setDataowner(String dataowner) {
		this.dataowner = dataowner;
	}
	@Transient
	public String getLanguageCn() {
		return languageCn;
	}
	public void setLanguageCn(String languageCn) {
		this.languageCn = languageCn;
	}
	@Transient
	public String getRemarkCn() {
		return remarkCn;
	}
	public void setRemarkCn(String remarkCn) {
		this.remarkCn = remarkCn;
	}
	@Transient
	public String getDbNumber() {
		return dbNumber;
	}
	public void setDbNumber(String dbNumber) {
		this.dbNumber = dbNumber;
	}
	@Transient
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
