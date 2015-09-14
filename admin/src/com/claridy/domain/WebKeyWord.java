package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="webkeyword")
public class WebKeyWord implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2889570145714332793L;
	private String uuid;
	private String keyWord; 
	private int target;  
	private String webAccountUuid;
	private Date createDate;
	@Id
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="keyword")
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	@Column(name="webaccountuuid")
	public String getWebAccountUuid() {
		return webAccountUuid;
	}
	public void setWebAccountUuid(String webAccountUuid) {
		this.webAccountUuid = webAccountUuid;
	}
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
