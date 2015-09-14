package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmSysPhoneticCompare;
import com.claridy.domain.WebEmployee;

public interface IermSysPhoneticCompareDAO extends IBaseDAO {

	public List<ErmSysPhoneticCompare> findErmSys(ErmSysPhoneticCompare ermsys,WebEmployee webEmployee);
}
