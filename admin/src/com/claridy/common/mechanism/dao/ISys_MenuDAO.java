package com.claridy.common.mechanism.dao;

import java.util.List;

import com.claridy.common.mechanism.domain.Sys_Menu;
import com.claridy.domain.WebFunction;

public interface ISys_MenuDAO extends IBaseDAO
{
	public List<WebFunction> findAll(String languageType) throws DataAccessException;
	
	public List<WebFunction> findAllList(String languageType) throws DataAccessException;
	
	public List<WebFunction> getChildMenu(WebFunction webfunction,String languageType);
	
	public Object getHasChild(String uuid,String languageType);
	
	public Sys_Menu find(String func_no) throws DataAccessException;
	
}
