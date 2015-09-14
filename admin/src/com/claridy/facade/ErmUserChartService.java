package com.claridy.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IErmUserChartDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmPersonalizeRescount;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Service
public class ErmUserChartService {
	@Autowired
	private IErmUserChartDAO ErmUserChartDAO;

	public List<ErmPersonalizeRescount> findUserResChart(String tempstartDateDbx, String tempendDateDbx, int chartNumber, String resType) {
		List<String[]> dataList = sort(tempstartDateDbx, tempendDateDbx, resType);
		List<String[]> newdataList = new ArrayList<String[]>();
		for (int i = 0; dataList.size() > 1;) {
			String[] oldData = dataList.get(i);
			for (int j = i + 1; j < dataList.size(); j++) {
				String[] newData = dataList.get(j);
				int oldCount = Integer.valueOf(oldData[4]);
				int newCount = Integer.valueOf(newData[4]);
				if (newCount > oldCount) {
					oldData = newData;
				}
			}
			newdataList.add(oldData);
			dataList.remove(oldData);
		}
		if (dataList.size() == 1) {
			String[] oldData = dataList.get(0);
			newdataList.add(oldData);
			dataList.remove(oldData);
		}
		for (int i = 0; i < newdataList.size(); i++) {
			String[] oldData = newdataList.get(i);
			dataList.add(oldData);
		}
		if (chartNumber < newdataList.size()) {
			String[] oldData = newdataList.get(chartNumber - 1);
			for (int k = chartNumber; k < dataList.size(); k++) {
				String[] newData = dataList.get(k);
				int oldCount = Integer.valueOf(oldData[4]);
				int newCount = Integer.valueOf(newData[4]);
				if (newCount == oldCount) {
				} else {
					newdataList.remove(newData);
				}
			}
		}
		List<ErmPersonalizeRescount> rescounts = new ArrayList<ErmPersonalizeRescount>(1);
		for (int i = 0; i < newdataList.size(); i++) {
			String[] strs = newdataList.get(i);
			ErmPersonalizeRescount rescount = new ErmPersonalizeRescount();
			rescount.setShows1(strs[0]);
			rescount.setShows2(strs[1]);
			rescount.setShows3(strs[2]);
			rescount.setShows4(strs[3]);
			rescount.setShow5(Integer.parseInt(strs[4]));
			rescounts.add(rescount);
		}
		return rescounts;
	}

	public List<String[]> sort(String tempstartDateDbx, String tempendDateDbx, String resType) {
		List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
		List<Object> userResChartList = null;
		if (resType == "0") {
			userResChartList = ErmUserChartDAO.findAccountIdAll(tempstartDateDbx, tempendDateDbx);
		} else {
			userResChartList = ErmUserChartDAO.findAccountIdResType(tempstartDateDbx, tempendDateDbx, resType);
		}
		for (int i = 0; i < userResChartList.size(); i++) {
			Object[] accountIdget = (Object[]) userResChartList.get(i);
			String accountId = String.valueOf(accountIdget[0]);
			String[] countNumber = new String[5];
			List<Object> ermUserChartLst = null;
			if (resType == "0") {
				ermUserChartLst = ErmUserChartDAO.findErmResUser(tempstartDateDbx, tempendDateDbx, accountId);
			} else {
				ermUserChartLst = ErmUserChartDAO.findErmResUserResType(tempstartDateDbx, tempendDateDbx, accountId, resType);
			}
			Object[] obj = (Object[]) ermUserChartLst.get(0);
			String resId = (String) obj[1];
			countNumber[0] = (String) obj[0];
			WebAccount webAccount = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findAccountName((String) obj[0]);
			countNumber[1] = webAccount.getNameZhTw();
			WebOrg webOrg = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgIdParent((String) obj[4]);
			countNumber[2] = webOrg.getOrgName();
			webOrg = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgName((String) obj[5]);
			if (webOrg != null && !"".equals(webOrg)) {
				countNumber[3] = webOrg.getOrgName();
			} else {
				countNumber[3] = "";
			}
			countNumber[4] = String.valueOf(ermUserChartLst.size());
			dataList.add(countNumber);
		}
		return dataList;
	}

	public WebOrg findOrgName(String orgid) {
		return ErmUserChartDAO.findOrgName(orgid);
	}

	public WebOrg findOrgIdParent(String orgIdParent) {
		return ErmUserChartDAO.findOrgIdParent(orgIdParent);
	}

	public WebAccount findAccountName(String accountid) {
		return ErmUserChartDAO.findAccountName(accountid);
	}

	public ErmCodeDb findDb(String dbId) {
		return ErmUserChartDAO.findDb(dbId);
	}

	public List<Object> findResName(String resourcesId, String dbId) {
		return ErmUserChartDAO.findResName(resourcesId, dbId);
	}

	public List<Object> findAccountIdAll() {
		return ErmUserChartDAO.findAccountIdAll();
	}
}
