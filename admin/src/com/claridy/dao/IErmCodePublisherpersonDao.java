package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;

public interface IErmCodePublisherpersonDao extends IBaseDAO {

	public List<ErmCodePublisherPerson> findAll(WebEmployee webEmployee)
			throws DataAccessException;

	public List<ErmCodePublisherPerson> findErmCodePublisherPerson(
			String publisheId, String name, WebEmployee webEmployee)
			throws DataAccessException;

	public ErmCodePublisherPerson findErmCodePublisherPersonById(
			String publisheId, String name, WebEmployee webEmployee)
			throws DataAccessException;

}
