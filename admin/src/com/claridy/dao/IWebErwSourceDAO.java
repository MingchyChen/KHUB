package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebErwSourceUnit;

public interface IWebErwSourceDAO extends IBaseDAO {
	
	/**
	 * 根據UUID取得WebErwSource
	 * @param uuid
	 * @return
	 */
	public WebErwSource getWebErwSourceByUUID(String uuid);
	
	/**
	 * 根據DBID取得WebErwSource
	 * @param dbid
	 * @return
	 */
	public WebErwSource getWebErwSourceByDBID(String dbid);
	
	/**
	 * 取得WebErwSource列表
	 * @param webErwSource
	 * @param webEmployee
	 * @return
	 */
	public List<WebErwSource> getWebErwSourceList(WebErwSource webErwSource, WebEmployee webEmployee);
	
	/**
	 * 更新WebErwSource
	 * @param webErwSource
	 * @return
	 */
	public WebErwSource updateWebErwSource(WebErwSource webErwSource);
	
	/**
	 * 新增WebErwSource
	 * @param webErwSource
	 * @return	UUID
	 */
	public WebErwSource addWebErwSource(WebErwSource webErwSource);
	/**
	 * 查詢webErwSource全部
	 * @return
	 */
	public List<WebErwSource> findAll();
}
