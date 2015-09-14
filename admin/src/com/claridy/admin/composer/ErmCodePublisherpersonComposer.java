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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.ErmCodePublisherpersonService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author lwchen 代理商出版商聯絡人 2014/07/25
 * 
 */
public class ErmCodePublisherpersonComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1379084135571132414L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Combobox tboxPublisherId;
	@Wire
	private Textbox tboxPersonname;
	@Wire
	private Textbox tboxEmail;
	@Wire
	private Textbox tboxTelephone;
	@Wire
	private Textbox tboxFax;
	@Wire
	private Textbox tboxTitle;
	@Wire
	private int currentPage;
	@Wire
	private Window addErmCodePublisherpersonWin;
	@Wire
	private Window editErmCodePublisherpersonWin;
	private WebEmployee webEmployee;
	private List<ErmCodePublisher> ermCodePublishers;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		try {
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap = ZkUtils.getExecutionArgs();
			currentPage = Integer.parseInt(tmpMap.get("currentPage"));
			ErmCodePublisherPerson tempErmCodePublisherPerson = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService"))
					.findErmCodePublisherPersonById(tmpMap.get("publisherId"), tmpMap.get("personName"), webEmployee);

			ermCodePublishers = ((CodePublisherService) SpringUtil.getBean("codePublisherService")).findAll(webEmployee);
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			tboxPublisherId.appendChild(com);
			for (int i = 0; i < ermCodePublishers.size(); i++) {
				ErmCodePublisher ermCodePublisher = new ErmCodePublisher();
				ermCodePublisher = ermCodePublishers.get(i);
				com = new Comboitem();
				com.setLabel(ermCodePublisher.getName());
				com.setValue(ermCodePublisher.getPublisherId());
				tboxPublisherId.appendChild(com);
				if (tempErmCodePublisherPerson != null && !"".equals(tempErmCodePublisherPerson.getPublisherId())) {
					if (ermCodePublisher.getPublisherId().equals(tempErmCodePublisherPerson.getPublisherId())) {
						tboxPublisherId.setSelectedIndex(i + 1);
					}
				}
			}

			if (tempErmCodePublisherPerson != null) {
				tboxPersonname.setValue(tempErmCodePublisherPerson.getPersonName());
				tboxEmail.setValue(tempErmCodePublisherPerson.getMail());
				tboxTelephone.setValue(tempErmCodePublisherPerson.getTelephone());
				tboxFax.setValue(tempErmCodePublisherPerson.getFax());
				tboxTitle.setValue(tempErmCodePublisherPerson.getTitle());
			} else {
				tboxPublisherId.setSelectedIndex(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人加載編輯頁面報錯", e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String publisherId = tboxPublisherId.getSelectedItem().getValue();
			String personname = tboxPersonname.getValue();
			String email = tboxEmail.getValue();
			if (!StringUtils.isBlank(email)) {
				if (!ZkUtils.isEmail(email)) {
					ZkUtils.showExclamation(Labels.getLabel("emailError"), Labels.getLabel("warn"));
					tboxEmail.focus();
					return;
				}
			}
			String telephone = tboxTelephone.getValue();
			String fax = tboxFax.getValue();
			String title = tboxTitle.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			personname = XSSStringEncoder.encodeXSSString(personname);
			email = XSSStringEncoder.encodeXSSString(email);

			telephone = XSSStringEncoder.encodeXSSString(telephone);
			fax = XSSStringEncoder.encodeXSSString(fax);
			title = XSSStringEncoder.encodeXSSString(title);

			if (StringUtils.isBlank(publisherId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.publisherId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(personname)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.personname") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPersonname.focus();
				return;
			}
			boolean saveStatus = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService")).save(publisherId, personname,
					email, telephone, fax, title, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"ermCodePublisherperson_" + publisherId + personname);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "publisherperson/codePublisherperson.zul";
				ZkUtils.refurbishMethod(url);
				addErmCodePublisherpersonWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(Labels.getLabel("ermCodeItem.code") + Labels.getLabel("isExist"), Labels.getLabel("error"));
				clearInput();
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人新增報錯", e);
		}
	}

	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		tboxPersonname.setText(StringUtils.EMPTY);
	}

	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String publisherId = tboxPublisherId.getSelectedItem().getValue();
			String personname = tboxPersonname.getValue();
			String email = tboxEmail.getValue();
			if (!StringUtils.isBlank(email)) {
				if (!ZkUtils.isEmail(email)) {
					ZkUtils.showExclamation(Labels.getLabel("emailError"), Labels.getLabel("warn"));
					tboxEmail.focus();
					return;
				}
			}
			String telephone = tboxTelephone.getValue();
			String fax = tboxFax.getValue();
			String title = tboxTitle.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			personname = XSSStringEncoder.encodeXSSString(personname);
			email = XSSStringEncoder.encodeXSSString(email);
			telephone = XSSStringEncoder.encodeXSSString(telephone);
			fax = XSSStringEncoder.encodeXSSString(fax);
			title = XSSStringEncoder.encodeXSSString(title);

			if (StringUtils.isBlank(publisherId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.publisherId") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(personname)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.personname") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPersonname.focus();
				return;
			}
			boolean saveStatus = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService")).update(publisherId,
					personname, email, telephone, fax, title, 1, webEmployee);

			if (saveStatus) {

				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"ermCodePublisherperson_" + publisherId + personname);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				editErmCodePublisherpersonWin.detach();
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人修改報錯", e);
		}
	}

	public void editSearchOrgList() {
		Combobox publisherIdBox = (Combobox) editErmCodePublisherpersonWin.getParent().getFellowIfAny("publisherIdBox");
		Textbox personnameBox = (Textbox) editErmCodePublisherpersonWin.getParent().getFellowIfAny("personnameBox");
		Listbox publisherLix = (Listbox) editErmCodePublisherpersonWin.getParent().getFellowIfAny("publisherLix");
		try {
			String publisherId = publisherIdBox.getSelectedItem().getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			String personname = personnameBox.getValue();
			personname = XSSStringEncoder.encodeXSSString(personname);
			int selected = publisherIdBox.getSelectedIndex();
			List<ErmCodePublisherPerson> result = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService"))
					.findErmCodePublisherPerson(publisherId, personname, webEmployee);
			ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(result);
			tmpLML.setMultiple(true);
			publisherLix.setModel(tmpLML);
			publisherLix.setActivePage(currentPage);
			publisherIdBox.setSelectedIndex(selected);
			personnameBox.setValue(personname);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人集合出錯", e);
		}
	}

}
