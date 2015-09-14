package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_ParamDAO;
import com.claridy.common.mechanism.domain.Sys_Param;


@Repository
public class Sys_ParamDAO extends BaseDAO implements ISys_ParamDAO
{ 
	@Autowired  
	public Sys_ParamDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_Param> findAll() throws DataAccessException{
		return (List<Sys_Param>) super.findByCriteria( DetachedCriteria.forClass(Sys_Param.class));
	} 
	
	public Sys_Param find(String func_no,String parent) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_Param.class.getSimpleName());
		sbHql.append(" WHERE func_no = ? and parent = ? ");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, func_no);
		query.setParameter(1, parent);

		return (Sys_Param) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_Param> findByCategory(String category) throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Sys_Param.class);
		detachedCriteria.add(Restrictions.eq("category", category));
		detachedCriteria.add(Restrictions.ne("parent", "0"));
		detachedCriteria.addOrder(Order.asc("seq"));
		return (List<Sys_Param>) super.findByCriteria(detachedCriteria);
	}
	
	public Sys_Param findByValue(String category, String funcValue) throws DataAccessException{
		Criteria criteria = this.getSession().createCriteria(Sys_Param.class);
		criteria.add(Restrictions.eq("category", category));
		if(null != funcValue && !"".equals(funcValue))
			criteria.add(Restrictions.eq("func_value", funcValue));
		criteria.add(Restrictions.ne("parent", "0"));
		criteria.addOrder(Order.asc("seq"));
		return (Sys_Param) criteria.uniqueResult();
	}
	
	/**
	 * 查詢文苑上層類別集合
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sys_Param> findSysParamList(String parent) {
		String hql = "from Sys_Param where parent='"+parent+"'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Sys_Param> list = query.list();
		return list;
	}
	public Sys_Param findByFuncno(String funcno) throws DataAccessException{
		Sys_Param sysparam=null;
		String hql = "from Sys_Param where func_no='"+funcno+"'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		if(query.list().size()>0){
			sysparam=(Sys_Param)query.list().get(0);
		}else{
			sysparam=new Sys_Param();
		}
		return sysparam;
	}
}
