package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IERMCodePublisherDAO;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;

@Repository
public class ERMCodePublisherDAO extends BaseDAO implements IERMCodePublisherDAO {

	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ERMCodePublisherDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodePublisher> findAll(WebEmployee webEmployee) throws DataAccessException {
		//DetachedCriteria criteria = DetachedCriteria.forClass(ERMSystemSetting.class);
		DetachedCriteria criteria=daoimpl.getCriteris(ErmCodePublisher.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
//		criteria.add(Restrictions.eq("isDefault", "N"));
		criteria.addOrder(Order.desc("createDate"));
		List<ErmCodePublisher> tmpErmCodePublisherList = (List<ErmCodePublisher>) super
				.findByCriteria(criteria);
		return tmpErmCodePublisherList;
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodePublisher> search(String publisherId,String name,WebEmployee webEmployee)
			throws DataAccessException {
		DetachedCriteria criteria=daoimpl.getCriteris(ErmCodePublisher.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions
				.like("publisherId", publisherId, MatchMode.ANYWHERE));
		criteria.add(Restrictions
				.like("name", name, MatchMode.ANYWHERE));
//		criteria.add(Restrictions.eq("isDefault", "N"));
		criteria.addOrder(Order.desc("createDate"));
		return (List<ErmCodePublisher>) super.findByCriteria(criteria);
	}

	public ErmCodePublisher findByPublisherId(String publisherId)
			throws DataAccessException {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + ErmCodePublisher.class.getSimpleName());
//		sbHql.append(" WHERE isDefault ='N' and isDataEffid=1 and funcId = ? order by createDate");
		sbHql.append(" WHERE isDataEffid=1 and publisherId = ? order by createDate");
		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, publisherId);

		return (ErmCodePublisher) query.uniqueResult();
	}
	/**
	 * 根據條件查詢集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodePublisher> findPublisherList(String openValue,String openType,String name,String code,WebEmployee webEmployee){
		String hql=daoimpl.getHql("ErmCodePublisher", webEmployee);
		hql += " and isDataEffid = 1";
		if (!code.equalsIgnoreCase("")) {
			hql+=" and lower(publisherId) like '%" + code.toLowerCase() + "%'";
		}
		if (!name.equalsIgnoreCase("")) {
			hql+=" and lower(name) like '%" + name.toLowerCase() + "%'";
		}
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		List<ErmCodePublisher> list=query.list();
        return list;
	}
	/**
	 * 根據名稱查詢代理商集合
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodePublisher> findPublisherList(String name)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ErmCodePublisher.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions.eq("name", name));
		return (List<ErmCodePublisher>) super.findByCriteria(criteria);
	}
}
