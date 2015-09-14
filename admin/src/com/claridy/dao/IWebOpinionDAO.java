package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinion;
import com.claridy.domain.WebOpinionReply;

public interface IWebOpinionDAO extends IBaseDAO {
	public List<WebOpinion> findOpinionInfoAll(WebEmployee webEmployee);
	public List<WebOpinion> findOpinionByTitle(WebEmployee webEmployee,String title);
	public List<WebOpinion> findedtAddList(String searchType, String searchValue);
	public List<WebOpinionReply> findReplyList(String searchType, String searchValue);
	public WebOpinionReply findReply(String searchType, String searchValue);
}
