package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebFarmingNews;
import com.claridy.domain.WebFarmingNewsClick;

public interface IWebFarmingNewsClickDAO extends IBaseDAO {
	public List<WebFarmingNewsClick> findWebFarmClickList(Date startDate, Date endDate);
	public List<WebFarmingNews> findAllWebFarm();
}
