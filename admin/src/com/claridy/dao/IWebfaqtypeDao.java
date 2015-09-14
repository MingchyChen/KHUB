package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaqType;

public interface IWebfaqtypeDao extends IBaseDAO {
	
	public List<WebFaqType> findAll(WebEmployee webEmployee);
	
	public List<WebFaqType> findAll();
	
	public WebFaqType findById(String uuid);
	
	
	public List<WebFaqType> findBy(String keyWord,WebEmployee webEmployee);
	
	

}
