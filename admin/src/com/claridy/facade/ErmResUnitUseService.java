package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IErmResUnitUseDAO;
import com.claridy.dao.IErmResUserListDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Service
public class ErmResUnitUseService {
	@Autowired
	private IErmResUserListDAO ErmResUserListDAO;
	@Autowired
	private IErmResUnitUseDAO ErmResUnitUseDAO;
	int a = 0;

	public String[] findErmResourcesRsconAll(String tempstartDateDbx, String tempendDateDbx, String unitName, String resId, String[] dataBase,
			List<ErmResourcesMainfileV> ermResourcesMainfileVs, List<WebOrg> webOrgs) {
		int count = 0;
		String resName = "";
		String orgId = "";
		String dbId = "";
		List<Object> ermResUnitUseList = null;
		if (dataBase == null) {
			ermResUnitUseList = ErmResUnitUseDAO.findErmResUnitList(tempstartDateDbx, tempendDateDbx, resId);
			for (int i = 0; i < ermResUnitUseList.size(); i++) {
				Object[] obj = (Object[]) ermResUnitUseList.get(i);
				// WebOrg webOrg = ((ErmResUnitUseService)
				// SpringUtil.getBean("ermResUnitUseService")).findOrgName((String)
				// obj[4]);
				for (int j = 0; j < webOrgs.size(); j++) {
					if (webOrgs.get(j).getOrgId().equals(String.valueOf(obj[4]))) {
						if (webOrgs.get(j).getOrgName() != null) {
							if (webOrgs.get(j).getOrgName().equals(unitName)) {
								count = count + 1;
								orgId = webOrgs.get(j).getOrgId();
								break;
							}
						}
					}
				}
				ErmResourcesMainfileV ermResourcesMainfileV = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService"))
						.findResName((String) obj[1]);
				// for (int j = 0; j < ermResourcesMainfileVs.size(); j++) {
//				if (ermResourcesMainfileV.getResourcesId().equals(String.valueOf(obj[1]))) {
					resName = ermResourcesMainfileV.getTitle();
//					break;
					// }
//				}
				// if (webOrg.getOrgName() != null) {
				// if (webOrg.getOrgName().equals(unitName)) {
				// count = count + 1;
				// orgId = webOrg.getOrgId();
				// }
				// }
				// resName = ermResourcesMainfileV.getTitle();
			}
		} else {
			for (int k = 0; k < dataBase.length; k++) {
				String dbName = dataBase[k];
				ErmCodeDb ermCodeDb = ErmResUnitUseDAO.findDbId(dbName);
				dbId = ermCodeDb.getDbId();
				ermResUnitUseList = ErmResUnitUseDAO.findErmResUnitList(tempstartDateDbx, tempendDateDbx, resId);
				for (int i = 0; i < ermResUnitUseList.size(); i++) {
					Object[] obj = (Object[]) ermResUnitUseList.get(i);
					WebOrg webOrg = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findOrgName((String) obj[4]);
					ErmResourcesMainfileV ermResourcesMainfileV = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService"))
							.findResName((String) obj[1]);
					if (unitName.equals(webOrg.getOrgName()) && obj[3].equals(dbId)) {
						count = count + 1;
						orgId = webOrg.getOrgId();
					}
					resName = ermResourcesMainfileV.getTitle();
				}
			}
		}
		String[] data = new String[8];
		data[0] = String.valueOf(count);
		data[1] = unitName;
		data[2] = orgId;
		data[3] = resId;
		data[4] = resName;
		return data;
	}

	public String[] findResIdAll(String tempstartDateDbx, String tempendDateDbx, String resName) {
		List<Object> allResId = ErmResUnitUseDAO.findResIdAll(tempstartDateDbx, tempendDateDbx);
		String[] allResIded = new String[allResId.size()];
		int k = 0;
		for (int i = 0; i < allResId.size(); i++) {
			if ("".equals(resName)) {
				Object[] resId = (Object[]) allResId.get(i);
				allResIded[i] = String.valueOf(resId[0]);
			} else {
				String[] searchResIded = new String[1];
				Object[] resId = (Object[]) allResId.get(i);
				String resourseId = String.valueOf(resId[0]);
				ErmResourcesMainfileV ermResourcesMainfileV = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService"))
						.findResName(resourseId);
				if (ermResourcesMainfileV.getTitle() != null) {
					if (ermResourcesMainfileV.getTitle().toLowerCase().indexOf(resName.toLowerCase()) != -1) {
						allResIded[k] = resourseId;
						k = k + 1;
					}
				}
			}
		}
		return allResIded;
	}

	public List<Object> findErmResUser(String tempstartDateDbx, String tempendDateDbx) {
		return ErmResUserListDAO.findErmResUser(tempstartDateDbx, tempendDateDbx);
	}

	public List<Object> findMonthErmResUser(String tempstartDateDbx, String tempendDateDbx, String resId) {
		return ErmResUserListDAO.findMonthErmResUser(tempstartDateDbx, tempendDateDbx, resId);
	}

	public WebOrg findOrgName(String orgid) {
		return ErmResUnitUseDAO.findOrgName(orgid);
	}

	public List<WebOrg> findedOrgName(String orgName) {
		return ErmResUserListDAO.findedOrgName(orgName);
	}

	public WebOrg findOrgIdParent(String orgIdParent) {
		return ErmResUnitUseDAO.findOrgIdParent(orgIdParent);
	}

	public List<ErmCodeDb> findedDb(String db) {
		return ErmResUserListDAO.findedDb(db);
	}

	public List<Object> findAllDataBase() {
		return ErmResUserListDAO.findAllDataBase();
	}

	public List<Object> findAllOrgIdParent() {
		return ErmResUserListDAO.findAllOrgIdParent();
	}

	public WebAccount findAccountName(String accountid) {
		return ErmResUnitUseDAO.findAccountName(accountid);
	}

	public ErmCodeDb findDb(String dbId) {
		return ErmResUnitUseDAO.findDb(dbId);
	}

	public ErmCodeDb findDbId(String dbName) {
		return ErmResUnitUseDAO.findDbId(dbName);
	}

	public ErmResourcesMainfileV findResName(String resourcesId) {
		return ErmResUnitUseDAO.findResName(resourcesId);
	}

	public List<ErmResourcesMainfileV> findResAll() {
		return ErmResUnitUseDAO.findResAll();
	}
}
