package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesScollegeLog;

public interface IErmResourcesScollegeLogDAO extends IBaseDAO {
	
	public List<ErmResourcesScollegeLog> findScoLogList(String resourcesId);
	public Integer getScollegeLog(String resourcesId,String suitcollegeId,String dbId);
}
