package com.claridy.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeDbDAO;
import com.claridy.dao.IErmResourcesScollegeLogDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesScollegeLog;
import com.claridy.domain.WebOrg;

@Service
public class ErmResourcesScollegeLogService {
	
	@Autowired
	private IErmResourcesScollegeLogDAO ermResourcsScollDAO;
	@Autowired
	private IWebOrgDAO webOrgDAO;
	@Autowired
	private IErmCodeDbDAO ermCodeDbDAO;
	/**
	 * 查詢全部
	 * @param webEmployee
	 * @return
	 */
	public List<ErmResourcesScollegeLog> findScoLogList(String resourcesId){
		 List<ErmResourcesScollegeLog> list= ermResourcsScollDAO.findScoLogList(resourcesId);
		 List<ErmResourcesScollegeLog> listRet=new ArrayList<ErmResourcesScollegeLog>();
		 for (int i = 0; i < list.size(); i++) {
			 ErmResourcesScollegeLog scollTemp=list.get(i);
			 WebOrg webOrg=webOrgDAO.getOrgById(scollTemp.getSuitcollegeId());
			 scollTemp.setOrgName(webOrg.getOrgName());
			 ErmCodeDb ermCodeDb=ermCodeDbDAO.getErmDbByDbId(scollTemp.getDbId());
			 if(ermCodeDb!=null){
				 scollTemp.setDbName(ermCodeDb.getName());
			 }
			 listRet.add(scollTemp);
		 }
		 return listRet;
	}
	/**
	 * 判斷數據是否已存在
	 * @param resourcesId
	 * @param suitcollegeId
	 * @return
	 */
	public Integer getScollegeLog(String resourcesId,String suitcollegeId,String dbId){
		return ermResourcsScollDAO.getScollegeLog(resourcesId, suitcollegeId,dbId);
	}
	/**
	 * 新增方法
	 * @param resReltitle
	 */
	public void create(ErmResourcesScollegeLog recommon){
		ermResourcsScollDAO.saveOrUpdate(recommon);
	}
	/**
	 * 刪除方法
	 * @param resReltitle
	 */
	public void delete(ErmResourcesScollegeLog recommon){
		recommon.setIsDataEffid(0);
		ermResourcsScollDAO.merge(recommon);
	}
}
