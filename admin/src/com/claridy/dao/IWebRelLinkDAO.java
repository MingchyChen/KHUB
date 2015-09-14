package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebRellink;

public interface IWebRelLinkDAO extends IBaseDAO {
	public List<WebRellink> findWebRelLinkAll(WebEmployee webEmployee);
	public List<WebRellink> findWebRellinkBynameZhTw(String nameZhTw,WebEmployee webEmployee);
	public List<WebRellink> findedtAddList(String searchType,String searchValue);
	public List<WebRellink> findRelLinkByMenu(String menuType);
}
