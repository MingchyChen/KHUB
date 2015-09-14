package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.common.mechanism.domain.Sys_Pagination;
import com.claridy.domain.Category;

public interface ICategoryDAO extends IBaseDAO
{
	public List<Category> findAll() throws DataAccessException;
	
	public Category find(String categorycode) throws DataAccessException;
	
	public List<Category> search(int currentPage, int pageSize) throws DataAccessException;
	
	public int getTotalCount() throws DataAccessException; 
	
	public Sys_Pagination find(String order, String categorycode, int currentPage, int pageSize) throws DataAccessException ;
	
	public List<Category> findCategoryList(String parent);
	
	public List<Category> findCategoryList2(String parent);
	
	public Sys_Pagination getSelCategoryList(String categoryname,String parent, String[] sort, int currentPage, int pageSize);
	
	public String getNum(String categorycode,String parent);
}
