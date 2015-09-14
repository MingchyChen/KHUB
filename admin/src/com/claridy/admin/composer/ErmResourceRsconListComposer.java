package com.claridy.admin.composer;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.facade.ErmResourcesRsconService;

public class ErmResourceRsconListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9083887205024283981L;
	
	@Wire
	private Datebox startDBox;
	@Wire
	private Datebox endDBox;
	@Wire
	private Combobox statusCBox;
	@Wire
	private Listbox ResourceRsconLix;

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			List<ErmResourcesRscon> ermResourcesRsconList=((ErmResourcesRsconService)SpringUtil.getBean("ermResourcesRsconService")).findAll();
			if(ermResourcesRsconList.size()>0){
				for(ErmResourcesRscon ermResourcesRscon:ermResourcesRsconList){
					if(ermResourcesRscon.getResourcesId()!=null&&!"".equals(ermResourcesRscon.getResourcesId())){
						ermResourcesRscon.setDbId(ermResourcesRscon.getResourcesId().substring(0,2));
					}
				}
			}
			ListModelList<ErmResourcesRscon> model=new ListModelList<ErmResourcesRscon>(ermResourcesRsconList);
			model.setMultiple(true);
			statusCBox.setSelectedIndex(0);
			ResourceRsconLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			startDBox.setValue(null);
			endDBox.setValue(null);
			statusCBox.setSelectedIndex(0);
			List<ErmResourcesRscon> ermResourcesRsconList=((ErmResourcesRsconService)SpringUtil.getBean("ermResourcesRsconService")).findAll();
			ListModelList<ErmResourcesRscon> model=new ListModelList<ErmResourcesRscon>(ermResourcesRsconList);
			model.setMultiple(true);
			ResourceRsconLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			if(startDBox.getValue()!=null&&!startDBox.getValue().equals("")||
					(endDBox.getValue()!=null&&!"".equals(endDBox.getValue()))||
					(!statusCBox.getSelectedItem().getValue().equals("0"))){
				Date sDate=startDBox.getValue();
				Date eDate=endDBox.getValue();
				String status=statusCBox.getSelectedItem().getValue();
				List<ErmResourcesRscon> ermResourcesRsconList=((ErmResourcesRsconService)SpringUtil.getBean("ermResourcesRsconService")).findById(sDate,eDate, status);
				if(ermResourcesRsconList.size()>0){
					for(ErmResourcesRscon ermResourcesRscon:ermResourcesRsconList){
						if(ermResourcesRscon.getResourcesId()!=null&&!"".equals(ermResourcesRscon.getResourcesId())){
							ermResourcesRscon.setDbId(ermResourcesRscon.getResourcesId().substring(0,2));
						}
					}
				}
				
				ListModelList<ErmResourcesRscon> model=new ListModelList<ErmResourcesRscon>(ermResourcesRsconList);
				model.setMultiple(true);
				ResourceRsconLix.setModel(model);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
				return;
			}
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	
	
}
