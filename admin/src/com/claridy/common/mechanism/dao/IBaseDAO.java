package com.claridy.common.mechanism.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;

import com.claridy.common.mechanism.domain.Sys_Pagination;

public interface IBaseDAO{

	public Sys_Pagination findBySQL(String strSQL, int currentPage, int pageSize);
	public Object get(Class<?> clazz, Serializable id, boolean lock) throws DataAccessException ;

	public List<?> findByHQL(String strHQL) throws DataAccessException ;
	public void merge(Object obj) throws DataAccessException ;
	public void updateByHQL(String strHQL) throws DataAccessException ;
	
	public Sys_Pagination findByHQL(String strHQL, int currentPage, int pageSize) throws DataAccessException ;
	
	public List<?> findByCriteria(DetachedCriteria detachedCriteria) throws DataAccessException ;
	
	public Sys_Pagination findByCriteria(DetachedCriteria detachedCriteria, int currentPage, int pageSize) throws DataAccessException ;

	public int getRowCount(String strHQL) throws DataAccessException;
	
	public int getRowCount(DetachedCriteria detachedCriteria) throws DataAccessException;
	
	public void create(Object obj) throws DataAccessException ;

	public void update(Object obj) throws DataAccessException ;

	public void saveOrUpdate(Object obj) throws DataAccessException ;

	public void delete(Object obj) throws DataAccessException ;

	public void delete(Class<?> clazz, Serializable id) throws DataAccessException ;

	public int getTotalRecoredRsize(Query query)  throws DataAccessException ;

	public void initialize(Object object, String[] properties) throws DataAccessException ;

	public int calculateFirstRowNum(int pageNo, int pageSize)  throws DataAccessException ;

}
