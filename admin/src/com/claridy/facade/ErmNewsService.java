package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmNewsDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebEmployee;

@Service
public class ErmNewsService {
	@Autowired
	private IErmNewsDAO ermNewsDAO;

	public List<ErmNews> findAllErmNews(WebEmployee webEmployee) {
		return ermNewsDAO.findAllErmNews(webEmployee);
	}

	public List<ErmNews> findErmNewsByParam(String title, String onDown,
			WebEmployee webEmployee) {
		return ermNewsDAO.findErmNewsByParam(title, onDown, webEmployee);
	}

	public void deleteErmNews(ErmNews ermNews) {
		ermNews.setIsDataEffid(0);
		ermNewsDAO.merge(ermNews);
	}

	public void addErmNews(ErmNews ermNews) {
		ermNews.setIsDataEffid(1);
		ermNewsDAO.merge(ermNews);
	}

	public void updateErmNews(ErmNews ermNews) {
		ermNews.setIsDataEffid(1);
		ermNewsDAO.merge(ermNews);
	}

	public List<ErmNews> findedtAddList(String searchType, String searchValue) {
		return ermNewsDAO.findedtAddList(searchType, searchValue);
	}
}
