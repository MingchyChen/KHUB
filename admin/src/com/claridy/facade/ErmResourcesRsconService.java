package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResouresRsconDAO;
import com.claridy.domain.ErmResourcesRscon;

@Service
public class ErmResourcesRsconService {
	
	@Autowired
	private IErmResouresRsconDAO ErmResourcesRsconDAO;
	
	public List<ErmResourcesRscon> findAll(){
		return ErmResourcesRsconDAO.findbyDate(null,null, null);
	}
	
	public List<ErmResourcesRscon> findById(Date sDate,Date eDate,String status){
		return ErmResourcesRsconDAO.findbyDate(sDate,eDate, status);
	}

}
