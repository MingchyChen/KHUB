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
import com.claridy.domain.WebRellink;
import com.claridy.facade.WebRelLinkService;
import com.claridy.facade.WebSearchInfoService;
import com.claridy.facade.WebSysLogService;
/**
 * zjgao nj
 * 首頁上方鏈接管理
 * 2014/07/11
 */
public class FirstTopListComposer extends SelectorComposer<Component>{

	
	private static final long serialVersionUID = 7594317914367068775L;
	@Wire
	private Textbox linkName;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private List<WebRellink> webRelLinkList;
	@Wire
	protected Listbox webRelLinklbx;
	@Wire
	protected WebRellink webRellink;
	@Wire
	private Window addWebRelLink;
	private final Logger log = LoggerFactory.getLogger(getClass());

	
	
	
	


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Listen("onClick = #showAll")
	public void searchAll(){
		try {
			webRelLinkList = ((WebRelLinkService) SpringUtil
					.getBean("webRelLinkService")).findwebRelLinkAll(webEmployee);
			ListModelList<WebRellink> tmpLML = new ListModelList<WebRellink>(
					webRelLinkList);
			tmpLML.setMultiple(true);
			webRelLinklbx.setModel(tmpLML);
			linkName.setValue("");
		} catch (WrongValueException e) {
			log.error("FirstTop顯示全部異常"+e);
		}
	}
	@Listen("onClick = #pagSearchBtn")
	public void searchBynameZhTw(){
		try {
			String nameZhTw=linkName.getText().trim().toString();
			if (StringUtils.isBlank(nameZhTw)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				linkName.focus();
				return;
			}
			webRelLinkList = ((WebRelLinkService) SpringUtil
					.getBean("webRelLinkService")).findwenRelLinkBynameZhTw(nameZhTw,webEmployee);
			ListModelList<WebRellink> tmpLML = new ListModelList<WebRellink>(
					webRelLinkList);
			tmpLML.setMultiple(true);
			webRelLinklbx.setModel(tmpLML);
		} catch (WrongValueException e) {
			log.error("FirstTopList條件查詢異常"+e);
		}
	}

	@Listen("onClick=#deleteBtn")
	public void deleteWebEmployee(){
		try {
			int updateChecked=webRelLinklbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webRelLinklbx.getSelectedItems();
							for(Listitem employee:listitem){
								webRellink=employee.getValue();
								List<WebRellink> webRellinkList=((WebRelLinkService)SpringUtil.getBean("webRelLinkService")).findedtAddList("uuid", webRellink.getUuid());
								/*if(webRellinkList.size()>0){
									ZkUtils.showExclamation(
											Labels.getLabel("webOrg.weborgLix.existOrg"),
											Labels.getLabel("warn"));
									return;
								}
								int sum=((WebOrgListService)SpringUtil.getBean("webRelLinkService")).getEmpAndAccOrgSum(webRellink.getUuid());
								if(sum>0){
									ZkUtils.showExclamation(
											Labels.getLabel("webOrg.weborgLix.existOrg"),
											Labels.getLabel("warn"));
									return;
								}*/
								((WebRelLinkService)SpringUtil.getBean("webRelLinkService")).deleteWebRelLink(webRellink);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webRellink.getUuid(),"webrellink_"+webRellink.getUuid());
							}
							String url="topLink/firstTopLink.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectOneData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("FirstTopList刪除異常"+e);
		}
	}
	
	@Listen("onClick=#addBtn")
	public void addWebEmployee(){
		addWebRelLink=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/topLink/firstTopLinkAdd.zul", null, null);
		addWebRelLink.doModal();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp); 
			//獲取用戶資訊
			webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			
			webRelLinkList = ((WebRelLinkService) SpringUtil
					.getBean("webRelLinkService")).findwebRelLinkAll(webEmployee);
			ListModelList<WebRellink> tmpLML = new ListModelList<WebRellink>(
					webRelLinkList);
			tmpLML.setMultiple(true);
			webRelLinklbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("FirstTopList初始化異常"+e);
		}
	}
	@Listen("onClick = #open")
	public void isOpen(){
		try {
			int updateChecked=webRelLinklbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion(  Labels.getLabel("sureOpenZt"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webRelLinklbx.getSelectedItems();
							for(Listitem employee:listitem){
								webRellink=employee.getValue();
								webRellink.setIsDisplay(1);
								((WebRelLinkService)SpringUtil.getBean("webRelLinkService")).updateWebRelLink(webRellink);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webRellink.getUuid(),"webrellink_"+webRellink.getUuid());
							}
							String url="topLink/firstTopLink.zul";
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
			int updateChecked=webRelLinklbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion(  Labels.getLabel("sureOpenZt"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=webRelLinklbx.getSelectedItems();
							for(Listitem employee:listitem){
								webRellink=employee.getValue();
								webRellink.setIsDisplay(0);
								((WebRelLinkService)SpringUtil.getBean("webRelLinkService")).updateWebRelLink(webRellink);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webRellink.getUuid(),"webrellink_"+webRellink.getUuid());
							}
							String url="topLink/firstTopLink.zul";
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
	

}
