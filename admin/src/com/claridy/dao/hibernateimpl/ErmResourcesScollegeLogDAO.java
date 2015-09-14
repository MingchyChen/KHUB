package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesScollegeLogDAO;
import com.claridy.domain.ErmResourcesScollegeLog;
@Repository
public class ErmResourcesScollegeLogDAO extends BaseDAO implements IErmResourcesScollegeLogDAO {

	
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public ErmResourcesScollegeLogDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmResourcesScollegeLog> findScoLogList(String resourcesId) {
		Session session=this.getSession();
		String hql="from ErmResourcesScollegeLog where resourcesId='"+resourcesId+"' and isDataEffid=1";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesScollegeLog> listRet=query.list();
		return listRet;
	}
	/**
	 * 判斷數據是否已經存在
	 * @param resourcesId
	 * @param suitcollegeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer getScollegeLog(String resourcesId,String suitcollegeId,String dbId) {
		Session session=this.getSession();
		String hql="from ErmResourcesScollegeLog where resourcesId='"+resourcesId+"' and  suitcollegeId='"+suitcollegeId+"' and dbId='"+dbId+"' and isDataEffid=1";
		Query query=null;
		query=session.createQuery(hql);
		List<ErmResourcesScollegeLog> list=query.list();
		return list.size();
	}
}
