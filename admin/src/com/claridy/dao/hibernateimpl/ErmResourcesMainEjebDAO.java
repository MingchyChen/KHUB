package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesMainEjebDAO;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;
@Repository
public class ErmResourcesMainEjebDAO extends BaseDAO implements IErmResourcesMainEjebDAO{
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmResourcesMainEjebDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	public void updateErmResMainEjeb(String sql){
		Session session=this.getSession();
		Query query=session.createQuery(sql);
		query.executeUpdate();
	}
	public ErmResourcesMainEjeb getResMainEjebByResId(String resourcesId) {
		Session session = this.getSession();
		Query query = session
				.createQuery("from ErmResourcesMainEjeb where resourcesId=:resourcesId");
		query.setString("resourcesId", resourcesId);
		ErmResourcesMainEjeb resMainEjebRet = (ErmResourcesMainEjeb) query
				.uniqueResult();
		return resMainEjebRet;
	}
	public ErmResourcesMainEjeb getResMainEjebByResIdInDb(String resourcesId) {
		Session session = this.getSession();
		Query query = session
				.createQuery("from ErmResourcesMainEjeb where resourcesId='"+resourcesId+"'");
		//query.setString("resourcesId", resourcesId);
//		ErmResourcesMainEjeb resMainEjebRet = (ErmResourcesMainEjeb) query
//				.uniqueResult();
		ErmResourcesMainEjeb resMainEjebRet =(ErmResourcesMainEjeb) query.list().get(0);
		return resMainEjebRet;
	}

	public List<ErmResourcesMainEjeb> findAllResMainEjebList(
			ErmResourcesMainDbws ermResMainDbws, WebEmployee webEmployee) {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = daoimpl.getCriteris(
				ErmResourcesMainEjeb.class, webEmployee);
		/*
		 * if(name!=null&&!"".equals(name)){
		 * criteria.add(Restrictions.like("nameZhTw",name,MatchMode.ANYWHERE));
		 * }
		 */
		criteria.addOrder(Order.desc("createDate"));
		List<ErmResourcesMainEjeb> resMainEjebListRet = (List<ErmResourcesMainEjeb>) super
				.findByCriteria(criteria);
		return resMainEjebListRet;
	}
	public List<ErmResourcesMainEjeb> findAllResMainEjebList() {
		// TODO Auto-generated method stub
		/*Session session = this.getSession();
		Query query = session
				.createQuery("from ErmResourcesMainEjeb where typeId='EJ'");
		return query.list();*/
		//String hql=daoimpl.getHql("WebSearchInfo", webEmployee);
		//hql+=" and isDataEffid=1 ";
		Query query=this.getSession().createQuery("from ErmResourcesMainEjeb where typeId='EJ'");
		List<ErmResourcesMainEjeb> retList=query.list();
		return retList;
	}
	public List<ErmResourcesEjebItem> findAllResMainEjebItemList(
			ErmResourcesEjebItem ermResourcesEjebItem, WebEmployee webEmployee) {
		Session session = this.getSession();
		Query query = session
				.createQuery("from ErmResourcesEjebItem where history='N' or history is null");
		return query.list();
	}
	public List<ErmResourcesMainEjeb> findAllResMainEbList(String typeId,String id,String name) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainEjeb.class);
		criteria.add(Restrictions.eq("typeId",typeId));
		if(id!=null&&!"".equals(id)){
			criteria.add(Restrictions.eq("resourcesId",id));
		}
		if(name!=null&&!"".equals(name)){
			criteria.add(Restrictions.eq("title",name));
		}
		return criteria.getExecutableCriteria(getSession()).list();
	}

}
