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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesCkrs;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesCkrsService;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.WebSysLogService;

public class ErmResourcesCKRSAddComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8304123272324960408L;
	
	@Wire
	private Combobox typeCBox;
	@Wire
	private Textbox reourseIdTBox;
	@Wire
	private Textbox titleTBox;
	@Wire
	private Window addermResourcesCkRSWin;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private ErmResourcesMainfileV ermResourcesMainFileV=null;

	private WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			List<ErmCodeGeneralCode> ermCodeLAllist=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(loginwebEmployee);
			Map<String,String> arg=ZkUtils.getExecutionArgs();
			String resourcesId=arg.get("resourcesId");
			String dbId=arg.get("dbId");
			int i=0;
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue(0);
			typeCBox.appendChild(com);
			for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodeLAllist){
				com=new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				typeCBox.appendChild(com);
				if(dbId!=null&&dbId.equals(ermCodeGeneralCode.getGeneralcodeId())){
					typeCBox.setSelectedIndex(i);
				}
				i++;
			}
			typeCBox.setSelectedIndex(0);
			
			ermResourcesMainFileV=((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).findByResourceId(resourcesId);
			if(ermResourcesMainFileV!=null&&ermResourcesMainFileV.getResourcesId()!=null&&!ermResourcesMainFileV.getResourcesId().equals("")){
				reourseIdTBox.setValue(ermResourcesMainFileV.getResourcesId());
				titleTBox.setValue(ermResourcesMainFileV.getTitle());
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	
	@Listen("onClick=#searchBtn")
	public void search(){
		if(typeCBox.getSelectedItem().getValue()!=null){
			if(!typeCBox.getSelectedItem().getValue().toString().equals("0")&&!typeCBox.getSelectedItem().getValue().equals("")){
				String typeId=typeCBox.getSelectedItem().getValue();
				Map<String,String> arg=new HashMap<String, String>();
				arg.put("typeId",typeId);
				Window ErmResourcesCkRSSearchWin=(Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermResourcesCkRS/ermResourcesCkrsSearch.zul",addermResourcesCkRSWin, arg);
				ErmResourcesCkRSSearchWin.doModal();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermResourceCKRS.selectSearch"),Labels.getLabel("info"));
			}
		}else{
			ZkUtils.showExclamation(Labels.getLabel("ermResourceCKRS.selectSearch"), Labels.getLabel("info"));
		}
	}
	
	
	@Listen("onClick=#saveBtn")
	public void insert(){
		try {
			if(reourseIdTBox.getValue()!=null&&!reourseIdTBox.getValue().equals("")){
				((ErmResourcesCkrsService)SpringUtil.getBean("ermResourcesCkrsService")).insert(ermResourcesMainFileV,loginwebEmployee);
				ZkUtils.refurbishMethod("ermResourcesCkRS/ermResourcesCkRS.zul");
				addermResourcesCkRSWin.detach();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermResourceCKRS.insert"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	

}
