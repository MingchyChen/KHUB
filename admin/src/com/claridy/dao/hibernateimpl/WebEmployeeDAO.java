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
import com.claridy.dao.IWebEmployeeDAO;
import com.claridy.domain.WebEmployee;

@Repository
public class WebEmployeeDAO extends BaseDAO implements IWebEmployeeDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebEmployeeDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	public WebEmployee login(String name,String password) {
		Session session=this.getSession();
		Query query=session.createQuery("from WebEmployee as web where web.employeeId=:name and web.pwd=:password and web.isDataEffid=1");
		query.setString("name",name);
		query.setString("password",password);
		WebEmployee webEmployee=(WebEmployee)query.uniqueResult();
		
		
		return webEmployee;
	}

	@SuppressWarnings("unchecked")
	public List<WebEmployee> findWebEmployee(WebEmployee web,WebEmployee webEmployee,int bool,int isdataeffid,int isauth){
		
		String hql=daoimpl.getHqlNoRelat("WebEmployee", webEmployee);
		
		hql+=" and isDataEffid="+isdataeffid;
		if(!findEmp(web, bool).equals("")){
			hql+=findEmp(web, bool);
		}
		if(hql.indexOf("where 1 = 1  or")!=-1){
			hql=hql.replace("where 1 = 1  or","where 1 = 1  and");
		}
		if(hql.indexOf("isDataEffid=1  ( and")!=-1){
			hql=hql.replace("isDataEffid=1  ( and", "isDataEffid=1 and (");
		}
		if(hql.indexOf("isDataEffid=1  ( or")!=-1){
			hql=hql.replace("isDataEffid=1  ( or", "isDataEffid=1 and (");
		}
		if(hql.indexOf("isDataEffid=0  ( and")!=-1){
			hql=hql.replace("isDataEffid=0  ( and", "isDataEffid=1 and (");
		}
		if(hql.indexOf("isDataEffid=0  ( or")!=-1){
			hql=hql.replace("isDataEffid=0  ( or", "isDataEffid=1 and (");
		}
		hql+=" order by createDate desc";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		
		return (List<WebEmployee>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public WebEmployee getEmpById(String uuid){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		criteria.add(Restrictions.eq("employeesn", uuid));
		List<WebEmployee> webEmployeeList=criteria.getExecutableCriteria(getSession()).list();
		if(webEmployeeList.size()>0){
			return webEmployeeList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<WebEmployee> find(String id,String name,String orgId){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		if(id!=null&&!id.equals("")){
			criteria.add(Restrictions.like("employeesn","%"+id+"%"));
		}
		if(name!=null&&!name.equals("")){
			criteria.add(Restrictions.like("employeeName",("%"+name+"%")));
		}
		if(orgId!=null&&!orgId.equals("0")){
			criteria.add(Restrictions.eq("parentWebOrg.orgId", orgId));
		}
		criteria.add(Restrictions.eq("isDataEffid",1));
		criteria.addOrder(Order.desc("createDate"));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<WebEmployee> findAll(){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<WebEmployee> findByIsdataEffid(){
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	public String findEmp(WebEmployee webEmployee,int bool){
		String strHQL=" ";
		if(webEmployee!=null){	
			strHQL+=" (";
			if(webEmployee.getEmployeesn()!=null&&!webEmployee.getEmployeesn().equals("")){
				strHQL+=" and employeesn='"+webEmployee.getEmployeesn()+"'";
			}
			if(webEmployee.getEmployeeId()!=null&&!webEmployee.getEmployeeId().equals("")){
				if(bool==1||bool==0){
					strHQL+=" and employeeId like'%"+webEmployee.getEmployeeId()+"%'";
				}else if(bool==2){
					strHQL+=" or employeeId like'%"+webEmployee.getEmployeeId()+"%'";
				}
				
			}
			if(webEmployee.getEmployeeName()!=null&&!webEmployee.getEmployeeName().equals("")){
				if(bool==1||bool==0){
					strHQL+=" and employeeName like'%"+webEmployee.getEmployeeName()+"%'";
				}else if(bool==2){

					strHQL+=" or employeeName like'%"+webEmployee.getEmployeeName()+"%'";
				}
				
			}
			if(webEmployee.getParentWebOrg()!=null&&webEmployee.getParentWebOrg().getOrgId()!=null&&!webEmployee.getParentWebOrg().getOrgId().equals("")&&!webEmployee.getParentWebOrg().getOrgId().equals("0")){
				if(bool==1||bool==0){
					strHQL+=" and parentOrgId='"+webEmployee.getParentWebOrg().getOrgId()+"'";
				}else if(bool==2){

					strHQL+=" or parentOrgId like'%"+webEmployee.getParentWebOrg().getOrgId()+"%'";
				}
			}
			if(webEmployee.getIdType()!=0){
				if(bool==1||bool==0){
					strHQL+=" and idType="+webEmployee.getIdType();
				}else if(bool==2){

					strHQL+=" or idType="+webEmployee.getIdType();
				}
			}
			if(webEmployee.getIsManager()!=-1){
				if(bool==1||bool==0){
					strHQL+=" and isManager="+webEmployee.getIsManager();
				}else if(bool==2){
					strHQL+=" or isManager="+webEmployee.getIsManager();
				}
			}
			strHQL+=" )";
		}
		if(strHQL.indexOf("( )")!=-1){
			strHQL=strHQL.replace("( )", "");
		}
		return strHQL;
	}
	
	@SuppressWarnings("unchecked")
	public List<WebEmployee> findWebEmployeeListByParentOrgId(String parentOrgId){
		Session session=this.getSession();
		Query query=session.createQuery("from WebEmployee where parentOrgId=:parentOrgId and isDataEffid=1");
		query.setString("parentOrgId",parentOrgId);
		List<WebEmployee> webEmployee=query.list();
		return webEmployee;
	}
	/**
	 * 根據主鍵查詢管理者對象
	 * @param employeesn
	 * @return
	 */
	public WebEmployee getWebEmployee(String employeesn){
		Session session=this.getSession();
		Query query=session.createQuery("from WebEmployee where employeesn=:employeesn");
		query.setString("employeesn",employeesn);
		List<WebEmployee> webEmployeeList=query.list();
		if(webEmployeeList.size()>0){
			return webEmployeeList.get(0);
		}else{
			return new WebEmployee();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<WebEmployee> findIsMangerByParent(String parentOrgId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		criteria.add(Restrictions.eq("isManager", 1));
		criteria.add(Restrictions.eq("isDataEffid",1));
		criteria.add(Restrictions.eq("parentWebOrg.orgId", parentOrgId));
		return criteria.getExecutableCriteria(getSession()).list();
	}

	public WebEmployee findByName(String name) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebEmployee.class);
		criteria.add(Restrictions.like("employeeName", ("%"+name+"%")));
		criteria.add(Restrictions.eq("isDataEffid", 1));
		return (WebEmployee) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}
}
