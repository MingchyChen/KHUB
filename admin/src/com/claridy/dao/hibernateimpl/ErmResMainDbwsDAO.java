package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResMainDbwsDAO;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmResMainDbwsDAO extends BaseDAO implements IErmResMainDbwsDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmResMainDbwsDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	/**
	 * 根據resourcesId查詢結果
	 * 
	 * @param uuid
	 * @return
	 */
	public ErmResourcesMainDbws getResMainDbwsByResId(String resourcesId) {
		Session session = this.getSession();
		Query query = session
				.createQuery("from ErmResourcesMainDbws where resourcesId=:resourcesId");
		query.setString("resourcesId", resourcesId);
		ErmResourcesMainDbws resMainDbwsRet = (ErmResourcesMainDbws) query
				.uniqueResult();
		return resMainDbwsRet;
	}
	/**
	 * 返回全部數據的總數
	 * 
	 * @param uuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int findResourcesMainDbwsList() {
		Session session = this.getSession();
		Query query = session.createQuery("from ErmResourcesMainDbws");
		List<ErmResourcesMainDbws> resMainDbwsList = query.list();
		return resMainDbwsList.size();
	}
	/**
	 * 根據頁面查詢全部結果
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ErmResourcesMainDbws> findAllResMainDbwsList(
			ErmResourcesMainDbws ermResMainDbws, WebEmployee webEmployee) {
		DetachedCriteria criteria = daoimpl.getCriteris(
				ErmResourcesMainDbws.class, webEmployee);
		/*
		 * if(name!=null&&!"".equals(name)){
		 * criteria.add(Restrictions.like("nameZhTw",name,MatchMode.ANYWHERE));
		 * }
		 */
		criteria.addOrder(Order.desc("createDate"));
		List<ErmResourcesMainDbws> resMainDbwsListRet = (List<ErmResourcesMainDbws>) super
				.findByCriteria(criteria);
		return resMainDbwsListRet;
	}

	public List<ErmResourcesMainDbws> findAllList() {
		Session session = this.getSession();
		Query query = session.createQuery("from ErmResourcesMainDbws");
		List<ErmResourcesMainDbws> resMainDbwsList = query.list();
		return resMainDbwsList;
	}
}
