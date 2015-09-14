package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebAdwall;
import com.claridy.domain.WebEmployee;

public interface IWebAdwallDAO extends IBaseDAO
{
	public List<WebAdwall> findAll(WebEmployee webEmployee);
	
	public List<WebAdwall> search(String adnameZhTw,WebEmployee webEmployee);
	
	public WebAdwall getWebAdwallById(String uuid);
}
