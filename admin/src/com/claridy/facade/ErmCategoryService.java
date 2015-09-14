package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCategoryDAO;
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.domain.ErmCodeCategory;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;

@Service
public class ErmCategoryService {

	@Autowired
	private IErmCategoryDAO ermCategoryDao;
	@Autowired
	public IErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;

	public List<ErmCodeCategory> findAll(String typeId, String categoryType,
			WebEmployee webEmployee) {
		return ermCategoryDao.findAll(typeId, categoryType, webEmployee);
	}

	/**
	 * 刪除單位權限
	 * 
	 * @param webOrg
	 */
	public void deleteWebOrg(ErmCodeCategory ermCategory) {
		ermCategoryDao.merge(ermCategory);
	}

	/**
	 * 根據itemId獲取數據集合
	 * 
	 * @param itemId
	 * @return
	 */
	public List<ErmCodeGeneralCode> findGeneralCodeByItemId(String itemId) {
		return ermCodeGeneralCodeDAO.findErmCodeGeneralCodeByItemId(itemId);
	}
}
