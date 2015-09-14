package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResMonthUseDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Repository
public class ErmResMonthUseDAO extends BaseDAO implements IErmResMonthUseDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	protected IWebOrgDAO iWebOrgDAO;

	@Autowired
	public ErmResMonthUseDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResourcesRsconAll(String tempstartDateDbx,
			String tempendDateDbx) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,resources_id,CONVERT(VARCHAR(8),createdate,112) as datetime,db_id,parentorgid,orgid from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "' and resources_id!=''";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResUnitList(String tempstartDateDbx,
			String tempendDateDbx, String resId) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,resources_id,CONVERT(VARCHAR(8),createdate,112) as datetime,db_id,parentorgid,orgid from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "' and resources_id='"
				+ resId
				+ "'";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResUnitListBydata(String tempstartDateDbx,
			String tempendDateDbx, String resId, String dbId) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,resources_id,CONVERT(VARCHAR(8),createdate,112) as datetime,db_id,parentorgid,orgid from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "' and resources_id='"
				+ resId
				+ "'and db_id='"
				+ dbId
				+ "'";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public WebOrg findOrgName(String orgid) {
		WebOrg webOrg = new WebOrg();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from WebOrg ");
		sbHql.append(" where orgid ='" + orgid + "' and isDataEffid=1 ");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<WebOrg> webOrgListRet = query.list();
		if (webOrgListRet.size() > 0) {
			webOrg = webOrgListRet.get(0);
		}
		return webOrg;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findResIdAll(String tempstartDateDbx,
			String tempendDateDbx) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select resources_id,COUNT(*) from erm_personalize_rescount where CONVERT(VARCHAR(6),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx) + "' group by resources_id";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public WebOrg findOrgIdParent(String orgIdParent) {
		WebOrg webOrg = new WebOrg();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from WebOrg ");
		sbHql.append(" where orgId ='" + orgIdParent + "' and isDataEffid=1 ");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<WebOrg> webOrgListRet = query.list();
		if (webOrgListRet.size() > 0) {
			webOrg = webOrgListRet.get(0);
		}
		return webOrg;
	}

	@SuppressWarnings("unchecked")
	public WebAccount findAccountName(String accountId) {
		WebAccount webAccount = new WebAccount();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from WebAccount ");
		sbHql.append(" where accountId ='" + accountId + "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<WebAccount> webAccountList = query.list();
		if (webAccountList.size() > 0) {
			webAccount = webAccountList.get(0);
		}
		return webAccount;
	}

	@SuppressWarnings("unchecked")
	public ErmCodeDb findDb(String dbId) {
		ErmCodeDb ermCodeDb = new ErmCodeDb();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmCodeDb ");
		sbHql.append(" where dbId ='" + dbId + "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<ErmCodeDb> ermCodeDbList = query.list();
		if (ermCodeDbList.size() > 0) {
			ermCodeDb = ermCodeDbList.get(0);
		}
		return ermCodeDb;
	}
	
	@SuppressWarnings("unchecked")
	public ErmCodeDb findDbId(String dbName) {
		ErmCodeDb ermCodeDb = new ErmCodeDb();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmCodeDb ");
		sbHql.append(" where name ='" + dbName + "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<ErmCodeDb> ermCodeDbList = query.list();
		if (ermCodeDbList.size() > 0) {
			ermCodeDb = ermCodeDbList.get(0);
		}
		return ermCodeDb;
	}

	@SuppressWarnings("unchecked")
	public ErmResourcesMainfileV findResName(String resourcesId) {
		ErmResourcesMainfileV ermResourcesMainfileV = new ErmResourcesMainfileV();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmResourcesMainfileV ");
		sbHql.append(" where resourcesId ='" + resourcesId + "'");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<ErmResourcesMainfileV> list = query.list();
		if (list.size() > 0) {
			ermResourcesMainfileV = list.get(0);
		}
		return ermResourcesMainfileV;
	}
	/**
	 * 根據菜單類型獲取集合
	 * 
	 * @param menuType
	 * @return
	 */

}
