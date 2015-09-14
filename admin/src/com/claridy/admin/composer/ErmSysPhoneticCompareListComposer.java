package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysPhoneticCompare;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmSysPhoneticCompareService;
import com.claridy.facade.WebSysLogService;

/**
 * zfdong nj 注音符號筆畫對照 清單列 2014/8/6
 */
public class ErmSysPhoneticCompareListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8292992647166345427L;

	@Wire
	private Textbox characterCnTbx;

	private ErmSysPhoneticCompare ermSysTic;
	@Wire
	private Listbox ermSysPhoneticLix;
	@Wire
	private Window ermSysTicAddWin;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			ermSysTic = new ErmSysPhoneticCompare();
			WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmSysPhoneticCompare> ermSysList = ((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).findByCn(
					ermSysTic, loginwebEmployee);
			ListModelList<ErmSysPhoneticCompare> modelList = new ListModelList<ErmSysPhoneticCompare>(ermSysList);
			modelList.setMultiple(true);
			ermSysPhoneticLix.setModel(modelList);
		} catch (Exception e) {
			log.error("phoneTic初始化異常" + e);
		}
	}

	@Listen("onClick=#pagSearchBtn")
	public void save() {
		try {
			ermSysTic = new ErmSysPhoneticCompare();
			String character = characterCnTbx.getValue().toString();
			character = XSSStringEncoder.encodeXSSString(character);
			if (character == null || character.equals("")) {
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"), Labels.getLabel("info"));
				return;
			}
			ermSysTic.setCharacterCn(character);
			WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmSysPhoneticCompare> ermSysList = ((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).findByCn(
					ermSysTic, loginwebEmployee);
			ListModelList<ErmSysPhoneticCompare> modelList = new ListModelList<ErmSysPhoneticCompare>(ermSysList);
			modelList.setMultiple(true);
			ermSysPhoneticLix.setModel(modelList);
		} catch (WrongValueException e) {
			log.error("phoneTic查詢異常" + e);
		}
	}

	@Listen("onClick=#clearBtn")
	public void clear() {
		ListModelList<ErmSysPhoneticCompare> modelList = new ListModelList<ErmSysPhoneticCompare>();
		ermSysPhoneticLix.setMultiple(true);
		ermSysPhoneticLix.setModel(modelList);
	}

	@Listen("onClick=#addBtn")
	public void addErmSys() {
		ermSysTicAddWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermSysPhoneticCompare/ermSysPhoneticCompareAdd.zul", null, null);
		ermSysTicAddWin.doModal();
	}

	@Listen("onClick=#showAll")
	public void showAll() {
		try {
			ermSysTic = new ErmSysPhoneticCompare();
			characterCnTbx.setValue("");
			WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmSysPhoneticCompare> ermSysList = ((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).findByCn(
					ermSysTic, loginwebEmployee);
			ListModelList<ErmSysPhoneticCompare> modelList = new ListModelList<ErmSysPhoneticCompare>(ermSysList);
			modelList.setMultiple(true);
			ermSysPhoneticLix.setModel(modelList);
		} catch (WrongValueException e) {
			log.error("phoneTic顯示全部異常" + e);
		}
	}

	@Listen("onClick=#deleteBtn")
	public void delete() {
		try {
			int select = ermSysPhoneticLix.getSelectedCount();
			if (select > 0) {
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener<Event>() {

					public void onEvent(Event event) throws Exception {
						int clickButton = (Integer) event.getData();
						if (clickButton == Messagebox.OK) {
							Set<Listitem> listitem = ermSysPhoneticLix.getSelectedItems();
							WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for (Listitem ermSysTic : listitem) {
								ErmSysPhoneticCompare ermSys = ermSysTic.getValue();
								ermSys.setIsDataEffId(0);
								((ErmSysPhoneticCompareService) SpringUtil.getBean("ermSysPhoneticCompareService")).delete(ermSys);
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										loginwebEmployee.getEmployeesn(), "phoneTic_" + ermSys.getUuid());

							}
							ZkUtils.refurbishMethod("ermSysPhoneticCompare/ermSysPhoneticCompare.zul");
						}
					}

				});
			} else {
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("PhoneTic刪除異常" + e);
		}
	}

}
