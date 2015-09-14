package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IErmResourcesUploadfileDAO;
import com.claridy.domain.ErmResourcesUploadfile;

@Service
public class ErmResourcesUploadfileService {
	@Autowired
	private IErmResourcesUploadfileDAO ermResourcesUploadfileDAO;
	
	public void addUploadFile(ErmResourcesUploadfile ermResourcesUploadfile){
		ermResourcesUploadfileDAO.saveOrUpdate(ermResourcesUploadfile);
	}
	public List<ErmResourcesUploadfile> findByResourcesId(String resourceId){
		return ermResourcesUploadfileDAO.findByResourcesId(resourceId);
	}
	public void delUploadFile(ErmResourcesUploadfile ermResourcesUploadfile){
		ermResourcesUploadfileDAO.delete(ermResourcesUploadfile);
	}
}
