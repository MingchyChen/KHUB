package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesOdetails;

public interface IErmResourcesOdetailsDAO extends IBaseDAO {
	
	public List<ErmResourcesOdetails> findOdetailsList(String resourcesId);
	public ErmResourcesOdetails getOdetails(String resourcesId,String year);
}
