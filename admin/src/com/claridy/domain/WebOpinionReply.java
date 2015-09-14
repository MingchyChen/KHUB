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
@Table(name = "webopinionreply")
public class WebOpinionReply implements Serializable {
	private static final long serialVersionUID = 4710706985739801900L;
	private String uuid;
	private WebOpinion webOpinion;//Webopinion.uuid
	private String replyZhTw;//回覆內容
	private WebEmployee replyWebEmployee;//回覆之管理者
	private WebEmployee webEmployee;
	private String dataOwnerGroup;
	private Date createDate;
	private String latelyChangedUser;
	private Date latelyChangedDate;
	private int isDataEffid;//資料是否有效1表有效、0表無效

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
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "webopinionuuid")
	public WebOpinion getWebOpinion() {
		return webOpinion;
	}

	public void setWebOpinion(WebOpinion webOpinion) {
		this.webOpinion = webOpinion;
	}
	
	@Column(name = "reply_zh_tw")
	public String getReplyZhTw() {
		return replyZhTw;
	}

	public void setReplyZhTw(String replyZhTw) {
		this.replyZhTw = replyZhTw;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "employeesn")
	public WebEmployee getReplyWebEmployee() {
		return replyWebEmployee;
	}

	public void setReplyWebEmployee(WebEmployee replyWebEmployee) {
		this.replyWebEmployee = replyWebEmployee;
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
	public int getIsDataEffid() {
		return isDataEffid;
	}

	public void setIsDataEffid(int isDataEffid) {
		this.isDataEffid = isDataEffid;
	}
}
