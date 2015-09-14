package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;

public interface IErmSysNotifyConfigDAO extends IBaseDAO {
	public  List<ErmSysNotifyConfig> findAll(WebEmployee webEmployee);
	public List<ErmSysNotifyConfig> findErmSysNofityCfgByTypeId(String typeId,WebEmployee webEmployee);
	public List<ErmSysNotifyConfig> findedtAddList(String typeId,String groupId) ;
}
