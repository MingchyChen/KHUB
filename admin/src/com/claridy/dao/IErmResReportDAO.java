package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.ErmSysIpconfig;

public interface IErmResReportDAO extends IBaseDAO {

	public List<Object> findSendReport(int firstYear,
			String firstMonth, int secondYear, String secondMonth);
	
	public List<Object> findErmResourcesRsconAll(int firstYear,
			int secondYear);
}
