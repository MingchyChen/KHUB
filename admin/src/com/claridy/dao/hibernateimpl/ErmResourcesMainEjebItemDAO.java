package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesMainEjebItemDAO;
import com.claridy.domain.ErmResourcesEjebItem;

@Repository
public class ErmResourcesMainEjebItemDAO extends BaseDAO implements
		IErmResourcesMainEjebItemDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmResourcesMainEjebItemDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public List<ErmResourcesEjebItem> getResMainEjebItemByResId(
			String resourcesId) {
		Session session = this.getSession();
		Query query = session.createQuery("from ErmResourcesEjebItem where resourcesId='"+resourcesId+"' and history='N' or history is null");
		List<ErmResourcesEjebItem> list=query.list();
		return list;
	}

	public ErmResourcesEjebItem getMainEjebItemByResId(String resourcesId) {
		Session session = this.getSession();
		Query query = session.createQuery("from ErmResourcesEjebItem where resourcesId='"+resourcesId+"' order by endOrderDate desc ");
		List<ErmResourcesEjebItem> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new ErmResourcesEjebItem();
		}
	}

}
