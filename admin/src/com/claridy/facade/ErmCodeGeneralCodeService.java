package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmCodeItemDao;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;
@Service
public class ErmCodeGeneralCodeService {
	@Autowired
	public IErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;
	
	@Autowired
	public IErmCodeItemDao ErmCodeItemDao;

	public List<ErmCodeGeneralCode> findAll(WebEmployee webEmployee) {
		return ermCodeGeneralCodeDAO.findAll(webEmployee);
	}
	public List<ErmCodeGeneralCode> search(String itemId,String generalcodeId,String name1,String name2,String yesOrNo,WebEmployee webEmployee){
		return 	ermCodeGeneralCodeDAO.search(itemId,generalcodeId,name1,name2,yesOrNo,webEmployee);	
	}
	public ErmCodeGeneralCode findByItemIDAndGeneralcodeId(String itemId,String generalcodeId){
		return ermCodeGeneralCodeDAO.findByItemIDAndGeneralcodeId(itemId,generalcodeId);
		
	}
	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeByItemId(String itemId) {
		return ermCodeGeneralCodeDAO.findErmCodeGeneralCodeByItemId(itemId);
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
	public List<ErmCodeGeneralCode> findGeneralList(String openValue,String openType,String name,String code,WebEmployee webEmployee){
		return 	ermCodeGeneralCodeDAO.findGeneralList(openValue,openType,name,code,webEmployee);	
	}
	public boolean save(String itemId,String generalcode_Id,String name1,String name2,String history,String note,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
		ermCodeGeneralCode =  ermCodeGeneralCodeDAO.findByItemIDAndGeneralcodeId(itemId, generalcode_Id);
		if (ermCodeGeneralCode.getGeneralcodeId()==null) {
			ErmCodeGeneralCode codeRet = new ErmCodeGeneralCode();
			ErmCodeItem item=new ErmCodeItem();
			item=ErmCodeItemDao.findByItemId(itemId);
			codeRet.setErmCodeItem(item);
			codeRet.setGeneralcodeId(generalcode_Id);
			codeRet.setName1(name1);
			codeRet.setName2(name2);
			codeRet.setHistory(history);
			codeRet.setNote(note);
			codeRet.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				codeRet.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				codeRet.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			codeRet.setCreateDate(new Date());
			codeRet.setIsDataEffid(1);
			ermCodeGeneralCodeDAO.saveOrUpdate(codeRet);
			saveStatus = true;
		}
		
		return saveStatus;
	}
	public boolean update(String itemId,String generalcode_Id,String name1,String name2,String history,String note,WebEmployee webEmployee) {
		boolean saveStatus = false;
		ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
		ermCodeGeneralCode = findByItemIDAndGeneralcodeId(itemId, generalcode_Id);
		if (ermCodeGeneralCode.getGeneralcodeId()!=null) {
			ErmCodeItem item=new ErmCodeItem();
			item=ErmCodeItemDao.findByItemId(itemId);
			ermCodeGeneralCode.setErmCodeItem(item);
			ermCodeGeneralCode.setGeneralcodeId(generalcode_Id);
			ermCodeGeneralCode.setName1(name1);
			ermCodeGeneralCode.setName2(name2);
			ermCodeGeneralCode.setHistory(history);
			ermCodeGeneralCode.setNote(note);
			ermCodeGeneralCodeDAO.merge(ermCodeGeneralCode);
			saveStatus = true;
		}

		return saveStatus;
	}
	public void delete(String itemId,String generalcodeId) {
		ErmCodeGeneralCode tmpermCodeGeneralCode = ermCodeGeneralCodeDAO.findByItemIDAndGeneralcodeId(itemId,generalcodeId);
		tmpermCodeGeneralCode.setIsDataEffid(0);
		ermCodeGeneralCodeDAO.merge(tmpermCodeGeneralCode);
	}
}
