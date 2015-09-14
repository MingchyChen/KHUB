package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebErwSourceUnit;
import com.claridy.facade.WebErwSourceService;
import com.claridy.facade.WebErwSourceUnitService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * 魏建國 nj
 * 管際合作管理新增/編輯資料庫
 */
public class WebErwSourceComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Wire
	private Label message;
	
	@Wire
	private Textbox nameZhTw;
	
	@Wire
	private Textbox dbid;
	
	@Wire
	private Textbox publisher;
	
	@Wire
	private Button cancelBtn;
	
	@Wire
	private Button addWebErwSourceUnit;
	@Wire
	private Listbox webErwSourceUnitListBox;
	
	@Wire Window addWebErwSourceWin;
	
	private WebErwSource webErwSource;
	
	private String nameZhTwF;
	private String dbidF;
	private String publisherF;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String webErwSourceId=map.get("webErwSourceId");
			
			
			webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).getWebErwSourceByUUID(webErwSourceId);
			
			if(webErwSource == null){
				webErwSource = new WebErwSource();
				addWebErwSourceUnit.setDisabled(true);
				addWebErwSourceWin.setTitle(Labels.getLabel("add"));
				cancelBtn.setLabel(Labels.getLabel("saveCancel"));
			}else{
				addWebErwSourceUnit.setDisabled(false);
				nameZhTw.setValue(webErwSource.getNameZhTw());
				dbid.setValue(webErwSource.getDbid());
				publisher.setValue(webErwSource.getPublisher());
				searchUnit();
				addWebErwSourceWin.setTitle(Labels.getLabel("edit"));
				cancelBtn.setLabel(Labels.getLabel("updCancel"));
			}
		} catch (Exception e) {
			log.error("初始化異常",e);
		}
	}
	
	public void searchUnit(){
		if(webErwSource.getWebErwSources() != null){
			List<WebErwSourceUnit> webErwSourceUnitList = webErwSource.getWebErwSources();
			ListModelList<WebErwSourceUnit> webErwSourceLML = new ListModelList<WebErwSourceUnit>(webErwSourceUnitList);
			webErwSourceLML.setMultiple(true);
			webErwSourceUnitListBox.setModel(webErwSourceLML);
		}
	}
	
	@Listen("onClick=#saveBtn")
	public void saveBtn(){
		try {
			nameZhTwF = XSSStringEncoder.encodeXSSString(nameZhTw.getValue().trim());
			dbidF = XSSStringEncoder.encodeXSSString(dbid.getValue().trim());
			publisherF = XSSStringEncoder.encodeXSSString(publisher.getValue().trim());
			WebErwSource webErwSourcetmp=((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).getWebErwSourceByDBID(dbidF);
			Boolean isExitDBID = false;
			if(webErwSourcetmp!=null&&webErwSourcetmp.getIsDataEffid()==1){
				isExitDBID=true;
			}
			if("".equals(nameZhTwF)){
				ZkUtils.showExclamation( Labels.getLabel("webErwSource.nameZhTwcheck"),Labels.getLabel("info"));
				nameZhTw.focus();
			}else if("".equals(dbidF)){
				ZkUtils.showExclamation(Labels.getLabel("webErwSource.dbidcheck"),Labels.getLabel("info"));
				dbid.focus();
			}else if(isExitDBID && !dbidF.equals(webErwSource.getDbid())){
				ZkUtils.showExclamation(Labels.getLabel("webErwSource.dbidcheckexit"),Labels.getLabel("info"));
				dbid.focus();
			}else{
				message.setValue("");
				webErwSource.setNameZhTw(nameZhTwF);
				webErwSource.setDbid(dbidF);
				webErwSource.setPublisher(publisherF);
				webErwSource.setIsDataEffid(1);
				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				if(webErwSource.getUuid() != null && !"".equals(webErwSource.getUuid())){
					webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).updateWebErwSource(webErwSource);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSource.getUuid());
				}else{
					webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).addWebErwSource(webErwSource);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSource.getUuid());
				}
				
				ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
				addWebErwSourceUnit.setDisabled(false);
				webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).getWebErwSourceByUUID(webErwSource.getUuid());
				searchUnit();
				addWebErwSourceWin.detach();
				Map<String, String> map=new HashMap<String, String>();
				map.put("webErwSourceId",  webErwSource.getUuid());
				Window newAdd=(Window)com.claridy.common.util.ZkUtils.createComponents("/WEB-INF/pages/system/webErwSource/webErwSourceEdit.zul", null,
						map);
				newAdd.doModal();
			}
			
		} catch (Exception e) {
			log.error("保存異常",e);
			ZkUtils.showInformation(Labels.getLabel("saveFailed"),Labels.getLabel("info"));
		}
		
	}
	
	@Listen("onClick=#cancelBtn")
	public void cancelBtn(){
		ZkUtils.refurbishMethod("webErwSource/webErwSource.zul");
		addWebErwSourceWin.detach();
	}
	
	@Listen("onClick=#addWebErwSourceUnit")
	public void addWebErwSourceUnit(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("webErwSourceId", webErwSource.getUuid());
		Window newAdd=(Window)com.claridy.common.util.ZkUtils.createComponents("/WEB-INF/pages/system/webErwSource/webErwSourceUnitEdit.zul", this.getSelf(),
				map);
		newAdd.doModal();
	}
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deleteBtn")
	public void deleteWebErwSource(){
		try {
			int updateChecked=webErwSourceUnitListBox.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webErwSourceUnitListBox.getSelectedItems();
							WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for(Listitem webErwSourceUnitListItem:listitem){
								WebErwSourceUnit webErwSourceUnit=webErwSourceUnitListItem.getValue();
								((WebErwSourceUnitService)SpringUtil.getBean("webErwSourceUnitService")).delWebErwSourceUnit(webErwSourceUnit);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSource.getUuid());
							}
							webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).getWebErwSourceByUUID(webErwSource.getUuid());
							searchUnit();
						}
					}
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("刪除webAccount異常",e);
		}
	}
	
//	@GlobalCommand
//	public void refreshUnit(){
//		searchUnit();
//	}
	
	public String getNameZhTwF() {
		return nameZhTwF;
	}

	public String getDbidF() {
		return dbidF;
	}

	public String getPublisherF() {
		return publisherF;
	}
	
	public WebErwSource getWebErwSource(){
		return webErwSource;
	}

}
