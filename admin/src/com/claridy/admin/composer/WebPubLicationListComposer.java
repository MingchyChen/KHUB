package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebPublication;
import com.claridy.facade.WebIndexInfoService;
import com.claridy.facade.WebPublicationService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 農業出版品 清單列
 * 2014/8/6
 */
public class WebPubLicationListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4736697491405942793L;
	
	@Wire
	private Listbox WebPubLicationLix;
	@Wire
	private Radiogroup displayrdb;
	@Wire
	private Textbox titletbox;
	
	private Window addWenPubLicationWin;
	
	
	
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebPublication> pubLicationList=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findAll(webEmployee);
			ListModelList<WebPublication> model=new ListModelList<WebPublication>(pubLicationList);
			model.setMultiple(true);
			WebPubLicationLix.setModel(model);
		} catch (Exception e) {
			log.error("webPubLication初始化異常"+e);
		}
	}
	
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			WebPublication webPubLication=new WebPublication();
			String title=titletbox.getValue().trim();
			title=XSSStringEncoder.encodeXSSString(title);
			webPubLication.setTitleZhTw(title);
			if(displayrdb.getSelectedItem()!=null){
				String isDisplay=displayrdb.getSelectedItem().getValue();
				webPubLication.setIsDisplay(Integer.parseInt(isDisplay));
			}else{
				webPubLication.setIsDisplay(-1);
			}
			if(title!=null&&!title.equals("")||(displayrdb.getSelectedItem()!=null)){
				
			}else{
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"),Labels.getLabel("info"));
				return;
			}
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebPublication> pubLicationList=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findByConditions(webPubLication,webEmployee);
			ListModelList<WebPublication> model=new ListModelList<WebPublication>(pubLicationList);
			model.setMultiple(true);
			WebPubLicationLix.setModel(model);
		} catch (WrongValueException e) {
			log.error("webPubLication查詢異常"+e);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			log.error("webPubLication查詢異常"+e);
		}
	}
	
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		try {
			titletbox.setValue("");
			displayrdb.setSelectedItem(null);
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebPublication> pubLicationList=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findAll(webEmployee);
			ListModelList<WebPublication> model=new ListModelList<WebPublication>(pubLicationList);
			model.setMultiple(true);
			WebPubLicationLix.setModel(model);
		} catch (Exception e) {
			log.error("顯示全部異常"+e);
		}
	}
	
	@Listen("onClick=#deleteBtn")
	public void delete(){
		try {
			int clickCount=WebPubLicationLix.getSelectedCount();
			if(clickCount>0){
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener<Event>() {

					public void onEvent(Event event) throws Exception {
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=WebPubLicationLix.getSelectedItems();
							WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for(Listitem webPublication:listitem){
								WebPublication webPubLication=webPublication.getValue();
								webPubLication.setIsDataEffid(0);
								((WebPublicationService)SpringUtil.getBean("webPublicationService")).deleteWebPubLication(webPubLication);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "webPubLication_"+webPubLication.getUuid());
							}
							ZkUtils.refurbishMethod("webPubLication/webPubLication.zul");
						}
					}
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.deleteBtnIsNull"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("刪除webPubLication異常"+e);
		}	
	}
	
	@Listen("onClick=#addBtn")
	public void insert(){
		addWenPubLicationWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/webPubLication/webPubLicationAdd.zul",null,null);
		addWenPubLicationWin.doModal();
	}
	
	@Listen("onClick=#startBtn")
	public void startIsDisplary(){
		try {
			int updateChecked=WebPubLicationLix.getSelectedCount();
			if(updateChecked>0){
				Set<Listitem> listitem=WebPubLicationLix.getSelectedItems();
				WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				WebPublication webPubLication;
				for(Listitem employee:listitem){
					webPubLication=employee.getValue();
					webPubLication.setIsDisplay(1);
					((WebPublicationService)SpringUtil.getBean("webPublicationService")).updateWebPubLication(webPubLication);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "webPubLication_"+WebPubLicationLix.getUuid());
				}
				ZkUtils.refurbishMethod("webPubLication/webPubLication.zul");
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.startBtnIsNull"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("開啟webIndexInfo狀態異常"+e);
		}
	}
	@Listen("onClick=#closeBtn")
	public void closeIsDisplay(){
		try {
			int updateChecked=WebPubLicationLix.getSelectedCount();
			if(updateChecked>0){
				Set<Listitem> listitem=WebPubLicationLix.getSelectedItems();
				WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				WebPublication webPubLication;
				for(Listitem employee:listitem){
					webPubLication=employee.getValue();
					webPubLication.setIsDisplay(0);
					((WebPublicationService)SpringUtil.getBean("webPublicationService")).updateWebPubLication(webPubLication);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "webPublication_"+webPubLication.getUuid());
				}
				ZkUtils.refurbishMethod("webPubLication/webPubLication.zul");
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.closeBtnIsNull"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("關閉webIndexInfo狀態異常"+e);
		}
	}
	
	
	
	 
}
