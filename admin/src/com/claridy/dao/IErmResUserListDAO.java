package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.WebOrg;

public interface IErmResUserListDAO extends IBaseDAO {

	public WebOrg findOrgName(String orgid);
	
	public WebOrg findOrgIdParent(String orgIdParent);
	
	public List<WebOrg> findedOrgName(String orgName);
	
	public List<Object> findAllOrgIdParent();
	
	public List<Object> findAllDataBase();

	public WebAccount findAccountName(String accountId);

	public List<Object> findResName(String resourcesId, String dbId);
	
	public ErmCodeDb findDb(String dbId);
	
	public List<ErmCodeDb> findedDb(String db);

	public List<Object> findErmResourcesRsconAll(String tempstartDateDbx,
			String tempendDateDbx,String tempSortType,String tempSort);
	
	public List<Object> findErmResUser(String tempstartDateDbx,
			String tempendDateDbx);
	
	public List<Object> findMonthErmResUser(String tempstartDateDbx,
			String tempendDateDbx,String resId);
	
}
