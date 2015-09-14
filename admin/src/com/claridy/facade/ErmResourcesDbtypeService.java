package com.claridy.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmResourcesDbtypeDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesDbtype;

@Service
public class ErmResourcesDbtypeService {

	
	@Autowired
	private IErmResourcesDbtypeDAO ermResourcsDbtypeDAO;
	@Autowired
	private IErmCodeGeneralCodeDAO ermGeneralDao;
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmResourcesDbtype> findDbtypeList(String resourcesId){
		 List<ErmResourcesDbtype> list= ermResourcsDbtypeDAO.findDbtypeList(resourcesId);
		 List<ErmResourcesDbtype> listRet=new ArrayList<ErmResourcesDbtype>();
		 for (int i = 0; i < list.size(); i++) {
			 ErmResourcesDbtype dbtypeTemp=list.get(i);
			 ErmCodeGeneralCode ermGeneralTemp=ermGeneralDao.findByItemIDAndGeneralcodeId("DBTYPE",dbtypeTemp.getDbtypeId());
		 	 dbtypeTemp.setDbtypeName(ermGeneralTemp.getName1());
			 listRet.add(dbtypeTemp);
		 }
		 return listRet;
	}
	public ErmResourcesDbtype getDbtype(String resourcesId,String dbtypeId){
		return ermResourcsDbtypeDAO.getDbtype(resourcesId, dbtypeId);
	}
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmResourcesDbtype dbtype){
		ermResourcsDbtypeDAO.create(dbtype);
	}
	/**
	 * 修改方法
	 * @param resReltitle
	 */
	public void update(ErmResourcesDbtype dbtype){
		ermResourcsDbtypeDAO.merge(dbtype);
	}
	/**
	 * 刪除方法
	 * @param resReltitle
	 */
	public void delete(ErmResourcesDbtype dbtype){
		ermResourcsDbtypeDAO.delete(dbtype);
	}
}
