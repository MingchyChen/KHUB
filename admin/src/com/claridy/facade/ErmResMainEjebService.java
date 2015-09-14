package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResourcesMainEjebDAO;
import com.claridy.dao.IErmResourcesMainEjebItemDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.ErmResourcesMainfileV;

@Service
public class ErmResMainEjebService {
	/**
	 * 根據resourcesId查詢結果
	 * 
	 * @param uuid
	 * @return
	 */
	@Autowired
	public IErmResourcesMainfileVDAO resMainfileDao;
	@Autowired
	public IErmResourcesMainEjebDAO resourcesMainEjebDAO;
	@Autowired
	public IErmResourcesMainEjebItemDAO resourcesMainEjebItemDAO;

	public ErmResourcesMainfileV getResMainfileByResId(String resourcesId) {
		return resMainfileDao.getErmResMainfileVByResId(resourcesId);
	}
	/**
	 * 修改方法
	 * @param noticeTemp
	 */
	public void UpdResMainEjeb(ErmResourcesMainEjeb ermResourcesMainEjeb,ErmResourcesEjebItem ejebItem){
		resourcesMainEjebDAO.merge(ermResourcesMainEjeb);
		resourcesMainEjebItemDAO.merge(ejebItem);
	}
	/****
	 * 單獨新增mainEjeb
	 * @param ermResourcesMainEjeb
	 */
	public void updMainEjebOne(ErmResourcesMainEjeb ermResourcesMainEjeb){
		resourcesMainEjebDAO.merge(ermResourcesMainEjeb);
	}
	/****
	 * 修改ErmResourcesEjebItem
	 */
	public void updMainEjebItemOne(ErmResourcesEjebItem ermResourcesEjebItem){
		//resourcesMainEjebItemDAO.getResMainEjebItemByResId(resourcesId);
		resourcesMainEjebItemDAO.merge(ermResourcesEjebItem);
	}
	/**
	 * 根據resourcesId查詢ejebItem
	 * 
	 */
	
	 public ErmResourcesEjebItem getResMainEjebItemByResId(String resourcesId){
		List<ErmResourcesEjebItem> list= resourcesMainEjebItemDAO.getResMainEjebItemByResId(resourcesId);
		ErmResourcesEjebItem ermResourcesEjebItem=list.get(0);
		 return ermResourcesEjebItem;
	}
	 /**
		 * 根據resourcesId查詢ejebItem集合
		 * 
		 */
		
		 public List<ErmResourcesEjebItem> findResMainEjebItemByResId(String resourcesId){
			List<ErmResourcesEjebItem> list= resourcesMainEjebItemDAO.getResMainEjebItemByResId(resourcesId);
			 return list;
		}
	 /**
		 * 根據resourcesId查詢ejeb
		 * 
		 */
		
		 public ErmResourcesMainEjeb getResMainEjebByResId(String resourcesId){
			ErmResourcesMainEjeb ermResourcesEjeb= resourcesMainEjebDAO.getResMainEjebByResId(resourcesId);
			 return ermResourcesEjeb;
		}
	/**
	 * 新增方法
	 * @param webOrg
	 */
	public void addResMainEjeb(ErmResourcesMainEjeb ermResourcesMainEjeb,ErmResourcesEjebItem ejebItem){
		resourcesMainEjebDAO.saveOrUpdate(ermResourcesMainEjeb);
		resourcesMainEjebItemDAO.saveOrUpdate(ejebItem);
	}
	/**
	 * 修改方法
	 * @param webOrg
	 */
	public void updateResMainEjeb(ErmResourcesMainEjeb ermResourcesMainEjeb,ErmResourcesEjebItem ejebItem){
		resourcesMainEjebDAO.merge(ermResourcesMainEjeb);
		resourcesMainEjebItemDAO.merge(ejebItem);
	}
	/**
	 * 修改方法
	 * @param ejebItem
	 */
	public void addResMainEjeb(ErmResourcesEjebItem ejebItem){
		resourcesMainEjebItemDAO.saveOrUpdate(ejebItem);
	}
	
	/**
	 * 刪除方法
	 * @param webOrg
	 */
	public void deleteResMainEjeb(ErmResourcesMainEjeb ermResourcesMainEjeb){
		ermResourcesMainEjeb.setIsDataEffid(0);
		resMainfileDao.merge(ermResourcesMainEjeb); 
	}
	/**
	 * 刪除所屬資料庫方法
	 * @param webOrg
	 */
	public void deleteErmEjebItem(ErmResourcesEjebItem ejebItem){
		ejebItem.setHistory("Y");
		resMainfileDao.merge(ejebItem); 
	}
	/***
	 * 查詢全部ejeb
	 */
	public List<ErmResourcesMainEjeb> findAllEjeb(){
		return resourcesMainEjebDAO.findAllResMainEjebList();
	}
/***
 * resourcesId和dbId查詢
 */
	public ErmResourcesMainfileV getErmResMainfileVByResIdAndDbid(
			String resourcesId, String dbId) {
		return resMainfileDao.getErmResMainfileVByResIdAndDbid(resourcesId, dbId);
	}
	/***
	 * 使用resourcesId查詢ErmResourcesMainfileV
	 */
	public List<ErmResourcesMainfileV> findByTypeId(String typeId){
		return resMainfileDao.findByTypeId(typeId);
	}
}

