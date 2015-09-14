package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IERMCodePublisherDAO;
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;

@Service
public class CodePublisherService {
	@Autowired
	public IERMCodePublisherDAO ermCodePublisherDAO;
	
	@Autowired
	public IErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;
	

	public List<ErmCodePublisher> findAll(WebEmployee webEmployee) {
		return ermCodePublisherDAO.findAll(webEmployee);
	}
	/**
	 * 根據條件查詢集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<ErmCodePublisher> findPublisherList(String openValue,String openType,String name,String code,WebEmployee webEmployee){
		return 	ermCodePublisherDAO.findPublisherList(openValue,openType,name,code,webEmployee);	
	}
	public List<ErmCodePublisher> search(String publisherId,String name,WebEmployee webEmployee) {
		return ermCodePublisherDAO.search(publisherId,name,webEmployee);
	}

	public ErmCodePublisher findByPublisherID(String publisherId) {
		return ermCodePublisherDAO.findByPublisherId(publisherId);
	}

	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeByItemId(String itemId) {
		return ermCodeGeneralCodeDAO.findErmCodeGeneralCodeByItemId(itemId);
	}
	
	public boolean save(String publisherId, String name, String url,String country, 
			String zip, String city,String address, String contact, 
			String note,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodePublisher checkERMCodePublisher = new ErmCodePublisher();
		checkERMCodePublisher = findByPublisherID(publisherId);
		if (null == checkERMCodePublisher) {
			ErmCodePublisher tmpERMCodePublisher = new ErmCodePublisher();
			tmpERMCodePublisher.setPublisherId(publisherId);
			tmpERMCodePublisher.setName(name);
			tmpERMCodePublisher.setUrl(url);
			tmpERMCodePublisher.setCountry(country);
			tmpERMCodePublisher.setZip(zip);
			tmpERMCodePublisher.setCity(city);
			tmpERMCodePublisher.setAddress(address);
			tmpERMCodePublisher.setContact(contact);
			tmpERMCodePublisher.setNote(note);
			tmpERMCodePublisher.setWebEmployee(webEmployee);
			if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
				tmpERMCodePublisher.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			}else{
				tmpERMCodePublisher.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			tmpERMCodePublisher.setCreateDate(new Date());
			tmpERMCodePublisher.setIsDataEffid(1);
//			tmpERMCodePublisher.setIsDefault("N");
			ermCodePublisherDAO.saveOrUpdate(tmpERMCodePublisher);
			saveStatus = true;
		}
		
		return saveStatus;
	}

	public boolean update(String publisherId, String name, String url,String country, 
			String zip, String city,String address, String contact, 
			String note,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodePublisher checkERMCodePublisher = new ErmCodePublisher();
		checkERMCodePublisher = findByPublisherID(publisherId);
		if (null != checkERMCodePublisher) {
			checkERMCodePublisher.setPublisherId(publisherId);
			checkERMCodePublisher.setName(name);
			checkERMCodePublisher.setUrl(url);
			checkERMCodePublisher.setCountry(country);
			checkERMCodePublisher.setZip(zip);
			checkERMCodePublisher.setCity(city);
			checkERMCodePublisher.setAddress(address);
			checkERMCodePublisher.setContact(contact);
			checkERMCodePublisher.setLatelyChangedUser(webEmployee.getEmployeesn());
			checkERMCodePublisher.setNote(note);
			checkERMCodePublisher.setLatelyChangedDate(new Date());
			ermCodePublisherDAO.merge(checkERMCodePublisher);
			saveStatus = true;
		}
		
		return saveStatus;
	}

//	public void deleteCodePublisher(ErmCodePublisher ermCodePublisher) {
//		ermCodePublisherDAO.delete(ermCodePublisher);
//	}
	
	public void deleteCodePublisher(String publisherId) {
		ErmCodePublisher tmpErmCodePublisher = ermCodePublisherDAO.findByPublisherId(publisherId);
		tmpErmCodePublisher.setIsDataEffid(0);
		ermCodePublisherDAO.merge(tmpErmCodePublisher);
	}

}
