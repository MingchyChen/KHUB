package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebFounctionEmployeeDAO;
import com.claridy.domain.WebFunctionEmployee;

@Repository
public class WebFunctionEmployeeDAO extends BaseDAO implements IWebFounctionEmployeeDAO {
	
	@Autowired
	public WebFunctionEmployeeDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebFunctionEmployee> getWebFunctionEmployee(String employeesn){
		Session session=this.getSession();
		String hql="from WebFunctionEmployee as webFunEmp where webFunEmp.employeesn='"+employeesn+"'";
		Query query=session.createQuery(hql);
		return query.list();
	}
	
	public void deleteWebFunctinEmp(String employeesn){
		Session session=this.getSession();
		String hql="delete from WebFunctionEmployee as webFuncEmp where webFuncEmp.employeesn='"+employeesn+"'";
		Query query=session.createQuery(hql);
		query.executeUpdate();
		
	}
	
}
