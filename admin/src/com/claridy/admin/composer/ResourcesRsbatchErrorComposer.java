package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;

public class ResourcesRsbatchErrorComposer extends SelectorComposer<Component>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Label path_label;//頁面路徑
	@Wire
	private Label all_resources;//所有上傳的資料筆數
	@Wire
	private Label erro_resources;//比對失敗的資料筆數
	@Wire
	private Grid erro_data_grid;//顯示比對失敗的資料Grid
	@Wire
	private Button out_btn;//匯出失敗的資料
	@Wire
	private Button return_btn;//返回原有頁面
	@Wire
	private Window win;
	@Wire
	private Label warn1;//上傳成功:共
	@Wire
	private Label warn2;//筆，上傳失敗明細：共
	@Wire
	private Label warn3;//筆
	//多語系
	private String pageTitle,warn1mess,warn2mess,warn3mess,ishere,noishere,returnMes,message,exprot,errorMessage,dbs,ejs,ebs,wss,ots,resourcesType,importFailResources;
	String selectedName=null;
	private String resources_code_id="";//資源類型
	private Map<StringBuffer,String[]> errMap=new LinkedHashMap<StringBuffer,String[]>();//錯誤數據
	/**
	 * 加載數據
	 */
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		Session session=Sessions.getCurrent();
		languageLoad();
		
	    path_label.setValue(pageTitle);//電子資源管理>>批次匯入>>比對結果>>錯誤結果
	    warn1.setValue(warn1mess);
	    warn2.setValue(warn2mess);
	    warn3.setValue(warn3mess);
	    //combobox中選中的值（定義在Resources_upload_result類中）
		//if(session.getAttribute("selectedName")!=null){
	    if(session.getAttribute("selectedName")!=null){
			//selectedName=session.getAttribute("selectedName").toString();
			selectedName=(String) session.getAttribute("selectedName");
		}
		if(selectedName!=null){	
			String content="";
			if(selectedName.equals("exist")){
				content=ishere;//已存在
			}else if(selectedName.equals("noExist")){
				content=noishere;//未存在
			}
			return_btn.setLabel(returnMes+content+message);//"返回"+content+"資料"
		}
		//資源類型id
		//if(	session.getAttribute("comb_resource_type")!=null){
		if(session.getAttribute("comb_resource_type")!=null){
			//resources_code_id=session.getAttribute("comb_resource_type").toString();
			resources_code_id=(String) session.getAttribute("comb_resource_type");
		}
		out_btn.setLabel(exprot);//匯出
	/*	//所有選中需要處理的數據筆數
		String isCheckedNumber="";
		if(session.getAttribute("isChecked")!=null){
			isCheckedNumber=session.getAttribute("isChecked").toString();
		}*/
		//處理成功的數據筆數
		String successNumber="";
		//if(session.getAttribute("successNumber")!=null){
		if(session.getAttribute("successNumber")!=null&&!"".equals(session.getAttribute("successNumber"))){	
			//successNumber=session.getAttribute("successNumber").toString();
			successNumber=session.getAttribute("successNumber").toString();
		}
		all_resources.setStyle("color:red");
		all_resources.setValue(successNumber);
		//查看錯誤信息是否有值
		//if(session.getAttribute("errMap")!=null){
		if(session.getAttribute("errMap")!=null){
		//	errMap=(Map<StringBuffer,String[]>)session.getAttribute("errMap");
			errMap=(Map<StringBuffer, String[]>) session.getAttribute("errMap");
		}
		erro_resources.setStyle("color:red");
		erro_resources.setValue(String.valueOf(errMap.size()));//上傳失敗筆數
		onChangeGrid();//加載Grid
	}
	/**
	 * 多語系
	 */
	public void languageLoad(){
		//得到當前語系
//		Sys sys=new Sys();
//		java.util.Locale locale = Locales.getLocale(sys.getLocale());
//		Locales.setThreadLocal(locale);
		pageTitle=Labels.getLabel("resources_rsbatch_error.pageTitle");
		warn1mess=Labels.getLabel("resources_rsbatch_error.warn1mess");
		warn2mess=Labels.getLabel("resources_rsbatch_error.warn2mess");
		warn3mess=Labels.getLabel("resources_rsbatch_error.warn3mess");
		ishere=Labels.getLabel("resources_rsbatch_error.isHere");
		noishere=Labels.getLabel("resources_rsbatch_error.noIsHere");
		returnMes=Labels.getLabel("resources_rsbatch_error.returnMes");
		message=Labels.getLabel("resources_rsbatch_error.message");
		exprot=Labels.getLabel("resources_rsbatch_error.export");
		errorMessage=Labels.getLabel("resources_rsbatch_error.errorMessage");
		dbs=Labels.getLabel("resources_rsbatch_error.dbNames");
		ejs=Labels.getLabel("resources_rsbatch_error.ejNames");
		ebs=Labels.getLabel("resources_rsbatch_error.ebNames");
		wss=Labels.getLabel("resources_rsbatch_error.wsNames");
		ots=Labels.getLabel("resources_rsbatch_error.otNames");
		resourcesType=Labels.getLabel("resources_rsbatch_error.resourcesType");
		importFailResources=Labels.getLabel("resources_rsbatch_error.importFailResources");
	}
	
	/**
	 * 加載Grid
	 *
	 */
	public void onChangeGrid(){
		int title_index=addGridTitle();//加載表頭信息
		Session session=Sessions.getCurrent();
		String[][] model=null;//將數據以二維數組格式存入
		List<String[]> list=new ArrayList<String[]>();//儲存錯誤數據的
		String[] title=null;//儲存錯誤信息內容
		String resources_type_name="";
		//資源類型name
		if(session.getAttribute("resources_type_name")!=null){
			resources_type_name=session.getAttribute("resources_type_name").toString();
		}
		
		
		//將Map中的數據遍曆出來
		if(errMap!=null && errMap.size()>0){
			title=new String[errMap.size()];
			int index=0;
			Iterator it=errMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<StringBuffer,String[]> entry=(Map.Entry<StringBuffer,String[]>)it.next();
				title[index]=entry.getKey().toString();//Key
				String[] temp=(String[])entry.getValue();//Value
				list.add(temp);
				index++;
			}
		}
		//已存在數據
		if(selectedName!=null && selectedName.equals("exist")){
		model=new String[list.size()][list.get(0).length];
		for(int i=0;i<list.size();i++){
			String[] tempArray=list.get(i);
			for(int j=0;j<tempArray.length;j++){
				if(j==0){
					model[i][0]=title[i];
				}else if(j==1){
					model[i][1]=resources_type_name;
				}else{
				model[i][j]=tempArray[j-1];
				}
			}
		}
		}else if(selectedName!=null && selectedName.equals("noExist")){//未存在數據
			model=new String[list.size()][list.get(0).length+2];
			for(int i=0;i<list.size();i++){
				String[] tempArray=list.get(i);
				for(int j=0;j<tempArray.length+2;j++){
					if(j==0){
						model[i][0]=title[i];	
					}else if(j==1){
						model[i][1]=resources_type_name;	
					}else{
					model[i][j]=tempArray[j-2];
					}
					
				}
			}
		}
		//win.setWidth(String.valueOf((100*title_index))+"px");
		ListModel listModel=new SimpleListModel(model);//將數組中的數據轉換成Grid需要的格式
		erro_data_grid.setModel(listModel);//將listModel中的數據放到Grid中
		erro_data_grid.setInnerWidth(String.valueOf((100*title_index))+"px");//grid寬度
		erro_data_grid.setRowRenderer(new Resources_rsbatch_error_Renderer());
	}
	/**
	 * 添加grid頭部信息
	 * 
	 */
		public int addGridTitle(){
			/*//電子資源庫
			String[] dbName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","出版商","更新頻率/刊期","使用說明","同時上線人數","連線方式","圖書館經費","存放地點","Domain name"};
			//電子期刊
			String[] ejName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","更新頻率/刊期","核心","出版商","出版地","所屬資料庫","出版商首頁","ISSN(PRINTED)","ISSN(ONLINE)","embargo","館藏信息","圖書館經費","存放地點"};
			//電子書
			String[] ebName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","核心","出版商","出版地","所屬資料庫","出版商首頁","ISBN(PRINTED)","ISBN(ONLINE)","embargo","電子書分類號","電子書索書號","圖書館經費","作者","版本","存放地點"};
			//網絡資源
			String[] wsName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
			//其他
			String[] otName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
			*/
			//電子資源庫
			String[] dbName=dbs.split(",");
			//電子期刊
			String[] ejName=ejs.split(",");
			//電子書
			String[] ebName=ebs.split(",");
			//網絡資源
			String[] wsName=wss.split(",");
			//其他
			String[] otName=ots.split(",");
			String[] gridTitle=null;//動態儲存grid頭部信息
			
			if(("DB").equals(resources_code_id)){
				gridTitle=dbName;
			}else if(("EJ").equals(resources_code_id)){
				gridTitle=ejName;
			}else if(("EB").equals(resources_code_id)){
				gridTitle=ebName;
			}else if(("WS").equals(resources_code_id)){
				gridTitle=wsName;
			}else if(("OT").equals(resources_code_id)){
				gridTitle=otName;
			}
			int index=gridTitle.length+2;
			Columns columns=new Columns();
			//如果是已存在，加載已存在的columns
			
				
			for(int i=0;i<index;i++){
				Column column=new Column();
				if(i==0){
					column.setLabel("");
				}else if(i==1){
					column.setLabel(resourcesType);//資源類型
				}else{
					column.setLabel(gridTitle[i-2]);
				}
				column.setWidth("100px");
				columns.appendChild(column);
				erro_data_grid.appendChild(columns);
			}
			return index;
		}
	/**
	 * 匯出失敗的資料
	 * @throws JRException 
	 */
	@Listen("onClick=#out_btn")	
	public void onClick$out_btn() throws JRException{
		String dbNames=errorMessage+","+resourcesType+","+dbs;
		String ejNames=errorMessage+","+resourcesType+","+ejs;
		String ebNames=errorMessage+","+resourcesType+","+ebs;
		String wsNames=errorMessage+","+resourcesType+","+wss;
		String otNames=errorMessage+","+resourcesType+","+ots;
		//電子資源庫
		//String[] dbName={"錯誤信息","資源類型","題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","出版商","更新頻率/刊期","使用說明","同時上線人數","連線方式","圖書館經費","存放地點","Domain Name"};
		String[] dbName=dbNames.split(",");
		String[] dbValue={"error_message","type_name","Title","Url1","Url2","Remark_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Agented_id","Publisher_id","Frenquency","Intro","Concur","Connect_id","Libarymoney","Place_id","Domain"};
		//電子期刊
		//String[] ejName={"錯誤信息","資源類型","題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","更新頻率/刊期","核心","出版商","出版地","所屬資料庫","出版商首頁","ISSN(PRINTED)","ISSN(ONLINE)","embargo","館藏信息","圖書館經費","存放地點"};
		String[] ejName=ejNames.split(",");
		String[] ejValue={"error_message","type_name","Title","Url1","Url2","Remark_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Frenquency","Core","Publisher_id","Pubplace","Db_id","Publisherurl","Issnprinted","Issnonline","Embargo","Eholdings","Libarymoney","Place_id"};
		//電子書
		//String[] ebName={"錯誤信息","資源類型","題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","核心","出版商","出版地","所屬資料庫","出版商首頁","ISBN(PRINTED)","ISBN(ONLINE)","embargo","電子書分類號","電子書索書號","圖書館經費","作者","版本","存放地點"};
		String[] ebName=ebNames.split(",");
		String[] ebValue={"error_message","type_name","Title","Url1","Url2","Remark_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Core","Publisher_id","Pubplace","Db_id","Publisherurl","Isbnprinted","Isbnonline","Embargo","Cn","Calln","Libarymoney","Author","Version","Place_id"};
		//網絡資源
		//String[] wsName={"錯誤信息","資源類型","題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
		String[] wsName=wsNames.split(",");
		String[] wsValue={"error_message","type_name","Title","Url1","Url2","Remark_id","Publisher_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Agented_id","Version"};
		//其他
		//String[] otName={"錯誤信息","資源類型","題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
		String[] otName=otNames.split(",");
		String[] otValue={"error_message","type_name","Title","Url1","Url2","Remark_id","Publisher_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Agented_id","Version"};
		//String dbfilepath="/file/電子資料庫錯誤信息.xls";
		String dbfilepath="dbErroMessage.xls";
	    String ejfilepath="ejErroMessage.xls";
	    String ebfilepath="ebErroMessage.xls";
	    String wsfilepath="wsErroMessage.xls";
	    String otfilepath="otErroMessage.xls";
        String[] gridTitle=null;//動態儲存Excel頭部信息
        String[] reportDetail=null;//綁定detail的字段名
		String fileName="";
		if(("DB").equals(resources_code_id)){
			gridTitle=dbName;
			reportDetail=dbValue;
			fileName=dbfilepath;
		}else if(("EJ").equals(resources_code_id)){
			gridTitle=ejName;
			reportDetail=ejValue;
			fileName=ejfilepath;
		}else if(("EB").equals(resources_code_id)){
			gridTitle=ebName;
			reportDetail=ebValue;
			fileName=ebfilepath;
		}else if(("WS").equals(resources_code_id)){
			gridTitle=wsName;
			reportDetail=wsValue;
			fileName=wsfilepath;
		}else if(("OT").equals(resources_code_id)){
			gridTitle=otName;
			reportDetail=otValue;
			fileName=otfilepath;
		}
		List<String[]> error_list=new ArrayList<String[]>();
		//將Grid內的數據取出來給error_list
		if(erro_data_grid!=null && erro_data_grid.getChildren().size()>0){
			ListModel listModel=erro_data_grid.getModel();
			for(int i=0;i<listModel.getSize();i++){
				String[] temp_array=(String[])listModel.getElementAt(i);
				error_list.add(temp_array);
			}
		}
		
		//報表內容
		List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
		Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report(importFailResources);//匯入失敗資料列表
		JasperReport jasperReport=rsbatch_report.getJasperReport(gridTitle, reportDetail);
		//報表賦值
		JRDataSource dataSource=new Resources_rsbatch_dataSource(reportDetail,error_list);
		JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
		jasperPrintList.add(jasperPrint);
		//生成excel報表
		JExcelApiExporter excelExporter = new JExcelApiExporter();
		excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		//報表路徑
		//String webPath=Properties.webprjpath;
		String webPath=((SystemProperties) SpringUtil
				.getBean("systemProperties")).systemDocumentPath
				+ SystemVariable.RSBATCH_PATH;
		excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, webPath+fileName);
		excelExporter.exportReport();
		//Executions.sendRedirect(fileName);
		
		String filePath=webPath+fileName;
		doEncoding(filePath, fileName);
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
	 * 返回
	 */
	@Listen("onClick=#return_btn")
	public void onClick$return_btn(){
		//Executions.sendRedirect("/resources_upload_result.jsf");
		Session session=Sessions.getCurrent();
		session.setAttribute("comb_resource_type", resources_code_id);
		String url="resourcesRsbatch/resourcesUploadResult.zul";
		ZkUtils.refurbishMethod(url);	
	}
}
class Resources_rsbatch_error_Renderer implements RowRenderer{

	private Session session=Sessions.getCurrent();
	public void render(Row row, Object data, int arg2) throws Exception {
		// TODO Auto-generated method stub
		String[] row_data=(String[])data;
		
		for(int i=0;i<row_data.length;i++){
			Label label=new Label();
			if(i==0){
				label.setStyle("color:red;");
			}
			label.setValue(row_data[i]);
			
			label.setParent(row);
		}
	}
	
}