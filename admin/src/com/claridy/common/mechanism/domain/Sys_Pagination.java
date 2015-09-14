package com.claridy.common.mechanism.domain;

import java.util.List;

public class Sys_Pagination {	
	private int rowCount;//總筆數
	private int pageCount;//總頁數
	private List<?> aaData;//回傳的資料
	private long iTotalRecords;//未經過濾的資料總筆數
	private long iTotalDisplayRecords;//經過濾的資料總筆數，但在大部份的使用上情形上幾乎同於iTotalRecords
	private String sEcho;
	
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}	
	public List<?> getAaData() {
		return aaData;
	}
	public void setAaData(List<?> aaData) {
		this.aaData = aaData;
	}
	public long getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
}

