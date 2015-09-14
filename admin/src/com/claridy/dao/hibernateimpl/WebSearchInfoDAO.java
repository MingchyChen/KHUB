package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebSearchInfoDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;

@Repository
public class WebSearchInfoDAO extends BaseDAO implements IWebSearchInfoDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public WebSearchInfoDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<WebSearchInfo> findSearchInfoAll(WebEmployee webEmployee) {
		String hql=daoimpl.getHql("WebSearchInfo", webEmployee);
		hql+=" and isDataEffid=1 ";
		Query query=this.getSession().createQuery(hql);
		List<WebSearchInfo> retList=query.list();
		return retList;
	}
	@SuppressWarnings("unchecked")
	public List<WebSearchInfo> findsearchInfoByNameZhTw(String nameZhTw,
			WebEmployee webEmployee) {
		String hql=daoimpl.getHql("WebSearchInfo", webEmployee);
		hql+="and isDataEffid=1 and nameZhTw like '%"+nameZhTw+"%'";
		Query query=this.getSession().createQuery(hql);
		List<WebSearchInfo> retList=query.list();
		return retList;
	}
	@SuppressWarnings("unchecked")
	public List<WebSearchInfo> findedtAddList(String searchType,String searchValue) {
		Session session=this.getSession();
		String hql="";
		Query query=null;
		if(searchType==null&&searchValue==null){
			hql="from WebSearchInfo where isDataEffid=1";
			query=session.createQuery(hql);
		}else{
			hql="from WebSearchInfo where isDataEffid=1 and "+searchType+"='"+searchValue+"'";
			query=session.createQuery(hql);
		}
		List<WebSearchInfo> list=query.list();
		return list;		 
	}
}
