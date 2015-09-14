package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

public interface IErmResMonthUseDAO extends IBaseDAO {

	public WebOrg findOrgName(String orgid);
	
	public WebOrg findOrgIdParent(String orgIdParent);

	public WebAccount findAccountName(String accountId);

	public ErmResourcesMainfileV findResName(String resourcesId);
	
	public ErmCodeDb findDb(String dbId);
	
	public ErmCodeDb findDbId(String dbName);

	public List<Object> findErmResourcesRsconAll(String tempstartDateDbx,
			String tempendDateDbx);
	
	public List<Object> findErmResUnitList(String tempstartDateDbx,
			String tempendDateDbx, String resId);
	
	public List<Object> findErmResUnitListBydata(String tempstartDateDbx,
			String tempendDateDbx, String resId,String dbId);
	
	public List<Object> findResIdAll(String tempstartDateDbx,
			String tempendDateDbx);
}
