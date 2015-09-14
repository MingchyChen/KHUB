package com.claridy.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.dao.IErmResourcesCkrsDAO;
import com.claridy.dao.IErmResourcesMainDbwsDAO;
import com.claridy.dao.IErmResourcesMainEjebDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.dao.hibernateimpl.ErmResourcesRfconDAO;
import com.claridy.dao.hibernateimpl.ErmResourcesRsconDAO;
import com.claridy.domain.ErmResourcesCkrs;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesRecon;
import com.claridy.domain.ErmResourcesRfcon;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;


@Service
public class ErmResourcesCkrsService {

	
	@Autowired
	private IErmResourcesCkrsDAO ermResourcesCkrsDAO;
	@Autowired
	private IErmResourcesMainfileVDAO ermfileDAO;
	@Autowired
	private WebSysLogService webSysLogService;
	@Autowired
	private ErmResourcesRfconDAO ermResourcesRfconDAO;
	@Autowired
	private ErmResourcesRsconDAO ermResourcesRsconDAO;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public List<ErmResourcesMainfileV> findAll(){
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		List<ErmResourcesMainfileV> ermResourcesMainfileVList=new ArrayList<ErmResourcesMainfileV>(); 
		if(ermCkrsList.size()>0){
			for(ErmResourcesCkrs ermResourcesCkrs:ermCkrsList){
				ErmResourcesMainfileV ermResourcesMainfileV=ermfileDAO.getErmResMainfileVByResId(ermResourcesCkrs.getResourcesId());
				if(ermResourcesMainfileV!=null&&ermResourcesMainfileV.getResourcesId()!=null&&!ermResourcesMainfileV.getResourcesId().equals("")){
					ermResourcesMainfileVList.add(ermResourcesMainfileV);
				}
			}
		}
		return ermResourcesMainfileVList;
	}
	
	public void delete(String resourId,WebEmployee loginWebEmployee){
		ErmResourcesCkrs ermResrourcesCkrs=ermResourcesCkrsDAO.findByResourId(resourId);
		if(ermResrourcesCkrs.getUuid()!=null){
			ermResrourcesCkrs.setIsDataEffid(0);
			ermResrourcesCkrs.setLatelyChangedDate(new Date());
			ermResrourcesCkrs.setLatelyChangedUser(loginWebEmployee.getEmployeeId());
			ermResourcesCkrsDAO.merge(ermResrourcesCkrs);
			webSysLogService.delLog(ZkUtils.getRemoteAddr(),loginWebEmployee.getEmployeeName(), "ermResourcesCKRS_"+ermResrourcesCkrs.getUuid());
		}
	}
	
	public List<ErmResourcesMainfileV> findByTypeName(String typeId,String name){
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		List<ErmResourcesMainfileV> ermResourcesMainfileVList=new ArrayList<ErmResourcesMainfileV>(); 
		if(ermCkrsList.size()>0){
			for(ErmResourcesCkrs ermResourcesCkrs:ermCkrsList){
				ErmResourcesMainfileV ermResourcesMainfileV=ermfileDAO.getErmMainFileByTypeName(ermResourcesCkrs.getResourcesId(),typeId,name);
				if(ermResourcesMainfileV!=null&&ermResourcesMainfileV.getResourcesId()!=null&&!ermResourcesMainfileV.getResourcesId().equals("")){
					ermResourcesMainfileVList.add(ermResourcesMainfileV);
				}
			}
		}
		return ermResourcesMainfileVList;
	}
	
	public List<ErmResourcesMainfileV> findMainfileVAll(){
		List<ErmResourcesMainfileV> ermResourcesMainList=ermfileDAO.findAll();
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		for(int i=0;i<ermResourcesMainList.size();i++){
			for(int j=0;j<ermCkrsList.size();j++){
				if(ermResourcesMainList.get(i).getResourcesId().equals(ermCkrsList.get(j).getResourcesId())){
					ermResourcesMainList.remove(i);
					if(i>0){
						i=i-1;
					}
				}
			}
		}
		return ermResourcesMainList;
	}
	
	
	public List<ErmResourcesMainfileV> findByTypeId(String typeId){
		List<ErmResourcesMainfileV> ermResourcesMainList=ermfileDAO.findByTypeId(typeId);
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		for(int i=0;i<ermResourcesMainList.size();i++){
			for(int j=0;j<ermCkrsList.size();j++){
				if(ermResourcesMainList.get(i).getResourcesId()!=null&&ermResourcesMainList.get(i).getResourcesId().equals(ermCkrsList.get(j).getResourcesId())){
					ermResourcesMainList.remove(i);
					if(i>0){
						i=i-1;
					}
				}
			}
		}
		return ermResourcesMainList;
	}
	
	public List<ErmResourcesMainfileV> findByIdName(String typeId,String id,String name){
		List<ErmResourcesMainfileV> ermResourcesMainList=ermfileDAO.findByIdName(typeId,id,name);
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		for(int i=0;i<ermResourcesMainList.size();i++){
			for(int j=0;j<ermCkrsList.size();j++){
				if(ermResourcesMainList.get(i).getResourcesId().equals(ermCkrsList.get(j).getResourcesId())){
					ermResourcesMainList.remove(i);
					if(i>0){
						i=i-1;
					}
				}
			}
		}
		return ermResourcesMainList; 
	}
	
