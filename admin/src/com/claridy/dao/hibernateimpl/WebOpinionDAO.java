package com.claridy.dao.hibernateimpl;

import java.util.List;

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
import com.claridy.common.util.SystemProperties;
import com.claridy.dao.IWebOpinionDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinion;
import com.claridy.domain.WebOpinionReply;
import com.claridy.domain.WebOrg;

@Repository
public class WebOpinionDAO extends BaseDAO implements IWebOpinionDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemProperties systemProperties;
	@Autowired
	public WebOpinionDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據傳過來的domain Class和登錄用戶返回查詢的criteria
	 * 
	 * @param domainName
	 * @param webEmployee
	 * @return
	 */
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
								webOrg.getOrgId()),Restrictions.eq("dataowner",
										webEmployee.getEmployeesn())));
					} else {
						criteria.add(Restrictions.eq("dataowner",
								webEmployee.getEmployeesn()));
					}
				}
			}
		} catch (Exception e) {
			log.error("根據傳過來的domain Class和登錄用戶返回查詢的criteria",e);
		}
		
		return criteria;
	}
	@SuppressWarnings("unchecked")
	public List<WebOpinion> findOpinionInfoAll(WebEmployee webEmployee) {
		DetachedCriteria criteria = getCriteris(WebOpinion.class,webEmployee);
		criteria.addOrder(Order.desc("createDate"));
		List<WebOpinion> list = (List<WebOpinion>) super.findByCriteria(criteria);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<WebOpinion> findOpinionByTitle(WebEmployee webEmployee,
			String title) {
		DetachedCriteria criteria = getCriteris(WebOpinion.class,webEmployee);
		if (title != null && !"".equals(title)) {
			//criteria.add(Restrictions.like("replyZhTw", title));
			criteria.add(Restrictions.or(Restrictions.like("titleZhTw", "%"+title+"%"), Restrictions.like("contentZhTw", "%"+title+"%")));
		}
		criteria.addOrder(Order.desc("createDate"));
		List<WebOpinion> list = (List<WebOpinion>) super.findByCriteria(criteria);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<WebOpinion> findedtAddList(String searchType, String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from WebOpinion order by createDate desc";
			query = session.createQuery(hql);
		} else {
			hql = "from WebOpinion where " + searchType + "='"
					+ searchValue + "' order by createDate desc";
			query = session.createQuery(hql);
		}
		List<WebOpinion> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<WebOpinionReply> findReplyList(String searchType,
			String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from WebOpinionReply where isDataEffid=1 order by latelyChangedDate desc";
			query = session.createQuery(hql);
		} else {
			hql = "from WebOpinionReply where isDataEffid=1 and " + searchType + "='"
					+ searchValue + "' order by latelyChangedDate desc";
			query = session.createQuery(hql);
		}
		List<WebOpinionReply> list = query.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public WebOpinionReply findReply(String searchType, String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from WebOpinionReply where isDataEffid=1 order by latelyChangedDate desc";
			query = session.createQuery(hql);
		} else {
			hql = "from WebOpinionReply where isDataEffid=1 and " + searchType + "='"
					+ searchValue + "' order by latelyChangedDate desc";
			query = session.createQuery(hql);
		}
		List<WebOpinionReply> list = query.list();
		return list.get(0);
	}

}
