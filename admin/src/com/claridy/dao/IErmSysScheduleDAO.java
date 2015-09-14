package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;

public interface IErmSysScheduleDAO extends IBaseDAO {

	public List<ErmSysSchedule> findAll(WebEmployee webEmployee);
	
	public ErmSysSchedule findById(String id,WebEmployee webEmployee);
	
	public ErmSysSchedule findAllByStatus(String uuid);
}
