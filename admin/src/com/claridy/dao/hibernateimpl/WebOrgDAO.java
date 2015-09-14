package com.claridy.dao.hibernateimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.FrontWebFuncOrg;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunctionOrg;
import com.claridy.domain.WebOrg;

@Repository
public class WebOrgDAO extends BaseDAO implements IWebOrgDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public WebOrgDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findWebOrgList(String orgName, String orgParentName, WebEmployee webEmployee) {
		String hql = daoimpl.getHql("WebOrg", webEmployee);
		hql += " and isDataEffid = 1 ";
		if (orgName != null && !"".equals(orgName)) {
			hql += "and orgName like '%" + orgName + "%' ";
		}
		if (orgParentName != null && !"".equals(orgParentName)) {
			hql += "and orgIdParent like '%" + orgParentName + "%' ";
		}
		hql += " order by sort";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<WebOrg> list = query.list();

		List<WebOrg> orgListRet = new ArrayList<WebOrg>();
		for (int i = 0; i < list.size(); i++) {
			WebOrg weborg = list.get(i);
			query = session.createQuery("from WebOrg where isDataEffid = 1 and orgId=:orgIdParent");
			query.setString("orgIdParent", weborg.getOrgIdParent());
			WebOrg weborg1 = (WebOrg) query.uniqueResult();
			if (weborg1 != null && !"".equals(weborg1)) {
				weborg.setOrgIdParent(weborg1.getOrgName());
			} else {
				weborg.setOrgIdParent("");
			}
			orgListRet.add(weborg);
		}
		return orgListRet;
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findEdtAddWebOrgList(String searchType, String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from WebOrg where isDataEffid = 1 ";
			query = session.createQuery(hql);
		} else {
			hql = "from WebOrg where isDataEffid = 1 and " + searchType + " = '" + searchValue + "' order by sort";
			query = session.createQuery(hql);
		}
		List<WebOrg> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<WebEmployee> findWebEmployeeList(String orgId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "from WebEmployee where isDataEffid = 1 and (orgId = '" + orgId + "' or parentOrgId = '" + orgId + "')";
		query = session.createQuery(hql);
		List<WebEmployee> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<WebAccount> findWebAccountList(String orgId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "from WebAccount where isDataEffid = 1 and (orgId = '" + orgId + "' or parentOrgId = '" + orgId + "')";
		query = session.createQuery(hql);
		List<WebAccount> list = query.list();
		return list;
	}

	/**
	 * 根據OrgId獲取單位權限菜單關聯表集合
	 * 
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebFunctionOrg> getFunctionOrgList(String orgId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "from WebFunctionOrg where orgId = '" + orgId + "' ";
		query = session.createQuery(hql);
		List<WebFunctionOrg> list = query.list();
		return list;
	}

	/**
	 * 根據OrgId獲取首頁上方連接菜單關聯表集合
	 * 
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FrontWebFuncOrg> getFrontOrgList(String orgId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "from FrontWebFuncOrg where orgId = '" + orgId + "' ";
		query = session.createQuery(hql);
		List<FrontWebFuncOrg> retList = query.list();
		return retList;
	}

	/**
	 * 根據Employeesn獲取管理者權限菜單關聯表集合
	 * 
	 * @param orgId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getFunctionEmployList(WebEmployee webEmployee) {
		List<String> list = new ArrayList<String>();
		try {
			Session session = this.getSession();
			String sql = "";
			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"".equals(webEmployee.getWeborg().getOrgId())) {
				sql = "select func_uuid from webfunction_org where orgid='" + webEmployee.getWeborg().getOrgId() + "' union select func_uuid from "
						+ "webfunction_org where orgid='" + webEmployee.getParentWebOrg().getOrgId() + "' union select func_uuid from  "
						+ "webfunction_employee where employeesn='" + webEmployee.getEmployeesn() + "'";
			} else {
				sql = "select func_uuid from " + "webfunction_org where orgid='" + webEmployee.getParentWebOrg().getOrgId()
						+ "' union select func_uuid from  " + "webfunction_employee where employeesn='" + webEmployee.getEmployeesn() + "'";
			}
			Query query = null;
			query = session.createSQLQuery(sql);
			list = query.list();
		} catch (Exception e) {
			log.error("根據Employeesn獲取管理者權限菜單關聯表集合報錯", e);
		}
		return list;
	}

	/**
	 * 根據主鍵獲取集合總數
	 */
	public int getSumFunctionUuid(String funcUuid, WebEmployee webEmployee) {
		int retNum = 0;
		try {
			String sql = "";
			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"".equals(webEmployee.getWeborg().getOrgId())) {
				sql = "select COUNT(*) from webfunction_org where func_uuid in (select uuid from webfunction where func_parent='" + funcUuid
						+ "') and (orgid='" + webEmployee.getWeborg().getOrgId() + "' or orgid='" + webEmployee.getParentWebOrg().getOrgId() + "')";
			} else {
				sql = "select COUNT(*) from webfunction_org where func_uuid in (select uuid from webfunction where func_parent='" + funcUuid
						+ "') and (orgid='" + webEmployee.getParentWebOrg().getOrgId() + "')";
			}

			Session session = this.getSession();
			Query query = session.createSQLQuery(sql);
			Object obj = query.uniqueResult();
			int result = 0;
			if (obj != null && !"".equals(obj)) {
				result = Integer.parseInt(obj.toString());
			}
			String sql2 = "select COUNT(*) from webfunction_employee where func_uuid in (select uuid from webfunction where func_parent='" + funcUuid
					+ "') and employeesn='" + webEmployee.getEmployeesn() + "'";
			Query query2 = session.createSQLQuery(sql2);
			Object obj2 = query2.uniqueResult();
			int result2 = 0;
			if (obj2 != null && !"".equals(obj2)) {
				result2 = Integer.parseInt(obj2.toString());
			}
			retNum = result + result2;
		} catch (Exception e) {
			log.error("根據主鍵獲取集合總數", e);
		}
		return retNum;
	}

