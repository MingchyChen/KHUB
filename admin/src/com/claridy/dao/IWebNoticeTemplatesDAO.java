package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;

public interface IWebNoticeTemplatesDAO extends IBaseDAO {
	public WebNoticeTemplates findWebNoticeTempByUuid(String uuid);
	public List<WebNoticeTemplates> findAllWebNoticeTemp(String name,WebEmployee webEmployee);
}
