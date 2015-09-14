package com.claridy.dao;


import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.Sys_User;

public interface ISys_UserDAO extends IBaseDAO
{
	public Sys_User findSysUser(String name);
	public Sys_User getUser(String username,String password);
}
