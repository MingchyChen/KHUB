package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebEduresourcesDAO;
import com.claridy.domain.WebEduresource;
import com.claridy.domain.WebEmployee;

@Repository
public class WebEduresourcesDAO extends BaseDAO implements IWebEduresourcesDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebEduresourcesDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebEduresource> findAll(WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(WebEduresource.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.desc("createDate"));
		List<WebEduresource> tmpEduresourcesList = (List<WebEduresource>) super
				.findByCriteria(criteria);
		return tmpEduresourcesList;
	}

	@SuppressWarnings("unchecked")
	public List<WebEduresource> search(String keyword,WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(WebEduresource.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions
				.like("titleZhTw", keyword, MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("createDate"));
		return (List<WebEduresource>) super.findByCriteria(criteria);
	}
	
	public WebEduresource getWebEduresById(String uuid){
		try {
			StringBuffer sbHql = new StringBuffer();
			sbHql.append(" FROM " + WebEduresource.class.getSimpleName());
			sbHql.append(" WHERE uuid = ?  and isDataEffid=1 ");
			Query query = this.getSession().createQuery(sbHql.toString());
			query.setParameter(0, uuid);
			return (WebEduresource) query.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}
}
