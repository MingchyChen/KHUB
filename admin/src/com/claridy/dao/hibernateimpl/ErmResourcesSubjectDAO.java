package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesSubjectIdDAO;
import com.claridy.domain.ErmResourcesSubject;
@Repository
public class ErmResourcesSubjectDAO extends BaseDAO implements IErmResourcesSubjectIdDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public ErmResourcesSubjectDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesSubject> getDomain(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesSubject where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesSubject> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<ErmResourcesSubject> findSubjectList(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesSubject where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesSubject> listRet=query.list();
		return listRet;
	}
	@SuppressWarnings("unchecked")
	public ErmResourcesSubject getSubject(String resourcesId,String subjectId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesSubject where resourcesId='"+resourcesId+"' and  subjectId='"+subjectId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesSubject> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmResourcesSubject();
		}
	}
}
