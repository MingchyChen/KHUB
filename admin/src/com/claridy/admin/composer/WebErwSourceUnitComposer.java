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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebErwSourceUnit;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebErwSourceService;
import com.claridy.facade.WebErwSourceUnitService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * 魏建國 nj
 * 管際合作管理新增出版商
 */
public class WebErwSourceUnitComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Wire
	private Label message;
	
	@Wire
	private Label lblNum;
	
	@Wire
	private Label lblwebEmployeesn;
	
	@Wire
	private Combobox webOrgOrg;
	
	@Wire
	private Radiogroup isCooperation;
	
	@Wire
	private Combobox webEmployeesn;
	
	@Wire
	private Intbox roundNum;
	
	@Wire Window addWebErwSourceWinUnit;
	
	private WebErwSourceUnit webErwSourceUnit;
	
	private String webOrgOrgF;
	private String isCooperationF;
	private String webEmployeesnF;
	private Integer roundNumF;
	private String webErwSourceId;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			webErwSourceId=map.get("webErwSourceId");
			String webErwSourceUnitId=map.get("webErwSourceUnitId");
			
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList();
			ListModel<WebOrg> webOrgListModel = new ListModelList<WebOrg>(webOrgList);
			webOrgOrg.setModel(webOrgListModel);
			
			webErwSourceUnit = ((WebErwSourceUnitService)SpringUtil.getBean("webErwSourceUnitService")).getWebErwSourceUnitByUUID(webErwSourceUnitId);
			
			if(webErwSourceUnit == null){
				webErwSourceUnit = new WebErwSourceUnit();
				addWebErwSourceWinUnit.setTitle(Labels.getLabel("add"));
			}else{
				webOrgOrg.setValue(webErwSourceUnit.getWebOrgOrg().getOrgName());
				isCooperation.setSelectedIndex(webErwSourceUnit.getIsCooperation());
				if(webErwSourceUnit.getWebEmployeesn() != null)
					webEmployeesn.setValue(webErwSourceUnit.getWebEmployeesn().getEmployeeName());
				roundNum.setValue(webErwSourceUnit.getRoundNum());
				addWebErwSourceWinUnit.setTitle(Labels.getLabel("edit"));
			}
			
			changeStyle();
		} catch (Exception e) {
			log.error("初始化異常",e);
		}
	}
	
	@Listen("onChange=#webOrgOrg")
	public void changeEmployee(){
		webEmployeesn.setText("");
		Comboitem selectItem = webOrgOrg.getSelectedItem();
		if (selectItem != null) {
			WebOrg webOrg = (WebOrg) selectItem.getValue();
			if (webOrg != null) {
				if (webOrg.getOrgId() != null) {
					List<WebEmployee> webEmployeeList = ((WebEmployeeListService) SpringUtil
							.getBean("webEmployeeListService"))
							.findWebEmployeeListByParentOrgId(webOrg.getOrgId());
					ListModel<WebEmployee> webEmployeeListModel = new ListModelList<WebEmployee>(
							webEmployeeList);
					webEmployeesn.setModel(webEmployeeListModel);
				}
			}
		}
	}
	
	@Listen("onClick=#isCooperation")
	public void changeStyle(){
		lblNum.setStyle("color: red; ");
		lblwebEmployeesn.setStyle("color: red; ");
		
	}
	
	@Listen("onClick=#saveBtn")
	public void saveBtn(){
		if(addWebErwSourceWinUnit == null){
			return;
		}
		try {
			webOrgOrgF = XSSStringEncoder.encodeXSSString(webOrgOrg.getValue().trim());
			isCooperationF = isCooperation.getSelectedItem().getValue();
			webEmployeesnF = XSSStringEncoder.encodeXSSString(webEmployeesn.getValue().trim());
			roundNumF =roundNum.getValue();
			if("".equals(webOrgOrgF)){
				ZkUtils.showExclamation(Labels.getLabel("webErwSourceUnit.webOrgOrgcheck"),Labels.getLabel("info"));
//				message.setValue(Labels.getLabel("info")+": "+Labels.getLabel("webErwSourceUnit.webOrgOrgcheck"));
				webOrgOrg.focus();
			}else if("".equals(isCooperationF)){
				ZkUtils.showExclamation(Labels.getLabel("webErwSourceUnit.isCooperationcheck"),Labels.getLabel("info"));
//				message.setValue(Labels.getLabel("info")+": "+Labels.getLabel("webErwSourceUnit.isCooperationcheck"));
				isCooperation.focus();
			}else if("".equals(webEmployeesnF) && !"0".equals(isCooperationF)){
				ZkUtils.showExclamation(Labels.getLabel("webErwSourceUnit.webEmployeesncheck"),Labels.getLabel("info"));
//				message.setValue(Labels.getLabel("info")+": "+Labels.getLabel("webErwSourceUnit.webEmployeesncheck"));
				webEmployeesn.focus();
			}else if((roundNumF == null || roundNumF==0) && !"0".equals(isCooperationF)){
				ZkUtils.showExclamation(Labels.getLabel("webErwSourceUnit.roundNumcheck"),Labels.getLabel("info"));
//				message.setValue(Labels.getLabel("info")+": "+Labels.getLabel("webErwSourceUnit.roundNumcheck"));
				roundNum.focus();
			}else{
				message.setValue("");
				webErwSourceUnit.setWebOrgOrg((WebOrg)webOrgOrg.getSelectedItem().getValue());
				webErwSourceUnit.setIsCooperation(Integer.valueOf(isCooperationF));
				if(webEmployeesn.getSelectedItem() != null)
					webErwSourceUnit.setWebEmployeesn((WebEmployee) webEmployeesn.getSelectedItem().getValue());
				webErwSourceUnit.setRoundNum(roundNumF);

				WebErwSource webErwSource = ((WebErwSourceService)SpringUtil.getBean("webErwSourceService")).getWebErwSourceByUUID(webErwSourceId);
				webErwSourceUnit.setWebErwSource(webErwSource);
				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				if(webErwSourceUnit.getUuid() != null && !"".equals(webErwSourceUnit.getUuid())){
					webErwSourceUnit = ((WebErwSourceUnitService)SpringUtil.getBean("webErwSourceUnitService")).updateWebErwSourceUnit(webErwSourceUnit);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSourceUnit.getUuid());
				}else{
					webErwSourceUnit = ((WebErwSourceUnitService)SpringUtil.getBean("webErwSourceUnitService")).addWebErwSourceUnit(webErwSourceUnit);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "weberesource_"+webErwSourceUnit.getUuid());
				}
				ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
				Map<String, String> map=new HashMap<String, String>();
				map.put("webErwSourceId",  webErwSource.getUuid());
				if(addWebErwSourceWinUnit != null){
					if(addWebErwSourceWinUnit.getParent() != null)
						addWebErwSourceWinUnit.getParent().detach();
				}
				Window newAdd=(Window)com.claridy.common.util.ZkUtils.createComponents("/WEB-INF/pages/system/webErwSource/webErwSourceEdit.zul", null,
						map);
				newAdd.doModal();
				
				if(addWebErwSourceWinUnit != null){
					addWebErwSourceWinUnit.detach();
				}
			}
			
		} catch (Exception e) {
			log.error("保存異常",e);
			ZkUtils.showInformation(Labels.getLabel("saveFailed"),Labels.getLabel("info"));
		}
	}
	
	@Listen("onClick=#cancelBtn")
	public void cancelBtn(){
		addWebErwSourceWinUnit.detach();
	}
	
	public WebErwSourceUnit getWebErwSourceUnit(){
		return webErwSourceUnit;
	}

}
