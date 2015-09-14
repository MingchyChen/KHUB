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

import com.claridy.dao.IWebAccountDAO;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.domain.AccountApplyDetail;
import com.claridy.domain.AccountApplyNumber;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebOrg;

@Service
public class AccountApplyNumberService {
	@Autowired
	private IWebAccountDAO webAccountDao;
	
	@Autowired
	private IWebOrgDAO webOrgDao;
	
	/**
	 * 根據時間查詢帳號申請量統計集合
	 * 報表剛進去調用的方法
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountApplyNumber> findAccAppNumList(Date startDate, Date endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AccountApplyNumber> accAppNumListRet=new ArrayList<AccountApplyNumber>();
		List<WebOrg> parentOrgList=webOrgDao.findParentOrg("");
		List<Object> accAppNumberList=webAccountDao.findAccApplyNumList(sdf.format(startDate), sdf.format(endDate));
		for (int i = 0; i < accAppNumberList.size(); i++) {
			Object[] obj=(Object[])accAppNumberList.get(i);
			String parentOrgId=obj[0].toString();
			Integer applyNumber=Integer.parseInt(obj[1].toString());
			//循環單位集合獲取單位名稱
			for (WebOrg webOrg:parentOrgList) {
				if(webOrg.getOrgId().equals(parentOrgId)){
					AccountApplyNumber accAppNumberTemp=new AccountApplyNumber();
					accAppNumberTemp.setParentOrgName(webOrg.getOrgName());
					accAppNumberTemp.setParentId(parentOrgId);
					accAppNumberTemp.setParentSort(webOrg.getSort());
					accAppNumberTemp.setApplyNumber(applyNumber);
					accAppNumListRet.add(accAppNumberTemp);
				}
			}
		}
		List<AccountApplyNumber> accAppNumListRet2 = Sort(accAppNumListRet, "getParentSort", "asc");
		return accAppNumListRet2;
	}
	/**
	 * 根據時間查詢個組室帳號申請量統計集合
	 * 點擊單位名稱調用的方法
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountApplyNumber> findAccAppNumListByParent(Date startDate, Date endDate,String parentOrgId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AccountApplyNumber> accAppNumListRet=new ArrayList<AccountApplyNumber>();
		List<WebAccount> webAccountList=webAccountDao.findWebAccListByParent(sdf.format(startDate), sdf.format(endDate),parentOrgId);
		List<WebOrg> orgList=webOrgDao.findOrg(parentOrgId);
		for (int i = 0; i < orgList.size(); i++) {
			WebOrg webOrg=orgList.get(i);
			AccountApplyNumber accAppNumTemp=new AccountApplyNumber();
			int applyNumber=0;
			for(WebAccount webAccount:webAccountList){
				if(webAccount.getOrgid()!=null&&!"".equals(webAccount.getOrgid())&&webAccount.getOrgid().equals(webOrg.getOrgId())){
					applyNumber++;
				}
			}
			accAppNumTemp.setOrgName(webOrg.getOrgName());
			accAppNumTemp.setOrgId(webOrg.getOrgId());
			accAppNumTemp.setParentId(parentOrgId);
			accAppNumTemp.setApplyNumber(applyNumber);
			accAppNumListRet.add(accAppNumTemp);
		}
		
		AccountApplyNumber OaccAppNumTemp=new AccountApplyNumber();
		int OApplyNumber=0;
		for(WebAccount webAccount:webAccountList){
			if(webAccount.getParentorgid().equals(parentOrgId)&&("".equals(webAccount.getOrgid())||webAccount.getOrgid()==null)){
				OApplyNumber++;
			}
		}
		//其他
		OaccAppNumTemp.setOrgName(Labels.getLabel("webEmployee.tboxIdType.other"));
		OaccAppNumTemp.setOrgId("other"+parentOrgId);
		OaccAppNumTemp.setParentId(parentOrgId);
		OaccAppNumTemp.setApplyNumber(OApplyNumber);
		accAppNumListRet.add(OaccAppNumTemp);
		
		int applyNumber=0;
		for (int i = 0; i < accAppNumListRet.size(); i++) {
			AccountApplyNumber accAppNumTemp=accAppNumListRet.get(i);
			applyNumber+=accAppNumTemp.getApplyNumber();
		}
		AccountApplyNumber accAppNumTemp=new AccountApplyNumber();
		//小計
		accAppNumTemp.setOrgName(Labels.getLabel("subTitle"));
		accAppNumTemp.setOrgId("");
		accAppNumTemp.setParentId(parentOrgId);
		accAppNumTemp.setApplyNumber(applyNumber);
		accAppNumListRet.add(accAppNumTemp);
		return accAppNumListRet;
	}
	
	/**
	 * 根據時間單位查詢帳號申請量詳細信息
	 * 點擊第一層數字和第二層小計數字調用的方法
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountApplyDetail> findAccAppDetByParentOrg(Date startDate, Date endDate,String parentOrgId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AccountApplyDetail> accAppDetListRet=new ArrayList<AccountApplyDetail>();
		List<WebAccount> webAccountList=webAccountDao.findWebAccListByParent(sdf.format(startDate), sdf.format(endDate),parentOrgId);
		List<WebOrg> orgList=webOrgDao.findWebOrgListToCombox();
		for(WebAccount webAccount:webAccountList){
			AccountApplyDetail accAppDetTemp=new AccountApplyDetail();
			accAppDetTemp.setCreateDate(webAccount.getCreateDate());
			accAppDetTemp.setApplyAccountId(webAccount.getAccountId());
			accAppDetTemp.setApplyAccountName(webAccount.getNameZhTw());
			String pOrgId=webAccount.getParentorgid();
			String orgId=webAccount.getOrgid();
			//循環單位組室集合獲取單位組室名稱
			for (WebOrg webOrg:orgList) {
				if(webOrg.getOrgId().equals(pOrgId)){
					accAppDetTemp.setApplyParentOrgName(webOrg.getOrgName());
				}
				if(webOrg.getOrgId().equals(orgId)){
					accAppDetTemp.setApplyOrgName(webOrg.getOrgName());
				}
			}
			String checkResult="";
			if(webAccount.getIsCheck()==0){
				checkResult=Labels.getLabel("webAccount.isCheckPend");
			}else if(webAccount.getIsCheck()==1){
				checkResult=Labels.getLabel("webAccount.isCheckOK");
			}else{
				checkResult=Labels.getLabel("webAccount.isCheckNO");
			}
			accAppDetTemp.setCheckResult(checkResult);
			accAppDetListRet.add(accAppDetTemp);
		}
		return accAppDetListRet;
	}
	/**
	 * 根據時間單位组室为空查詢帳號申請量詳細信息
	 * 點擊第二層其他一列數字調用的方法
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountApplyDetail> findAccAppDetByParentOrgOther(Date startDate, Date endDate,String parentOrgId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AccountApplyDetail> accAppDetListRet=new ArrayList<AccountApplyDetail>();
		List<WebAccount> webAccountList=webAccountDao.findWebAccListByParent(sdf.format(startDate), sdf.format(endDate),parentOrgId);
		List<WebOrg> orgList=webOrgDao.findWebOrgListToCombox();
		for(WebAccount webAccount:webAccountList){
			if(webAccount.getParentorgid().equals(parentOrgId)&&(webAccount.getOrgid()==null||webAccount.getOrgid().equals(""))){
				AccountApplyDetail accAppDetTemp=new AccountApplyDetail();
				accAppDetTemp.setCreateDate(webAccount.getCreateDate());
				accAppDetTemp.setApplyAccountId(webAccount.getAccountId());
				accAppDetTemp.setApplyAccountName(webAccount.getNameZhTw());
				String pOrgId=webAccount.getParentorgid();
				String orgId=webAccount.getOrgid();
				//循環單位組室集合獲取單位組室名稱
				for (WebOrg webOrg:orgList) {
					if(webOrg.getOrgId().equals(pOrgId)){
						accAppDetTemp.setApplyParentOrgName(webOrg.getOrgName());
					}
					if(webOrg.getOrgId().equals(orgId)){
						accAppDetTemp.setApplyOrgName(webOrg.getOrgName());
					}
				}
				String checkResult="";
				if(webAccount.getIsCheck()==0){
					checkResult=Labels.getLabel("webAccount.isCheckPend");
				}else if(webAccount.getIsCheck()==1){
					checkResult=Labels.getLabel("webAccount.isCheckOK");
				}else{
					checkResult=Labels.getLabel("webAccount.isCheckNO");
				}
				accAppDetTemp.setCheckResult(checkResult);
				accAppDetListRet.add(accAppDetTemp);
			}
		}
		return accAppDetListRet;
	}
	/**
	 * 根據時間單位組室查詢登入量詳細信息
	 * 點擊第二層組室對應數字調用的方法
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	public List<AccountApplyDetail> findAccAppDetByOrg(Date startDate, Date endDate,String parentOrgId,String orgId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<AccountApplyDetail> accAppDetListRet=new ArrayList<AccountApplyDetail>();
		List<WebAccount> webAccountList=webAccountDao.findWebAccListByParent(sdf.format(startDate), sdf.format(endDate),parentOrgId);
		List<WebOrg> orgList=webOrgDao.findWebOrgListToCombox();
		for(WebAccount webAccount:webAccountList){
			if(webAccount.getOrgid()!=null&&webAccount.getOrgid().equals(orgId)){
				AccountApplyDetail accAppDetTemp=new AccountApplyDetail();
				accAppDetTemp.setCreateDate(webAccount.getCreateDate());
				accAppDetTemp.setApplyAccountId(webAccount.getAccountId());
				accAppDetTemp.setApplyAccountName(webAccount.getNameZhTw());
				String pOrgId=webAccount.getParentorgid();
				String orgIdStr=webAccount.getOrgid();
				//循環單位組室集合獲取單位組室名稱
				for (WebOrg webOrg:orgList) {
					if(webOrg.getOrgId().equals(pOrgId)){
						accAppDetTemp.setApplyParentOrgName(webOrg.getOrgName());
					}
					if(webOrg.getOrgId().equals(orgIdStr)){
						accAppDetTemp.setApplyOrgName(webOrg.getOrgName());
					}
				}
				String checkResult="";
				if(webAccount.getIsCheck()==0){
					checkResult=Labels.getLabel("webAccount.isCheckPend");
				}else if(webAccount.getIsCheck()==1){
					checkResult=Labels.getLabel("webAccount.isCheckOK");
				}else{
					checkResult=Labels.getLabel("webAccount.isCheckNO");
				}
				accAppDetTemp.setCheckResult(checkResult);
				accAppDetListRet.add(accAppDetTemp);
			}
		}
		return accAppDetListRet;
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
	public List<AccountApplyNumber> Sort(List<AccountApplyNumber> list,
			final String method, final String sort) {
		// 用内部类实现排序
		Collections.sort(list, new Comparator<AccountApplyNumber>() {
			public int compare(AccountApplyNumber a, AccountApplyNumber b) {
				int ret = 0;
				try {
					// 获取m1的方法名
					Method m1 = a.getClass().getMethod(method, null);
					// 获取m2的方法名
					Method m2 = b.getClass().getMethod(method, null);

					if (sort != null && "desc".equals(sort)) {

						ret = m2.invoke(((AccountApplyNumber) b), null)
								.toString()
								.compareTo(
										m1.invoke(((AccountApplyNumber) a), null)
												.toString());

					} else {
						// 正序排序
						ret = m1.invoke(((AccountApplyNumber) a), null)
								.toString()
								.compareTo(
										m2.invoke(((AccountApplyNumber) b), null)
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
