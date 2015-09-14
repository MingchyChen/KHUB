package com.claridy.common.mechanism.dao;

import java.util.List;

import com.claridy.common.mechanism.domain.Sys_Count;
import com.claridy.common.mechanism.domain.Sys_Pagination;

public interface ISys_CountDAO extends IBaseDAO
{
	public List<Sys_Count> findAll() throws DataAccessException; 
	
	public Sys_Count find(String obj_pk,String obj_name,String obj_time) throws DataAccessException;
	
	public long findSumCount(String obj_pk,String obj_name) throws DataAccessException;
	
	public List getHotRes();

	public Sys_Pagination getResList(String startime, String endtime, String[] sort, int currentPage,int pageSize);
}
