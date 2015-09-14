package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
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
 * @author lwchen
 * 代理商出版商聯絡人
 * 2014/07/25
 *
 */
public class ErmCodePublisherpersonListComposer extends
		SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -984466786814168042L;
	@Wire
	private Combobox publisherIdBox;
	@Wire
	private Textbox personnameBox;
	@Wire
	private Listbox publisherLix;
	@Wire
	private Window addErmCodePublisherpersonWin;
	private WebEmployee webEmployee;
	@WireVariable
	private List<ErmCodePublisher> ermCodePublishers;
	@WireVariable
	private List<ErmCodePublisherPerson> ermCodePublisherPersons;
	@WireVariable
	private ErmCodePublisherPerson ermCodePublisherPerson;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			ermCodePublishers = ((CodePublisherService) SpringUtil
					.getBean("codePublisherService")).findAll(webEmployee);
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			publisherIdBox.appendChild(com);
			for (int i = 0; i < ermCodePublishers.size(); i++) {
				ErmCodePublisher ermCodePublisher = new ErmCodePublisher();
				ermCodePublisher = ermCodePublishers.get(i);
				com = new Comboitem();
				com.setLabel(ermCodePublisher.getName());
				com.setValue(ermCodePublisher.getPublisherId());
				publisherIdBox.appendChild(com);
			}
			publisherIdBox.setSelectedIndex(0);
			// 初始頁面加載
			ermCodePublisherPersons = ((ErmCodePublisherpersonService) SpringUtil
					.getBean("ermCodePublisherpersonService"))
					.findAll(webEmployee);
			ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(
					ermCodePublisherPersons);
			tmpLML.setMultiple(true);
			publisherLix.setModel(tmpLML);
		} catch (Exception e) {
			log.error("初始化generalCode異常" + e);
		}
	}

	@Listen("onClick = #addBtn")
	public void add() {
		try {
			addErmCodePublisherpersonWin = (Window) ZkUtils
					.createComponents(
							"/WEB-INF/pages/system/publisherperson/codePublisherpersonAdd.zul",
							null, null);
			addErmCodePublisherpersonWin.doModal();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人跳轉新增頁面報錯",e);
		}
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		try {
			publisherIdBox.setSelectedIndex(0);
			personnameBox.setValue("");
			List<ErmCodePublisherPerson> result = ((ErmCodePublisherpersonService) SpringUtil
					.getBean("ermCodePublisherpersonService")).findAll(webEmployee);
			ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(
					result);
			tmpLML.setMultiple(true);
			publisherLix.setModel(tmpLML);
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人顯示全部報錯",e);
		}
	}

	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			String publisherId = publisherIdBox.getSelectedItem().getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			String personname = personnameBox.getValue();
			personname = XSSStringEncoder.encodeXSSString(personname);

			if (StringUtils.isBlank(publisherId)&&StringUtils.isBlank(personname)) {
				ZkUtils.showExclamation(
						Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				publisherIdBox.focus();
				return;
			}
			
			List<ErmCodePublisherPerson> result = ((ErmCodePublisherpersonService) SpringUtil.getBean("ermCodePublisherpersonService"))
					.findErmCodePublisherPerson(publisherId, personname, webEmployee);
			ListModelList<ErmCodePublisherPerson> tmpLML = new ListModelList<ErmCodePublisherPerson>(
					result);
			tmpLML.setMultiple(true);
			publisherLix.setModel(tmpLML);
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人查詢報錯",e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = publisherLix.getSelectedCount();
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"),
						Labels.getLabel("info"), new EventListener() {
							public void onEvent(Event event) throws Exception {
								int clickedButton = (Integer) event.getData();
								if (clickedButton == Messagebox.OK) {
									Set<Listitem> selectedModels = publisherLix
											.getSelectedItems();
									for (Listitem tmpEST : selectedModels) {
										ermCodePublisherPerson = tmpEST.getValue();
										((ErmCodePublisherpersonService) SpringUtil
												.getBean("ermCodePublisherpersonService"))
												.update(ermCodePublisherPerson
														.getPublisherId(),
														ermCodePublisherPerson
																.getPersonName(),
														ermCodePublisherPerson
																.getMail(),
														ermCodePublisherPerson
																.getTelephone(),
														ermCodePublisherPerson
																.getFax(),
														ermCodePublisherPerson
																.getTitle(), 0,
														webEmployee);
										((WebSysLogService) SpringUtil
												.getBean("webSysLogService")).delLog(
												ZkUtils.getRemoteAddr(),
												webEmployee.getEmployeesn(),
												"ermCodeItem_"
														+ ermCodePublisherPerson.getPublisherId());
									}
									Desktop dkp = Executions.getCurrent()
											.getDesktop();
									Page page = dkp.getPageIfAny("templatePage");
									Include contentInclude = (Include) page
											.getFellowIfAny("contentInclude");
									contentInclude.setSrc("home.zul");
									contentInclude.setSrc("publisherperson/codePublisherperson.zul");
								} else {
									// 取消刪除
									return;
								}
							}
						});
			} else {
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("代理商出版商聯絡人刪除報錯",e);
		}
	}

}
