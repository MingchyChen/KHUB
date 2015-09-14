package com.claridy.dao;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebRellinkClick;

public interface IWebRellinkClickDAO extends IBaseDAO {
	
	
	public 	WebRellinkClick findByaccountIdAndUuid(String relinkId,String accountId);
}
