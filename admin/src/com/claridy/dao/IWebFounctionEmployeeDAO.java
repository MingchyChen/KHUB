package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebFunctionEmployee;

public interface IWebFounctionEmployeeDAO extends IBaseDAO {
	public List<WebFunctionEmployee> getWebFunctionEmployee(String employeesn);
	
	public void deleteWebFunctinEmp(String employeesn);
}
