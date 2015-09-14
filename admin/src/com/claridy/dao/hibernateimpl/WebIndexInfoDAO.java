package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;


import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebIndexInfoDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;

@Repository
public class WebIndexInfoDAO extends BaseDAO implements IWebIndexInfoDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebIndexInfoDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebIndexInfo> findByName(String name,WebEmployee webemployee,int isauth) {
		String hql=daoimpl.getHql("WebIndexInfo", webemployee);
		if(name!=null&&!name.equals("")){
			hql+=" and matterZhTw like '%"+name+"%'";
		}
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return query.list();
	}
	
	
	public WebIndexInfo findByMatter(String matter){
		Session session=getSession();
		Query query=session.createQuery("from WebIndexInfo as info where info.matterZhTw='"+matter+"'");
		return (WebIndexInfo) query.uniqueResult();
	}
	/**
	 * 獲取頁尾資訊
	 * @return
	 */
	public WebIndexInfo getFooterInfo() {
		String hql = "from WebIndexInfo where isDisplay='1' and isDataEffid='1' and uuid='5'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<WebIndexInfo> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return new WebIndexInfo();
		}
	}
}
