package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

public interface IWebCooperationDAO extends IBaseDAO {

	
	public List<WebCooperation> find(WebOrg applyWebOrg,WebEmployee webEmployee,WebOrg acceptWebOrg,String uuid,String applyName,int status,Date startDate,Date endDate);
	
	public List<WebCooperation> findWebCooperationList(Date startDate, Date endDate);
	
	public List<Object> findApplyCooperResList(String startDate, String endDate);
	
	public List<Object> findCooperNumberList(String startDate, String endDate,String parentOrgId,String dbId);
	
	public List<WebCooperation> findWebCooperationListByStatus(String startDate, String endDate,String parentOrgId,String dbId,String status);

	public List<Object> findCooperUnitsList(String startDate, String endDate);
	
	public List<Object> findCooperUnitsNumberList(String startDate, String endDate,String parentOrgId);
	
	public List<WebCooperation> findCooperUnitsListByStatus(String startDate, String endDate,String parentOrgId,String status);
}
