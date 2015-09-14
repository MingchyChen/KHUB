package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesOdetailsUploadFileDAO;
import com.claridy.domain.ErmResourcesOdetailsUploadfile;

@Repository
public class ErmResourcesOdetailsUploadFileDAO extends BaseDAO implements IErmResourcesOdetailsUploadFileDAO{
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmResourcesOdetailsUploadFileDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public List<ErmResourcesOdetailsUploadfile> findOdetailsUploadFile() {
		String hql="from ErmResourcesOdetailsUploadfile where isDataEffid=1";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		List<ErmResourcesOdetailsUploadfile> list=query.list();
		return list;
	}
	public ErmResourcesOdetailsUploadfile getUpLoadFileByResIdAndYear(String resourcesId,String year){
		String hql="from ErmResourcesOdetailsUploadfile where isDataEffid=1 and resourcesId='"+resourcesId+"' and year='"+year+"'";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		ErmResourcesOdetailsUploadfile file=(ErmResourcesOdetailsUploadfile) query.uniqueResult();
		return file;
	}
	
}
