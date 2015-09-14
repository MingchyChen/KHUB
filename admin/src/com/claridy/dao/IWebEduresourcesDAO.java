package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEduresource;
import com.claridy.domain.WebEmployee;

public interface IWebEduresourcesDAO extends IBaseDAO
{
	public List<WebEduresource> findAll(WebEmployee webEmployee);
	
	public List<WebEduresource> search(String keyword,WebEmployee webEmployee);
	
	public WebEduresource getWebEduresById(String uuid);
}
