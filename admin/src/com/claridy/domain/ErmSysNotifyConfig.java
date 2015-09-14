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
@Table(name = "erm_sys_notifyconfig")
public class ErmSysNotifyConfig implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String typeId;
	// private String groupId;
	private WebOrg webOrg;
	private String subject;
	private String content;
	private String beforeDays;
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffid;// 資料是否有效1表有效、0表無效
	private String typeName;

	@Transient
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the item_id
	 */
	@Id
	@Column(name = "type_id")
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	// @Id
	// @Column(name="group_id")
	// public String getGroupId() {
	// return groupId;
	// }
	// public void setGroupId(String groupId) {
	// this.groupId = groupId;
	// }
	//@Id
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="group_id")
	public WebOrg getWebOrg() {
		return webOrg;
	}
	public void setWebOrg(WebOrg webOrg) {
		this.webOrg = webOrg;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "beforedays")
	public String getBeforeDays() {
		return beforeDays;
	}

	public void setBeforeDays(String beforeDays) {
		this.beforeDays = beforeDays;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
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
