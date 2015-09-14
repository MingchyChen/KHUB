package com.claridy.common.mechanism.dao;

import java.util.List;

import com.claridy.common.mechanism.domain.Sys_File;

public interface ISys_FileDAO extends IBaseDAO
{
	public List<Sys_File> findAll() throws DataAccessException;
	
	public Sys_File find(String file_pk) throws DataAccessException;
	
	public Sys_File create(Sys_File sys_File) throws DataAccessException;
	public void delete(Sys_File sys_File) throws DataAccessException;
	
}
