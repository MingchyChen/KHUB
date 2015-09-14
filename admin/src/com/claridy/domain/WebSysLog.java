package com.claridy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="websyslog")
public class WebSysLog implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2889570145714332793L;
	private String nkey;
	private Date ndate;
	private String nip;
	private String employeesn;
	private String nlocate;
	private String nnote;
	
	@Id
	public String getNkey() {
		return nkey;
	}
	public void setNkey(String nkey) {
		this.nkey = nkey;
	}
	
	public Date getNdate() {
		return ndate;
	}
	public void setNdate(Date ndate) {
		this.ndate = ndate;
	}
	
	public String getNip() {
		return nip;
	}
	public void setNip(String nip) {
		this.nip = nip;
	}
	public String getEmployeesn() {
		return employeesn;
	}
	public void setEmployeesn(String employeesn) {
		this.employeesn = employeesn;
	}
	public String getNlocate() {
		return nlocate;
	}
	public void setNlocate(String nlocate) {
		this.nlocate = nlocate;
	}
	public String getNnote() {
		return nnote;
	}
	public void setNnote(String nnote) {
		this.nnote = nnote;
	}
	
	
}
