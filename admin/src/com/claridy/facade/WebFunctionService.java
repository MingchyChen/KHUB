package com.claridy.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebFunctionDAO;

@Service
public class WebFunctionService {

	@Autowired
	public IWebFunctionDAO webfunctiondao;
	
}
