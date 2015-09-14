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
@Table( name = "erm_resources_main_dbws")
public class ErmResourcesMainDbws implements Serializable {

	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//資源編號DB或WS+9碼流水號
	private String title;//題名
	private String url1;//url
	private String url2;//相關url
	private String remarkId;//採購註記
	private Date starOrderDate;//起訂開始日期
	private Date endOrderDate;//迄訂結束日期
	private String coverage;//收錄年代
	private String regllyIp;//合法ip
	private String idpwd;//帳號與密碼
	private String others;//其他資訊
	private String brief1;//資源簡述摘要
	private String brief2;//資源簡述摘要(英文)
	private String languageId;//語言
	private String agentedId;//資料庫代理商
	private String publisherId;//資料庫出版商
	private String frenquency;//更新頻率/刊期
	private String intro;//使用說明
	private String concur;//同時上線人數
	private String connectId;//連線方式
	private String libaryMoney;//圖書館經費
	private String imgUrl;//圖片
	private String typeId;//資源類型
	private String placeId;//存放地點
	private String version;//版本
	private String state;//狀態
	private String domain;//domain name
	private String history;//刪除註記
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
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
	@Column(name = "imgurl")
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
}
