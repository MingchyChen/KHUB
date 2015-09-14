package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesSuunitIdDAO;
import com.claridy.domain.ErmResourcesSuunit;
@Repository
public class ErmResourcesSuunitIdDAO extends BaseDAO implements IErmResourcesSuunitIdDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public ErmResourcesSuunitIdDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErmResourcesSuunit> getDomain(String dbId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="from ErmResourcesSuunit where dbId='"+dbId+"'";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesSuunit> list=query.list();
		return list;
	}

}
