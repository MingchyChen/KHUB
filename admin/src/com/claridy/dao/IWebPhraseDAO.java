package com.claridy.dao;

import java.util.List;


import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebPhrase;

public interface IWebPhraseDAO extends IBaseDAO {
	
	public List<WebPhrase> find(String keyWord);
	
	public WebPhrase findById(String uuid);
}
