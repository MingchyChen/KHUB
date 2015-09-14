package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;

public interface IErmCodeItemDao extends IBaseDAO {

	public List<ErmCodeItem> findErmCodeItem(ErmCodeItem ermCodeItem,
			WebEmployee webemployee) throws DataAccessException;

	public ErmCodeItem findByItemId(String itemId) throws DataAccessException;

	public List<ErmCodeItem> findByhistory(WebEmployee webemployee)
			throws DataAccessException;

	public void deleteItems(String itemId) throws DataAccessException;

	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeItemId(String itemId,
			WebEmployee webemployee) throws DataAccessException;

}
