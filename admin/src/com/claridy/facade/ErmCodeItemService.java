package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeItemDao;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;

@Service
public class ErmCodeItemService {

	@Autowired
	protected IErmCodeItemDao rmcodeitemdao;

	public List<ErmCodeItem> findErmCodeItem(ErmCodeItem ermCodeItem,
			WebEmployee webEmployee) {
		return rmcodeitemdao.findErmCodeItem(ermCodeItem, webEmployee);
	}

	public ErmCodeItem findByItemId(String itemId) {
		return rmcodeitemdao.findByItemId(itemId);
	}

	public List<ErmCodeItem> findByhistory(WebEmployee webEmployee) {
		return rmcodeitemdao.findByhistory(webEmployee);
	}

	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeItemId(String itemId,
			WebEmployee webEmployee) {
		return rmcodeitemdao.findErmCodeGeneralCodeItemId(itemId, webEmployee);
	}

	public boolean save(String itemId, String name1, String name2,
			String history, WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodeItem checkErmCodeItem = new ErmCodeItem();
		checkErmCodeItem = findByItemId(itemId);
		if (null == checkErmCodeItem) {
			ErmCodeItem eci = new ErmCodeItem();
			eci.setIsDataEffid(1);
			eci.setItemId(itemId);
			eci.setName1(name1);
			eci.setName2(name2);
			eci.setHistory(history);
			eci.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				eci.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				eci.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			eci.setCreateDate(new Date());
			rmcodeitemdao.saveOrUpdate(eci);
			saveStatus = true;
		}

		return saveStatus;
	}

	public boolean update(String itemId, String name1, String name2,
			String history, WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodeItem checkErmCodeItem = new ErmCodeItem();
		checkErmCodeItem = findByItemId(itemId);
		if (null != checkErmCodeItem) {
			checkErmCodeItem.setItemId(itemId);
			checkErmCodeItem.setName1(name1);
			checkErmCodeItem.setName2(name2);
			checkErmCodeItem.setHistory(history);
			checkErmCodeItem.setLatelyChangedUser(webEmployee.getEmployeesn());
			checkErmCodeItem.setLatelyChangedDate(new Date());
			rmcodeitemdao.merge(checkErmCodeItem);
			saveStatus = true;
		}

		return saveStatus;
	}

	public void delete(String itemId) {
		ErmCodeItem tmpErmCodeItem = rmcodeitemdao.findByItemId(itemId);
		tmpErmCodeItem.setIsDataEffid(0);
		rmcodeitemdao.deleteItems(itemId);
		rmcodeitemdao.merge(tmpErmCodeItem);
	}

}
