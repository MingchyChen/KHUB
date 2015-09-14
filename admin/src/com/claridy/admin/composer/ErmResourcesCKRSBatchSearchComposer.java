package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.hql.ast.tree.SelectClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesCkrsService;
import com.claridy.facade.ErmResourcesConfigService;

public class ErmResourcesCKRSBatchSearchComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3149579080629336421L;

	@Wire
	private Combobox typeCBox;
	@Wire
	private Textbox titleTBox;
	@Wire
	private Listbox ckRsLix;
	@Wire
	private Window ErmResourcesCkRSBatchSearchWin;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
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
			List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findMainfileVAll();
			
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
			List<ErmResourcesMainfileV> ermResourcesMainfileV=null;
			
			ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findMainfileVAll();
			
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
			
			String typeId=typeCBox.getSelectedItem().getValue();
			String title=titleTBox.getValue().trim();
			if(typeId!=null&&!typeId.equals("0")||
					(title!=null&&!"".equals(title))){
				List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByIdName(typeId,null,title);
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
	
	@Listen("onClick=#saveBtn")
	public void insert(){
		try {
			int count=ckRsLix.getSelectedCount();
			if(count>0){
				Set<Listitem> listitems=ckRsLix.getSelectedItems();
				for(Listitem listitem:listitems){
					ErmResourcesMainfileV ermResourcesMainfileV=listitem.getValue();
					((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).insert(ermResourcesMainfileV,loginwebEmployee);
				}
				ZkUtils.refurbishMethod("ermResourcesCkRS/ermResourcesCkRS.zul");
				ErmResourcesCkRSBatchSearchWin.detach();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermResourceCKRS.insert"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	

}
