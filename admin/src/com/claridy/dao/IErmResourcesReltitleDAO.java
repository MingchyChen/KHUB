package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesReltitle;

public interface IErmResourcesReltitleDAO extends IBaseDAO {
	
	public List<ErmResourcesReltitle> findReltitleList(String resourcesId);
	public ErmResourcesReltitle getReltitle(String resourcesId,String relatedTitleId);
}
