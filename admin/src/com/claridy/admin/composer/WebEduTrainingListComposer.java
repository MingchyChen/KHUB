package com.claridy.admin.composer;

import java.util.Date;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebEduTrainingService;
import com.claridy.facade.WebSysLogService;

/**
 * zjgao nj 查詢說明管理 2014/08/06
 */
public class WebEduTrainingListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4177684310895821220L;

	@Wire
	private Datebox startDateDbox;
	@Wire
	private Datebox endDateDbox;
	@Wire
	private Listbox WebEduTrainLix;
	@Wire
	private Window webEduTrainingWin;
	@Wire
	private WebEmployee webEmployee;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebEduTraining> webEduTrainList = ((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).findAll(webEmployee);
			ListModelList<WebEduTraining> modelList = new ListModelList<WebEduTraining>(webEduTrainList);
			modelList.setMultiple(true);
			WebEduTrainLix.setModel(modelList);
		} catch (Exception e) {
			log.error("webEduTraining初始化異常" + e);
		}
	}

	@Listen("onClick=#pagSearchBtn")
	public void find() {
		try {
			Date startDate = startDateDbox.getValue();
			Date endDate = endDateDbox.getValue();
			if (startDate == null) {
				ZkUtils.showExclamation(Labels.getLabel("pleaseSelectStartDate"), Labels.getLabel("warn"));
				return;
			}
			List<WebEduTraining> webEduTrainList = ((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).findByDate(startDate,
					endDate, webEmployee);
			ListModelList<WebEduTraining> modelList = new ListModelList<WebEduTraining>(webEduTrainList);
			modelList.setMultiple(true);
			WebEduTrainLix.setModel(modelList);
		} catch (WrongValueException e) {
			log.error("webEduTraining查詢異常" + e);
		}
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		try {
			List<WebEduTraining> webEduTrainList = ((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).findAll(webEmployee);
			ListModelList<WebEduTraining> modelList = new ListModelList<WebEduTraining>(webEduTrainList);
			modelList.setMultiple(true);
			WebEduTrainLix.setModel(modelList);
			startDateDbox.setValue(null);
			endDateDbox.setValue(null);
		} catch (WrongValueException e) {
			log.error("webEduTraining查詢全部異常" + e);
		}
	}

	@Listen("onClick=#addBtn")
	public void add() {
		try {
			webEduTrainingWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/webEduTraining/webEduTrainingAdd.zul", null, null);
			webEduTrainingWin.doModal();

		} catch (WrongValueException e) {
			log.error("webEduTraining新增異常" + e);
		}
	}

	@Listen("onClick=#deleteBtn")
	public void delete() {
		int count = WebEduTrainLix.getSelectedCount();
		if (count > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener<Event>() {

				public void onEvent(Event event) throws Exception {
					int ckickButton = (Integer) event.getData();
					if (ckickButton == Messagebox.OK) {
						Set<Listitem> listitems = WebEduTrainLix.getSelectedItems();
						WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
						for (Listitem webEduTrain : listitems) {
							WebEduTraining webEdutraining = webEduTrain.getValue();
							webEdutraining.setIsDataEffid(0);
							((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).delete(webEdutraining);
							((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
									loginwebEmployee.getEmployeesn(), "WebEduTraining_" + webEdutraining.getUuid());
						}
						ZkUtils.refurbishMethod("webEduTraining/webEduTraining.zul");
					}

				}
			});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"), Labels.getLabel("info"));
			return;
		}
	}

}
