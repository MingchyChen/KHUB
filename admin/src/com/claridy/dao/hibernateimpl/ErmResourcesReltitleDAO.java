package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesReltitleDAO;
import com.claridy.domain.ErmResourcesReltitle;
@Repository
public class ErmResourcesReltitleDAO extends BaseDAO implements IErmResourcesReltitleDAO {
	
	@Autowired
	public ErmResourcesReltitleDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesReltitle> findReltitleList(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesReltitle where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesReltitle> list=query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ErmResourcesReltitle getReltitle(String resourcesId,String relatedTitleId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesReltitle where resourcesId='"+resourcesId+"' and  relatedTitleId='"+relatedTitleId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesReltitle> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmResourcesReltitle();
		}
	}

}
