package com.claridy.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IWebCooperationDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.AccountNumber;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebOrg;

@Service
public class CooperUsageService {
	@Autowired
	private IWebOrgDAO webOrgDao;
	
	@Autowired
	private IWebCooperationDAO webCooperationDao;
	
	/**
	 * 根據時間單位查詢帳號統計集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountNumber> findCooperUsageList(Date startDate, Date endDate){
		List<AccountNumber> accNumberListRet=new ArrayList<AccountNumber>();
		List<WebOrg> parentOrgList=webOrgDao.findParentOrg("");
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationList(startDate, endDate);
		for (int i = 0; i < parentOrgList.size(); i++) {
			WebOrg webOrg=parentOrgList.get(i);
			AccountNumber accNumberTemp=new AccountNumber();
			int cooperNumber=0;
			for (int j = 0; j < webCoopertList.size(); j++) {
				WebCooperation webCooperation=webCoopertList.get(j);
				WebAccount webAccount=webCooperation.getApplyAccount();
				if(webAccount.getParentorgid().equals(webOrg.getOrgId())){
					cooperNumber++;
				}
			}
			accNumberTemp.setParentOrgName(webOrg.getOrgName());
			accNumberTemp.setpUrl(webOrg.getOrgId());
			accNumberTemp.setCooperNumber(cooperNumber);
			accNumberListRet.add(accNumberTemp);
		}
		return accNumberListRet;
	}
	/**
	 * 根據時間單位查詢個組室帳號統計集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountNumber> findCooperUsageListByOrg(Date startDate, Date endDate,String parentOrgId){
		List<AccountNumber> accNumberListRet=new ArrayList<AccountNumber>();
		List<WebOrg> orgList=webOrgDao.findOrg(parentOrgId);
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationList(startDate, endDate);
		for (int i = 0; i < orgList.size(); i++) {
			WebOrg webOrg=orgList.get(i);
			AccountNumber accNumberTemp=new AccountNumber();
			int cooperNumber=0;
			for (int j = 0; j < webCoopertList.size(); j++) {
				WebCooperation webCooperation=webCoopertList.get(j);
				WebAccount webAccount=webCooperation.getApplyAccount();
				if(webAccount.getOrgid()!=null&&!"".equals(webAccount.getOrgid())&&webAccount.getOrgid().equals(webOrg.getOrgId())){
					cooperNumber++;
				}
			}
			accNumberTemp.setParentOrgName(webOrg.getOrgName());
			accNumberTemp.setpUrl(webOrg.getOrgId());
			accNumberTemp.setCooperNumber(cooperNumber);
			accNumberListRet.add(accNumberTemp);
		}
		
		AccountNumber OaccNumberTemp=new AccountNumber();
		int OcooperNumber=0;
		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation=webCoopertList.get(j);
			WebAccount webAccount=webCooperation.getApplyAccount();
			if(webAccount.getParentorgid().equals(parentOrgId)&&("".equals(webAccount.getOrgid())||webAccount.getOrgid()==null)){
				OcooperNumber++;
			}
		}
		//其他
		OaccNumberTemp.setParentOrgName(Labels.getLabel("webEmployee.tboxIdType.other"));
		OaccNumberTemp.setpUrl("other"+parentOrgId);
		OaccNumberTemp.setCooperNumber(OcooperNumber);
		accNumberListRet.add(OaccNumberTemp);
		
		int cooperNumber=0;
		for (int i = 0; i < accNumberListRet.size(); i++) {
			AccountNumber accNumberTemp=accNumberListRet.get(i);
			cooperNumber+=accNumberTemp.getCooperNumber();
		}
		AccountNumber accNumberTemp=new AccountNumber();
		//小計
		accNumberTemp.setParentOrgName(Labels.getLabel("subTitle"));
		accNumberTemp.setpUrl(parentOrgId);
		accNumberTemp.setCooperNumber(cooperNumber);
		accNumberListRet.add(accNumberTemp);
		return accNumberListRet;
	}
	
	/**
	 * 根據時間單位查詢管合申請量詳細信息
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByParentOrg(Date startDate, Date endDate,String parentOrgId){
		List<WebCooperation> cooperNumberListRet=new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationList(startDate, endDate);
		
		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation=webCoopertList.get(j);
			WebAccount webAccount=webCooperation.getApplyAccount();
			if(webAccount.getParentorgid().equals(parentOrgId)){
				cooperNumberListRet.add(webCooperation);
			}
		}
		
		WebOrg webPraentOrg=null;
		WebOrg webOrg=null;
		for(int i=0;i<cooperNumberListRet.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}
			
			webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if(webOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}
	/**
	 * 根據時間單位查詢管合申請量詳細信息
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByParentOrgOther(Date startDate, Date endDate,String parentOrgId){
		List<WebCooperation> cooperNumberListRet=new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationList(startDate, endDate);
		
		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation=webCoopertList.get(j);
			WebAccount webAccount=webCooperation.getApplyAccount();
			if(webAccount.getParentorgid().equals(parentOrgId)&&(webAccount.getOrgid()==null||webAccount.getOrgid().equals(""))){
				cooperNumberListRet.add(webCooperation);
			}
		}
		
		WebOrg webPraentOrg=null;
		WebOrg webOrg=null;
		for(int i=0;i<cooperNumberListRet.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}
			
			webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if(webOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}
	/**
	 * 根據時間組室查詢管合申請量詳細信息
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByOrg(Date startDate, Date endDate,String orgId){
		List<WebCooperation> cooperNumberListRet=new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationList(startDate, endDate);
		
		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation=webCoopertList.get(j);
			WebAccount webAccount=webCooperation.getApplyAccount();
			if(webAccount.getOrgid()!=null&&webAccount.getOrgid().equals(orgId)){
				cooperNumberListRet.add(webCooperation);
			}
		}
		
		WebOrg webPraentOrg=null;
		WebOrg webOrg=null;
		for(int i=0;i<cooperNumberListRet.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}
			
			webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if(webOrg!=null){
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			}else{
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}
}
