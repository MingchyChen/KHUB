package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.hibernateimpl.ErmCodeGeneralCodeDAO;
import com.claridy.dao.hibernateimpl.ErmSysNotifyConfigDAO;
import com.claridy.dao.hibernateimpl.WebOrgDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

@Service
public class ErmSysNotiyConfigService {
	@Autowired
	private ErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;
	@Autowired
	private ErmSysNotifyConfigDAO ermSysNotifyConfigDAO;
	@Autowired
	private WebOrgDAO webOrgDAO;
	
	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeByItemId(String itemId){
		return ermCodeGeneralCodeDAO.findErmCodeGeneralCodeByItemId(itemId);
	}
	public List<ErmSysNotifyConfig> findALL(WebEmployee webEmployee){
		return ermSysNotifyConfigDAO.findAll(webEmployee);
	}
	public List<ErmSysNotifyConfig> findErmSysNofityCfgByTypeId(String typeId,WebEmployee webEmployee){
		return ermSysNotifyConfigDAO.findErmSysNofityCfgByTypeId(typeId, webEmployee);
	}
	
	public void deleteErmSysNotifyCfg(ErmSysNotifyConfig ermSysNotifyConfig){
		ermSysNotifyConfig.setIsDataEffid(0);
		ermSysNotifyConfigDAO.saveOrUpdate(ermSysNotifyConfig);
	}
	public List<WebOrg> findWebOrgListToCombox(){
		return webOrgDAO.findWebOrgListToCombox();
	}
	public List<ErmSysNotifyConfig> findedtAddList(String typeId,String groupId) {
		return ermSysNotifyConfigDAO.findedtAddList(typeId, groupId);
	}
	public void addErmSysNotifyCfg(ErmSysNotifyConfig ermSysNotifyConfig){
		ermSysNotifyConfig.setIsDataEffid(1);
		ermSysNotifyConfigDAO.saveOrUpdate(ermSysNotifyConfig);
	}
	public void editErmSysNotifyCfg(ErmSysNotifyConfig ermSysNotifyConfig){
		ermSysNotifyConfig.setIsDataEffid(1);
		ermSysNotifyConfigDAO.merge(ermSysNotifyConfig);
	}
}
