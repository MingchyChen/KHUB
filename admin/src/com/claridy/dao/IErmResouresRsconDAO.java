package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesRscon;

public interface IErmResouresRsconDAO extends IBaseDAO {
	
	public List<ErmResourcesRscon> findbyDate(Date sdate,Date eDate,String status);
	
	

}
