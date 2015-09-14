package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmResourcesConfigDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesConfig;
import com.claridy.domain.WebEmployee;

@Service
public class ErmResourcesConfigService {

	
	@Autowired
	private IErmResourcesConfigDAO ermResourcsConfigDAO;
	@Autowired
	private IErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;
	
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmCodeGeneralCode> findAll(WebEmployee webEmployee){
		 List<ErmCodeGeneralCode> remCodeList= ermCodeGeneralCodeDAO.findByItemId(webEmployee, "RETYPE");
		 for(int i=0;i<remCodeList.size();i++){
			 ErmResourcesConfig ermResourcesConfig=ermResourcsConfigDAO.findById(webEmployee, remCodeList.get(i).getGeneralcodeId());
			 if(ermResourcesConfig==null){
				 remCodeList.remove(i);
				 i=i-1;
			 } 
		 }
		 return remCodeList;
	}
	
	public List<ErmCodeGeneralCode> findCodeAll(WebEmployee webEmployee){
		List<ErmCodeGeneralCode> remCodeList=ermCodeGeneralCodeDAO.findByItemId(webEmployee, "RETYPE");
		for(int i=0;i<remCodeList.size();i++){
			 if(remCodeList.get(i).getGeneralcodeId().equals("EB")||remCodeList.get(i).getGeneralcodeId().equals("WS")){
				 remCodeList.remove(i);
				 i=i-1;
			 }
			 
		 }
		return remCodeList;
	}
	/**
	 * 根據generalcodeid查詢
	 * @param webemployee
	 * @param generalCodeId
	 * @return
	 */
	public List<ErmCodeGeneralCode> findById(WebEmployee webemployee,String generalCodeId){
		return  ermCodeGeneralCodeDAO.findBygeneralId(webemployee,generalCodeId);
	}
	
	/**
	 * 根據typeId查詢
	 * @param webemployee
	 * @param typeId
	 * @return
	 */
	public ErmResourcesConfig findByTypeId(WebEmployee webemployee,String typeId){
		return ermResourcsConfigDAO.findById(webemployee,typeId);
	}
	
	
	/**
	 * 修改
	 * @param ermResourcesConfig
	 */
	public void update(ErmResourcesConfig ermResourcesConfig){
		ermResourcsConfigDAO.merge(ermResourcesConfig);
	}
	
	
	/**
	 * 修改表的isdataeffid為0
	 * @param ermResourcesConfig
	 */
	public void delete(ErmResourcesConfig ermResourcesConfig){
		ermResourcsConfigDAO.merge(ermResourcesConfig);
	}
}
