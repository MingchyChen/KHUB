package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
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
 * 
 * @author lwchen 共用代碼類別 2014/07/25
 * 
 */
public class ErmCodeItemComposer extends SelectorComposer<Component> {

	/**
	 */
	private static final long serialVersionUID = -1611595985756506276L;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Wire
	private Textbox tboxTypeCode;
	@Wire
	private Textbox tboxTypeName;
	@Wire
	private Textbox tboxTypeNameUS;
	@Wire
	private Radiogroup RdpHistory;
	@Wire
	protected Listbox generalCodeLbx;
	@Wire
	private Window addErmCodeItemWin;
	@Wire
	private Window ermCodeItemEditWin;
	@Wire
	private Window addGeneralCodeWin;
	@Wire
	private int currentPage;
	private WebEmployee webEmployee;
	private List<ErmCodeGeneralCode> ermCodeGeneralCodes;
	private ErmCodeGeneralCode ErmCodeGeneralCode;
	private ErmCodeItem ermCodeItem;

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
			ErmCodeItem tempErmCodeItem = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).findByItemId(tmpMap.get("itemId"));
			ermCodeItem = tempErmCodeItem;
			if (null != tempErmCodeItem) {
				tboxTypeCode.setValue(tempErmCodeItem.getItemId());
				tboxTypeName.setValue(tempErmCodeItem.getName1());
				tboxTypeNameUS.setValue(tempErmCodeItem.getName2());
				String history = tempErmCodeItem.getHistory();
				if (history != null && !history.equals("")) {
					if (history.equals("Y")) {
						RdpHistory.setSelectedIndex(0);
					} else if (history.equals("N")) {
						RdpHistory.setSelectedIndex(1);
					}
				}
				ermCodeGeneralCodes = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).findErmCodeGeneralCodeItemId(
						tempErmCodeItem.getItemId(), webEmployee);
				ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(ermCodeGeneralCodes);
				tmpLML.setMultiple(true);
				generalCodeLbx.setModel(tmpLML);
			} else {
				RdpHistory.setSelectedIndex(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別頁面報錯", e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String itemid = tboxTypeCode.getValue();
			String name1 = tboxTypeName.getValue();
			String name2 = tboxTypeNameUS.getValue();
			String history = (RdpHistory.getSelectedItem() == null ? null : RdpHistory.getSelectedItem().getValue().toString());
			itemid = XSSStringEncoder.encodeXSSString(itemid);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			history = XSSStringEncoder.encodeXSSString(history);

			if (StringUtils.isBlank(itemid)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.code") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeCode.focus();
				return;
			} else if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.typeName") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeName.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.typeNameUS") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeNameUS.focus();
				return;
			}
			boolean saveStatus = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).save(itemid, name1, name2, history, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"ermCodeItem_" + itemid);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "codeType/codeType.zul";
				ZkUtils.refurbishMethod(url);
				addErmCodeItemWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(Labels.getLabel("ermCodeItem.code") + Labels.getLabel("isExist"), Labels.getLabel("error"));
				clearInput();
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別新增方法報錯", e);
		}
	}

	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		tboxTypeCode.setText(StringUtils.EMPTY);
	}

	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String itemid = tboxTypeCode.getValue();
			String name1 = tboxTypeName.getValue();
			String name2 = tboxTypeNameUS.getValue();
			String history = (RdpHistory.getSelectedItem() == null ? null : RdpHistory.getSelectedItem().getValue().toString());
			itemid = XSSStringEncoder.encodeXSSString(itemid);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			history = XSSStringEncoder.encodeXSSString(history);

			if (StringUtils.isBlank(itemid)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.code") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeCode.focus();
				return;
			} else if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.typeName") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeName.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodeItem.typeNameUS") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxTypeNameUS.focus();
				return;
			}

			boolean saveStatus = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).update(itemid, name1, name2, history, webEmployee);
			if (saveStatus) {

				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"ermCodeItem_" + itemid);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				ermCodeItemEditWin.detach();
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別修改方法報錯", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Listen("onClick = #addBtn")
	public void add() {
		try {
			Map map = new HashMap();
			map.put("item_id", tboxTypeCode.getValue());
			addGeneralCodeWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/codeType/generalCodeAdd.zul", this.getSelf(), map);
			addGeneralCodeWin.doModal();
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別明細新增頁面跳轉報錯", e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = generalCodeLbx.getSelectedCount();
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) throws Exception {
						int clickedButton = (Integer) event.getData();
						if (clickedButton == Messagebox.OK) {
							Set<Listitem> selectedModels = generalCodeLbx.getSelectedItems();
							for (Listitem tmpEST : selectedModels) {
								ErmCodeGeneralCode = tmpEST.getValue();
								((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService")).delete(ErmCodeGeneralCode
										.getErmCodeItem().getItemId(), ErmCodeGeneralCode.getGeneralcodeId());
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										webEmployee.getEmployeesn(), "generalcode_" + ErmCodeGeneralCode.getErmCodeItem().getItemId()
												+ ErmCodeGeneralCode.getGeneralcodeId());
							}
							searchErmCodeItem();
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
			// TODO: handle exception
			log.error("共用代碼類別刪除報錯", e);
		}
	}

	public void searchErmCodeItem() {
		try {
			ermCodeGeneralCodes = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).findErmCodeGeneralCodeItemId(
					ermCodeItem.getItemId(), webEmployee);
			ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(ermCodeGeneralCodes);
			tmpLML.setMultiple(true);
			generalCodeLbx.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別頁面報錯", e);
		}
	}

	public void editSearchOrgList() {
		Textbox codeBoxs = (Textbox) ermCodeItemEditWin.getParent().getFellowIfAny("codeBox");
		Textbox typeNameBoxs = (Textbox) ermCodeItemEditWin.getParent().getFellowIfAny("typeNameBox");
		Textbox typeNameUSBoxs = (Textbox) ermCodeItemEditWin.getParent().getFellowIfAny("typeNameUSBox");
		Radiogroup historyRdp = (Radiogroup) ermCodeItemEditWin.getParent().getFellowIfAny("historyRdp");
		Listbox codeLix = (Listbox) ermCodeItemEditWin.getParent().getFellowIfAny("codeLix");
		try {
			String codeBox = codeBoxs.getValue().trim();
			String typeNameBox = typeNameBoxs.getValue().trim();
			String typeNameUSBox = typeNameUSBoxs.getValue().trim();
			int selectradio = historyRdp.getSelectedIndex();
			codeBox = XSSStringEncoder.encodeXSSString(codeBox);
			typeNameBox = XSSStringEncoder.encodeXSSString(typeNameBox);
			typeNameUSBox = XSSStringEncoder.encodeXSSString(typeNameUSBox);

			String history = (historyRdp.getSelectedItem() == null ? null : historyRdp.getSelectedItem().getValue().toString());
			history = XSSStringEncoder.encodeXSSString(history);
			ErmCodeItem ermCodeItem = new ErmCodeItem();
			ermCodeItem.setItemId(codeBox);
			ermCodeItem.setName1(typeNameBox);
			ermCodeItem.setName2(typeNameUSBox);
			ermCodeItem.setHistory(history);

			List<ErmCodeItem> result = ((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).findErmCodeItem(ermCodeItem, webEmployee);
			ListModelList<ErmCodeItem> tmpLML = new ListModelList<ErmCodeItem>(result);
			tmpLML.setMultiple(true);
			codeLix.setModel(tmpLML);

			codeLix.setActivePage(currentPage);
			codeBoxs.setValue(codeBox);
			typeNameBoxs.setValue(typeNameBox);
			typeNameUSBoxs.setValue(typeNameUSBox);
			historyRdp.setSelectedIndex(selectradio);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別集合出錯", e);
		}
	}
}
