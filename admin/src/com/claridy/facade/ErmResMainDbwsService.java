package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResMainDbwsDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;

@Service
public class ErmResMainDbwsService {

	@Autowired
	public IErmResMainDbwsDAO resMainDbwsDao;
	@Autowired
	public IErmResourcesMainfileVDAO resMainfileDao;
	@Autowired
	protected DaoImplUtil daoImpl;
	/**
	 * 根據resourcesId查詢結果
	 * @param uuid
	 * @return
	 */
	public ErmResourcesMainDbws getResMainDbwsByResId(String resourcesId){
		return resMainDbwsDao.getResMainDbwsByResId(resourcesId);
	}
	/**
	 * 根據resourcesId查詢結果
	 * @param uuid
	 * @return
	 */
	public ErmResourcesMainfileV getResMainfileByResId(String resourcesId){
		return resMainfileDao.getErmResMainfileVByResId(resourcesId);
	}
	/**
	 * 根據頁面條件查詢全部結果
	 * @param name
	 * @return
	 */
	public List<ErmResourcesMainDbws> findAllResMainDbwsList(
			ErmResourcesMainDbws ermResMainDbws, WebEmployee webEmployee){
		return resMainDbwsDao.findAllResMainDbwsList(ermResMainDbws,webEmployee);
	}
	/**
	 * 修改方法
	 * @param noticeTemp
	 */
	public void UpdResMainDbws(ErmResourcesMainDbws resMainDbws){
		resMainDbwsDao.merge(resMainDbws);
	}
	/**
	 * 新增方法
	 * @param webOrg
	 */
	public void addResMainDbws(ErmResourcesMainDbws resMainDbws){
		resMainDbwsDao.saveOrUpdate(resMainDbws);
	}
	
	/**
	 * 刪除方法
	 * @param webOrg
	 */
	public void deleteResMainDbws(ErmResourcesMainDbws resMainDbws){
		resMainDbws.setIsDataEffid(0);
		resMainDbwsDao.merge(resMainDbws); 
	}
	public List<ErmResourcesMainDbws> findAllList(){
		return resMainDbwsDao.findAllList();
	}
	
}
