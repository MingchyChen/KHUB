package com.claridy.admin.composer;

import java.util.List;
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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.facade.WebErwSourceService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * 魏建國 nj
 * 管際合作管理集合
 */
public class WebErwSourceListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Wire
	private Textbox nameZhTw;
	
	@Wire
	private Textbox dbid;
	
	@Wire
	private Listbox webErwSourceListBox;
	
	@Wire Window webErwSourceWin;
	
	@WireVariable
	private List<WebErwSource> webErwSourceList;
	
	private String nameZhTwF;
	private String dbidF;
	
	
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			nameZhTwF=nameZhTw.getValue().trim();
			dbidF=dbid.getValue().trim();
			if((nameZhTwF!=null&&!nameZhTwF.equals(""))
					||(dbidF!=null&&!dbidF.equals(""))){
				getList();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"),Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("查詢失敗",e);
		}
		
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			nameZhTwF = null;
			dbidF = null;
			nameZhTw.setValue("");
			dbid.setValue("");
			getList();
		} catch (Exception e) {
			log.error("查詢失敗",e);
		}
	}
	
	private void getList() throws Exception{
		WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		webErwSourceList = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).searchWebErwSource(dbidF, nameZhTwF, webEmployee);
		ListModelList<WebErwSource> webErwSourceLML = new ListModelList<WebErwSource>(webErwSourceList);
		webErwSourceLML.setMultiple(true);
		webErwSourceListBox.setModel(webErwSourceLML);
	}
	
	@Listen("onClick=#addBtn")
	public void addWebErwSource(){
		webErwSourceWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/webErwSource/webErwSourceEdit.zul", this.getSelf(), null);
		webErwSourceWin.doModal();
	}
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deleteBtn")
	public void deleteWebErwSource(){
		try {
			int updateChecked=webErwSourceListBox.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webErwSourceListBox.getSelectedItems();
							WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for(Listitem webErwSourceListItem:listitem){
								WebErwSource webErwSource=webErwSourceListItem.getValue();
								((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).delWebErwSource(webErwSource);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSource.getUuid());
							}
							ZkUtils.refurbishMethod("webErwSource/webErwSource.zul");
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
	
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			showAll();
		} catch (Exception e) {
			log.error("初始化異常",e);
		} 
	}

}
