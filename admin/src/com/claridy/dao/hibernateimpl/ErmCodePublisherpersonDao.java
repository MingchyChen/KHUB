package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmCodePublisherpersonDao;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmCodePublisherpersonDao extends BaseDAO implements
		IErmCodePublisherpersonDao {

	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmCodePublisherpersonDao(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodePublisherPerson> findAll(WebEmployee webEmployee)
			throws DataAccessException {
		String hql = daoimpl.getHql("ErmCodePublisherPerson", webEmployee);
		hql += " and isDataEffid = 1";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodePublisherPerson> findErmCodePublisherPerson(
			String publisheId, String name, WebEmployee webEmployee)
			throws DataAccessException {
		String hql = daoimpl.getHql("ErmCodePublisherPerson", webEmployee);
		hql += " and isDataEffid = 1";
		if (publisheId != null && !publisheId.equals("")) {
			hql += " and publisherId = '" + publisheId + "'";
		}
		if (name != null && !name.equals("")) {
			hql += " and personName like '%" + name + "%'";
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	public ErmCodePublisherPerson findErmCodePublisherPersonById(
			String publisheId, String name, WebEmployee webEmployee)
			throws DataAccessException {
		String hql = daoimpl.getHql("ErmCodePublisherPerson", webEmployee);
		hql += " and isDataEffid = 1 and publisherId = '" + publisheId + "' and personName = '" + name + "'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		return (ErmCodePublisherPerson) query.uniqueResult();
	}

}
