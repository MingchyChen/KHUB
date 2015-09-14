package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesDbtypeDAO;
import com.claridy.domain.ErmResourcesDbtype;
@Repository
public class ErmResourcesDbtypeDAO extends BaseDAO implements IErmResourcesDbtypeDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public ErmResourcesDbtypeDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesDbtype> getDomain(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesDbtype where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesDbtype> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<ErmResourcesDbtype> findDbtypeList(String resourcesId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesDbtype where resourcesId='"+resourcesId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesDbtype> listRet=query.list();
		return listRet;
	}
	@SuppressWarnings("unchecked")
	public ErmResourcesDbtype getDbtype(String resourcesId,String dbtypeId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesDbtype where resourcesId='"+resourcesId+"' and  dbtypeId='"+dbtypeId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesDbtype> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmResourcesDbtype();
		}
	}
}
