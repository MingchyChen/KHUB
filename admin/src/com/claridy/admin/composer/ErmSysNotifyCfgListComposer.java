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
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmSysNotiyConfigService;
import com.claridy.facade.WebSysLogService;


/**
 * zjgao nj
 * 資源到期通知設定
 * 2014/07/28
 */
public class ErmSysNotifyCfgListComposer extends SelectorComposer<Component>{

	
	private static final long serialVersionUID = -9049036898098362930L;

	
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected List<ErmCodeGeneralCode>  ermCodeGeneralCodeList;
	@Wire
	private Combobox resourceBox;
	@Wire
	protected List<ErmSysNotifyConfig> ermSysNotifyConfigList;
	@Wire
	protected Listbox ermSysNotifyConfiglbx;
	@Wire
	protected ErmSysNotifyConfig ermSysNotifyConfig;
	@Wire
	protected Window ermSysNotifyConfigAddWin;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		try {
			super.doAfterCompose(comp);
			ermCodeGeneralCodeList=((ErmSysNotiyConfigService) SpringUtil.getBean("ermSysNotiyConfigService")).findErmCodeGeneralCodeByItemId("RETYPE");
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("ermSysNotifyConfig.noLimit"));
			com.setValue("0");
			resourceBox.appendChild(com);
			for(int i=0;i<ermCodeGeneralCodeList.size();i++){
				ErmCodeGeneralCode ermCodeGeneralCode=new ErmCodeGeneralCode();
				ermCodeGeneralCode=ermCodeGeneralCodeList.get(i);
				com=new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				resourceBox.appendChild(com);
			}
			resourceBox.setSelectedIndex(0);
			
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			
			ermSysNotifyConfigList=((ErmSysNotiyConfigService)SpringUtil.getBean("ermSysNotiyConfigService")).findALL(webEmployee);
			List<ErmSysNotifyConfig> tempErmCfgList=ermSysNotifyConfigList;
			for (ErmSysNotifyConfig ermcfg : tempErmCfgList) {
				for(int i=0;i<ermCodeGeneralCodeList.size();i++){
					ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
					if(ermcfg.getTypeId().equals(tempEcgc.getGeneralcodeId())){
						ermcfg.setTypeName(tempEcgc.getName1());
					}
				}
			}
			ListModelList<ErmSysNotifyConfig> tmpLML = new ListModelList<ErmSysNotifyConfig>(
					tempErmCfgList);
			tmpLML.setMultiple(true);
			ermSysNotifyConfiglbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("ErmSysNotifyCfg列表初始化異常"+e);
		}
	}
	
	@Listen("onClick = #showAll")
	public void searchAll(){
		try {
			ermSysNotifyConfigList = ((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.findALL(webEmployee);
			List<ErmSysNotifyConfig> tempErmCfgList=ermSysNotifyConfigList;
			for (ErmSysNotifyConfig ermcfg : tempErmCfgList) {
				for(int i=0;i<ermCodeGeneralCodeList.size();i++){
					ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
					if(ermcfg.getTypeId().equals(tempEcgc.getGeneralcodeId())){
						ermcfg.setTypeName(tempEcgc.getName1());
					}
				}
			}
			ListModelList<ErmSysNotifyConfig> tmpLML = new ListModelList<ErmSysNotifyConfig>(
					tempErmCfgList);
			tmpLML.setMultiple(true);
			ermSysNotifyConfiglbx.setModel(tmpLML);
			resourceBox.setSelectedIndex(0);
		} catch (Exception e) {
			log.error("ErmSysNotifyCfg全部查詢異常"+e);
		}
	}

	@Listen("onClick = #pagSearchBtn")
	public void findErmSysNotifyByTypeId(){
		try {
			String typeId=resourceBox.getSelectedItem().getValue().toString();
			if ("0".equals(typeId)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				return;
			}
			if(!typeId.equals("0")){
			ermSysNotifyConfigList = ((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.findErmSysNofityCfgByTypeId( resourceBox.getSelectedItem().getValue().toString(), webEmployee);
			}
			else{
				ermSysNotifyConfigList = ((ErmSysNotiyConfigService) SpringUtil
						.getBean("ermSysNotiyConfigService"))
						.findALL(webEmployee);
			}
			
			List<ErmSysNotifyConfig> tempErmCfgList=ermSysNotifyConfigList;
			for (ErmSysNotifyConfig ermcfg : tempErmCfgList) {
				for(int i=0;i<ermCodeGeneralCodeList.size();i++){
					ErmCodeGeneralCode tempEcgc=ermCodeGeneralCodeList.get(i);
					if(ermcfg.getTypeId().equals(tempEcgc.getGeneralcodeId())){
						ermcfg.setTypeName(tempEcgc.getName1());
					}
				}
			}
			ListModelList<ErmSysNotifyConfig> tmpLML = new ListModelList<ErmSysNotifyConfig>(
					tempErmCfgList);
			tmpLML.setMultiple(true);
			ermSysNotifyConfiglbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("ErmSysNotifyCfg條件查詢異常"+e);
		}
	}
	@Listen("onClick = #deleteBtn")
	public void deleteErmSysNotifyCfg(){
		try {
			int updateChecked=ermSysNotifyConfiglbx.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=ermSysNotifyConfiglbx.getSelectedItems();
							for(Listitem employee:listitem){
								ermSysNotifyConfig=employee.getValue();
								((ErmSysNotiyConfigService)SpringUtil.getBean("ermSysNotiyConfigService")).deleteErmSysNotifyCfg(ermSysNotifyConfig);
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),ermSysNotifyConfig.getTypeId(),"ermSysNotifyConfig_"+ermSysNotifyConfig.getTypeId());
							}
							String url="ermSysNotifyConfig/ermSysNotifyConfig.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("ErmSysNotifyCfg刪除異常"+e);
		}
	}
	@Listen("onClick = #addBtn")
	public void addErmSysNotifyCfg(){
		ermSysNotifyConfigAddWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/ermSysNotifyConfig/ermSysNotifyConfigAdd.zul", null, null);
		ermSysNotifyConfigAddWin.doModal();
	}
}
