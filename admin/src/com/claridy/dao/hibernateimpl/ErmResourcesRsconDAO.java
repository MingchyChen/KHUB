package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesRsconDAO;
import com.claridy.domain.ErmResourcesRscon;

@Repository
public class ErmResourcesRsconDAO extends BaseDAO implements
		IErmResourcesRsconDAO {

	@Autowired
	public ErmResourcesRsconDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public ErmResourcesRscon findByResourcesId(String resourcesId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesRscon.class);
		if(resourcesId!=null){
			criteria.add(Restrictions.eq("resourcesId", resourcesId));
		}
		return (ErmResourcesRscon) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}

}
