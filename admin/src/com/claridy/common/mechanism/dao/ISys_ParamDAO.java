package com.claridy.common.mechanism.dao;

import java.util.List;

import com.claridy.common.mechanism.domain.Sys_Param;

public interface ISys_ParamDAO extends IBaseDAO
{
	public List<Sys_Param> findAll() throws DataAccessException;
	
	public Sys_Param find(String func_no,String parent) throws DataAccessException;
	
	public List<Sys_Param> findByCategory(String category) throws DataAccessException;
	
	public Sys_Param findByValue(String category, String funcValue) throws DataAccessException;
	
	public List<Sys_Param> findSysParamList(String parent);
	
	public Sys_Param findByFuncno(String funcno) throws DataAccessException;
}
