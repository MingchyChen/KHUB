package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmCategoryDAO;
import com.claridy.domain.ErmCodeCategory;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmCategoryDAO extends BaseDAO implements IErmCategoryDAO
{ 
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired  
	public ErmCategoryDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmCodeCategory> findAll(String typeId,String categoryType,WebEmployee webEmployee){
		DetachedCriteria criteria=daoimpl.getCriteris(ErmCodeCategory.class, webEmployee);
		criteria.add(Restrictions.eq("typeId", typeId));
		criteria.add(Restrictions.eq("categoryType", categoryType));
		//criteria.add( Restrictions.sqlRestriction(" (history='N' or history is null or history='') "));
		return (List<ErmCodeCategory>) super.findByCriteria( criteria);
	} 
}
