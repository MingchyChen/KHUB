package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebEmployee;

public interface IErmNewsDAO extends IBaseDAO {
	public List<ErmNews> findAllErmNews(WebEmployee webEmployee);

	public List<ErmNews> findErmNewsByParam(String title, String onDown,
			WebEmployee webEmployee);

	public List<ErmNews> findedtAddList(String searchType, String searchValue);
}
