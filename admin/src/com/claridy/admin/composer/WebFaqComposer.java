package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaq;
import com.claridy.facade.WebFaqService;
import com.claridy.facade.WebSysLogService;

public class WebFaqComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6627151242432256861L;

	private WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	@Wire
	private Textbox keywordtbox;
	@Wire
	private Listbox webfaqtypeLix;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		try {
			List<WebFaq> webFaqList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findAll(loginwebEmployee);
			ListModelList<WebFaq> model = new ListModelList<WebFaq>(webFaqList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
		} catch (Exception e) {
			log.error("faq加載報錯",e);
		}

	}

	@Listen("onClick=#searchbtn")
	public void search() {
		try {
			String keyWord = XSSStringEncoder.encodeXSSString(keywordtbox.getValue().trim());
			if (keyWord != null && !"".equals(keyWord)) {
				List<WebFaq> webFaqList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findBy(keyWord, loginwebEmployee);
				ListModelList<WebFaq> model = new ListModelList<WebFaq>(webFaqList);
				model.setMultiple(true);
				webfaqtypeLix.setModel(model);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("inputString"), Labels.getLabel("info"));
			}
		} catch (WrongValueException e) {
			log.error("faq查詢報錯",e);
		}

	}

	@Listen("onClick=#showAll")
	public void showAll() {
		try {
			keywordtbox.setValue("");
			List<WebFaq> webFaqList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findAll(loginwebEmployee);
			ListModelList<WebFaq> model = new ListModelList<WebFaq>(webFaqList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
		} catch (Exception e) {
			log.error("faq顯示全部報錯",e);
		}
	}

	@Listen("onClick=#addbtn")
	public void addWebFaqType() {
		try {
			Window editWin = (Window) Executions.createComponents("/WEB-INF/pages/system/webfaq/webfaqAE.zul", null, null);
			editWin.doModal();
		} catch (Exception e) {
			log.error("faq跳轉報錯",e);
		}
	}

	@Listen("onClick=#deletebtn")
	public void deleteWebFaqType() {
		try {
			int updateChecked = webfaqtypeLix.getSelectedCount();
			if (updateChecked > 0) {
				// “你確定要刪除該資料嗎？”
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) {
						int clickButton = (Integer) event.getData();
						if (clickButton == Messagebox.OK) {
							Set<Listitem> listItem = webfaqtypeLix.getSelectedItems();
							WebFaq webFaq;
							for (Listitem webFaqTmp : listItem) {
								webFaq = webFaqTmp.getValue();
								((WebFaqService) SpringUtil.getBean("webFaqService")).delete(webFaq);
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										loginwebEmployee.getEmployeesn(), "webfaqtype_" + webFaq.getUuid());
							}
							List<WebFaq> webFaqList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findAll(loginwebEmployee);
							ListModelList<WebFaq> model = new ListModelList<WebFaq>(webFaqList);
							model.setMultiple(true);
							webfaqtypeLix.setModel(model);
						}
					}
				});
			} else {
				ZkUtils.showExclamation(Labels.getLabel("deleteData"), Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("刪除faq報錯",e);
		}
	}

}
