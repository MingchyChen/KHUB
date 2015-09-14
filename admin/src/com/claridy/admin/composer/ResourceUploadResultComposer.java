package com.claridy.admin.composer;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.logging.Log;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmResMainDbwsService;
import com.claridy.facade.ErmResMainEjebService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;
import com.claridy.facade.ResourcesValidate;
import com.claridy.facade.WebSysLogService;



public class ResourceUploadResultComposer extends SelectorComposer<Component>{
	@Wire
	private Label path_label;//頁面路徑
	@Wire
	private Label allResources_label ;//上傳數據的總筆數
	@Wire
	private Label existResources_label;//已存在數據的筆數
	@Wire
	private Label noExistResources_label;//未存在數據的筆數
	@Wire
	private Combobox resources_cobobox;//下拉列表選項
	@Wire
	private Grid resources_grid;//grid
	@Wire
	private Button replace_button;//取代
	@Wire
	private Button insert_button;//存為新紀錄
	@Wire
	private Window win;
	@Wire
	private Label warn1;//共上傳
	@Wire
	private Label warn2;//筆資料
	@Wire
	private Label warn3;//筆已存在
	@Wire
	private Label warn4;//筆不存在
	@Wire
	private Label warn5;//篩選條件
	private String resources_code_id="";//資源類型id
	private int validate_num=0;//全局變量，記錄是數據庫數據還是錯誤數據（validate方法中使用）
	String[] Array_value=null;//存放選中的資源類型屬性的數組
	int value_index=0;//記錄erm_resources_mainfile中的字段的個數

	private List<String[]> existList = new ArrayList<String[]>();// 已存在數據
	private List<String[]> existDBList = new ArrayList<String[]>();// 數據庫中已存在數據
	private List<String[]> nExistList = new ArrayList<String[]>();// 未存在數據
	private Map<StringBuffer,String[]> errMap = null;//錯誤數據
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());
	//多語系
	private String warnMessage,isSavaNew,qidingDate,message1,message2,message3,message4,chooseInsteadData,sameUrl,sameIp,pure,access,place,subject,language,suitCollage,colleage,dailishang,publisher,resourcesDB,formaterror,noData;
	
	
	/**
	 * 加載數據
	 */
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		Map<String, Object> map = new HashMap<String, Object>();
		map = ZkUtils.getExecutionArgs();
		webEmployee = (WebEmployee) ZkUtils
				.getSessionAttribute("webEmployee");
		//得到當前語系
	/*	Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
		Locales.setThreadLocal(locale);*/
		languageLoad();
		path_label.setValue(Labels.getLabel("resources_upload_result.pageTitle"));//電子資源管理>>批次匯入>>比對結果
		int existNum=0;//記錄已存在數據的筆數
		int nExistNum=0;//記錄未存在數據的筆數
		Session session=Sessions.getCurrent();
		resources_code_id=(String) session.getAttribute("comb_resource_type");
		//已存在數據doAfterExistData
		if(session.getAttribute("doAfterExistData")!=null){
			existList=(List<String[]>)session.getAttribute("doAfterExistData");
			existNum=existList.size()/2;
		}
		else if(session.getAttribute("existList")!=null){
			existList=(List<String[]>)session.getAttribute("existList");
			existNum=existList.size();
		}
		//數據庫中已存在的數據
		if(session.getAttribute("existDBList")!=null){
			existDBList=(List<String[]>)session.getAttribute("existDBList");
		}
		//未存在數據
		if(session.getAttribute("doAfterNoExistData")!=null){
			nExistList=(List<String[]>)session.getAttribute("doAfterNoExistData");
			nExistNum=nExistList.size();
		}
		else if(session.getAttribute("nExistList")!=null){
			nExistList=(List<String[]>)session.getAttribute("nExistList");
			nExistNum=nExistList.size();
		}
		//資源類型id
		if(	session.getAttribute("comb_resource_type")!=null){
			//resources_code_id=map.get("comb_resource_type").toString();
			resources_code_id=(String) Sessions.getCurrent().getAttribute("comb_resource_type");
		}
		
		
		//加載combobox
		for(int i=0;i<2;i++){
			Comboitem combitem=new Comboitem();
			if(i==0){
			    combitem.setLabel(Labels.getLabel("resources_upload_result.isHere"));//已存在
			    combitem.setValue("exist");
			}else{
				combitem.setLabel(Labels.getLabel("resources_upload_result.notIsHearre"));//未存在
				 combitem.setValue("noExist");
			}
			resources_cobobox.appendChild(combitem);
		}
		int comb_index=0;
		//已存在、未存在
		if(map.get("selectedName")!=null){
			String selectedLabel=map.get("selectedName").toString();
			if(selectedLabel.equals("noExist")){
				comb_index=1;
			    replace_button.setVisible(false);//如果是未存在，則取代功能按鈕隱藏
				
			}
		}
		resources_cobobox.setSelectedIndex(comb_index);
		warn1.setValue(Labels.getLabel("resources_upload_result.warn1mes"));
		warn2.setValue(Labels.getLabel("resources_upload_result.warn2mes"));
		warn3.setValue(Labels.getLabel("resources_upload_result.warn3mes"));
		warn4.setValue(Labels.getLabel("resources_upload_result.warn4mes"));
		warn5.setValue(Labels.getLabel("resources_upload_result.warn5mes"));
		//取代
		replace_button.setLabel(Labels.getLabel("resources_upload_result.instead"));//取代
		//存為新紀錄
		insert_button.setLabel(Labels.getLabel("resources_upload_result.saveNew"));//存為新紀錄
		//已存在Label
		existResources_label.setValue(String.valueOf(existNum));
		existResources_label.setStyle("color:red;");
		//未存在Label
		noExistResources_label.setValue(String.valueOf(nExistNum));
		noExistResources_label.setStyle("color:red;");
		//所有筆數顯示Label
		allResources_label.setValue(String.valueOf(existNum+nExistNum));
		allResources_label.setStyle("color:red;");
		//調用加載grid的方法
		changeGrid();
	}
	
	/**
	 * 多語系
	 */
	public void languageLoad(){
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		warnMessage=Labels.getLabel("resources_upload_result.warnMessage");//提示信息
		qidingDate=Labels.getLabel("resources_upload_result.qidingDate");//起訂日期
		isSavaNew=Labels.getLabel("resources_upload_result.isSavaNew");//是否確定存為新記錄
		message1=Labels.getLabel("resources_upload_result.message1");//是否確定取代記錄
		message2=Labels.getLabel("resources_upload_result.message2");//起訂日期格式不正確
		message3=Labels.getLabel("resources_upload_result.message3");//起訂日期不能大於訖訂日期格
		message4=Labels.getLabel("resources_upload_result.message4");//請選擇要存為新紀錄的數據
		chooseInsteadData=Labels.getLabel("resources_upload_result.chooseInsteadData");//請選擇要取代的數據
		sameUrl=Labels.getLabel("resources_upload_result.sameUrl");//相關url
		sameIp=Labels.getLabel("resources_upload_result.sameIp");//合法ip
		pure=Labels.getLabel("resources_upload_result.pure");//採購註記
		access=Labels.getLabel("resources_upload_result.access");//連線方式
		place=Labels.getLabel("resources_upload_result.place");//存放地點
		subject=Labels.getLabel("resources_upload_result.subject");//主題表
		language=Labels.getLabel("resources_upload_result.language");//語言
		suitCollage=Labels.getLabel("resources_upload_result.suitCollage");//適用學院
		colleage=Labels.getLabel("resources_upload_result.colleage");//訂購學院
		dailishang=Labels.getLabel("resources_upload_result.dailishang");//代理商
		publisher=Labels.getLabel("resources_upload_result.publisher");//出版商
		resourcesDB=Labels.getLabel("resources_upload_result.resourcesDB");//所屬資料庫
		formaterror=Labels.getLabel("resources_upload_result.formaterror");//格式不對
		noData=Labels.getLabel("resources_upload_result.noData");//查無資料
	}
	
	
	/**
	 * grid加載數據
	 */
	@SuppressWarnings("unchecked")
	public void changeGrid(){
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		//如果grid中有值，就清空grid
		Session session=Sessions.getCurrent();
		Map<String, Object> map = new HashMap<String, Object>();
		if(resources_grid.getModel()!=null && resources_grid.getModel().getSize()>=0){
			for(int i=0;i<resources_grid.getChildren().size();i++){
				if(resources_grid.getChildren().get(i).getClass().toString().equals("class org.zkoss.zul.Columns")){		
					resources_grid.getChildren().remove(i);
					break;
				}
			}
		}
		String resources_tag=resources_cobobox.getSelectedItem().getValue().toString();
		List<String[]> list=new ArrayList<String[]>();
		String[][] models=null;//grid內容
		if("exist".equals(resources_tag)){
			if(map.get("doAfterExistData")!=null){
				list=(List<String[]>)map.get("doAfterExistData");
			}else{
			//將existList和existDBList中的數據放到一個集合中
			if(existList!=null && existList.size()>0 && existDBList!=null && existDBList.size()>0){
					for(int i=0;i<existDBList.size();i++){
						String[] existArray=existList.get(i);
						String[] DBListArray=existDBList.get(i);
						String[] newExistArray=new String[DBListArray.length];
						String[] newDBArray=new String[DBListArray.length];
						for(int j=0;j<DBListArray.length;j++){
							if(j==0 || j==(DBListArray.length-1)){
								newExistArray[j]="";
							}else{
								newExistArray[j]=existArray[j-1];
							}
							if(j==0){
								newDBArray[j]=DBListArray[DBListArray.length-2];
							}else if(j==(DBListArray.length-1)){
								newDBArray[j]=DBListArray[DBListArray.length-1];
							}else{
							newDBArray[j]=DBListArray[j-1];
							}
						}
						list.add(newDBArray);
						list.add(newExistArray);	
				}
			}
			}	
		}else if("noExist".equals(resources_tag)){
		
			if(map.get("doAfterNoExistData")!=null){
				list=(List<String[]>)map.get("doAfterNoExistData");
			}else{
			if(nExistList!=null && nExistList.size()>0){		
				list=nExistList;
			}
		}
			 
		}
		int index=addGridTitle(resources_tag);//添加grid表頭
		//將List中的數據進行處理返回二維數組
		models=doList(list);
		//判斷  ：已存在、未存在狀態
		//map.put("yes_no_exist_status",resources_tag);
		session.setAttribute("yes_no_exist_status", resources_tag);
		if(models==null){
			replace_button.setDisabled(true);//取代
			insert_button.setDisabled(true);
			models=new String[0][0];
		}else{
			replace_button.setDisabled(false);//取代
			insert_button.setDisabled(false);
		}
		
		//win.setWidth(String.valueOf((100*index))+"px");
		ListModel listModel=new SimpleListModel(models);//將二維數組存到ListModel中
		resources_grid.setModel(listModel);//將ListModel放到grid中
		resources_grid.setInnerWidth(String.valueOf((100*index))+"px");
		resources_grid.setRowRenderer(new Resources_upload_result_Renderer());
		resources_grid.setMold("paging");
		resources_grid.setPageSize(1000);//每頁顯示幾條記錄
		
	}
	/**
	 * 處理未存在、已存在的集合
	 */
	public String[][] doList(List<String[]> list){
		String[][] model=null;
		if(list!=null && list.size()>0 ){//判斷集合不為空
		 model=new String[list.size()][list.get(0).length];
		for(int i=0;i<list.size();i++){
			String[] resources_data=(String[])list.get(i);
			for(int j=0;j<resources_data.length;j++){
				model[i][j]=resources_data[j]==null?"":String.valueOf(resources_data[j]);
			}	
		}
		}
		return model;
	}
