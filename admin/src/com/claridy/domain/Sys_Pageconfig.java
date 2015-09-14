package com.claridy.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table (name = "SYS_PAGECONFIG")
public class Sys_Pageconfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String uuid;
	private String indexinfo;
	private String librarylink;
	private String logo_url;
	private String school_id;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getIndexinfo() {
		return indexinfo;
	}
	public void setIndexinfo(String indexinfo) {
		this.indexinfo = indexinfo;
	}
	public String getLibrarylink() {
		return librarylink;
	}
	public void setLibrarylink(String librarylink) {
		this.librarylink = librarylink;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	
}
