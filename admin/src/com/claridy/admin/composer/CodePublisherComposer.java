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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.*;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.ErmCodePublisherpersonService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * engin 代理商出版商資料Add,Edit 2014/07/28
 * 
 */
public class CodePublisherComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8591720837169072811L;

	@Wire
	private Textbox tboxPublisherId;
	@Wire
	private Textbox tboxNameZhTw;
	@Wire
	private Textbox tboxUrl;
	@Wire
	private Textbox tboxCountry;
	@Wire
	private Textbox tboxZip;
	@Wire
	private Textbox tboxCity;
	@Wire
	private Textbox tboxAddress;
	@Wire
	private Combobox cboxContact;
	@Wire
	private Textbox tboxNote;
	@Wire
	private Listbox publisherLix;
	@Wire
	protected List<ErmCodeGeneralCode> ermCodeGeneralCodeList;
	@Wire
	private Window editCodePublisherWin;
	@Wire
	private Window addCodePublisherWin;
	@Wire
	private Window addErmCodePublisherpersonWin;
	@WireVariable
	private ErmCodePublisherPerson ermCodePublisherPerson;
	@WireVariable
	private List<ErmCodePublisherPerson> ermCodePublisherPersonList;
	@Wire
	private int currentPage;
	private WebEmployee webEmployee;
	ErmCodePublisher eRMCodePublisher;
	private final Logger log = LoggerFactory.getLogger(getClass());

	// private RandomIDGenerator randomIDGenerator;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ermCodeGeneralCodeList = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).findErmCodeGeneralCodeByItemId("CONTACT");
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		Map<String, String> tmpMap = new HashMap<String, String>();
		tmpMap = ZkUtils.getExecutionArgs();
		String publisherId = tmpMap.get("publisherId");
		currentPage = Integer.parseInt(tmpMap.get("currentPage"));
		ErmCodePublisher tempERMCodePublisher = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).findByPublisherID(publisherId);
		eRMCodePublisher = tempERMCodePublisher;
		if (null != tempERMCodePublisher) {
			tboxPublisherId.setValue(tempERMCodePublisher.getPublisherId());
			tboxNameZhTw.setValue(tempERMCodePublisher.getName());
			tboxUrl.setValue(tempERMCodePublisher.getUrl());
			tboxCountry.setValue(tempERMCodePublisher.getCountry());
			tboxZip.setValue(tempERMCodePublisher.getZip());
			tboxCity.setValue(tempERMCodePublisher.getCity());
			tboxAddress.setValue(tempERMCodePublisher.getAddress());
			String contact = tempERMCodePublisher.getContact();
			int is_select = 0;
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			cboxContact.appendChild(com);
			for (int i = 0; i < ermCodeGeneralCodeList.size(); i++) {
				ErmCodeGeneralCode tempEcgc = ermCodeGeneralCodeList.get(i);
				com = new Comboitem();
				com.setLabel(tempEcgc.getName1());
				com.setValue(tempEcgc.getGeneralcodeId());
				if (contact != null && !"".equals(contact)) {
					if (contact.equals(tempEcgc.getGeneralcodeId())) {
						is_select = i + 1;
					}
				}
				cboxContact.appendChild(com);
			}
			cboxContact.setSelectedIndex(is_select);
			tboxNote.setValue(tempERMCodePublisher.getNote());

			// 當頁面為編輯時，加載設定明細頁面的list
			ermCodePublisherPersonList = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService"))
					.findErmCodePublisherPerson(publisherId, "", webEmployee);

			ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(ermCodePublisherPersonList);
			tmpLML.setMultiple(true);
			publisherLix.setModel(tmpLML);
		} else {
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("0");
			cboxContact.appendChild(com);
			for (int i = 0; i < ermCodeGeneralCodeList.size(); i++) {
				ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
				ermCodeGeneralCode = ermCodeGeneralCodeList.get(i);
				com = new Comboitem();
				com.setLabel(ermCodeGeneralCode.getName1());
				com.setValue(ermCodeGeneralCode.getGeneralcodeId());
				cboxContact.appendChild(com);
			}
			cboxContact.setSelectedIndex(0);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			// String publisherId = tboxPublisherId.getValue();
			String publisherId = RandomIDGenerator.getSerialNumber("PU");
			String name = tboxNameZhTw.getValue();
			String url = tboxUrl.getValue();
			if (!StringUtils.isBlank(url)) {
				if (!ZkUtils.isUrl(url)) {
					ZkUtils.showExclamation(Labels.getLabel("urlError"), Labels.getLabel("warn"));
					tboxUrl.focus();
					return;
				}
			}
			String country = tboxCountry.getValue();
			String zip = tboxZip.getValue();
			String city = tboxCity.getValue();
			String address = tboxAddress.getValue();
			String contact = cboxContact.getSelectedItem().getValue();
			String note = tboxNote.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			name = XSSStringEncoder.encodeXSSString(name);
			url = XSSStringEncoder.encodeXSSString(url);
			country = XSSStringEncoder.encodeXSSString(country);
			zip = XSSStringEncoder.encodeXSSString(zip);
			city = XSSStringEncoder.encodeXSSString(city);
			address = XSSStringEncoder.encodeXSSString(address);
			contact = XSSStringEncoder.encodeXSSString(contact);
			note = XSSStringEncoder.encodeXSSString(note);

			if (StringUtils.isBlank(publisherId)) {
				// 代碼不能為空
				ZkUtils.showExclamation(Labels.getLabel("codePublisher.codePublisherTbx.publisherId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(name)) {
				// 名稱不能為空！
				ZkUtils.showExclamation(Labels.getLabel("codePublisher.codePublisherTbx.name") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxNameZhTw.focus();
				return;
			}
			boolean saveStatus = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).save(publisherId, name, url, country, zip, city,
					address, contact, note, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"codePublisher_" + publisherId);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String toUrl = "codePublisher/codePublisher.zul";
				ZkUtils.refurbishMethod(toUrl);
				addCodePublisherWin.detach();
			} else {
				// "代碼已存在"
				ZkUtils.showError(Labels.getLabel("codePublisher.codePublisherTbx.publisherId") + Labels.getLabel("isExist"),
						Labels.getLabel("error"));
				clearInput();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("存儲代理商出版商資料錯誤：", e);

		}
	}

	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		tboxPublisherId.setText(StringUtils.EMPTY);
	}

	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String publisherId = tboxPublisherId.getValue();
			String name = tboxNameZhTw.getValue();
			String url = tboxUrl.getValue();
			if (!StringUtils.isBlank(url)) {
				if (!ZkUtils.isUrl(url)) {
					ZkUtils.showExclamation(Labels.getLabel("urlError"), Labels.getLabel("warn"));
					tboxUrl.focus();
					return;
				}
			}
			String country = tboxCountry.getValue();
			String zip = tboxZip.getValue();
			String city = tboxCity.getValue();
			String address = tboxAddress.getValue();
			String contact = cboxContact.getSelectedItem().getValue();
			String note = tboxNote.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			name = XSSStringEncoder.encodeXSSString(name);
			url = XSSStringEncoder.encodeXSSString(url);
			country = XSSStringEncoder.encodeXSSString(country);
			zip = XSSStringEncoder.encodeXSSString(zip);
			city = XSSStringEncoder.encodeXSSString(city);
			address = XSSStringEncoder.encodeXSSString(address);
			contact = XSSStringEncoder.encodeXSSString(contact);
			note = XSSStringEncoder.encodeXSSString(note);

			if (StringUtils.isBlank(publisherId)) {
				ZkUtils.showExclamation(Labels.getLabel("codePublisher.codePublisherTbx.publisherId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(name)) {
				ZkUtils.showExclamation(Labels.getLabel("codePublisher.codePublisherTbx.name") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxNameZhTw.focus();
				return;
			}

			boolean saveStatus = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).update(publisherId, name, url, country, zip,
					city, address, contact, note, webEmployee);
			if (saveStatus) {

				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"codePublisher_" + publisherId);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				editCodePublisherWin.detach();
			} else {
				// 功能號已存在
				ZkUtils.showError(Labels.getLabel("codePublisher.codePublisherTbx.publisherId") + " " + Labels.getLabel("isExist"),
						Labels.getLabel("error"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("更新代理商出版商資料錯誤：", e);
		}
	}

	// ==================處理明細檔資料===================
	@Listen("onClick = #addBtn")
	public void add() {
		Map<String, String> map = new HashMap<String, String>();
		// 跳轉到Add頁面是根據map中的key獲取value值
		map.put("publisherId", tboxPublisherId.getValue());
		map.put("personName", "");
		addErmCodePublisherpersonWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/codePublisher/codePublisherpersonAdd.zul",
				this.getSelf(), map);
		addErmCodePublisherpersonWin.doModal();
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		int size = publisherLix.getSelectedCount();
		try {
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) throws Exception {
						int clickedButton = (Integer) event.getData();
						if (clickedButton == Messagebox.OK) {
							Set<Listitem> selectedModels = publisherLix.getSelectedItems();
							WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
							for (Listitem tmpEST : selectedModels) {
								ermCodePublisherPerson = tmpEST.getValue();
								((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService")).update(
										ermCodePublisherPerson.getPublisherId(), ermCodePublisherPerson.getPersonName(),
										ermCodePublisherPerson.getMail(), ermCodePublisherPerson.getTelephone(), ermCodePublisherPerson.getFax(),
										ermCodePublisherPerson.getTitle(), 0, webEmployee);
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										loginwebEmployee.getEmployeesn(), "org_" + webEmployee.getEmployeesn());
							}
							getCodePublisherList();
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
			log.error("刪除代理商出版商資料錯誤：", e);
		}
	}

	public void getCodePublisherList() {
		// 當頁面為編輯時，加載設定明細頁面的list
		ermCodePublisherPersonList = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService"))
				.findErmCodePublisherPerson(eRMCodePublisher.getPublisherId(), "", webEmployee);

		ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(ermCodePublisherPersonList);
		tmpLML.setMultiple(true);
		publisherLix.setModel(tmpLML);
	}

	public void editSearchOrgList() {
		Textbox publisherIdBoxs = (Textbox) editCodePublisherWin.getParent().getFellowIfAny("publisherIdBox");
		Textbox publisherNameBoxs = (Textbox) editCodePublisherWin.getParent().getFellowIfAny("publisherNameBox");
		Listbox codePublisherLbx = (Listbox) editCodePublisherWin.getParent().getFellowIfAny("codePublisherLbx");
		try {
			String publisherId = publisherIdBoxs.getValue().trim();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			String publisherName = publisherNameBoxs.getValue().trim();
			publisherName = XSSStringEncoder.encodeXSSString(publisherName);
			List<ErmCodePublisher> ermCodePublisherList = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).search(publisherId,
					publisherName, webEmployee);

			ListModelList<ErmCodePublisher> ErmCodePublisherModel = new ListModelList<ErmCodePublisher>(ermCodePublisherList);
			ErmCodePublisherModel.setMultiple(true);
			codePublisherLbx.setModel(ErmCodePublisherModel);
			codePublisherLbx.setActivePage(currentPage);
			publisherIdBoxs.setValue(publisherId);
			publisherNameBoxs.setValue(publisherName);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("代理商出版商集合出錯", e);
		}
	}
}
