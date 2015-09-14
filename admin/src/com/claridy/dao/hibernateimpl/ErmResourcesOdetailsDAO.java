package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesOdetailsDAO;
import com.claridy.domain.ErmResourcesOdetails;
@Repository
public class ErmResourcesOdetailsDAO extends BaseDAO implements IErmResourcesOdetailsDAO {
	
	@Autowired
	public ErmResourcesOdetailsDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesOdetails> findOdetailsList(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesOdetails where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesOdetails> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public ErmResourcesOdetails getOdetails(String resourcesId,String year) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesOdetails where resourcesId='"+resourcesId+"' and  year='"+year+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesOdetails> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmResourcesOdetails();
		}
	}
}
