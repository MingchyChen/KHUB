package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebPublication;

public interface IWebPubLicationDAO extends IBaseDAO {

	
	public List<WebPublication> findBy(WebPublication webPubLication,WebEmployee webEmployee);
}
