package com.claridy.admin.composer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesDbtype;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesOdetails;
import com.claridy.domain.ErmResourcesReltitle;
import com.claridy.domain.ErmResourcesScollegeLog;
import com.claridy.domain.ErmResourcesSubject;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeDbService;
import com.claridy.facade.ErmResMainDbwsService;
import com.claridy.facade.ErmResourcesDbtypeService;
import com.claridy.facade.ErmResourcesOdetailsService;
import com.claridy.facade.ErmResourcesReltitleService;
import com.claridy.facade.ErmResourcesScollegeLogService;
import com.claridy.facade.ErmResourcesSubjectService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * sunchao nj
 * 電子資料庫後分類列表頁
 * 2014/09/23
 */
public class ErmTypeResListComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -4495610820701239659L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	//資源名稱
	@Wire
	private Textbox resNameBox;
	//編號
	@Wire
	private Textbox resIdBox;
	@Wire
	private Textbox dbIdBox;
	//題名
	@Wire
	private Textbox titleBox;
	@Wire
	private Listbox relTitleLix;
	@Wire
	private Listbox subjectLix;
	@Wire
	private Listbox dbtypeLix;
	@Wire
	private Listbox odetailsLix;
	@Wire
	private Listbox recommonLix;
	@Wire
	private Div divDbtype;
	@Wire
	private Listheader dbNameLhd;
	@Wire
	private Label dbMessage;
	@Wire
	private String resourcesId;
	@Wire
	private String dbId;
	public static boolean iscanOpen = true;
	public static boolean iscanLoad = true;
	private WebEmployee webEmployee;
	@Listen("onClick=#listBtn")
	public void listBtn(){
		String url="";
		if(resourcesId!=null&&!"".equals(resourcesId)){
			String resStr=resourcesId.substring(0,2);
			if(resStr.equals("EJ")){
				url="ermResMainEjeb/ermResMainEjeb.zul";
			}else if(resStr.equals("DB")){
				url="ermResMainDbws/ermResMainDbws.zul";
			}
		}
		ZkUtils.refurbishMethod(url);
	}
	/**
	 * 新增相關題名
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#relTitleAddBtn")
	public void relTitleAddBtn(){
		Map map=new HashMap();
		map.put("resTitleId",  resIdBox.getValue());
		Window ermResMainDbwsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResMainDbws/relTitleAdd.zul", null, map);
		ermResMainDbwsAddWin.doModal();
	}
	/**
	 * 新增主題分類
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#subjectAddBtn")
	public void subjectAddBtn(){
		Map map=new HashMap();
		map.put("resourcesId",  resIdBox.getValue());
		Window ermResMainDbwsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResMainDbws/subjectAdd.zul", null, map);
		ermResMainDbwsAddWin.doModal();
	}
	/**
	 * 新增資料類型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#dbtypeAddBtn")
	public void dbtypeAddBtn(){
		Map map=new HashMap();
		map.put("resourcesId",  resIdBox.getValue());
		Window ermResMainDbwsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResMainDbws/dbtypeAdd.zul", null, map);
		ermResMainDbwsAddWin.doModal();
	}
	/**
	 * 新增採購細節
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#odetailsAddBtn")
	public void odetailsAddBtn(){
		Map map=new HashMap();
		map.put("resYearId",  resIdBox.getValue());
		Window ermResMainDbwsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResMainDbws/odetailsAdd.zul", null, map);
		ermResMainDbwsAddWin.doModal();
	}
	/**
	 * 新增推薦資源單位
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#recommonAddBtn")
	public void recommonAddBtn(){
		Map map=new HashMap();
		map.put("resourcesId",  resIdBox.getValue());
		if(resourcesId!=null&&!"".equals(resourcesId)){
			String resStr=resourcesId.substring(0,2);
			if(resStr.equals("DB")||resStr.equals("WS")){
				map.put("dbId",  "");
			}else{
				map.put("dbId",  dbIdBox.getValue());
			}
		}
		Window ermResMainDbwsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResMainDbws/ermRecommonAdd.zul", null, map);
		ermResMainDbwsAddWin.doModal();
	}
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			resourcesId=Executions.getCurrent().getParameter("resourcesId");
			dbId=Executions.getCurrent().getParameter("dbId");
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ErmResourcesMainfileV resourcesMainfile=((ErmResMainDbwsService)SpringUtil.getBean("ermResMainDbwsService")).getResMainfileByResId(resourcesId);
			if(resourcesMainfile.getTypeId()!=null&&resourcesMainfile.getTypeId().equals("DB")){
				resNameBox.setValue(Labels.getLabel("ermSysNotifyConfig.electronicDatabase"));
			}else if(resourcesMainfile.getTypeId()!=null&&resourcesMainfile.getTypeId().equals("EJ")){
				resNameBox.setValue(Labels.getLabel("ermSysNotifyConfig.electronicJournals"));
			}else if(resourcesMainfile.getTypeId()!=null&&resourcesMainfile.getTypeId().equals("EB")){
				resNameBox.setValue(Labels.getLabel("ermSysNotifyConfig.eBook"));
			}else{
				resNameBox.setValue(Labels.getLabel("ermSysNotifyConfig.netWorkResource"));
			}
			resIdBox.setValue(resourcesMainfile.getResourcesId());
			if(!StringUtils.isBlank(dbId)){
				ZkUtils.setSessionAttribute("typeDbId", dbId);
			}else{
				dbId=(String) ZkUtils.getSessionAttribute("typeDbId");
			}
			dbIdBox.setValue((String) ZkUtils.getSessionAttribute("typeDbId"));
			titleBox.setValue(resourcesMainfile.getTitle());
			//相關題名
			List<ErmResourcesReltitle> relTitleResult = ((ErmResourcesReltitleService) SpringUtil
					.getBean("ermResourcesReltitleService")).findReltitleList(resourcesId);
			ListModelList<ErmResourcesReltitle> relTitleTmpLML = new ListModelList<ErmResourcesReltitle>(
					relTitleResult);
			relTitleTmpLML.setMultiple(true);
			relTitleLix.setModel(relTitleTmpLML);
			//主題分類
			List<ErmResourcesSubject> subjectResult = ((ErmResourcesSubjectService) SpringUtil
					.getBean("ermResourcesSubjectService")).findSubjectList(resourcesId);
			ListModelList<ErmResourcesSubject> subjectTmpLML = new ListModelList<ErmResourcesSubject>(
					subjectResult);
			subjectTmpLML.setMultiple(true);
			subjectLix.setModel(subjectTmpLML);
			
			if(resourcesId!=null&&!"".equals(resourcesId)){
				String resStr=resourcesId.substring(0,2);
				if(resStr.equals("EJ")){
					divDbtype.setStyle("display:none;");
				}else{
					divDbtype.setStyle("display:block;");
					//資料類型
					List<ErmResourcesDbtype> dbTypeResult = ((ErmResourcesDbtypeService) SpringUtil
							.getBean("ermResourcesDbtypeService")).findDbtypeList(resourcesId);
					ListModelList<ErmResourcesDbtype> dbTypeTmpLML = new ListModelList<ErmResourcesDbtype>(
							dbTypeResult);
					dbTypeTmpLML.setMultiple(true);
					dbtypeLix.setModel(dbTypeTmpLML);
				}
			}
			
			//採購細節
			List<ErmResourcesOdetails> oDetailsResult = ((ErmResourcesOdetailsService) SpringUtil
					.getBean("ermResourcesOdetailsService")).findOdetailsList(resourcesId);
			ListModelList<ErmResourcesOdetails> oDetailsTmpLML = new ListModelList<ErmResourcesOdetails>(
					oDetailsResult);
			oDetailsTmpLML.setMultiple(true);
			odetailsLix.setModel(oDetailsTmpLML);
			//推薦資源單位
			List<ErmResourcesScollegeLog> recommonOrgResult = ((ErmResourcesScollegeLogService) SpringUtil
					.getBean("ermResourcesScollegeLogService")).findScoLogList(resourcesId);
			ListModelList<ErmResourcesScollegeLog> recommonOrgTmpLML = new ListModelList<ErmResourcesScollegeLog>(
					recommonOrgResult);
			recommonOrgTmpLML.setMultiple(true);
			recommonLix.setModel(recommonOrgTmpLML);
			
			if(resourcesId!=null&&!"".equals(resourcesId)){
				String resStr=resourcesId.substring(0,2);
				if(!resStr.equals("DB")&&!resStr.equals("WS")){
					dbNameLhd.setLabel(Labels.getLabel("ermResourcesConfig.dbid"));
					ErmCodeDb ermCodeDb=((ErmCodeDbService) SpringUtil
							.getBean("ermCodeDbService")).findcodeDbByDbId(dbId, webEmployee);
					dbMessage.setValue(Labels.getLabel("ermTypeRes.dbMessage")+ermCodeDb.getName());
				}else{
					dbMessage.setVisible(false);
				}
			}
			
		} catch (Exception e) {
			log.error("查詢電子資料庫後分類列表頁集合出錯",e);
		}
	}
	//刪除相關題名
	@Listen("onClick=#delRelTitleBtn")
	public void delRelTitleBtn(){
		try {
			int updateChecked=relTitleLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=relTitleLix.getSelectedItems();
							for(Listitem relTitle:listitem){
								ErmResourcesReltitle resRelTitleTemp=relTitle.getValue();
								((ErmResourcesReltitleService)SpringUtil.getBean("ermResourcesReltitleService")).delete(resRelTitleTemp);
							}
							String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
							ZkUtils.refurbishMethod(url);
							//更新solr數據
							if(resourcesId!=null&&!"".equals(resourcesId)){
								String resStr=resourcesId.substring(0,2);
								if(resStr.equals("EJ")||resStr.equals("EB")){
									try {
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}else{
									try {
										((ResourcesMainDbwsSolrSearch) SpringUtil
												.getBean("resourcesMainDbwsSolrSearch"))
												.resources_main_dbws_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}
							}
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除相關題名",e);
		}
	}
	//刪除主題分類
	@Listen("onClick=#delSubjectBtn")
	public void delSubjectBtn(){
		try {
			int updateChecked=subjectLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=subjectLix.getSelectedItems();
							for(Listitem resSubject:listitem){
								ErmResourcesSubject resSubjectTemp=resSubject.getValue();
								((ErmResourcesSubjectService)SpringUtil.getBean("ermResourcesSubjectService")).delete(resSubjectTemp);
							}
							String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
							ZkUtils.refurbishMethod(url);
							//更新solr數據
							if(resourcesId!=null&&!"".equals(resourcesId)){
								String resStr=resourcesId.substring(0,2);
								if(resStr.equals("EJ")||resStr.equals("EB")){
									try {
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}else{
									try {
										((ResourcesMainDbwsSolrSearch) SpringUtil
												.getBean("resourcesMainDbwsSolrSearch"))
												.resources_main_dbws_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}
							}
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除主題分類",e);
		}
	}
	//刪除資料類型
	@Listen("onClick=#delDbtypeBtn")
	public void delDbtypeBtn(){
		try {
			int updateChecked=dbtypeLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=dbtypeLix.getSelectedItems();
							for(Listitem resSubject:listitem){
								ErmResourcesDbtype resSubjectTemp=resSubject.getValue();
								((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).delete(resSubjectTemp);
							}
							String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
							ZkUtils.refurbishMethod(url);
							//更新solr數據
							if(resourcesId!=null&&!"".equals(resourcesId)){
								String resStr=resourcesId.substring(0,2);
								if(resStr.equals("EJ")||resStr.equals("EB")){
									try {
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}else{
									try {
										((ResourcesMainDbwsSolrSearch) SpringUtil
												.getBean("resourcesMainDbwsSolrSearch"))
												.resources_main_dbws_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}
							}
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除主題分類",e);
		}
	}
	//刪除採購細節
	@Listen("onClick=#delOdetailsBtn")
	public void delOdetailsBtn(){
		try {
			int updateChecked=odetailsLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=odetailsLix.getSelectedItems();
							for(Listitem resSubject:listitem){
								ErmResourcesOdetails resSubjectTemp=resSubject.getValue();
								((ErmResourcesOdetailsService)SpringUtil.getBean("ermResourcesOdetailsService")).delete(resSubjectTemp);
							}
							String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
							ZkUtils.refurbishMethod(url);
							//更新solr數據
							if(resourcesId!=null&&!"".equals(resourcesId)){
								String resStr=resourcesId.substring(0,2);
								if(resStr.equals("EJ")||resStr.equals("EB")){
									try {
										((ResourcesMainEjebSolrSearch) SpringUtil
												.getBean("resourcesMainEjebSolrSearch"))
												.resources_main_ejeb_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}else{
									try {
										((ResourcesMainDbwsSolrSearch) SpringUtil
												.getBean("resourcesMainDbwsSolrSearch"))
												.resources_main_dbws_editData(resourcesId);
									} catch (SQLException e) {
										log.error("更新solr數據報錯",e);
									}
								}
							}
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除主題分類:",e);
		}
	}
	//刪除推薦資源單位
	@Listen("onClick=#delRecommonBtn")
	public void delRecommonBtn(){
		try {
			int updateChecked=recommonLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=recommonLix.getSelectedItems();
							for(Listitem recommon:listitem){
								ErmResourcesScollegeLog recommonScollege=recommon.getValue();
								((ErmResourcesScollegeLogService) SpringUtil
										.getBean("ermResourcesScollegeLogService")).delete(recommonScollege);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										webEmployee.getEmployeeName(), "ermRecommon_"+resourcesId+recommonScollege.getSuitcollegeId());
							}
							String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除推薦資源單位:",e);
		}
	}
}
