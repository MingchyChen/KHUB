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
import com.claridy.dao.IWebAdwallDAO;
import com.claridy.domain.WebAdwall;
import com.claridy.domain.WebEmployee;

@Repository
public class WebAdwallDAO extends BaseDAO implements IWebAdwallDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebAdwallDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebAdwall> findAll(WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(WebAdwall.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.desc("createDate"));
		List<WebAdwall> tmpERMSystemSettingList = (List<WebAdwall>) super
				.findByCriteria(criteria);
		return tmpERMSystemSettingList;
	}

	@SuppressWarnings("unchecked")
	public List<WebAdwall> search(String adnameZhTw,WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(WebAdwall.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions
				.like("adnameZhTw", adnameZhTw, MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("createDate"));
		return (List<WebAdwall>) super.findByCriteria(criteria);
	}
	
	public WebAdwall getWebAdwallById(String uuid){
		try {
			StringBuffer sbHql = new StringBuffer();
			sbHql.append(" FROM " + WebAdwall.class.getSimpleName());
			sbHql.append(" WHERE uuid = ?  and isDataEffid=1 ");
			Query query = this.getSession().createQuery(sbHql.toString());
			query.setParameter(0, uuid);
			return (WebAdwall) query.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}
}
