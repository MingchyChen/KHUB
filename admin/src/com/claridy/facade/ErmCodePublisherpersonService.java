package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodePublisherpersonDao;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;

@Service
public class ErmCodePublisherpersonService {

	@Autowired
	private IErmCodePublisherpersonDao ermCodePublisherpersonDao;

	public List<ErmCodePublisherPerson> findAll(WebEmployee webEmployee) {
		return ermCodePublisherpersonDao.findAll(webEmployee);
	}

	public List<ErmCodePublisherPerson> findErmCodePublisherPerson(
			String publisheId, String name, WebEmployee webEmployee) {
		return ermCodePublisherpersonDao.findErmCodePublisherPerson(publisheId,
				name, webEmployee);
	}

	public ErmCodePublisherPerson findErmCodePublisherPersonById(
			String publisheId, String name, WebEmployee webEmployee) {
		return ermCodePublisherpersonDao.findErmCodePublisherPersonById(
				publisheId, name, webEmployee);
	}
	
	public boolean save(String publisheId, String name, String email,
			String phone, String fax, String title, WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodePublisherPerson ermCodePublisherPerson = new ErmCodePublisherPerson();
		ermCodePublisherPerson = findErmCodePublisherPersonById(publisheId, name, webEmployee);
		if (null == ermCodePublisherPerson) {
			ErmCodePublisherPerson eci = new ErmCodePublisherPerson();
			eci.setIsDataEffid(1);
			eci.setPublisherId(publisheId);
			eci.setPersonName(name);
			eci.setMail(email);
			eci.setTelephone(phone);
			eci.setFax(fax);
			eci.setTitle(title);
			eci.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				eci.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				eci.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			eci.setCreateDate(new Date());
			ermCodePublisherpersonDao.saveOrUpdate(eci);
			saveStatus = true;
		}

		return saveStatus;
	}
	
	public boolean update(String publisheId, String name, String email,
			String phone, String fax, String title,int isDataEffid, WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodePublisherPerson ermCodePublisherPerson = new ErmCodePublisherPerson();
		ermCodePublisherPerson = findErmCodePublisherPersonById(publisheId, name, webEmployee);
		if (null != ermCodePublisherPerson) {
			ermCodePublisherPerson.setPublisherId(publisheId);
			ermCodePublisherPerson.setPersonName(name);
			ermCodePublisherPerson.setMail(email);
			ermCodePublisherPerson.setTelephone(phone);
			ermCodePublisherPerson.setFax(fax);
			ermCodePublisherPerson.setTitle(title);
			ermCodePublisherPerson.setIsDataEffid(isDataEffid);
			ermCodePublisherPerson.setLatelyChangedUser(webEmployee.getEmployeesn());
			ermCodePublisherPerson.setLatelyChangedDate(new Date());
			ermCodePublisherpersonDao.merge(ermCodePublisherPerson);
			saveStatus = true;
		}

		return saveStatus;
	}


}
