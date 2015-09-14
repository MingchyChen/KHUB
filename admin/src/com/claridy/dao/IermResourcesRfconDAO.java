package com.claridy.dao;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesRfcon;

public interface IermResourcesRfconDAO extends IBaseDAO {
	
	public ErmResourcesRfcon findById(String resourcesId);

}
