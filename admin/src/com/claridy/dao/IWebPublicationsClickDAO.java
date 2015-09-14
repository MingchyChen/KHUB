package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebPublication;
import com.claridy.domain.WebPublicationsClick;

public interface IWebPublicationsClickDAO extends IBaseDAO {
	public List<WebPublicationsClick> findWebPubClickList(Date startDate, Date endDate);
	public List<WebPublication> findAllWebPub();
}
