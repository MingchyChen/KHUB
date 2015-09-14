package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_MenuDAO;
import com.claridy.common.mechanism.domain.Sys_Menu;
import com.claridy.domain.WebFunction;


@Repository
public class Sys_MenuDAO extends BaseDAO implements ISys_MenuDAO
{ 
	@Autowired  
	public Sys_MenuDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebFunction> findAll(String languageType) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFunction.class);
		criteria.add(Restrictions.eq("funcParent", "0"));
		criteria.add(Restrictions.eq("functionClass", "leftmenu"));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.asc("seq"));
		return (List<WebFunction>) super.findByCriteria(criteria);
	} 
	@SuppressWarnings("unchecked")
	public List<WebFunction> findAllList(String languageType) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFunction.class);
		criteria.add(Restrictions.eq("functionClass", "leftmenu"));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.asc("seq"));
		return (List<WebFunction>) super.findByCriteria(criteria);
	} 
	public List<WebFunction> getChildMenu(WebFunction webfunction,String languageType){
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFunction.class);
		criteria.add(Restrictions.eq("funcParent", webfunction.getUuid()));
		criteria.add(Restrictions.eq("functionClass", "leftmenu"));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.asc("seq"));
		return (List<WebFunction>) super.findByCriteria(criteria);
	}
	public Object getHasChild(String uuid,String languageType){
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFunction.class);
		criteria.add(Restrictions.eq("funcParent", uuid));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<WebFunction> list=(List<WebFunction>) super.findByCriteria(criteria);
		if(list!=null&&list.size()>0){
			return 1;
		}else{
			return 0;
		}
	}
	public Sys_Menu find(String func_no) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_Menu.class.getSimpleName());
		sbHql.append(" WHERE func_no = ?");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, func_no);

		return (Sys_Menu) query.uniqueResult();
	}
}
