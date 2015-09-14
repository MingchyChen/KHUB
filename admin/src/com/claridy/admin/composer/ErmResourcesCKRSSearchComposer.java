package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.facade.ErmResourcesCkrsService;

public class ErmResourcesCKRSSearchComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7574303534274585490L;
	
	@Wire
	private Textbox idTBox;
	@Wire
	private Textbox titleTBox;
	@Wire
	private Listbox ckRsLix;
	@Wire
	private Window ErmResourcesCkRSSearchWin;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String typeId="";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		try {
			Map<String,String> arg=ZkUtils.getExecutionArgs();
			typeId=arg.get("typeId");
			List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByTypeId(typeId);
			
			ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
			ckRsLix.setModel(model);
		} catch (Exception e) {	
			log.error(""+e);
		}
	}
	
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			List<ErmResourcesMainfileV> ermResourcesMainfileV=null;
			
			ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByTypeId(typeId);
			
			ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
			ckRsLix.setModel(model);
		} catch (Exception e) {	
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#searhcBtn")
	public void search(){
		try {
			
			String id=idTBox.getValue().trim();
			String title=titleTBox.getValue().trim();
			if(id!=null&&!id.equals("")||
					(title!=null&&!"".equals(title))){
				List<ErmResourcesMainfileV> ermResourcesMainfileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByIdName(typeId,id,title);
				ListModelList<ErmResourcesMainfileV> model=new ListModelList<ErmResourcesMainfileV>(ermResourcesMainfileV);
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
				Listitem listitem=ckRsLix.getSelectedItem();
				ErmResourcesMainfileV ermResourceFileV=listitem.getValue();
				Map<String,String> args=new HashMap<String, String>();
				args.put("resourcesId",ermResourceFileV.getResourcesId());
				args.put("dbId",ermResourceFileV.getTypeId());
				if(ErmResourcesCkRSSearchWin!=null){
					if(ErmResourcesCkRSSearchWin.getParent()!=null){
						ErmResourcesCkRSSearchWin.getParent().detach();
					}
				}
				Window addermResourcesCkRSWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResourcesCkRS/ermResourcesCkrsAdd.zul",null, args);
				addermResourcesCkRSWin.doModal();
				if(ErmResourcesCkRSSearchWin!=null){
					ErmResourcesCkRSSearchWin.detach();
				}
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermResourceCKRS.insert"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}

	
	
}
