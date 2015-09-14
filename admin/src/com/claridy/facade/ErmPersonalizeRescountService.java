package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmPersonalizeRescountDAO;
import com.claridy.domain.ErmPersonalizeRescount;

@Service
public class ErmPersonalizeRescountService {

	
	@Autowired
	private IErmPersonalizeRescountDAO ermPerRescountDao;
	
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmPersonalizeRescount> findPersonalizeRescountList(){
		 List<ErmPersonalizeRescount> listRet= ermPerRescountDao.findPersonalizeRescountList();
		 return listRet;
	}
	/**
	 * 根據使用者賬號和資源編號查詢對象
	 * @param accountId
	 * @param resourcesId
	 * @return
	 */
	public ErmPersonalizeRescount getPersonalizeRescount(String accountId,String resourcesId,String dbId){
		return ermPerRescountDao.getPersonalizeRescount(accountId, resourcesId,dbId);
	}
	/**
	 * 
	 */
	
	
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmPersonalizeRescount odetails){
		ermPerRescountDao.create(odetails);
	}
	/**
	 * 修改方法
	 * @param resReltitle
	 */
	public void update(ErmPersonalizeRescount odetails){
		ermPerRescountDao.merge(odetails);
	}
}
