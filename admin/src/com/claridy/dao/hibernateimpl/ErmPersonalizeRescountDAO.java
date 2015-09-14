package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmPersonalizeRescountDAO;
import com.claridy.domain.ErmPersonalizeRescount;
@Repository
public class ErmPersonalizeRescountDAO extends BaseDAO implements IErmPersonalizeRescountDAO {
	
	@Autowired
	public ErmPersonalizeRescountDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmPersonalizeRescount> findPersonalizeRescountList() {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmPersonalizeRescount";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmPersonalizeRescount> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public ErmPersonalizeRescount getPersonalizeRescount(String accountId,String resourcesId,String dbId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmPersonalizeRescount where accountid='"+accountId+"' and resourcesId='"+resourcesId+"'  and dbId='"+dbId+"' and isDataEffid=1";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmPersonalizeRescount> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmPersonalizeRescount();
		}
	}
}
