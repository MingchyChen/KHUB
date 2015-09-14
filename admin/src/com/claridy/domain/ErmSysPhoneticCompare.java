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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "erm_sys_phonetic_compare")
public class ErmSysPhoneticCompare implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private String characterCn;
	private Integer characterNum;
	private String phoneticOne;
	private String phoneticTwo;
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private Integer isDataEffId;
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
	@Column(name="character_cn")
	public String getCharacterCn() {
		return characterCn;
	}
	public void setCharacterCn(String characterCn) {
		this.characterCn = characterCn;
	}
	@Column(name="character_num")
	public Integer getCharacterNum() {
		return characterNum;
	}
	public void setCharacterNum(Integer characterNum) {
		this.characterNum = characterNum;
	}
	@Column(name="phonetic_one")
	public String getPhoneticOne() {
		return phoneticOne;
	}
	public void setPhoneticOne(String phoneticOne) {
		this.phoneticOne = phoneticOne;
	}
	@Column(name="phonetic_two")
	public String getPhoneticTwo() {
		return phoneticTwo;
	}
	public void setPhoneticTwo(String phoneticTwo) {
		this.phoneticTwo = phoneticTwo;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
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
	
	@Column(name="isdataeffid")
	public Integer getIsDataEffId() {
		return isDataEffId;
	}
	public void setIsDataEffId(Integer isDataEffId) {
		this.isDataEffId = isDataEffId;
	}
	
	
}
