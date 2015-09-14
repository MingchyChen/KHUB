package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesCkrs;

public interface IErmResourcesCkrsDAO extends IBaseDAO {

	public List<ErmResourcesCkrs> findAll();
	
	public ErmResourcesCkrs findByResourId(String resourId);
	
	public ErmResourcesCkrs getByResourId(String resourId);
	
	
	
}
