package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeDbService;
import com.claridy.facade.ResourcesMainfileSolrSearch;
import com.claridy.facade.WebSysLogService;

public class ErmCodeDbListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5922776660449184829L;
	@Wire
	private Listbox resCodeDbLix;
	@Wire
	private Textbox nameTxt;
	@Wire
	private Textbox dbIdTxt;
	@Wire
	private Window ermCodeDbListWin;
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		try {
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			List<ErmCodeDb> codeDbList = ((ErmCodeDbService) SpringUtil
					.getBean("ermCodeDbService")).findAllCodeDbIsUse();
			ListModelList<ErmCodeDb> model = new ListModelList<ErmCodeDb>(
					codeDbList);
			model.setMultiple(true);
			resCodeDbLix.setModel(model);
		} catch (Exception e) {
			log.error("初始化異常", e);
		}
	}

	@Listen("onClick=#pagSearchBtn")
	public void searchByprama() {
		try {
			String name = nameTxt.getValue();
			String dbId = dbIdTxt.getValue();
			List<ErmCodeDb> codeDbList = ((ErmCodeDbService) SpringUtil
					.getBean("ermCodeDbService")).findPublisherList("", "",
					name, dbId, webEmployee);
			ListModelList<ErmCodeDb> model = new ListModelList<ErmCodeDb>(
					codeDbList);
			model.setMultiple(true);
			resCodeDbLix.setModel(model);
		} catch (WrongValueException e) {
			log.error("資料庫查詢異常", e);
		}
	}
	@Listen("onClick=#addBtn")
	public void toAddPage(){
		try {
			ermCodeDbListWin = (Window) ZkUtils
					.createComponents(
							"/WEB-INF/pages/system/ermCodeDb/ermCodeDbAdd.zul",
							null, null);
			ermCodeDbListWin.doModal();
		} catch (WrongValueException e) {
			log.error("webEduTraining新增異常" + e);
		}
	}
	@Listen("onClick=#clearBtn")
	public void clearBtn(){
		nameTxt.setValue("");
		dbIdTxt.setValue("");
	}
	@Listen("onClick=#delSuunitBtn")
	public void delSunnit() {
		int count = resCodeDbLix.getSelectedCount();
		if (count > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener<Event>() {

						public void onEvent(Event event) throws Exception {
							int ckickButton = (Integer) event.getData();
							if (ckickButton == Messagebox.OK) {
								Set<Listitem> listitems = resCodeDbLix
										.getSelectedItems();
								for (Listitem listitem : listitems) {
									ErmCodeDb ermCodeDb = listitem
											.getValue();
									((ErmCodeDbService) SpringUtil.getBean("ermCodeDbService"))
									.delCodeDb(ermCodeDb);
									((ResourcesMainfileSolrSearch) SpringUtil.getBean("resourcesMainfileSolrSearch"))
									.addEditEjebItemData(ermCodeDb.getDbId());
									((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(), 
											webEmployee.getEmployeesn(), "codeDb_"+ermCodeDb.getDbId());
								}
								String url = "ermCodeDb/ermCodeDb.zul";
								ZkUtils.refurbishMethod(url);
							}

						}
					});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
					Labels.getLabel("info"));
			return;
		}

	}
}
