package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IWebHomeLinkDAO;
import com.claridy.common.mechanism.domain.WebHomeLink;


@Repository
public class WebHomeLinkDAO extends BaseDAO implements IWebHomeLinkDAO
{ 
	@Autowired  
	public WebHomeLinkDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebHomeLink> findAll() throws DataAccessException{
		return (List<WebHomeLink>) super.findByCriteria( DetachedCriteria.forClass(WebHomeLink.class));
	} 
	
	public WebHomeLink find(String func_no) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + WebHomeLink.class.getSimpleName());
		sbHql.append(" WHERE func_no = ?");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, func_no);

		return (WebHomeLink) query.uniqueResult();
	}
	/**
	 * 查詢菜單集合
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebHomeLink> findMenuList() {
		String hql = "from WebHomeLink where func_no not in ('wxcldl0001')";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<WebHomeLink> list = query.list();
		return list;
	}
}
