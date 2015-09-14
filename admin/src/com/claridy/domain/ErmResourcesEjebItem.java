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
@Table( name = "erm_resources_ejeb_item")
public class ErmResourcesEjebItem implements Serializable {

	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//資源編號EJ或EB+9碼流水號
	private String url1;//url
	private String url2;//相關url
	private String remarkId;//private String remarkId;//採購註記ERM_CODE_GENERALCODE,ITEM=’PURE’
	private Date starOrderDate;//起訂開始日期
	private Date endOrderDate;//迄訂結束日期
	private String coverage;//收錄年代
	private String regllyIp;//合法ip
	private String idpwd;//帳號與密碼
	private String others;//其他資訊
	private String publisherId;//資料庫出版商
	private String frenquency;//更新頻率/刊期
	private String pubPlace;//出版地
	private String dbId;//所屬資料庫
	private String publisherUrl;//出版商首頁此欄位農委會不需要，公圖版要
	private String embargo;//EMBARGO
	private String eholdings;//館藏資訊
	private String libaryMoney;//圖書館經費N否,Y是此欄位農委會不需要，公圖版要
	private String placeId;//存放地點ERM_CODE_GENERALCODE,ITEM=’PLACE’
	private String version;//版本EB
	private String state;//狀態1正常2暫存
	private String history;//停用註記N:否 Y:是
	private String imgurlF;//圖片
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
	private String dbIdShowStr;
	
	@Id
	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}
	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}
	@Id
	@Column(name = "db_id")
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	@Column(name = "pubplace")
	public String getPubPlace() {
		return pubPlace;
	}
	public void setPubPlace(String pubPlace) {
		this.pubPlace = pubPlace;
	}
	public String getEmbargo() {
		return embargo;
	}
	public void setEmbargo(String embargo) {
		this.embargo = embargo;
	}
	public String getEholdings() {
		return eholdings;
	}
	public void setEholdings(String eholdings) {
		this.eholdings = eholdings;
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
	@Column(name = "publisherurl")
	public String getPublisherUrl() {
		return publisherUrl;
	}
	public void setPublisherUrl(String publisherUrl) {
		this.publisherUrl = publisherUrl;
	}
	@Column(name = "libarymoney")
	public String getLibaryMoney() {
		return libaryMoney;
	}
	public void setLibaryMoney(String libaryMoney) {
		this.libaryMoney = libaryMoney;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "imgurl_f")
	public String getImgurlF() {
		return imgurlF;
	}
	public void setImgurlF(String imgurlF) {
		this.imgurlF = imgurlF;
	}
	@Column(name = "place_id")
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	@Transient
	public String getDbIdShowStr() {
		return dbIdShowStr;
	}
	public void setDbIdShowStr(String dbIdShowStr) {
		this.dbIdShowStr = dbIdShowStr;
	}
	
}
