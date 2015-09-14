package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.mechanism.domain.Sys_Pagination;
import com.claridy.dao.ICategoryDAO;
import com.claridy.domain.Category;

@Repository
public class CategoryDAO extends BaseDAO implements ICategoryDAO
{ 
	@Autowired  
	public CategoryDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> findAll() throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Category.class);
		detachedCriteria.addOrder(Order.asc("sort"));
		return (List<Category>) super.findByCriteria( detachedCriteria);
	} 
	
	public Category find(String categorycode) throws DataAccessException{
		String hql="from Category where categorycode='"+categorycode+"' and isdelete=0";
		Query query = this.getSession().createQuery(hql);
		List<Category> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return new Category();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> search(int currentPage, int pageSize) throws DataAccessException {
		Query query = this.getSession().createQuery(" FROM " + Category.class.getSimpleName() +" where isdelete=0 order by categorycode");
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		return (List<Category>) query.list();
	}

	@SuppressWarnings("unchecked")
	public int getTotalCount() throws DataAccessException {
		Query query = this.getSession().createQuery(" FROM " + Category.class.getSimpleName()+"  where isdelete=0 order by sort");
		List<Category> list = (List<Category>) query.list();
		Integer a = (Integer) list.size();
		return a.intValue();
	}
	
	public Sys_Pagination find(String order, String categorycode, int currentPage, int pageSize) throws DataAccessException {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" from ");
		sbHql.append("  " + Category.class.getSimpleName() + " ");
		sbHql.append(" where ");
		sbHql.append("  categorycode = '" + categorycode + "' and isdelete=0");
		if ("".equals(order)) {
			sbHql.append(" order by sort ");			
		} else {
			sbHql.append(" order by " + order+",sort");
		}
		
		return super.findByHQL(sbHql.toString(), currentPage, pageSize);
	}
	/**
	 * 查詢上層類別集合
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findCategoryList(String parent) {
		String hql = "from Category where parent='"+parent+"' and isdelete=0 order by sort";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Category> list = query.list();
		return list;
	}
	/**
	 * 查詢上層類別集合
	 * @param parent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Category> findCategoryList2(String parent) {
		String hql = "from Category where parent like '"+parent+"%' and isdelete=0 order by sort";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Category> list = query.list();
		return list;
	}
	//查詢集合
	public Sys_Pagination getSelCategoryList(String categoryname,String parent, String[] sort, int currentPage, int pageSize) { 
		String c_categoryname="%";
		String c_parent="%";
		//名稱
		if(categoryname != null && !"".equals(categoryname.trim())) {
			c_categoryname="%"+categoryname+"%";
		}
		//上層分類
		if(parent != null && !"".equals(parent.trim())) {
			c_parent=""+parent+"";
		}
		String hql="from Category ca1 where ca1.categoryname like '"+c_categoryname+"' and ca1.parent like '"+c_parent+"' and ca1.isdelete=0 order by ca1.parent,ca1.sort";
		//sbSQL.append("select ca1.categorycode,ca1.categoryname,ca2.func_name as parent,ca1.created_date,ca1.sort,ca1.ifdel from category ca1 left join webhomelink ca2 on ca1.parent=ca2.func_no where ca1.categoryname like '"+c_categoryname+"' and ca1.parent like '"+c_parent+"' and ca1.parent not in ('wxcldl0001') and ca1.isdelete=0 order by ca1.parent desc,ca1.sort");
		return super.findByHQL(hql, currentPage, pageSize);
	}
	/**
	 * 根據上層分類獲取總數
	 * @param parent
	 * @return
	 */
	public String getNum(String categorycode,String parent){
		String hql="";
		if(parent.trim().equals("0")){
			hql="from Category where parent='"+categorycode+"' and isdelete=0";
		}else{
			hql="from Resources where categorycode='"+categorycode+"' and isdelete=0";
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Object> list = query.list();
		if(list.size()>0){
			return String.valueOf(list.size());
		}else{
			return "0";
		}
	}
}
