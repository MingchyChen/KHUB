package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;


public interface IErmCodeGeneralCodeDAO extends IBaseDAO{
	
	public List<ErmCodeGeneralCode> findAll(WebEmployee webEmployee) throws DataAccessException;
	
	public List<ErmCodeGeneralCode> search(String itemId,String generalcodeId,String name1,String name2,String yesOrNo,WebEmployee webEmployee) throws DataAccessException;
	
	public List<ErmCodeGeneralCode> findGeneralList(String openValue,String openType,String name,String code,WebEmployee webEmployee);
	
	public ErmCodeGeneralCode findByItemIDAndGeneralcodeId(String itemId,String generalcodeId);
	
	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeByItemId(String itemId);
	
	public String getNameBySql(String sql);
	
	public Object getObjectBySql(String sql);
	
	public List<ErmCodeGeneralCode> findByItemId(WebEmployee webEmployee,String itemId);
	
	public List<ErmCodeGeneralCode> findBygeneralId(WebEmployee webEmployee,String generalId);
	
	public List<ErmCodeGeneralCode> findGeneralCodeList(String name,String itemId);
}
