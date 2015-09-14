package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeCategory;
import com.claridy.domain.WebEmployee;

public interface IErmCategoryDAO extends IBaseDAO
{
	public List<ErmCodeCategory> findAll(String typeId,String categoryType,WebEmployee webEmployee) throws DataAccessException;
}
