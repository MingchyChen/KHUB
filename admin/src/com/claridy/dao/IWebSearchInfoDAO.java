package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;

public interface IWebSearchInfoDAO extends IBaseDAO{
	public List<WebSearchInfo> findSearchInfoAll(WebEmployee webEmployee);
	public List<WebSearchInfo> findsearchInfoByNameZhTw(String nameZhTw,WebEmployee webEmployee);
	public List<WebSearchInfo> findedtAddList(String searchType,String searchValue);
}
