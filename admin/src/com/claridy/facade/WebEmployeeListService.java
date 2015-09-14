package com.claridy.facade;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebEmployeeDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

@Service
public class WebEmployeeListService {
	
	@Autowired
	private IWebEmployeeDAO webEmployeeDao;
	
	@Autowired
	private IWebOrgDAO webOrgDao;
	
	
	public List<WebEmployee> findWebEmployee(WebEmployee web,WebEmployee webEmployee,int bool,int isdataeffid){
		List<WebOrg> webOrgList;
		if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!webEmployee.getWeborg().getOrgId().equals("")){
			 webOrgList=webOrgDao.findParentOrg(webEmployee.getWeborg().getOrgId());
		}else{
			 webOrgList=webOrgDao.findParentOrg(webEmployee.getParentWebOrg().getOrgId());
		} 
		return webEmployeeDao.findWebEmployee(web,webEmployee,bool,isdataeffid,webOrgList.get(0).getIsAuth());
	}
	
	public WebEmployee getEmpById(String uuid){
		return webEmployeeDao.getEmpById(uuid);
	}
	
	public List<WebEmployee> findbyNameIdOrg(String id,String name,String orgId){
		return webEmployeeDao.find(id, name, orgId);
	}
	
	public List<WebEmployee> findWebEmployeeListByParentOrgId(String parentOrgId){
		return webEmployeeDao.findWebEmployeeListByParentOrgId(parentOrgId);
	}
	/**
	 * 根據主鍵查詢管理者對象
	 * @param employeesn
	 * @return
	 */
	public WebEmployee getWebEmployee(String employeesn){
		return webEmployeeDao.getWebEmployee(employeesn);
	}
	public List<WebEmployee> findAll(){
		return webEmployeeDao.findAll();
	}
	
	public List<WebEmployee> findById(){
		return webEmployeeDao.findByIsdataEffid();
	}
	
	
	
	
	public List<WebOrg> findOrg(String parentId){
		List<WebOrg> weblist;
		
		weblist=webOrgDao.findOrg(parentId);
		
		return weblist;
	}

	public List<WebOrg> findParentOrg(String orgId){
		List<WebOrg> weblist;
		weblist=webOrgDao.findParentOrg(orgId);
		
		return weblist;
	}
	public void eidtWebEmployee(WebEmployee webEmployee){
		webEmployeeDao.saveOrUpdate(webEmployee);
		return;
	}
	
	public void addWebEmployee(WebEmployee webEmployee){
		webEmployeeDao.saveOrUpdate(webEmployee);
	}
	
	
	public void deleteWebEmployee(WebEmployee webEmployee){
		webEmployee.setIsDataEffid(0);
		webEmployeeDao.merge(webEmployee);
	}
	public void update(WebEmployee webEmployee){
		webEmployeeDao.update(webEmployee);
	}
}
