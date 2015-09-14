package com.claridy.dao.hibernateimpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebErwSourceUnitDAO;
import com.claridy.domain.WebErwSourceUnit;

@Repository
public class WebErwSourceUnitDAO extends BaseDAO implements IWebErwSourceUnitDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public WebErwSourceUnitDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public WebErwSourceUnit getWebErwSourceUnitByUUID(String uuid){
		Session session=this.getSession();
		Query query=session.createQuery("from WebErwSourceUnit where uuid=:uuid");
		query.setString("uuid",uuid);
		WebErwSourceUnit webErwSourceUnit = (WebErwSourceUnit)query.uniqueResult();
		return webErwSourceUnit;
	}
	
	public WebErwSourceUnit updateWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit) {
//		saveOrUpdate(webErwSource);
		merge(webErwSourceUnit);
		webErwSourceUnit = getWebErwSourceUnitByUUID(webErwSourceUnit.getUuid());
		return webErwSourceUnit;
	}

	public WebErwSourceUnit addWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit) {
		saveOrUpdate(webErwSourceUnit);
		
		webErwSourceUnit = getWebErwSourceUnitByUUID(webErwSourceUnit.getUuid());
		return webErwSourceUnit;
	}
}
