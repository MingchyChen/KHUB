package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebFaqDAO;
import com.claridy.dao.IWebfaqtypeDao;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaq;
import com.claridy.domain.WebFaqType;

@Service
public class WebFaqService {
	
	@Autowired
	private IWebFaqDAO webFaqDao;
	@Autowired
	private IWebfaqtypeDao webFaqTypeDao; 
	
	public WebFaqType findTypeById(String uuid){
		return webFaqTypeDao.findById(uuid);
	}
	
	public List<WebFaq> findAll(WebEmployee webEmployee){
		return webFaqDao.findAll(webEmployee);
	}
	
	public List<WebFaqType> findTypeAll(){
		return webFaqTypeDao.findAll();
	}
	
	
	public WebFaq findById(String uuid){
		return webFaqDao.findById(uuid);
	}
	
	public List<WebFaq> findBy(String keyWord,WebEmployee webEmployee){
		return webFaqDao.findBy(keyWord,webEmployee);
	}
	
	public void delete(WebFaq webFaq){
		webFaq.setIsDataEffid(0);
		webFaqDao.merge(webFaq);
	}
	
	public void update(WebFaq webFaq){
		webFaqDao.merge(webFaq);
	}
	
	public void save(WebFaq webFaq){
		webFaqDao.saveOrUpdate(webFaq);
	}

}
