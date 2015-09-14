package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesMainDbws;

public interface IErmResourcesMainDbwsDAO extends IBaseDAO {
	
	public List<ErmResourcesMainDbws> findAll(String typeid,String id,String name);

}
