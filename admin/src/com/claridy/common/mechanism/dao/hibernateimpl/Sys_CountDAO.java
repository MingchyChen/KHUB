package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_CountDAO;
import com.claridy.common.mechanism.domain.Sys_Count;
import com.claridy.common.mechanism.domain.Sys_Pagination;

@Repository
public class Sys_CountDAO extends BaseDAO implements ISys_CountDAO
{  
	@Autowired  
	public Sys_CountDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_Count> findAll() throws DataAccessException{
		return (List<Sys_Count>) super.findByCriteria( DetachedCriteria.forClass(Sys_Count.class));
	} 
	
	public Sys_Count find(String obj_Pk,String obj_Name,String obj_time) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_Count.class.getSimpleName());
		sbHql.append(" WHERE obj_pk = ? and obj_name = ? and obj_time = ?");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, obj_Pk);
		query.setParameter(1, obj_Name);
		query.setParameter(2, obj_time);
		
		return (Sys_Count) query.uniqueResult();
	}
	
	public long findSumCount(String obj_pk,String obj_name) throws DataAccessException{
		long count = 0L;
		
		StringBuffer queryString = new StringBuffer();
		queryString.append("SELECT sum(count) FROM " + Sys_Count.class.getSimpleName());
		queryString.append(" WHERE obj_pk = ? and obj_name = ?");

		Query query = this.getSession().createSQLQuery(queryString.toString());
		query.setParameter(0, obj_pk);
		query.setParameter(1, obj_name);
		count = (Long.parseLong(query.uniqueResult().toString()));
		return count ;
	}
	/**
	 * 熱門資料庫集合
	 * @return
	 */
	public List getHotRes(){
		String sql="select sys.obj_pk,res.title,sum(count) as sumcount from sys_count sys inner join webresources res on sys.obj_pk=res.resources_id group by obj_pk,obj_name,res.title order by sumcount desc";
		Session session = this.getSession();
		Query query = session.createSQLQuery(sql);
		query.setMaxResults(5);
		List list = query.list();
		return list;
	}

	public Sys_Pagination getResList(String startime, String endtime,
			String[] sort, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("select nvl(ocount,0) as count,res.title from (select obj_pk,obj_name,sum(count) as ocount from sys_count where 1=1 ");
		//開始日期
		if(startime != null && !"".equals(startime.trim())) {
			sbSQL.append(" and obj_time>='"+startime+"' ");
		}
		//結束日期
		if(endtime != null && !"".equals(endtime.trim())) {
			sbSQL.append(" and obj_time<='"+endtime+"' ");
		}
		sbSQL.append(" group by obj_pk,obj_name) syscount right join (select * from webresources where webtype='normal' and isdelete=0) res on syscount.obj_pk=res.resources_id order by count desc ");
		return super.findBySQL(sbSQL.toString(), currentPage, pageSize);
	}
}
