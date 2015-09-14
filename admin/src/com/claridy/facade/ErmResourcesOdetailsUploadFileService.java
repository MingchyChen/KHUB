package com.claridy.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmResourcesOdetailsUploadFileDAO;
import com.claridy.domain.ErmResourcesOdetailsUploadfile;

@Service
public class ErmResourcesOdetailsUploadFileService {
	@Autowired
	private IErmResourcesOdetailsUploadFileDAO ermResourcesOdetailsUploadFileDAO;

	public void addErmResourceOderUploadFile(
			ErmResourcesOdetailsUploadfile ermResourcesOdetailsUploadfile) {
		ermResourcesOdetailsUploadFileDAO.saveOrUpdate(ermResourcesOdetailsUploadfile);
	}
	public void editErmResourceOderUploadFile(
			ErmResourcesOdetailsUploadfile ermResourcesOdetailsUploadfile) {
		ermResourcesOdetailsUploadFileDAO.merge(ermResourcesOdetailsUploadfile);
	}
	public ErmResourcesOdetailsUploadfile getUpLoadFileByResIdAndYear(String resourcesId,String year){
		return ermResourcesOdetailsUploadFileDAO.getUpLoadFileByResIdAndYear(resourcesId, year);
	}
	public void delErmResourceOderUploadFile(
			ErmResourcesOdetailsUploadfile ermResourcesOdetailsUploadfile) {
		ermResourcesOdetailsUploadFileDAO.delete(ermResourcesOdetailsUploadfile);
	}
}
