package com.claridy.admin.composer;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSystemCkrscon;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmSystemCkrsconService;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebSysLogService;


public class ErmSystemCkrsconComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125573746581791329L;
	
	@Wire
	private Combobox hourCBbx;
	@Wire
	private Combobox minCBbx;
	@Wire
	private Textbox groupIdTBox;
	@Wire
	private Textbox groupNameTBox;
	
	private WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			ErmSystemCkrscon ckrsconList=((ErmSystemCkrsconService)SpringUtil.getBean("ermSystemCkrsconService")).getErmSystemCkrscon(webEmployee);
			if(ckrsconList.getGourpId()!=null&&!ckrsconList.getGourpId().equals("")){
				groupIdTBox.setValue(ckrsconList.getGourpId());
			}
			String[] gourpIds=ckrsconList.getGourpId().split(",");
			String name="";
			if(gourpIds.length>0){
				for(int i=0;i<gourpIds.length;i++){
					List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findbyNameIdOrg(gourpIds[i], null, null);
					if(webEmployeeList.size()>0){
						if(name.equals("")&&webEmployeeList.size()>0){
							name=webEmployeeList.get(0).getEmployeeName();
						}else{
							name=name+","+webEmployeeList.get(0).getEmployeeName();
						}
					}
				}
			}
			if(name!=null&&!name.equals("")){
				groupNameTBox.setValue(name);
			}
			if(ckrsconList!=null&&ckrsconList.getCheckItem()!=null&&!ckrsconList.getCheckItem().equals("")){
				
				String time=ckrsconList.getCheckItem();
				String[] times=time.split(":");
				
				for(int i=0;i<24;i++){
					if(i<10){
						if(Integer.parseInt(times[0])==i){
							hourCBbx.setSelectedIndex(i);
							break;
						}
					}else{
						int hour=Integer.parseInt(times[0]);
						hourCBbx.setSelectedIndex(hour);
						break;
						
					}
				}
				if(times[1].equals("00")){
					minCBbx.setSelectedIndex(0);
				}
				if(times[1].equals("30")){
					minCBbx.setSelectedIndex(1);
				}
			}else{
				hourCBbx.setSelectedIndex(0);
				minCBbx.setSelectedIndex(0);
			}
			
			
		} catch (Exception e) {
			log.error(""+e);
		}
		
		
		
	}
	
	
	@Listen("onClick=#updateBtn")
	public void update(){
		try {
			String groupName=groupNameTBox.getValue();
			if (StringUtils.isBlank(groupName)) {
				// 功能不能為空
				ZkUtils.showExclamation(
						Labels.getLabel("ermSystemCkrscon.addresseeGroup") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				groupNameTBox.focus();
				return;
			}
			ErmSystemCkrscon ckrsconList=((ErmSystemCkrsconService)SpringUtil.getBean("ermSystemCkrsconService")).getErmSystemCkrscon(webEmployee);
			String time=hourCBbx.getSelectedItem().getValue().toString()+":"+minCBbx.getSelectedItem().getValue().toString();
			if(ckrsconList!=null&&!ckrsconList.equals("")){
				ckrsconList.setCheckItem(time);
				if(groupIdTBox.getValue()!=null&&!groupIdTBox.getValue().equals("")){
					ckrsconList.setGourpId(groupIdTBox.getValue().toString());
				}else{
					ckrsconList.setGourpId("");
				}
				ckrsconList.setLatelyChangedDate(new Date());
				ckrsconList.setWebEmployee(webEmployee);
				((ErmSystemCkrsconService)SpringUtil.getBean("ermSystemCkrsconService")).update(ckrsconList);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "ckrscon_"+ckrsconList.getUuid());
			}
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("ermSystemCkrscon/ermSystemCkrscon.zul");
		} catch (Exception e) {
			log.error(""+e);
		}
		
	}
	
	
	
	
	

	
	
}
