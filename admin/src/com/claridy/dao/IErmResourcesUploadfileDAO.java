package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesUploadfile;

public interface IErmResourcesUploadfileDAO extends IBaseDAO{
	public List<ErmResourcesUploadfile> findByResourcesId(String resourceId);
}
