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
@Table(name = "erm_personalize_rescount")
public class ErmPersonalizeRescount implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private String accountid;
	private String resourcesId;
	private Integer resourcesCount;
	private String dbId;
	private String rescountType;
	private String parentOrgId;
	private String orgId;
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;
	private Integer show1;
	private Integer show2;
	private Integer show3;
	private Integer show4;
	private Integer show5;
	private Integer show6;
	private Integer show7;
	private Integer show8;
	private String shows1;
	private String shows2;
	private String shows3;
	private String shows4;
	private String shows5;
	private String shows6;
	private String shows7;
	private String shows8;

	@Id
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the item_id
	 */

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}

	@Column(name = "resources_count")
	public Integer getResourcesCount() {
		return resourcesCount;
	}

	public void setResourcesCount(Integer resourcesCount) {
		this.resourcesCount = resourcesCount;
	}

	@Column(name = "db_id")
	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	@Column(name = "rescount_type")
	public String getRescountType() {
		return rescountType;
	}

	public void setRescountType(String rescountType) {
		this.rescountType = rescountType;
	}

	@Column(name = "parentorgid")
	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	@Column(name = "orgid")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
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

	@Transient
	public Integer getShow1() {
		return show1;
	}

	public void setShow1(Integer show1) {
		this.show1 = show1;
	}

	@Transient
	public Integer getShow2() {
		return show2;
	}

	public void setShow2(Integer show2) {
		this.show2 = show2;
	}

	@Transient
	public Integer getShow3() {
		return show3;
	}

	public void setShow3(Integer show3) {
		this.show3 = show3;
	}

	@Transient
	public Integer getShow4() {
		return show4;
	}

	public void setShow4(Integer show4) {
		this.show4 = show4;
	}

	@Transient
	public Integer getShow5() {
		return show5;
	}

	public void setShow5(Integer show5) {
		this.show5 = show5;
	}

	@Transient
	public Integer getShow6() {
		return show6;
	}

	public void setShow6(Integer show6) {
		this.show6 = show6;
	}

	@Transient
	public Integer getShow7() {
		return show7;
	}

	public void setShow7(Integer show7) {
		this.show7 = show7;
	}

	@Transient
	public Integer getShow8() {
		return show8;
	}

	public void setShow8(Integer show8) {
		this.show8 = show8;
	}

	@Transient
	public String getShows1() {
		return shows1;
	}

	public void setShows1(String shows1) {
		this.shows1 = shows1;
	}

	@Transient
	public String getShows2() {
		return shows2;
	}

	public void setShows2(String shows2) {
		this.shows2 = shows2;
	}

	@Transient
	public String getShows3() {
		return shows3;
	}

	public void setShows3(String shows3) {
		this.shows3 = shows3;
	}

	@Transient
	public String getShows4() {
		return shows4;
	}

	public void setShows4(String shows4) {
		this.shows4 = shows4;
	}

	@Transient
	public String getShows5() {
		return shows5;
	}

	public void setShows5(String shows5) {
		this.shows5 = shows5;
	}

	@Transient
	public String getShows6() {
		return shows6;
	}

	public void setShows6(String shows6) {
		this.shows6 = shows6;
	}

	@Transient
	public String getShows7() {
		return shows7;
	}

	public void setShows7(String shows7) {
		this.shows7 = shows7;
	}

	@Transient
	public String getShows8() {
		return shows8;
	}

	public void setShows8(String shows8) {
		this.shows8 = shows8;
	}

}