/**
 * 添加grid頭部信息
 * 
 */
	public int addGridTitle(String resources_tag){
		//得到當前語系
//		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		//電子資源庫
		//題名,url,相關url,採購註記,起訂日期,迄訂日期,收錄年代,合法ip,帳號與密碼,其他資訊,資源簡述摘要,資源簡述摘要(英文),語言,代理商,出版商,更新頻率/刊期,使用說明,同時上線人數,連線方式,圖書館經費,存放地點,Domain name
		//題名,url,相關url,採購註記,起訂日期,迄訂日期,收錄年代,合法ip,帳號與密碼,其他資訊,資源簡述摘要,資源簡述摘要(英文),語言,更新頻率/刊期,核心,出版商,出版地,所屬資料庫,出版商首頁,ISSN(PRINTED),ISSN(ONLINE),embargo,館藏信息,圖書館經費,存放地點
		//題名,url,相關url,採購註記,起訂日期,迄訂日期,收錄年代,合法ip,帳號與密碼,其他資訊,資源簡述摘要,資源簡述摘要(英文),語言,核心,出版商,出版地,所屬資料庫,出版商首頁,ISBN(PRINTED),ISBN(ONLINE),embargo,電子書分類號,電子書索書號,圖書館經費,作者,版本,存放地點
		//題名,url,相關url,採購註記,出版商,起訂日期,迄訂日期,收錄年代,合法ip,帳號與密碼,其他資訊,資源簡述摘要,資源簡述摘要(英文),語言,代理商,版本
		//題名,url",相關url,採購註記,出版商,起訂日期,迄訂日期,收錄年代,合法ip,帳號與密碼,其他資訊,資源簡述摘要,資源簡述摘要(英文),語言,代理商,版本
		String dbNames=Labels.getLabel("resources_upload_result.dbNames");
		String ejNames=Labels.getLabel("resources_upload_result.ejNames");
		String ebNames=Labels.getLabel("resources_upload_result.ebNames");
		String wsNames=Labels.getLabel("resources_upload_result.wsNames");
		String otNames=Labels.getLabel("resources_upload_result.otNames");
		String[] dbName=dbNames.split(",");
		//電子期刊
		String[] ejName=ejNames.split(",");
		//電子書
		String[] ebName=ebNames.split(",");
		//網絡資源
		String[] wsName=wsNames.split(",");
		//其他
		String[] otName=otNames.split(",");
		
		/*	String[] dbName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","出版商","更新頻率/刊期","使用說明","同時上線人數","連線方式","圖書館經費","存放地點","Domain name"};
		//電子期刊
		String[] ejName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","更新頻率/刊期","核心","出版商","出版地","所屬資料庫","出版商首頁","ISSN(PRINTED)","ISSN(ONLINE)","embargo","館藏信息","圖書館經費","存放地點"};
		//電子書
		String[] ebName={"題名","url","相關url","採購註記","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","核心","出版商","出版地","所屬資料庫","出版商首頁","ISBN(PRINTED)","ISBN(ONLINE)","embargo","電子書分類號","電子書索書號","圖書館經費","作者","版本","存放地點"};
		//網絡資源
		String[] wsName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
		//其他
		String[] otName={"題名","url","相關url","採購註記","出版商","起訂日期","迄訂日期","收錄年代","合法ip","帳號與密碼","其他資訊","資源簡述摘要","資源簡述摘要(英文)","語言","代理商","版本"};
		*/
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
		int index=gridTitle.length+3;
		Columns columns=new Columns();
		//如果是已存在，加載已存在的columns
		if("exist".equals(resources_tag)){
			
		for(int i=0;i<index;i++){
			Column column=new Column();
			if(i==0){
				Checkbox ck=new Checkbox();
				ck.setLabel(Labels.getLabel("resources_upload_result.allChoose"));//全選
				ck.addEventListener(Events.ON_CHECK, new Resources_upload_result_onSelected());
				column.appendChild(ck);
			}else if(i==1){
				column.setLabel(Labels.getLabel("resources_upload_result.resourceType"));//資源類型
			}else{
				if(i==index-1){
					column.setLabel(Labels.getLabel("resources_upload_result.lastUpdateDate"));//最後更新日期
				}else{
				column.setLabel(gridTitle[i-2]);
				}
			}
			column.setWidth("100px");
			columns.appendChild(column);
		}
		}else if("noExist".equals(resources_tag)){//如果是未存在，加載未存在的columns
			for(int i=0;i<index;i++){
				Column column=new Column();
				if(i==0){
					Checkbox ck=new Checkbox();
					ck.setLabel(Labels.getLabel("resources_upload_result.allChoose"));//全選
					ck.addEventListener(Events.ON_CHECK, new Resources_upload_result_onSelected());
					column.appendChild(ck);
				}else if(i==1){
					column.setLabel(Labels.getLabel("resources_upload_result.biduiResult"));//比對結果
				}else if(i==2){
					column.setLabel(Labels.getLabel("resources_upload_result.resourceType"));//資源類型
				}else{
					column.setLabel(gridTitle[i-3]);
				}
				column.setWidth("100px");
				columns.appendChild(column);
			}
		}
		resources_grid.appendChild(columns);
		return index;
	}
	/**
	 * 下拉列表變動事件
	 */
	@Listen("onChange=#resources_cobobox")
	public void onChange$resources_cobobox(){
		String selectedName=resources_cobobox.getSelectedItem().getValue().toString();
		if("exist".equals(selectedName)){
			replace_button.setVisible(true);
		}else if("noExist".equals(selectedName)){
			replace_button.setVisible(false);
		}
		changeGrid();
	}
	/**
	 * 取代
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * 
	 */
	@Listen("onClick=#replace_button")
	public void onClick$replace_button() throws SQLException, ParseException, InterruptedException{
		Session session=Sessions.getCurrent();
		webEmployee = (WebEmployee) ZkUtils
				.getSessionAttribute("webEmployee");
		boolean isDo=false;
        /*int number=Messagebox.show(message1,warnMessage,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);//"是否確定取代記錄？","消息提示"
		if(number==Messagebox.OK){
			isDo=true;
		}*/
		isDo=true;
		if(isDo){
		// 連接數據庫
//		UbictechConnectFactory connFactory = new UbictechConnectFactory();
//		Connection conn = connFactory.getUbictechConn();
		try{
		int successNumber=0;//記錄數據處理成功的筆數
		//清空錯誤數據的session
		if(session.getAttribute("errMap")!=null){
			session.removeAttribute("errMap");
		}
		errMap=new LinkedHashMap<StringBuffer,String[]>();//實例化errMap
		publicMethod();//數據的“取代”、"存為新紀錄”的公共方法
		//boolean mark_index=true;//判斷數據是否有錯誤：有則返回false,沒有就返回true
		//判斷Grid中是否有數據
		if(resources_grid.getChildren()!=null && resources_grid.getChildren().size()>0){
			List grid_list=resources_grid.getChildren();
			ListModel listModel=resources_grid.getModel();
			String[][] model=null;
			Columns columns=null;
			for(int i=0;i<grid_list.size();i++){
				//判斷是否是Columns控件
				if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Columns")){
					columns=(Columns)grid_list.get(i);	
					break;
				}
				
			}
			
			//循環Grid
			for(int i=0;i<grid_list.size();i++){
				//如果是Rows
				if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Rows")){
					Rows rows=(Rows)grid_list.get(i);
					List<String[]> afterDataList=new ArrayList<String[]>();//記錄處理後剩下的數據
					int isChecked=0;//記錄勾選的checkbox數目
					//循環Rows內所有的Row
					for(int j=0;j<rows.getChildren().size();j++){
						Row row=(Row)rows.getChildren().get(j);
						//如果是checkbox
						if(row.getFirstChild().getClass().toString().equals("class org.zkoss.zul.Checkbox")){
							Checkbox ckbox=(Checkbox)row.getFirstChild();
							if(ckbox.isChecked()){//判斷checkbox是否被選中
								isChecked++;
								String resources_id="";//資源類型id
								String[] last_row_data=(String[])listModel.getElementAt(j-1);//取到上一行中的第一格數據（resources_id）
								resources_id=last_row_data[0];
								String[] row_data=(String[])listModel.getElementAt(j);//取得checkbox選中的行的數據
								//根據resources_id(pk)獲取一筆數據
								
								//TODO 表需要改IOerm_resources_mainfile改為另3張表IOerm_resources_main_dbws,IOerm_resources_main_ejeb,IOerm_resources_ejeb_item
								if(("DB").equals(resources_code_id) || ("WS").equals(resources_code_id)){
//								IOerm_resources_main_dbws resources_mainfile=new IOerm_resources_main_dbws();
//								resources_mainfile=new IOerm_resources_main_dbws().selectOne(connFactory, conn, resources_id);
								ErmResourcesMainDbws resources_mainfile=new ErmResourcesMainDbws();
								resources_mainfile=((ErmResMainDbwsService) SpringUtil
										.getBean("ermResMainDbwsService")).getResMainDbwsByResId(resources_id);
								StringBuffer sb=new StringBuffer();//存儲錯誤信息
								String[] obj=new String[row_data.length-2];
								for(int k=0;k<row_data.length;k++){
									if(k>1){
										String value=row_data[k-1];//每個單元格內的數據
										obj[k-2]=value;
										Column column=(Column)columns.getChildren().get(k);//獲取每一個單元格所對應的Column
										String label_value=column.getLabel();//獲取Column內Label內的值
										//調用驗證方法驗證數據
										if(!value.equals("")){
                                       String result=validate(label_value,value);
                                       if(validate_num==1){//validate_num==1表示數據有誤
                                    	   sb.append(result);
                                    	   sb.append("\n"); 
                                       }else if(validate_num==2){//validate_num==2表示數據正常，並將需要的數據存到數組中
                                    	   obj[k-2]=result;
                                       }
										}
										if(qidingDate.equals(label_value.trim())){//起訂日期
											//如果起訂日期和訖訂日期不為空
											if(!value.toString().equals("") && !row_data[k].toString().equals("")){
												//Resources_validate validate=new Resources_validate();
												ResourcesValidate validate=((ResourcesValidate) SpringUtil
														.getBean("resourcesValidate"));
												if(!validate.isdateFormat(value)){
													sb.append(message2);//起訂日期格式不正確
													sb.append("\n");
												}
												if(!validate.isdateFormat(row_data[k].toString())){
													sb.append(message2);//起訂日期格式不正確
													sb.append("\n");
												}
												//起訂日期和訖訂日期的格式都正確時
												if(validate.isdateFormat(value.trim()) && validate.isdateFormat(row_data[k].trim())){
													boolean bool=validate.isdate(value, row_data[k]);
													if(!bool){
														sb.append(message3);//起訂日期不能大於訖訂日期格
														sb.append("\n");
													}
												}
											}
										}
									}
								}
								//如果有錯誤信息
								if(!sb.toString().equals("")){
									errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
								}else{
									//否則執行數據庫操作
									Class clas=resources_mainfile.getClass();
									for(int m=0;m<value_index;m++){
									try {
										PropertyDescriptor pd=new PropertyDescriptor(Array_value[m],clas);
										Method method=pd.getWriteMethod();
										String type=pd.getPropertyType().getName();
										try {
											if(type.equals("int")){//如果此屬性是int類型
												method.invoke(resources_mainfile, Integer.parseInt(obj[m]));
											}else if(type.equals("java.util.Date")){//如果此屬性是Date類型
												if(obj[m]!=null && !obj[m].equals("") && !obj[m].equalsIgnoreCase("null")){
													DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
													Date date=df.parse(obj[m]);
													method.invoke(resources_mainfile,date);
												}
											}else if(type.equals("double")){
												if(obj[m]!=null && !obj[m].equals("")){
													method.invoke(resources_mainfile, Double.parseDouble(obj[m]));
												}
											}else{
												if(obj[m]!=null && !obj[m].equals("")){
													method.invoke(resources_mainfile, obj[m]);
												}
												
											}
											
										} catch (IllegalArgumentException e) {
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											e.printStackTrace();
										}
									} catch (IntrospectionException e) {
										e.printStackTrace();
									}
									}
									//更新數據庫
								//	boolean bool=resources_mainfile.update(connFactory, conn);
									resources_mainfile.setWebEmployee(webEmployee);
									resources_mainfile.setCreateDate(new Date());
									try {
										((ErmResMainDbwsService) SpringUtil
												.getBean("ermResMainDbwsService")).UpdResMainDbws(resources_mainfile);
										((ResourcesMainDbwsSolrSearch) SpringUtil
												.getBean("resourcesMainDbwsSolrSearch"))
												.resources_main_dbws_editData(resources_mainfile
														.getResourcesId());
										successNumber++;
									} catch (Exception e) {
										log.error("取代電子資源異常"+e);
									}
									((WebSysLogService) SpringUtil.getBean("webSysLogService"))
									.insertLog(ZkUtils.getRemoteAddr(),
											webEmployee.getEmployeesn(), "resourcesmaindbws_"
													+ resources_mainfile.getResourcesId());
//									//如果更新成功則向Log表中增加一條日誌
//									if(bool){
//										successNumber++;
//										resources_mainfile.save_log(connFactory, conn,resources_mainfile, "U");
//										conn.commit();
//										//講數據添加到solr中
//										//TODO Resources_mainfile_search表需要改
//										Resources_main_dbws_search search=new Resources_main_dbws_search();
//										search.resources_main_dbws_editData_add(resources_id);
//									}
									
								}
							}
								//-----------------------------------------------------------------------------------------------------------------------
								else if(("EJ").equals(resources_code_id) || ("EB").equals(resources_code_id)){
//									IOerm_resources_main_ejeb resources_main_ejeb=new IOerm_resources_main_ejeb();
//									IOerm_resources_ejeb_item resources_ejeb_item=new IOerm_resources_ejeb_item();
									ErmResourcesMainEjeb resources_main_ejeb=new ErmResourcesMainEjeb();
									ErmResourcesEjebItem resources_ejeb_item=new ErmResourcesEjebItem();
									resources_main_ejeb=((ErmResMainEjebService) SpringUtil
											.getBean("ermResMainEjebService")).getResMainEjebByResId(resources_id);
									resources_ejeb_item=((ErmResMainEjebService) SpringUtil
											.getBean("ermResMainEjebService")).getResMainEjebItemByResId(resources_id);
//									resources_main_ejeb.setResources_id(resources_id);
//									resources_ejeb_item.setResources_id(resources_id);
									resources_main_ejeb.setResourcesId(resources_id);
									resources_ejeb_item.setResourcesId(resources_id);
									//resources_main_ejeb=new IOerm_resources_main_ejeb().selectOne(connFactory, conn, resources_id);										
									//resources_ejeb_item=new IOerm_resources_ejeb_item().selectOne(connFactory, conn, resources_id);										
									StringBuffer sb=new StringBuffer();//存儲錯誤信息
									String[] obj=new String[row_data.length-2];
									for(int k=0;k<row_data.length;k++){
										if(k>1){
											String value=row_data[k-1];//每個單元格內的數據
											obj[k-2]=value;
											Column column=(Column)columns.getChildren().get(k);//獲取每一個單元格所對應的Column
											String label_value=column.getLabel();//獲取Column內Label內的值
											//調用驗證方法驗證數據
											if(!value.equals("")){
												String result=validate(label_value,value);
												if(validate_num==1){//validate_num==1表示數據有誤
													sb.append(result);
													sb.append("\n"); 
												}else if(validate_num==2){//validate_num==2表示數據正常，並將需要的數據存到數組中
													obj[k-2]=result;
												}
											}
											if(qidingDate.equals(label_value.trim())){//起訂日期
												//如果起訂日期和訖訂日期不為空
												if(!value.toString().equals("") && !row_data[k].toString().equals("")){
													ResourcesValidate validate=new ResourcesValidate();
													if(!validate.isdateFormat(value)){
														sb.append(message2);//起訂日期格式不正確
														sb.append("\n");
													}
													if(!validate.isdateFormat(row_data[k].toString())){
														sb.append(message2);//起訂日期格式不正確
														sb.append("\n");
													}
													//起訂日期和訖訂日期的格式都正確時
													if(validate.isdateFormat(value.trim()) && validate.isdateFormat(row_data[k].trim())){
														boolean bool=validate.isdate(value, row_data[k]);
														if(!bool){
															sb.append(message3);//起訂日期不能大於訖訂日期格
															sb.append("\n");
														}
													}
													
													
												}
											}
										}
									}
									//如果有錯誤信息
									if(!sb.toString().equals("")){
										errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
									}else{
										//否則執行數據庫操作
										DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
										if(("EB").equals(resources_code_id)){
											
											//resources_ejeb_item.setDb_id(obj[16]);
											resources_ejeb_item.setDbId(obj[16]);
											resources_main_ejeb.setTitle(obj[0]);
											resources_main_ejeb.setBrief1(obj[10]);
											resources_main_ejeb.setBrief2(obj[11]);
											
										//	resources_main_ejeb.setLanguage_id(obj[12]);
											resources_main_ejeb.setLanguageId(obj[12]);
											resources_main_ejeb.setCore(obj[13]);
											resources_main_ejeb.setIsbnonline(obj[18]);
											resources_main_ejeb.setIsbnprinted(obj[19]);
											resources_main_ejeb.setCn(obj[21]);
											resources_main_ejeb.setCalln(obj[22]);
											resources_main_ejeb.setAuthor(obj[24]);
											
											resources_ejeb_item.setUrl1(obj[1]);
											resources_ejeb_item.setUrl2(obj[2]);
										//	resources_ejeb_item.setRemark_id(obj[3]);
											resources_ejeb_item.setRemarkId(obj[3]);
										//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
											resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
											if(obj[5]!=null&&!"".equals(obj[5])){
												//resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
												resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
											}
											resources_ejeb_item.setCoverage(obj[6]);
										//	resources_ejeb_item.setRegllyip(obj[7]);
											resources_ejeb_item.setRegllyIp(obj[7]);
											resources_ejeb_item.setIdpwd(obj[8]);
											resources_ejeb_item.setOthers(obj[9]);
										//	resources_ejeb_item.setPublisher_id(obj[14]);
											resources_ejeb_item.setPublisherId(obj[14]);
										//	resources_ejeb_item.setPubplace(obj[15]);
											resources_ejeb_item.setPubPlace(obj[15]);
										//	resources_ejeb_item.setPublisherurl(obj[17]);
											resources_ejeb_item.setPublisherUrl(obj[17]);
											if(obj[20] == null || "".equals(obj[20])){
												//resources_ejeb_item.setEmbargo(0);
											}else{
												//resources_ejeb_item.setEmbargo(Double.valueOf(obj[20]));
												resources_ejeb_item.setEmbargo(obj[20]);
											}
										//	resources_ejeb_item.setLibarymoney(obj[23]);
											resources_ejeb_item.setLibaryMoney(obj[23]);
											resources_ejeb_item.setVersion(obj[25]);
										//	resources_ejeb_item.setPlace_id(obj[26]);		
											resources_ejeb_item.setPlaceId(obj[26]);
										}
										else if (("EJ").equals(resources_code_id)){
										//	resources_ejeb_item.setDb_id(obj[17]);
											resources_ejeb_item.setDbId(obj[17]);
											resources_main_ejeb.setTitle(obj[0]);
											resources_main_ejeb.setBrief1(obj[10]);
											resources_main_ejeb.setBrief2(obj[11]);
										//	resources_main_ejeb.setLanguage_id(obj[12]);
											resources_main_ejeb.setLanguageId(obj[12]);
											resources_main_ejeb.setCore(obj[14]);
											resources_main_ejeb.setIssnonline(obj[19]);
											resources_main_ejeb.setIssnprinted(obj[20]);
											
											resources_ejeb_item.setUrl1(obj[1]);
											resources_ejeb_item.setUrl2(obj[2]);
										//	resources_ejeb_item.setRemark_id(obj[3]);
											resources_ejeb_item.setRemarkId(obj[13]);
										//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
											resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
											if(obj[5]!=null&&!"".equals(obj[5])){
												//resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
												resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
											}
											resources_ejeb_item.setCoverage(obj[6]);
										//	resources_ejeb_item.setRegllyip(obj[7]);
											resources_ejeb_item.setRegllyIp(obj[7]);
											resources_ejeb_item.setIdpwd(obj[8]);
											resources_ejeb_item.setOthers(obj[9]);
											resources_ejeb_item.setFrenquency(obj[13]);
										//	resources_ejeb_item.setPublisher_id(obj[15]);
											resources_ejeb_item.setPublisherId(obj[15]);
										//	resources_ejeb_item.setPubplace(obj[16]);
											resources_ejeb_item.setPubPlace(obj[16]);
										//	resources_ejeb_item.setPublisherurl(obj[18]);
											resources_ejeb_item.setPublisherUrl(obj[18]);
											//resources_ejeb_item.setEmbargo(Double.parseDouble(obj[21]));
											if(obj[21] == null || "".equals(obj[21])){
												//resources_ejeb_item.setEmbargo(0);
											}else{
											//	resources_ejeb_item.setEmbargo(Double.valueOf(obj[21]));
												resources_ejeb_item.setEmbargo(obj[21]);
											}
											resources_ejeb_item.setEholdings(obj[22]);
										//	resources_ejeb_item.setLibarymoney(obj[23]);
											resources_ejeb_item.setLibaryMoney(obj[23]);
										//	resources_ejeb_item.setPlace_id(obj[24]);
											resources_ejeb_item.setPlaceId(obj[24]);
										}
//										//更新數據庫
//										boolean bool=resources_main_ejeb.update(connFactory, conn);
//										boolean bool2=resources_ejeb_item.update(connFactory, conn);
//										//如果更新成功則向Log表中增加一條日誌
//										if(bool || bool2){
//											successNumber++;
//											resources_main_ejeb.save_log(connFactory, conn,resources_main_ejeb, "U");
//											resources_ejeb_item.save_log(connFactory, conn,resources_ejeb_item, "U");
//											conn.commit();
//											//講數據添加到solr中
//											//TODO Resources_mainfile_search表需要改
//											//	new Resources_mainfile_search().resources_mainfile_editData(resources_mainfile);
//											Resources_main_ejeb_search search = new Resources_main_ejeb_search();
//											search.resources_main_ejeb_editData(resources_id);
//										}
										resources_main_ejeb.setWebEmployee(webEmployee);
										resources_main_ejeb.setCreateDate(new Date());
										resources_ejeb_item.setWebEmployee(webEmployee);
										resources_ejeb_item.setCreateDate(new Date());
										((WebSysLogService) SpringUtil.getBean("webSysLogService"))
										.editLog(ZkUtils.getRemoteAddr(),
												webEmployee.getEmployeesn(), "resourcesmainejeb_"
														+ resources_main_ejeb.getResourcesId());
								try {
									((ErmResMainEjebService) SpringUtil
											.getBean("ermResMainEjebService")).updateResMainEjeb(
													resources_main_ejeb, resources_ejeb_item);
									((ResourcesMainEjebSolrSearch) SpringUtil
											.getBean("resourcesMainEjebSolrSearch"))
											.resources_main_ejeb_editData(resources_main_ejeb
													.getResourcesId());
									successNumber++;
								} catch (Exception e) {
									log.error("電子期刊新增異常"+e);
								}
									}
								}
								//-------------------------------------------------------------------------------------------------------
							}else{
								//記錄未被選中需要處理的數據並存儲到List中
								String[] lastArray=(String[])listModel.getElementAt(j-1);
								String[] array=(String[])listModel.getElementAt(j);
								afterDataList.add(lastArray);
								afterDataList.add(array);
							}
						}
					}
					
					//如果沒有選擇任何checkbox（即數據未做任何處理）
					if(isChecked==0){
						Messagebox.show(chooseInsteadData,warnMessage,Messagebox.OK,Messagebox.INFORMATION);//"請選擇要取代的數據","訊息提示"
					}else{//否則
						String selectedName=resources_cobobox.getSelectedItem().getValue().toString();//記錄combobox選中項
						session.setAttribute("selectedName", selectedName);
						session.setAttribute("isChecked", isChecked);
						//將未選中的數據存到session中，即使為null也儲存
						session.setAttribute("doAfterExistData", afterDataList);
						
						//將錯誤數據存到Session中
						if(errMap!=null && errMap.size()>0){
							session.setAttribute("errMap", errMap);
							session.setAttribute("successNumber", String.valueOf(successNumber));//處理成功的數據筆數
							//頁面跳轉
						//	Executions.sendRedirect("/resourcesRsbatchError.zul");
							String url="resourcesRsbatch/resourcesRsbatchError.zul";
							ZkUtils.refurbishMethod(url);	
						}else{//如果沒有則說明全部數據都處理成功
							session.setAttribute("successNumber", successNumber);//處理成功的數據筆數
							//Executions.sendRedirect("/resources_rsbatch_return.jsf");
							//Executions.sendRedirect("/resourcesRsbatchReturn.zul");
							String url="resourcesRsbatch/resourcesRsbatchReturn.zul";
							ZkUtils.refurbishMethod(url);	
						}
					}
				}
			}
		}
			}catch(Exception e){
				log.error("取代報錯",e);
			}
		}
		
	}
	
	/**
	 *  驗證數據格式是否正確
	 * @param label_value
	 * @param value
	 * @return
	 * @throws InterruptedException 
	 */
	public String validate(String label_value,String value) throws InterruptedException{
		validate_num=0;//初始化為0
		String result=null;
		//對Label所對應的單元格數據做驗證
		//ResourcesValidate validate=new ResourcesValidate();
		ResourcesValidate validate=((ResourcesValidate) SpringUtil.getBean("resourcesValidate"));
		//sameUrl="相關url",sameIp="合法ip"
		//"URL".equals(label_value) || sameUrl.equals(label_value) || sameIp.equals(label_value) || 
		if("ISBN(PRINTED)".equals(label_value) || "ISBN(ONLINE)".equals(label_value) || 
			"ISSN(PRINTED)".equals(label_value) || "ISSN(ONLINE)".equals(label_value)){
			/*if("URL".equals(label_value)){	//url
				if(!validate.checkUrl(value)){
					result="URL"+formaterror;//格式不對
				}
			}else if(sameUrl.equals(label_value)){//相關url
				if(!validate.checkUrl(value)){
					result="相關URL格式不對";	
				}
			}else if(sameIp.equals(label_value)){//合法ip
				if(!validate.checkIp(value)){
					result="IP"+formaterror;//格式不對		
				}
			}else */
			if("ISBN(PRINTED)".equals(label_value)){
				//ISBN(PRINTED)驗證
				//如果數據中有'-',則去除'-'
				/*
				if(label_value.contains("-")){
					value=label_value.replace("-", "");
				}*/
				if(!validate.checkIsbn(value)){
					result="ISBN(PRINTED)"+formaterror;	
				}	
			}else if("ISBN(ONLINE)".equals(label_value)){//ISBN(ONLINE)驗證
				
				if(!validate.checkIsbn(value)){
					result="ISBN(ONLINE)"+formaterror;//格式不對											
				}
			}else if("ISSN(PRINTED)".equals(label_value)){//ISSN(PRINTED)
	
				if(!validate.checkIssn(value)){
					result="ISSN(PRINTED)"+formaterror;	//格式不對	
				}
			}else if("ISSN(ONLINE)".equals(label_value)){//ISSN(ONLINE)
	
				if(!validate.checkIssn(value)){
					result="ISSN(ONLINE)"+formaterror;	//格式不對	
				}
			}
			if(result!=null){//如果不為null則說明驗證不通過，有錯誤存在
				validate_num=1;//數據有誤
			}
		//pure="採購註記",access="連線方式",place="存放地點",subject="主題表"
		}else if(pure.equals(label_value) || access.equals(label_value) || place.equals(label_value) || subject.equals(label_value)){
			 if(pure.equals(label_value)){
					validate.setItem_id("PURE");
				}else if(access.equals(label_value)){
					validate.setItem_id("ACCESS");
				}else if(place.equals(label_value)){
					validate.setItem_id("PLACE");
				}else if(subject.equals(label_value)){
					String type="";
					if(("DB").equals(resources_code_id)){
					 type="DBSUB";
					}else if(("EJ").equals(resources_code_id)){
						 type="EJSUB";
					}else if(("EB").equals(resources_code_id)){
						 type="EBSUB";
					}else if(("WS").equals(resources_code_id)){
						 type="WSSUB";
					}else if(("OT").equals(resources_code_id)){
						 type="OTSUB";
					}
					validate.setItem_id(type);
		        }
				result=validate.checkResources_subject(value);
				if(result!=null){//如果返回值不為空則說明驗證通過，返回ID
					validate_num=2;//數據正常
				}else{//否則說明沒有查到此筆數據
					validate_num=1;
					result=label_value+noData;//查無資料
				}
         }/*else if("電子資料庫類型".equals(label_value)){
        	
					validate.setItem_id("RETYPE");
					result=validate.checkResources_subject(value);
					if(result!=null){//如果返回值不為空則說明驗證通過，返回ID
						validate_num=2;//數據正常
					}else{//否則說明沒有查到此筆數據
						validate_num=1;
						result=label_value+"查無資料";
					}
         }*/
	//language="語言"
	else if(language.equals(label_value)){
    			for(int i=0;i<2;i++){
    				if(i==0 && "DB".equals(resources_code_id)){
    					validate.setItem_id("DBLAN");
    				}else if(i == 1 && !"DB".equals(resources_code_id)){
    					validate.setItem_id("RELAN");
    				}
//					if(("EJ").equals(resources_code_id) || ("EB").equals(resources_code_id) || ("DB").equals(resources_code_id)){
//						validate.setItem_id("RELAN");
//					}else{
//						validate.setItem_id("DBLAN");
//					}
    				result=validate.checkResources_subject(value);
    				if(result!=null){
    					validate_num=2;//數據正常
    					break;
    				}else{
    					validate_num=1;
    					result=label_value+noData;//查無資料
    				}
    			 }
    		   //suitCollage="適用學院",colleage="訂購學院"
         }else if(suitCollage.equals(label_value) || colleage.equals(label_value)){
//        	 result=validate.checkCollege(value);
//        	 if(result!=null){
//        		 validate_num=2;//數據正常
//        	 }else{
//        		 validate_num=1;
//					result=label_value+noData;//查無資料
//        	 }
         }/*else if("適用系所".equals(label_value) || "訂購系所".equals(label_value)){
        	 result=validate.checkDep(value);
        	 if(result!=null){
        		 validate_num=2;//數據正常
        	 }else{
        		 validate_num=1;
					result=label_value+"查無資料";
        	 }
         }*/
         //dailishang=代理商,publisher=出版商
         else if(dailishang.equals(label_value) || publisher.equals(label_value)){
        	 result=validate.checkAgentedPublisher(value);
        	 if(result!=null){
        		 validate_num=2;//數據正常
        	 }else{
        		 validate_num=1;
					result=label_value+noData;//查無資料
        	 }
         }
	//resourcesDB=所屬資料庫
         else if(resourcesDB.equals(label_value)){
        	 result=validate.checkDb(value);
        	 if(result!=null){
        		 validate_num=2;//數據正常
        	 }else{
        		 validate_num=1;
					result=label_value+noData;//查無資料
        	 }
         }
         else if("embargo".equals(label_value)){
        	 boolean bool=value.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        	 if(bool==true){
        		 validate_num=2;//數據正常
        	 }else{
        		 validate_num=1;
				 result="類型為整數";//查無資料
        	 }
         }
        return result;
	}

	/**
	 * 存為新紀錄
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * 
	 */
	@Listen("onClick=#insert_button")
	public void onClick$insert_button() throws SQLException, ParseException, InterruptedException{
		Session session=Sessions.getCurrent();
	//	Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
//		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
//		Locales.setThreadLocal(locale);
		//"是否確定存為新記錄？","消息提示":isSavaNew=是否確定存為新記錄
		//map = ZkUtils.getExecutionArgs();
		/*int number=Messagebox.show(isSavaNew,warnMessage,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);//是否確定存為新記錄？","消息提示"
 	    if(number==Messagebox.OK){}*/ 

		// 連接數據庫
//		UbictechConnectFactory connFactory = new UbictechConnectFactory();
//		Connection conn = connFactory.getUbictechConn();
		try{
		//獲取下拉列表選中的值
		String selectedName=resources_cobobox.getSelectedItem().getValue().toString();
		publicMethod();//數據的“取代”、“村為新紀錄”的公共方法
		int isChecked=0;//記錄勾選的checkbox數目
		//清空錯誤數據的session
		if(session.getAttribute("errMap")!=null){
			session.removeAttribute("errMap");
		}
		errMap=new LinkedHashMap<StringBuffer,String[]>();//實例化errMap
		int successNumber=0;//記錄處理成功的數據筆數
		if("exist".equals(selectedName)){
			//判斷Grid中是否有數據
			if(resources_grid.getChildren()!=null && resources_grid.getChildren().size()>0){
				List grid_list=resources_grid.getChildren();
				ListModel listModel=resources_grid.getModel();
				Columns columns=null;
				for(int i=0;i<grid_list.size();i++){
					//判斷是否是Columns控件
					if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Columns")){
						columns=(Columns)grid_list.get(i);	
						break;
					}
					
				}
				//循環Grid
				for(int i=0;i<grid_list.size();i++){
					//如果是Rows
					if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Rows")){
						Rows rows=(Rows)grid_list.get(i);
						List<String[]> afterDataList=new ArrayList<String[]>();//記錄處理後剩下的數據
						//循環Rows內所有的Row
						for(int j=0;j<rows.getChildren().size();j++){
							Row row=(Row)rows.getChildren().get(j);
							//如果是checkbox
							if(row.getFirstChild().getClass().toString().equals("class org.zkoss.zul.Checkbox")){
								Checkbox ckbox=(Checkbox)row.getFirstChild();
								if(ckbox.isChecked()){//判斷checkbox是否被選中
									isChecked++;
									//新增
	
									String[] row_data=(String[])listModel.getElementAt(j);//取得checkbox選中的行的數據
									//根據resources_id(pk)獲取一筆數據
									//TODO IOerm_resources_mainfile需要換
									if(("DB").equals(resources_code_id) || ("WS").equals(resources_code_id)){
//										IOerm_resources_main_dbws resources_mainfile=new IOerm_resources_main_dbws();
									//IOerm_resources_mainfile resources_mainfile=new IOerm_resources_mainfile();
										ErmResourcesMainDbws resources_mainfile=new ErmResourcesMainDbws();
									StringBuffer sb=new StringBuffer();//存儲錯誤信息
									
									String[] obj=new String[row_data.length-2];
									for(int k=0;k<row_data.length;k++){
										if(k>1){
											String value=row_data[k-1];//每個單元格內的數據
											obj[k-2]=value;
											Column column=(Column)columns.getChildren().get(k);//獲取每一個單元格所對應的Column
											String label_value=column.getLabel();//獲取Column內Label內的值
											//調用驗證方法驗證數據
											if(!value.equals("")){
	                                       String result=validate(label_value,value);
	                                       if(validate_num==1){//validate_num==1表示數據有誤，返回錯誤信息
	                                    	   sb.append(result);
	                                    	   sb.append("\n");
	                                       }else if(validate_num==2){//validate_num==1表示數據正常，返回需要的數據並存到數組中
	                                    	   obj[k-2]=result;
	                                       }
											}
											
											if(qidingDate.equals(label_value)){//起訂日期
												//如果起訂日期和訖訂日期不為空
												if(!value.equals("") && !row_data[k].equals("")){
													ResourcesValidate validate=new ResourcesValidate();
													if(!validate.isdateFormat(value)){
														sb.append(message2);//訖訂日期格式不正確
														sb.append("\n");
													}
													if(!validate.isdateFormat(row_data[k])){
														sb.append(message2);//訖訂日期格式不正確
														sb.append("\n");
													}
													//起訂日期和訖訂日期的格式都正確時
													if(validate.isdateFormat(value) && validate.isdateFormat(row_data[k])){
														boolean bool=validate.isdate(value, row_data[k]);
														if(!bool){
															sb.append(message3);//起訂日期不能大於訖訂日期格
															sb.append("\n");
														}
													}
													
													
												}
											}
											
										}
									}
									//如果有錯誤信息
									if(!sb.toString().equals("")){
										errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
									}else{
										//否則執行數據庫操作
										Class clas=resources_mainfile.getClass();
										for(int m=0;m<value_index;m++){
										try {
											
											PropertyDescriptor pd=new PropertyDescriptor(Array_value[m],clas);
											Method method=pd.getWriteMethod();
											try {
												String type=pd.getPropertyType().getName();
												if(type.equals("int")){//如果此屬性是int類型
													method.invoke(resources_mainfile, Integer.parseInt(obj[m]));
												}else if(type.equals("java.util.Date")){//如果此屬性是Date類型
													if(obj[m]!=null && !obj[m].equals("") && !obj[m].equalsIgnoreCase("null")){
														DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
														Date date=df.parse(obj[m]);
														method.invoke(resources_mainfile,date);
													}
												}else if(type.equals("double")){
													if(obj[m]!=null && !obj[m].equals("") ){
														method.invoke(resources_mainfile, Double.parseDouble(obj[m]));
													}
												}else {
													if(obj[m]!=null && !obj[m].equals("") ){
														method.invoke(resources_mainfile, obj[m]);
													}
												}
												
											} catch (IllegalArgumentException e) {
												e.printStackTrace();
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												e.printStackTrace();
											}
										} catch (IntrospectionException e) {
											e.printStackTrace();
										}
										}
										//狀態：1表示正常，2表示暫存
										resources_mainfile.setState("1");
										//資源類型
									//	resources_mainfile.setType_id(resources_code_id);
										resources_mainfile.setTypeId(resources_code_id);
										//獲取主鍵編碼
									//	String series = new com.ubictech.util.SeriesParam_Handler().getSeriesParam(connFactory,conn,resources_code_id);
										//resources_mainfile.setResources_id(series);
										List<ErmResourcesMainDbws> dbwsList = ((ErmResMainDbwsService) SpringUtil
												.getBean("ermResMainDbwsService")).findAllResMainDbwsList(
														resources_mainfile, webEmployee);
										String tempId = "DB";
										Long num = (long) dbwsList.size();
										num = num + 1;
										String resourcesId = tempId + RandomIDGenerator.fmtLong(num, 9);
										resources_mainfile.setResourcesId(resourcesId);
										resources_mainfile.setIsDataEffid(1);
										resources_mainfile.setHistory("N");
										resources_mainfile.setState("1");
										//向數據庫中新增一條數據
										//boolean bool=resources_mainfile.insert(connFactory, conn);
										//如果新增成功則向Log表中增加一條日誌
//										if(bool){
//											successNumber++;
//											resources_mainfile.save_log(connFactory, conn,resources_mainfile, "I");
//											conn.commit();
//											//講數據添加到solr中
//											//TODO Resources_mainfile_search需要換
//											new Resources_main_dbws_search().resources_main_dbws_editData_add(series);
//										}
										((WebSysLogService) SpringUtil.getBean("webSysLogService"))
										.editLog(ZkUtils.getRemoteAddr(),
												webEmployee.getEmployeesn(), "resourcesmaindbws_"
														+ resources_mainfile.getResourcesId());
										try {
											((ErmResMainDbwsService) SpringUtil
											.getBean("ermResMainDbwsService"))
											.addResMainDbws(resources_mainfile);

											((ResourcesMainDbwsSolrSearch) SpringUtil
											.getBean("resourcesMainDbwsSolrSearch"))
											.resources_main_dbws_editData(resources_mainfile
													.getResourcesId());
											successNumber++;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											//e.printStackTrace();
											log.error("資料庫添加異常",e);
										}
									}
									}
									//---------------------------------------------------------------------------------------------------------------
									if(("EJ").equals(resources_code_id) || ("EB").equals(resources_code_id)){
										//IOerm_resources_main_ejeb resources_main_ejeb=new IOerm_resources_main_ejeb();
									//	IOerm_resources_ejeb_item resources_ejeb_item=new IOerm_resources_ejeb_item();
										ErmResourcesMainEjeb resources_main_ejeb=new ErmResourcesMainEjeb();
										ErmResourcesEjebItem resources_ejeb_item=new ErmResourcesEjebItem();
										//IOerm_resources_mainfile resources_mainfile=new IOerm_resources_mainfile();
										
										StringBuffer sb=new StringBuffer();//存儲錯誤信息
										
										String[] obj=new String[row_data.length-2];
										for(int k=0;k<row_data.length;k++){
											if(k>1){
												String value=row_data[k-1];//每個單元格內的數據
												obj[k-2]=value;
												Column column=(Column)columns.getChildren().get(k);//獲取每一個單元格所對應的Column
												String label_value=column.getLabel();//獲取Column內Label內的值
												//調用驗證方法驗證數據
												if(!value.equals("")){
													String result=validate(label_value,value);
													if(validate_num==1){//validate_num==1表示數據有誤，返回錯誤信息
														sb.append(result);
														sb.append("\n");
													}else if(validate_num==2){//validate_num==1表示數據正常，返回需要的數據並存到數組中
														obj[k-2]=result;
													}
												}
												if(qidingDate.equals(label_value)){//起訂日期
													//如果起訂日期和訖訂日期不為空
													if(!value.equals("") && !row_data[k].equals("")){
														ResourcesValidate validate=new ResourcesValidate();
														if(!validate.isdateFormat(value)){
															sb.append(message2);//訖訂日期格式不正確
															sb.append("\n");
														}
														if(!validate.isdateFormat(row_data[k])){
															sb.append(message2);//訖訂日期格式不正確
															sb.append("\n");
														}
														//起訂日期和訖訂日期的格式都正確時
														if(validate.isdateFormat(value) && validate.isdateFormat(row_data[k])){
															boolean bool=validate.isdate(value, row_data[k]);
															if(!bool){
																sb.append(message3);//起訂日期不能大於訖訂日期格
																sb.append("\n");
															}
														}
														
														
													}
												}
												
											}
										}
										//如果有錯誤信息
										if(!sb.toString().equals("")){
											errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
										}else{
											//否則執行數據庫操作
											//TODO 有改動
											//狀態：1表示正常，2表示暫存
											resources_ejeb_item.setState("1");
											//資源類型
											//resources_main_ejeb.setType_id(resources_code_id);
											resources_main_ejeb.setTypeId(resources_code_id);
											//獲取主鍵編碼
//											String series = new com.ubictech.util.SeriesParam_Handler().getSeriesParam(connFactory,conn,resources_code_id);
//											resources_main_ejeb.setResources_id(series);
//											resources_ejeb_item.setResources_id(series);
											List<ErmResourcesMainEjeb> ejebList = ((ErmResMainEjebService) SpringUtil
													.getBean("ermResMainEjebService")).findAllEjeb();
											String tempId = "EJ";
											Long num = (long) ejebList.size();
											num = num + 1;
											// String resourcesId = RandomIDGenerator.getSerialNumber("EJ");
											String resourcesId = tempId + RandomIDGenerator.fmtLong(num, 9);
											resources_main_ejeb.setResourcesId(resourcesId);
											resources_ejeb_item.setResourcesId(resourcesId);
											DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
											if(("EB").equals(resources_code_id)){
												//resources_ejeb_item.setDb_id(obj[16]);
												resources_ejeb_item.setDbId(obj[16]);
												resources_main_ejeb.setTitle(obj[0]);
												resources_main_ejeb.setBrief1(obj[10]);
												resources_main_ejeb.setBrief2(obj[11]);
											//	resources_main_ejeb.setLanguage_id(obj[12]);
												resources_main_ejeb.setLanguageId(obj[12]);
												resources_main_ejeb.setCore(obj[13]);
												resources_main_ejeb.setIsbnonline(obj[18]);
												resources_main_ejeb.setIsbnprinted(obj[19]);
												resources_main_ejeb.setCn(obj[21]);
												resources_main_ejeb.setCalln(obj[22]);
												resources_main_ejeb.setAuthor(obj[24]);
												
												resources_ejeb_item.setUrl1(obj[1]);
												resources_ejeb_item.setUrl2(obj[2]);
											//	resources_ejeb_item.setRemark_id(obj[3]);
												resources_ejeb_item.setRemarkId(obj[3]);
											//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
												resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
												if(obj[5]!=null&&!"".equals(obj[5])){
													//resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
													resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
												}
												resources_ejeb_item.setCoverage(obj[6]);
											//	resources_ejeb_item.setRegllyip(obj[7]);
												resources_ejeb_item.setRegllyIp(obj[7]);
												resources_ejeb_item.setIdpwd(obj[8]);
												resources_ejeb_item.setOthers(obj[9]);
												//resources_ejeb_item.setPublisher_id(obj[14]);
												resources_ejeb_item.setPublisherId(obj[14]);
												//resources_ejeb_item.setPubplace(obj[15]);
												resources_ejeb_item.setPubPlace(obj[15]);
											//	resources_ejeb_item.setPublisherurl(obj[17]);
												resources_ejeb_item.setPublisherUrl(obj[17]);
												if(obj[20] == null || "".equals(obj[20])){
													//resources_ejeb_item.setEmbargo(0);
												}else{
													//resources_ejeb_item.setEmbargo(Double.valueOf(obj[20]));
													resources_ejeb_item.setEmbargo(obj[20]);
												}
											//	resources_ejeb_item.setLibarymoney(obj[23]);
												resources_ejeb_item.setLibaryMoney(obj[23]);
												resources_ejeb_item.setVersion(obj[25]);
											//	resources_ejeb_item.setPlace_id(obj[26]);
												resources_ejeb_item.setPlaceId(obj[26]);
												
											}else if (("EJ").equals(resources_code_id)){
											//	resources_ejeb_item.setDb_id(obj[17]);
												resources_ejeb_item.setDbId(obj[17]);
												resources_main_ejeb.setTitle(obj[0]);
												resources_main_ejeb.setBrief1(obj[10]);
												resources_main_ejeb.setBrief2(obj[11]);
											//	resources_main_ejeb.setLanguage_id(obj[12]);
												resources_main_ejeb.setLanguageId(obj[12]);
												resources_main_ejeb.setCore(obj[14]);
												resources_main_ejeb.setIssnonline(obj[19]);
												resources_main_ejeb.setIssnprinted(obj[20]);
												resources_main_ejeb.setCreateDate(new Date());
												resources_main_ejeb.setWebEmployee(webEmployee);
												
												resources_ejeb_item.setUrl1(obj[1]);
												resources_ejeb_item.setUrl2(obj[2]);
											//	resources_ejeb_item.setRemark_id(obj[3]);
												resources_ejeb_item.setRemarkId(obj[3]);
											//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
												resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
												if(obj[5]!=null&&!"".equals(obj[5])){
												//	resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
													resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
												}
												resources_ejeb_item.setCoverage(obj[6]);
											//	resources_ejeb_item.setRegllyip(obj[7]);
												resources_ejeb_item.setRegllyIp(obj[7]);
												resources_ejeb_item.setIdpwd(obj[8]);
												resources_ejeb_item.setOthers(obj[9]);
												resources_ejeb_item.setFrenquency(obj[13]);
											//	resources_ejeb_item.setPublisher_id(obj[15]);
												resources_ejeb_item.setPublisherId(obj[15]);
											//	resources_ejeb_item.setPubplace(obj[16]);
												resources_ejeb_item.setPubPlace(obj[16]);
											//	resources_ejeb_item.setPublisherurl(obj[18]);
												resources_ejeb_item.setPublisherUrl(obj[18]);
												if(obj[21] == null || "".equals(obj[21])){
													//resources_ejeb_item.setEmbargo(0);
												}else{
													//resources_ejeb_item.setEmbargo(Double.valueOf(obj[21]));
													resources_ejeb_item.setEmbargo(obj[21]);
												}
												resources_ejeb_item.setEholdings(obj[22]);
											//	resources_ejeb_item.setLibarymoney(obj[23]);
												resources_ejeb_item.setLibaryMoney(obj[23]);
											//	resources_ejeb_item.setPlace_id(obj[24]);
												resources_ejeb_item.setPlaceId(obj[24]);
												resources_ejeb_item.setWebEmployee(webEmployee);
												resources_ejeb_item.setCreateDate(new Date());
												resources_ejeb_item.setState("1");
												resources_ejeb_item.setHistory("N");
											}
											//向數據庫中新增一條數據
//											boolean bool=resources_main_ejeb.insert(connFactory, conn);
//											boolean bool2=resources_ejeb_item.insert(connFactory, conn);
//											//如果新增成功則向Log表中增加一條日誌
//											if(bool || bool2){
//												successNumber++;
//												resources_main_ejeb.save_log(connFactory, conn,resources_main_ejeb, "I");
//												resources_ejeb_item.save_log(connFactory, conn,resources_ejeb_item, "I");
//												conn.commit();
//												//講數據添加到solr中
//												//TODO Resources_mainfile_search需要換
//												new Resources_main_ejeb_search().resources_main_ejeb_editData(series);
//											}
											((WebSysLogService) SpringUtil.getBean("webSysLogService"))
											.insertLog(ZkUtils.getRemoteAddr(),
													webEmployee.getEmployeesn(), "resourcesmainejeb_"
															+ resources_main_ejeb.getResourcesId());
									try {
										((ErmResMainEjebService) SpringUtil
												.getBean("ermResMainEjebService")).addResMainEjeb(
														resources_main_ejeb, resources_ejeb_item);
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resources_main_ejeb
														.getResourcesId());
										successNumber++;
									} catch (Exception e) {
										log.error("存為新記錄異常",e);
									}
										}
									}
									//---------------------------------------------------------------------------------------------------------------
								}else{
									//記錄未被選中需要處理的數據並存儲到List中
									String[] lastArray=(String[])listModel.getElementAt(j-1);
									String[] array=(String[])listModel.getElementAt(j);
									afterDataList.add(lastArray);
									afterDataList.add(array);
								}
							}
						}
						session.setAttribute("comb_resource_type", resources_code_id);
						//如果數據有變動
						if(isChecked!=0){
							session.setAttribute("selectedName", selectedName);
							session.setAttribute("isChecked", isChecked);//選中需要處理的數據筆數
							//將未選中的數據存到session中，即使為null也儲存
							session.setAttribute("doAfterExistData", afterDataList);
							if(errMap!=null && errMap.size()>0){
								session.setAttribute("errMap", errMap);
								//處理成功的數據筆數
								session.setAttribute("successNumber", successNumber);
								//頁面跳轉
								//Executions.sendRedirect("/resources_rsbatch_error.jsf");
								String url="resourcesRsbatch/resourcesRsbatchError.zul";
								ZkUtils.refurbishMethod(url);	
								//Executions.sendRedirect("/resourcesRsbatchError.zul");
							}else{//如果沒有
								//處理成功的數據筆數
								session.setAttribute("successNumber", successNumber);
								//Executions.sendRedirect("/resources_rsbatch_return.jsf");
								//Executions.sendRedirect("/resourcesRsbatchReturn.zul");
								String url="resourcesRsbatch/resourcesRsbatchReturn.zul";
								ZkUtils.refurbishMethod(url);	
							}
						
						}
					}
				}
			}
			
		}else if("noExist".equals(selectedName)){
			if(resources_grid.getChildren()!=null && resources_grid.getChildren().size()>0){
				List grid_list=resources_grid.getChildren();
				ListModel listModel=resources_grid.getModel();
				Columns columns=null;
				for(int i=0;i<grid_list.size();i++){
					//判斷是否是Columns控件
					if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Columns")){
						columns=(Columns)grid_list.get(i);	
						break;
					}
					
				}
				
				//循環Grid
				for(int i=0;i<grid_list.size();i++){
					//如果是Rows
					if(grid_list.get(i).getClass().toString().equals("class org.zkoss.zul.Rows")){
						Rows rows=(Rows)grid_list.get(i);
						List<String[]> afterDataList=new ArrayList<String[]>();
						//循環Rows內所有的Row
						for(int j=0;j<rows.getChildren().size();j++){
							Row row=(Row)rows.getChildren().get(j);
							//如果是checkbox
							if(row.getFirstChild().getClass().toString().equals("class org.zkoss.zul.Checkbox")){
								Checkbox ckbox=(Checkbox)row.getFirstChild();
								if(ckbox.isChecked()){//判斷checkbox是否被選中
									isChecked++;
									//TODO 有新增
									String[] row_data=(String[])listModel.getElementAt(j);//取得checkbox選中的行的數據
									//根據resources_id(pk)獲取一筆數據
									//TODO IOerm_resources_mainfile去掉
									if(("DB").equals(resources_code_id) || ("WS").equals(resources_code_id)){
									//	IOerm_resources_main_dbws resources_mainfile=new IOerm_resources_main_dbws();
										ErmResourcesMainDbws resources_mainfile=new ErmResourcesMainDbws();
									//IOerm_resources_mainfile resources_mainfile=new IOerm_resources_mainfile();
									
									StringBuffer sb=new StringBuffer();//存儲錯誤信息
									
									String[] obj=new String[row_data.length];
									for(int k=0;k<row_data.length;k++){
											String value=row_data[k];//每個單元格內的數據
											obj[k]=value;
											Column column=(Column)columns.getChildren().get(k+3);//獲取每一個單元格所對應的Column
											String label_value=column.getLabel();//獲取Column內Label內的值
											//調用驗證方法驗證數據
											if(!value.equals("")){
	                                       String result=validate(label_value,value);
	                                       if(validate_num==1){//validate_num==1表示數據有誤
	                                    	   sb.append(result);
	                                    	   sb.append("\n");
	                                       }else if(validate_num==2){//validate_num==1表示數據正常，並將需要的數據存到數組中
	                                    	   obj[k]=result;
	                                       }
	                                    }
										if(qidingDate.equals(label_value)){//起訂日期
											//如果起訂日期和訖訂日期不為空
											if(!value.equals("") && !row_data[k+1].equals("")){
												ResourcesValidate validate=new ResourcesValidate();
												if(!validate.isdateFormat(value)){
													sb.append(message2);//起訂日期格式不正確
													sb.append("\n");
												}
												if(!validate.isdateFormat(row_data[k+1])){
													sb.append(message2);//起訂日期格式不正確
													sb.append("\n");
												}
												//起訂日期和訖訂日期的格式都正確時
												if(validate.isdateFormat(value) && validate.isdateFormat(row_data[k+1])){
													boolean bool=validate.isdate(value, row_data[k+1]);
													if(!bool){
														sb.append(message3);//起訂日期不能大於訖訂日期格
														sb.append("\n");
													}
												}
												
												
											}
										}	
									}
									//如果有錯誤信息
									if(!sb.toString().equals("")){
										errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
									}else{
										//否則執行數據庫操作
										Class clas=resources_mainfile.getClass();
										int m=0;
										for(m=0;m<value_index;m++){
										try {
											PropertyDescriptor pd=new PropertyDescriptor(Array_value[m],clas);
											Method method=pd.getWriteMethod();
											try {
												String type=pd.getPropertyType().getName();
												if(type.equals("int")){//如果此屬性是int類型
													method.invoke(resources_mainfile, Integer.parseInt(obj[m]));
												}else if(type.equals("java.util.Date")){//如果此屬性是Date類型
													if(obj[m]!=null && !obj[m].equals("") && !obj[m].equalsIgnoreCase("null")){
														DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
														Date date=df.parse(obj[m]);
														method.invoke(resources_mainfile,date);
													}
												}else if(type.equals("double")){
													if(obj[m]!=null && !obj[m].equals("")){
														method.invoke(resources_mainfile, Double.parseDouble(obj[m]));
													}
												}else{
													if(obj[m]!=null && !obj[m].equals("")){
														method.invoke(resources_mainfile, obj[m]);
													}
													
												}
											} catch (IllegalArgumentException e) {
												e.printStackTrace();
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												e.printStackTrace();
											}
										} catch (IntrospectionException e) {
											e.printStackTrace();
										}
										}
										//獲取表的狀態1代表正常，2代表暫存
										resources_mainfile.setState("1");
										//資源類型
									//	resources_mainfile.setType_id(resources_code_id);
										resources_mainfile.setTypeId(resources_code_id);
										//獲取主鍵編碼
//										String series = new com.ubictech.util.SeriesParam_Handler().getSeriesParam(connFactory,conn,resources_code_id);
//										resources_mainfile.setResources_id(series);
//										List<ErmResourcesMainDbws> dbwsList = ((ErmResMainDbwsService) SpringUtil
//												.getBean("ermResMainDbwsService")).findAllResMainDbwsList(
//														resources_mainfile, webEmployee);
										List<ErmResourcesMainDbws> dbwsList = ((ErmResMainDbwsService) SpringUtil
												.getBean("ermResMainDbwsService")).findAllList();
										String tempId = "DB";
										Long num = (long) dbwsList.size();
										num = num + 1;
										String resourcesId = tempId + RandomIDGenerator.fmtLong(num, 9);
										resources_mainfile.setResourcesId(resourcesId);
//										//向數據庫中新增一條數據
//										boolean bool=resources_mainfile.insert(connFactory, conn);
//										//如果新增成功則向Log表中增加一條日誌
//										if(bool){
//											successNumber++;
//											resources_mainfile.save_log(connFactory, conn,resources_mainfile, "I");
//											conn.commit();
//											//講數據添加到solr中
//											//TODO Resources_mainfile_search改掉
//											new Resources_main_dbws_search().resources_main_dbws_editData_add(series);
//										}
										((WebSysLogService) SpringUtil.getBean("webSysLogService"))
										.editLog(ZkUtils.getRemoteAddr(),
												webEmployee.getEmployeesn(), "resourcesmaindbws_"
														+ resources_mainfile.getResourcesId());
								try {
									((ErmResMainDbwsService) SpringUtil
											.getBean("ermResMainDbwsService"))
											.addResMainDbws(resources_mainfile);

									((ResourcesMainDbwsSolrSearch) SpringUtil
											.getBean("resourcesMainDbwsSolrSearch"))
											.resources_main_dbws_editData(resources_mainfile
													.getResourcesId());
									successNumber++;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									log.error("資料庫新增異常",e);
								}
									}
								}
									//-------------------------------------------------------------------------------------
									if(("EJ").equals(resources_code_id) || ("EB").equals(resources_code_id)){
//										IOerm_resources_main_ejeb resources_main_ejeb=new IOerm_resources_main_ejeb();
//										IOerm_resources_ejeb_item resources_ejeb_item=new IOerm_resources_ejeb_item();
										ErmResourcesMainEjeb resources_main_ejeb=new ErmResourcesMainEjeb();
										ErmResourcesEjebItem resources_ejeb_item=new ErmResourcesEjebItem();
										//IOerm_resources_mainfile resources_mainfile=new IOerm_resources_mainfile();
										
										StringBuffer sb=new StringBuffer();//存儲錯誤信息
										
										String[] obj=new String[row_data.length];
										
										for(int k=0;k<row_data.length;k++){
											
											String value=row_data[k];//每個單元格內的數據
											obj[k]=value;
											Column column=(Column)columns.getChildren().get(k+3);//獲取每一個單元格所對應的Column
											String label_value=column.getLabel();//獲取Column內Label內的值
											//調用驗證方法驗證數據
											if(!value.equals("")){
												String result=validate(label_value,value);
												if(validate_num==1){//validate_num==1表示數據有誤
													sb.append(result);
													sb.append("\n");
												}else if(validate_num==2){//validate_num==1表示數據正常，並將需要的數據存到數組中
													obj[k]=result;
												}
											}
											if(qidingDate.equals(label_value)){//起訂日期
												//如果起訂日期和訖訂日期不為空
												if(!value.equals("") && !row_data[k+1].equals("")){
													ResourcesValidate validate=new ResourcesValidate();
													if(!validate.isdateFormat(value)){
														sb.append(message2);//起訂日期格式不正確
														sb.append("\n");
													}
													if(!validate.isdateFormat(row_data[k+1])){
														sb.append(message2);//起訂日期格式不正確
														sb.append("\n");
													}
													//起訂日期和訖訂日期的格式都正確時
													if(validate.isdateFormat(value) && validate.isdateFormat(row_data[k+1])){
														boolean bool=validate.isdate(value, row_data[k+1]);
														if(!bool){
															sb.append(message3);//起訂日期不能大於訖訂日期格
															sb.append("\n");
														}
													}
													
													
												}
											}	
											
										}
										//如果有錯誤信息
										if(!sb.toString().equals("")){
											errMap.put(sb, row_data);//將錯誤信息及數組存到Map中
										}else{
											//否則執行數據庫操作
											

											//獲取表的狀態1代表正常，2代表暫存
											resources_ejeb_item.setState("1");
											//資源類型
										//	resources_main_ejeb.setType_id(resources_code_id);
											resources_main_ejeb.setTypeId(resources_code_id);
											//獲取主鍵編碼
//											String series = new com.ubictech.util.SeriesParam_Handler().getSeriesParam(connFactory,conn,resources_code_id);
//											resources_main_ejeb.setResources_id(series);
//											resources_ejeb_item.setResources_id(series);
											List<ErmResourcesMainEjeb> ejebList = ((ErmResMainEjebService) SpringUtil
													.getBean("ermResMainEjebService")).findAllEjeb();
											String tempId = "EJ";
											Long num = (long) ejebList.size();
											num = num + 1;
											// String resourcesId = RandomIDGenerator.getSerialNumber("EJ");
											String resourcesId = tempId + RandomIDGenerator.fmtLong(num, 9);
											resources_main_ejeb.setResourcesId(resourcesId);
											resources_ejeb_item.setResourcesId(resourcesId);
											DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
											if(("EB").equals(resources_code_id)){
											//	resources_ejeb_item.setDb_id(obj[16]);
												resources_ejeb_item.setDbId(obj[16]);
												resources_main_ejeb.setTitle(obj[0]);
												resources_main_ejeb.setBrief1(obj[10]);
												resources_main_ejeb.setBrief2(obj[11]);
												
											//	resources_main_ejeb.setLanguage_id(obj[12]);
												resources_main_ejeb.setLanguageId(obj[12]);
												resources_main_ejeb.setCore(obj[13]);
												resources_main_ejeb.setIsbnonline(obj[18]);
												resources_main_ejeb.setIsbnprinted(obj[19]);
												resources_main_ejeb.setCn(obj[21]);
												resources_main_ejeb.setCalln(obj[22]);
												resources_main_ejeb.setAuthor(obj[24]);
												
												resources_ejeb_item.setUrl1(obj[1]);
												resources_ejeb_item.setUrl2(obj[2]);
											//	resources_ejeb_item.setRemark_id(obj[3]);
												resources_ejeb_item.setRemarkId(obj[3]);
											//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
												resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
												if(obj[5]!=null&&!"".equals(obj[5])){
												//	resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
													resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
												}
												resources_ejeb_item.setCoverage(obj[6]);
											//	resources_ejeb_item.setRegllyip(obj[7]);
												resources_ejeb_item.setRegllyIp(obj[7]);
												resources_ejeb_item.setIdpwd(obj[8]);
												resources_ejeb_item.setOthers(obj[9]);
											//	resources_ejeb_item.setPublisher_id(obj[14]);
												resources_ejeb_item.setPublisherId(obj[14]);
											//	resources_ejeb_item.setPubplace(obj[15]);
												resources_ejeb_item.setPubPlace(obj[15]);
											//	resources_ejeb_item.setPublisherurl(obj[17]);
												resources_ejeb_item.setPublisherUrl(obj[17]);
												if(obj[20] == null || "".equals(obj[20])){
													//resources_ejeb_item.setEmbargo(0);
												}else{
												//	resources_ejeb_item.setEmbargo(Double.valueOf(obj[20]));
													resources_ejeb_item.setEmbargo(obj[20]);
												}
										//		resources_ejeb_item.setLibarymoney(obj[23]);
												resources_ejeb_item.setLibaryMoney(obj[23]);
												resources_ejeb_item.setVersion(obj[25]);
												//resources_ejeb_item.setPlace_id(obj[26]);		
												resources_ejeb_item.setPlaceId(obj[26]);
											}
											else if (("EJ").equals(resources_code_id)){

											//	resources_ejeb_item.setDb_id(obj[17]);
												resources_ejeb_item.setDbId(obj[17]);
												resources_main_ejeb.setTitle(obj[0]);
												resources_main_ejeb.setBrief1(obj[10]);
												resources_main_ejeb.setBrief2(obj[11]);
											//	resources_main_ejeb.setLanguage_id(obj[12]);
												resources_main_ejeb.setLanguageId(obj[12]);
												resources_main_ejeb.setCore(obj[14]);
												resources_main_ejeb.setIssnonline(obj[19]);
												resources_main_ejeb.setIssnprinted(obj[20]);
												
												resources_ejeb_item.setUrl1(obj[1]);
												resources_ejeb_item.setUrl2(obj[2]);
											//	resources_ejeb_item.setRemark_id(obj[3]);
												resources_ejeb_item.setRemarkId(obj[3]);
											//	resources_ejeb_item.setStarorderdate(df.parse(obj[4]));
												resources_ejeb_item.setStarOrderDate(df.parse(obj[4]));
												if(obj[5]!=null&&!"".equals(obj[5])){
													//resources_ejeb_item.setEndorderdate(df.parse(obj[5]));
													resources_ejeb_item.setEndOrderDate(df.parse(obj[5]));
												}
												resources_ejeb_item.setCoverage(obj[6]);
											//	resources_ejeb_item.setRegllyip(obj[7]);
												resources_ejeb_item.setRegllyIp(obj[7]);
												resources_ejeb_item.setIdpwd(obj[8]);
												resources_ejeb_item.setOthers(obj[9]);
												resources_ejeb_item.setFrenquency(obj[13]);
											//	resources_ejeb_item.setPublisher_id(obj[15]);
												resources_ejeb_item.setPublisherId(obj[15]);
											//	resources_ejeb_item.setPubplace(obj[16]);
												resources_ejeb_item.setPubPlace(obj[16]);
											//	resources_ejeb_item.setPublisherurl(obj[18]);
												resources_ejeb_item.setPublisherUrl(obj[18]);
												if(obj[21] == null || "".equals(obj[21])){
													//resources_ejeb_item.setEmbargo(0);
												}else{
													//resources_ejeb_item.setEmbargo(Double.valueOf(obj[21]));
													resources_ejeb_item.setEmbargo(obj[21]);
												}
												resources_ejeb_item.setEholdings(obj[22]);
											//	resources_ejeb_item.setLibarymoney(obj[23]);
												resources_ejeb_item.setLibaryMoney(obj[23]);
											//	resources_ejeb_item.setPlace_id(obj[24]);
												resources_ejeb_item.setPlaceId(obj[24]);
												resources_main_ejeb.setIsDataEffid(1);
												resources_ejeb_item.setIsDataEffid(1);
												resources_ejeb_item.setHistory("N");
												resources_main_ejeb.setCreateDate(new Date());
												resources_main_ejeb.setWebEmployee(webEmployee);
												resources_ejeb_item.setCreateDate(new Date());
												resources_ejeb_item.setWebEmployee(webEmployee);
											}
											//向數據庫中新增一條數據
//											boolean bool=resources_main_ejeb.insert(connFactory, conn);
//											boolean bool2=resources_ejeb_item.insert(connFactory, conn);
//											//如果新增成功則向Log表中增加一條日誌
//											if(bool || bool2){
//												successNumber++;
//												//resources_mainfile.save_log(resources_mainfile, "I");
//												conn.commit();
//												//講數據添加到solr中
//												//TODO Resources_mainfile_search改掉
//												new Resources_main_ejeb_search().resources_main_ejeb_editData(series);
//											}
											((WebSysLogService) SpringUtil.getBean("webSysLogService"))
											.insertLog(ZkUtils.getRemoteAddr(),
													webEmployee.getEmployeesn(), "resourcesmainejeb_"
															+ resources_main_ejeb.getResourcesId());
									try {
										((ErmResMainEjebService) SpringUtil
												.getBean("ermResMainEjebService")).addResMainEjeb(
														resources_main_ejeb, resources_ejeb_item);
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resources_main_ejeb
														.getResourcesId());
										successNumber++;
									} catch (Exception e) {
										log.error("電子期刊添加異常"+e);
									}
										}
									}
									//-------------------------------------------------------------------------------------
								}else{
									//記錄未被選中需要處理的數據並存儲到List中
									String[] array=(String[])listModel.getElementAt(j);
									afterDataList.add(array);
								}
							}
						}
						//如果數據有變動
						if(isChecked!=0){
							session.setAttribute("selectedName", selectedName);
							session.setAttribute("isChecked", isChecked);//選中需要處理的數據筆數
							//將未選中的數據存到session中，即使為null也儲存
							session.setAttribute("doAfterNoExistData", afterDataList);
							session.setAttribute("comb_resource_type", resources_code_id);
							//如果有錯誤數據存在
							if(errMap!=null && errMap.size()>0){
								session.setAttribute("errMap", errMap);
								//處理成功的數據筆數
								session.setAttribute("successNumber", successNumber);
								//頁面跳轉
								//Executions.sendRedirect("/resources_rsbatch_error.jsf");
							//	Executions.sendRedirect("/resourcesRsbatchError.zul");
								String url="resourcesRsbatch/resourcesRsbatchError.zul";
								ZkUtils.refurbishMethod(url);	
							}else{//如果沒有
								//處理成功的數據筆數
								session.setAttribute("successNumber", successNumber);
//								Executions.sendRedirect("/resources_rsbatch_return.jsf");
								//Executions.sendRedirect("/resourcesRsbatchReturn.zul");
								String url="resourcesRsbatch/resourcesRsbatchReturn.zul";
								ZkUtils.refurbishMethod(url);	
							}
						
						}
					}
				}
			
			}
			
		}
		//如果沒有選擇任何checkbox
		if(isChecked==0){
			Messagebox.show(message4,warnMessage,Messagebox.OK,Messagebox.INFORMATION);//"請選擇要存為新紀錄的數據","訊息提示"
		}
		
		}catch(Exception e){
			log.error("",e);
		}finally{
		}
 	    
	}
	
	/**
	 * 數據的“取代”、“村為新紀錄”的公共方法
	 * 
	 */
	public void publicMethod(){
		//電子資源庫
	    String[] dbValue={"Title","Url1","Url2","RemarkId","StarOrderDate","EndOrderDate","Coverage","RegllyIp","Idpwd","Others","Brief1","Brief2","LanguageId","AgentedId","PublisherId","Frenquency","Intro","Concur","ConnectId","LibaryMoney","PlaceId"};
			//電子期刊
	    String[] ejValue={"Title","Url1","Url2","Remark_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Frenquency","Core","Publisher_id","Pubplace","Db_id","Publisherurl","Issnprinted","Issnonline","Embargo","Eholdings","Libarymoney","Place_id"};
			//電子書
		String[] ebValue={"Title","Url1","Url2","Remark_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Core","Publisher_id","Pubplace","Db_id","Publisherurl","Isbnprinted","Isbnonline","Embargo","Cn","Calln","Libarymoney","Author","Version","Place_id"};
			//網絡資源
		String[] wsValue={"Title","Url1","Url2","Remark_id","Publisher_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Agented_id","Version"};
			//其他
		String[] otValue={"Title","Url1","Url2","Remark_id","Publisher_id","Starorderdate","Endorderdate","Coverage","Regllyip","Idpwd","Others","Brief1","Brief2","Language_id","Agented_id","Version"};
		
		
		if(("DB").equals(resources_code_id)){
			Array_value=dbValue;
			value_index=21;
		}else if(("EJ").equals(resources_code_id)){
			Array_value=ejValue;
			value_index=25;
		}else if(("EB").equals(resources_code_id)){
			Array_value=ebValue;
			value_index=27;
		}else if(("WS").equals(resources_code_id)){
			Array_value=wsValue;
			value_index=16;
		}else if(("OT").equals(resources_code_id)){
			Array_value=otValue;
			value_index=16;
		}
		
	}
	/**
	 * 點擊分頁按鈕進行跳頁的事件
	 */
	public void onPaging$resources_grid(){
		int pageSize=resources_grid.getPageSize();
		//獲取columns內的全選按鈕
		Checkbox ckbox=(Checkbox)resources_grid.getColumns().getFirstChild().getFirstChild();
		//如果按鈕已選中
		if(ckbox.isChecked()){
			ckbox.setChecked(false);	
		}
		//獲取上一頁的頁碼並且將上一頁的checkbox全不選
		if(Executions.getCurrent().getSession().getAttribute("activePage")!=null){
			int activesize=Integer.parseInt(Executions.getCurrent().getSession().getAttribute("activePage").toString());
			Rows rows=resources_grid.getRows();
			//循環當前頁碼內的row
			/*for(int i=0;i<rows.getChildren().size();i++){
				if(i>=activesize*pageSize && i<(activesize+1)*pageSize){
					Row row=(Row)rows.getChildren().get(i);
					if(row.getFirstChild().getClass().getName().equals("org.zkoss.zul.Checkbox")){
						Checkbox ckb=(Checkbox)row.getFirstChild();
						ckb.setChecked(false);
					}
				}
			}*/
		}
	}

}
/**
 * 加載 
 * @author nj
 *
 */
