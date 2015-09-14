package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IERMSystemSettingDAO;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;

@Service
public class SystemSettingService {

	@Autowired
	public IERMSystemSettingDAO ermSystemSettingDAO;

	public List<ErmSystemSetting> findAll(WebEmployee webEmployee) {
		return ermSystemSettingDAO.findAll(webEmployee);
	}

	public List<ErmSystemSetting> search(String funName,WebEmployee webEmployee) {
		return ermSystemSettingDAO.search(funName,webEmployee);
	}
	
	public List<ErmSystemSetting> findSysSettingList(String term) {
		return ermSystemSettingDAO.findSysSettingList(term);
	}

	public ErmSystemSetting findByFunID(String funId) {
		return ermSystemSettingDAO.findByFunID(funId);
	}
	
	public ErmSystemSetting getSysByFunID(String funId) {
		return ermSystemSettingDAO.getSysByFunID(funId);
	}

	public boolean save(String funId, String funValue, String funName,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmSystemSetting checkERMSystemSetting = new ErmSystemSetting();
		checkERMSystemSetting = findByFunID(funId);
		if (null == checkERMSystemSetting) {
			ErmSystemSetting tmpERMSystemSetting = new ErmSystemSetting();
			tmpERMSystemSetting.setFuncId(funId);
			tmpERMSystemSetting.setFuncValue(funValue);
			tmpERMSystemSetting.setFuncName(funName);
			tmpERMSystemSetting.setWebEmployee(webEmployee);
			if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
				tmpERMSystemSetting.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			}else{
				tmpERMSystemSetting.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			tmpERMSystemSetting.setCreateDate(new Date());
			tmpERMSystemSetting.setIsDataEffid(1);
			tmpERMSystemSetting.setIsDefault("N");
			ermSystemSettingDAO.saveOrUpdate(tmpERMSystemSetting);
			saveStatus = true;
		}
		
		return saveStatus;
	}

	public boolean update(String funId, String funValue, String funName,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmSystemSetting checkERMSystemSetting = new ErmSystemSetting();
		checkERMSystemSetting = findByFunID(funId);
		if (null != checkERMSystemSetting) {
			checkERMSystemSetting.setFuncId(funId);
			checkERMSystemSetting.setFuncValue(funValue);
			checkERMSystemSetting.setFuncName(funName);
			checkERMSystemSetting.setLatelyChangedUser(webEmployee.getEmployeesn());
			checkERMSystemSetting.setLatelyChangedDate(new Date());
			ermSystemSettingDAO.saveOrUpdate(checkERMSystemSetting);
			saveStatus = true;
		}
		
		return saveStatus;
	}

	public void deleteSystemSetting(String funId) {
		ErmSystemSetting tmpERMSystemSetting = ermSystemSettingDAO.findByFunID(funId);
		tmpERMSystemSetting.setIsDataEffid(0);
		ermSystemSettingDAO.saveOrUpdate(tmpERMSystemSetting);
	}
}
