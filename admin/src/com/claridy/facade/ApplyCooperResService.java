package com.claridy.facade;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IWebCooperationDAO;
import com.claridy.dao.IWebErwSourceDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.ApplyCooperRes;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebOrg;

@Service
public class ApplyCooperResService {
	@Autowired
	private IWebOrgDAO webOrgDao;
	@Autowired
	private IWebErwSourceDAO erwSourceDao;
	@Autowired
	private IWebCooperationDAO webCooperationDao;
	
	/**
	 * 根據時間查詢申請館合資料庫統計集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<ApplyCooperRes> findApplyCooperResList(Date startDate, Date endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<ApplyCooperRes> accNumberListRet=new ArrayList<ApplyCooperRes>();
		List<WebOrg> parentOrgList=webOrgDao.findParentOrg("");//單位集合
		List<WebErwSource> erwSourceList=erwSourceDao.findAll();//資料庫集合
		List<Object> webCoopertList=webCooperationDao.findApplyCooperResList(sdf.format(startDate), sdf.format(endDate));
		for (int i = 0; i < webCoopertList.size(); i++) {
			ApplyCooperRes appCooperResTemp=new ApplyCooperRes();
			Object[] obj=(Object[])webCoopertList.get(i);
			String parentOrgId=obj[0].toString();
			String dbId=obj[1].toString();
			Integer applyNumber=Integer.parseInt(obj[2].toString());
			List<Object> findCooperNumberList=webCooperationDao.findCooperNumberList(sdf.format(startDate),  
					sdf.format(endDate),parentOrgId,dbId);
			//循環單位集合獲取單位名稱
			for (WebOrg webOrg:parentOrgList) {
				if(webOrg.getOrgId().equals(parentOrgId)){
					appCooperResTemp.setParentOrgName(webOrg.getOrgName());
					appCooperResTemp.setParentId(parentOrgId);
					appCooperResTemp.setParentSort(webOrg.getSort());
					//循環資料庫集合獲取資料庫名稱
					for (WebErwSource erwSource:erwSourceList) {
						if(erwSource.getDbid().equals(dbId)){
							appCooperResTemp.setDbName(erwSource.getNameZhTw());
							appCooperResTemp.setDbId(dbId);
						}
					}
					appCooperResTemp.setApplyNumber(applyNumber);//申請次數
					int nuclearNumber=0;//核可次數
					int rejectedNumber=0;//駁回次數
					int untreatedNumber=0;//未處理次數
					for (int j = 0; j < findCooperNumberList.size(); j++) {
						Object[] coopObj=(Object[])findCooperNumberList.get(j);
						String status=coopObj[2].toString();
						int number=Integer.parseInt(coopObj[3].toString());
						if(status.equals("0")){
							untreatedNumber=number;
						}
						if(status.equals("1")){
							nuclearNumber=number;		
						}
						if(status.equals("2")){
							rejectedNumber=number;
						}
					}
					appCooperResTemp.setNuclearNumber(nuclearNumber);
					appCooperResTemp.setRejectedNumber(rejectedNumber);
					appCooperResTemp.setUntreatedNumber(untreatedNumber);
					accNumberListRet.add(appCooperResTemp);
				}
			}
		}
		
		int applyNumber=0;
		int nuclearNumber=0;//核可次數
		int rejectedNumber=0;//駁回次數
		int untreatedNumber=0;//未處理次數
		List<ApplyCooperRes> accNumberListRet2 = Sort(accNumberListRet, "getParentSort", "asc");
		for(ApplyCooperRes appCooperRes:accNumberListRet2){
			applyNumber+=appCooperRes.getApplyNumber();
			nuclearNumber+=appCooperRes.getNuclearNumber();
			rejectedNumber+=appCooperRes.getRejectedNumber();
			untreatedNumber+=appCooperRes.getUntreatedNumber();
		}
		ApplyCooperRes appCooperResTemp=new ApplyCooperRes();
		appCooperResTemp.setParentId("title");
		appCooperResTemp.setParentOrgName(Labels.getLabel("title"));
		appCooperResTemp.setDbId("");
		appCooperResTemp.setDbName("");
		appCooperResTemp.setApplyNumber(applyNumber);
		appCooperResTemp.setNuclearNumber(nuclearNumber);
		appCooperResTemp.setRejectedNumber(rejectedNumber);
		appCooperResTemp.setUntreatedNumber(untreatedNumber);
		accNumberListRet2.add(appCooperResTemp);
		return accNumberListRet2;
	}
	
	
	/**
	 * 根據開始日期和結束日期提供單位資料庫Id以及申請單狀態查詢
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	public List<WebCooperation> findWebCooperationListByStatus(Date startDate, Date endDate,String parentOrgId,String dbId,String status){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<WebCooperation> webCoopertList=webCooperationDao.findWebCooperationListByStatus(sdf.format(startDate), sdf.format(endDate),parentOrgId,dbId,status);
		
		WebOrg webPraentOrg=null;
		WebOrg webOrg=null;
		for(int i=0;i<webCoopertList.size();i++){
			webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCoopertList.get(i).getApplyAccount().getParentorgid());
			if(webPraentOrg!=null){
				webCoopertList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
			}else{
				webCoopertList.get(i).getApplyAccount().setParentOrgName("");
			}
			
			webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCoopertList.get(i).getApplyAccount().getOrgid());
			if(webOrg!=null){
				webCoopertList.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
			}else{
				webCoopertList.get(i).getApplyAccount().setOrgName("");
			}
		}
		return webCoopertList;
	}

	/**
	 * 
	 * @param list
	 *            要排序的集合 
	 * @param method
	 *            要排序的實體的屬性所對應的get方法
	 * @param sort
	 *            desc 為正序
	 */
	public List<ApplyCooperRes> Sort(List<ApplyCooperRes> list,
			final String method, final String sort) {
		// 用内部类实现排序
		Collections.sort(list, new Comparator<ApplyCooperRes>() {
			public int compare(ApplyCooperRes a, ApplyCooperRes b) {
				int ret = 0;
				try {
					// 获取m1的方法名
					Method m1 = a.getClass().getMethod(method, null);
					// 获取m2的方法名
					Method m2 = b.getClass().getMethod(method, null);

					if (sort != null && "desc".equals(sort)) {

						ret = m2.invoke(((ApplyCooperRes) b), null)
								.toString()
								.compareTo(
										m1.invoke(((ApplyCooperRes) a), null)
												.toString());

					} else {
						// 正序排序
						ret = m1.invoke(((ApplyCooperRes) a), null)
								.toString()
								.compareTo(
										m2.invoke(((ApplyCooperRes) b), null)
												.toString());
					}
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ret;
			}
		});
		return list;
	}
}
