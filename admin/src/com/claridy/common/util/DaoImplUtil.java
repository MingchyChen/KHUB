package com.claridy.common.util;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

@Repository
public class DaoImplUtil extends BaseDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	public DaoImplUtil(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@Autowired
	private SystemProperties systemProperties;

	/**
	 * 根據傳過來的domain Class和登錄用戶返回查詢的criteria
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DetachedCriteria getCriteris(Class domainName,
			WebEmployee webEmployee) {
		DetachedCriteria criteria = DetachedCriteria.forClass(domainName);
		
		try {
			Session session = this.getSession();
			Query query = session.createQuery("from WebOrg where orgId=:orgId");
			if (webEmployee.getWeborg() != null
					&& webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				query.setString("orgId", webEmployee.getWeborg().getOrgId());
			} else {
				query.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
			}
			WebOrg webOrg = (WebOrg) query.uniqueResult();
			// 如果是1則調用權限的內容
			if (systemProperties.dataAuth.equals("1")) {
				if (webOrg != null && 1 != webOrg.getIsAuth()) {
					if (webEmployee.getIsManager() == 1) {
						criteria.add(Restrictions.or(Restrictions.eq("dataOwnerGroup",
								webOrg.getOrgId()),Restrictions.eq("webEmployee.employeesn",
										webEmployee.getEmployeesn())));
					} else {
						criteria.add(Restrictions.eq("webEmployee.employeesn",
								webEmployee.getEmployeesn()));
					}
				}
			}
		} catch (Exception e) {
			log.error("根據傳過來的domain Class和登錄用戶返回查詢的criteria",e);
		}
		
		return criteria;
	}
	
	
	/**
	 * 根據傳過來的domain Class和登錄用戶返回查詢的criteria
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DetachedCriteria getWebCooperationCriteris(Class domainName,
			WebEmployee webEmployee) {
		Session session = this.getSession();
		Query query = session.createQuery("from WebOrg where orgId=:orgId");
		query.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
		WebOrg webOrg = (WebOrg) query.uniqueResult();
		DetachedCriteria criteria = DetachedCriteria.forClass(domainName);
		// 如果是1則調用權限的內容
		if (systemProperties.dataAuth.equals("1")) {
			if (webOrg != null && 1 != webOrg.getIsAuth()) {
				if (webEmployee.getIsManager() == 1) {
					criteria.add(Restrictions.or(Restrictions.eq("dataOwnerGroup",
							webOrg.getOrgId()),Restrictions.eq("acceptEmployee.employeesn",
									webEmployee.getEmployeesn())));
				} else {
					criteria.add(Restrictions.eq("acceptEmployee.employeesn",
							webEmployee.getEmployeesn()));
				}
			}
		}
		return criteria;
	}

	/**
	 * 根據傳過來的domain Class和登錄用戶返回查詢的criteria（domain創建者沒有配置多對一關係）
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DetachedCriteria getCriterisNoRelat(Class domainName,
			WebEmployee webEmployee) {
		Session session = this.getSession();
		Query query = session.createQuery("from WebOrg where orgId=:orgId");
		query.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
		WebOrg webOrg = (WebOrg) query.uniqueResult();
		DetachedCriteria criteria = DetachedCriteria.forClass(domainName);
		// 如果是1則調用權限的內容
		if (systemProperties.dataAuth.equals("1")) {
			if (webOrg != null && 1 != webOrg.getIsAuth()) {
				if (webEmployee.getIsManager() == 1) {
					criteria.add(Restrictions.eq("dataOwnerGroup",
							webOrg.getOrgId()));
				} else {
					criteria.add(Restrictions.eq("dataOwner",
							webEmployee.getEmployeesn()));
				}
			}
		}
		return criteria;
	}

	/**
	 * 根據傳過來的domain name和登錄用戶返回查詢的hql
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
	public String getHql(String domainName, WebEmployee webEmployee) {
		String hql = "from " + domainName + " where 1 = 1 ";
		
		try {
			Session session = this.getSession();
			Query queryOrg = session.createQuery("from WebOrg where orgId=:orgId");
			if (webEmployee.getWeborg() != null
					&& webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				queryOrg.setString("orgId", webEmployee.getWeborg().getOrgId());
			} else {
				queryOrg.setString("orgId", webEmployee.getParentWebOrg()
						.getOrgId());
			}
			WebOrg webOrg = (WebOrg) queryOrg.uniqueResult();
			// 如果是1則調用權限的內容
			if (systemProperties.dataAuth.equals("1")) {
				if (webOrg != null && 1 != webOrg.getIsAuth()) {
					if (webEmployee.getIsManager() == 1) {
						hql += "and (dataOwnerGroup = '" + webOrg.getOrgId() + "' or webEmployee.employeesn = '"
								+ webEmployee.getEmployeesn() + "') ";
					} else {
						hql += "and webEmployee.employeesn = '"
								+ webEmployee.getEmployeesn() + "' ";
					}
				}
			}
		} catch (Exception e) {
			log.error("根據傳過來的domain name和登錄用戶返回查詢的hql",e);
		}
		
		return hql;
	}
	
	public String getWebAccountHql(String domainName, WebEmployee webEmployee) {
		Session session = this.getSession();
		String hql = "from " + domainName + " where 1 = 1 ";
		Query queryOrg = session.createQuery("from WebOrg where orgId=:orgId");
		queryOrg.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
		WebOrg webOrg = (WebOrg) queryOrg.uniqueResult();
		// 如果是1則調用權限的內容
		if (systemProperties.dataAuth.equals("1")) {
			if (webOrg != null && 1 != webOrg.getIsAuth()) {
				if (webEmployee.getIsManager() == 1) {
					hql += "and dataOwnerGroup = '" + webOrg.getOrgId() + "' or webEmployee.employeesn = '"
							+ webEmployee.getEmployeesn() + "' ";
				} else {
					hql += "and webEmployee.employeesn = '"
							+ webEmployee.getEmployeesn() + "' ";
				}
			}
		}
		return hql;
	}
	
	public String getWebAccountmpHql(String domainName, WebEmployee webEmployee) {
		Session session = this.getSession();
		String hql = "from " + domainName + " where 1 = 1 ";
		Query queryOrg = session.createQuery("from WebOrg where orgId=:orgId");
		queryOrg.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
		WebOrg webOrg = (WebOrg) queryOrg.uniqueResult();
		// 如果是1則調用權限的內容
		if (systemProperties.dataAuth.equals("1")) {
			if (webOrg != null && 1 != webOrg.getIsAuth()) {
				if (webEmployee.getIsManager() == 1) {
					hql += "and ( parentorgid= '" + webOrg.getOrgId() + "' or webEmployee.employeesn = '"
							+ webEmployee.getEmployeesn() + "') ";
				} else {
					hql += "and webEmployee.employeesn = '"
							+ webEmployee.getEmployeesn() + "' ";
				}
			}
		}
		return hql;
	}

	/**
	 * 根據傳過來的domain name和登錄用戶返回查詢的hql（domain創建者沒有配置多對一關係）
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
	public String getHqlNoRelat(String domainName, WebEmployee webEmployee) {
		Session session = this.getSession();
		String hql = "from " + domainName + " where 1 = 1 ";
		Query queryOrg = session.createQuery("from WebOrg where orgId=:orgId");
		queryOrg.setString("orgId", webEmployee.getParentWebOrg().getOrgId());
		WebOrg webOrg = (WebOrg) queryOrg.uniqueResult();
		// 如果是1則調用權限的內容
		if (systemProperties.dataAuth.equals("1")) {
			if (webOrg != null && 1 != webOrg.getIsAuth()) {
				if (webEmployee.getIsManager() == 1) {
					hql += "and ( parentWebOrg.orgId= '" + webOrg.getOrgId() + "' or dataOwner = '"
							+ webEmployee.getEmployeesn() + "') ";
				} else {
					hql += "and dataOwner = '" + webEmployee.getEmployeesn()
							+ "' ";
				}
			}
		}
		return hql;
	}

	public String getErmSequence(Class domainName, String type, int size) {
		Configuration config=new Configuration().configure();
        SessionFactory sessionFactory=config.buildSessionFactory();
		AbstractEntityPersister classMetadata = (AbstractEntityPersister) sessionFactory.getClassMetadata(domainName);
		String tableName = classMetadata.getTableName();
		return "";
	}
}
