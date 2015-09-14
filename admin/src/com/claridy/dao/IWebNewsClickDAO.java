package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebNewsClick;

public interface IWebNewsClickDAO extends IBaseDAO {
	public List<WebNewsClick> findWebNewsClickList(Date startDate, Date endDate);
	public List<ErmNews> findAllWebNews();
}
