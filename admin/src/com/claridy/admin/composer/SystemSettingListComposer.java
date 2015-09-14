package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ResourcesMainfileSolrSearch;
import com.claridy.facade.SystemSettingService;
import com.claridy.facade.WebSysLogService;

public class SystemSettingListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8994020132060546141L;
	// systemSetting
	@Wire
	private Textbox keywordBox;
	@Wire
	protected Listbox sysSettingLbx;
	@Wire
	protected Grid sysSettingGrid;
	@Wire
	private Window addSysSettingWin;
	@Wire
	private Window webSysLogWin;
	@WireVariable
	private List<ErmSystemSetting> ermSystemSettingList;
	@WireVariable
	private ErmSystemSetting ermSystemSetting;
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			String keyword = keywordBox.getValue().trim();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			if (StringUtils.isBlank(keyword)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"), Labels.getLabel("warn"));
				keywordBox.focus();
				return;
			}
			List<ErmSystemSetting> result = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).search(keyword, webEmployee);
			ListModelList<ErmSystemSetting> tmpLML = new ListModelList<ErmSystemSetting>(result);
			tmpLML.setMultiple(true);
			sysSettingLbx.setModel(tmpLML);
		} catch (WrongValueException e) {
			log.error("systemSetting.zul搜尋失敗", e);
		}
	}

	@Listen("onClick = #addSolr")
	public void addSolr() {
		try {
			((ResourcesMainfileSolrSearch) SpringUtil.getBean("resourcesMainfileSolrSearch")).addData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		try {
			keywordBox.setValue("");
			List<ErmSystemSetting> result = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).search("", webEmployee);
			ListModelList<ErmSystemSetting> tmpLML = new ListModelList<ErmSystemSetting>(result);
			tmpLML.setMultiple(true);
			sysSettingLbx.setModel(tmpLML);
		} catch (WrongValueException e) {
			log.error("systemSetting.zul顯示全部失敗", e);
		}
	}

	@Listen("onClick = #addBtn")
	public void add() {
		try {
			addSysSettingWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/sysSetting/systemSettingAdd.zul", null, null);
			addSysSettingWin.doModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * @Listen("onClick = #editBtn") public void edit() { int sumChecked =
	 * sysSettingLbx.getSelectedCount(); if (sumChecked == 1) { ermSystemSetting
	 * = sysSettingLbx.getSelectedItem().getValue(); String funcId=
	 * ermSystemSetting.getFunc_id(); String
	 * funcValue=ermSystemSetting.getFunc_value(); String
	 * funcName=ermSystemSetting.getFunc_name();
	 * 
	 * Map<String, String> params = new HashMap<String, String>();
	 * params.put("func_id",funcId); params.put("func_value", funcValue);
	 * params.put("func_name",funcName ); editSysSettingWin = (Window)
	 * ZkUtils.createComponents(
	 * "/WEB-INF/pages/system/sysSetting/systemSettingEdit.zul", null, params);
	 * editSysSettingWin.doModal(); } else if (sumChecked > 1) {
	 * //"只能選擇一筆資料進行編輯"
	 * ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"),
	 * Labels.getLabel("warn")); return; } else { //"請先選擇一筆資料進行編輯"
	 * ZkUtils.showExclamation(Labels.getLabel("selectOneData"),
	 * Labels.getLabel("info")); return; } }
	 */

	@Command
	public void viewLog() {
		try {
			ermSystemSetting = sysSettingLbx.getSelectedItem().getValue();
			Map<String, String> params = new HashMap<String, String>();
			params.put("nlocate", "sysSetting_" + ermSystemSetting.getFuncId());
			webSysLogWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/common/webSysLog.zul", null, params);
			webSysLogWin.doModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = sysSettingLbx.getSelectedCount();
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) throws Exception {
						int clickedButton = (Integer) event.getData();
						if (clickedButton == Messagebox.OK) {
							Set<Listitem> selectedModels = sysSettingLbx.getSelectedItems();
							for (Listitem tmpEST : selectedModels) {
								ermSystemSetting = tmpEST.getValue();
								((SystemSettingService) SpringUtil.getBean("systemSettingService")).deleteSystemSetting(ermSystemSetting.getFuncId());
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										webEmployee.getEmployeesn(), "sysSetting_" + ermSystemSetting.getFuncId());
							}
							Desktop dkp = Executions.getCurrent().getDesktop();
							Page page = dkp.getPageIfAny("templatePage");
							Include contentInclude = (Include) page.getFellowIfAny("contentInclude");
							contentInclude.setSrc("home.zul");
							contentInclude.setSrc("sysSetting/systemSetting.zul");
						} else {
							// 取消刪除
							return;
						}
					}
				});
			} else {
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectMultData"), Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("systemSetting.zul刪除失敗", e);
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// 初始頁面加載
			ermSystemSettingList = ((SystemSettingService) SpringUtil.getBean("systemSettingService")).findAll(webEmployee);
			ListModelList<ErmSystemSetting> tmpLML = new ListModelList<ErmSystemSetting>(ermSystemSettingList);
			tmpLML.setMultiple(true);
			sysSettingLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("systemSetting.zul初始化失敗", e);
		}
	}

	/**
	 * @return the ermSystemSetting
	 */
	public ErmSystemSetting getErmSystemSetting() {

		return ermSystemSetting;
	}

	/**
	 * @param ermSystemSetting
	 *            the ermSystemSetting to set
	 */
	public void setErmSystemSetting(ErmSystemSetting ermSystemSetting) {
		this.ermSystemSetting = ermSystemSetting;
	}

}
