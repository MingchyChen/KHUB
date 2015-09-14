package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmSysIpConfigDAO;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;

@Service
public class ErmSysIpConfigService {
	@Autowired
	private IErmSysIpConfigDAO ermSysIpConfigDAO;
	
	public List<ErmSysIpconfig> findermSysIpConfigAll(String isOpenRdoValue,String parentOrgName,WebEmployee webEmployee){
		return ermSysIpConfigDAO.findErmSysIpConfigAll(isOpenRdoValue,parentOrgName,webEmployee);
	}
	public List<ErmSysIpconfig> findedtAddList(String searchType,String searchValue){
		return ermSysIpConfigDAO.findedtAddList(searchType, searchValue);
	}
	public void deleteermSysIpConfig(ErmSysIpconfig ermSysIpConfig){
		ermSysIpConfig.setIsDataEffid(0);
		ermSysIpConfigDAO.saveOrUpdate(ermSysIpConfig);
	}
	public void deleteermSysIpConfig(String uuid){
		ErmSysIpconfig ermSysIpConfig = ermSysIpConfigDAO.findByUuid(uuid);
		ermSysIpConfig.setIsDataEffid(0);
		ermSysIpConfigDAO.merge(ermSysIpConfig);
	}
	public void addermSysIpConfig(ErmSysIpconfig ermSysIpConfig){
		ermSysIpConfig.setIsDataEffid(1);
		ermSysIpConfigDAO.saveOrUpdate(ermSysIpConfig);
	}
	
	public void updateermSysIpConfig(ErmSysIpconfig ermSysIpConfig){
		ermSysIpConfig.setIsDataEffid(1);
		ermSysIpConfigDAO.merge(ermSysIpConfig);
	}
}
