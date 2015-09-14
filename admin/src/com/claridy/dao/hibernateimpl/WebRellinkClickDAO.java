package com.claridy.dao.hibernateimpl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebRellinkClickDAO;
import com.claridy.domain.WebRellinkClick;
@Repository
public class WebRellinkClickDAO extends BaseDAO implements IWebRellinkClickDAO {

	
	@Autowired
	public WebRellinkClickDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	public WebRellinkClick findByaccountIdAndUuid(String relinkId,
			String accountId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebRellinkClick.class);
		if(relinkId!=null){
			criteria.add(Restrictions.eq("uuid", relinkId));
		}
		if(accountId!=null){
			criteria.add(Restrictions.eq("webAccountUuid", accountId));
		}
		return null;
	}

}
