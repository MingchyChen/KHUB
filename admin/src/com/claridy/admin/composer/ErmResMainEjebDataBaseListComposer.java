package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeDbService;

public class ErmResMainEjebDataBaseListComposer extends
		SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5415764791838248654L;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Wire
	private Textbox openCode;
	@Wire
	private Textbox openName;
	@Wire
	private Listbox ermResOpenLix;
	@Wire
	private Window ermResDataBaseWin;
	private WebEmployee webEmployee;
	private String openValue;
	private String openType;

	@Listen("onClick=#pagSearchBtn")
	public void search() {
		try {
			String code = openCode.getValue();
			String name = openName.getValue();
			code = XSSStringEncoder.encodeXSSString(code);
			name = XSSStringEncoder.encodeXSSString(name);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");

			List<ErmCodeDb> result = ((ErmCodeDbService) SpringUtil
					.getBean("ermCodeDbService")).findPublisherList(openValue,
					openType, name, code, webEmployee);
			ListModelList<ErmCodeDb> listModel = null;
			if (result.size() > 0) {
				listModel = new ListModelList<ErmCodeDb>(result);
				listModel.setMultiple(true);
				ermResOpenLix.setModel(listModel);
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢集合出錯", e);
		}
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		openCode.setValue("");
		openName.setValue("");
		search();
	}

	@Listen("onClick = #selectBtn")
	public void selectBtn() {
		int size = ermResOpenLix.getSelectedCount();
		if (size == 0) {
			// 請選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("selectOneData"),
					Labels.getLabel("info"));
			return;
		} else if (size > 1) {
			// 只能選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"),
					Labels.getLabel("info"));
			return;
		} else {
			Set<Listitem> selectedModels = ermResOpenLix.getSelectedItems();
			for (Listitem tmpEST : selectedModels) {
				//ErmCodePublisher tmpCodePublisher = tmpEST.getValue();
				ErmCodeDb tempCodeDb=tmpEST.getValue();
				if (openValue.trim().equals("publisher")) {
					Label publisherSearch = (Label) ermResDataBaseWin
							.getParent().getFellowIfAny("publisherSearch");
					Textbox pubTbx = (Textbox) ermResDataBaseWin.getParent()
							.getFellowIfAny("pubTbx");
//					publisherSearch.setValue(tmpCodePublisher.getPublisherId());
//					pubTbx.setValue(tmpCodePublisher.getName());
					publisherSearch.setValue(tempCodeDb.getDbId());
					pubTbx.setValue(tempCodeDb.getName());
				} else {
					Label agentedSearch = (Label) ermResDataBaseWin.getParent()
							.getFellowIfAny("agentedSearch");
					Textbox agenTbx = (Textbox) ermResDataBaseWin.getParent()
							.getFellowIfAny("agenTbx");
//					agentedSearch.setValue(tmpCodePublisher.getPublisherId());
//					agenTbx.setValue(tmpCodePublisher.getName());
					agentedSearch.setValue(tempCodeDb.getDbId());
					agenTbx.setValue(tempCodeDb.getName());
				}
				ermResDataBaseWin.detach();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap = ZkUtils.getExecutionArgs();
			openValue = tmpMap.get("openValue").toString();
			openType = tmpMap.get("openType").toString();
			if (openValue.trim().equals("publisher")) {
				ermResDataBaseWin.setTitle(Labels
						.getLabel("ermResMainDbws.publisherId"));
			} else if (openValue.trim().equals("language")) {
				ermResDataBaseWin.setTitle(Labels
						.getLabel("ermResMainDbws.agentedId"));
			} else {
				ermResDataBaseWin.setTitle(Labels.getLabel("ermResMainEjeb.resource"));//資料庫
			}
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			search();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查詢通知單範本管理集合出錯", e);
		}
	}
}
