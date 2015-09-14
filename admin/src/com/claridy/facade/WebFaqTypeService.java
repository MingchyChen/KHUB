package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebfaqtypeDao;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaqType;

@Service
public class WebFaqTypeService {
	
	@Autowired
	private IWebfaqtypeDao webFaqTypeDAO;
	
	public List<WebFaqType> findAll(WebEmployee webEmployee){
		return webFaqTypeDAO.findAll(webEmployee);
	}
	
	public WebFaqType findById(String uuid){
		return webFaqTypeDAO.findById(uuid);
	}
	
	public void save(WebFaqType webFaqType){
		webFaqTypeDAO.saveOrUpdate(webFaqType);
	}
	
	public void update(WebFaqType webFaqType){
		webFaqTypeDAO.merge(webFaqType);
		
	}
	
	public void delete(WebFaqType webFaqType){
		webFaqType.setIsDataEffid(0);
		webFaqTypeDAO.merge(webFaqType);
	}
	
	public List<WebFaqType> findBy(String keyWord,WebEmployee webEmployee){
		return webFaqTypeDAO.findBy(keyWord,webEmployee);
	}

}
