package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebOrgListService;
import com.sun.java.swing.plaf.windows.resources.windows;

public class ErmSystemCkrsconGourpIdComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5953449352492792472L;
	
	@Wire
	private Textbox IdBox;
	@Wire
	private Textbox NameBox;
	@Wire
	private Combobox orgNameBox;
	@Wire
	private Listbox GroupIdLix;
	
	@Wire
	private Window ermSystemCkrsconGourpIdWin;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList();
			Comboitem comitem=new Comboitem();
			comitem.setLabel(Labels.getLabel("select"));
			comitem.setValue("0");
			orgNameBox.appendChild(comitem);
			for(int i=0;i<webOrgList.size();i++){
				comitem=new Comboitem();
				comitem.setLabel(webOrgList.get(i).getOrgName());
				comitem.setValue(webOrgList.get(i).getOrgId());
				orgNameBox.appendChild(comitem);
			}
			orgNameBox.setSelectedIndex(0);
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findById();
			ListModelList<WebEmployee> model=new ListModelList<WebEmployee>(webEmployeeList);
			model.setMultiple(true);
			GroupIdLix.setModel(model);
			
		} catch (Exception e) {
			log.error(""+e);
		}
		
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			IdBox.setValue("");
			NameBox.setValue("");
			orgNameBox.setSelectedIndex(0);
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findById();
			ListModelList<WebEmployee> model=new ListModelList<WebEmployee>(webEmployeeList);
			model.setMultiple(true);
			GroupIdLix.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			if(IdBox.getValue()!=null&&!IdBox.getValue().equals("")||
					(NameBox.getValue()!=null&&!NameBox.getValue().equals(""))||
					(orgNameBox.getSelectedItem()!=null&&!orgNameBox.getSelectedItem().getValue().equals("0"))){
				String id=IdBox.getValue().trim();
				id=XSSStringEncoder.encodeXSSString(id);
				String name=NameBox.getValue().trim();
				name=XSSStringEncoder.encodeXSSString(name);
				List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findbyNameIdOrg(id, name, orgNameBox.getSelectedItem().getValue().toString());
				ListModelList<WebEmployee> model=new ListModelList<WebEmployee>(webEmployeeList);
				model.setMultiple(true);
				GroupIdLix.setModel(model);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
				return;
			}
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#updateBtn")
	public void update(){
		int count=GroupIdLix.getSelectedCount();
		if(count>0){
			Set<Listitem> listItem=GroupIdLix.getSelectedItems();
			String id="";
			Textbox groupIdTBox=(Textbox) ermSystemCkrsconGourpIdWin.getParent().getFellowIfAny("groupIdTBox");
			String name="";
			Textbox groupNameTBox=(Textbox) ermSystemCkrsconGourpIdWin.getParent().getFellowIfAny("groupNameTBox");
			for(Listitem item:listItem){
				WebEmployee webEmployee=item.getValue();
				if(name.equals("")){
					name=webEmployee.getEmployeeName();
				}else{
					name=name+","+webEmployee.getEmployeeName();
				}
				if(id.equals("")){
					id=webEmployee.getEmployeesn();
				}else{
					id=id+","+webEmployee.getEmployeesn();
				}
			}
			groupIdTBox.setValue(id);
			groupNameTBox.setValue(name);
			if(ermSystemCkrsconGourpIdWin!=null){
				ermSystemCkrsconGourpIdWin.detach();
			}
		}
	}
	
	

}