class Resources_upload_result_Renderer implements RowRenderer{
	
	private Session session=Sessions.getCurrent();
	
	public void render(Row row, Object data, int arg2) throws Exception {
		/*//得到當前語系
		Wp_User_Handler wp_user_handler = (Wp_User_Handler) session.getAttribute("wp_user_handler");
		java.util.Locale locale = Locales.getLocale(wp_user_handler.getLocaleStr());
		Locales.setThreadLocal(locale);*/
		//Map<String, Object> map = new HashMap<String, Object>();
		String isHere=Labels.getLabel("resources_upload_result.isHere");//已存在
		String notIsHere=Labels.getLabel("resources_upload_result.notIsHearre");//未存在
		String[] row_data=(String[])data;//獲取每一行的數據並以數組形式存儲
	    if(row_data.length==0){
	    	return;
	    }
		String yes_no_exist_status="";
		String resources_type_name="";
		//判斷是已存在還是未存在
		if(session.getAttribute("yes_no_exist_status")!=null){
			yes_no_exist_status=session.getAttribute("yes_no_exist_status").toString();
			//yes_no_exist_status=(String) map.get("yes_no_exist_status");
		}
		//資源類型name
		if(session.getAttribute("resources_type_name")!=null){
			resources_type_name=session.getAttribute("resources_type_name").toString();
			//resources_type_name=(String) map.get("resources_type_name");
		}
		int index=0;
		int num=row_data.length;
		if(yes_no_exist_status.equals("exist")){
			if("".equals(row_data[0])){//如果沒有主鍵存在
				Checkbox ck=new Checkbox();
				ck.addEventListener(Events.ON_CHECK, new Resources_upload_result_onSelectedOneCheckbox());
				ck.setParent(row);
			}else {
				Cell cell=new Cell();
				Label label=new Label(isHere);//已存在
				Label label1=new Label(row_data[0]);
				label1.setVisible(false);//資源類型id不可見
				label.setParent(cell);
				label1.setParent(cell);
				cell.setParent(row);
			}
			index=1;
			num=num-1;
		}else if(yes_no_exist_status.equals("noExist")){
			Checkbox ck=new Checkbox();
			ck.addEventListener(Events.ON_CHECK, new Resources_upload_result_onSelectedOneCheckbox());
			ck.setParent(row);
			//比對結果
			Label label=new Label(notIsHere);//未存在
			label.setParent(row);
		}
		//資源類型
		Label label_type=new Label(resources_type_name);
		label_type.setParent(row);
		//循環將數組中的數據放到grid的row中
		for(int i=0;i<num;i++){
		Label label=new Label(row_data[i+index]);
		label.setParent(row);
		}
		
	}
	
}
/**
 * 
 * checkbox全選/不全選
 * @author nj
 *
 */
