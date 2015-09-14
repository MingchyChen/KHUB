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
@Table(name = "erm_resources_reltitle")
public class ErmResourcesReltitle implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//資源編號DB+9碼流水號
	private String relatedTitleId;//相關題名編號RR_XXXX為2碼流水號PK
	private String name;//相關題名
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
	@Column(name = "resources_id")
	public String getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(String resourcesId) {
		this.resourcesId = resourcesId;
	}
	@Id
	@Column(name = "relatedtitleid")
	public String getRelatedTitleId() {
		return relatedTitleId;
	}

	public void setRelatedTitleId(String relatedTitleId) {
		this.relatedTitleId = relatedTitleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
