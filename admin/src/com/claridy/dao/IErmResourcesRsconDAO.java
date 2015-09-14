package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesRscon;

public interface IErmResourcesRsconDAO extends IBaseDAO {

	public ErmResourcesRscon findByResourcesId(String resourcesId);
}