class Resources_upload_result_onSelected implements EventListener{

	public void onEvent(Event event) throws Exception {
		Checkbox ckbox=(Checkbox)event.getTarget();//獲取當前選中的checkbox
		Column column=(Column)ckbox.getParent();
		Columns columns=(Columns)column.getParent();
		Grid grid=(Grid)columns.getParent();
		//當前頁數
		int activePage=grid.getActivePage();
		//所有的數據條數
		int pageCount=grid.getPageCount();
		//每頁顯示數據條數
		int pageSize=grid.getPageSize();
		Executions.getCurrent().getSession().setAttribute("activePage", activePage);
		
		int num=0;//標記數字：0表示未選中，1表示已選中
		if(ckbox.isChecked()){//如果checkbox被選中	
			num=1;
		}
		boolean bool=false;
		if(ckbox.isChecked()){
			ckbox.setChecked(true);
			bool=true;
		}else{
			ckbox.setChecked(false);
		    bool=false;	
		}
		//List list=grid.getChildren();	
		//for(int i=0;i<list.size();i++){
			//判斷是否是Rows控件
			//if(list.get(i).getClass().toString().equals("class org.zkoss.zul.Rows")){
				
				Rows rows=grid.getRows();
				for(int j=0;j<rows.getChildren().size();j++){
					if(j>=activePage*pageSize && j<(activePage+1)*pageSize){
					Row row=(Row)rows.getChildren().get(j);
					//判斷是否是Checkbox控件
					if(row.getFirstChild().getClass().toString().equals("class org.zkoss.zul.Checkbox")){
						Checkbox checkbox=(Checkbox)row.getFirstChild();
					/*	if(num==0){//如果全選是未選中，則所以的Checkbox不選中
						checkbox.setChecked(false);
						}else if(num==1){//如果全選是已選中，則所以的Checkbox選中
							checkbox.setChecked(true);
							}*/
						checkbox.setChecked(bool);
						
						}
					}
					}
				//}
		//	}		
	}
}
/**
 * 單個checkbox選擇
 */
