package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;

public interface IWebIndexInfoDAO extends IBaseDAO{

	public List<WebIndexInfo> findByName(String name,WebEmployee webemployee,int isauth);
	
	public WebIndexInfo findByMatter(String matter);
	
	public WebIndexInfo getFooterInfo();
}
