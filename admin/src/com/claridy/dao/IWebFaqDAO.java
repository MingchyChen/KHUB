package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaq;

public interface IWebFaqDAO extends IBaseDAO {

	public List<WebFaq> findAll(WebEmployee webEmployee);
	
	public WebFaq findById(String uuid);
	
	public List<WebFaq> findBy(String keyWord,WebEmployee webEmployee);
	
}
