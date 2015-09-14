package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmPersonalizeRescount;

public interface IErmPersonalizeRescountDAO extends IBaseDAO {
	
	public List<ErmPersonalizeRescount> findPersonalizeRescountList();
	public ErmPersonalizeRescount getPersonalizeRescount(String accountId,String resourcesId,String dbId);
}
