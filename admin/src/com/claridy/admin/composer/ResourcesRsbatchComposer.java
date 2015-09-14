package com.claridy.admin.composer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.ResourcesMainfileSolrSearch;


public class ResourcesRsbatchComposer extends SelectorComposer<Component>{
	@Wire
	private Label path_label;//頁面路徑
	@Wire
	private Combobox comb_resource_type;// 资源类型
	@Wire
	private Button btn_upload;// 上傳
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Button uploadFile;
	@Wire
	private Button delFile;
	private String uploadFileName;
	private String fileName;
	private List<String[]> excelDataList = null;//Excel中的數據
	private List<String[]> existList = null;// 已存在數據
	private List<String[]> existDBList = null;//數據庫中已存在數據
	private List<String[]> nExistList = null;// 未存在數據
	//private HashMap<String,String[]> errMap = new HashMap<String,String[]>();//錯誤數據
	private String errMsg = "";// 錯誤信息
	@Wire
	private Label warn1;//*為確保系統處理效能，建議一次上傳比對的的筆數，以5000筆為限
	@Wire
	private Label warn2;//下載匯入檔案格式
	@Wire
	private Label warn3;//匯入資料
	@Wire
	private Label warn4;//上傳檔案
	@Wire
	private Toolbarbutton dbtoolbar;
	@Wire
	private Toolbarbutton ejtoolbar;
	@Wire
	private Toolbarbutton ebtoolbar;
	@Wire
	private Toolbarbutton wstoolbar;
	//private Toolbarbutton ottoolbar;
	//private String dbfilepath="/DB.xls";
	private String dbfilepath= ((SystemProperties) SpringUtil
			.getBean("systemProperties")).systemDocumentPath
			+ SystemVariable.RSBATCH_PATH+"DB.xls";
	private String ejfilepath=((SystemProperties) SpringUtil
			.getBean("systemProperties")).systemDocumentPath
			+ SystemVariable.RSBATCH_PATH+"EJ.xls";
	private String ebfilepath=((SystemProperties) SpringUtil
			.getBean("systemProperties")).systemDocumentPath
			+ SystemVariable.RSBATCH_PATH+"EB.xls";
	private String wsfilepath=((SystemProperties) SpringUtil
			.getBean("systemProperties")).systemDocumentPath
			+ SystemVariable.RSBATCH_PATH+"WS.xls";
	//private String otfilepath="/file/其他.xls";
	private String languageTag="";//記錄多語系
	//多語系
	private String DBFileName,EJFileName,EBFileName,WSFileName;
	String formatStr = "yyyy/MM/dd";
	SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
	/*//電子資源庫
	String[] dbName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代",
			"合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言",
			"代理商","出版商","更新頻率/刊期","使用說明","同時上線人數","連線方式",
			"圖書館經費","存放地點","Domain name"};
	//電子期刊
	String[] ejName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代",
			"合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言",
			"更新頻率/刊期","核心","出版商","出版地","所屬資料庫","出版商首頁","ISSN(PRINTED)",
			"ISSN(ONLINE)","embargo","館藏信息","圖書館經費","存放地點"};
	//電子書
	String[] ebName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代",
			"合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言",
			"核心","出版商","出版地","所屬資料庫","出版商首頁","ISBN(PRINTED)","ISBN(ONLINE)",
			"embargo","電子書分類號","電子書索書號","圖書館經費","作者","版本","存放地點"};
	//網絡資源
	String[] wsName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期",
			"收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)",
			"語言","代理商","版本"};
	//其他
	String[] otName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期",
			"收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)",
			"語言","代理商","版本"};*/
	String[] dbName,ejName,ebName,wsName,otName;
	private final Logger log = LoggerFactory.getLogger(getClass());
	WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	/**
	 * 頁面加載
	 */
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
//		//得到當前語系
//		Sys sys=new Sys();
//		languageTag=sys.getLocale();//zh_TW,zh_CN
//		java.util.Locale locale = Locales.getLocale(sys.getLocale());
//		Locales.setThreadLocal(locale);
		String dbNames=Labels.getLabel("resources_rsbatch.dbNames");
		dbName=dbNames.split("[,]");
		String ejNames=Labels.getLabel("resources_rsbatch.ejNames");
		ejName=ejNames.split("[,]");
		String ebNames=Labels.getLabel("resources_rsbatch.ebNames");
		ebName=ebNames.split("[,]");
		String wsNames=Labels.getLabel("resources_rsbatch.wsNames");
		wsName=wsNames.split("[,]");
		String otNames=Labels.getLabel("resources_rsbatch.otNames");
		otName=otNames.split("[,]");
		warn1.setValue(Labels.getLabel("resources_rsbatch.warn1mes"));
		//下載匯入檔案格式
		warn2.setValue(Labels.getLabel("resources_rsbatch.warn2mes"));
		warn3.setValue(Labels.getLabel("resources_rsbatch.warn3mes"));
		warn4.setValue(Labels.getLabel("resources_rsbatch.warn4mes"));
		DBFileName=Labels.getLabel("resources_rsbatch.db");//電子資料庫
		EJFileName=Labels.getLabel("resources_rsbatch.ej");
		EBFileName=Labels.getLabel("resources_rsbatch.eb");
		WSFileName=Labels.getLabel("resources_rsbatch.ws");
		dbtoolbar.setLabel(DBFileName);//電子資料庫
		//有改動,之前寫反電子期刊與電子書
		ejtoolbar.setLabel(EJFileName);//電子期刊
		ebtoolbar.setLabel(EBFileName);//電子書
		wstoolbar.setLabel(WSFileName);//網路資源
		// 連接數據庫
/*		UbictechConnectFactory connFactory = new UbictechConnectFactory();
		Connection conn = connFactory.getUbictechConn();

		IOerm_code_generalcode erm_code_generalcode = new IOerm_code_generalcode();
		erm_code_generalcode.setFilter(" item_id='RETYPE' AND history='N'");
		List<IOerm_code_generalcode> erm_code_generalcode_list = erm_code_generalcode
				.search(connFactory, conn);*/
//		if (erm_code_generalcode_list != null
//				&& erm_code_generalcode_list.size() > 0) {
//
//			for (int i = 0; i < erm_code_generalcode_list.size(); i++) {
//				Comboitem item = new Comboitem();
//				erm_code_generalcode = erm_code_generalcode_list.get(i);
//				item.setLabel(erm_code_generalcode.getName1());
//				item.setValue(erm_code_generalcode.getGeneralcode_id());
//				comb_resource_type.appendChild(item);
//			}
//			comb_resource_type.setSelectedIndex(0);
//		}
		List<ErmCodeGeneralCode> ermCodeLAllist=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(webEmployee);
		Comboitem com=new Comboitem();
//		com.setLabel(Labels.getLabel("select"));
//		com.setValue(0);
	//	comb_resource_type.appendChild(com);
		for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodeLAllist){
			com=new Comboitem();
			com.setLabel(ermCodeGeneralCode.getName1());
			com.setValue(ermCodeGeneralCode.getGeneralcodeId());
			comb_resource_type.appendChild(com);
		}
		comb_resource_type.setSelectedIndex(0);
		path_label.setValue(Labels.getLabel("resources_rsbatch.pageTitle"));//電子資源管理>>批次匯入
		// 上傳
		//btn_upload.setLabel(Labels.getLabel("resources_rsbatch.upload"));//上傳

		// 如果沒有關閉connFactory