	/**
	 * 根據OrgId刪除單位權限菜單關聯表數據
	 * 
	 * @param orgId
	 */
	public void deleteFuncOrg(String orgId) {
		Session session = this.getSession();
		String deleteHql = "delete from WebFunctionOrg where orgId = '" + orgId + "'";
		Query query = session.createQuery(deleteHql);
		query.executeUpdate();
	}

	/**
	 * 根據OrgId刪除首頁上面鏈接菜單關聯表數據
	 * 
	 * @param orgId
	 */
	public void deleteFrontFuncOrg(String orgId) {
		Session session = this.getSession();
		String deleteHql = "delete from FrontWebFuncOrg where orgId = '" + orgId + "'";
		Query query = session.createQuery(deleteHql);
		query.executeUpdate();
	}

	/**
	 * 查詢全部可用數據
	 */
	@SuppressWarnings("unchecked")
	public List<WebOrg> findWebOrgListToCombox() {
		String hql = "from WebOrg where isDataEffid = 1 order by sort";
		Query query = this.getSession().createQuery(hql);
		List<WebOrg> list = query.list();
		return list;
	}

	/**
	 * 根據orgId獲取org對象
	 */
	@SuppressWarnings("unchecked")
	public WebOrg findById(String uuid) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebOrg.class);
		if (uuid != null) {
			criteria.add(Restrictions.eq("orgId", uuid));
		}
		List<WebOrg> orgListRet = (List<WebOrg>) super.findByCriteria(criteria);
		WebOrg orgRet = new WebOrg();
		if (orgListRet.size() > 0) {
			orgRet = orgListRet.get(0);
		}
		return orgRet;
	}

	/**
	 * 根據orgName獲取org對象
	 */
	@SuppressWarnings("unchecked")
	public WebOrg getOrgByName(String orgName, String orgParentName) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebOrg.class);
		if (orgName != null && !"".equals(orgName)) {
			criteria.add(Restrictions.eq("orgName", orgName));
		}
		if (orgParentName != null && !"".equals(orgParentName)) {
			criteria.add(Restrictions.eq("orgIdParent", orgParentName));
		}
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<WebOrg> orgListRet = (List<WebOrg>) super.findByCriteria(criteria);
		WebOrg orgRet = new WebOrg();
		if (orgListRet.size() > 0) {
			orgRet = orgListRet.get(0);
		}
		return orgRet;
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findByParentId(String parentId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebOrg.class);
		if (parentId != null) {
			criteria.add(Restrictions.or(Restrictions.isNull("orgIdParent"), Restrictions.eq("orgIdParent", "0")));
			criteria.add(Restrictions.eq("orgId", parentId));
		}
		criteria.addOrder(Order.asc("sort"));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return criteria.getExecutableCriteria(getSession()).list();

	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findByWebEmployeeParentId(String parentId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebOrg.class);
		if (parentId != null) {
			criteria.add(Restrictions.eq("orgIdParent", parentId));
		}
		criteria.addOrder(Order.asc("sort"));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return criteria.getExecutableCriteria(getSession()).list();
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findOrg(String parentId) {
		List<WebOrg> weblist;
		String hql = "from WebOrg as web where web.isDataEffid=1";
		if (!StringUtils.isBlank(parentId)) {
			hql += " and web.orgIdParent='" + parentId + "'";
		}
		hql += " order by web.sort";
		Session session = getSession();
		weblist = (List<WebOrg>) session.createQuery(hql).list();
		return weblist;
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findParentOrg(String orgId) {
		List<WebOrg> weblist;
		String hql = "";
		if (orgId != null && !"".equals(orgId)) {
			hql = "from WebOrg as web where web.isDataEffid=1 and web.orgId='" + orgId + "' order by web.sort";
		} else {
			hql = "from WebOrg as web where web.isDataEffid=1 and (web.orgIdParent is NULL or web.orgIdParent='0' or web.orgIdParent='')  order by web.sort";
		}
		Session session = getSession();
		weblist = (List<WebOrg>) session.createQuery(hql).list();

		return weblist;
	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findWebOrgParam(String orgId, String orgName) {
		String hql = "from WebOrg where isDataEffid=1 and orgIdParent='0'";
		if (orgId != null && !"".equals(orgId)) {
			hql += " and orgId like '%" + orgId + "%'";
		}
		if (orgName != null && !"".equals(orgName)) {
			hql += " and orgName like '%" + orgName + "%'";
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<WebOrg> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public WebOrg getOrgById(String orgId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebOrg.class);
		criteria.add(Restrictions.eq("orgId", orgId));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<WebOrg> webOrgList = criteria.getExecutableCriteria(getSession()).list();
		if (webOrgList.size() > 0) {
			return webOrgList.get(0);
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<WebOrg> findOrgList(String orgName) {
		List<WebOrg> weblist;
		String hql = "from WebOrg where isDataEffid=1 and orgIdParent='0' and orgName like '%" + orgName + "%' order by sort";
		Session session = getSession();
		weblist = (List<WebOrg>) session.createQuery(hql).list();
		return weblist;
	}
}
