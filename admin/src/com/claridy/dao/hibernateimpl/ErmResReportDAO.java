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
import com.claridy.dao.IErmResReportDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebOrg;

@Repository
public class ErmResReportDAO extends BaseDAO implements IErmResReportDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	protected IWebOrgDAO iWebOrgDAO;

	@Autowired
	public ErmResReportDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<Object> findErmResourcesRsconAll(int firstYear, int secondYear) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select resources_id,CONVERT(VARCHAR(4),ckdate,112) as datetime,urlcon,title,count(urlcon) from erm_resources_rscon where CONVERT(VARCHAR(4),ckdate,112) between '"
				+ String.valueOf(firstYear)
				+ "' and '"
				+ String.valueOf(secondYear)
				+ "' and resources_id!='' group by resources_id,CONVERT(VARCHAR(4),ckdate,112),urlcon,title,ckdate";
		query = session.createSQLQuery(hql);
		List<Object> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> findSendReport(int firstYear, String firstMonth,
			int secondYear, String secondMonth) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		hql = "select resources_id,CONVERT(VARCHAR(6),ckdate,112) as datetime,urlcon,title,count(urlcon) from erm_resources_rscon where CONVERT(VARCHAR(6),ckdate,112) between '"
				+ String.valueOf(firstYear)
				+ firstMonth
				+ "' and '"
				+ String.valueOf(secondYear)
				+ secondMonth
				+ "' and resources_id!='' group by resources_id,CONVERT(VARCHAR(6),ckdate,112),urlcon,title,ckdate";
		query = session.createSQLQuery(hql);
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
