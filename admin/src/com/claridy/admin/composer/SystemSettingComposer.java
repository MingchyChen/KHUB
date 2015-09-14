package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.SystemSettingService;
import com.claridy.facade.WebSysLogService;

public class SystemSettingComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8591720837169072811L;
	// SystemSetting
	@Wire
	private Textbox tboxFuncId;
	@Wire
	private Textbox tboxFuncValue;
	@Wire
	private Textbox tboxFuncName;
	@Wire
	private Window editSysSettingWin;
	@Wire
	private Window addSysSettingWin;
	@Wire
	private int currentPage;
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap = ZkUtils.getExecutionArgs();
			currentPage = Integer.parseInt(tmpMap.get("currentPage"));
			ErmSystemSetting tempERMSystemSetting = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).findByFunID(tmpMap
					.get("funcId"));
			if (null != tempERMSystemSetting) {
				tboxFuncId.setValue(tempERMSystemSetting.getFuncId());
				tboxFuncValue.setValue(tempERMSystemSetting.getFuncValue());
				tboxFuncName.setValue(tempERMSystemSetting.getFuncName());
			}
		} catch (Exception e) {
			log.error("systemSettingEdit.zul初始化失敗", e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String funcId = tboxFuncId.getValue();
			String funcValue = tboxFuncValue.getValue();
			String funcName = tboxFuncName.getValue();
			funcId = XSSStringEncoder.encodeXSSString(funcId);
			funcValue = XSSStringEncoder.encodeXSSString(funcValue);
			funcName = XSSStringEncoder.encodeXSSString(funcName);

			if (StringUtils.isBlank(funcId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncId.focus();
				return;
			} else if (StringUtils.isBlank(funcValue)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcValue") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncValue.focus();
				return;
			} else if (StringUtils.isBlank(funcName)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcName") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncName.focus();
				return;
			}
			boolean saveStatus = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).save(funcId, funcValue, funcName, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"sysSetting_" + funcId);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "sysSetting/systemSetting.zul";
				ZkUtils.refurbishMethod(url);
				addSysSettingWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(Labels.getLabel("systemSetting.sysSettingLbx.funcId") + Labels.getLabel("isExist"), Labels.getLabel("error"));
				clearInput();
			}
		} catch (WrongValueException e) {
			log.error("systemSettingEdit.zul新增失敗", e);
		}
	}

	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		tboxFuncId.setText(StringUtils.EMPTY);
	}

	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String funcId = tboxFuncId.getValue();
			String funcValue = tboxFuncValue.getValue();
			String funcName = tboxFuncName.getValue();
			funcId = XSSStringEncoder.encodeXSSString(funcId);
			funcValue = XSSStringEncoder.encodeXSSString(funcValue);
			funcName = XSSStringEncoder.encodeXSSString(funcName);

			if (StringUtils.isBlank(funcId)) {
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncId.focus();
				return;
			} else if (StringUtils.isBlank(funcValue)) {
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcValue") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncValue.focus();
				return;
			} else if (StringUtils.isBlank(funcName)) {
				ZkUtils.showExclamation(Labels.getLabel("systemSetting.sysSettingLbx.funcName") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxFuncName.focus();
				return;
			}

			boolean saveStatus = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).update(funcId, funcValue, funcName, webEmployee);
			if (saveStatus) {

				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"sysSetting_" + funcId);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				editSysSettingWin.detach();
			} else {
				// 功能號已存在
				ZkUtils.showError(Labels.getLabel("systemSetting.sysSettingLbx.funcId") + " " + Labels.getLabel("isExist"), Labels.getLabel("error"));
			}
		} catch (WrongValueException e) {
			log.error("systemSettingEdit.zul編輯失敗", e);
		}
	}

	public void editSearchOrgList() {
		Textbox keywordBox = (Textbox) editSysSettingWin.getParent().getFellowIfAny("keywordBox");
		Listbox sysSettingLbx = (Listbox) editSysSettingWin.getParent().getFellowIfAny("sysSettingLbx");
		try {
			String keyword = keywordBox.getValue().trim();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			List<ErmSystemSetting> result = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).search(keyword, webEmployee);
			ListModelList<ErmSystemSetting> tmpLML = new ListModelList<ErmSystemSetting>(result);
			tmpLML.setMultiple(true);
			sysSettingLbx.setModel(tmpLML);
			sysSettingLbx.setActivePage(currentPage);
			keywordBox.setValue(keyword);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("systemSettingEdit.zul集合出錯", e);
		}
	}
}
