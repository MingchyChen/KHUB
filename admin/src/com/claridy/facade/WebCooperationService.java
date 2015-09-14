package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebCooperationDAO;
import com.claridy.dao.IWebEmployeeDAO;
import com.claridy.dao.IWebErwSourceDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.dao.IWebPhraseDAO;
import com.claridy.dao.hibernateimpl.WebCooperationDAO;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebPhrase;


@Service
public class WebCooperationService {

	@Autowired
	private IWebCooperationDAO webCooperationDAO;
	
	@Autowired
	private IWebOrgDAO webOrgDao;
	
	@Autowired
	private IWebErwSourceDAO webErwSourceDAO;
	@Autowired
	private IWebPhraseDAO webPhraseDAO;
	@Autowired
	private IWebEmployeeDAO webEmployeeDAO;
	@Autowired
	private IWebAccountDAO webAccountDAO;
	/**
	 * 查詢全部
	 * @param 
	 * @return List<WebCooperation>
	 */
	public List<WebCooperation> findAll(WebEmployee webEmployee){
		List<WebCooperation> webCooperationList=webCooperationDAO.find(null,webEmployee,null, null,null, -1, null, null);
		WebOrg webPraentOrg=null;
		for(int i=0;i<webCooperationList.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				webCooperationList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				webCooperationList.remove(i);
			}
		}
		return webCooperationList;
	}
	/**
	 * 根據條件搜索結果
	 * @param webCooperation
	 * @return List<WebCooperation>
	 */
	public List<WebCooperation> findByWebCooperation(WebOrg applyWebOrg,WebEmployee webEmployee,WebOrg acceptWebOrg,String uuid,String acceptPeople,int status,Date startDate,Date endDate){
		List<WebCooperation> webCooperationList=webCooperationDAO.find(applyWebOrg,webEmployee, acceptWebOrg, uuid, acceptPeople, status, startDate, endDate);
		WebOrg webPraentOrg=null;
		for(int i=0;i<webCooperationList.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				webCooperationList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				webCooperationList.remove(i);
			}
		}
		return webCooperationList;
	}
	/**
	 * 根據id查詢結果
	 * @param uuid
	 * @return WebCooperation
	 */
	public WebCooperation findById(String uuid,WebEmployee webEmployee){
		WebCooperation webCooperation=new WebCooperation();
		webCooperation.setUuid(uuid);
		return webCooperationDAO.find(null,webEmployee,null, uuid,null, -1, null, null).get(0);
	}
	
	/**
	 * 修改
	 * @param webCooperation
	 */
	public void update(WebCooperation webCooperation){
		webCooperationDAO.merge(webCooperation);
	}
	
	
	public List<WebOrg> findParentOrg(String orgId){
		List<WebOrg> weblist;
		weblist=webOrgDao.findParentOrg(orgId);
		
		return weblist;
	}
	

	public List<WebErwSource> findWeberSource(){
		return webErwSourceDAO.findAll();
	}
	
	public WebPhrase findByWebPhraseId(String uuid){
		return webPhraseDAO.findById(uuid);
	}
	
	public List<WebEmployee> findWebEmployeeListByParentOrgId(String parentOrgId){
		return webEmployeeDAO.findIsMangerByParent(parentOrgId);
	}
	
	public WebAccount findById(String name){
		return webAccountDAO.findById(name);
	}
}