	public ErmResourcesMainfileV findByResourceId(String resourcesId){
		return ermfileDAO.findByResourceId(resourcesId);
	}
	
	/**
	 * 偵測電子資源
	 */
	public void searchCkrs(){
		List<ErmResourcesCkrs> ermCkrsList=ermResourcesCkrsDAO.findAll();
		for(int i=0;i<ermCkrsList.size();i++){
			ErmResourcesMainfileV ermResourcesMainfileV=findByResourceId(ermCkrsList.get(i).getResourcesId());
			boolean ok=urlValidata(ermResourcesMainfileV.getUrl1());
			if(!ok){
				try {
					// 插入rscon
					ErmResourcesRscon ermResourcesRscon = new ErmResourcesRscon();
					ermResourcesRscon.setResourcesId(ermResourcesMainfileV
							.getResourcesId());
					ermResourcesRscon.setTitle(ermResourcesMainfileV.getTitle());
					ermResourcesRscon.setUuid(UUIDGenerator.getUUID());
					ermResourcesRscon.setUrlcon("2");
					ermResourcesRscon.setCkdate(new Date());
					ermResourcesRscon.setDbId(ermResourcesMainfileV.getDbId());
					ermResourcesRscon.setCreateDate(new Date());
					ermResourcesRscon.setIsDataEffid(1);
					ermResourcesRsconDAO.saveOrUpdate(ermResourcesRscon);

					Date date = new Date();

					// 插入rfcon
					ErmResourcesRfcon ermResourcesRfcon = new ErmResourcesRfcon();
					ErmResourcesRfcon ermResourcesRfcon2 = ermResourcesRfconDAO
							.findById(ermResourcesMainfileV.getResourcesId());
					if (ermResourcesRfcon2 != null
							&& ermResourcesRfcon2.getResourcesId() != null
							&& !"".equals(ermResourcesRfcon2.getResourcesId())) {
						ermResourcesRfcon2.setCkdate(date);
						ermResourcesRfcon2.setLatelyChangedDate(new Date());
						ermResourcesRfcon2.setIsDataEffid(1);
						ermResourcesRfconDAO.merge(ermResourcesRfcon2);
					} else {
						ermResourcesRfcon.setUuid(UUIDGenerator.getUUID());
						ermResourcesRfcon.setResourcesId(ermResourcesMainfileV
								.getResourcesId());
						ermResourcesRfcon.setTitle(ermResourcesMainfileV.getTitle());
						ermResourcesRfcon.setUrl(ermResourcesMainfileV.getUrl1());
						ermResourcesRfcon.setCkdate(date);
						ermResourcesRfcon.setDbId(ermResourcesMainfileV.getDbId());
						ermResourcesRfcon.setCreateDate(new Date());
						ermResourcesRfcon.setIsDataEffid(1);
						ermResourcesRfconDAO.saveOrUpdate(ermResourcesRfcon);
					}
				} catch (Exception e) {
					log.error(""+e); 
				}
			}
			ermCkrsList.get(i).setIsDataEffid(0);
			ermResourcesCkrsDAO.merge(ermCkrsList.get(i));
		}
	}
	
	// 判斷ULR是否有效
	public boolean urlValidata(String urlPath) {
		boolean bool = true;
		try {
			URL url = new URL(urlPath);
			URLConnection uc = url.openConnection();// 創建連接
			// 用IO讀取內容
			BufferedReader br = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			br.close();
		} catch (Exception e) {
			bool = false;// 如果讀取異常則返回false;
		}
		return bool;
	}
	
	public void insert(ErmResourcesMainfileV ermResourcesMainFileV,WebEmployee webEmployee){
		ErmResourcesCkrs ermResourcesCkrs=ermResourcesCkrsDAO.getByResourId(ermResourcesMainFileV.getResourcesId());
		if(ermResourcesCkrs!=null&&ermResourcesCkrs.getResourcesId()!=null&&!ermResourcesCkrs.getResourcesId().equals("")){
			ermResourcesCkrs.setIsDataEffid(1);
			ermResourcesCkrsDAO.merge(ermResourcesCkrs);
			webSysLogService.editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "ermResourcesCKRS_"+ermResourcesCkrs.getUuid());
		}else{
			ermResourcesCkrs=new ErmResourcesCkrs();
			ermResourcesCkrs.setUuid(UUIDGenerator.getUUID());
			ermResourcesCkrs.setCreateDate(new Date());
			ermResourcesCkrs.setResourcesId(ermResourcesMainFileV.getResourcesId());
			ermResourcesCkrs.setIsDataEffid(1);
			ermResourcesCkrs.setWebEmployee(webEmployee);
			ermResourcesCkrs.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			ermResourcesCkrsDAO.create(ermResourcesCkrs);
			webSysLogService.editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "ermResourcesCKRS_"+ermResourcesCkrs.getUuid());
		}
		
	}
}
