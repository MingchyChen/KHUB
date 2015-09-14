package com.claridy.dao.hibernateimpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebErwSourceDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebErwSourceUnit;

@Repository
public class WebErwSourceDAO extends BaseDAO implements IWebErwSourceDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	
	@Autowired
	public WebErwSourceDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public WebErwSource getWebErwSourceByUUID(String uuid){
		Session session=this.getSession();
		Query query=session.createQuery("from WebErwSource where uuid=:uuid");
		query.setString("uuid",uuid);
		WebErwSource webErwSource = (WebErwSource)query.uniqueResult();
		return webErwSource;
	}
	
	public WebErwSource getWebErwSourceByDBID(String dbid){
		Session session=this.getSession();
		Query query=session.createQuery("from WebErwSource where dbid=:dbid and isDataEffid=1");
		query.setString("dbid",dbid);
		WebErwSource webErwSource = (WebErwSource)query.uniqueResult();
		return webErwSource;
	}
	
	@SuppressWarnings("unchecked")
	public List<WebErwSource> getWebErwSourceList(WebErwSource webErwSource,WebEmployee webEmployee) {
		List<WebErwSource> webErwSourceList = new ArrayList<WebErwSource>(); 
		if(webEmployee != null){
			DetachedCriteria criteria=daoimpl.getCriteris(WebErwSource.class,webEmployee);
			String dbid = webErwSource.getDbid();
			String nameZhTw = webErwSource.getNameZhTw();
			
			if(webErwSource != null){
				if(dbid != null && !"".equals(dbid.trim())){
					criteria.add(Restrictions.like("dbid", dbid, MatchMode.ANYWHERE));
				}
				if(nameZhTw != null && !"".equals(nameZhTw.trim())){
					criteria.add(Restrictions.like("nameZhTw", nameZhTw, MatchMode.ANYWHERE));
				}
				criteria.add(Restrictions.eq("isDataEffid", 1));
			}
			
			webErwSourceList = (List<WebErwSource>) super.findByCriteria(criteria);
		}
		
		return webErwSourceList;
	}

	public WebErwSource updateWebErwSource(WebErwSource webErwSource) {
//		saveOrUpdate(webErwSource);
		merge(webErwSource);
		webErwSource = getWebErwSourceByUUID(webErwSource.getUuid());
		return webErwSource;
	}

	public WebErwSource addWebErwSource(WebErwSource webErwSource) {
		saveOrUpdate(webErwSource);
		
		webErwSource = getWebErwSourceByUUID(webErwSource.getUuid());
		return webErwSource;
	}
	
	public List<WebErwSource> findAll(){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebErwSource.class);
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
}
