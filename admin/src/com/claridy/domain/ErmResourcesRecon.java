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
@Table(name = "erm_resources_recon")
public class ErmResourcesRecon implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private String usrno;//回報人員帳號 ERM_USER_ACUSER 農委會為webaccount.accountid
	private String resourcesId;
	private String urlcon;//連線狀態1正常2異常ERM_CODE_GENERALCODEITEM_ID='URLCON' 預設寫入異常
	private Date redate;//回報日期2011/01/01只有日期,不用時間
	private String note;//備註
	private String dbId;//所屬資料庫 電子期刊/書資料庫 ERM_CODE_DB
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;

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

	public String getUsrno() {
		return usrno;
	}

	public void setUsrno(String usrno) {
		this.usrno = usrno;
	}

	public String getUrlcon() {
		return urlcon;
	}

	public void setUrlcon(String urlcon) {
		this.urlcon = urlcon;
	}

	public Date getRedate() {
		return redate;
	}

	public void setRedate(Date redate) {
		this.redate = redate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	@Column(name = "db_id")
	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
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
