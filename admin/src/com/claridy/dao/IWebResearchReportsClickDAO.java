package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebReSearchReports;
import com.claridy.domain.WebResearchReportsClick;

public interface IWebResearchReportsClickDAO extends IBaseDAO {
	public List<WebResearchReportsClick> findWebReportClickList(Date startDate, Date endDate);
	public List<WebReSearchReports> findAllWebReport();
}
