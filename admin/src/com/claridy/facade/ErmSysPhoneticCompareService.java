package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IermSysPhoneticCompareDAO;
import com.claridy.domain.ErmSysPhoneticCompare;
import com.claridy.domain.WebEmployee;

@Service
public class ErmSysPhoneticCompareService {

	@Autowired
	private IermSysPhoneticCompareDAO ermSysTicDao;
	
	public List<ErmSysPhoneticCompare> findByCn(ErmSysPhoneticCompare ermSys,WebEmployee webEmployee){
		return ermSysTicDao.findErmSys(ermSys,webEmployee);
	}
	
	public void delete(ErmSysPhoneticCompare ermSys){
		ermSysTicDao.merge(ermSys);
	}
	
	public void edit(ErmSysPhoneticCompare ermSys){
		ermSysTicDao.merge(ermSys);
	}
	
	public void save(ErmSysPhoneticCompare ermSys){
		ermSysTicDao.saveOrUpdate(ermSys);
	}
}
