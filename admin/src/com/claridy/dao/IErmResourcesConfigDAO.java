package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesConfig;
import com.claridy.domain.WebEmployee;

public interface IErmResourcesConfigDAO extends IBaseDAO {
	
	
	public ErmResourcesConfig findById(WebEmployee webEmployee,String generalCodeId);

	
	public List<ErmResourcesConfig> findAll(WebEmployee webEmployee);
}
