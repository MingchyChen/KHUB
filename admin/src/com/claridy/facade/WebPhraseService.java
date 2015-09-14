package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebCooperationDAO;
import com.claridy.dao.IWebPhraseDAO;
import com.claridy.domain.WebPhrase;

@Service
public class WebPhraseService {
	
	@Autowired
	private IWebPhraseDAO webPhraseDAO;
	
	public List<WebPhrase> findAll(){
		return webPhraseDAO.find(null);
	}
	
	public void insert(WebPhrase webPhrase){
		webPhraseDAO.saveOrUpdate(webPhrase);
	}
	
	public List<WebPhrase> findByKeyWord(String keyWork){
		return webPhraseDAO.find(keyWork);
	}
	
	public WebPhrase findById(String uuid){
		return webPhraseDAO.findById(uuid);
	}
	
	
	
}
