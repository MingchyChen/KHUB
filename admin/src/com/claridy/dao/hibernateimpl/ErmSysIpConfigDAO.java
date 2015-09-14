package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmSysIpConfigDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.WebOrg;

@Repository
public class ErmSysIpConfigDAO extends BaseDAO implements IErmSysIpConfigDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	protected IWebOrgDAO iWebOrgDAO;

	@Autowired
	public ErmSysIpConfigDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmSysIpconfig> findErmSysIpconfigAll(WebEmployee webEmployee) {
		// String hql="from ErmSysIpconfig where isDataEffid='1'";
		String hql = daoimpl.getHql("ErmSysIpconfig", webEmployee);
		hql += " and isDataEffid=1 ";
		Query query = this.getSession().createQuery(hql);
		List<ErmSysIpconfig> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ErmSysIpconfig> findedtAddList(String searchType,
			String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from ErmSysIpconfig where isDataEffid=1";
			query = session.createQuery(hql);
		} else {
			hql = "from ErmSysIpconfig where isDataEffid=1 and " + searchType
					+ "='" + searchValue + "'";
			query = session.createQuery(hql);
		}
		List<ErmSysIpconfig> list = query.list();
		for (int i = 0; i < list.size(); i++) {
			String tmpOrgId = list.get(i).getOrgId();
			WebOrg tmpWebOrg = iWebOrgDAO.findById(tmpOrgId);
			list.get(i).setOrgName(tmpWebOrg.getOrgName());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ErmSysIpconfig> findErmSysIp(String searchType,
			String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from ErmSysIpconfig where isDataEffid=1";
			query = session.createQuery(hql);
		} else {
			hql = "from ErmSysIpconfig where isDataEffid=1 and " + searchType
					+ "like'" + searchValue + "'";
			query = session.createQuery(hql);
		}
		List<ErmSysIpconfig> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public ErmSysIpconfig findByUuid(String uuid) {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmSysIpconfig ");
		sbHql.append(" where isDataEffid=1 and uuid ='" + uuid + "'");
		Query query = this.getSession().createQuery(sbHql.toString());
		// query.setParameter(0, uuid);

		return (ErmSysIpconfig) query.uniqueResult();
	}

	/**
	 * 根據菜單類型獲取集合
	 * 
	 * @param menuType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ErmSysIpconfig> findRelLinkByMenu(String menuType) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ErmSysIpconfig.class);
		criteria.add(Restrictions.eq("menuType", Integer.parseInt(menuType)));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.desc("createdate"));
		List<ErmSysIpconfig> retRelLinkList = (List<ErmSysIpconfig>) super
				.findByCriteria(criteria);
		return retRelLinkList;
	}

	public List<ErmSysIpconfig> findErmSysIpConfigAll(String isOpenRdoValue,String parentOrgName,WebEmployee webEmployee) {
		String hql = daoimpl.getHql("ErmSysIpconfig", webEmployee);
		if(parentOrgName!=null&&!"".equals(parentOrgName)){
		hql +=" and orgId = '"+parentOrgName+"' ";	
		}else if(isOpenRdoValue!=null&&!"".equals(isOpenRdoValue)){
			hql +=" and isopen ='"+isOpenRdoValue+"'";
		}
		hql += " and isDataEffid=1 ";
		Query query = this.getSession().createQuery(hql);
		List<ErmSysIpconfig> list = query.list();
		// 取得OrgName
		for (int i = 0; i < list.size(); i++) {
			String tmpOrgId = list.get(i).getOrgId();
			WebOrg tmpWebOrg = iWebOrgDAO.findById(tmpOrgId);
			list.get(i).setOrgName(tmpWebOrg.getOrgName());
		}
		return list;
	}

}
