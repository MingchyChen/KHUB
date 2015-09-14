package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebEduresourcesDAO;
import com.claridy.domain.WebEduresource;
import com.claridy.domain.WebEmployee;

@Service
public class WebEduresourcesService {

	@Autowired
	public IWebEduresourcesDAO webeduresDAO;

	public List<WebEduresource> findAll(WebEmployee webEmployee) {
		return webeduresDAO.findAll(webEmployee);
	}

	public List<WebEduresource> search(String adnameZhTw,WebEmployee webEmployee) {
		return webeduresDAO.search(adnameZhTw,webEmployee);
	}
	
	public WebEduresource getWebAdwallById(String uuid) {
		return webeduresDAO.getWebEduresById(uuid);
	}

	public void save(WebEduresource webAdwall) {
		webeduresDAO.saveOrUpdate(webAdwall);
	}

	public void update(WebEduresource webAdwall) {
		webeduresDAO.merge(webAdwall);
	}

	public void delete(String uuid) {
		WebEduresource webAdwall = webeduresDAO.getWebEduresById(uuid);
		if(webAdwall!=null){
			webAdwall.setIsDataEffid(0);
			webeduresDAO.merge(webAdwall);
		}
	}
}
