package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.ErmSysIpconfig;

public interface IErmSysIpConfigDAO extends IBaseDAO {
	public List<ErmSysIpconfig> findErmSysIpConfigAll(String isOpenRdoValue,String parentOrgName,WebEmployee webEmployee);
	public List<ErmSysIpconfig> findedtAddList(String searchType,String searchValue);
	public List<ErmSysIpconfig> findRelLinkByMenu(String menuType);
	public ErmSysIpconfig findByUuid(String uuid);
	public List<ErmSysIpconfig> findErmSysIp(String searchType, String searchValue);
}
