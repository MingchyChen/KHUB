package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResUserListDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Service
public class ErmResUserListService {
	@Autowired
	private IErmResUserListDAO ErmResUserListDAO;

	public List<Object> findErmResourcesRsconAll(String tempstartDateDbx, String tempendDateDbx, String tempSortType, String tempSort) {
		return ErmResUserListDAO.findErmResourcesRsconAll(tempstartDateDbx, tempendDateDbx, tempSortType, tempSort);
	}

	public WebOrg findOrgName(String orgid) {
		return ErmResUserListDAO.findOrgName(orgid);
	}

	public WebOrg findOrgIdParent(String orgIdParent) {
		return ErmResUserListDAO.findOrgIdParent(orgIdParent);
	}

	public WebAccount findAccountName(String accountid) {
		return ErmResUserListDAO.findAccountName(accountid);
	}

	public ErmCodeDb findDb(String dbId) {
		return ErmResUserListDAO.findDb(dbId);
	}

	public List<Object> findResName(String resourcesId, String dbId) {
		return ErmResUserListDAO.findResName(resourcesId, dbId);
	}

	public List<WebOrg> findedOrgName(String orgName) {
		return ErmResUserListDAO.findedOrgName(orgName);
	}
}
