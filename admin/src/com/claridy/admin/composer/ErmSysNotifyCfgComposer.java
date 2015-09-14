package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmSysNotiyConfigService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * zjgao nj
 * 資源到期通知設定
 * 2014/07/28
 */
public class ErmSysNotifyCfgComposer extends SelectorComposer<Component> {

	
	private static final long serialVersionUID = 2590142702659296678L;
	@Wire
	protected List<ErmCodeGeneralCode> ermCodeGeneralCodeList;// 資源類型集合
	@Wire
	private Combobox resourceBox;
	@Wire
	private List<WebOrg> webOrgList;// 收件群組集合
	@Wire
	private Combobox receiptGroupBox;
	@Wire
	private ErmSysNotifyConfig ermSysNotifyConfig;
	@Wire
	protected Textbox subjectTxt;
	@Wire
	protected CKeditor contentEdt;
	@Wire
	protected Textbox beforeDaystxt;
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected Window ermSysNotifyConfigAddWin;
	@Wire
	protected Window ermSysNotifyConfigEdtWin;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String typeId = map.get("typeId");
			String groupId = map.get("groupId");
			if (typeId != null || groupId != null) {
				ermSysNotifyConfig = ((ErmSysNotiyConfigService) SpringUtil
						.getBean("ermSysNotiyConfigService")).findedtAddList(
						typeId, groupId).get(0);
			}
			int tempResourceIndex = 0;
			int tempreceiptIndex = 0;
			ermCodeGeneralCodeList = ((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.findErmCodeGeneralCodeByItemId("RETYPE");
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("ermSysNotifyConfig.noLimit"));
			com.setValue("0");
			resourceBox.appendChild(com);
			for (int i = 0; i < ermCodeGeneralCodeList.size(); i++) {
				ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
				ermCodeGeneralCode = ermCodeGeneralCodeList.get(i);
				com = new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				resourceBox.appendChild(com);
				if (ermSysNotifyConfig != null
						&& ermSysNotifyConfig.getTypeId() != null) {
					if (ermCodeGeneralCode.getGeneralcodeId().equals(
							ermSysNotifyConfig.getTypeId())) {
						tempResourceIndex = i + 1;
					}
				}

			}
			resourceBox.setSelectedIndex(tempResourceIndex);

			webOrgList = ((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService")).findWebOrgListToCombox();
			Comboitem comOrg = new Comboitem();
			comOrg.setLabel(Labels.getLabel("select"));
			comOrg.setValue("0");
			receiptGroupBox.appendChild(comOrg);
			for (int i = 0; i < webOrgList.size(); i++) {
				WebOrg webOrg = new WebOrg();
				webOrg = webOrgList.get(i);
				comOrg = new Comboitem();
				comOrg.setLabel(webOrg.getOrgName());
				comOrg.setValue(webOrg.getOrgId());
				receiptGroupBox.appendChild(comOrg);
				if (ermSysNotifyConfig != null
						&& ermSysNotifyConfig.getTypeId() != null) {
					if (webOrg.getOrgId().equals(ermSysNotifyConfig.getWebOrg().getOrgId())) {
						tempreceiptIndex = i + 1;
					}
				}
			}
			receiptGroupBox.setSelectedIndex(tempreceiptIndex);

			if (ermSysNotifyConfig != null && !"".equals(ermSysNotifyConfig)) {
				subjectTxt.setValue(ermSysNotifyConfig.getSubject());
				contentEdt.setValue(ermSysNotifyConfig.getContent());
				beforeDaystxt.setValue(ermSysNotifyConfig.getBeforeDays());
			}
		} catch (Exception e) {
			log.error("ErmSysNotifyCfg初始化異常"+e);
		}

	}

	@Listen("onClick = #saveBtn")
	public void addErmSymNotifyCfg() {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ermSysNotifyConfig = new ErmSysNotifyConfig();
			// ermSysNotifyConfig.setUuid(UUIDGenerator.getUUID());
			String tempTypeId = "";
			String tempGroupId = "";
			String tempSubject = "";
			String tempContent = "";
			String tempBeforeDays = "";
			Date date = new Date();

			ermSysNotifyConfig.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				ermSysNotifyConfig.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				ermSysNotifyConfig.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			ermSysNotifyConfig.setCreateDate(date);
			List<ErmSysNotifyConfig> tempCfgList= ((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.findALL(webEmployee);
			if (!resourceBox.getSelectedItem().getValue().toString().equals("0")) {
				tempTypeId = resourceBox.getSelectedItem().getValue().toString();
				tempTypeId = XSSStringEncoder.encodeXSSString(tempTypeId);
				ermSysNotifyConfig.setTypeId(tempTypeId);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.resourceType") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				resourceBox.focus();
				return;
			}
			if (!receiptGroupBox.getSelectedItem().getValue().toString()
					.equals("0")) {
				tempGroupId = receiptGroupBox.getSelectedItem().getValue()
						.toString().trim();
				tempGroupId = XSSStringEncoder.encodeXSSString(tempGroupId);
				//ermSysNotifyConfig.setGroupId(tempGroupId);
				WebOrg tempWebOrg=new WebOrg();
				tempWebOrg.setOrgId(tempGroupId);
				ermSysNotifyConfig.setWebOrg(tempWebOrg);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.receiptGroup") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				receiptGroupBox.focus();
				return;
			}
			
			tempSubject = subjectTxt.getValue().toString().trim();
			if(tempSubject!=null&&!"".equals(tempSubject)){
				tempSubject = XSSStringEncoder.encodeXSSString(tempSubject);
				ermSysNotifyConfig.setSubject(tempSubject);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("ermCodeItem.keyNote") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				subjectTxt.focus();
				return;
			}
			
			tempContent = contentEdt.getValue().toString().trim();
			if(tempContent!=null&&!"".equals(tempContent)){
				ermSysNotifyConfig.setContent(tempContent);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("ermCodeItem.content") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				//receiptGroupBox.focus();
				return;
			}
			

			tempBeforeDays = beforeDaystxt.getValue().toString().trim();
			boolean tempBool = isNumber(tempBeforeDays);
			if (tempBool) {
				tempBeforeDays = XSSStringEncoder.encodeXSSString(tempBeforeDays);
				ermSysNotifyConfig.setBeforeDays(tempBeforeDays);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.beforeDays") + " "
								+ Labels.getLabel("ermSysNotifyConfig.isNumber"),
						Labels.getLabel("warn"));
				beforeDaystxt.focus();
				return;
			}

			ermSysNotifyConfig.setLatelyChangedUser(webEmployee.getEmployeesn());
			ermSysNotifyConfig.setLatelyChangedDate(new Date());
			for (ErmSysNotifyConfig ermSysNotifyConfig : tempCfgList) {
				if(ermSysNotifyConfig.getTypeId().equals(resourceBox.getSelectedItem().getValue().toString())){
					ZkUtils.showExclamation(
							Labels.getLabel("ermSysNotifyConfig.resourceType") + " "
									+ Labels.getLabel("ermSysNotifyConfig.there"),
							Labels.getLabel("warn"));
					resourceBox.setSelectedIndex(0);
					return;
				}
			}
			((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.addErmSysNotifyCfg(ermSysNotifyConfig);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(
					ZkUtils.getRemoteAddr(), webEmployee.getEmployeeName(),
					"ermSysNotifyConfig_" + tempTypeId + tempGroupId);

			// 新增
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			String url = "ermSysNotifyConfig/ermSysNotifyConfig.zul";
			ZkUtils.refurbishMethod(url);
			ermSysNotifyConfigAddWin.detach();
		} catch (WrongValueException e) {
			log.error("ErmSysNotifyCfg新增異常"+e);
		}
	}

	@Listen("onClick = #editBtn")
	public void editErmSymNotifyCfg() {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// ermSysNotifyConfig.setUuid(UUIDGenerator.getUUID());
			String tempTypeId = "";
			String tempGroupId = "";
			String tempSubject = "";
			String tempContent = "";
			String tempBeforeDays = "";
			Date date = new Date();
			
			if (webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
				ermSysNotifyConfig.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				ermSysNotifyConfig.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			ermSysNotifyConfig.setCreateDate(date);

			if (!resourceBox.getSelectedItem().getValue().toString().equals("0")) {
				tempTypeId = resourceBox.getSelectedItem().getValue().toString();
				tempTypeId = XSSStringEncoder.encodeXSSString(tempTypeId);
				ermSysNotifyConfig.setTypeId(tempTypeId);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.resourceType") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				resourceBox.focus();
				return;
			}
			/*if (!receiptGroupBox.getSelectedItem().getValue().toString()
					.equals("0")) {
				tempGroupId = receiptGroupBox.getSelectedItem().getValue()
						.toString().trim();
				tempGroupId = XSSStringEncoder.encodeXSSString(tempGroupId);
				//ermSysNotifyConfig.setGroupId(tempGroupId);
				WebOrg tempWebOrg=new WebOrg();
				tempWebOrg.setOrgId(tempGroupId);
				ermSysNotifyConfig.setWebOrg(tempWebOrg);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.receiptGroup") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				receiptGroupBox.focus();
				return;
			}*/

			tempSubject = subjectTxt.getValue().toString().trim();
			if(tempSubject!=null&&!"".equals(tempSubject)){
				tempSubject = XSSStringEncoder.encodeXSSString(tempSubject);
				ermSysNotifyConfig.setSubject(tempSubject);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("ermCodeItem.keyNote") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				subjectTxt.focus();
				return;
			}
			

			tempContent = contentEdt.getValue().toString().trim();
			if(tempContent!=null&&!"".equals(tempContent)){
				ermSysNotifyConfig.setContent(tempContent);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("ermCodeItem.content") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				//receiptGroupBox.focus();
				return;
			}
			

			tempBeforeDays = beforeDaystxt.getValue().toString().trim();
			boolean tempBool = isNumber(tempBeforeDays);
			if (tempBool) {
				tempBeforeDays = XSSStringEncoder.encodeXSSString(tempBeforeDays);
				ermSysNotifyConfig.setBeforeDays(tempBeforeDays);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermSysNotifyConfig.beforeDays") + " "
								+ Labels.getLabel("ermSysNotifyConfig.isNumber"),
						Labels.getLabel("warn"));
				beforeDaystxt.focus();
				return;
			}

			ermSysNotifyConfig.setLatelyChangedUser(webEmployee.getEmployeesn());
			ermSysNotifyConfig.setLatelyChangedDate(new Date());
			((ErmSysNotiyConfigService) SpringUtil
					.getBean("ermSysNotiyConfigService"))
					.editErmSysNotifyCfg(ermSysNotifyConfig);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(
					ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
					"ermSysNotifyConfig_" + tempTypeId + tempGroupId);

			// 新增
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			String url = "ermSysNotifyConfig/ermSysNotifyConfig.zul";
			ZkUtils.refurbishMethod(url);
			ermSysNotifyConfigEdtWin.detach();
		} catch (WrongValueException e) {
			log.error("ermSysNotifyCfg修改異常"+e);
		}
	}

	public boolean isNumber(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			return false;
		}
	}
}
