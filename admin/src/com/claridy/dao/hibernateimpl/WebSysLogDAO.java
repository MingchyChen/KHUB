package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebSysLogDAO;
import com.claridy.domain.WebSysLog;

@Repository
public class WebSysLogDAO extends BaseDAO implements IWebSysLogDAO {
	@Autowired
	public WebSysLogDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebSysLog> search(String nLocate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebSysLog.class);
		criteria.add(Restrictions.eq("nlocate", nLocate));
		criteria.addOrder(Order.desc("ndate"));
		List<WebSysLog> tmpWebSysLogList = (List<WebSysLog>) super
				.findByCriteria(criteria);
		return tmpWebSysLogList;
	}

}
