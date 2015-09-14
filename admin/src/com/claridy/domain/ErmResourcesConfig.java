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
@Table(name = "erm_resources_config")
public class ErmResourcesConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3606881308596734452L;
	private String typeId;//資源型態erm_code_generalcode,item_id=’retype’
	private int title;//題名DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int url1;//url DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int url2;//相關url DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int remarkId;//採購註記 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int starOrderDate;//起訂日期 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int endOrderDate;//迄訂日期 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int coverage;//收錄年代 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int regllyIp;//合法ip DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int idpwd;//帳號與密碼 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int others;//其他資訊 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int brief1;//資源簡述摘要 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int brief2;//資源簡述摘要(英文) DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int languageId;//語言 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int agentedId;//代理商 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int publisherId;//出版商 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int frenquency;//更新頻率/刊期 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int intro;//使用說明 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int concur;//同時上線人數 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int connectId;//連線方式 DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int core;//核心 EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int pubPlace;//出版地 EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int dbId;//所屬資料庫 EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int publisherurl;//出版商首頁 EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int issnprinted;//issn(printed)ej若為０則不顯示，填入數字則為前臺顯示順序
	private int issnonline;//issn(online)ej若為０則不顯示，填入數字則為前臺顯示順序
	private int isbnprinted;//sbn(printed)eb若為０則不顯示，填入數字則為前臺顯示順序
	private int isbnonline;//isbn(online)eb若為０則不顯示，填入數字則為前臺顯示順序
	private int embargo;//embargoEJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int cn;//電子書分類號eb若為０則不顯示，填入數字則為前臺顯示順序
	private int calln;//電子書索書號eb若為０則不顯示，填入數字則為前臺顯示順序
	private int eholdings;//館藏資訊ej若為０則不顯示，填入數字則為前臺顯示順序
	private int libaryMoney;//圖書館經費db,EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int imgurl;//圖片url DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int author;//作者eb若為０則不顯示，填入數字則為前臺顯示順序
	private int placeId;//存放地點DB,EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int version;//版本EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int relatedTitle;//相關題名DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int subject;//主題DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int type;//資料庫類型DB若為０則不顯示，填入數字則為前臺顯示順序
	private int suitCollege;//適用學院DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private int suitDep;//適用系所EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int orderCollege;//訂購學院EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int orderDep;//訂購系所EJ,EB若為０則不顯示，填入數字則為前臺顯示順序
	private int uploadFile;//上傳檔案DB,EJ,EB,WS若為０則不顯示，填入數字則為前臺顯示順序
	private String latelyChangedUser;// 移動者
	private WebEmployee webEmployee;//建立者
	private String dataOwnerGroup;// 建立者群組
	private Date createDate;// 建立時間
	private int isDataEffid;// 資料是否有效1表有效、0表無效
	private Date latelyChangedDate;// 移動時間

	@Id
	@Column(name="type_id")
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
	public int getTitle() {
		return title;
	}
	
	

	public void setTitle(int title) {
		this.title = title;
	}
	public int getUrl1() {
		return url1;
	}
	public void setUrl1(int url1) {
		this.url1 = url1;
	}
	public int getUrl2() {
		return url2;
	}
	public void setUrl2(int url2) {
		this.url2 = url2;
	}
	@Column(name="remark_id")
	public int getRemarkId() {
		return remarkId;
	}
	public void setRemarkId(int remarkId) {
		this.remarkId = remarkId;
	}
	@Column(name="starorderdate")
	public int getStarOrderDate() {
		return starOrderDate;
	}
	public void setStarOrderDate(int starOrderDate) {
		this.starOrderDate = starOrderDate;
	}
	@Column(name="endorderdate")
	public int getEndOrderDate() {
		return endOrderDate;
	}
	public void setEndOrderDate(int endOrderDate) {
		this.endOrderDate = endOrderDate;
	}
	public int getCoverage() {
		return coverage;
	}
	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}
	@Column(name="regllyip")
	public int getRegllyIp() {
		return regllyIp;
	}
	public void setRegllyIp(int regllyIp) {
		this.regllyIp = regllyIp;
	}
	public int getIdpwd() {
		return idpwd;
	}
	public void setIdpwd(int idpwd) {
		this.idpwd = idpwd;
	}
	public int getOthers() {
		return others;
	}
	public void setOthers(int others) {
		this.others = others;
	}
	public int getBrief1() {
		return brief1;
	}
	public void setBrief1(int brief1) {
		this.brief1 = brief1;
	}
	public int getBrief2() {
		return brief2;
	}
	public void setBrief2(int brief2) {
		this.brief2 = brief2;
	}
	@Column(name="language_id")
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	@Column(name="agented_id")
	public int getAgentedId() {
		return agentedId;
	}
	public void setAgentedId(int agentedId) {
		this.agentedId = agentedId;
	}
	@Column(name="publisher_id")
	public int getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(int publisherId) {
		this.publisherId = publisherId;
	}
	public int getFrenquency() {
		return frenquency;
	}
	public void setFrenquency(int frenquency) {
		this.frenquency = frenquency;
	}
	public int getIntro() {
		return intro;
	}
	public void setIntro(int intro) {
		this.intro = intro;
	}
	public int getConcur() {
		return concur;
	}
	public void setConcur(int concur) {
		this.concur = concur;
	}
	@Column(name="connect_id")
	public int getConnectId() {
		return connectId;
	}
	public void setConnectId(int connectId) {
		this.connectId = connectId;
	}
	public int getCore() {
		return core;
	}
	public void setCore(int core) {
		this.core = core;
	}
	@Column(name="pubplace")
	public int getPubPlace() {
		return pubPlace;
	}
	public void setPubPlace(int pubPlace) {
		this.pubPlace = pubPlace;
	}
	@Column(name="db_id")
	public int getDbId() {
		return dbId;
	}
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
	public int getPublisherurl() {
		return publisherurl;
	}
	public void setPublisherurl(int publisherurl) {
		this.publisherurl = publisherurl;
	}
	public int getIssnprinted() {
		return issnprinted;
	}
	public void setIssnprinted(int issnprinted) {
		this.issnprinted = issnprinted;
	}
	public int getIssnonline() {
		return issnonline;
	}
	public void setIssnonline(int issnonline) {
		this.issnonline = issnonline;
	}
	public int getIsbnprinted() {
		return isbnprinted;
	}
	public void setIsbnprinted(int isbnprinted) {
		this.isbnprinted = isbnprinted;
	}
	public int getIsbnonline() {
		return isbnonline;
	}
	public void setIsbnonline(int isbnonline) {
		this.isbnonline = isbnonline;
	}
	public int getEmbargo() {
		return embargo;
	}
	public void setEmbargo(int embargo) {
		this.embargo = embargo;
	}
	public int getCn() {
		return cn;
	}
	public void setCn(int cn) {
		this.cn = cn;
	}
	public int getCalln() {
		return calln;
	}
	public void setCalln(int calln) {
		this.calln = calln;
	}
	public int getEholdings() {
		return eholdings;
	}
	public void setEholdings(int eholdings) {
		this.eholdings = eholdings;
	}
	@Column(name="libarymoney")
	public int getLibaryMoney() {
		return libaryMoney;
	}
	public void setLibaryMoney(int libaryMoney) {
		this.libaryMoney = libaryMoney;
	}
	public int getImgurl() {
		return imgurl;
	}
	public void setImgurl(int imgurl) {
		this.imgurl = imgurl;
	}
	public int getAuthor() {
		return author;
	}
	public void setAuthor(int author) {
		this.author = author;
	}
	@Column(name="place_id")
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Column(name="relatedtitle")
	public int getRelatedTitle() {
		return relatedTitle;
	}
	public void setRelatedTitle(int relatedTitle) {
		this.relatedTitle = relatedTitle;
	}
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Column(name="suitcollege")
	public int getSuitCollege() {
		return suitCollege;
	}
	public void setSuitCollege(int suitCollege) {
		this.suitCollege = suitCollege;
	}
	@Column(name="suitdep")
	public int getSuitDep() {
		return suitDep;
	}
	public void setSuitDep(int suitDep) {
		this.suitDep = suitDep;
	}
	@Column(name="ordercollege")
	public int getOrderCollege() {
		return orderCollege;
	}
	public void setOrderCollege(int orderCollege) {
		this.orderCollege = orderCollege;
	}
	@Column(name="orderdep")
	public int getOrderDep() {
		return orderDep;
	}
	public void setOrderDep(int orderDep) {
		this.orderDep = orderDep;
	}
	@Column(name="uploadfile")
	public int getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(int uploadFile) {
		this.uploadFile = uploadFile;
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
	
	
}
