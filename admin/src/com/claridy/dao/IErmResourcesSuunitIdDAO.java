package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesSuunit;

public interface IErmResourcesSuunitIdDAO extends IBaseDAO {
	
	public List<ErmResourcesSuunit> getDomain(String resourcesId);
}
