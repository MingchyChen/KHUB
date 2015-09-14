package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesSubject;

public interface IErmResourcesSubjectIdDAO extends IBaseDAO {
	
	public List<ErmResourcesSubject> getDomain(String dbId);
	public List<ErmResourcesSubject> findSubjectList(String resourcesId);
	public ErmResourcesSubject getSubject(String resourcesId,String subjectId);
}