class Resources_upload_result_onSelectedOneCheckbox implements EventListener{

	public void onEvent(Event event) throws Exception {
	   Checkbox ck=(Checkbox)event.getTarget();
	   Grid grid=(Grid)ck.getParent().getParent().getParent();
	 //當前頁數
		int activePage=grid.getActivePage();
		//所有的頁碼數
		int pageCount=grid.getPageCount();
		//每頁顯示數據條數
		int pageSize=grid.getPageSize();
        
		Executions.getCurrent().getSession().setAttribute("activePage", activePage);
		
		Rows rows=grid.getRows();
		int count=0;
		for(int i=0;i<rows.getChildren().size();i++){
			if(i>=activePage*pageSize && i<(activePage+1)*pageSize){
				Row row=(Row)rows.getChildren().get(i);
				if(row.getFirstChild().getClass().toString().equals("class org.zkoss.zul.Checkbox")){
					Checkbox cbox=(Checkbox)row.getFirstChild();
					if(cbox.isChecked()){
						count++;
					}
				}else{
					count++;
				}
				
			}
		}
		//如果是最後一頁（條數與pageSize不一致）
		if(activePage+1==pageCount){
		int result=(pageCount*pageSize)-rows.getChildren().size();
		if(result>0){
			pageSize=pageSize-result;
		}
		}
		Checkbox ckbox=(Checkbox)grid.getColumns().getFirstChild().getFirstChild();
		if(count==pageSize){
		     if(!ckbox.isChecked()){
		    	 ckbox.setChecked(true);
		     }
		}else{
			 if(ckbox.isChecked()){
		    	 ckbox.setChecked(false);
		     }
		}
	   
		
	}
}
