package com.claridy.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IWebIndexInfoDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;
import com.claridy.domain.WebOrg;

@Service
public class WebIndexInfoService {

	@Autowired
	private IWebIndexInfoDAO webIndexDao;
	
	
	@SuppressWarnings("unchecked")
	public List<WebOrg> findOrg(String orgId){
		List<WebOrg> weblist;
		weblist=(List<WebOrg>) webIndexDao.findByHQL("from WebOrg as web where web.orgId='"+orgId+"'");
		
		
		return weblist;
	}
	public List<WebIndexInfo> findByName(String name,WebEmployee webEmployee){
		List<WebOrg> webOrgList;
		if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!webEmployee.getWeborg().getOrgId().equals("")){
			 webOrgList=findOrg(webEmployee.getWeborg().getOrgId());
		}else{
			 webOrgList=findOrg(webEmployee.getParentWebOrg().getOrgId());
		}
		return webIndexDao.findByName(name,webEmployee,webOrgList.get(0).getIsAuth());
	}
	/**
	 * 獲取頁尾資訊
	 * @return
	 */
	public WebIndexInfo getFooterInfo(){
		return webIndexDao.getFooterInfo();
	}
	public void updatewebIndexInfo(WebIndexInfo webIndex){
		webIndexDao.merge(webIndex);
	}
	
	public WebIndexInfo findByMatter(String matter){
		return webIndexDao.findByMatter(matter);
	}
	public void deleteWebIndexInfo(WebIndexInfo webIndexInfo){
		webIndexDao.merge(webIndexInfo);
	}
	public void updateIsDisplay(WebIndexInfo webIndexInfo){
		webIndexDao.merge(webIndexInfo);
	}
}
