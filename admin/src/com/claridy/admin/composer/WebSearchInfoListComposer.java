package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;
import com.claridy.facade.WebSearchInfoService;
import com.claridy.facade.WebSysLogService;

/**
 * zjgao nj
 * 查詢說明管理
 * 2014/07/17
 */
public class WebSearchInfoListComposer extends SelectorComposer<Component> {

	
	private static final long serialVersionUID = 1067542429670217400L;

	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected List<WebSearchInfo> webSearchInfoList;
	@Wire
	protected Listbox webSearchInfolbx;
	@Wire
	protected Textbox titleName;
	@Wire
	protected WebSearchInfo webSearchInfo;
	@Wire
	protected Window addSearchInfoWin;
	private final Logger log = LoggerFactory.getLogger(getClass());
	

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			// TODO Auto-generated method stub
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webSearchInfoList = ((WebSearchInfoService) SpringUtil
					.getBean("webSearchInfoService"))
					.findSearchInfoAll(webEmployee);
			ListModelList<WebSearchInfo> tmpLML = new ListModelList<WebSearchInfo>(
					webSearchInfoList);
			tmpLML.setMultiple(true);
			webSearchInfolbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("WebSearchInfoList初始化異常"+e);
		}
	}

	@Listen("onClick = #showAll")
	public void searchAll(){
		try {
			webSearchInfoList = ((WebSearchInfoService) SpringUtil
					.getBean("webSearchInfoService"))
					.findSearchInfoAll(webEmployee);
			ListModelList<WebSearchInfo> tmpLML = new ListModelList<WebSearchInfo>(
					webSearchInfoList);
			tmpLML.setMultiple(true);
			webSearchInfolbx.setModel(tmpLML);
			titleName.setValue("");
		} catch (WrongValueException e) {
			log.error("WebSearchInfoList顯示全部異常"+e);
		}
	}
	@Listen("onClick=#pagSearchBtn")
	public void findwebSearchInfoByNameZhTw(){
		String tempNameZhTw=titleName.getText().trim().toString();
		if (StringUtils.isBlank(tempNameZhTw)) {
			ZkUtils.showExclamation(Labels.getLabel("inputString"),
					Labels.getLabel("warn"));
			titleName.focus();
			return;
		}
		webSearchInfoList = ((WebSearchInfoService) SpringUtil
				.getBean("webSearchInfoService"))
				.findSearchInfoByNameZhTw(tempNameZhTw, webEmployee);
		ListModelList<WebSearchInfo> tmpLML = new ListModelList<WebSearchInfo>(
				webSearchInfoList);
		tmpLML.setMultiple(true);
		webSearchInfolbx.setModel(tmpLML);
	}
	@Listen("onClick=#deleteBtn")
	public void deleteSearchInfoByUuid(){
		try {
			int updateChecked=webSearchInfolbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webSearchInfolbx.getSelectedItems();
							for(Listitem employee:listitem){
								webSearchInfo=employee.getValue();
								//List<WebSearchInfo> websearchInfoList=((WebSearchInfoService)SpringUtil.getBean("webSearchInfoService")).findedtAddList("uuid", webSearchInfo.getUuid());
								((WebSearchInfoService)SpringUtil.getBean("webSearchInfoService")).deleteSearchInfobyUuid(webSearchInfo);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webSearchInfo.getUuid(),"websearchinfo_"+webSearchInfo.getUuid());
							}
							String url="queryExplain/queryExplain.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("webSearchInfoList刪除異常"+e);
		}
	}
	@Listen("onClick=#addBtn")
	public void addWebEmployee(){
		addSearchInfoWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/queryExplain/queryExplainAdd.zul", null, null);
		addSearchInfoWin.doModal();
	}
	@Listen("onClick = #open")
	public void isOpen(){
		try {
			int updateChecked=webSearchInfolbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion(  Labels.getLabel("sureOpenZt"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webSearchInfolbx.getSelectedItems();
							for(Listitem employee:listitem){
								webSearchInfo=employee.getValue();
								webSearchInfo.setIsDisplay(1);
								((WebSearchInfoService)SpringUtil.getBean("webSearchInfoService")).updateSearchInfo(webSearchInfo);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webSearchInfo.getUuid(),"websearchinfo_"+webSearchInfo.getUuid());
							}
							String url="queryExplain/queryExplain.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("pleaseChooseLastOneOpen"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("webSearchInfoList修改異常"+e);
		}
	}
	@Listen("onClick = #close")
	public void isClose(){
		try {
			int updateChecked=webSearchInfolbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureCloseZt"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webSearchInfolbx.getSelectedItems();
							for(Listitem employee:listitem){
								webSearchInfo=employee.getValue();
								webSearchInfo.setIsDisplay(0);
								((WebSearchInfoService)SpringUtil.getBean("webSearchInfoService")).updateSearchInfo(webSearchInfo);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webSearchInfo.getUuid(),"websearchinfo_"+webSearchInfo.getUuid());
							}
							String url="queryExplain/queryExplain.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("pleaseChooseLastOneClose"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("webSearchInfoList修改異常"+e);
		}
	}
}
