package com.claridy.admin.composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.mechanism.facase.EmailService;
import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 使用者賬號管理 清單列
 * 2014/8/6
 */
public class WebAccountListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -436347586216509116L;

	@Wire
	private Listbox accountLix;
	@Wire
	private Textbox accountIdBox;
	
	@Wire
	private Textbox accountNameBox;
	@Wire
	private Combobox idTypeBox;
	@Wire
	private Combobox orgNameBox;
	
	@WireVariable
	private List<WebAccount> webAccountList;
	
	@WireVariable
	private List<WebOrg> webOrgList;
	@Wire
	private Radiogroup registerRgp;
	
	
	@Wire
	private Window addWebAccountWin;
	@Wire
	private Radiogroup rgroupstate;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Combobox isCheckCBbox;
	
	private WebEmployee LoginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	
	
	@WireVariable
	private WebAccount webAccount;
	
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			String accountId=accountIdBox.getValue().trim();
			String accountName=accountNameBox.getValue().trim();
			String orgName=orgNameBox.getSelectedItem().getValue();
			String isCheck=isCheckCBbox.getSelectedItem().getValue();
			accountId=XSSStringEncoder.encodeXSSString(accountId);
			accountName=XSSStringEncoder.encodeXSSString(accountName);
			orgName=XSSStringEncoder.encodeXSSString(orgName);
			Integer isregister=-1;
			if((accountId!=null&&!accountId.equals(""))
					||(accountName!=null&&!"".equals(accountName))
					||(orgName!=null&&!"0".equals(orgName))
					||(isCheck!=null&&!"-1".equals(isCheck))
					||(!orgNameBox.getSelectedItem().getValue().equals("0"))
					||(rgroupstate.getSelectedItem()!=null)
					||(!idTypeBox.getSelectedItem().getValue().equals("0"))
					||(registerRgp.getSelectedItem()!=null)){
				
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
				return;
			}
			if(registerRgp.getSelectedItem()!=null){
				isregister=Integer.parseInt(registerRgp.getSelectedItem().getValue().toString());
			}
			WebAccount webAccount=new WebAccount();
			webAccount.setAccountId(accountId);
			webAccount.setNameZhTw(accountName);
			if(rgroupstate.getSelectedItem()!=null){
				webAccount.setStatus(Integer.parseInt(rgroupstate.getSelectedItem().getValue().toString()));
			}else{
				webAccount.setStatus(-1);
			}
			if(!orgNameBox.getSelectedItem().getValue().equals("0")){
				
				
				webAccount.setParentorgid(orgNameBox.getSelectedItem().getValue().toString());
			}
			if(idTypeBox.getSelectedItem()!=null){
				webAccount.setType(Integer.parseInt((String) idTypeBox.getSelectedItem().getValue()));
			}
			webAccount.setIsCheck(Integer.parseInt(isCheck));
			List<WebAccount> webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(webAccount,isregister,1,LoginwebEmployee);
			//webAccount.getWebOrg().getOrgName()+"-"+webAccount.getWebParentorg().getOrgName();
			/*for(int i=0;i<webAccountList.size();i++){
				if(webAccountList.get(i).getWebOrg()!=null){
					
					webAccountList.get(i).setOrgId();
					
				}
			}*/
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			WebEmployee webemployeeTmp=new WebEmployee();
			for(int i=0;i<webAccountList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getParentorgid());
				if(webPraentOrg!=null){
					webAccountList.get(i).setParentOrgName(webPraentOrg.getOrgName());
				}else{
					webAccountList.get(i).setParentOrgName("");
				}
				
				webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getOrgid());
				if(webOrg!=null){
					webAccountList.get(i).setOrgName(webOrg.getOrgName());
				}else{
					webAccountList.get(i).setOrgName("");
				}
				
				
			}
			
			ListModelList<WebAccount> WebAccountModel=new ListModelList<WebAccount>(webAccountList);
			WebAccountModel.setMultiple(true);
			accountLix.setModel(WebAccountModel);
		} catch (Exception e) {
			log.error("查詢webAccount異常"+e);
		} 
	}
		
	@Listen("onClick=#addBtn")
	public void addWebAccount(){
		addWebAccountWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/webAccount/webAccountAdd.zul", null, null);
		addWebAccountWin.doModal();
	}
	/**
	 * 刪除使用者
	 */
	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deleteBtn")
	public void deleteWebAccount(){
		try {
			int updateChecked=accountLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=accountLix.getSelectedItems();
							WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for(Listitem employee:listitem){
								webAccount=employee.getValue();
								WebAccount webAccountTmp=((WebAccountService)SpringUtil.getBean("webAccountService")).getAccount(webAccount.getUuid());
								webAccountTmp.setIsDataEffid(0);
								webAccountTmp.setAccountPic(null);
								((WebAccountService)SpringUtil.getBean("webAccountService")).deleteWebAccount(webAccountTmp);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "account_"+webAccount.getUuid());
								
							}
							ZkUtils.refurbishMethod("webAccount/webAccount.zul");
						}
					}
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("刪除webAccount異常"+e);
		}
	}
	
	/**
	 * execl匯出
	 */
	@Listen("onClick=#execleBtn")
	public void remit() {
		try {
			int count=accountLix.getSelectedCount();
			if(count>0){
				
				String[] title={Labels.getLabel("account"),Labels.getLabel("name"),Labels.getLabel("unit"),Labels.getLabel("orgid"),Labels.getLabel("ermCodePublisherperson.title"),Labels.getLabel("phone"),"E-Mail",Labels.getLabel("state")};
				String[] value={"accountId","nameZhTw","parentOrgName","orgName","titleZhTw","tel","email","status"};
				Set<Listitem> listiem=accountLix.getSelectedItems();
				
				String realth=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDocumentPath;
				String fileName="document/upload/WebAccountExcel.xls";
				List<String[]> webAccountList=new ArrayList<String[]>();
				for(Listitem webAccount:listiem){
					WebAccount webAccountTmp=webAccount.getValue();
					String status="";
					if(webAccountTmp.getStatus()==1){
						status=Labels.getLabel("searchInfo.isdisable");
					}else if(webAccountTmp.getStatus()==0){
						status=Labels.getLabel("searchInfo.disable");
					}
					String titleZhTw="";
					if(webAccountTmp.getType()==3){
						titleZhTw=Labels.getLabel("webEmployeeAdd.tboxIdType.use");
					}
					if(webAccountTmp.getType()==4){
						titleZhTw=Labels.getLabel("webEmployee.tboxIdType.fuse");
					}
					if(webAccountTmp.getType()==5){
						titleZhTw=Labels.getLabel("webEmployee.tboxIdType.aidUse");
					}
					if(webAccountTmp.getType()==6){
						titleZhTw=Labels.getLabel("webEmployee.tboxIdType.technician");
					}
					if(webAccountTmp.getType()==7){
						titleZhTw=Labels.getLabel("webEmployee.tboxIdType.ftechnician");
					}
					if(webAccountTmp.getType()==8){
						titleZhTw=Labels.getLabel("webEmployee.tboxIdType.other");
					}
					WebOrg webParentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountTmp.getParentorgid());
					WebOrg webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountTmp.getOrgid());
					String[] values={webAccountTmp.getAccountId(),webAccountTmp.getNameZhTw(),webParentOrg.getOrgName(),"",titleZhTw,webAccountTmp.getTel(),webAccountTmp.getEmail(),status};
					if(webAccountTmp.getOrgid()!=null&&!webAccountTmp.getOrgid().equals("")){
						values[3]=webOrg.getOrgName();
					}
					
					webAccountList.add(values);
				}
				List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
				Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report(Labels.getLabel("webAccount.excelExport"));
				JasperReport jasperReport=rsbatch_report.getJasperReport(title, value);
				JRDataSource dataSource=new Resources_rsbatch_dataSource(value,webAccountList);
				JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
				jasperPrintList.add(jasperPrint);
				JExcelApiExporter excelExporter=new JExcelApiExporter();
				excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
				excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth+fileName);
				excelExporter.exportReport();
				Executions.sendRedirect(fileName);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("selectOut"),Labels.getLabel("info"));
			}
		} catch (JRException e) {
			log.error(""+e);
		}
	}

	/**
	 * 審核通過
	 */
	@Listen("onClick=#auditOkBtn") 
	public void auditOK(){
		try {
			int count=accountLix.getSelectedCount();
			if(count>0){
				ZkUtils.showQuestion(Labels.getLabel("webAccount.auditISOk"), Labels.getLabel("info"), new EventListener<Event>() {

					public void onEvent(Event event) throws Exception {
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							 Set<Listitem> listitem=accountLix.getSelectedItems();
							 boolean isOk=false;
							 for(Listitem account:listitem){
								 WebAccount webAccountTmp=account.getValue();
								 if(webAccountTmp.getIsCheck()!=1){
									 WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
									 webAccountTmp.setIsCheck(1);
									 webAccountTmp.setApprovePeople(loginwebEmployee.getEmployeesn());
									 webAccountTmp.setApproveDate(new Date());
									 ((WebAccountService)SpringUtil.getBean("webAccountService")).update(webAccountTmp);
									 ((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "account_"+webAccountTmp.getUuid(),Labels.getLabel("webAccount.auditOK"));
									 SystemProperties tmpSystemProperties=((SystemProperties)SpringUtil.getBean("systemProperties"));
									 String url= tmpSystemProperties.agridlURL+"/startUser?uuid="+webAccountTmp.getUuid();
									 isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToAccount("3", webAccountTmp,url);
									 
								 }else{
									 isOk=true;
								 }
							 }
							 if(isOk){
								 ZkUtils.showInformation(Labels.getLabel("webAccount.auditOK"),
											Labels.getLabel("info"));
									ZkUtils.refurbishMethod("webAccount/webAccount.zul");
							 }
						}
						
					}
					
				});
			}
		} catch (Exception e) {
			log.error("webAccount審核通過異常"+e);
		}
	}
	/**
	 * 駁回
	 */
	@Listen("onClick=#auditNOBtn")
	public void auditNO(){
		try {
			int count=accountLix.getSelectedCount();
			if(count>0){
				ZkUtils.showQuestion(Labels.getLabel("webAccount.auditIsNO"), Labels.getLabel("info"), new EventListener<Event>() {

					public void onEvent(Event event) throws Exception {
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							 Set<Listitem> listitem=accountLix.getSelectedItems();
							 WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							 boolean isOk=false;
							 for(Listitem account:listitem){
								 WebAccount webAccountTmp=account.getValue();
								
								 if(webAccountTmp.getIsCheck()!=2){
									 webAccountTmp.setIsCheck(2);
									 ((WebAccountService)SpringUtil.getBean("webAccountService")).update(webAccountTmp);
									 ((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "account_"+webAccountTmp.getUuid(),Labels.getLabel("webAccount.auditNO"));
									 isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToAccount("2", webAccountTmp,null);
									
								 }else{
									 isOk=true;
								 }
							 }
							 
							 if(isOk){
								 ZkUtils.showInformation(Labels.getLabel("webAccount.auditNO"),
											Labels.getLabel("info"));
									ZkUtils.refurbishMethod("webAccount/webAccount.zul");
							 }
						}
						
					}
					
				});
			}
		} catch (Exception e) {
			log.error("webaccount駁回異常"+e);
		}
	}
	
	@Listen("onClick=#showAll")
	public void shoaAll(){
		try {
			webOrgList=((WebAccountService)SpringUtil.getBean("webAccountService")).findOrg(null);
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			accountIdBox.setValue("");
			accountNameBox.setValue("");
			registerRgp.setSelectedItem(null);
			rgroupstate.setSelectedItem(null);
			idTypeBox.setSelectedIndex(0);
			orgNameBox.setSelectedIndex(0);
			isCheckCBbox.setSelectedIndex(0);
			Comboitem com;
			for(int i=0;i<webOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=webOrgList.get(i);
				com=new Comboitem();
				com.setLabel(webOrg.getOrgName());
				com.setValue(webOrg.getOrgId());
				orgNameBox.appendChild(com);
			}
			webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(null,-1,1,LoginwebEmployee);
			/*for(int i=0;i<webAccountList.size();i++){
				List<WebOrg> webOrgLists=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(webAccountList.get(i).getParentorgId());
				webAccountList.get(i).setOrgId(webAccountList.get(i).getOrgId()+"-"+webOrgLists.get(0).getOrgName());
			}*/
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			for(int i=0;i<webAccountList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getParentorgid());
				if(webPraentOrg!=null){
					webAccountList.get(i).setParentOrgName(webPraentOrg.getOrgName());
				}else{
					webAccountList.get(i).setParentOrgName("");
				}
				
				webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getOrgid());
				if(webOrg!=null){
					webAccountList.get(i).setOrgName(webOrg.getOrgName());
				}else{
					webAccountList.get(i).setOrgName("");
				}
				
				
			}
			ListModelList<WebAccount> tmpLML=new ListModelList<WebAccount>(webAccountList);
			tmpLML.setMultiple(true);
			accountLix.setModel(tmpLML);
		} catch (Exception e) {
			log.equals("查詢所有webAccount異常"+e);
		}
	}
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp); 
			webOrgList=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(null);
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("0");
			orgNameBox.appendChild(com);
			isCheckCBbox.setSelectedIndex(0);
			idTypeBox.setSelectedIndex(0);
			for(int i=0;i<webOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=webOrgList.get(i);
				com=new Comboitem(); 
				com.setLabel(webOrg.getOrgName());
				com.setValue(webOrg.getOrgId());
				orgNameBox.appendChild(com);
			}
			orgNameBox.setSelectedIndex(0);
			webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(null,-1,1,LoginwebEmployee);
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			for(int i=0;i<webAccountList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getParentorgid());
				if(webPraentOrg!=null){
					webAccountList.get(i).setParentOrgName(webPraentOrg.getOrgName());
				}else{
					webAccountList.get(i).setParentOrgName("");
				}
				
				webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getOrgid());
				if(webOrg!=null){
					webAccountList.get(i).setOrgName(webOrg.getOrgName());
				}else{
					webAccountList.get(i).setOrgName("");
				}
				
				
			}
			ListModelList<WebAccount> tmpLML=new ListModelList<WebAccount>(webAccountList);
			tmpLML.setMultiple(true); 
			accountLix.setModel(tmpLML);
		} catch (Exception e) {
			log.error("初始化webAccount異常"+e);
		}
		
		
		
	}
	
	

}
