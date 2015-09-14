package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebAdwallDAO;
import com.claridy.domain.WebAdwall;
import com.claridy.domain.WebEmployee;

@Service
public class WebAdwallService {

	@Autowired
	public IWebAdwallDAO webAdwallDAO;

	public List<WebAdwall> findAll(WebEmployee webEmployee) {
		return webAdwallDAO.findAll(webEmployee);
	}

	public List<WebAdwall> search(String adnameZhTw,WebEmployee webEmployee) {
		return webAdwallDAO.search(adnameZhTw,webEmployee);
	}
	
	public WebAdwall getWebAdwallById(String uuid) {
		return webAdwallDAO.getWebAdwallById(uuid);
	}

	public void save(WebAdwall webAdwall) {
		webAdwallDAO.saveOrUpdate(webAdwall);
	}

	public void update(WebAdwall webAdwall) {
		webAdwallDAO.merge(webAdwall);
	}

	public void delete(String uuid) {
		WebAdwall webAdwall = webAdwallDAO.getWebAdwallById(uuid);
		if(webAdwall!=null){
			webAdwall.setIsDataEffid(0);
			webAdwallDAO.merge(webAdwall);
		}
	}
}
