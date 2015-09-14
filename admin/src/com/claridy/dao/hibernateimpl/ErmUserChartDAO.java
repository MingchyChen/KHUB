package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmUserChartDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Repository
public class ErmUserChartDAO extends BaseDAO implements IErmUserChartDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	protected IWebOrgDAO iWebOrgDAO;

	@Autowired
	public ErmUserChartDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResUser(String tempstartDateDbx,
			String tempendDateDbx, String accountId) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,resources_id,CONVERT(VARCHAR(8),createdate,112) as datetime,db_id,parentorgid,orgid from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "' and accountid='"
				+ accountId
				+ "' group by accountid,resources_id,CONVERT(VARCHAR(8),createdate,112),db_id,parentorgid,orgid,uuid";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResUserResType(String tempstartDateDbx,
			String tempendDateDbx, String accountId, String resType) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,resources_id,CONVERT(VARCHAR(8),createdate,112) as datetime,db_id,parentorgid,orgid from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "' and accountid='"
				+ accountId
				+ "'and resources_id like '%"
				+ resType
				+ "%' group by accountid,resources_id,CONVERT(VARCHAR(8),createdate,112),db_id,parentorgid,orgid,uuid";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findAccountIdAll(String tempstartDateDbx,
			String tempendDateDbx) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,COUNT(*) from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx) + "' group by accountid order by COUNT(*) desc";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findAccountIdResType(String tempstartDateDbx,
			String tempendDateDbx, String resType) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,COUNT(*) from erm_personalize_rescount where CONVERT(VARCHAR(8),createdate,112) between'"
				+ String.valueOf(tempstartDateDbx)
				+ "' and '"
				+ String.valueOf(tempendDateDbx)
				+ "'and resources_id like '%"
				+ resType + "%' group by accountid order by COUNT(*) desc";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findAccountIdAll() {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select accountid,COUNT(*) from erm_personalize_rescount group by accountid";
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
	public List<WebOrg> findedOrgName(String orgName) {
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from WebOrg ");
		sbHql.append(" where orgName ='" + orgName + "' and isDataEffid=1 ");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<WebOrg> list = query.list();
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
	public List<Object> findAllOrgIdParent() {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select distinct parentorgid from erm_personalize_rescount";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findAllDataBase() {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select distinct db_id from erm_personalize_rescount where db_id!='0'";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public WebAccount findAccountName(String accountId) {
		WebAccount webAccount = new WebAccount();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from WebAccount ");
		sbHql.append(" where accountId ='" + accountId + "' and isDataEffid=1 ");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<WebAccount> webAccountListRet = query.list();
		if (webAccountListRet.size() > 0) {
			webAccount = webAccountListRet.get(0);
		}
		return webAccount;
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodeDb> findedDb(String db) {
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmCodeDb ");
		sbHql.append(" where name ='" + db + "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<ErmCodeDb> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public ErmCodeDb findDb(String dbId) {
		ErmCodeDb ermCodeDb = new ErmCodeDb();
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmCodeDb ");
		sbHql.append(" where dbId ='" + dbId + "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<ErmCodeDb> ermCodeDbListRet = query.list();
		if (ermCodeDbListRet.size() > 0) {
			ermCodeDb = ermCodeDbListRet.get(0);
		}
		return ermCodeDb;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findResName(String resourcesId, String dbId) {
		Session session = this.getSession();
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ErmResourcesMainfileV ");
		sbHql.append(" where resourcesId ='" + resourcesId
				+ "' and isDataEffid=1");
		Query query = this.getSession().createQuery(sbHql.toString());
		List<Object> list = query.list();
		return list;
	}
	/**
	 * 根據菜單類型獲取集合
	 * 
	 * @param menuType
	 * @return
	 */

}
