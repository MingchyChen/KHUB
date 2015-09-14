package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesDbtype;

public interface IErmResourcesDbtypeDAO extends IBaseDAO {
	
	public List<ErmResourcesDbtype> getDomain(String dbId);
	public List<ErmResourcesDbtype> findDbtypeList(String resourcesId);
	public ErmResourcesDbtype getDbtype(String resourcesId,String dbtypeId);
}
