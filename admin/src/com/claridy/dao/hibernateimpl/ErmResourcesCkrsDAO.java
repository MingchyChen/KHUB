package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesCkrsDAO;
import com.claridy.domain.ErmResourcesCkrs;

@Repository
public class ErmResourcesCkrsDAO extends BaseDAO implements
		IErmResourcesCkrsDAO {

	@Autowired
	public ErmResourcesCkrsDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<ErmResourcesCkrs> findAll() {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesCkrs.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.desc("createDate"));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	/**
	 * isdataeffid為1時
	 */
	@SuppressWarnings("unchecked")
	public ErmResourcesCkrs findByResourId(String resourId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesCkrs.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions.eq("resourcesId",resourId ));
		List<ErmResourcesCkrs> ermResourcesCkrs=criteria.getExecutableCriteria(getSession()).list();
		if(ermResourcesCkrs.size()>0){
			return ermResourcesCkrs.get(0);
		}
		return new ErmResourcesCkrs();
	}
	/**
	 * 查询
	 */
	public ErmResourcesCkrs getByResourId(String resourId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesCkrs.class);
		criteria.add(Restrictions.eq("resourcesId",resourId ));
		List<ErmResourcesCkrs> ermResourcesCkrs=criteria.getExecutableCriteria(getSession()).list();
		if(ermResourcesCkrs.size()>0){
			return ermResourcesCkrs.get(0);
		}
		return new ErmResourcesCkrs();
	}

}
