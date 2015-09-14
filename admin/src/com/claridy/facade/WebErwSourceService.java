package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.dao.IWebErwSourceDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;

@Service
public class WebErwSourceService {

	@Autowired
	private IWebErwSourceDAO erwSourceDao;
	
	/**
	 * 根據UUID取得WebErwSource
	 * @param uuid
	 * @return
	 */
	public WebErwSource getWebErwSourceByUUID(String uuid){
		WebErwSource webErwSource = erwSourceDao.getWebErwSourceByUUID(uuid);
		return webErwSource;
	}
	
	/**
	 * 根據DBID取得WebErwSource
	 * @param dbid
	 * @return
	 */
	public WebErwSource getWebErwSourceByDBID(String dbid){
		WebErwSource webErwSource = erwSourceDao.getWebErwSourceByDBID(dbid);
		return webErwSource;
	}
	
	/**
	 * 查詢所有的WebErwSource 
	 * @param webEmployee
	 * @return
	 */
	public List<WebErwSource> getAllWebErwSouce(WebEmployee webEmployee){
		List<WebErwSource> result = erwSourceDao.getWebErwSourceList(null, webEmployee);
		return result;
	}
	
	/**
	 * 根據dbid與Name查詢WebErwSource
	 * @param dbid
	 * @param name
	 * @return
	 */
	public List<WebErwSource> searchWebErwSource(String dbid, String name, WebEmployee webEmployee){
		WebErwSource webErwSource = new WebErwSource();
		webErwSource.setDbid(dbid);
		webErwSource.setNameZhTw(name);
		List<WebErwSource> result = erwSourceDao.getWebErwSourceList(webErwSource, webEmployee);
		return result;
	}
	
	public WebErwSource addWebErwSource(WebErwSource webErwSource){
		webErwSource.setUuid(UUIDGenerator.getUUID());
		webErwSource.setCreateDate(new Date());
		webErwSource.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSource.setWebEmployee(loginWebEmployee);
		if(loginWebEmployee.getWeborg()!=null&&loginWebEmployee.getWeborg().getOrgId()!=null&&!loginWebEmployee.getWeborg().getOrgId().equals("")){
			webErwSource.setDataOwnerGroup(loginWebEmployee.getWeborg().getOrgId());
		}else{
			webErwSource.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
		}
		webErwSource.setLatelyChangeDuser(loginWebEmployee);
		webErwSource.setIsDataEffid(1);
		webErwSource = erwSourceDao.addWebErwSource(webErwSource);
		return webErwSource;
	}
	
	public WebErwSource updateWebErwSource(WebErwSource webErwSource){
		webErwSource.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSource.setLatelyChangeDuser(loginWebEmployee);
		webErwSource = erwSourceDao.updateWebErwSource(webErwSource);
		return webErwSource;
	}
	
	public WebErwSource delWebErwSource(WebErwSource webErwSource){
		webErwSource.setLatelyChangedDate(new Date());
		WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
		webErwSource.setLatelyChangeDuser(loginWebEmployee);
		webErwSource.setIsDataEffid(0);
		webErwSource = erwSourceDao.updateWebErwSource(webErwSource);
		return webErwSource;
	}
}
