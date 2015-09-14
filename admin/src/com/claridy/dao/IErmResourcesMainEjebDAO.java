package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.WebEmployee;

public interface IErmResourcesMainEjebDAO extends IBaseDAO {
	public void updateErmResMainEjeb(String sql);

	public ErmResourcesMainEjeb getResMainEjebByResId(String resourcesId);

	public List<ErmResourcesMainEjeb> findAllResMainEjebList();
	
	public List<ErmResourcesMainEjeb> findAllResMainEbList(String typeId,String id,String name);

	public List<ErmResourcesEjebItem> findAllResMainEjebItemList(
			ErmResourcesEjebItem ermResourcesEjebItem, WebEmployee webEmployee);
}
