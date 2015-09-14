package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesOdetailsUploadfile;


public interface IErmResourcesOdetailsUploadFileDAO extends IBaseDAO{
	public List<ErmResourcesOdetailsUploadfile> findOdetailsUploadFile();
	public ErmResourcesOdetailsUploadfile getUpLoadFileByResIdAndYear(String resourcesId,String year);
}
