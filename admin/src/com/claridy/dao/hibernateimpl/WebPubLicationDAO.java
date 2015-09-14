package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebPubLicationDAO;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebPublication;
import com.claridy.domain.WebSysLog;

@Repository
public class WebPubLicationDAO extends BaseDAO implements IWebPubLicationDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebPubLicationDAO(HibernateTemplate hibernateTemplate){
		super.setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebPublication> findBy(WebPublication webPubLication,WebEmployee webEmployee) {
		DetachedCriteria criteria = daoimpl.getCriteris(WebPublication.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		if(webPubLication!=null){
			if(webPubLication.getTitleZhTw()!=null&&!webPubLication.getTitleZhTw().equals("")){
				criteria.add(Restrictions.like("titleZhTw", ("%"+webPubLication.getTitleZhTw())+"%"));
			}
			if(webPubLication.getIsDisplay()!=-1){
				criteria.add(Restrictions.eq("isDisplay",webPubLication.getIsDisplay()));
			}
			if(webPubLication.getUuid()!=null&&!"".equals(webPubLication.getUuid())){
				criteria.add(Restrictions.eq("uuid",webPubLication.getUuid()));
			}
		}
		List<WebPublication> webPubLicationList=criteria.getExecutableCriteria(getSession()).list();
		return webPubLicationList;
	}
}
