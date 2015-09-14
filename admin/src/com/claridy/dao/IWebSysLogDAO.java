package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebSysLog;

public interface IWebSysLogDAO extends IBaseDAO {
	
	public List<WebSysLog> search(String nLocate) throws DataAccessException;
	
}
