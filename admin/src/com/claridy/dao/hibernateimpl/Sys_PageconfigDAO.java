package com.claridy.dao.hibernateimpl;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.ISys_PageconfigDAO;
import com.claridy.domain.Sys_Pageconfig;

@Repository
public class Sys_PageconfigDAO extends BaseDAO implements ISys_PageconfigDAO {

	@Autowired
	public Sys_PageconfigDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public Sys_Pageconfig findBySchoolID(String schoolID) {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_Pageconfig.class.getSimpleName());
		sbHql.append(" WHERE school_id = ? ");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, schoolID);

		return (Sys_Pageconfig) query.uniqueResult();
	}

}
