package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmCodeDbDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebEmployee;

@Service
public class ErmCodeDbService {
	@Autowired
	private IErmCodeDbDAO codeDbDAO;

	public List<ErmCodeDb> findPublisherList(String openValue, String openType,
			String name, String code, WebEmployee webEmployee) {
		return codeDbDAO.findPublisherList(openValue, openType, name, code,
				webEmployee);
	}
	public ErmCodeDb findcodeDbByDbId(String dbId,WebEmployee webEmployee) {
		List<ErmCodeDb> list=codeDbDAO.findcodeDbByDbId(dbId, webEmployee);
		ErmCodeDb ermCodeDb=new ErmCodeDb();
		if(list.size()>0){
			ermCodeDb=list.get(0);
		}
		return ermCodeDb;
	}
	public List<ErmCodeDb> findAllCodeDb(WebEmployee webEmployee) {
		return codeDbDAO.findAllCodeDb(webEmployee);
	}
	public void saveCodeDb(ErmCodeDb ermCodeDb){
		codeDbDAO.merge(ermCodeDb);
	}
	public void delCodeDb(ErmCodeDb ermCodeDb){
		ermCodeDb.setIsDataEffid(0);
		codeDbDAO.merge(ermCodeDb);
		codeDbDAO.updateErmEjebItemByDbId(ermCodeDb.getDbId());
	}
	public List<ErmCodeDb> findAllCodeDbIsUse() {
		return codeDbDAO.findAllCodeDbIsUse();
	}
}
