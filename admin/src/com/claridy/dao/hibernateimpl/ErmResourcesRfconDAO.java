package com.claridy.dao.hibernateimpl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IermResourcesRfconDAO;
import com.claridy.domain.ErmResourcesRfcon;
@Repository
public class ErmResourcesRfconDAO extends BaseDAO implements
		IermResourcesRfconDAO {

	@Autowired
	public ErmResourcesRfconDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public ErmResourcesRfcon findById(String resourcesId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesRfcon.class);
		if(resourcesId!=null){
			criteria.add(Restrictions.eq("resourcesId", resourcesId));
		}
		return (ErmResourcesRfcon) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}

}
