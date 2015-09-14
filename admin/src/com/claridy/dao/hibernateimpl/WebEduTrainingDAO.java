package com.claridy.dao.hibernateimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebEduTrainingDAO;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;

@Repository
public class WebEduTrainingDAO extends BaseDAO implements IWebEduTrainingDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public WebEduTrainingDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebEduTraining> findBy(Date startDate, Date endDate,
			String uuid, WebEmployee webEmployee) {
		// DetachedCriteria
		// criteria=DetachedCriteria.forClass(WebEduTraining.class);
		DetachedCriteria criteria = daoimpl.getCriteris(WebEduTraining.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		if (startDate != null) {
			if (endDate != null) {
				criteria.add(Restrictions.between("startDate", startDate,
						endDate));
			} else {
				criteria.add(Restrictions.ge("startDate", startDate));
			}
		} else if (endDate != null) {
			criteria.add(Restrictions.le("startDate", endDate));
		}

		if (uuid != null) {
			criteria.add(Restrictions.eq("uuid", uuid));
		}
		List<WebEduTraining> webEduList = criteria.getExecutableCriteria(
				getSession()).list();
		return webEduList;
	}

	@SuppressWarnings("unchecked")
	public List<WebEduTraining> findedtAddList(String searchType,
			String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from WebEduTraining where isDataEffid=1";
			query = session.createQuery(hql);
		} else {
			hql = "from WebEduTraining where isDataEffid=1 and " + searchType
					+ "='" + searchValue + "'";
			query = session.createQuery(hql);
		}
		List<WebEduTraining> list = query.list();
		return list;
	}

}
