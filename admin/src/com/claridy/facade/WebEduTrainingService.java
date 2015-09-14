package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebEduTrainingDAO;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;

@Service
public class WebEduTrainingService {

	@Autowired
	private IWebEduTrainingDAO webEduTrainingDAO;
	
	public List<WebEduTraining> findByDate(Date startDate,Date endDate,WebEmployee webEmployee){
		return webEduTrainingDAO.findBy(startDate, endDate, null,webEmployee);
	}
	
	public WebEduTraining findById(String uuid,WebEmployee webEmployee){
		return webEduTrainingDAO.findBy(null, null, uuid,webEmployee).get(0);
	}
	
	public List<WebEduTraining> findAll(WebEmployee webEmployee){
		return webEduTrainingDAO.findBy(null, null, null,webEmployee);
	}
	
	public void delete(WebEduTraining webEdutrain){
		webEdutrain.setIsDataEffid(0);
		webEduTrainingDAO.merge(webEdutrain);
	}
	
	public void update(WebEduTraining webEdutrain){
		webEdutrain.setIsDataEffid(1);
		webEduTrainingDAO.merge(webEdutrain);
	}
	
	public void save(WebEduTraining webEdutrain){
		webEdutrain.setIsDataEffid(1);
		webEduTrainingDAO.saveOrUpdate(webEdutrain);
	}
	public List<WebEduTraining> findedtAddList(String searchType,String searchValue) {
		return webEduTrainingDAO.findedtAddList(searchType, searchValue);
	}
}
