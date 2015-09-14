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
@Table(name = "erm_resources_odetails")
public class ErmResourcesOdetails implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String resourcesId;//資源編號
	private String year;//採購年度(PK)
	private String money;//採購金額
	private String note;//經費來源註記
	private String remarkId;//採購註記ERM_CODE_GENERALCODE,ITEM=’PURE’
	private Date starOrderDate;//起訂日期
	private Date endOrderDate;//訖訂日期
	private String agentedId;//代理商ERM_CODE_PUBLISHER
	private String orderUser;//承辦人員
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
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
	
	@Column(name = "agented_id")
	public String getAgentedId() {
		return agentedId;
	}
	public void setAgentedId(String agentedId) {
		this.agentedId = agentedId;
	}
	
	@Column(name = "order_user")
	public String getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
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
