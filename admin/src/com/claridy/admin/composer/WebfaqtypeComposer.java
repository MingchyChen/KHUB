package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaqType;
import com.claridy.facade.WebFaqTypeService;
import com.claridy.facade.WebSysLogService;

public class WebfaqtypeComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2972445418279039472L;
	
	@Wire
	private Textbox keywordtbox;
	@Wire
	private Listbox webfaqtypeLix;
	
	
	private WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			List<WebFaqType> webFaqTypeList = ((WebFaqTypeService)SpringUtil.getBean("webFaqTypeService")).findAll(loginwebEmployee);
			ListModelList<WebFaqType> model = new ListModelList<WebFaqType>(webFaqTypeList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
		} catch (Exception e) {
			log.error("faqtype加載報錯",e);
		}
	}
	
	@Listen("onClick=#searchbtn")
	public void search(){
		try {
			String keyWord = XSSStringEncoder.encodeXSSString(keywordtbox.getValue().trim());
			if(keyWord!=null&&!"".equals(keyWord)){
				List<WebFaqType> webFaqTypeList = ((WebFaqTypeService)SpringUtil.getBean("webFaqTypeService")).findBy(keyWord,loginwebEmployee);
				ListModelList<WebFaqType> model = new ListModelList<WebFaqType>(webFaqTypeList);
				model.setMultiple(true);
				webfaqtypeLix.setModel(model);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
			}
		} catch (WrongValueException e) {
			log.error("faqtype查詢報錯",e);
		}
		
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			keywordtbox.setValue("");
			List<WebFaqType> webFaqTypeList = ((WebFaqTypeService)SpringUtil.getBean("webFaqTypeService")).findAll(loginwebEmployee);
			ListModelList<WebFaqType> model = new ListModelList<WebFaqType>(webFaqTypeList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
		} catch (Exception e) {
			log.error("faqtype顯示全部報錯",e);
		}
	}
	
	@Listen("onClick=#addbtn")
	public void addWebFaqType(){
		try {
			Window editWin = (Window) Executions.createComponents("/WEB-INF/pages/system/webfaqtype/webfaqtypeAE.zul",null,null);
			editWin.doModal();
		} catch (Exception e) {
			log.error("faqtype跳轉報錯",e);
		}
	}
	
	@Listen("onClick=#deletebtn")
	public void deleteWebFaqType(){
		try {
			int updateChecked=webfaqtypeLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listItem = webfaqtypeLix.getSelectedItems();
							WebFaqType webFaqType;
							for(Listitem webFaqTypeTmp:listItem){
								webFaqType = webFaqTypeTmp.getValue();
								((WebFaqTypeService)SpringUtil.getBean("webFaqTypeService")).delete(webFaqType);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "webfaqtype_"+webFaqType.getUuid());
							}
							List<WebFaqType> webFaqTypeList = ((WebFaqTypeService)SpringUtil.getBean("webFaqTypeService")).findAll(loginwebEmployee);
							ListModelList<WebFaqType> model = new ListModelList<WebFaqType>(webFaqTypeList);
							model.setMultiple(true);
							webfaqtypeLix.setModel(model);
						}
					}
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("deleteData"),Labels.getLabel("info"));
			}
		}catch(Exception e){
			log.error("刪除faqtype報錯",e);
		}			
	}
}
