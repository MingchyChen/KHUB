package com.claridy.facade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.dao.IWebErwSourceUnitDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSourceUnit;

@Service
public class WebErwSourceUnitService {

	@Autowired
	private IWebErwSourceUnitDAO erwSourceUnitDao;
	
	
	/**
	 * 根據UUID取得WebErwSourceUnit
	 * @param uuid
	 * @return
	 */
	public WebErwSourceUnit getWebErwSourceUnitByUUID(String uuid){
		WebErwSourceUnit webErwSourceUnit = erwSourceUnitDao.getWebErwSourceUnitByUUID(uuid);
		return webErwSourceUnit;
	}
	
	public WebErwSourceUnit addWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit){
		webErwSourceUnit.setUuid(UUIDGenerator.getUUID());
		webErwSourceUnit.setCreateDate(new Date());
		webErwSourceUnit.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSourceUnit.setDataOwner(loginWebEmployee);
		if(loginWebEmployee.getWeborg()!=null&&loginWebEmployee.getWeborg().getOrgId()!=null&&!loginWebEmployee.getWeborg().getOrgId().equals("")){
			webErwSourceUnit.setDataOwnerGroup(loginWebEmployee.getWeborg().getOrgId());
		}else{
			webErwSourceUnit.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
		}
		webErwSourceUnit.setLatelyChangeDuser(loginWebEmployee);
		webErwSourceUnit.setIsDataEffid(1);
		webErwSourceUnit = erwSourceUnitDao.addWebErwSourceUnit(webErwSourceUnit);
		return webErwSourceUnit;
	}
	
	public WebErwSourceUnit updateWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit){
		webErwSourceUnit.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSourceUnit.setLatelyChangeDuser(loginWebEmployee);
		webErwSourceUnit = erwSourceUnitDao.updateWebErwSourceUnit(webErwSourceUnit);
		return webErwSourceUnit;
	}
	
	public WebErwSourceUnit delWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit){
		webErwSourceUnit.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSourceUnit.setLatelyChangeDuser(loginWebEmployee);
		webErwSourceUnit.setIsDataEffid(0);
		webErwSourceUnit = erwSourceUnitDao.updateWebErwSourceUnit(webErwSourceUnit);
		return webErwSourceUnit;
	}
}
