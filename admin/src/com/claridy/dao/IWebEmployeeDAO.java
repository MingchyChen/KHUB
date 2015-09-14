package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;

public interface IWebEmployeeDAO extends IBaseDAO{
	
	public WebEmployee login(String name,String password);
	public List<WebEmployee> findWebEmployee(WebEmployee webEmployee,WebEmployee web,int bool,int isdataeffid,int isauth);
	public WebEmployee getEmpById(String uuid);
	public List<WebEmployee> findAll();
	public List<WebEmployee> findWebEmployeeListByParentOrgId(String parentOrgId);
	public List<WebEmployee> findByIsdataEffid();
	
	public List<WebEmployee> find(String id,String name,String orgId);
	
	public WebEmployee getWebEmployee(String employeesn);
	
	public List<WebEmployee> findIsMangerByParent(String parentOrgId);
	
	public WebEmployee findByName(String name);
}
 