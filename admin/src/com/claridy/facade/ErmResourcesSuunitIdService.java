package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResourcesSuunitIdDAO;
import com.claridy.domain.ErmResourcesSuunit;

@Service
public class ErmResourcesSuunitIdService {
	@Autowired
	private IErmResourcesSuunitIdDAO suunitDAO;
	public void addSuunit(ErmResourcesSuunit ermResourcesSuunit){
		suunitDAO.saveOrUpdate(ermResourcesSuunit);
	}
	public List<ErmResourcesSuunit> findBySuunitResId(String resourcesId){
		return suunitDAO.getDomain(resourcesId);
	}
	public void delSunnit(ErmResourcesSuunit ermResourcesSuunit){
		suunitDAO.delete(ermResourcesSuunit);
	}
}
