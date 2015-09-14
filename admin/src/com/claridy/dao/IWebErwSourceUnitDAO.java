package com.claridy.dao;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebErwSourceUnit;

public interface IWebErwSourceUnitDAO extends IBaseDAO {
	
	/**
	 * 根據UUID取得WebErwSourceUnit
	 * @param uuid
	 * @return
	 */
	public WebErwSourceUnit getWebErwSourceUnitByUUID(String uuid);
	
	/**
	 * 更新WebErwSourceUnit
	 * @param webErwSourceUnit
	 * @return
	 */
	public WebErwSourceUnit updateWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit);
	
	/**
	 * 新增WebErwSourceUnit
	 * @param webErwSourceUnit
	 * @return	UUID
	 */
	public WebErwSourceUnit addWebErwSourceUnit(WebErwSourceUnit webErwSourceUnit);
}
