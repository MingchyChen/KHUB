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
import com.claridy.dao.IWebRelLinkDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.domain.WebRellink;

@Repository
public class WebRelLinkDAO extends BaseDAO implements IWebRelLinkDAO{
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebRelLinkDAO(HibernateTemplate hibernateTemplate){
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebRellink> findWebRelLinkAll(WebEmployee webEmployee) {
		//String hql="from WebRellink where isDataEffid='1'";
		String hql=daoimpl.getHql("WebRellink", webEmployee);
		hql+=" and isDataEffid=1 ";
		Query query=this.getSession().createQuery(hql);
		List<WebRellink> list=query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<WebRellink> findWebRellinkBynameZhTw(String nameZhTw,WebEmployee webEmployee) {
		String hql=daoimpl.getHql("WebRellink", webEmployee);
		hql+=" and isDataEffid=1 and nameZhTw like'%"+nameZhTw+"%'";
		Query query=this.getSession().createQuery(hql);
		List<WebRellink> list=query.list();
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<WebRellink> findedtAddList(String searchType,String searchValue) {
		Session session=this.getSession();
		String hql="";
		Query query=null;
		if(searchType==null&&searchValue==null){
			hql="from WebRellink where isDataEffid=1";
			query=session.createQuery(hql);
		}else{
			hql="from WebRellink where isDataEffid=1 and "+searchType+"='"+searchValue+"'";
			query=session.createQuery(hql);
		}
		List<WebRellink> list=query.list();
		return list;		 
	}
	/**
	 * 根據菜單類型獲取集合
	 * @param menuType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebRellink> findRelLinkByMenu(String menuType){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebRellink.class);
		criteria.add(Restrictions.eq("menuType", Integer.parseInt(menuType)));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.addOrder(Order.desc("createdate"));
		List<WebRellink> retRelLinkList = (List<WebRellink>) super
				.findByCriteria(criteria);
		return retRelLinkList;
	}
}