//		if (connFactory != null) {
//			connFactory.closeConn(conn);
//			conn = null;
//			connFactory = null;
//		}
	}
	
	/**
	 * 
	 * 上傳文件
	 * 
	 * @throws IOException
	 * 
	 */
	@Listen("onClick=#btn_upload")
	public void onClick$btn_upload(Event event) throws IOException {
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		Desktop dt = Executions.getCurrent().getDesktop();
		Configuration config = dt.getWebApp().getConfiguration();
		// 上傳文件的最大大小
		config.setMaxUploadSize(1024);
		// 中文亂碼
		config.setUploadCharset("UTF-8");
		Media media = null;
		//上傳提示
		String uploadMessage=Labels.getLabel("resources_rsbatch.uploadMessage");
		media = Fileupload.get(Labels.getLabel("resources_rsbatch.chooseUpload"), Labels.getLabel("resources_rsbatch.fileupload"));//"選擇上傳的文件", "文件上傳"
		if (media != null) {
			// 獲取文件名及文件後綴名
			String filename = ((Media) media).getName();
			// 獲取下拉列表中的資源名稱
			String resource_name = comb_resource_type.getSelectedItem()
					.getLabel();
			String[] file=filename.split("[.]");

			// 判斷文件格式是否正確
			if (!filename.endsWith(".xls")) {
				Messagebox.show(Labels.getLabel("resources_rsbatch.message1"), uploadMessage, Messagebox.OK,
						Messagebox.EXCLAMATION);//"文件類型不是xls,請重新上傳", "上傳提示"
				return;
			}else {
				// 判斷文件名是否屬於當前選中的資源類型
			  /*  if(!file[0].equals(resource_name)){
	            	try {
						Messagebox.show("上傳的文件類型與當前選中的類型不匹配,請重新上傳", "上傳提示", Messagebox.OK,
								Messagebox.EXCLAMATION);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					return;
	            }*/
			}
			//InputStream ins = new ByteArrayInputStream(media.getByteData());
			InputStream ins =((Media) media).getStreamData();
			//ByteArrayInputStream ins=new ByteArrayInputStream(media.getByteData());
			 if (filename.endsWith(".xls")) {
				readExcel(ins);
			}
			if (!("").equals(errMsg)) {
				Messagebox.show(errMsg, uploadMessage, Messagebox.OK,
						Messagebox.EXCLAMATION);//"上傳提示"
				errMsg="";
				return;
			}

			ins.close();

			//已存在數據
//			if(existList!=null && existList.size()>0){
//				session.setAttribute("existList", existList);
//			}else{
//				if(session.getAttribute("existList")!=null){//如果不為空則清空session
//					session.removeAttribute("existList");
//				}
//			}
//			//數據庫中已存在的數據
//			if(existDBList!=null && existDBList.size()>0){
//				session.setAttribute("existDBList", existDBList);
//			}else{
//				if(session.getAttribute("existDBList")!=null){//如果不為空則清空session
//					session.removeAttribute("existDBList");
//				}
//			}
//			//未存在數據
//			if(nExistList!=null && nExistList.size()>0){
//				session.setAttribute("nExistList", nExistList);
//			}else{
//				if(session.getAttribute("nExistList")!=null){//如果不為空則清空session
//					session.removeAttribute("nExistList");
//				}
//			}
//			//錯誤數據
//			if(session.getAttribute("errMap")!=null){//如果不為空則清空session
//				session.removeAttribute("errMap");
//			}
//			//(已存在)處理後的數據如果存在（定義在Resources_upload_result類中）
//			if(session.getAttribute("doAfterExistData")!=null){//如果不為空則清空session
//				session.removeAttribute("doAfterExistData");
//			}
//			//(未存在)處理後的數據如果存在（定義在Resources_upload_result類中）
//			if(session.getAttribute("doAfterNoExistData")!=null){//如果不為空則清空session
//				session.removeAttribute("doAfterNoExistData");
//			}
//			//combobox中選中的值（定義在Resources_upload_result類中）
//			if(session.getAttribute("selectedName")!=null){//如果不為空則清空session
//				session.removeAttribute("selectedName");
//			}
//			//電子資料庫類型id和類型name
//			String resources_type_id=comb_resource_type.getSelectedItem().getValue().toString();
//			String resources_type_name=comb_resource_type.getSelectedItem().getLabel();
//			session.setAttribute("comb_resource_type", resources_type_id);
//			session.setAttribute("resources_type_name", resources_type_name);
			//頁面跳轉
			//Executions.sendRedirect("/resources_upload_result.jsf");
			//Executions.sendRedirect("/WEB-INF/pages/system/resourcesRsbatch/resourcesUploadResult.zul");
			 String url = "resourcesRsbatch/resourcesUploadResult.zul";
			 ZkUtils.refurbishMethod(url);
		}

	}

	/**
	 * Excel
	 * 
	 * @param ins
	 * @return
	 */
	public boolean readExcel(InputStream ins) {
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		String message2=Labels.getLabel("resources_rsbatch.message2");//文件讀取失敗
		String message3=Labels.getLabel("resources_rsbatch.message3");//一次最多只能上傳1000筆數據
		String message4=Labels.getLabel("resources_rsbatch.message4");//上傳的文件欄位不正確，請重新下載匯入檔案模版
		String message5=Labels.getLabel("resources_rsbatch.message5");//上傳的文件欄位題名不正確，請重新下載匯入檔案模版
		String message6=Labels.getLabel("resources_rsbatch.message6");//電子資源的題名，URL，採購註記，起訂日期，語言，Domain name欄位值必須填寫
		String message7=Labels.getLabel("resources_rsbatch.message7");//電子資源的題名，URL，採購註記，起訂日期，語言,所屬資料庫欄位值必須填寫
		String message8=Labels.getLabel("resources_rsbatch.message8");//電子資源的題名，URL，採購註記，起訂日期，語言欄位值必須填寫
		boolean bool = false;
		excelDataList = new ArrayList<String[]>();
		HSSFWorkbook workbook;
		HSSFSheet sheet = null;
		
		String rsType = comb_resource_type.getSelectedItem().getValue().toString();
		try {
			//ByteArrayInputStream bs=null;
			workbook = new HSSFWorkbook(ins);
			sheet = workbook.getSheetAt(0);
			ins.close();
		} catch (IOException e) {
			e.printStackTrace();
			errMsg = message2;
			return bool;
		}

		
		if(sheet != null){
			HSSFRow row = null;
			HSSFCell cell = null;
			
			int i; // row number
			short j; // column number
			int rowNum = sheet.getLastRowNum();	//total row number
			//check total row number
			if(rowNum > 5000){
				errMsg = message3;//一次最多只能上傳1000筆數據！
				return bool;
			}
			//get columns
			for (i = 0; i <= rowNum; i++) {
				row = sheet.getRow(i);
				if (row != null){
					String[] rowdata = null;	//rows value
					
					//create rowdata
					if("DB".equals(rsType)){
						rowdata = new String[dbName.length];
					}else if("EJ".equals(rsType)){
						rowdata = new String[ejName.length];
					}else if("EB".equals(rsType)){
						rowdata = new String[ebName.length];
					}else if("WS".equals(rsType)){
						rowdata = new String[wsName.length];
					}else if("OT".equals(rsType)){
						rowdata = new String[otName.length];
					}
					if(i==0 && (rowdata == null || rowdata.length > row.getLastCellNum())){
						errMsg = message4;//上傳的文件欄位不正確，請重新下載匯入檔案模版！
						return bool;
					}else{
						int PhysicalNumberOfCells=0;//記錄一行所有的cell中是否都是空值
						//fill rowdata	
						for (j = 0; j < rowdata.length; j++) {
							String cellStr = null;
							cell = row.getCell(j);
							if (cell != null) {
								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_NUMERIC: 		// if numeral | date
									if(HSSFDateUtil.isCellDateFormatted(cell)) {	//if date
										double d=cell.getNumericCellValue();
										cellStr = dateFormat.format(HSSFDateUtil.getJavaDate(d));
									}else{
										//數字類型轉換為int
										cellStr = String.valueOf((int)cell.getNumericCellValue());	//if numeral
									}
									break;
								default:
									cellStr = cell.getStringCellValue();
									if(j==18 || j==19 || j==20){
										if(cellStr.contains("-")){
											cellStr=cellStr.replace("-", "");
										}
									}else{
										
									}
									
									break;
								}
							}else{
								cellStr = "";
							}
							if(rowdata.length > j){
								rowdata[j] = cellStr.trim();
								if(cellStr.trim()==null || "".equals(cellStr.trim())){
									PhysicalNumberOfCells++;
								}
							}
						}
						//如果此行所有單元格的數據都為空
						if(PhysicalNumberOfCells==rowdata.length){
							continue;
						}
						
						//check columns title
						if(i==0){
							boolean checkTitle = false;
							if("DB".equals(rsType)){
								checkTitle = ckColumnsTitle(dbName,rowdata);
							}else if("EJ".equals(rsType)){
								checkTitle = ckColumnsTitle(ejName,rowdata);
							}else if("EB".equals(rsType)){
								checkTitle = ckColumnsTitle(ebName,rowdata);
							}else if("WS".equals(rsType)){
								checkTitle = ckColumnsTitle(wsName,rowdata);
							}else if("OT".equals(rsType)){
								checkTitle = ckColumnsTitle(otName,rowdata);
							}
							if(!checkTitle){
								errMsg = message5;//上傳的文件欄位題名不正確，請重新下載匯入檔案模版！
								return bool;
							}
						}else{
							//check the non-empty
							if("DB".equals(rsType)){
								if("".equals(rowdata[0]) || "".equals(rowdata[1]) 
										|| "".equals(rowdata[3]) || "".equals(rowdata[4])){
									errMsg = message6;//電子資源的題名，URL，採購註記，起訂日期欄位值必須填寫
									return bool;
								}
								excelDataList.add(rowdata);
								
							}else if("OT".equals(rsType) || "WS".equals(rsType)){
								if("".equals(rowdata[0]) || "".equals(rowdata[1]) 
										|| "".equals(rowdata[3]) || "".equals(rowdata[4])){
									errMsg = message8;//電子資源的題名，URL，採購註記，起訂日期欄位值必須填寫
									return bool;
								}
								excelDataList.add(rowdata);
							} 
							else if("EJ".equals(rsType)){
								if("".equals(rowdata[0]) || "".equals(rowdata[1]) 
										|| "".equals(rowdata[3]) || "".equals(rowdata[4])
										||  "".equals(rowdata[17])){
									errMsg = message7;//電子資源的題名，URL，採購註記，起訂日期，所屬資料庫欄位值必須填寫
									return bool;
								}
								excelDataList.add(rowdata);
							}
								else{
									if("".equals(rowdata[0]) || "".equals(rowdata[1]) 
											|| "".equals(rowdata[3]) || "".equals(rowdata[4])
											||  "".equals(rowdata[16])){
										errMsg = message7;//電子資源的題名，URL，採購註記，起訂日期，所屬資料庫欄位值必須填寫
										return bool;
									}
									excelDataList.add(rowdata);
							}
							
							
						}
						
					}
					
				}
			}
		}
		bool = existsDB(rsType);
		return bool;
	}

	/**
	 * check columnsTitle
	 * @param baseTitle
	 * @param excelTitle
	 * @return
	 */
	public boolean ckColumnsTitle(String[] baseTitle, String[] excelTitle){
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		String message01=Labels.getLabel("resources_rsbatch.message01");//文件讀取失敗  不存在 
		String message02=Labels.getLabel("resources_rsbatch.message02");//文件讀取失敗  ',或該題名錯誤!  
		String message03=Labels.getLabel("resources_rsbatch.message03");//文件讀取失敗   提示信息
		boolean result = true;
		try {
			if(baseTitle.length == excelTitle.length){
				for(int i = 0;i < baseTitle.length; i++){
					if( !(baseTitle[i].trim().equals(excelTitle[i].trim())) ){
						//增加彈出題名錯誤提示框
						Messagebox.show(message01+baseTitle[i].trim()+message02,message03,Messagebox.OK,Messagebox.INFORMATION);
					   result = false;
					}
				}
			}
		} catch (Exception e) {		
			result = false;
		}
		return result;
	}
	/**
	 * 下載電子資料庫範本
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#dbtoolbar")
	public void onClick$dbtoolbar() throws FileNotFoundException{
 		if(languageTag.equals("zh_CN")){//如果是簡體中文
			this.setDbfilepath("/file/DB_zh_cn.xls");
			doEncoding(this.getDbfilepath(),"DB_zh_cn"+".xls");//電子資料庫
		}else
			doEncoding(this.getDbfilepath(),"DB"+".xls");//電子資料庫
	}

	/**
	 * 下載電子期刊範本
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ejtoolbar")
	public void onClick$ejtoolbar() throws FileNotFoundException{
		//Filedownload.save(this.getEjfilepath(), "xls");
		if(languageTag.equals("zh_CN")){//如果是簡體中文
			this.setEjfilepath("/file/EJ_zh_cn.xls");
			doEncoding(this.getEjfilepath(),"EJ_zh_cn"+".xls");//電子期刊
		}else
		doEncoding(this.getEjfilepath(),"EJ"+".xls");//電子期刊
	}
	/**
	 * 下載電子書範本
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ebtoolbar")
	public void onClick$ebtoolbar() throws FileNotFoundException{
		//Filedownload.save(this.getEbfilepath(), "xls");
        if(languageTag.equals("zh_CN")){//如果是簡體中文
			this.setEbfilepath("/file/EB_zh_cn.xls");
			doEncoding(this.getEbfilepath(),"EB_zh_cn"+".xls");//電子書
		}else
		doEncoding(this.getEbfilepath(),"EB"+".xls");//電子書
	}
	/**
	 * 下載網絡資源範本
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#wstoolbar")
	public void onClick$wstoolbar() throws FileNotFoundException{
		//Filedownload.save(this.getWsfilepath(), "xls");
        if(languageTag.equals("zh_CN")){//如果是簡體中文
			this.setWsfilepath("/file/WS_zh_cn.xls");//將文件路徑修改成中文簡體的路徑
			doEncoding(this.getWsfilepath(),"WS_zh_cn"+".xls");//網路資源
		}else
		doEncoding(this.getWsfilepath(),"WS"+".xls");//網路資源
	}
	/**
	 * 下載其他範本
	 * @throws FileNotFoundException
	 */
	/*public void onClick$ottoolbar() throws FileNotFoundException{
		//Filedownload.save(this.getOtfilepath(), "xls");
		doEncoding(this.getOtfilepath());
	}*/
	/**
	 * 資源下載亂碼處理
	 */
	public void doEncoding(String fileName,String flnm){
		//編碼後文件名
        String encodedName = null; 
        //String name=fileName.substring(fileName.lastIndexOf("/")+1);
        try {
        	encodedName = URLEncoder.encode(flnm,"UTF-8");//轉換字符編碼
		    //String path=Properties.webprjpath;//獲取服務器路徑
        	//String path="document/ermFile/";
		    //Filedownload
			//Filedownload.save(new FileInputStream(path+fileName),"application/octet-stream",encodedName);
			Filedownload.save(new FileInputStream(fileName),"application/octet-stream",encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getDbfilepath() {
		return dbfilepath;
	}
	
	/**
	 * 將Excel中的數據分為已存在、未存在
	 * @param rowdata
	 * @return 
	 */
	public boolean existsDB(String rsType){
		existList = new ArrayList<String[]>();
		nExistList = new ArrayList<String[]>();
		existDBList = new ArrayList<String[]>();
//		UbictechConnectFactory connFactory = new UbictechConnectFactory();
//		Connection conn = connFactory.getUbictechConn();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
		//數據庫查詢出的數據集
		List<String[]> dbList = new ArrayList<String[]>();
		String sql = "";
		if("DB".equals(rsType)){
			//電子資料庫的查詢SQL
			sql = "select m.TITLE,m.URL1,m.URL2,g1.NAME1 as gName1,m.STARORDERDATE,m.ENDORDERDATE,m.COVERAGE,"+
					"m.REGLLYIP,m.IDPWD,m.OTHERS,m.BRIEF1,m.BRIEF2,g2.NAME1 as gName2,p1.NAME as pName1,p2.NAME as pName2, "+
					"m.FRENQUENCY,m.INTRO,m.CONCUR,g3.NAME1 as gName3,m.LIBARYMONEY,g4.NAME1 as gName4,m.RESOURCES_ID, "+
					"m.latelychangeddate  "+
					"from erm_resources_main_dbws m left join ERM_CODE_GENERALCODE g1 "+
					"on (m.REMARK_ID = g1.GENERALCODE_ID AND g1.item_id='PURE' AND g1.history='N') "+
					"left join ERM_CODE_GENERALCODE g2 "+
					"on (m.LANGUAGE_ID = g2.GENERALCODE_ID "+
					"AND g2.item_id='DBLAN' AND g2.history='N') "+
					"left join ERM_CODE_PUBLISHER p1 "+
					"on (m.AGENTED_ID = p1.PUBLISHER_ID) "+
					"left join ERM_CODE_PUBLISHER p2 "+
					"on (m.PUBLISHER_ID = p2.PUBLISHER_ID) "+
					"left join ERM_CODE_GENERALCODE g3 "+
					"on (m.CONNECT_ID = g3.GENERALCODE_ID AND g3.item_id='ACCESS' AND g3.history='N') "+
					"left join ERM_CODE_GENERALCODE g4 "+
					"on (m.PLACE_ID = g4.GENERALCODE_ID AND g4.item_id='PLACE' AND g4.history='N') "+
					"where m.resources_id like 'DB%' and (m.history='N' or m.history='' or m.history is null)";

		}else if("EJ".equals(rsType)){
			//電子期刊的查詢SQL
			sql = "select m.TITLE,i.URL1,i.URL2,g1.NAME1 as name1,i.STARORDERDATE,i.ENDORDERDATE,"+
			          "i.COVERAGE,i.REGLLYIP,i.IDPWD,i.OTHERS, "+
			          "m.BRIEF1,m.BRIEF2,g2.NAME1 as name2,i.FRENQUENCY,m.CORE, "+
			          "p1.NAME as p1Name,i.PUBPLACE,d1.NAME as d1Name,i.PUBLISHERURL,m.ISSNPRINTED, "+
			          "m.ISSNONLINE,i.EMBARGO,i.EHOLDINGS,i.LIBARYMONEY,g3.NAME1 as name3, "+
			          "m.RESOURCES_ID,m.latelychangeddate "+
			          "from ERM_RESOURCES_MAIN_EJEB m "+
			          "left join ERM_RESOURCES_EJEB_ITEM i on i.resources_id=m.resources_id "+
			          "left join ERM_CODE_GENERALCODE g1 "+
			          "on (i.REMARK_ID = g1.GENERALCODE_ID AND g1.item_id='PURE' AND g1.history='N') "+
			          "left join ERM_CODE_GENERALCODE g2 on (m.LANGUAGE_ID = g2.GENERALCODE_ID "+
			          "AND g2.item_id='RELAN' AND g2.history='N') "+
			          "left join ERM_CODE_PUBLISHER p1 on (i.PUBLISHER_ID = p1.PUBLISHER_ID) "+
			          "left join ERM_CODE_DB d1 on (i.DB_ID = d1.DB_ID) "+
			          "left join ERM_CODE_GENERALCODE g3 "+
			          "on (i.PLACE_ID = g3.GENERALCODE_ID AND g3.item_id='PLACE' AND g3.history='N') "+
			          "where m.resources_id like 'EJ%' and (i.history='N' or i.history='' or i.history is null)";
		}else if("EB".equals(rsType)){
			//電子書的查詢SQL
			sql ="select m.TITLE,i.URL1,i.URL2,g1.NAME1,i.STARORDERDATE,i.ENDORDERDATE,"+
					"i.COVERAGE,i.REGLLYIP,i.IDPWD,i.OTHERS, "+
					"m.BRIEF1,m.BRIEF2,g2.NAME1,m.CORE,i.p1.NAME, "+
					"i.PUBPLACE,d1.NAME,i.PUBLISHERURL,m.ISBNPRINTED,m.ISBNONLINE, "+
					"i.EMBARGO,m.CN,m.CALLN,i.LIBARYMONEY,m.AUTHOR,i.VERSION,g3.NAME1,"+
					"m.RESOURCES_ID,m.latelychangeddate "+
					"from ERM_RESOURCES_MAIN_EJEB m "+
					"left join ERM_RESOURCES_EJEB_ITEM i on i.resources_id=m.resources_id "+
					"left join ERM_CODE_GENERALCODE g1 "+
					"on (i.REMARK_ID = g1.GENERALCODE_ID AND g1.item_id='PURE' AND g1.history='N') "+
					"left join ERM_CODE_GENERALCODE g2 on (m.LANGUAGE_ID = g2.GENERALCODE_ID "+
					"AND g2.item_id='RELAN' AND g2.history='N') "+
					"left join ERM_CODE_DB d1 on (i.DB_ID = d1.DB_ID) "+
					"left join ERM_CODE_PUBLISHER p1 on (i.PUBLISHER_ID = p1.PUBLISHER_ID) "+
					"left join ERM_CODE_GENERALCODE g3 "+
					"on (i.PLACE_ID = g3.GENERALCODE_ID AND g3.item_id='PLACE' AND g3.history='N') "+
					"where m.resources_id like 'EB%' and (i.history='N' or i.history='' or i.history is null)";
		}else if("WS".equals(rsType)){
			//網路資源的查詢SQL
			sql = "select m.TITLE,m.URL1,m.URL2,g1.NAME1,p1.NAME,m.STARORDERDATE,m.ENDORDERDATE, "+
					"m.COVERAGE,m.REGLLYIP,m.IDPWD,OTHERS, "+
					"m.BRIEF1,m.BRIEF2,g1.NAME1,p2.NAME,m.VERSION,"+
					"m.RESOURCES_ID,m.latelychangeddate "+
					"from erm_resources_main_dbws m left join ERM_CODE_GENERALCODE g1 "+
					"on (m.REMARK_ID = g1.GENERALCODE_ID AND g1.item_id='PURE' AND g1.history='N') "+
					"left join ERM_CODE_PUBLISHER p1 "+
					"on (m.PUBLISHER_ID = p1.PUBLISHER_ID) "+
					"left join ERM_CODE_GENERALCODE g2 "+
					"on (m.LANGUAGE_ID = g2.GENERALCODE_ID AND g2.item_id='RELAN' AND g2.history='N') "+
					"left join ERM_CODE_PUBLISHER p2 "+
					"on (m.AGENTED_ID = p2.PUBLISHER_ID) "+
					"where m.resources_id like 'WS%' and (m.history='N' or m.history='' or m.history is null)";
		}
		else{
			//其他的查詢SQL
			sql = "select m.TITLE,m.URL1,m.URL2,g1.NAME1,p1.NAME,m.STARORDERDATE,m.ENDORDERDATE, "+
					"m.COVERAGE,m.REGLLYIP,m.IDPWD,OTHERS, "+
					"m.BRIEF1,m.BRIEF2,g1.NAME1,p2.NAME,m.VERSION, "+
					"m.RESOURCES_ID,m.latelychangeddate "+
					"from ERM_RESOURCES_MAINFILE m left join ERM_CODE_GENERALCODE g1 "+
					"on (m.REMARK_ID = g1.GENERALCODE_ID AND g1.item_id='PURE' AND g1.history='N') "+
					"left join ERM_CODE_PUBLISHER p1 "+
					"on (m.PUBLISHER_ID = p1.PUBLISHER_ID) "+
					"left join ERM_CODE_GENERALCODE g2 "+
					"on (m.LANGUAGE_ID = g2.GENERALCODE_ID AND g2.item_id='RELAN'  AND g2.history='N') "+
					"left join ERM_CODE_PUBLISHER p2 "+
					"on (m.AGENTED_ID = p2.PUBLISHER_ID)"+
					"where m.resources_id like 'OT%'";
		}
		//System.out.println("0000000000000000000000000000");
		//System.out.println(sql);
		try { 
			List<Object> resourcesListTemp=((ResourcesMainfileSolrSearch) SpringUtil
					.getBean("resourcesMainfileSolrSearch")).findObjectListBySql(sql);
			//System.out.println(resourcesListTemp.size());
			for (int k = 0; k < resourcesListTemp.size(); k++) {
				Object[] objTemp = (Object[]) resourcesListTemp.get(k);
				//為電子資料庫的集合填充數據
				if("DB".equals(rsType)){
					String[] data = new String[23];
					for(int j = 0;j<data.length-2;j++){
						//data[j++] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						//起訖日期
						if(j != 4 && j!= 5){
							//data[j++] = rs.getString(j) == null ? "" : rs.getString(j);
						//	System.out.println("=="+j);
							data[j] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						}else{
							//System.out.println(j);
						//	System.out.println(objTemp[j]);
							if(objTemp[j]!=null){
								data[j] = dateFormat.format(objTemp[j]==null ? "":objTemp[j]);
							}else{
								data[j] = (String) (objTemp[j] == null ? "" : objTemp[j]);
							}
						}
						
					}
/*					//副檔
					data[22] = "";
					data[23] = "";
					data[24] = "";
					data[25] = "";
					data[26] = "";
					data[27] = "";*/
					//RESOURCES_ID
					if(objTemp[21]!=null){
						//data[21] =  dateFormat.format(objTemp[22]);
						//System.out.println(objTemp[21]);
						//data[21] = dateFormat.format(objTemp[22]==null ? "":objTemp[22]);
						data[21]=(String) (objTemp[21] == null ? "" : objTemp[21]);
					}else{
						data[21] =  (String) (objTemp[22] == null ? "" : objTemp[22]);
					}
					
					//LAST_UPDATE_DATE
					try {
						data[22] = dateFormat.format(objTemp[23]==null ? "":objTemp[23]);
						//data[22] = (String) objTemp[23];
					} catch (Exception e) {
						data[22] = null;
					}
					dbList.add(data);
					
				//為電子期刊的集合填充數據
				}else if("EJ".equals(rsType)){
					if(k==22519){
						int s=0;
					}
					String[] data = new String[27];
					for(int j = 0;j<data.length-2;j++){
					//data[j++] = rs.getString(j) == null ? "" : rs.getString(j);
						//System.out.println(objTemp[j]+"=="+j);
						//起訖日期
						if(j != 4 && j!= 5&&j!=21){
							//System.out.println(objTemp[j]);
							data[j] =(String) (objTemp[j] == null ? "" : objTemp[j]);
						}else if(j==21){
							data[j] =(String) (objTemp[j] == null ? "" : objTemp[j]+"");
						}
						else{
							if(objTemp[j]!=null){
								data[j] = dateFormat.format(objTemp[j]);
							}else{
								data[j]="";
							}
							
						}
					}
				/*	//副檔
					data[25] = "";
					data[26] = "";
					data[27] = "";
					data[28] = "";
					data[29] = "";
					data[30] = "";
					data[31] = "";*/
					//RESOURCES_ID
					data[25] = (String) objTemp[25];
					//LAST_UPDATE_DATE
					try {
						data[26] = dateFormat.format(objTemp[26]);
					} catch (Exception e) {
						data[26] = null;
					}
					dbList.add(data);
				//為電子書的集合填充數據
				}else if("EB".equals(rsType)){
					String[] data = new String[29];
					for(int j = 0;j<data.length-2;){
					//	data[j++] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						//起訖日期
						if(j != 4 && j!= 5)
							//data[j++] = rs.getString(j) == null ? "" : rs.getString(j);
							data[j++] = (String) (objTemp[j] == null ? "" :objTemp[j]);
						else{
							if(objTemp[j]!=null){
								data[j++] = dateFormat.format(objTemp[j]);
							}else{
								data[j++]="";
							}
						}
					}
				/*	//副檔
					data[27] = "";
					data[28] = "";
					data[29] = "";
					data[30] = "";
					data[31] = "";
					data[32] = "";
					data[33] = "";*/
					//RESOURCES_ID
					data[27] = (String) objTemp[28];
					//LAST_UPDATE_DATE					
				try {
					data[28] = dateFormat.format(objTemp[29]);
				} catch (Exception e) {
					data[28] = null;
				}
					dbList.add(data);
				//為網路資源的集合填充數據
				}else if("WS".equals(rsType)){
					String[] data = new String[18];
					for(int j = 0;j<data.length-2;){
						//data[j++] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						//起訖日期
						if(j != 5 && j!= 6)
							//data[j++] = rs.getString(j) == null ? "" : rs.getString(j);
							data[j++] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						else{
							if(objTemp[j]!=null){
								data[j++] = dateFormat.format(objTemp[j]);
							}else{
								data[j++]="";
							}
						}
					}
				/*	//副檔
					data[16] = "";
					data[17] = "";
					data[18] = "";
					data[19] = "";
					data[20] = "";
					data[21] = "";
					data[22] = "";
					data[23] = "";*/
					//RESOURCES_ID
					data[16] = (String) objTemp[17];
					//LAST_UPDATE_DATE
					try {
						data[17] = dateFormat.format(objTemp[18]);
					} catch (Exception e) {
						data[17] = null;
					}
					dbList.add(data);
				//為其他的集合填充數據
				}else{
					String[] data = new String[18];
					for(int j = 0;j<data.length-10;){
						//data[j++] = (String) (objTemp[j] == null ? "" : objTemp[j]);
						//起訖日期
						if(j != 5 && j!= 6)
							//data[j++] = rs.getString(j) == null ? "" : rs.getString(j);
							data[j++] =  (String) (objTemp[j] == null ? "" :objTemp[j]);
						else{
							if( objTemp[j]!=null){
								data[j++] = dateFormat.format( objTemp[j]);
							}else{
								data[j++]="";
							}
						}
					}
				/*	//副檔
					data[16] = "";
					data[17] = "";
					data[18] = "";
					data[19] = "";
					data[20] = "";
					data[21] = "";
					data[22] = "";
					data[23] = "";*/
					//RESOURCES_ID
					data[16] = (String) objTemp[17];
					//LAST_UPDATE_DATE
					data[17] = dateFormat.format(objTemp[18]);
					dbList.add(data);
				}
			
			}
			/*ps = conn.prepareStatement(sql); 
			rs = ps.executeQuery(); 
			while (rs.next()) {}*/ 
		}catch(Exception e){ 
			errMsg = "數據庫讀取錯誤:"+e.getMessage();//數據庫讀取錯誤
			log.error(e.getMessage(),e);
			return false;
		} finally { 
			/*try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
			}*/
		}
		/*try {
			conn.close();
		} catch (SQLException e) {
		}*/
		// 如果沒有關閉connFactory
		/*if (connFactory != null) {
			connFactory.closeConn(conn);
			conn = null;
			connFactory = null;
		}*/
		//分為已存在、未存在
		for(int i = 0;i<excelDataList.size();i++){
			boolean flag = false;			
			if("DB".equals(rsType)){
				//電子資料庫
				for(int j = 0;j<dbList.size();j++){
					//比對題名，出版商
					if(excelDataList.get(i)[0].trim().equals(dbList.get(j)[0].trim()) 
							&& excelDataList.get(i)[14].trim().equals(dbList.get(j)[14].trim())){
						//已存在
						existList.add(excelDataList.get(i));
						existDBList.add(dbList.get(j));
						flag = true;
						continue;
					}
				}
			}else if("EJ".equals(rsType)){
				//電子期刊
				for(int j = 0;j<dbList.size();j++){
					//比對題名，所屬資料庫
					if(excelDataList.get(i)[0].trim().equals(dbList.get(j)[0].trim()) 
							&& excelDataList.get(i)[17].trim().equals(dbList.get(j)[17].trim())){
						//已存在
						existList.add(excelDataList.get(i));
						existDBList.add(dbList.get(j));
						flag = true;
						continue;
					}
				}
			}else if("EB".equals(rsType)){
				//電子書
				for(int j = 0;j<dbList.size();j++){
					//比對題名，所屬資料庫
					if(excelDataList.get(i)[0].trim().equals(dbList.get(j)[0].trim())
							&& excelDataList.get(i)[16].trim().equals(dbList.get(j)[16].trim())){
						//已存在
						existList.add(excelDataList.get(i));
						existDBList.add(dbList.get(j));
						flag = true;
						continue;
					}
				}
			}else if("WS".equals(rsType)){
				//網路資源
				for(int j = 0;j<dbList.size();j++){
					//比對題名，出版商
					if(excelDataList.get(i)[0].trim().equals(dbList.get(j)[0].trim()) 
							&& excelDataList.get(i)[4].trim().equals(dbList.get(j)[4].trim())){
						//已存在
						existList.add(excelDataList.get(i));
						existDBList.add(dbList.get(j));
						flag = true;
						continue;
					}
				}
			}else if("OT".equals(rsType)){
				//網路資源
				for(int j = 0;j<dbList.size();j++){
					//比對題名，出版商
					if(excelDataList.get(i)[0].trim().equals(dbList.get(j)[0]) 
							&& excelDataList.get(i)[4].trim().equals(dbList.get(j)[4])){
						//已存在
						existList.add(excelDataList.get(i));
						existDBList.add(dbList.get(j));
						flag = true;
						continue;
					}
				}
			}
			//未存在
			if(!flag){
				nExistList.add(excelDataList.get(i));
			}
		}
		//TODO 顯示已存在
		return true;
	}
	public void setDbfilepath(String dbfilepath) {
		this.dbfilepath = dbfilepath;
	}
	public String getEjfilepath() {
		return ejfilepath;
	}

	public void setEjfilepath(String ejfilepath) {
		this.ejfilepath = ejfilepath;
	}

	public String getEbfilepath() {
		return ebfilepath;
	}

	public void setEbfilepath(String ebfilepath) {
		this.ebfilepath = ebfilepath;
	}

	public String getWsfilepath() {
		return wsfilepath;
	}

	public void setWsfilepath(String wsfilepath) {
		this.wsfilepath = wsfilepath;
	}

/*	public String getOtfilepath() {
		return otfilepath;
	}

	public void setOtfilepath(String otfilepath) {
		this.otfilepath = otfilepath;
	}*/
	@Listen("onUpload=#uploadFile")
	public void uploadFile(UploadEvent event) {
		try {
			Media media = event.getMedia();
			int size = media.getByteData().length;
			String oldUpStr = uploadFileNameTxt.getValue();
			String hzStr = oldUpStr.substring(oldUpStr.lastIndexOf(".") + 1);
			//上傳提示
			String uploadMessage=Labels.getLabel("resources_rsbatch.uploadMessage");
			if (hzStr.equals("xls") || hzStr.equals("xlsx")) {
				/*// 獲取文件名及文件後綴名
				String filename = ((Media) media).getName();
				// 獲取下拉列表中的資源名稱
				String resource_name = comb_resource_type.getSelectedItem()
						.getValue();
				String[] file1=filename.split("[.]");
				if(!file1[0].equals(resource_name)){
	            	try {
						Messagebox.show("上傳的文件類型與當前選中的類型不匹配,請重新上傳", "上傳提示", Messagebox.OK,
								Messagebox.EXCLAMATION);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
		        }*/
				if (size <= 10485760) {
					if (media != null) {
						if (media.isBinary()) {
							Desktop dtp = Executions.getCurrent().getDesktop();
							String realPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							InputStream in = media.getStreamData();
							String oldFileName = media.getName();
							int suffixNameIndex = oldFileName.lastIndexOf(".");
							String suffixName = oldFileName
									.substring(suffixNameIndex);
							fileName = RandomIDGenerator.getRandomId()
									+ suffixName; /*
												 * System.currentTimeMillis() +
												 * "_" +
												 * uploadFileNameTxt.getValue();
												 */
							File file = new File(realPath + fileName);
							uploadFileNameTxt.setValue(media.getName());
//							uploadFile.setVisible(false);
//							delFile.setVisible(true);
							readExcel(in);
							if (!("").equals(errMsg)) {
								Messagebox.show(errMsg, uploadMessage, Messagebox.OK,
										Messagebox.EXCLAMATION);//"上傳提示"
								errMsg="";
								return;
							}
							//Files.copy(file, in);
							//Files.close(in);
							//in.close();
						} else {
							Desktop dtp = Executions.getCurrent().getDesktop();
							// String realPath = ((SystemProperties) SpringUtil
							// .getBean("systemProperties")).webEduTrainingDocementPath
							// + "/";
							InputStream in = media.getStreamData();
							String realPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ "/"
									+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							// System.out.println(realPath);
							File file = new File(realPath);
							String sr = media.getStringData();
							/*
							 * String fileName = System.currentTimeMillis() +
							 * "_" + media.getName().substring(0,
							 * media.getName().indexOf("."));
							 */
							String type = uploadFileNameTxt.getValue();
							type = type.substring(type.lastIndexOf("."));
							File files = File.createTempFile(fileName, type,
									file);
							fileName = files.getName();
							uploadFileNameTxt.setValue(media.getName());
//							uploadFile.setVisible(false);
//							delFile.setVisible(true);
							readExcel(in);
							//FileWriter fw = new FileWriter(files);
							//fw.write(sr);
							//fw.close();
						}
					}
				} else {
					ZkUtils.showExclamation("檔大小不能超過10M",
							Labels.getLabel("warn"));
					if (uploadFileName != null && !"".equals(uploadFileName)) {
						uploadFileNameTxt.setValue(uploadFileName);
					} else {
						uploadFileNameTxt.setValue("");
					}
					uploadFileNameTxt.setValue("");
					return;
				}
			} else {
				ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"),
						Labels.getLabel("warn"));
				if (uploadFileName != null && !"".equals(uploadFileName)) {
					uploadFileNameTxt.setValue(uploadFileName);
				} else {
					uploadFileNameTxt.setValue("");
				}
				uploadFileNameTxt.setValue("");
//				delFile.setVisible(false);
//				uploadFile.setVisible(true);
				return;
			}
		} catch (Exception e) {
			log.error("下載檔異常" + e);
		}
	//	Map<String, Object> map = new HashMap<String, Object>();
		Session session=Sessions.getCurrent();
		//已存在數據
		if(existList!=null && existList.size()>0){
			session.setAttribute("existList", existList);
		}else{
			if(session.getAttribute("existList")!=null){//如果不為空則清空session
				session.removeAttribute("existList");
			}
		}
		//數據庫中已存在的數據
		if(existDBList!=null && existDBList.size()>0){
			session.setAttribute("existDBList", existDBList);
		}else{
			if(session.getAttribute("existDBList")!=null){//如果不為空則清空session
				session.removeAttribute("existDBList");
			}
		}
		//未存在數據
		if(nExistList!=null && nExistList.size()>0){
			session.setAttribute("nExistList", nExistList);
		}else{
			if(session.getAttribute("nExistList")!=null){//如果不為空則清空session
				session.removeAttribute("nExistList");
			}
		}
		//錯誤數據
		if(session.getAttribute("errMap")!=null){//如果不為空則清空session
			session.removeAttribute("errMap");
		}
		//(已存在)處理後的數據如果存在（定義在Resources_upload_result類中）
		if(session.getAttribute("doAfterExistData")!=null){//如果不為空則清空session
			session.removeAttribute("doAfterExistData");
		}
		//(未存在)處理後的數據如果存在（定義在Resources_upload_result類中）
		if(session.getAttribute("doAfterNoExistData")!=null){//如果不為空則清空session
			session.removeAttribute("doAfterNoExistData");
		}
		//combobox中選中的值（定義在Resources_upload_result類中）
		if(session.getAttribute("selectedName")!=null){//如果不為空則清空session
			session.removeAttribute("selectedName");
		}
		//電子資料庫類型id和類型name
		String resources_type_id=comb_resource_type.getSelectedItem().getValue().toString();
		String resources_type_name=comb_resource_type.getSelectedItem().getLabel();
		//map.put("comb_resource_type", resources_type_id);
		session.setAttribute("comb_resource_type", resources_type_id);
	//	map.put("resources_type_name", resources_type_name);
		session.setAttribute("resources_type_name", resources_type_name);
		//頁面跳轉
		//Executions.sendRedirect("/resources_upload_result.jsf");
		//Executions.sendRedirect("/WEB-INF/pages/system/resourcesRsbatch/resourcesUploadResult.zul");
		 String url = "resourcesRsbatch/resourcesUploadResult.zul";
		 ZkUtils.refurbishMethod(url);
		//Executions.createComponents("/WEB-INF/pages/system/resourcesRsbatch/resourcesUploadResult.zul",this.getSelf(), map);
	}

}
