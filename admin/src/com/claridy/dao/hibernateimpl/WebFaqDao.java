package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebFaqDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaq;
import com.claridy.domain.WebFaqType;

@Repository
public class WebFaqDao extends BaseDAO implements IWebFaqDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public WebFaqDao(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public List<WebFaq> findAll(WebEmployee webEmployee) {
		String hql=daoimpl.getHql("WebFaq", webEmployee);
		hql+=" and isDataEffid=1";
		Session session = this.getSession();
		List<WebFaq> webFaqList = session.createQuery(hql).list();
		return webFaqList;
	}

	public WebFaq findById(String uuid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFaq.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<WebFaq> webFaqList = (List<WebFaq>) super.findByCriteria(criteria);
		return webFaqList.get(0);
	}

	public List<WebFaq> findBy(String keyWord,WebEmployee webEmployee) {
		DetachedCriteria criteria=daoimpl.getCriteris(WebFaq.class, webEmployee);
		criteria.add(Restrictions.like("titleZhTw","%"+keyWord+"%"));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<WebFaq> webFaqList = (List<WebFaq>) super.findByCriteria(criteria);
		return webFaqList;
	}



}
