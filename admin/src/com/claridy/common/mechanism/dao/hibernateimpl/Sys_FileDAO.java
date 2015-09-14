package com.claridy.common.mechanism.dao.hibernateimpl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_FileDAO;
import com.claridy.common.mechanism.domain.Sys_File;




@Repository
public class Sys_FileDAO extends BaseDAO implements ISys_FileDAO
{ 
	@Autowired  
	public Sys_FileDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Sys_File> findAll() throws DataAccessException{
		return (List<Sys_File>) super.findByCriteria( DetachedCriteria.forClass(Sys_File.class));
	} 
	
	public Sys_File find(String file_pk) throws DataAccessException{
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + Sys_File.class.getSimpleName());
		sbHql.append(" WHERE file_pk = ? ");

		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, Long.parseLong(file_pk));

		return (Sys_File) query.uniqueResult();
	}

	public Sys_File create(Sys_File sys_File) throws DataAccessException {
		Sys_File sys_File2 = null;
		try {
		 getHibernateTemplate().save(sys_File);
		} catch (Exception e) {
			throw new DataAccessException("Create Sys_File failed.", e);
		
		}
		sys_File2 = getHibernateTemplate().get(Sys_File.class, sys_File.getFile_pk());
		return sys_File2;
	}

	public void delete(Sys_File sys_File) throws DataAccessException {
		super.delete(sys_File);
		
	}

}
