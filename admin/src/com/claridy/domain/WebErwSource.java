package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "weberwsource")
public class WebErwSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3054681570679447529L;
	@Id
	private String uuid;// id
	@Column(name = "name_zh_tw")
	private String nameZhTw;// 資料庫名稱
	private String dbid;// 資料庫id
	private String publisher;// 出版商
	@Column(name = "isdataeffid")
	private int isDataEffid;// 資料是否有效1表有效、0表無效
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="latelychangeduser")
	private WebEmployee latelyChangeDuser;// 異動者
	@Column(name = "latelychangeddate")
	private Date latelyChangedDate;// 異動時間
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )  
	@JoinColumn(name="dataowner")
	private WebEmployee webEmployee;// 建立者
	@Column(name = "createdate")
	private Date createDate;// 建立時間
	@Column(name = "dataownergroup")
	private String dataOwnerGroup;// 建立者群組
	@Column(name = "roundnum")
	private int roundNum;// 輪職順序

	@OneToMany(targetEntity = WebErwSourceUnit.class)
	@JoinColumn(name = "weberwsourceuuid", referencedColumnName = "uuid")
	@Fetch(FetchMode.SELECT)
	@Where(clause="isDataEffid = 1")
	private List<WebErwSourceUnit> webErwSources;

	public List<WebErwSourceUnit> getWebErwSources() {
		return webErwSources;
	}

	public void setWebErwSources(List<WebErwSourceUnit> webErwSources) {
		this.webErwSources = webErwSources;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getNameZhTw() {
		return nameZhTw;
	}

	public void setNameZhTw(String nameZhTw) {
		this.nameZhTw = nameZhTw;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}

	public WebEmployee getLatelyChangeDuser() {
		return latelyChangeDuser;
	}

	public void setLatelyChangeDuser(WebEmployee latelyChangeDuser) {
		this.latelyChangeDuser = latelyChangeDuser;
	}

	public Date getLatelyChangedDate() {
		return latelyChangedDate;
	}

	public void setLatelyChangedDate(Date latelyChangedDate) {
		this.latelyChangedDate = latelyChangedDate;
	}

	public WebEmployee getWebEmployee() {
		return webEmployee;
	}

	public void setWebEmployee(WebEmployee webEmployee) {
		this.webEmployee = webEmployee;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDataOwnerGroup() {
		return dataOwnerGroup;
	}

	public void setDataOwnerGroup(String dataOwnerGroup) {
		this.dataOwnerGroup = dataOwnerGroup;
	}

	public int getRoundNum() {
		return roundNum;
	}

	public void setRoundNum(int roundNum) {
		this.roundNum = roundNum;
	}

}
