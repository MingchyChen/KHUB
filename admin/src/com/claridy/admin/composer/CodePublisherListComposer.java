package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.WebSysLogService;
/**
 * 
 * engin
 * 代理商出版商資料List
 * 2014/07/28
 *
 */
public class CodePublisherListComposer extends SelectorComposer<Component> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4495610820701239659L;
		@Wire
		private Listbox codePublisherLbx;
		@Wire
		private Textbox publisherIdBox;
		@Wire
		private Textbox publisherNameBox;
		@WireVariable
		private List<ErmCodePublisher> ermCodePublisherList;
		@Wire
		protected List<ErmCodeGeneralCode>  ermCodeGeneralCodeList;
		@WireVariable
		private List<WebOrg> webOrgList;
		@Wire
		private Radiogroup orOrAnd;
		@Wire
		private Window addCodePublisherWin;
		@WireVariable
		private ErmCodePublisher ermCodePublisher;
		@WireVariable
		private WebEmployee webEmployee;
		private final Logger log = LoggerFactory.getLogger(getClass());
		public ErmCodePublisher getErmCodePublisher() {
			return ermCodePublisher;
		}

		public void setErmCodePublisher(ErmCodePublisher ermCodePublisher) {
			this.ermCodePublisher = ermCodePublisher;
		}
		
		public WebEmployee getWebEmployee() {
			return webEmployee;
		}

		public void setWebEmployee(WebEmployee webEmployee) {
			this.webEmployee = webEmployee;
		}

		@Override
		public void doAfterCompose(Component comp) throws Exception {
			super.doAfterCompose(comp); 			
			ermCodeGeneralCodeList=((CodePublisherService) SpringUtil.getBean("codePublisherService")).findErmCodeGeneralCodeByItemId("CONTACT");
			
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			//初始頁面加載
			ermCodePublisherList = ((CodePublisherService) SpringUtil
					.getBean("codePublisherService")).findAll(webEmployee);
			
			List<ErmCodePublisher> tempErmCodePublisherList = ermCodePublisherList;
			for (ErmCodePublisher ermColdPublisher : tempErmCodePublisherList) {
				//默認全部為空
				ermColdPublisher.setContactName("");
				for(int i=0;i<ermCodeGeneralCodeList.size();i++){
					ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
					String contact = ermColdPublisher.getContact();
					if(contact != null && !"".equals(contact)){
						if(contact.equals(tempEcgc.getGeneralcodeId())){
							ermColdPublisher.setContactName(tempEcgc.getName1());
						}
					}
				}
			}
			
			ListModelList<ErmCodePublisher> tmpLML = new ListModelList<ErmCodePublisher>(
					ermCodePublisherList);
			tmpLML.setMultiple(true);
			codePublisherLbx.setModel(tmpLML);
		}
		
		@Listen("onClick=#pagSearchBtn")
		public void search(){
			try {
				String publisherId=publisherIdBox.getValue().trim();
				String publisherName=publisherNameBox.getValue().trim();
				if (StringUtils.isBlank(publisherId)&&StringUtils.isBlank(publisherName)) {
					ZkUtils.showExclamation(
							Labels.getLabel("inputString"),
							Labels.getLabel("warn"));
					publisherIdBox.focus();
					return;
				}
				XSSStringEncoder.encodeXSSString(publisherId);
				XSSStringEncoder.encodeXSSString(publisherName);
				
				ermCodeGeneralCodeList=((CodePublisherService) SpringUtil.getBean("codePublisherService")).findErmCodeGeneralCodeByItemId("CONTACT");
				WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				List<ErmCodePublisher> ermCodePublisherList=((CodePublisherService)SpringUtil.getBean("codePublisherService")).search(publisherId, publisherName, webEmployee);
				
				List<ErmCodePublisher> tempErmCodePublisherList = ermCodePublisherList;
				for (ErmCodePublisher ermColdPublisher : tempErmCodePublisherList) {
					//默認全部為請選擇
					ermColdPublisher.setContactName("");
					for(int i=0;i<ermCodeGeneralCodeList.size();i++){
						ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
						String contact = ermColdPublisher.getContact();
						if(contact != null && !"".equals(contact)){
							if(contact.equals(tempEcgc.getGeneralcodeId())){
								ermColdPublisher.setContactName(tempEcgc.getName1());
							}
						}
					}
				}
				
				ListModelList<ErmCodePublisher> ErmCodePublisherModel=new ListModelList<ErmCodePublisher>(ermCodePublisherList);
				ErmCodePublisherModel.setMultiple(true);
				codePublisherLbx.setModel(ErmCodePublisherModel);
			} catch (Exception e) {
				log.error("查詢ermCodePublisher異常"+e);
			} 
		}
		
		@Listen("onClick=#addBtn")
		public void addWebEmployee(){
			addCodePublisherWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/codePublisher/codePublisherAdd.zul", this.getSelf(), null);
			addCodePublisherWin.doModal();
		}
				
		@SuppressWarnings("rawtypes")
		@Listen("onClick=#deleteBtn")
		public void deleteWebEmployee(){
			int updateChecked=codePublisherLbx.getSelectedCount();
			try {
				if(updateChecked>0){
					//“你確定要刪除該資料嗎？”
					ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
						public void onEvent(Event event){
							int clickButton=(Integer) event.getData();
							if(clickButton==Messagebox.OK){
								Set<Listitem> listitem=codePublisherLbx.getSelectedItems();
								WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
								for(Listitem codePublisher:listitem){
									ermCodePublisher=codePublisher.getValue();
									((CodePublisherService)SpringUtil.getBean("codePublisherService")).deleteCodePublisher(ermCodePublisher.getPublisherId());
									((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "org_"+webEmployee.getEmployeesn());
								}
								
								ZkUtils.refurbishMethod("codePublisher/codePublisher.zul");
							}
						}
					});
				}else {
					// "請先選擇一筆資料"
					ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
							Labels.getLabel("info"));
					return;
				}
			} catch (Exception e) {
				log.error("刪除ermCodePublisher異常"+e);
			}
		}
		
		@Listen("onClick=#showAllBtn")
		public void showAll(){
			try{
				//清空查詢條件
				publisherIdBox.setValue("");
				publisherNameBox.setValue("");
				//取得聯繫方式的映射數據表對象
				ermCodeGeneralCodeList=((CodePublisherService) SpringUtil.getBean("codePublisherService")).findErmCodeGeneralCodeByItemId("CONTACT");
				//顯示全部資料
				List<ErmCodePublisher> result=((CodePublisherService)SpringUtil.getBean("codePublisherService")).search("","",webEmployee);
				List<ErmCodePublisher> tempErmCodePublisherList = result;
				for (ErmCodePublisher ermColdPublisher : tempErmCodePublisherList) {
					//默認全部為請選擇
					ermColdPublisher.setContactName("");
					for(int i=0;i<ermCodeGeneralCodeList.size();i++){
						ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
						String contact = ermColdPublisher.getContact();
						if(contact != null && !"".equals(contact)){
							if(contact.equals(tempEcgc.getGeneralcodeId())){
								ermColdPublisher.setContactName(tempEcgc.getName1());
							}
						}
					}
				}
				ListModelList<ErmCodePublisher> tmpLML = new ListModelList<ErmCodePublisher>(
					result);
				tmpLML.setMultiple(true);
				codePublisherLbx.setModel(tmpLML);
			} catch (Exception e) {
				log.equals("查詢所有ermCodePublisher異常"+e);
			}
		}
			
}
