package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmSysScheduleDAO;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;

@Service
public class ErmSysScheduleService {
	
	@Autowired
	private IErmSysScheduleDAO cheduleDao;
	
	
	public List<ErmSysSchedule> findAll(WebEmployee webEmployee){
		return cheduleDao.findAll(webEmployee);
	}
	
	public ErmSysSchedule findById(String id,WebEmployee webEmployee){
		return cheduleDao.findById(id,webEmployee);
	}
	
	public void updateErmSysSchedule(ErmSysSchedule schedule){
		cheduleDao.merge(schedule);
	}
	
	public ErmSysSchedule findAllByStatus(String uuid){
		return cheduleDao.findAllByStatus(uuid);
	}
	
	public void ckrs_schedule(){
		System.out.println(1);
	}
	
	public void promo_schedule(){
		System.out.println(2);
	}
	
	public void mainfile_schedule(){
		System.out.println(3);
	}
}
