package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResourcesReltitleDAO;
import com.claridy.domain.ErmResourcesReltitle;

@Service
public class ErmResourcesReltitleService {

	
	@Autowired
	private IErmResourcesReltitleDAO ermResourcsReltitleDAO;
	
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmResourcesReltitle> findReltitleList(String resourcesId){
		 List<ErmResourcesReltitle> listRet= ermResourcsReltitleDAO.findReltitleList(resourcesId);
		 return listRet;
	}
	public ErmResourcesReltitle getReltitle(String resourcesId,String relatedTitleId){
		return ermResourcsReltitleDAO.getReltitle(resourcesId, relatedTitleId);
	}
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmResourcesReltitle resReltitle){
		ermResourcsReltitleDAO.create(resReltitle);
	}
	/**
	 * 修改方法
	 * @param resReltitle
	 */
	public void update(ErmResourcesReltitle resReltitle){
		ermResourcsReltitleDAO.merge(resReltitle);
	}
	/**
	 * 刪除方法
	 * @param resReltitle
	 */
	public void delete(ErmResourcesReltitle resReltitle){
		ermResourcsReltitleDAO.delete(resReltitle);
	}
}
