package com.claridy.dao.hibernateimpl;

import java.util.List;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebPhraseDAO;
import com.claridy.domain.WebPhrase;
@Repository
public class WebPhraseDAO extends BaseDAO implements IWebPhraseDAO {
	
	@Autowired
	public WebPhraseDAO(HibernateTemplate hibernateTemplate){
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebPhrase> find(String keyWord) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebPhrase.class);
		if(keyWord!=null){
			criteria.add(Restrictions.like("phraseZhTw",("%"+keyWord+"%")));
		}
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	public WebPhrase findById(String uuid) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebPhrase.class);
		if(uuid!=null){
			criteria.add(Restrictions.eq("uuid",uuid));
		}
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return (WebPhrase) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}

	
}
