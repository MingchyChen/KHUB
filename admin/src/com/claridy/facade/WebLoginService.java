package com.claridy.facade;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebEmployeeDAO;
import com.claridy.domain.WebEmployee;

@Service
public class WebLoginService {
	
	@Autowired
	private IWebEmployeeDAO webEmployeeDao;
	
	/**
	 * 用戶登陸
	 * @param name 用戶名
	 * @param password 密碼
	 * @return
	 */
	
	public WebEmployee login(String name,String password){

		WebEmployee webEmployee=webEmployeeDao.login(name, password);
		if(webEmployee!=null){
			webEmployee.setLoginDate(new Date());
			webEmployeeDao.merge(webEmployee);
			return webEmployee;
		}
		
		return null;
		
		
	}
	
}
