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

import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAdwall;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebAdwallService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author sunchao 2015/07/28 首頁輪播AD管理列表頁
 */
public class WebAdwallListComposer extends SelectorComposer<Component> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 8994020132060546141L;
	@Wire
	private Textbox keywordBox;
	@Wire
	protected Listbox webAdwallLbx;
	@Wire
	protected Grid sysSettingGrid;
	@Wire
	private Window webAdwallWin;
	@WireVariable
	private List<WebAdwall> webAdwallList;
	@WireVariable
	private WebAdwall webAdwall;
	private WebEmployee webEmployee;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// 初始頁面加載
			webAdwallList = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).findAll(webEmployee);
			if (webAdwallList != null && webAdwallList.size() > 0) {
				for (WebAdwall webAdwall : webAdwallList) {
					// img控件图片路径
					String imgPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).agridlURL.trim() + "/" + SystemVariable.ADWALL_PATH
							+ "/" + webAdwall.getFilellink();
					webAdwall.setFilellink(imgPath);
				}
			}
			ListModelList<WebAdwall> tmpLML = new ListModelList<WebAdwall>(webAdwallList);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("加載首頁輪播AD管理報錯", e);
		}
	}

	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			String keyword = keywordBox.getValue();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			if (StringUtils.isBlank(keyword)) {
				ZkUtils.showExclamation(Labels.getLabel("inputString"), Labels.getLabel("warn"));
				keywordBox.focus();
				return;
			}
			List<WebAdwall> result = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).search(keyword, webEmployee);
			if (webAdwallList != null && webAdwallList.size() > 0) {
				for (WebAdwall webAdwall : webAdwallList) {
					// img控件图片路径
					String imgPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).agridlURL.trim() + "/" + SystemVariable.ADWALL_PATH
							+ "/" + webAdwall.getFilellink();
					webAdwall.setFilellink(imgPath);
				}
			}
			ListModelList<WebAdwall> tmpLML = new ListModelList<WebAdwall>(result);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("查詢首頁輪播AD管理報錯", e);
		}
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		try {
			List<WebAdwall> result = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).findAll(webEmployee);
			if (webAdwallList != null && webAdwallList.size() > 0) {
				for (WebAdwall webAdwall : webAdwallList) {
					// img控件图片路径
					String imgPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).agridlURL.trim() + "/" + SystemVariable.ADWALL_PATH
							+ "/" + webAdwall.getFilellink();
					webAdwall.setFilellink(imgPath);
				}
			}
			ListModelList<WebAdwall> tmpLML = new ListModelList<WebAdwall>(result);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
			keywordBox.setValue("");
		} catch (Exception e) {
			log.error("顯示全部首頁輪播AD管理報錯", e);
		}
	}

	@Listen("onClick = #addBtn")
	public void add() {
		webAdwallWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/webadwall/webadwallAdd.zul", null, null);
		webAdwallWin.doModal();
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = webAdwallLbx.getSelectedCount();
			if (size > 0) {
				// "您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) throws Exception {
						int clickedButton = (Integer) event.getData();
						if (clickedButton == Messagebox.OK) {
							Set<Listitem> selectedModels = webAdwallLbx.getSelectedItems();
							for (Listitem tmpEST : selectedModels) {
								webAdwall = tmpEST.getValue();
								((WebAdwallService) SpringUtil.getBean("webAdwallService")).delete(webAdwall.getUuid());
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),
										webEmployee.getEmployeesn(), "webadwall__" + webAdwall.getUuid());
							}
							ZkUtils.refurbishMethod("webadwall/webadwall.zul");
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
			log.error("刪除首頁輪播AD管理報錯", e);
		}
	}
}
