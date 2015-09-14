package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebLog;
import com.claridy.domain.WebSysLog;

public interface IWebLogDAO extends IBaseDAO {
	
	public List<WebSysLog> search(String nLocate) throws DataAccessException;
	
	public List<WebLog> findWebLogList(Date startDate, Date endDate,String nLocate);
}
