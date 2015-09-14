package com.claridy.common.mechanism.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WEBHOMELINK")
public class WebHomeLink  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 
	 * This attribute maps to the column FUNC_NO in the SYS_MENU table.
	 */
	@Id
	protected String func_no;

	/** 
	 * This attribute maps to the column FUNC_NAME in the SYS_MENU table.
	 */
	protected String func_name;

	/** 
	 * This attribute maps to the column PARENT in the SYS_MENU table.
	 */
	protected String parent;

	/** 
	 * This attribute maps to the column FUNC_URL in the SYS_MENU table.
	 */
	protected String func_url;

	/** 
	 * This attribute maps to the column ALT in the SYS_MENU table.
	 */
	protected String alt;

	/** 
	 * This attribute maps to the column SEQ in the SYS_MENU table.
	 */
	protected String seq;
	
	/** 
	 * This attribute maps to the column ISACTIVE in the SYS_MENU table.
	 */
	protected String isactive;

	/** 
	 * This attribute maps to the column ISDEFAULT in the SYS_MENU table.
	 */
	protected String isdefault;
	
	public String getFunc_no() {
		return func_no;
	}

	public void setFunc_no(String func_no) {
		this.func_no = func_no;
	}

	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getFunc_url() {
		return func_url;
	}

	public void setFunc_url(String func_url) {
		this.func_url = func_url;
	}
	
	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}
}
