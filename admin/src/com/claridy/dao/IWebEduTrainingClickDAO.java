package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEduTrainingClick;

public interface IWebEduTrainingClickDAO extends IBaseDAO {
	public List<WebEduTrainingClick> findWebEduClickList(Date startDate, Date endDate);
	public List<WebEduTraining> findAllWebEdu();
}
