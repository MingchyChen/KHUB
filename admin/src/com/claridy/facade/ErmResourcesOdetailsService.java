package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResourcesOdetailsDAO;
import com.claridy.domain.ErmResourcesOdetails;

@Service
public class ErmResourcesOdetailsService {

	
	@Autowired
	private IErmResourcesOdetailsDAO ermResourcsOdetailsDAO;
	
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmResourcesOdetails> findOdetailsList(String resourcesId){
		 List<ErmResourcesOdetails> listRet= ermResourcsOdetailsDAO.findOdetailsList(resourcesId);
		 return listRet;
	}
	public ErmResourcesOdetails getOdetails(String resourcesId,String year){
		return ermResourcsOdetailsDAO.getOdetails(resourcesId, year);
	}
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmResourcesOdetails odetails){
		ermResourcsOdetailsDAO.create(odetails);
	}
	/**
	 * 修改方法
	 * @param resReltitle
	 */
	public void update(ErmResourcesOdetails odetails){
		ermResourcsOdetailsDAO.merge(odetails);
	}
	/**
	 * 刪除方法
	 * @param resReltitle
	 */
	public void delete(ErmResourcesOdetails odetails){
		ermResourcsOdetailsDAO.delete(odetails);
	}
}
