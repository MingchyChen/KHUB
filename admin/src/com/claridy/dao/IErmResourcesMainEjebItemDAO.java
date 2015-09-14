package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesEjebItem;

public interface IErmResourcesMainEjebItemDAO extends IBaseDAO{
	public List<ErmResourcesEjebItem> getResMainEjebItemByResId(String resourcesId);
	public ErmResourcesEjebItem getMainEjebItemByResId(String resourcesId);
}
