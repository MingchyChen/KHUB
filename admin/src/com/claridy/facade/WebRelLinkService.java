package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebRelLinkDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebRellink;

@Service
public class WebRelLinkService {
	@Autowired
	private IWebRelLinkDAO webRelLinkDAO;
	
	public List<WebRellink> findwebRelLinkAll(WebEmployee webEmployee){
		return webRelLinkDAO.findWebRelLinkAll(webEmployee);
	}
	public List<WebRellink> findwenRelLinkBynameZhTw(String nameZhTw,WebEmployee webEmployee){
		return webRelLinkDAO.findWebRellinkBynameZhTw(nameZhTw,webEmployee);
	}
	public List<WebRellink> findedtAddList(String searchType,String searchValue){
		return webRelLinkDAO.findedtAddList(searchType, searchValue);
	}
	public void deleteWebRelLink(WebRellink webRellink){
		webRellink.setIsDataEffid(0);
		webRelLinkDAO.saveOrUpdate(webRellink);
	}
	public void addWebRelLink(WebRellink webRellink){
		webRellink.setIsDataEffid(1);
		webRelLinkDAO.saveOrUpdate(webRellink);
	}
	
	public void updateWebRelLink(WebRellink webRellink){
		webRellink.setIsDataEffid(1);
		webRelLinkDAO.merge(webRellink);
	}
}
