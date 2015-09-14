package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;

public interface IWebEduTrainingDAO extends IBaseDAO {

	public List<WebEduTraining> findBy(Date startDate,Date endDate,String uuid,WebEmployee webEmployee);
	public List<WebEduTraining> findedtAddList(String searchType,String searchValue);
}
