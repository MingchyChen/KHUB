package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesConfigDAO;
import com.claridy.domain.ErmResourcesConfig;
import com.claridy.domain.WebEmployee;
@Repository
public class ErmResourcesConfigDAO extends BaseDAO implements IErmResourcesConfigDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public ErmResourcesConfigDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	
	@SuppressWarnings("unchecked")
	public ErmResourcesConfig findById(WebEmployee webEmployee,String generalCodeId) {
		DetachedCriteria criteria=daoimpl.getCriteris(ErmResourcesConfig.class, webEmployee);
		criteria.add(Restrictions.eq("typeId", generalCodeId));
		return  (ErmResourcesConfig) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesConfig> findAll(WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(ErmResourcesConfig.class, webEmployee);
		return criteria.getExecutableCriteria(getSession()).list();
	}

}
