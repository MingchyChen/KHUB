package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.WebEmployee;

public interface IErmResMainDbwsDAO extends IBaseDAO {
	public ErmResourcesMainDbws getResMainDbwsByResId(String resourcesId);
	public int findResourcesMainDbwsList();
	public List<ErmResourcesMainDbws> findAllResMainDbwsList(
			ErmResourcesMainDbws ermResMainDbws, WebEmployee webEmployee);
	public List<ErmResourcesMainDbws> findAllList();
}
