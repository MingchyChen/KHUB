package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;

public interface IWebAccountDAO extends IBaseDAO {
	
	public List<WebAccount> findWebAccount(WebAccount web,int isregister,int isdataeffid,WebEmployee webEmployee);
	
	public List<WebAccount> findAccount(WebAccount webAccount,WebEmployee webEmployee);
	
	public WebAccount findById(String name);
	
	public WebAccount getAccount(String accountId);
	
	public List<WebAccount> findAccountList();
	
	public List<Object> findAccApplyNumList(String startDate, String endDate);
	
	public List<Object> findAccApplyNumListByParent(String startDate, String endDate,String parentOrgId);
	
	public List<WebAccount> findWebAccListByParent(String startDate, String endDate,String parentOrgId);
}
