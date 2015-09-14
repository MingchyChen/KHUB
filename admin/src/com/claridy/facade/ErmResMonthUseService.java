package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IErmResMonthUseDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Service
public class ErmResMonthUseService {
	@Autowired
	private IErmResMonthUseDAO ErmResMonthUseDAO;

	public String[] findErmResourcesRsconAll(String tempstartDateDbx, String tempendDateDbx, String[] webOrgName, String resId, String[] dataBase) {
		int count = 0;
		String resName = "";
		String dbId = "";
		List<Object> ermResUnitUseList = null;
		if (dataBase == null) {
			ermResUnitUseList = ErmResMonthUseDAO.findErmResUnitList(tempstartDateDbx, tempendDateDbx, resId);
			for (int i = 0; i < ermResUnitUseList.size(); i++) {
				Object[] obj = (Object[]) ermResUnitUseList.get(i);
				WebOrg webOrg = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findOrgName((String) obj[4]);
				ErmResourcesMainfileV ermResourcesMainfileV = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService"))
						.findResName((String) obj[1]);
				for (int j = 0; j < webOrgName.length; j++) {
					if (webOrg != null && !"".equals(webOrg) && !"".equals(webOrg.getOrgId())) {
						if (webOrgName[j].equals(webOrg.getOrgName())) {
							count = count + 1;
						}
					}
				}
				resName = ermResourcesMainfileV.getTitle();
			}
		} else {
			for (int k = 0; k < dataBase.length; k++) {
				String dbName = dataBase[k];
				ErmCodeDb ermCodeDb = ErmResMonthUseDAO.findDbId(dbName);
				dbId = ermCodeDb.getDbId();
				ermResUnitUseList = ErmResMonthUseDAO.findErmResUnitList(tempstartDateDbx, tempendDateDbx, resId);
				for (int i = 0; i < ermResUnitUseList.size(); i++) {
					Object[] obj = (Object[]) ermResUnitUseList.get(i);
					WebOrg webOrg = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findOrgName((String) obj[4]);
					ErmResourcesMainfileV ermResourcesMainfileV = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService"))
							.findResName((String) obj[1]);
					for (int j = 0; j < webOrgName.length; j++) {
						if (webOrg != null && !"".equals(webOrg) && !"".equals(webOrg.getOrgId())) {
							if (webOrgName[j].equals(webOrg.getOrgName()) && obj[3].equals(dbId)) {
								count = count + 1;
							}
						}
					}
					resName = ermResourcesMainfileV.getTitle();
				}
			}
		}
		String[] data = new String[8];
		data[0] = String.valueOf(count);
		data[1] = null;
		data[3] = resId;
		data[4] = resName;
		data[5] = tempstartDateDbx;
		data[6] = tempendDateDbx;
		return data;
	}

	public String[] findResIdAll(String tempstartDateDbx, String tempendDateDbx, String resName) {

		List<Object> allResId = ErmResMonthUseDAO.findResIdAll(tempstartDateDbx, tempendDateDbx);
		String[] allResIded = new String[allResId.size()];
		int k = 0;
		for (int i = 0; i < allResId.size(); i++) {
			if ("".equals(resName)) {
				Object[] resId = (Object[]) allResId.get(i);
				allResIded[i] = String.valueOf(resId[0]);
			} else {
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

	public WebOrg findOrgName(String orgid) {
		return ErmResMonthUseDAO.findOrgName(orgid);
	}

	public WebOrg findOrgIdParent(String orgIdParent) {
		return ErmResMonthUseDAO.findOrgIdParent(orgIdParent);
	}

	public WebAccount findAccountName(String accountid) {
		return ErmResMonthUseDAO.findAccountName(accountid);
	}

	public ErmCodeDb findDb(String dbId) {
		return ErmResMonthUseDAO.findDb(dbId);
	}

	public ErmCodeDb findDbId(String dbName) {
		return ErmResMonthUseDAO.findDbId(dbName);
	}

	public ErmResourcesMainfileV findResName(String resourcesId) {
		return ErmResMonthUseDAO.findResName(resourcesId);
	}
}
