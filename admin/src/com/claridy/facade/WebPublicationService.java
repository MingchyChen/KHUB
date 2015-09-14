package com.claridy.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebPubLicationDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebPublication;

@Service
public class WebPublicationService {
	private final Logger log=LoggerFactory.getLogger(getClass());
	@Autowired
	private IWebPubLicationDAO webPubLicationDAO;
	
	
	public List<WebPublication> findAll(WebEmployee webEmployee){
		return webPubLicationDAO.findBy(null,webEmployee);
	}
	
	public WebPublication findById(String uuid,WebEmployee webEmployee){
		WebPublication webPubLication=new WebPublication();
		webPubLication.setUuid(uuid);
		webPubLication.setIsDisplay(-1);
		return webPubLicationDAO.findBy(webPubLication,webEmployee).get(0);
	}
	
	public List<WebPublication> findByConditions(WebPublication webPubLication,WebEmployee webEmployee){
		return webPubLicationDAO.findBy(webPubLication,webEmployee);
	}
	
	public void deleteWebPubLication(WebPublication webPubLication){
		try {
			webPubLicationDAO.merge(webPubLication);
		} catch (Exception e) {
			log.error("修改農業出版品報錯",e);
			webPubLicationDAO.saveOrUpdate(webPubLication);
		}
	}
	
	public void updateWebPubLication(WebPublication webPubLication){
		try {
			webPubLicationDAO.merge(webPubLication);
		} catch (Exception e) {
			log.error("修改農業出版品報錯",e);
			webPubLicationDAO.saveOrUpdate(webPubLication);
		}
	}
	
	public void saveWebPubLication(WebPublication webPubLication){
		webPubLicationDAO.saveOrUpdate(webPubLication);
	}
	
}
