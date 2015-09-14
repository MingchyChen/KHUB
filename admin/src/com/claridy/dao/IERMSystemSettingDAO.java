package com.claridy.dao;


import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;

public interface IERMSystemSettingDAO extends IBaseDAO
{
	public List<ErmSystemSetting> findAll(WebEmployee webEmployee) throws DataAccessException;
	
	public List<ErmSystemSetting> search(String funName,WebEmployee webEmployee) throws DataAccessException;
	
	public List<ErmSystemSetting> findSysSettingList(String term);
	 
	public ErmSystemSetting findByFunID(String funId)throws DataAccessException;
	
	public ErmSystemSetting getSysByFunID(String funId);
}
