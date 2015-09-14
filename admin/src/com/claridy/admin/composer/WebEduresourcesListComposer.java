package com.claridy.admin.composer;

import java.util.List;
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
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEduresource;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebEduresourcesService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author sunchao
 * 2015/07/28
 * 線上教學資源列表頁
 */
public class WebEduresourcesListComposer extends SelectorComposer<Component> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 8994020132060546141L;
	@Wire
	private Textbox keywordBox;
	@Wire
	protected Listbox webAdwallLbx;
	@Wire
	private Window webEduresWin;
	@WireVariable
	private List<WebEduresource> webAdwallList;
	@WireVariable
	private WebEduresource webAdwall;
	private WebEmployee webEmployee;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// 初始頁面加載
			webAdwallList = ((WebEduresourcesService) SpringUtil
					.getBean("webEduresourcesService")).findAll(webEmployee);
			ListModelList<WebEduresource> tmpLML = new ListModelList<WebEduresource>(webAdwallList);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("加載線上教學資源報錯",e);
		}
	}
	
	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			String keyword = keywordBox.getValue().trim();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			if (StringUtils.isBlank(keyword)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				keywordBox.focus();
				return;
			}
			List<WebEduresource> result = ((WebEduresourcesService) SpringUtil
					.getBean("webEduresourcesService")).search(keyword, webEmployee);
			ListModelList<WebEduresource> tmpLML = new ListModelList<WebEduresource>(result);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("查詢線上教學資源報錯",e);
		}
	}
	@Listen("onClick=#showAllBtn")
	public void showAll() {
		try {
			List<WebEduresource> result = ((WebEduresourcesService) SpringUtil
					.getBean("webEduresourcesService")).findAll(webEmployee);
			ListModelList<WebEduresource> tmpLML = new ListModelList<WebEduresource>(
					result);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
			keywordBox.setValue("");
		} catch (Exception e) {
			log.error("顯示全部線上教學資源報錯",e);
		}
	}

	@Listen("onClick = #addBtn")
	public void add() {
		webEduresWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/webeduresource/webeduresourceAdd.zul", null,null);
		webEduresWin.doModal();
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = webAdwallLbx.getSelectedCount();
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"),
						Labels.getLabel("info"), new EventListener() {
							public void onEvent(Event event) throws Exception {
								int clickedButton = (Integer) event.getData();
								if (clickedButton == Messagebox.OK) {
									Set<Listitem> selectedModels = webAdwallLbx
											.getSelectedItems();
									for (Listitem tmpEST : selectedModels) {
										webAdwall = tmpEST.getValue();
										((WebEduresourcesService) SpringUtil
												.getBean("webEduresourcesService"))
												.delete(webAdwall.getUuid());
										((WebSysLogService) SpringUtil
												.getBean("webSysLogService")).delLog(
												ZkUtils.getRemoteAddr(),
												webEmployee.getEmployeesn(),
												"webeduresource_"+ webAdwall.getUuid());
									}
									ZkUtils.refurbishMethod("webeduresource/webeduresource.zul");
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
			log.error("刪除線上教學資源報錯",e);
		}
	}
}
