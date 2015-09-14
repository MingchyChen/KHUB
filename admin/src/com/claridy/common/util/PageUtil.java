package com.claridy.common.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.impl.CriteriaImpl;

public class PageUtil {
	// 當前頁
	private int currentPage;
	// 每頁多少筆
	private int pageSize;
	// 總頁數
	private int pageCount;
	// 總筆數
	private int rowCount;
	// 排序欄位
	private String orderField;
	
	private String isPage;

	public void pagination(Criteria criteria) {
		if(isPage == null || "".equals(isPage) || !"true".equals(isPage)){
			currentPage = 1;
		}
		CriteriaImpl impl = (CriteriaImpl) criteria;
		Projection projection = impl.getProjection();
		long rowCountL = (Long)criteria.setProjection(Projections.rowCount())
				.uniqueResult();
		rowCount = (int)rowCountL;
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		criteria.setFirstResult((currentPage-1)*pageSize);
		criteria.setMaxResults(pageSize);
		if(orderField != null && "".equals(orderField)){
			criteria.addOrder(Property.forName(orderField).asc());
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		pageCount = (rowCount+pageSize-1) / pageSize;
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getIsPage() {
		return isPage;
	}

	public void setIsPage(String isPage) {
		this.isPage = isPage;
	}

}
