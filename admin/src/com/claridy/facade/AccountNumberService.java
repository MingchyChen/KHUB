package com.claridy.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebCooperationDAO;
import com.claridy.dao.IWebLogDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.AccountNumber;
import com.claridy.domain.LoginNumber;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebLog;
import com.claridy.domain.WebOrg;

@Service
public class AccountNumberService {
	@Autowired
	private IWebAccountDAO webAccountDao;

	@Autowired
	private IWebOrgDAO webOrgDao;

	@Autowired
	private IWebLogDAO webLogDao;

	@Autowired
	private IWebCooperationDAO webCooperationDao;

	/**
	 * 根據時間單位查詢帳號統計集合
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountNumber> findAccNumListByParentOrg(Date startDate, Date endDate, String parentOrgId) {
		List<AccountNumber> accNumberListRet = new ArrayList<AccountNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebOrg> parentOrgList = webOrgDao.findParentOrg(parentOrgId);
		List<WebLog> webLogList = webLogDao.findWebLogList(startDate, endDate, "webmemberlogin_");
		List<WebCooperation> webCoopertList = webCooperationDao.findWebCooperationList(startDate, endDate);
		for (int i = 0; i < parentOrgList.size(); i++) {
			WebOrg webOrg = parentOrgList.get(i);
			AccountNumber accNumberTemp = new AccountNumber();
			int loginNumber = 0;
			int cooperNumber = 0;
			for (int j = 0; j < webLogList.size(); j++) {
				WebLog webLog = webLogList.get(j);
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
						if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
							loginNumber++;
						}
					}
				}
			}
			for (int j = 0; j < webCoopertList.size(); j++) {
				WebCooperation webCooperation = webCoopertList.get(j);
				WebAccount webAccount = webCooperation.getApplyAccount();
				if (webAccount.getParentorgid().equals(webOrg.getOrgId())) {
					cooperNumber++;
				}
			}
			accNumberTemp.setParentOrgName(webOrg.getOrgName());
			accNumberTemp.setpUrl(webOrg.getOrgId());
			accNumberTemp.setLoginNumber(loginNumber);
			accNumberTemp.setCooperNumber(cooperNumber);
			accNumberListRet.add(accNumberTemp);
		}
		return accNumberListRet;
	}

	/**
	 * 根據時間單位查詢個組室帳號統計集合
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountNumber> findAccNumListByOrg(Date startDate, Date endDate, String parentOrgId) {
		List<AccountNumber> accNumberListRet = new ArrayList<AccountNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebOrg> orgList = webOrgDao.findOrg(parentOrgId);
		List<WebLog> webLogList = webLogDao.findWebLogList(startDate, endDate, "webmemberlogin_");
		List<WebCooperation> webCoopertList = webCooperationDao.findWebCooperationList(startDate, endDate);
		for (int i = 0; i < orgList.size(); i++) {
			WebOrg webOrg = orgList.get(i);
			AccountNumber accNumberTemp = new AccountNumber();
			int loginNumber = 0;
			int cooperNumber = 0;
			for (int j = 0; j < webLogList.size(); j++) {
				WebLog webLog = webLogList.get(j);
				for (int k = 0; k < webAccountList.size(); k++) {
					WebAccount webAccount = webAccountList.get(k);
					if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
						if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
								&& webAccount.getParentorgid().equals(parentOrgId)) {
							loginNumber++;
						}
					}
				}
			}
			for (int j = 0; j < webCoopertList.size(); j++) {
				WebCooperation webCooperation = webCoopertList.get(j);
				WebAccount webAccount = webCooperation.getApplyAccount();
				if (webAccount.getOrgid() != null && !"".equals(webAccount.getOrgid()) && webAccount.getOrgid().equals(webOrg.getOrgId())
						&& webAccount.getParentorgid().equals(parentOrgId)) {
					cooperNumber++;
				}
			}
			accNumberTemp.setParentOrgName(webOrg.getOrgName());
			accNumberTemp.setpUrl(webOrg.getOrgId());
			accNumberTemp.setLoginNumber(loginNumber);
			accNumberTemp.setCooperNumber(cooperNumber);
			accNumberListRet.add(accNumberTemp);
		}

		AccountNumber OaccNumberTemp = new AccountNumber();
		int OloginNumber = 0;
		int OcooperNumber = 0;
		for (int j = 0; j < webLogList.size(); j++) {
			WebLog webLog = webLogList.get(j);
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
						OloginNumber++;
					}
				}
			}
		}
		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation = webCoopertList.get(j);
			WebAccount webAccount = webCooperation.getApplyAccount();
			if (webAccount.getParentorgid().equals(parentOrgId) && ("".equals(webAccount.getOrgid()) || webAccount.getOrgid() == null)) {
				OcooperNumber++;
			}
		}
		// 其他
		OaccNumberTemp.setParentOrgName(Labels.getLabel("webEmployee.tboxIdType.other"));
		OaccNumberTemp.setpUrl("other" + parentOrgId);
		OaccNumberTemp.setLoginNumber(OloginNumber);
		OaccNumberTemp.setCooperNumber(OcooperNumber);
		accNumberListRet.add(OaccNumberTemp);

		int loginNumber = 0;
		int cooperNumber = 0;
		for (int i = 0; i < accNumberListRet.size(); i++) {
			AccountNumber accNumberTemp = accNumberListRet.get(i);
			loginNumber += accNumberTemp.getLoginNumber();
			cooperNumber += accNumberTemp.getCooperNumber();
		}
		AccountNumber accNumberTemp = new AccountNumber();
		// 小計
		accNumberTemp.setParentOrgName(Labels.getLabel("subTitle"));
		accNumberTemp.setpUrl(parentOrgId);
		accNumberTemp.setLoginNumber(loginNumber);
		accNumberTemp.setCooperNumber(cooperNumber);
		accNumberListRet.add(accNumberTemp);
		return accNumberListRet;
	}

	/**
	 * 根據時間單位查詢登入量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<LoginNumber> findLogNumListByParentOrg(Date startDate, Date endDate, String parentOrgId) {
		List<LoginNumber> loginNumberListRet = new ArrayList<LoginNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebLog> webLogList = webLogDao.findWebLogList(startDate, endDate, "webmemberlogin_");

		for (int j = 0; j < webLogList.size(); j++) {
			WebLog webLog = webLogList.get(j);
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
					if (webAccount.getParentorgid().equals(parentOrgId)) {
						LoginNumber loginNumberTemp = new LoginNumber();
						loginNumberTemp.setLoginDate(webLog.getNdate());
						loginNumberTemp.setAccountId(webAccount.getAccountId());
						loginNumberTemp.setAccountName(webAccount.getNameZhTw());
						loginNumberTemp.setLoginIp(webLog.getNip());
						loginNumberListRet.add(loginNumberTemp);
					}
				}
			}
		}
		return loginNumberListRet;
	}

	/**
	 * 根據時間單位组室为空查詢登入量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<LoginNumber> findLogNumListByParentOrgOther(Date startDate, Date endDate, String parentOrgId) {
		List<LoginNumber> loginNumberListRet = new ArrayList<LoginNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebLog> webLogList = webLogDao.findWebLogList(startDate, endDate, "webmemberlogin_");

		for (int j = 0; j < webLogList.size(); j++) {
			WebLog webLog = webLogList.get(j);
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
					if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
						LoginNumber loginNumberTemp = new LoginNumber();
						loginNumberTemp.setLoginDate(webLog.getNdate());
						loginNumberTemp.setAccountId(webAccount.getAccountId());
						loginNumberTemp.setAccountName(webAccount.getNameZhTw());
						loginNumberTemp.setLoginIp(webLog.getNip());
						loginNumberListRet.add(loginNumberTemp);
					}
				}
			}
		}
		return loginNumberListRet;
	}

	/**
	 * 根據時間組室查詢登入量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<LoginNumber> findLogNumListByOrg(Date startDate, Date endDate, String orgId) {
		List<LoginNumber> loginNumberListRet = new ArrayList<LoginNumber>();
		List<WebAccount> webAccountList = webAccountDao.findAccountList();
		List<WebLog> webLogList = webLogDao.findWebLogList(startDate, endDate, "webmemberlogin_");

		for (int j = 0; j < webLogList.size(); j++) {
			WebLog webLog = webLogList.get(j);
			for (int k = 0; k < webAccountList.size(); k++) {
				WebAccount webAccount = webAccountList.get(k);
				if (webAccount.getUuid().equals(webLog.getEmployeesn())) {
					if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
						LoginNumber loginNumberTemp = new LoginNumber();
						loginNumberTemp.setLoginDate(webLog.getNdate());
						loginNumberTemp.setAccountId(webAccount.getAccountId());
						loginNumberTemp.setAccountName(webAccount.getNameZhTw());
						loginNumberTemp.setLoginIp(webLog.getNip());
						loginNumberListRet.add(loginNumberTemp);
					}
				}
			}
		}
		return loginNumberListRet;
	}

	/**
	 * 根據時間單位查詢管合申請量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByParentOrg(Date startDate, Date endDate, String parentOrgId) {
		List<WebCooperation> cooperNumberListRet = new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList = webCooperationDao.findWebCooperationList(startDate, endDate);

		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation = webCoopertList.get(j);
			WebAccount webAccount = webCooperation.getApplyAccount();
			if (webAccount.getParentorgid().equals(parentOrgId)) {
				cooperNumberListRet.add(webCooperation);
			}
		}

		WebOrg webPraentOrg = null;
		WebOrg webOrg = null;
		for (int i = 0; i < cooperNumberListRet.size(); i++) {
			webPraentOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount()
					.getParentorgid());
			if (webPraentOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}

			webOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService"))
					.getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if (webOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}

	/**
	 * 根據時間單位查詢管合申請量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByParentOrgOther(Date startDate, Date endDate, String parentOrgId) {
		List<WebCooperation> cooperNumberListRet = new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList = webCooperationDao.findWebCooperationList(startDate, endDate);

		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation = webCoopertList.get(j);
			WebAccount webAccount = webCooperation.getApplyAccount();
			if (webAccount.getParentorgid().equals(parentOrgId) && (webAccount.getOrgid() == null || webAccount.getOrgid().equals(""))) {
				cooperNumberListRet.add(webCooperation);
			}
		}

		WebOrg webPraentOrg = null;
		WebOrg webOrg = null;
		for (int i = 0; i < cooperNumberListRet.size(); i++) {
			webPraentOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount()
					.getParentorgid());
			if (webPraentOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}

			webOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService"))
					.getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if (webOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}

	/**
	 * 根據時間組室查詢管合申請量詳細信息
	 * 
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<WebCooperation> findCooperNumListByOrg(Date startDate, Date endDate, String orgId) {
		List<WebCooperation> cooperNumberListRet = new ArrayList<WebCooperation>();
		List<WebCooperation> webCoopertList = webCooperationDao.findWebCooperationList(startDate, endDate);

		for (int j = 0; j < webCoopertList.size(); j++) {
			WebCooperation webCooperation = webCoopertList.get(j);
			WebAccount webAccount = webCooperation.getApplyAccount();
			if (webAccount.getOrgid() != null && webAccount.getOrgid().equals(orgId)) {
				cooperNumberListRet.add(webCooperation);
			}
		}

		WebOrg webPraentOrg = null;
		WebOrg webOrg = null;
		for (int i = 0; i < cooperNumberListRet.size(); i++) {
			webPraentOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService")).getOrgById(cooperNumberListRet.get(i).getApplyAccount()
					.getParentorgid());
			if (webPraentOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setParentOrgName("");
			}

			webOrg = ((WebOrgListService) SpringUtil.getBean("webOrgListService"))
					.getOrgById(cooperNumberListRet.get(i).getApplyAccount().getOrgid());
			if (webOrg != null) {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			} else {
				cooperNumberListRet.get(i).getApplyAccount().setOrgName("");
			}
		}
		return cooperNumberListRet;
	}
}
