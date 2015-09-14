package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebNoticeTemplatesDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

@Service
public class WebAccountService {

	@Autowired
	private IWebAccountDAO webAccountDao;
	@Autowired
	public IWebNoticeTemplatesDAO webnoticetempdao;
	@Autowired
	private IWebOrgDAO webOrgDao;
	
	public List<WebAccount> findWebAccount(WebAccount web,int isregister,int isdataeffid,WebEmployee webEmployee){
		
		return webAccountDao.findWebAccount(web,isregister, isdataeffid,webEmployee);
	}
	
	public List<WebAccount> findAccount(WebAccount web,WebEmployee webEmployee){
		return webAccountDao.findAccount(web,webEmployee);
	}
	
	public WebAccount getAccount(String accountId){
		return webAccountDao.getAccount(accountId);
	}
	
	public WebAccount findWebAccountIsNull(WebAccount web,int isdataeffid,WebEmployee webEmployee){
		return webAccountDao.findWebAccount(web,0, isdataeffid,webEmployee).get(0);
	}
	public List<WebOrg> findOrg(String parentId){
		List<WebOrg> weblist;
		
		weblist=webOrgDao.findOrg(parentId);
		return weblist;
	}
	public List<WebOrg> findParentOrg(String orgId){
		List<WebOrg> weblist;
		weblist=webOrgDao.findParentOrg(orgId);
		
		return weblist;
	}
	
	public void eidtWebAccount(WebAccount webAccount){
		webAccountDao.merge(webAccount);
		return;
	}
	
	public void addWebAccount(WebAccount webAccount){
		webAccountDao.saveOrUpdate(webAccount);
	}
	
	
	public void deleteWebAccount(WebAccount webAccount){
		
		webAccountDao.merge(webAccount);
	}
	public void update(WebAccount webAccount){
		webAccountDao.merge(webAccount);
	}
	
	
	public WebOrg findById(String uuid){
		return webOrgDao.findById(uuid);
	}
	
}
