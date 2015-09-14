package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebEmployee;

public interface IErmCodeDbDAO extends IBaseDAO{
	public List<ErmCodeDb> findPublisherList(String openValue,String openType,String name,String code,WebEmployee webEmployee);
	public List<ErmCodeDb> findcodeDbByDbId(String dbId,WebEmployee webEmployee);
	public List<ErmCodeDb> findAllCodeDb(WebEmployee webEmployee);
	public List<ErmCodeDb> findErmCodeDbList(String name)throws DataAccessException;
	public List<ErmCodeDb> findAllCodeDbIsUse();
	public void updateErmEjebItemByDbId(String dbId);
	public ErmCodeDb getErmDbByDbId(String dbId);
}
