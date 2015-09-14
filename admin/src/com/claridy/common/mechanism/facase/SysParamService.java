package com.claridy.common.mechanism.facase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_ParamDAO;
import com.claridy.common.mechanism.domain.Sys_Param;


@Service
public class SysParamService {

   	@Autowired
	private ISys_ParamDAO sys_ParamDAO;
   	
	public List<Sys_Param> findAll() throws DataAccessException{
		return sys_ParamDAO.findAll();
	} 
	
	public Sys_Param find(String func_no,String parent) throws DataAccessException{

		return sys_ParamDAO.find(func_no, parent);
	}
	
	public List<Sys_Param> findByCategory(String category) throws DataAccessException{
		return sys_ParamDAO.findByCategory(category);
	}
	
	public Sys_Param findByValue(String category, String funcValue) throws DataAccessException{
		return sys_ParamDAO.findByValue(category, funcValue);
	}
	
	public List<Sys_Param> findSysParamList(String parent){
		List<Sys_Param> list=sys_ParamDAO.findSysParamList(parent);
		return list;
	}
	
	public Sys_Param findSysParam(String funcno){
		Sys_Param sysparam=sys_ParamDAO.findByFuncno(funcno);
		return sysparam;
	}
}