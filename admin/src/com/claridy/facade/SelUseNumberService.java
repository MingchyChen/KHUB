package com.claridy.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebKeywordDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.SelUseNumber;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebKeyWord;
import com.claridy.domain.WebOrg;

@Service
public class SelUseNumberService {
	@Autowired
	private IWebOrgDAO webOrgDao;
	@Autowired
	private IWebAccountDAO webAccountDao;
	@Autowired
	private IWebKeywordDAO webKeyDao;
	
	/**
	 * 根據時間查詢查詢種類使用量統計集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<SelUseNumber> findSelUseNumberList(Date startDate, Date endDate){
		List<SelUseNumber> selNumberListRet=new ArrayList<SelUseNumber>();
		List<WebAccount> webAccountList=webAccountDao.findAccountList();
		List<WebOrg> parentOrgList=webOrgDao.findParentOrg("");
		List<WebKeyWord> webKeyWordList=webKeyDao.findSelUseNumberList(startDate, endDate);
		for (int i = 0; i < parentOrgList.size(); i++) {
			WebOrg webOrg=parentOrgList.get(i);
			SelUseNumber selUseNumTemp=new SelUseNumber();
			int titleNumber=0;
			int summonNumber=0;
			int museNumber=0;
			int ermNumber=0;
			for (WebKeyWord webKeyWord:webKeyWordList) {
				for (WebAccount webAccount:webAccountList) {
					if(webAccount.getUuid().equals(webKeyWord.getWebAccountUuid())){
						if(webAccount.getParentorgid().equals(webOrg.getOrgId())){
							if(webKeyWord.getTarget()==1){
								summonNumber++;
							}else if(webKeyWord.getTarget()==2){
								museNumber++;
							}else{
								ermNumber++;
							}
						}
					}
				}
			}
			titleNumber=summonNumber+museNumber+ermNumber;
			if(titleNumber!=0){
				selUseNumTemp.setParentOrgName(webOrg.getOrgName());
				selUseNumTemp.setParentSort(webOrg.getSort());
				selUseNumTemp.setTitleNumber(titleNumber);
				selUseNumTemp.setSummonNumber(summonNumber);
				selUseNumTemp.setMuseNumber(museNumber);
				selUseNumTemp.setErmNumber(ermNumber);
				selNumberListRet.add(selUseNumTemp);
			}
		}
		return selNumberListRet;
	}
}
