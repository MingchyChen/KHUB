package com.claridy.dao;

import java.util.Date;
import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebKeyWord;

public interface IWebKeywordDAO extends IBaseDAO {
	public List<WebKeyWord> findkeyword(String accountid);

	public List<WebKeyWord> getkeybyaccount(String accountid);

	public WebKeyWord getKWByAccountAndKey(String keyword, String accountid,int target);
	
	public List<WebKeyWord> findSelUseNumberList(Date startDate, Date endDate);
}
