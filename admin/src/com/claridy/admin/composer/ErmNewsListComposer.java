package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmNewsService;
import com.claridy.facade.WebSysLogService;

/**
 * @author zjgao 最新消息 2014/08/05
 */
public class ErmNewsListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -8893414000988594202L;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private List<ErmNews> newsList;
	@Wire
	private Listbox ermNewsLbx;
	// @Wire
	// private Textbox codingTxt;
	@Wire
	private Textbox titleTxt;
	@Wire
	private Datebox zxDateDbx;
	@Wire
	private ErmNews ermNews;
	@Wire
	private Window addErmNewsWin;
	@Wire
	private Radiogroup onDownRdb;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		try {
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			onDownRdb.setSelectedIndex(0);
			String onDown = onDownRdb.getSelectedItem().getValue();
			onDownRdb.getSelectedItem().getValue();
			newsList = ((ErmNewsService) SpringUtil.getBean("ermNewsService")).findErmNewsByParam("%" + "" + "%", onDown, webEmployee);
			ListModelList<ErmNews> tmpLML = new ListModelList<ErmNews>(newsList);
			tmpLML.setMultiple(true);
			ermNewsLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("ErmNewsList初始化異常" + e);
		}
	}

	@Listen("onClick = #showAll")
	public void searchAll() {
		try {
			newsList = ((ErmNewsService) SpringUtil.getBean("ermNewsService")).findAllErmNews(webEmployee);
			ListModelList<ErmNews> tmpLML = new ListModelList<ErmNews>(newsList);
			tmpLML.setMultiple(true);
			ermNewsLbx.setModel(tmpLML);
			// codingTxt.setValue("");
			titleTxt.setValue("");
			onDownRdb.setSelectedIndex(-1);
		} catch (WrongValueException e) {
			log.error("ErmNewsList查詢全部異常" + e);
		}
	}

	@Listen("onClick = #pagSearchBtn")
	public void pagSearchBtn() {
		try {
			String tempTitle = titleTxt.getValue();
			int tempIndex = onDownRdb.getSelectedIndex();
			String onDown = "";
			if (tempIndex == 0 || tempIndex == 1) {
				onDown = onDownRdb.getSelectedItem().getValue();
			} else {
				onDown = "";
			}
			if (StringUtils.isBlank(tempTitle)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"), Labels.getLabel("warn"));
				titleTxt.focus();
				return;
			}
			newsList = ((ErmNewsService) SpringUtil.getBean("ermNewsService")).findErmNewsByParam("%" + tempTitle + "%", onDown, webEmployee);
			ListModelList<ErmNews> tmpLML = new ListModelList<ErmNews>(newsList);
			tmpLML.setMultiple(true);
			ermNewsLbx.setModel(tmpLML);
		} catch (WrongValueException e) {
			log.error("ErmNewsList條件查詢異常" + e);
		}
	}

	@Listen("onClick = #deleteBtn")
	public void deleteErmNews() {
		int updateChecked = ermNewsLbx.getSelectedCount();
		if (updateChecked > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
				public void onEvent(Event event) {
					int clickButton = (Integer) event.getData();
					if (clickButton == Messagebox.OK) {
						Set<Listitem> listitem = ermNewsLbx.getSelectedItems();
						for (Listitem employee : listitem) {
							ermNews = employee.getValue();
							((ErmNewsService) SpringUtil.getBean("ermNewsService")).deleteErmNews(ermNews);
							((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(), ermNews.getUuid(), "webnews_"
									+ ermNews.getUuid());
						}
						String url = "ermNews/ermNews.zul";
						ZkUtils.refurbishMethod(url);
					}
				}
			});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"), Labels.getLabel("info"));
			return;
		}
	}

	@Listen("onClick = #addBtn")
	public void addErmNews() {
		try {
			addErmNewsWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/ermNews/ermNewsAdd.zul", null, null);
			addErmNewsWin.doModal();
		} catch (Exception e) {
			log.error("ErmNewList新增加載異常" + e);
		}
	}
}
