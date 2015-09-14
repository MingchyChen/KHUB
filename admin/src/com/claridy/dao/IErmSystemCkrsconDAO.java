package com.claridy.dao;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmSystemCkrscon;
import com.claridy.domain.WebEmployee;

public interface IErmSystemCkrsconDAO extends IBaseDAO {
	public ErmSystemCkrscon getErmSystemCkrscon(WebEmployee webEmployee);
	
	public ErmSystemCkrscon getErmSystemCkrsconAll();
}
