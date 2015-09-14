package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebOrgDAO;
import com.claridy.dao.IWebRelLinkDAO;
import com.claridy.domain.FrontWebFuncOrg;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunctionOrg;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebRellink;

@Service
public class WebOrgListService {
	
	@Autowired
	private IWebOrgDAO webOrgDao;
	@Autowired
	private IWebRelLinkDAO webRelLinkDao;
	
	/**
	 * 獲取單位權限集合
	 * @return
	 */
	public List<WebOrg> findWebOrgList(){
		return webOrgDao.findEdtAddWebOrgList("orgIdParent","0");
	}
	
	/**
	 * 獲取單位權限集合
	 * @param orgName
	 * @param orgParentName
	 * @param webEmployee
	 * @return
	 */
	public List<WebOrg> findWebOrgList(String orgName,String orgParentName,WebEmployee webEmployee){
		return webOrgDao.findWebOrgList(orgName,orgParentName,webEmployee);
	}
	
	/**
	 * 獲取單位權限集合
	 * @param searchType
	 * @param searchValue
	 * @return
	 */
	public List<WebOrg> findEdtAddWebOrgList(String searchType,String searchValue){
		return webOrgDao.findEdtAddWebOrgList(searchType,searchValue);
	}
	
	/**
	 * 根據單位Id獲取管理者和使用者數量總和
	 * @param orgId
	 * @return
	 */
	public int getEmpAndAccOrgSum(String orgId){
		List<WebEmployee> emplist=webOrgDao.findWebEmployeeList(orgId);
		List<WebAccount> acclist=webOrgDao.findWebAccountList(orgId);
		int sum=emplist.size()+acclist.size();
		return sum;
	}
	
	/**
	 * 修改單位權限
	 * @param webOrg
	 */
	public void eidtWebOrg(WebOrg webOrg){
		webOrgDao.saveOrUpdate(webOrg);
	}
	
	/**
	 * 新增單位權限
	 * @param webOrg
	 */
	public void addWebOrg(WebOrg webOrg){
		webOrgDao.saveOrUpdate(webOrg);
	}
	
	/**
	 * 刪除單位權限
	 * @param webOrg
	 */
	public void deleteWebOrg(WebOrg webOrg){
		webOrg.setIsDataEffid(0);
		webOrgDao.update(webOrg); 
	}
	/**
	 * 新增單位權限菜單關聯表數據
	 * @param webFuncOrg
	 */
	public void addWebFunctionOrg(WebFunctionOrg webFuncOrg){
		webOrgDao.saveOrUpdate(webFuncOrg);
	}
	/**
	 * 新增首頁上面鏈接菜單關聯表數據
	 * @param frontWebOrg
	 */
	public void addFrontFunctOrg(FrontWebFuncOrg frontWebOrg){
		webOrgDao.saveOrUpdate(frontWebOrg);
	}
	/**
	 * 根據OrgId獲取單位權限菜單關聯表集合
	 * @param orgId
	 * @return
	 */
	public List<WebFunctionOrg> getFunctionOrgList(String orgId){
		return webOrgDao.getFunctionOrgList(orgId);
	}
	/**
	 * 根據OrgId獲取首頁上方連接菜單關聯表集合
	 * @param orgId
	 * @return
	 */
	public List<FrontWebFuncOrg> getFrontOrgList(String orgId){
		return webOrgDao.getFrontOrgList(orgId);
	}
	/**
	 * 根據Employeesn獲取管理者權限菜單關聯表集合
	 * @param orgId
	 * @return
	 */
	public List<String> getFunctionEmployList(WebEmployee webEmployee){
		return webOrgDao.getFunctionEmployList(webEmployee);
	}
	/**
	 * 獲取所有權限集合
	 * @param funcUuid
	 * @param webEmployee
	 * @return
	 */
	public int getSumFunctionUuid(String funcUuid,WebEmployee webEmployee){
		return webOrgDao.getSumFunctionUuid(funcUuid,webEmployee);
	}
	/**
	 * 根據OrgId刪除單位權限菜單關聯表數據
	 * @param orgId
	 */
	public void deleteWebFuncOrg(String orgId){
		webOrgDao.deleteFuncOrg(orgId);
	}
	/**
	 * 根據OrgId刪除首頁上面鏈接菜單關聯表數據
	 * @param orgId
	 */
	public void deleteFrontFuncOrg(String orgId){
		webOrgDao.deleteFrontFuncOrg(orgId);
	}
	/**
	 * 根據菜單類型獲取集合
	 * @param menuType
	 * @return
	 */
	public List<WebRellink> findRelLinkByMenu(String menuType){
		return webRelLinkDao.findRelLinkByMenu(menuType);
	}
	/**
	 * 根據orgName獲取Org對象
	 * @param orgName
	 * @return
	 */
	public WebOrg getOrgByName(String orgName,String orgParentName){
		return webOrgDao.getOrgByName(orgName,orgParentName);
	}
	/***
	 * 根據傳入的參數查詢
	 * 
	 */
	public List<WebOrg> findWebOrgParam(String orgId,String orgName){
		return webOrgDao.findWebOrgParam(orgId, orgName);
	}
	
	public WebOrg getOrgById(String orgId){
		return webOrgDao.getOrgById(orgId);
	}
	
	public List<WebOrg> findOrgList(String orgName){
		return webOrgDao.findOrgList(orgName); 
	}
	
	public List<WebOrg> findWebOrgListToCombox(){
		return webOrgDao.findWebOrgListToCombox();
	}
}
