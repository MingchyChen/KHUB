package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;

public interface IERMCodePublisherDAO extends IBaseDAO {

	public List<ErmCodePublisher> findAll(WebEmployee webEmployee) throws DataAccessException;
	
	public List<ErmCodePublisher> search(String publisherId,String name,WebEmployee webEmployee) throws DataAccessException;
	 
	public ErmCodePublisher findByPublisherId(String publisherId)throws DataAccessException;
	
	public List<ErmCodePublisher> findPublisherList(String openValue,String openType,String name,String code,WebEmployee webEmployee);
	
	public List<ErmCodePublisher> findPublisherList(String name)throws DataAccessException;
}
