package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.FrontWebFuncOrg;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunctionOrg;
import com.claridy.domain.WebOrg;

public interface IWebOrgDAO extends IBaseDAO{
	public List<WebOrg> findWebOrgList(String orgName,String orgParentName,WebEmployee webEmployee);
	public List<WebOrg> findEdtAddWebOrgList(String searchType,String searchValue);
	public List<WebEmployee> findWebEmployeeList(String orgId);
	public List<WebAccount> findWebAccountList(String orgId);
	public List<WebFunctionOrg> getFunctionOrgList(String orgId);
	public List<FrontWebFuncOrg> getFrontOrgList(String orgId);
	public List<String> getFunctionEmployList(WebEmployee webEmployee);
	public int getSumFunctionUuid(String funcUuid,WebEmployee webEmployee);
	public void deleteFuncOrg(String orgId);
	public void deleteFrontFuncOrg(String orgId);
	public List<WebOrg> findWebOrgListToCombox();
	public WebOrg findById(String uuid);
	public WebOrg getOrgByName(String orgName,String orgParentName);
	public List<WebOrg> findByParentId(String parentId);
	public List<WebOrg> findByWebEmployeeParentId(String parentId);
	public List<WebOrg> findParentOrg(String orgId);
	public List<WebOrg> findOrg(String parentId);
	public List<WebOrg> findWebOrgParam(String orgId,String orgName);
	public WebOrg getOrgById(String orgId);
	public List<WebOrg> findOrgList(String orgName);
}
 