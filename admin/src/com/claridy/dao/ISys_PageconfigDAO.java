package com.claridy.dao;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.Sys_Pageconfig;


public interface ISys_PageconfigDAO extends IBaseDAO {

	public Sys_Pageconfig findBySchoolID(String schoolID);
	
}
