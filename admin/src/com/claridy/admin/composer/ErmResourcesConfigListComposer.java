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
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.WebSysLogService;

public class ErmResourcesConfigListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8362550725280704844L;
	@Wire
	private Listbox ermResourcesCofigLix;
	@Wire
	private Combobox nameCBox;
	@Wire
	private Window ermResourcesConfigAddWin;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			List<ErmCodeGeneralCode> ermCodeList=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findAll(webEmployee);
			List<ErmCodeGeneralCode> ermCodeLAllist=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(webEmployee);
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue(0);
			nameCBox.appendChild(com);
			for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodeLAllist){
				com=new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				nameCBox.appendChild(com);
			}
			nameCBox.setSelectedIndex(0);
			ListModelList<ErmCodeGeneralCode> model=new ListModelList<ErmCodeGeneralCode>(ermCodeList);
			model.setMultiple(true);
			ermResourcesCofigLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
		
	}
	
	@Listen("onClick=#showAll")
	public void findAll(){
		try {
			List<ErmCodeGeneralCode> ermCodeList=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findAll(webEmployee);
			ListModelList<ErmCodeGeneralCode> model=new ListModelList<ErmCodeGeneralCode>(ermCodeList);
			model.setMultiple(true);
			ermResourcesCofigLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#pagSearchBtn")
	public void findById(){
		try {
			if(!nameCBox.getSelectedItem().getValue().equals(0)){
				List<ErmCodeGeneralCode> ermErmCodeGeneralCodeList=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findById(webEmployee,nameCBox.getSelectedItem().getValue().toString());
				ListModelList<ErmCodeGeneralCode> model=new ListModelList<ErmCodeGeneralCode>(ermErmCodeGeneralCodeList);
				model.setMultiple(true);
				ermResourcesCofigLix.setModel(model);
				
			}else{
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"),Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#addBtn")
	public void add(){
		ermResourcesConfigAddWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/ermResourcesConfig/ermResourcesConfigAdd.zul", null, null);
		ermResourcesConfigAddWin.doModal();
	}
	
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deleteBtn")
	public void delete(){
		try {
			int count=ermResourcesCofigLix.getSelectedCount();
			if(count>0){
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listItems=ermResourcesCofigLix.getSelectedItems();
							for(Listitem listitem:listItems){
								ErmResourcesConfig ermResourcesConfig=listitem.getValue();
								((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).delete(ermResourcesConfig);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "account_"+ermResourcesConfig.getTypeId());
							}
						}
					}
				});
			}else{
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	
	
	
	

	
}
