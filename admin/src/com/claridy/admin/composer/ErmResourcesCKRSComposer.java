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
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesCkrsService;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.WebSysLogService;

public class ErmResourcesCKRSComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6881672678405968152L;
	
	@Wire
	private Combobox typeCBox;
	@Wire
	private Textbox nameTBox;
	@Wire
	private Listbox ckRsLix;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			List<ErmCodeGeneralCode> ermCodeLAllist=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(loginwebEmployee);
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue(0);
			typeCBox.appendChild(com);
			for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodeLAllist){
				com=new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				typeCBox.appendChild(com);
			}
			typeCBox.setSelectedIndex(0);
			List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findAll();
			ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
			model.setMultiple(true);
			ckRsLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findAll();
			ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
			model.setMultiple(true);
			ckRsLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#searhcBtn")
	public void search(){
		try {
			
			String typeId=typeCBox.getSelectedItem().getValue().toString();
			String name=nameTBox.getValue().trim();
			if(typeId!=null&&!typeId.equals("")||
					(name!=null&&!name.equals("")&&!"0".equals(name))){
				List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByTypeName(typeId,name);
				ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
				model.setMultiple(true);
				ckRsLix.setModel(model);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error(""+e); 
		}
	}
	
	@Listen("onClick=#deleteBtn")
	public void delete(){
		try {
			int count=ckRsLix.getSelectedCount();
			if(count>0){
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					
					public void onEvent(Event event) throws Exception {
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitems=ckRsLix.getSelectedItems();
							for(Listitem listitem:listitems){
								ErmResourcesMainfileV ermResourcesMainfileV=listitem.getValue();
								((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).delete(ermResourcesMainfileV.getResourcesId(),loginwebEmployee);
								ZkUtils.refurbishMethod("ermResourcesCkRS/ermResourcesCkRS.zul");
							}
						}
						
					}
					
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#addBtn")
	public void add(){
		Window ermResourcesCkrsAddWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResourcesCkRS/ermResourcesCkrsAdd.zul",null,null);
		ermResourcesCkrsAddWin.doModal();
	}
	
	@Listen("onClick=#batchAddBtn")
	public void batchAdd(){
		Window ErmResourcesCkRSBatchSearchWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResourcesCkRS/ermResourcesCkrsBatchSearch.zul",null,null);
		ErmResourcesCkRSBatchSearchWin.doModal();
	}
	
	

	
	
}
