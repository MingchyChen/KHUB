package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmSysNotifyConfigDAO;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebRellink;

@Repository
public class ErmSysNotifyConfigDAO extends BaseDAO implements IErmSysNotifyConfigDAO{
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ErmSysNotifyConfigDAO(HibernateTemplate hibernateTemplate){
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<ErmSysNotifyConfig> findAll(WebEmployee webEmployee) {
		String hql=daoimpl.getHql("ErmSysNotifyConfig", webEmployee);
		hql+=" and isDataEffid=1 ";
		Query query=this.getSession().createQuery(hql);
		List<ErmSysNotifyConfig> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<ErmSysNotifyConfig> findErmSysNofityCfgByTypeId(String typeId,WebEmployee webEmployee){
		String hql=daoimpl.getHql("ErmSysNotifyConfig", webEmployee);
		hql+=" and isDataEffid=1 and typeId like'%"+typeId+"%'";
		Query query=this.getSession().createQuery(hql);
		List<ErmSysNotifyConfig> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<ErmSysNotifyConfig> findedtAddList(String typeId,String groupId) {
		Session session=this.getSession();
		String hql="from ErmSysNotifyConfig where typeId='"+typeId+"' and webOrg.orgId='"+groupId+"'";
		Query query=session.createQuery(hql);
		List<ErmSysNotifyConfig> list=query.list();
		return list;		 
	}
}
