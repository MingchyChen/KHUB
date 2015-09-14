package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesUploadfileDAO;
import com.claridy.domain.ErmResourcesUploadfile;

@Repository
public class ErmResourcesUploadfileDAO extends BaseDAO implements
		IErmResourcesUploadfileDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmResourcesUploadfileDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<ErmResourcesUploadfile> findByResourcesId(String resourceId) {
		Session session = this.getSession();
		String hql = "from ErmResourcesUploadfile where isDataEffid=1 and resourcesId='"
				+ resourceId + "'";
		Query query = session.createQuery(hql);
		List<ErmResourcesUploadfile> list = query.list();
		return list;
	}

}
