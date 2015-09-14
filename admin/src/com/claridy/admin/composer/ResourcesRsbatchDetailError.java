package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;

public class ResourcesRsbatchDetailError extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4035623781256340529L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Label pagePath;//頁面路徑
	@Wire
	private Label showResult;//處理成功筆數
	@Wire
	private Label noDoData_label;//為處理筆數
	@Wire
	private Label failData_label;//處理失敗筆數
	@Wire
	private Listbox noDoOrErrorData;//下拉
	@Wire
	private Listbox showData;//顯示數據
	@Wire
	private Button exportData;//匯出
	@Wire
	private Label warn1;//筆數據處理成功
	@Wire
	private Label warn2;//筆數據未處理
	@Wire
	private Label warn3;//筆數據處理失敗
	private String resources_type="";//電子資源類型
	private String detailType="";//副檔類型
	private List<String[]> errorList=null;//匯入失敗的資料
	private List<String[]> notDoExcelList=null;//未處理
    private int succesDataNumber=0;//記錄匯入資料成功的筆數
    //多語系
    private String pageTitle,export,message,importfail,nowork,resourceNo,title,exportTitle,titlename,warn1mess,warn2mess,warn3mess;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			languageLoad();
			pagePath.setValue(pageTitle);//"電子資源管理>副檔上傳>結果顯示>處理結果"
			//資源類型（DB\EJ\EB\WS）
			if(Sessions.getCurrent().getAttribute("resources_Type")!=null){
				resources_type=Sessions.getCurrent().getAttribute("resources_Type").toString();
			}
			//副檔名稱（"相關題名","主題","類型","適用學院","適用系所","訂購學院","訂購系所"）
			if(Sessions.getCurrent().getAttribute("detail_Type")!=null){
				detailType=Sessions.getCurrent().getAttribute("detail_Type").toString();
			}
			//成功筆數
			if(Sessions.getCurrent().getAttribute("succesDataNumber")!=null){
				succesDataNumber=Integer.parseInt(Sessions.getCurrent().getAttribute("succesDataNumber").toString());
			}
	         //錯誤數據
			if(Sessions.getCurrent().getAttribute("errorList")!=null){
				errorList=(List<String[]>) Sessions.getCurrent().getAttribute("errorList");
			}else{
				errorList=new ArrayList<String[]>();
			}
			//未處理數據
			if(Sessions.getCurrent().getAttribute("notDoExcelList")!=null){
				notDoExcelList=(List<String[]>) Sessions.getCurrent().getAttribute("notDoExcelList");
			}else{
				notDoExcelList=new ArrayList<String[]>();
			}
			warn1.setValue(warn1mess);
			warn2.setValue(warn2mess);
			warn3.setValue(warn3mess);
			exportData.setLabel(export);//"匯出"
			showResult.setValue(String.valueOf(succesDataNumber));
			noDoData_label.setValue(String.valueOf(notDoExcelList.size()));
			failData_label.setValue(String.valueOf(errorList.size()));
			noDoOrErrorData.setMold("select");
			for(int i=0;i<2;i++){
				Listitem item=new Listitem();
				if(i==0){
					item.setLabel(message);//"信息"
					item.setValue("noDo");
				}else{
					item.setLabel(importfail);//"匯入失敗資源"
					item.setValue("error");
				}
				noDoOrErrorData.appendChild(item);
			}
			noDoOrErrorData.setSelectedIndex(0);
			loadHeader();
			loadListbox();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
	 * 多語系
	 */
	public void languageLoad(){
		pageTitle=Labels.getLabel("resources_rsbatch_detail_error.pagetitle");
		export=Labels.getLabel("resources_rsbatch_detail_error.export");
		message=Labels.getLabel("resources_rsbatch_detail_error.mes");
		importfail=Labels.getLabel("resources_rsbatch_detail_error.import");
		nowork=Labels.getLabel("resources_rsbatch_detail_error.headerLabel1");
		resourceNo=Labels.getLabel("resources_rsbatch_detail_error.headerLabel2");
		title=Labels.getLabel("resources_rsbatch_detail_error.headerLabel3");
		exportTitle=Labels.getLabel("resources_rsbatch_detail_error.exportTitle");
		titlename=Labels.getLabel("resources_rsbatch_detail_error.titlename");
		warn1mess=Labels.getLabel("resources_rsbatch_detail_error.warn1mess");
		warn2mess=Labels.getLabel("resources_rsbatch_detail_error.warn2mess");
		warn3mess=Labels.getLabel("resources_rsbatch_detail_error.warn3mess");
	}
	/**
	 * 加載表頭
	 */
	public void loadHeader(){
		String columnName=getDetailMap().get(detailType);
		Listhead head=new Listhead();
		for(int i=0;i<4;i++){
			Listheader header=new Listheader();
			if(i==0){
				header.setLabel(nowork);//"未處理"
			}else if(i==1){
				header.setLabel(resourceNo);//"資源編號"
			}else if(i==2){
				header.setLabel(title);//"題名"
			}else if(i==3){
				header.setLabel(columnName);
			}
			header.setWidth("200px");
			head.appendChild(header);
		}
		showData.appendChild(head);
	}
	/**
	 * 下拉變更事件
	 */
	@Listen("onSelect=#noDoOrErrorData")
	public void onSelect$noDoOrErrorData(){
		loadListbox();
	}
	
	/**
	 * 匯出錯誤信息或者為處理信息
	 * @throws JRException 
	 */
	@Listen("onClick=#exportData")
	public void onClick$exportData() throws JRException{
		List<String[]> list=new ArrayList<String[]>();
		//根據不同下拉選項去改變listbox表頭信息
		String tag=noDoOrErrorData.getSelectedItem().getValue().toString();
		if(tag.equals("noDo")){
			list=notDoExcelList;
		}else{
			list=errorList;
		}
	
		String name=getDetailMap().get(detailType);
		String title1[]={message,resourceNo,title,name};
		String eTitle[]={"errorMesg","resources_id","title",detailType};
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String date=df.format(new Date());
		//報表內容
		List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
		Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report(exportTitle);//"匯入失敗資料列表"
		JasperReport jasperReport=rsbatch_report.getJasperReport(title1, eTitle);
		//報表賦值
		JRDataSource dataSource=new Resources_rsbatch_dataSource(eTitle,list);
		JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
		jasperPrintList.add(jasperPrint);
		//生成excel報表
		JExcelApiExporter excelExporter = new JExcelApiExporter();
		excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		//報表路徑
		String webPath=((SystemProperties) SpringUtil
				.getBean("systemProperties")).systemDocumentPath
				+ SystemVariable.RSBATCH_PATH;
		String fileName=resources_type+"-"+detailType+"("+date+")ErrorMessage.xls";
		String filePath=webPath+fileName;
		excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		excelExporter.exportReport();
		
		//Executions.sendRedirect(fileName);
		doEncoding(filePath, fileName);// 多國語里的dbtitle
	}
	/**
	 * 資源下載亂碼處理
	 */
	public void doEncoding(String fileName, String flnm) {
		// 編碼後文件名
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(flnm, "UTF-8");// 轉換字符編碼
			// Filedownload
			Filedownload.save(new FileInputStream(fileName),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加載數據到listbox中
	 */
	public void loadListbox(){
		String nDorErrorTag=noDoOrErrorData.getSelectedItem().getValue().toString();
		List<String[]> list=new ArrayList<String[]>();
		if(nDorErrorTag.equals("noDo")){
			list=notDoExcelList;
		}else{
			list=errorList;
		}
		//如果無數據，則禁用匯出按鈕
		if(list.size()==0){
			exportData.setDisabled(true);
		}else{
			exportData.setDisabled(false);
		}
		String[][] mode=new String[list.size()][4];
		for(int i=0;i<list.size();i++){
			mode[i]=list.get(i);
		}
		showData.setWidth("800px");
		ListModel lm=new SimpleListModel(mode);
		showData.setModel(lm);
		showData.setItemRenderer(new Resources_rsbatch_detail_error_itemRenderer());
		showData.setMold("paging");
		showData.setPageSize(1000);
	}
	/**
	 * 將副檔名稱中英文放在map中，方便查找
	 * @return
	 */
	public Map<String,String> getDetailMap(){
		//"相關題名","主題","類型","適用學院","適用系所","訂購學院","訂購系所"
		String[] name=titlename.split(",");
		//String[] name={"相關題名","主題","類型","適用學院","適用系所","訂購學院","訂購系所"};
		String[] title={"name","subject","type","suitcollege","suitdep","orderCollege","orderdep"};
		Map<String,String> eMap=new HashMap<String,String>();
		for(int i=0;i<name.length;i++){
			eMap.put(title[i], name[i]);
		}
		return eMap;
	}

	
}

/**
 * 填充數據到Listbox中
 * @author nj
 *
 */
class Resources_rsbatch_detail_error_itemRenderer implements ListitemRenderer{
	public void render(Listitem item, Object obj, int arg2) throws Exception {
		String[] data=(String[])obj;
        for(int i=0;i<data.length;i++){
        	new Listcell(data[i]).setParent(item);
        }
	}
}