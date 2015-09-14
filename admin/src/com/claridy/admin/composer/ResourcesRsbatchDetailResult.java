package com.claridy.admin.composer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesDbtype;
import com.claridy.domain.ErmResourcesReltitle;
import com.claridy.domain.ErmResourcesSubject;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmResourcesDbtypeService;
import com.claridy.facade.ErmResourcesReltitleService;
import com.claridy.facade.ErmResourcesSubjectService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;
import com.claridy.facade.ResourcesMainfileSolrSearch;

public class ResourcesRsbatchDetailResult extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5366419318081405413L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Label pagePath;//頁面路徑
	@Wire
	private Label sucessNumber;//成功筆數
	@Wire
	private Label existResources_label;
	@Wire
	private Label noExistResources_label;
	@Wire
	private Listbox isExist_list;//下拉
	@Wire
	private Listbox detailData;//顯示數據
	@Wire
	private Button insertDB;//匯入資料
	@Wire
	private Button export;//匯出
	@Wire
	private Label warn1;//共上傳
	@Wire
	private Label warn2;//筆資料
	@Wire
	private Label warn3;//筆有相同標題的電子資源
	@Wire
	private Label warn4;//筆沒有相同標題的電子資源
	private String resources_type="";//電子資源類型
	private String detailType="";//副檔類型
	private String resources_id="";
	private String detail_name="";
	private String generalcode_id="";
	private String yesOrNo="";
	private String item_id="";
	private List<String[]> existDBList = null;//數據庫中已存在數據
	private List<String[]> nExistList = null;// 未存在數據
	private List<String[]> errorList=null;//匯入失敗的資料
	private List<String[]> notDoExcelList=null;//匯入失敗的資料
    private int succesDataNumber=0;//記錄匯入資料成功的筆數
    private WebEmployee webEmployee;
    //多語系
    private String warnMessage,pageTitle,sameResource,noSameResource,exportmes,importmes,title,noWork,message1,message2,message3,message4,message5,message6,colleage,suilt,errormes,reportTitle,titleNames,warn1mes,warn2mes,warn3mes,warn4mes;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		languageLoad();
		pagePath.setValue(pageTitle);//電子資源管理》副檔上傳》結果顯示
		warn1.setValue(warn1mes);
		warn2.setValue(warn2mes);
		warn3.setValue(warn3mes);
		warn4.setValue(warn4mes);
		if(Sessions.getCurrent().getAttribute("resources_Type")!=null){
			resources_type=Sessions.getCurrent().getAttribute("resources_Type").toString();
		}
		if(Sessions.getCurrent().getAttribute("detail_Type")!=null){
			detailType=Sessions.getCurrent().getAttribute("detail_Type").toString();
		}
		if(Sessions.getCurrent().getAttribute("existDBList")!=null){
			existDBList=(List<String[]>) Sessions.getCurrent().getAttribute("existDBList");
		}else{
			existDBList=new ArrayList<String[]>();
		}
		if(Sessions.getCurrent().getAttribute("nExistList")!=null){
			nExistList=(List<String[]>) Sessions.getCurrent().getAttribute("nExistList");
		}else{
			nExistList=new ArrayList<String[]>();
			}
		isExist_list.setMold("select");
		
		for(int i=0;i<2;i++){
			Listitem item=new Listitem();
			if(i==0){
				item.setLabel(sameResource);//有相同標題的電子資源
				item.setValue("exist");
			}else{
				item.setLabel(noSameResource);//沒有相同標題的電子資源
				item.setValue("nExist");
			}
			isExist_list.appendChild(item);
		}
		isExist_list.setSelectedIndex(0);
		export.setLabel(exportmes);//匯出
		insertDB.setLabel(importmes);//匯入資料
		int sumNumber=existDBList.size()+nExistList.size();
		sucessNumber.setValue(String.valueOf(sumNumber));
		existResources_label.setValue(String.valueOf(existDBList.size()));
		noExistResources_label.setValue(String.valueOf(nExistList.size()));
		loadHeader();//加載Listbox header
		changeListbox();
	}
	
	public void languageLoad(){
		warnMessage=Labels.getLabel("resources_rsbatch_detail_result.warnMessage");//提示信息
		pageTitle=Labels.getLabel("resources_rsbatch_detail_result.pageTitle");//電子資源管理》副檔上傳》結果顯示
		sameResource=Labels.getLabel("resources_rsbatch_detail_result.sameResource");//有相同標題的電子資源
		noSameResource=Labels.getLabel("resources_rsbatch_detail_result.noSameResource");//沒有相同標題的電子資源
		exportmes=Labels.getLabel("resources_rsbatch_detail_result.exportmes");//匯出
		importmes=Labels.getLabel("resources_rsbatch_detail_result.importmes");//匯入資料
		title=Labels.getLabel("resources_rsbatch_detail_result.title");//題名
		noWork=Labels.getLabel("resources_rsbatch_detail_result.noWork");//未處理
		message1=Labels.getLabel("resources_rsbatch_detail_result.message1");//副檔新增失敗，數據有誤
		message2=Labels.getLabel("resources_rsbatch_detail_result.message2");//此筆副檔資料已存在
		message3=Labels.getLabel("resources_rsbatch_detail_result.message3");//主題
		message4=Labels.getLabel("resources_rsbatch_detail_result.message4");//不存在，是否需要新增？
		message5=Labels.getLabel("resources_rsbatch_detail_result.message5");//新增失敗，資料有誤
		message6=Labels.getLabel("resources_rsbatch_detail_result.message6");//資料庫類型
		colleage=Labels.getLabel("resources_rsbatch_detail_result.colleage");//學院
		suilt=Labels.getLabel("resources_rsbatch_detail_result.suilt");//系所
		errormes=Labels.getLabel("resources_rsbatch_detail_result.errormes");//錯誤信息
		reportTitle=Labels.getLabel("resources_rsbatch_detail_result.reportTitle");//匯入失敗資料列表
		titleNames=Labels.getLabel("resources_rsbatch_detail_result.titleNames");//相關題名,主題,類型,適用學院,適用系所,訂購學院,訂購系所
		warn1mes=Labels.getLabel("resources_rsbatch_detail_result.warn1mes");
		warn2mes=Labels.getLabel("resources_rsbatch_detail_result.warn2mes");
		warn3mes=Labels.getLabel("resources_rsbatch_detail_result.warn3mes");
		warn4mes=Labels.getLabel("resources_rsbatch_detail_result.warn4mes");
	}
	/**
	 * 加載Listbox header
	 */
	public void loadHeader(){
		String columnName=getDetailMap().get(detailType);
		Listhead head=new Listhead();
		for(int i=0;i<2;i++){
			Listheader header=new Listheader();
			if(i==0){
				header.setLabel(title);//題名
			}else{
				header.setLabel(columnName);
			}
			header.setWidth("200px");
			head.appendChild(header);
		}
		detailData.appendChild(head);
	}
	
	/**
	 * 下拉選單變動事件
	 */
	@Listen("onSelect=#isExist_list")
	public void onSelect$isExist_list(){
		changeListbox();
	}
	/**
	 * 加載數據
	 */
	public void changeListbox(){
		String tagType=isExist_list.getSelectedItem().getValue().toString();
		List<String[]> listData=new ArrayList<String[]>();
		//如果“沒有相同標題的電子資源”
		if(tagType.equals("nExist")){
			insertDB.setVisible(false);
			export.setVisible(true);
			listData=nExistList;
		}else if(tagType.equals("exist")){//如果“有相同標題的電子資源”
			insertDB.setVisible(true);
			export.setVisible(false);
			listData=existDBList;
			
		}
		if(listData.size()==0){
			insertDB.setDisabled(true);
			export.setDisabled(true);
		}else{
			insertDB.setDisabled(false);
			export.setDisabled(false);
		}
		String[][] model=new String[listData.size()][3];
		for(int i = 0;i<listData.size();i++){
			model[i]=listData.get(i);
		}
		detailData.setWidth("400px");
		ListModel lm=new SimpleListModel(model);
		detailData.setModel(lm);
		detailData.setItemRenderer(new Resources_rsbatch_detail_itemRenderer());
		detailData.setMold("paging");
		detailData.setPageSize(1000);
		
	}
	
	/**
	 * 匯入資料
	 */
	@Listen("onClick=#insertDB")
	public void onClick$insertDB(){
		ListModel lmodel=detailData.getModel();
		errorList=new ArrayList<String[]>();
		notDoExcelList=new ArrayList<String[]>();
		for(int i=0;i<lmodel.getSize();i++){
			String[] mode=(String[])lmodel.getElementAt(i);
			String[] rsd=mode[0].split(",");
			
			for(int j=0;j<rsd.length;j++){
				String resources_id=rsd[j];
				String error=insertDataToDB(resources_id,mode[2]);
				String[] errorMsg;
				//error==null表示未處理的資料
				if(error==null){
					errorMsg=new String[4];
					errorMsg[0]=noWork+" "+Labels.getLabel("resources_rsbatch_detail_result.noexist");//未處理
					errorMsg[1]=resources_id;
					errorMsg[2]=mode[1];
					errorMsg[3]=mode[2];
					notDoExcelList.add(errorMsg);
				}else if(!error.equals("")){
					errorMsg=new String[4];
					errorMsg[0]=error;
					errorMsg[1]=resources_id;
					errorMsg[2]=mode[1];
					errorMsg[3]=mode[2];
					errorList.add(errorMsg);
				}else{
					String resources_type=(String)Sessions.getCurrent().getAttribute("resources_Type");
					if(resources_type.trim().equals("EJ") || resources_type.trim().equals("EB")){
						try {
							((ResourcesMainEjebSolrSearch) SpringUtil
									.getBean("resourcesMainEjebSolrSearch"))
									.resources_main_ejeb_editData(resources_id);
						} catch (SQLException e) {
							log.error(e.getMessage(),e);
						}
					}else {
						try {
							((ResourcesMainDbwsSolrSearch) SpringUtil
									.getBean("resourcesMainDbwsSolrSearch"))
									.resources_main_dbws_editData(resources_id);
						} catch (SQLException e) {
							log.error(e.getMessage(),e);
						}
					}
					succesDataNumber++;
				}
			}
			
			
		}

		Sessions.getCurrent().setAttribute("notDoExcelList", notDoExcelList);
		Sessions.getCurrent().setAttribute("errorList", errorList);
		Sessions.getCurrent().setAttribute("succesDataNumber", succesDataNumber);
		
		//Executions.getCurrent().sendRedirect("/resources_rsbatch_detail_error.jsf");
		String url="resourcesFileImport/resourcesRsbatchDetailError.zul";
		ZkUtils.refurbishMethod(url);	
		
	}
	/**
	 * 匯入數據庫
	 * @param connFactory
	 * @param conn
	 * @param resources_id
	 * @param detail_name
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String insertDataToDB(String resourcesId,String detailName){
		resources_id=resourcesId;
		detail_name=detailName;
		String sql="";
		try{
		if(detailType.equals("name")){
			sql="select count(*) as counts from erm_resources_reltitle where resources_id='"+resources_id+"' and name='"+detail_name+"'";
			String count="";
			count=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(sql);
			if(count.equals("0")){
				ErmResourcesReltitle resRelTitle=new ErmResourcesReltitle();
				resRelTitle.setResourcesId(resources_id);
				resRelTitle.setRelatedTitleId(UUIDGenerator.getUUID());
				resRelTitle.setName(detail_name);
				try {
					((ErmResourcesReltitleService)SpringUtil.getBean("ermResourcesReltitleService")).create(resRelTitle);
				} catch (Exception e) {
					log.error("副檔新增失敗，數據有誤",e);
					//副檔新增失敗，數據有誤
					return message1;
				}
			}else{
				return message2;//此筆副檔資料已存在
			}
		}else if(detailType.equals("subject")){
			item_id=resources_type+"SUB";
			sql="select generalcode_id from erm_code_generalcode where item_id='"+item_id+"' and name1='"+detail_name+"' and history='N' and isdataeffid=1";
			generalcode_id=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(sql);
			if(generalcode_id.equals("")){
				return null;
				/*Messagebox.show(message2+detail_name+message4, Labels.getLabel("info"), Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								// TODO Auto-generated method stub
								int clickButton=(Integer) event.getData();
								if(clickButton==Messagebox.OK){
									String gSql="select count(*) from erm_code_generalcode where item_id='"+item_id+"'";
									String count="";
									count=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(gSql);
									//獲取主題流水號
									Long number = Long.parseLong(count) + 1;
									String tempId="DS";
									if(item_id.equals("EJSUB")){
										tempId="JS";
									}else if(item_id.equals("EBSUB")){
										tempId="BS";
									}else if(item_id.equals("WSSUB")){
										tempId="WS";
									}
									String g_id = tempId + RandomIDGenerator.fmtLong(number, 5);
									boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil
											.getBean("ermCodeGeneralCodeService")).save(item_id,g_id, detail_name, "",
													"N", "",webEmployee);
									if(saveStatus){
										ErmResourcesSubject subject=new ErmResourcesSubject();
										subject.setResourcesId(resources_id);
										subject.setSubjectId(g_id);
										try {
											((ErmResourcesSubjectService)SpringUtil.getBean("ermResourcesSubjectService")).create(subject);
										} catch (Exception e) {
											log.error("新增失敗，資料有誤",e);
										}
									}
								}else{
									yesOrNo="cancel";
								}
							}});
				if(!yesOrNo.equals("ok")){
					return null;
				}*/
			}else{
				sql="select count(*) as counts from erm_resources_subject where resources_id='"+resources_id+"' and subject_id='"+generalcode_id+"'";
				String count="";
				count=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(sql);
				if(count.equals("0")){
					ErmResourcesSubject subject=new ErmResourcesSubject();
					subject.setResourcesId(resources_id);
					subject.setSubjectId(generalcode_id);
					try {
						((ErmResourcesSubjectService)SpringUtil.getBean("ermResourcesSubjectService")).create(subject);
					} catch (Exception e) {
						log.error("新增失敗，資料有誤",e);
						return message5;//新增失敗，資料有誤
					}
				}else{
					return message2;//此筆副檔資料已存在
				}
			}
		}else if(detailType.equals("type")){
			String dbtype="";
			sql="select generalcode_id from erm_code_generalcode where item_id='DBTYPE' and name1='"+detail_name+"' and history='N' and isdataeffid=1";
			dbtype=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(sql);
			if(dbtype.equals("")){
				return null;
				/*//int num=Messagebox.show("資料庫類型"+detail_name+"不存在，是否需要新增？","提示信息",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
				int num=Messagebox.show(message6+detail_name+message4,warnMessage,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
				if(num==Messagebox.YES){
					sql="select max(generalcode_id) as gid from erm_code_generalcode where item_id='DBTYPE'";
					String gid="";
					gid=((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).getNameBySql(sql);
					String g=String.valueOf(Integer.parseInt(gid)+1);
					if(g.length()<gid.length()){
						g="0"+g;
					}
					boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil
							.getBean("ermCodeGeneralCodeService")).save("DBTYPE",g, detail_name, "",
									"N", "",webEmployee);
					if(saveStatus){
						ErmResourcesDbtype db_type=new ErmResourcesDbtype();
						db_type.setResourcesId(resources_id);
						db_type.setDbtypeId(g);
						try {
							((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).create(db_type);
						} catch (Exception e) {
							log.error("副檔新增失敗，資料有誤",e);
							return message1;//副檔新增失敗，資料有誤
						}
					}else{
						return message5;//新增失敗，資料有誤
					}
				}else{
					return null;
				}*/
			}else{
				ErmResourcesDbtype dbtypeTemp=((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).getDbtype(resources_id, dbtype);
				if(!"".equals(dbtypeTemp.getDbtypeId())&&dbtypeTemp.getDbtypeId()!=null){
					return message2;//此筆副檔資料已存在
				}else{
					ErmResourcesDbtype db_type=new ErmResourcesDbtype();
					db_type.setResourcesId(resources_id);
					db_type.setDbtypeId(dbtype);
					try {
						((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).create(db_type);
					} catch (Exception e) {
						log.error("副檔新增失敗，資料有誤",e);
						return message5;//新增失敗，資料有誤
					}
				}
			}
		}/*else if(detailType.equals("suitcollege")){//適用學院
			String id=checkCollege(detail_name);
			if(id!=null && !id.equals("")){
				IOerm_resources_scollege scollege=new IOerm_resources_scollege();
				scollege=scollege.selectOne(connFactory, conn, "suitcollege_id='"+id+"' and resources_id='"+resources_id+"'");
				if(scollege!=null && scollege.getResources_id()!=null){
					return message2;//此筆副檔資料已存在
				}else{
					scollege=new IOerm_resources_scollege();
					scollege.setResources_id(resources_id);
					scollege.setSuitcollege_id(id);
					if(scollege.insert(connFactory, conn)){
						scollege.save_log(connFactory, conn,scollege, "I");
						conn.commit();
					}else{
						return message1;//副檔新增失敗，資料有誤
					}
				}
			}else{
				if(id.equals("")){
					return message5;//新增失敗，資料有誤
				}
				return null;
			}
		}else if(detailType.equals("orderCollege")){//訂購學院
			String id=checkCollege(detail_name);
			if(id!=null && !id.equals("")){
				IOerm_resources_ocollege ocollege=new IOerm_resources_ocollege();
				ocollege=ocollege.selectOne(connFactory, conn, "ocollege_id='"+id+"' and resources_id='"+resources_id+"'");
				if(ocollege!=null && ocollege.getResources_id()!=null){
					return message2;//此筆副檔資料已存在
				}else{
					ocollege=new IOerm_resources_ocollege();
					ocollege.setResources_id(resources_id);
					ocollege.setOrdercollege_id(id);
					if(ocollege.insert(connFactory, conn)){
						ocollege.save_log(connFactory, conn,ocollege, "I");
						conn.commit();
					}else{
						return message1;//副檔新增失敗，資料有誤
					}
				}
			}else{
				if(id.equals("")){
					return message5;//新增失敗，資料有誤
				}
				return null;
			}
		}else if(detailType.equals("suitdep")){//適用系所
			String id=checkdept(detail_name);
			if(id!=null && !id.equals("")){
				IOerm_resources_suitdep suitdep=new IOerm_resources_suitdep();
				suitdep=suitdep.selectOne(connFactory, conn, "resources_id='"+resources_id+"' and suitdep_id='"+id+"'");
				if(suitdep!=null && suitdep.getResources_id()!=null){
					return message2;//此筆副檔資料已存在
				}else{
					suitdep=new IOerm_resources_suitdep();
					suitdep.setResources_id(resources_id);
					suitdep.setSuitdep_id(id);
					if(suitdep.insert(connFactory, conn)){
						suitdep.save_log(connFactory, conn,suitdep, "I");
						conn.commit();
					}else{
						return message1;//副檔新增失敗，資料有誤
					}
				}
			}else{
				if(id.equals("")){
					return message5;//新增失敗，資料有誤
				}
				return null;
			}
		}else if(detailType.equals("orderdep")){//訂購系所
			String id=checkdept(detail_name);
			if(id!=null && !id.equals("")){
				IOerm_resources_orderdep odep=new IOerm_resources_orderdep();
				odep=odep.selectOne(connFactory, conn, "resources_id='"+resources_id+"' and orderdep_id='"+id+"'");
				if(odep!=null && odep.getResources_id()!=null){
					return message2;//此筆副檔資料已存在
				}else{
					odep=new IOerm_resources_orderdep();
					odep.setResources_id(resources_id);
					odep.setOrderdep_id(id);
					if(odep.insert(connFactory, conn)){
						odep.save_log(connFactory, conn,odep, "I");
						conn.commit();
					}else{
						return message1;//副檔新增失敗，資料有誤
					}
				}
			}else{
				if(id.equals("")){
					return message5;//新增失敗，資料有誤
				}
				return null;
			}
		}*/
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return "";
	}
	
	/**
	 * 匯出
	 * @throws JRException 
	 */
	@Listen("onClick=#export")
	public void onClick$export() throws JRException{
		String name=getDetailMap().get(detailType);
		String title1[]={errormes,title,name};//"錯誤信息","題名"
		String eTitle[]={"errorMesg","title",detailType};
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String date=df.format(new Date());
		//報表內容
		List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
		Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report(reportTitle);//匯入失敗資料列表
		JasperReport jasperReport=rsbatch_report.getJasperReport(title1, eTitle);
		//報表賦值
		JRDataSource dataSource=new Resources_rsbatch_dataSource(eTitle,nExistList);
		JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
		jasperPrintList.add(jasperPrint);
		//生成excel報表
		JExcelApiExporter excelExporter = new JExcelApiExporter();
		excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		//報表路徑
		String webPath=((SystemProperties) SpringUtil
				.getBean("systemProperties")).systemDocumentPath
				+ SystemVariable.RSBATCH_PATH;
		String fileName=resources_type+"-"+detailType+"("+date+").xls";
		String filePath=webPath+fileName;
		excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filePath);
		excelExporter.exportReport();
		
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
	 * 將副檔名稱中英文放在map中，方便查找
	 * @return
	 */
	public Map<String,String> getDetailMap(){
		//String titleNames="相關題名,主題,類型,適用學院,適用系所,訂購學院,訂購系所";
		String[] name=titleNames.split(",");
		//String[] name={"相關題名","主題","類型","適用學院","適用系所","訂購學院","訂購系所"};
		String[] title={"name","subject","type","suitcollege","suitdep","orderCollege","orderdep"};
		Map<String,String> eMap=new HashMap<String,String>();
		for(int i=0;i<name.length;i++){
			eMap.put(title[i], name[i]);
		}
		return eMap;
	}
	


	 /**
	  * 根據學院name1查詢學院是否存在，存在返回學院id，不存在則詢問是否新增
	  * 返回null則表示此數據不處理
	  * @param connFactory
	  * @param conn
	  * @param college_name
	  * @return
	  */
	/* public String checkCollege(String college_name){
		 IOerm_code_college college=new IOerm_code_college();
		 college=college.selectOne(connFactory, conn, "name1='"+college_name+"'");
		 if(college!=null && college.getCollege_id()!=null){
			 return college.getCollege_id();
		 }else{
			 try {
				 //int num=Messagebox.show("學院 "+college_name+" 不存在，是否需要新增","提示信息",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
				int num=Messagebox.show(colleage+college_name+message4,warnMessage,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
				if(num==Messagebox.YES){
					String id=updateSeries_Param(connFactory, conn, "College_Number");
					college=new IOerm_code_college();
					college.setCollege_id(id);
					college.setName1(college_name);
					college.setHistory("N");
					if(college.insert(connFactory, conn)){
						conn.commit();
						return id;
					}
					return "";
				}else{
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 return null;
		 //String sql="select college_id from erm_code_college where name='"+college_name+"' and history='N'";
		 
	 }*/
	 
	 /**
	  * 
	  * 根據系所名稱檢查系所表中是否存在，存在返回系所id，不存在則詢問是否新增
	  * 返回null則表示此數據不處理
	  * @param connFactory
	  * @param conn
	  * @param dept_name
	  * @return
	  */
	/* public String checkdept(String dept_name){
		 IOerm_code_department dept=new IOerm_code_department();
		 dept=dept.selectOne(connFactory, conn, "name1='"+dept_name+"'");
		 if(dept!=null && dept.getDepartment_id()!=null){
			 return dept.getDepartment_id();
		 }else{
			try {
				//int num = Messagebox.show("系所 "+dept_name+" 不存在，是否需要新增","提示信息",Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
				int num = Messagebox.show(suilt+dept_name+message5,warnMessage,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);
                if(num==Messagebox.YES){
                	 String id=updateSeries_Param(connFactory, conn, "Dept_Number");
    				 dept=new IOerm_code_department();
    				 dept.setCollege_id("0001");
    				 dept.setDepartment_id(id);
    				 dept.setName1(dept_name);
    				 dept.setHistory("N");
    				 if(dept.insert(connFactory, conn)){
    					 conn.commit();
    					 return id;
    				 }else{
    					 return null;
    				 }
				}else{
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		 }
		 return null;
	 }*/
	 /**
	  * 獲取主題的流水號
	  */
	/* public String updateSeries_Param_bySubject(String serId){
		 String sercount="";//獲取當前編號
		 int num_len=0;//獲取當前編號的長度
		 String sql="select num_len,substr(ser_count,3) as ser_count from series_params where ser_id='"+serId+"'";
		 Statement st=null;
		 ResultSet rs=null;
		 try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				num_len=rs.getInt("num_len");
				sercount=rs.getString("ser_count");
			}
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		 
		 sercount= String.valueOf(Integer.parseInt(sercount)+1);//給當前的單據編號+1
		 while(sercount.length()<num_len-2){
			 sercount="0"+sercount;
		 }
		String update_sql="update series_params set ser_count='"+resources_type+sercount+"' where ser_id='"+serId+"'";
		 try {
		st=conn.createStatement();
		st.executeUpdate(update_sql);
		conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 return resources_type+sercount;
	 }*/
	 /**
	  * 獲取並更新學院、系所的流水號
	  * @param item_id
	  * @param value
	  */
	 /*public String updateSeries_Param(String serId){
		 String sercount="";//獲取當前編號
		 int num_len=0;//獲取當前編號的長度
		 String sql="select num_len,ser_count from series_params where ser_id='"+serId+"'";
		 Statement st=null;
		 ResultSet rs=null;
		 try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()){
				num_len=rs.getInt("num_len");
				sercount=rs.getString("ser_count");
			}
			rs.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		 sercount= String.valueOf(Integer.parseInt(sercount)+1);//給當前的單據編號+1
		 while(sercount.length()<num_len){
			 sercount="0"+sercount;
		 }
		String update_sql="update series_params set ser_count='"+sercount+"' where ser_id='"+serId+"'";
		 try {
		st=conn.createStatement();
		st.executeUpdate(update_sql);
		conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		 return sercount;
		
	 } */

}

/**
 * 為listbox加載數據
 * @author nj
 *
 */
class Resources_rsbatch_detail_itemRenderer implements ListitemRenderer{
	public void render(Listitem item, Object obj, int arg2) throws Exception {
		String[] data=(String[])obj;
		Listcell cell=new Listcell();
		Label label_id=new Label(data[0]);//resources_id
		label_id.setVisible(false);
		Label label_name=new Label(data[1]);//題名
		cell.appendChild(label_id);
		cell.appendChild(label_name);
		cell.setParent(item);
		new Listcell(data[2]).setParent(item);//副檔
		
	}
	
}