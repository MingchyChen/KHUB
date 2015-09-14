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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmCodeItemService;
import com.claridy.facade.WebSysLogService;

/**
 * 共用代碼類別 lixiangfan
 * 
 * @author nj
 * 
 */
public class GeneralCodeComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8591720837169072811L;

	@Wire
	private Combobox itemIdBox;
	@Wire
	private Textbox generalcode_IdValue;
	@Wire
	private Textbox name1Value;
	@Wire
	private Textbox name2Value;
	@Wire
	private Radiogroup yesOrNoRdp;
	@Wire
	private Textbox noteValue;
	@Wire
	private Window editGeneralCodeWin;
	@Wire
	private Window addGeneralCodeWin;
	private WebEmployee webEmployee;
	@Wire
	private List<ErmCodeItem> ErmCodeItemList;
	@Wire
	private int currentPage;
	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		try {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap = ZkUtils.getExecutionArgs();
			currentPage = Integer.parseInt(tmpMap.get("currentPage"));
			ErmCodeGeneralCode tempErmCodeGeneralCode = ((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService"))
					.findByItemIDAndGeneralcodeId(tmpMap.get("itemId"), (String) tmpMap.get("generalcodeId"));

			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ErmCodeItemList = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).findByhistory(webEmployee);
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			itemIdBox.appendChild(com);
			for (int i = 0; i < ErmCodeItemList.size(); i++) {
				ErmCodeItem ErmCodeItem = new ErmCodeItem();
				ErmCodeItem = ErmCodeItemList.get(i);
				com = new Comboitem();
				com.setLabel(ErmCodeItem.getName1());
				com.setValue(ErmCodeItem.getItemId());
				itemIdBox.appendChild(com);
				if (tempErmCodeGeneralCode.getGeneralcodeId() != null && !"".equals(tempErmCodeGeneralCode)) {
					if (ErmCodeItem.getItemId().equals(tempErmCodeGeneralCode.getErmCodeItem().getItemId())) {
						itemIdBox.setSelectedIndex(i + 1);
					}
				}
			}

			if (tempErmCodeGeneralCode.getGeneralcodeId() != null && !"".equals(tempErmCodeGeneralCode)) {
				// itemIdBox.setValue(tempErmCodeGeneralCode.getErmCodeItem().getItemId());
				generalcode_IdValue.setValue(tempErmCodeGeneralCode.getGeneralcodeId());
				name1Value.setValue(tempErmCodeGeneralCode.getName1());
				name2Value.setValue(tempErmCodeGeneralCode.getName2());
				if ("N".equals(tempErmCodeGeneralCode.getHistory())) {
					yesOrNoRdp.setSelectedIndex(0);
				} else {
					yesOrNoRdp.setSelectedIndex(1);
				}
				noteValue.setValue(tempErmCodeGeneralCode.getNote());
			}
		} catch (Exception e) {
			log.error("共用代碼檔初始化失敗", e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String itemId = "";
			if (itemIdBox.getSelectedItem() != null && !"".equals(itemIdBox.getSelectedItem())) {
				itemId = itemIdBox.getSelectedItem().getValue();
			}
			String generalcode_Id = generalcode_IdValue.getValue();
			String name1 = name1Value.getValue();
			String name2 = name2Value.getValue();
			String yesOrNo = (yesOrNoRdp.getSelectedItem() == null ? null : yesOrNoRdp.getSelectedItem().getValue().toString());
			String note = noteValue.getValue();
			itemId = XSSStringEncoder.encodeXSSString(itemId);
			generalcode_Id = XSSStringEncoder.encodeXSSString(generalcode_Id);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			yesOrNo = XSSStringEncoder.encodeXSSString(yesOrNo);
			note = XSSStringEncoder.encodeXSSString(note);
			if (StringUtils.isBlank(itemId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.itemid") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			} else if (StringUtils.isBlank(generalcode_Id)) {
				ZkUtils.showExclamation(Labels.getLabel("generalcode.generalcode_id") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				generalcode_IdValue.focus();
				return;
			} else if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name1") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				name1Value.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name2") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				name2Value.focus();
				return;
			} else if (StringUtils.isBlank(yesOrNo)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.history") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}
			boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService")).save(itemId, generalcode_Id, name1,
					name2, yesOrNo, note, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"generalcode_" + itemId + generalcode_Id);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "generalCode/generalCode.zul";
				ZkUtils.refurbishMethod(url);
				addGeneralCodeWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(Labels.getLabel("generalcode.itemid") + Labels.getLabel("generalcode.generalcode_id") + Labels.getLabel("isExist"),
						Labels.getLabel("error"));
				clearInput();
				return;

			}
		} catch (WrongValueException e) {
			log.error("共用代碼檔新增失敗", e);
		}
	}

	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		itemIdBox.setText(StringUtils.EMPTY);
		generalcode_IdValue.setText(StringUtils.EMPTY);
	}

	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String itemId = itemIdBox.getSelectedItem().getValue();
			String generalcode_Id = generalcode_IdValue.getValue();
			String name1 = name1Value.getValue();
			String name2 = name2Value.getValue();
			String yesOrNo = (yesOrNoRdp.getSelectedItem() == null ? null : yesOrNoRdp.getSelectedItem().getValue().toString());
			String note = noteValue.getValue();
			itemId = XSSStringEncoder.encodeXSSString(itemId);
			generalcode_Id = XSSStringEncoder.encodeXSSString(generalcode_Id);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			yesOrNo = XSSStringEncoder.encodeXSSString(yesOrNo);
			note = XSSStringEncoder.encodeXSSString(note);
			if (StringUtils.isBlank(itemId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.itemid") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			} else if (StringUtils.isBlank(generalcode_Id)) {
				ZkUtils.showExclamation(Labels.getLabel("generalcode.generalcode_id") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				generalcode_IdValue.focus();
				return;
			} else if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name1") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				name1Value.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name2") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				name2Value.focus();
				return;
			} else if (StringUtils.isBlank(yesOrNo)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.history") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}
			boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService")).update(itemId, generalcode_Id, name1,
					name2, yesOrNo, note, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"generalcode_" + itemId + generalcode_Id);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				addGeneralCodeWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(Labels.getLabel("generalcode.itemid") + Labels.getLabel("generalcode.generalcode_id") + Labels.getLabel("isExist"),
						Labels.getLabel("error"));
				clearInput();
				return;
			}
		} catch (WrongValueException e) {
			log.error("共用代碼檔編輯失敗", e);
		}
	}

	public void editSearchOrgList() {
		try {
			Combobox itemIdBox = (Combobox) addGeneralCodeWin.getParent().getFellowIfAny("itemIdBox");
			Textbox generalcodeIdBoxs = (Textbox) addGeneralCodeWin.getParent().getFellowIfAny("generalcodeIdBox");
			Textbox name1Boxs = (Textbox) addGeneralCodeWin.getParent().getFellowIfAny("name1Box");
			Textbox name2Boxs = (Textbox) addGeneralCodeWin.getParent().getFellowIfAny("name2Box");
			Radiogroup yesOrNoRdp = (Radiogroup) addGeneralCodeWin.getParent().getFellowIfAny("yesOrNoRdp");
			Listbox ermSysPhoneticLix = (Listbox) addGeneralCodeWin.getParent().getFellowIfAny("generalCodeLbx");
			String itemId = itemIdBox.getSelectedItem().getValue();
			String generalcodeId = generalcodeIdBoxs.getValue();
			String name1 = name1Boxs.getValue();
			String name2 = name2Boxs.getValue();
			String yesOrNo = null;
			if (yesOrNoRdp.getSelectedItem() != null) {
				yesOrNo = yesOrNoRdp.getSelectedItem().getValue();
			}
			int selected1 = itemIdBox.getSelectedIndex();
			int selected2 = yesOrNoRdp.getSelectedIndex();
			itemId = XSSStringEncoder.encodeXSSString(itemId);
			generalcodeId = XSSStringEncoder.encodeXSSString(generalcodeId);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			yesOrNo = XSSStringEncoder.encodeXSSString(yesOrNo);

			List<ErmCodeGeneralCode> result = ((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService")).search(itemId,
					generalcodeId, name1, name2, yesOrNo, webEmployee);
			ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(result);
			tmpLML.setMultiple(true);
			ermSysPhoneticLix.setModel(tmpLML);
			ermSysPhoneticLix.setActivePage(currentPage);
			itemIdBox.setSelectedIndex(selected1);
			generalcodeIdBoxs.setValue(generalcodeId);
			name1Boxs.setValue(name1);
			name2Boxs.setValue(name2);
			yesOrNoRdp.setSelectedIndex(selected2);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼檔集合出錯", e);
		}
	}
}
