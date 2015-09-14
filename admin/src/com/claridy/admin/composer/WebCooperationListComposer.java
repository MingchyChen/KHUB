package com.claridy.admin.composer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;


import java.util.ArrayList;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebCooperationService;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebOrgListService;
/**
 * zfdong nj
 * 館際合作申請管理清單列
 * 2014/8/15
 */
public class WebCooperationListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283198248542073889L;
	@Wire
	private Combobox applyOrgCBbox;
	@Wire
	private Combobox acceptOrgCBbox;
	@Wire
	private Textbox applyOddTbox;
	@Wire
	private Textbox applyPeopleTbox;
	@Wire
	private Combobox retStatusCBbox;
	@Wire
	private Datebox startDate;
	@Wire
	private Datebox endDate;
	@Wire
	private Listbox webCooperationLix;
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		
		try {
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebCooperation> webCooperationList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findAll(loginwebEmployee);
			List<WebOrg> webOrgList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findParentOrg(null);
			Comboitem applyCom=new Comboitem();
			applyCom.setLabel(Labels.getLabel("select"));
			applyCom.setValue("-1");
			applyOrgCBbox.appendChild(applyCom);
			for(int i=0;i<webOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=webOrgList.get(i);
				applyCom=new Comboitem(); 
				applyCom.setLabel(webOrg.getOrgName());
				applyCom.setValue(webOrg.getOrgId());
				applyOrgCBbox.appendChild(applyCom);
			}
			applyOrgCBbox.setSelectedIndex(0);
			
			Comboitem acceptCom=new Comboitem();
			acceptCom.setLabel(Labels.getLabel("select"));
			acceptCom.setValue("-1");
			acceptOrgCBbox.appendChild(acceptCom);
			for(int i=0;i<webOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=webOrgList.get(i);
				acceptCom=new Comboitem(); 
				acceptCom.setLabel(webOrg.getOrgName());
				acceptCom.setValue(webOrg.getOrgId());
				acceptOrgCBbox.appendChild(acceptCom);
			}
			acceptOrgCBbox.setSelectedIndex(0);
			retStatusCBbox.setSelectedIndex(0);
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			for(int i=0;i<webCooperationList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getParentorgid());
				if(webPraentOrg!=null){
					webCooperationList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
					webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getOrgid());
					if(webOrg!=null){
						webCooperationList.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
					}else{
						webCooperationList.get(i).getApplyAccount().setOrgName("");
					}
				}else{
					webCooperationList.remove(i);
				}
			}
			ListModelList<WebCooperation> model=new ListModelList<WebCooperation>(webCooperationList);
			model.setMultiple(true);
			webCooperationLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#searchBtn")
	public void search(){
		try {
			if(applyOrgCBbox.getSelectedItem()!=null&&!applyOrgCBbox.getSelectedItem().getValue().equals("-1")
					||(acceptOrgCBbox.getSelectedItem()!=null&&!acceptOrgCBbox.getSelectedItem().getValue().equals("-1"))
					||(applyOddTbox.getValue()!=null&&!applyOddTbox.getValue().equals(""))
					||(applyPeopleTbox.getValue()!=null&&!applyPeopleTbox.getValue().equals(""))
					||(retStatusCBbox.getSelectedItem()!=null&&!retStatusCBbox.getSelectedItem().getValue().equals("-1"))
					||(startDate.getValue()!=null&&!startDate.getValue().equals(""))
					||(endDate.getValue()!=null&&!endDate.getValue().equals(""))){
				
				
			}else{
				 ZkUtils.showExclamation(Labels.getLabel("webCooperation.selectNo"),Labels.getLabel("info"));
				 return;
			}
			WebOrg applyAccount=null;
			WebOrg acceptEmployee=null;
			String uuid;	
			int status=-1;
			if(applyOrgCBbox.getSelectedItem()!=null&&!applyOrgCBbox.getSelectedItem().getValue().equals("-1")){
				WebOrg webOrg=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(applyOrgCBbox.getSelectedItem().getValue().toString()).get(0);
				applyAccount=webOrg;
			}
			if(acceptOrgCBbox.getSelectedItem()!=null&&!acceptOrgCBbox.getSelectedItem().getValue().equals("-1")){
				WebOrg webOrgAccpet=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(acceptOrgCBbox.getSelectedItem().getValue().toString()).get(0);
				acceptEmployee=webOrgAccpet;
			}
			String applyOdd=applyOddTbox.getValue().trim();
			applyOdd=XSSStringEncoder.encodeXSSString(applyOdd);
			uuid=applyOdd;
			
			String applyPeople=applyPeopleTbox.getValue().trim();
			applyPeople=XSSStringEncoder.encodeXSSString(applyPeople);
			/*if(applyPeople!=null&&!"".equals(applyPeople)){
				applyPeople=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findById(applyPeople).getUuid();
			}*/
			
			if(retStatusCBbox.getSelectedItem()!=null&&!retStatusCBbox.getSelectedItem().getValue().equals("-1")){
				String statusS=retStatusCBbox.getSelectedItem().getValue();
				status=Integer.parseInt(statusS);
			}
			
			 
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebCooperation> webCooperationList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findByWebCooperation(applyAccount,loginwebEmployee, acceptEmployee, uuid,applyPeople, status, startDate.getValue(), endDate.getValue());
			if(webCooperationList.size()>0&&applyPeople!=null&&!"".equals(applyPeople)){
				for(int i=0;i<webCooperationList.size();i++){
					if(webCooperationList.get(i).getApplyAccount().getNameZhTw().indexOf(applyPeople)==-1){
						webCooperationList.remove(i);
						i=i-1;
					}
				}
			}
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			for(int i=0;i<webCooperationList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getParentorgid());
				if(webPraentOrg!=null){
					webCooperationList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
					webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getOrgid());
					if(webOrg!=null){
						webCooperationList.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
					}else{
						webCooperationList.get(i).getApplyAccount().setOrgName("");
					}
				}else{
					webCooperationList.remove(i);
				}
			}
			ListModelList<WebCooperation> model=new ListModelList<WebCooperation>(webCooperationList);
			model.setMultiple(true);
			webCooperationLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	
	/**
	 * 汇出execl
	 */
	@Listen("onClick=#execleBtn")
	public void remit() {
		try {
			int count=webCooperationLix.getSelectedCount();
			if(count>0){
				
				String[] title={Labels.getLabel("webErwSource.applyOdd"),Labels.getLabel("webErwSource.applyOrg"),Labels.getLabel("webErwSource.applyTitle"),Labels.getLabel("webErwSource.apployPeople"),Labels.getLabel("webErwSource.applyDate"),Labels.getLabel("webErwSource.acceptOrg"),Labels.getLabel("webErwSource.retStatus")};
				String[] value={"uuid","applyAccount。parentOrgName","titleZhTw","applyAccount。nameZhTw","createDate","acceptEmployee。parentWebOrg。orgName","status"};
				Set<Listitem> listiem=webCooperationLix.getSelectedItems();
				
				String realth=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDocumentPath;
				String fileName="document/upload/WebCooperationExcel.xls";
				List<String[]> webAccountList=new ArrayList<String[]>();
				for(Listitem webEmployee:listiem){
					WebCooperation webCooperationTmp=webEmployee.getValue();
					String status="";
					if(webCooperationTmp.getStatus()==1){
						status=Labels.getLabel("webCooperation.statusOK");
					}else if(webCooperationTmp.getStatus()==0){
						status=Labels.getLabel("webCooperation.statusNoDispose");
					}else if(webCooperationTmp.getStatus()==2){
						status=Labels.getLabel("webCooperation.statusNO");
					}

					WebOrg webParentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationTmp.getApplyAccount().getParentorgid());
					if(webParentOrg!=null){
						webCooperationTmp.getApplyAccount().setParentOrgName(webParentOrg.getOrgName());
					}else{
						webCooperationTmp.getApplyAccount().setParentOrgName("");
					}
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String[] values={webCooperationTmp.getUuid(),webCooperationTmp.getApplyAccount().getParentOrgName(),webCooperationTmp.getTitleZhTw(),webCooperationTmp.getApplyAccount().getNameZhTw(),format.format(webCooperationTmp.getCreateDate()),webCooperationTmp.getAcceptEmployee().getParentWebOrg().getOrgName(),status};
		
					webAccountList.add(values);
				}
				List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
				Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report(Labels.getLabel("webCooperation.excelExport"));
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
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			applyOddTbox.setValue("");
			applyOrgCBbox.setSelectedIndex(0);
			acceptOrgCBbox.setSelectedIndex(0);
			applyPeopleTbox.setValue("");
			retStatusCBbox.setSelectedIndex(0);
			startDate.setValue(null);
			endDate.setValue(null);
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebCooperation> webCooperationList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findAll(loginwebEmployee);
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			for(int i=0;i<webCooperationList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getParentorgid());
				if(webPraentOrg!=null){
					webCooperationList.get(i).getApplyAccount().setParentOrgName(webPraentOrg.getOrgName());
					webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperationList.get(i).getApplyAccount().getOrgid());
					if(webOrg!=null){
						webCooperationList.get(i).getApplyAccount().setOrgName(webOrg.getOrgName());
					}else{
						webCooperationList.get(i).getApplyAccount().setOrgName("");
					}
				}else{
					webCooperationList.remove(i);
				}
			}
			ListModelList<WebCooperation> model=new ListModelList<WebCooperation>(webCooperationList);
			model.setMultiple(true);
			webCooperationLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	  
	
	

}
