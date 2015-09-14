package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebSearchInfoDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;

@Service
public class WebSearchInfoService {
	@Autowired
	private IWebSearchInfoDAO webSearchInfoDAO;
	
	public List<WebSearchInfo> findSearchInfoAll(WebEmployee webEmployee){
		return webSearchInfoDAO.findSearchInfoAll(webEmployee);
	}
	public List<WebSearchInfo> findSearchInfoByNameZhTw(String nameZhTw,WebEmployee webEmployee){
		return webSearchInfoDAO.findsearchInfoByNameZhTw(nameZhTw, webEmployee);
	}
	
	public void deleteSearchInfobyUuid(WebSearchInfo webSearchInfo){
		webSearchInfo.setIsDataEffid(0);
		webSearchInfoDAO.update(webSearchInfo);
	}
	public List<WebSearchInfo> findedtAddList(String searchType,String searchValue){
		return webSearchInfoDAO.findedtAddList(searchType, searchValue);
	}
	public void addSearchInfo(WebSearchInfo webSearchInfo){
		webSearchInfo.setIsDataEffid(1);
		webSearchInfoDAO.saveOrUpdate(webSearchInfo);
	}
	public void updateSearchInfo(WebSearchInfo webSearchInfo){
		webSearchInfo.setIsDataEffid(1);
		webSearchInfoDAO.update(webSearchInfo);
	}
	
}
