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
import com.claridy.dao.IWebNoticeTemplatesDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;

@Repository
public class WebNoticeTemplatesDAO extends BaseDAO implements IWebNoticeTemplatesDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebNoticeTemplatesDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據uuid查詢結果
	 * @param uuid
	 * @return
	 */
	public WebNoticeTemplates findWebNoticeTempByUuid(String uuid){
		Session session=this.getSession();
		Query query=session.createQuery("from WebNoticeTemplates where uuid=:uuid");
		query.setString("uuid",uuid);
		WebNoticeTemplates webNoticeTemplates=(WebNoticeTemplates)query.uniqueResult();
		return webNoticeTemplates;
	}
	/**
	 * 根據範本名稱查詢結果/查詢全部結果
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebNoticeTemplates> findAllWebNoticeTemp(String name,WebEmployee webEmployee){	
		DetachedCriteria criteria=daoimpl.getCriteris(WebNoticeTemplates.class, webEmployee);
		if(name!=null&&!"".equals(name)){
			criteria.add(Restrictions.like("nameZhTw",name,MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.desc("createDate"));
		List<WebNoticeTemplates> retNoticetempList = (List<WebNoticeTemplates>) super
				.findByCriteria(criteria);
		return retNoticetempList;
	}
}
