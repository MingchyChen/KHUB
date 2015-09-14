package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysPhoneticCompare;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmSysPhoneticCompareService;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebSysLogService;

/**
 * zfdong nj 注音符號筆畫對照修改 新增 2014/8/6
 */
public class ErmSysPhoneticCompareComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3462602595740739494L;

	@Wire
	private Window ermSysTicAddWin;
	@Wire
	private Window ermSysTicEditWin;
	@Wire
	private Textbox characterCnTbx;
	@Wire
	private Textbox characterNumTbx;
	@Wire
	private Textbox phoneticOneTbx;
	@Wire
	private Textbox phoneticTwoTbx;
	@Wire
	private int currentPage;
	@Wire
	private WebEmployee loginwebEmployee;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ErmSysPhoneticCompare ermSys;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			currentPage = Integer.parseInt(map.get("currentPage"));
			String uuid = map.get("ermSysPhoneticUuid");
			ermSys = new ErmSysPhoneticCompare();
			loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if (uuid != null) {
				ermSys.setUuid(uuid);
				ermSys = ((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).findByCn(ermSys, loginwebEmployee)
						.get(0);
				characterCnTbx.setValue(ermSys.getCharacterCn());
				characterNumTbx.setValue(Integer.toString(ermSys.getCharacterNum()));
				phoneticOneTbx.setValue(ermSys.getPhoneticOne());
				phoneticTwoTbx.setValue(ermSys.getPhoneticTwo());
			}
		} catch (Exception e) {
			log.error("PhoneTic編輯初始化異常" + e);
		}
	}

	@Listen("onClick=#addBtn")
	public void saveErmSysTic() {
		try {
			ermSys = new ErmSysPhoneticCompare();
			if (!characterCnTbx.getValue().equals("") && characterCnTbx.getValue() != null) {
				String characterCn = characterCnTbx.getValue().trim();
				characterCn = XSSStringEncoder.encodeXSSString(characterCn);
				ermSys.setCharacterCn(characterCn);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterCn"), Labels.getLabel("info"));
				characterCnTbx.focus();
				return;
			}
			if (!characterNumTbx.getValue().equals("") && characterNumTbx != null) {
				String characterNum = characterNumTbx.getValue().trim();
				characterNum = XSSStringEncoder.encodeXSSString(characterNum);
				Pattern pattern = Pattern.compile("[0-9]*");
				if (pattern.matcher(characterNum).matches()) {
					ermSys.setCharacterNum(Integer.parseInt(characterNum));
				} else {
					ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterNotNum"), Labels.getLabel("info"));
					characterNumTbx.focus();
					return;
				}
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterNum"), Labels.getLabel("info"));
				characterNumTbx.focus();
				return;
			}
			if (!phoneticOneTbx.getValue().equals("") && phoneticOneTbx != null) {
				String phoneticOne = phoneticOneTbx.getValue().trim();
				phoneticOne = XSSStringEncoder.encodeXSSString(phoneticOne);
				ermSys.setPhoneticOne(phoneticOne);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.phoneticOne"), Labels.getLabel("info"));
				phoneticOneTbx.focus();
				return;
			}
			if (!phoneticTwoTbx.getValue().equals("") && phoneticTwoTbx.getValue() != null) {
				String phoneticTwo = phoneticTwoTbx.getValue().trim();
				phoneticTwo = XSSStringEncoder.encodeXSSString(phoneticTwo);
				ermSys.setPhoneticTwo(phoneticTwo);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.phoneticTwo"), Labels.getLabel("info"));
				phoneticTwoTbx.focus();
				return;
			}
			WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ermSys.setUuid(UUIDGenerator.getUUID());
			ermSys.setWebEmployee(loginwebEmployee);
			ermSys.setCreateDate(new Date());

			if (loginwebEmployee.getWeborg() != null && loginwebEmployee.getWeborg().getOrgId() != null
					&& !"0".equals(loginwebEmployee.getWeborg().getOrgId())) {
				ermSys.setDataOwnerGroup(loginwebEmployee.getWeborg().getOrgId());
			} else {
				ermSys.setDataOwnerGroup(loginwebEmployee.getParentWebOrg().getOrgId());
			}

			ermSys.setIsDataEffId(1);
			((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).save(ermSys);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(),
					"phoneTic_" + ermSys.getUuid());
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			ZkUtils.refurbishMethod("ermSysPhoneticCompare/ermSysPhoneticCompare.zul");
			ermSysTicAddWin.detach();
		} catch (Exception e) {
			log.error("Phone新增異常" + e);
		}
	}

	@Listen("onClick=#editBtn")
	public void update() {
		try {
			if (!characterCnTbx.getValue().equals("") && characterCnTbx.getValue() != null) {
				String characterCn = characterCnTbx.getValue().trim();
				characterCn = XSSStringEncoder.encodeXSSString(characterCn);
				ermSys.setCharacterCn(characterCn);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterCn"), Labels.getLabel("info"));
				characterCnTbx.focus();
				return;
			}
			if (!characterNumTbx.getValue().equals("") && characterNumTbx != null) {
				String characterNum = characterNumTbx.getValue().trim();
				characterNum = XSSStringEncoder.encodeXSSString(characterNum);
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(characterNum);
				if (isNum.matches()) {
					ermSys.setCharacterNum(Integer.parseInt(characterNum));
				} else {
					ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterNotNum"), Labels.getLabel("info"));
					characterNumTbx.focus();
					return;
				}
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.characterNum"), Labels.getLabel("info"));
				characterNumTbx.focus();
				return;
			}
			if (!phoneticOneTbx.getValue().equals("") && phoneticOneTbx != null) {
				String phoneticOne = phoneticOneTbx.getValue().trim();
				phoneticOne = XSSStringEncoder.encodeXSSString(phoneticOne);
				ermSys.setPhoneticOne(phoneticOne);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.phoneticOne"), Labels.getLabel("info"));
				phoneticOneTbx.focus();
				return;
			}
			if (!phoneticTwoTbx.getValue().equals("") && phoneticTwoTbx.getValue() != null) {
				String phoneticTwo = phoneticTwoTbx.getValue().trim();
				phoneticTwo = XSSStringEncoder.encodeXSSString(phoneticTwo);
				ermSys.setPhoneticTwo(phoneticTwo);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("phoneTicComposer.phoneticTwo"), Labels.getLabel("info"));
				phoneticOneTbx.focus();
				return;
			}

			WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ermSys.setLatelyChangedUser(loginwebEmployee.getEmployeeName());
			ermSys.setLatelyChangedDate(new Date());
			((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).save(ermSys);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(),
					"phoneTic_" + ermSys.getUuid());
			ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
			editSearchOrgList();
			ermSysTicEditWin.detach();
		} catch (Exception e) {
			log.error("Phone編輯異常", e);
		}
	}

	public void editSearchOrgList() {
		Textbox employeeNameBox = (Textbox) ermSysTicEditWin.getParent().getFellowIfAny("characterCnTbx");
		Listbox ermSysPhoneticLix = (Listbox) ermSysTicEditWin.getParent().getFellowIfAny("ermSysPhoneticLix");
		try {
			String characterCn = employeeNameBox.getValue().trim();
			characterCn = XSSStringEncoder.encodeXSSString(characterCn);
			loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ErmSysPhoneticCompare ermSysTic = new ErmSysPhoneticCompare();
			ermSysTic.setCharacterCn(characterCn);
			List<ErmSysPhoneticCompare> ermSysList = ((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).findByCn(
					ermSysTic, loginwebEmployee);
			ListModelList<ErmSysPhoneticCompare> modelList = new ListModelList<ErmSysPhoneticCompare>(ermSysList);
			modelList.setMultiple(true);
			ermSysPhoneticLix.setModel(modelList);
			ermSysPhoneticLix.setActivePage(currentPage);
			if (!StringUtils.isBlank(characterCn)) {
				employeeNameBox.setValue(characterCn);
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error("Phone集合出錯", e);
		}
	}
}
