package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebFounctionEmployeeDAO;
import com.claridy.domain.WebFunctionEmployee;

@Service
public class WebFunctionEmployeeService {

	@Autowired
	private IWebFounctionEmployeeDAO webFunctionEmpDao;
	
	
	public void addWebFunctionEmployee(WebFunctionEmployee webFunctionEmp){
		webFunctionEmpDao.saveOrUpdate(webFunctionEmp);
	}
	
	public List<WebFunctionEmployee> getWebFunctionEmployee(String employeesn){
		return webFunctionEmpDao.getWebFunctionEmployee(employeesn);
	}
	
	public void deleteWebFunctionEmp(String webEmployeesn){
		webFunctionEmpDao.deleteWebFunctinEmp(webEmployeesn);
	}
	
}
