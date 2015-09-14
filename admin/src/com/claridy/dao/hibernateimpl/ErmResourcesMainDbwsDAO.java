package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesMainDbwsDAO;
import com.claridy.domain.ErmResourcesMainDbws;

@Repository
public class ErmResourcesMainDbwsDAO extends BaseDAO implements
		IErmResourcesMainDbwsDAO {

	@Autowired
	public ErmResourcesMainDbwsDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public List<ErmResourcesMainDbws> findAll(String typeId,String id,String name) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainDbws.class);
		criteria.add(Restrictions.eq("typeId",typeId));
		if(id!=null&&!"".equals(id)){
			criteria.add(Restrictions.eq("resourcesId",id));
		}
		if(name!=null&&!"".equals(name)){
			criteria.add(Restrictions.eq("title",name));
		}
		return criteria.getExecutableCriteria(getSession()).list();
	}

}
