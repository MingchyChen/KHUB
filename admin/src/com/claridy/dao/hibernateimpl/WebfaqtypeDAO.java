package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebfaqtypeDao;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaqType;

@Repository
public class WebfaqtypeDAO extends BaseDAO implements IWebfaqtypeDao {

	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public WebfaqtypeDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	/**
	 * 查詢全部
	 */
	public List<WebFaqType> findAll(WebEmployee webEmployee) {
		String hql=daoimpl.getHql("WebFaqType", webEmployee);
		hql+=" and isDataEffid=1";
		Session session = this.getSession();
		List<WebFaqType> webFaqTypeList = session.createQuery(hql).list();
		return webFaqTypeList;
	}
	/**
	 * 根據id查詢單筆
	 */
	public WebFaqType findById(String uuid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFaqType.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<WebFaqType> webFaqTypeList = (List<WebFaqType>) super.findByCriteria(criteria);
		return webFaqTypeList.get(0);
	}
	/**
	 * 根據條件查詢
	 */
	public List<WebFaqType> findBy(String keyWord,WebEmployee webEmployee) {
		DetachedCriteria criteria=daoimpl.getCriteris(WebFaqType.class, webEmployee);
		criteria.add(Restrictions.like("titleZhTw","%"+keyWord+"%"));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<WebFaqType> webFaqTypeList = (List<WebFaqType>) super.findByCriteria(criteria);
		return webFaqTypeList;
	}

	public List<WebFaqType> findAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFaqType.class);
		criteria.add(Restrictions.eq("isDataEffid",1));
		criteria.add(Restrictions.eq("isdisplay",1));
		criteria.addOrder(Order.asc("sortnum"));
		List<WebFaqType> webFaqTypeList = (List<WebFaqType>) super.findByCriteria(criteria);
		return webFaqTypeList;
	}

}
