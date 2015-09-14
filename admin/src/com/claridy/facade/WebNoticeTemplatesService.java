package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebNoticeTemplatesDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;

@Service
public class WebNoticeTemplatesService {

	@Autowired
	public IWebNoticeTemplatesDAO webnoticetempdao;
	/**
	 * 根據uuid查詢結果
	 * @param uuid
	 * @return
	 */
	public WebNoticeTemplates getWebNoticeTempByUuid(String uuid){
		return webnoticetempdao.findWebNoticeTempByUuid(uuid);
	}
	/**
	 * 根據範本名稱查詢結果/查詢全部結果
	 * @param name
	 * @return
	 */
	public List<WebNoticeTemplates> findAllWebNoticeTemp(String name,WebEmployee webEmployee){
		return webnoticetempdao.findAllWebNoticeTemp(name,webEmployee);
	}
	/**
	 * 修改方法
	 * @param noticeTemp
	 */
	public void SaveOrUpdNoticeTemp(WebNoticeTemplates noticeTemp){
		webnoticetempdao.merge(noticeTemp);
	}
}
