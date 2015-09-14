package com.claridy.dao.hibernateimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebFunctionDAO;

@Repository
public class WebFunctionDAO extends BaseDAO implements IWebFunctionDAO {
	@Autowired
	public WebFunctionDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

}
