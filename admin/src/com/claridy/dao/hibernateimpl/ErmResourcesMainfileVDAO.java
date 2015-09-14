package com.claridy.dao.hibernateimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmResourcesMainfileV;

@Repository
public class ErmResourcesMainfileVDAO extends BaseDAO implements IErmResourcesMainfileVDAO {
	@Autowired
	public ErmResourcesMainfileVDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 獲取集合
	 */
	@SuppressWarnings("unchecked")
	public List<ErmResourcesMainfileV> findErmResMainfileVList(String resourcesId){
		DetachedCriteria criteria = DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		criteria.add(Restrictions.eq("resourcesId", resourcesId));
		criteria.add(Restrictions.and(Restrictions.le("starOrderDate", new Date()), Restrictions.or(Restrictions.ge("endOrderDate", new Date()), Restrictions.isNull("endOrderDate"))));
		List<ErmResourcesMainfileV> mainfileList = (List<ErmResourcesMainfileV>) super
				.findByCriteria(criteria); 
		return mainfileList;
	}
	/**
	 * 根據resourcesId查詢對象
	 * @param resourcesId
	 * @return
	 */
	public ErmResourcesMainfileV getMainfileVByResId(String resourcesId){
		String hql="from ErmResourcesMainfileV where resourcesId='"+resourcesId+"'";
		List<ErmResourcesMainfileV> ermResMainfileVList=(List<ErmResourcesMainfileV>) super.findByHQL(hql);
		if(ermResMainfileVList.size()>0){
			return ermResMainfileVList.get(0);
		}else{
			return new ErmResourcesMainfileV();
		}
	}
	/**
	 * 根據resourcesId查詢對象
	 * @param resourcesId
	 * @return
	 */
	public ErmResourcesMainfileV getErmResMainfileVByResId(String resourcesId){
		String hql="from ErmResourcesMainfileV where resourcesId='"+resourcesId+"' and isDataEffid=1";
		List<ErmResourcesMainfileV> ermResMainfileVList=(List<ErmResourcesMainfileV>) super.findByHQL(hql);
		if(ermResMainfileVList.size()>0){
			return ermResMainfileVList.get(0);
		}else{
			return new ErmResourcesMainfileV();
		}
	}
	/**
	 * 根據傳過來的sql返回查詢結果
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findObjectListBySql(String sql){
		try {
			Session session = this.getSession();
			Query query = session.createSQLQuery(sql);
			query = session.createSQLQuery(sql);

			return query.list();
		} catch (Exception e) {
			return new ArrayList<Object>();
		}
	}
	/**
	 * 根據資源類型和姓名和resourId查詢
	 */
	public ErmResourcesMainfileV getErmMainFileByTypeName(String resourcesId,String typeId,String name){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		if(resourcesId!=null&&!resourcesId.equals("")){
			criteria.add(Restrictions.eq("resourcesId", resourcesId));
		}
		if(typeId!=null&&!typeId.equals("")&&!typeId.equals("0")){
			criteria.add(Restrictions.eq("typeId", typeId));
		}
		if(name!=null&&!name.equals("")){
			criteria.add(Restrictions.like("title",("%"+name+"%")));
		}
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<ErmResourcesMainfileV> ermResourcesMainfileVList=criteria.getExecutableCriteria(getSession()).list();
		if(ermResourcesMainfileVList.size()>0){
			return ermResourcesMainfileVList.get(0);
		}
		return new ErmResourcesMainfileV();
	}
	/**
	 * 根據typeid進行查詢
	 */
	@SuppressWarnings("unchecked")
	public List<ErmResourcesMainfileV> findByTypeId(String typeId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		criteria.add(Restrictions.eq("typeId", typeId));
		criteria.add(Restrictions.eq("isDataEffid",1));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	public List<ErmResourcesMainfileV> findAll(){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		criteria.add(Restrictions.eq("isDataEffid",1));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	public List<ErmResourcesMainfileV> findByIdName(String typeId,String id,String name){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		if(typeId!=null&&!"".equals(typeId)){
			criteria.add(Restrictions.eq("typeId", typeId));
		}
		if(id!=null&&!"".equals(id)){
			criteria.add(Restrictions.like("resourcesId",("%"+id+"%")));
		}
		if(name!=null&&!"".equals(name)){
			criteria.add(Restrictions.like("title",("%"+name+"%")));
		}
		criteria.add(Restrictions.eq("isDataEffid",1));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	public ErmResourcesMainfileV findByResourceId(String ResourceId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		criteria.add(Restrictions.eq("resourcesId", ResourceId));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<ErmResourcesMainfileV> ermResourcesMainfileVList=criteria.getExecutableCriteria(getSession()).list();
		if(ermResourcesMainfileVList.size()>0){
			return ermResourcesMainfileVList.get(0);
		}
		return new ErmResourcesMainfileV();
	}
	public ErmResourcesMainfileV getErmResMainfileVByResIdAndDbid(
			String resourcesId, String dbId) {
		String hql="from ErmResourcesMainfileV where resourcesId='"+resourcesId+"' and dbId='"+dbId+"'";
		Session session=this.getSession();
		ErmResourcesMainfileV mainFileV=(ErmResourcesMainfileV) session.createQuery(hql).uniqueResult();
		return mainFileV;
	}
	/**
	 * 獲取集合
	 */
	@SuppressWarnings("unchecked")
	public Integer findErmCodeDbList(String resourcesId){
		DetachedCriteria criteria = DetachedCriteria.forClass(ErmResourcesMainfileV.class);
		criteria.add(Restrictions.eq("resourcesId", resourcesId));
		criteria.add(Restrictions.and(Restrictions.le("starOrderDate", new Date()), Restrictions.or(Restrictions.ge("endOrderDate", new Date()), Restrictions.isNull("endOrderDate"))));
		criteria.add(Restrictions.eq("isDataEffid",1));
		List<ErmResourcesMainfileV> mainfileList = (List<ErmResourcesMainfileV>) super
				.findByCriteria(criteria); 
		return mainfileList.size();
	}
}
