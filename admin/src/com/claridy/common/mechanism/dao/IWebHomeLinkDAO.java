package com.claridy.common.mechanism.dao;

import java.util.List;

import com.claridy.common.mechanism.domain.WebHomeLink;

public interface IWebHomeLinkDAO extends IBaseDAO
{
	public List<WebHomeLink> findAll() throws DataAccessException;
	
	public WebHomeLink find(String func_no) throws DataAccessException;
	public List<WebHomeLink> findMenuList();
}
