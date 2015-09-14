package com.claridy.dao;

import java.util.List;

import com.claridy.common.mechanism.dao.IBaseDAO;
import com.claridy.domain.ErmResourcesMainfileV;

public interface IErmResourcesMainfileVDAO extends IBaseDAO {

	public List<ErmResourcesMainfileV> findErmResMainfileVList(String resourcesId);
	public ErmResourcesMainfileV getMainfileVByResId(String resourcesId);
	public ErmResourcesMainfileV getErmResMainfileVByResId(String resourcesId);
	public List<Object> findObjectListBySql(String sql);
	public ErmResourcesMainfileV getErmMainFileByTypeName(String resourcesId,String typeId,String name);
	
	public List<ErmResourcesMainfileV> findByTypeId(String typeId);
	
	public List<ErmResourcesMainfileV> findByIdName(String typeId,String id,String name);
	
	public ErmResourcesMainfileV findByResourceId(String ResourceId);
	
	public List<ErmResourcesMainfileV> findAll();
	
	public ErmResourcesMainfileV getErmResMainfileVByResIdAndDbid(String resourcesId,String dbId);
	public Integer findErmCodeDbList(String resourcesId);
}
