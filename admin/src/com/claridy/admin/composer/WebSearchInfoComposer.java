package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSearchInfo;
import com.claridy.facade.WebSearchInfoService;
import com.claridy.facade.WebSysLogService;

/**
 * zjgao nj 查詢說明管理 2014/07/17
 */
public class WebSearchInfoComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = -1800767415902433685L;
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected Textbox nameZhTwtbx;
	@Wire
	protected Radiogroup isOpenRdb;
	@Wire
	protected Intbox sortNumIbx;
	@Wire
	protected Intbox clickNumIbx;
	@Wire
	protected CKeditor contentZhTwEdt;
	@Wire
	protected WebSearchInfo webSearchInfo;
	@Wire
	protected Window addWebSearchInfoWin;
	@Wire
	protected Window editWebSearchInfoWin;
	@Wire
	private int currentPage;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			currentPage = Integer.parseInt(map.get("currentPage"));
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			String uuid = map.get("uuid");
			if (uuid != null) {
				webSearchInfo = ((WebSearchInfoService) SpringUtil.getBean("webSearchInfoService")).findedtAddList("uuid", uuid).get(0);
			}
			if (webSearchInfo != null && !"".equals(webSearchInfo)) {
				nameZhTwtbx.setValue(webSearchInfo.getNameZhTw());
				isOpenRdb.setSelectedIndex(webSearchInfo.getIsDisplay());
				sortNumIbx.setValue(webSearchInfo.getSortNum());
				clickNumIbx.setValue(webSearchInfo.getClickNum());
				contentZhTwEdt.setValue(webSearchInfo.getContentZhTw());
			} else {
				isOpenRdb.setSelectedIndex(1);
			}
		} catch (Exception e) {
			log.error("webSerachInfo初始化異常" + e);
		}

	}

	@Listen("onClick=#saveBtn")
	public void updateEmployee() throws Exception {
		try {
			webSearchInfo = new WebSearchInfo();
			webSearchInfo.setUuid(UUIDGenerator.getUUID());
			String tempNameZhTw = "";
			String tempIsOpen = "";
			Integer tempClickNum = 0;
			Integer tempSort = 0;
			String tempContentZhTw = "";
			Date date = new Date();

			webSearchInfo.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
				webSearchInfo.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				webSearchInfo.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			webSearchInfo.setCreateDate(date);

			if (nameZhTwtbx.getValue() != null && !"".equals(nameZhTwtbx.getValue().trim())) {
				tempNameZhTw = nameZhTwtbx.getValue().trim();
				tempNameZhTw = XSSStringEncoder.encodeXSSString(tempNameZhTw);
				webSearchInfo.setNameZhTw(tempNameZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("searchInfo.nameZhTw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				nameZhTwtbx.focus();
				return;
			}
			tempIsOpen = isOpenRdb.getSelectedItem().getValue().toString().trim();
			tempIsOpen = XSSStringEncoder.encodeXSSString(tempIsOpen);
			webSearchInfo.setIsDisplay(Integer.parseInt(tempIsOpen));

			tempSort = sortNumIbx.getValue();
			if (tempSort != null) {
				webSearchInfo.setSortNum(tempSort);
			}

			tempClickNum = clickNumIbx.getValue();
			if (tempClickNum != null) {
				webSearchInfo.setClickNum(tempClickNum);
			}

			if (contentZhTwEdt.getValue() != null && !"".equals(contentZhTwEdt.getValue().trim())) {
				tempContentZhTw = contentZhTwEdt.getValue().toString().trim();
				webSearchInfo.setContentZhTw(tempContentZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("searchInfo.contentZhTw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			webSearchInfo.setLatelyChangedUser(webEmployee.getEmployeesn());
			webSearchInfo.setLatelyChangedDate(new Date());
			((WebSearchInfoService) SpringUtil.getBean("webSearchInfoService")).addSearchInfo(webSearchInfo);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webSearchInfo.getWebEmployee()
					.getEmployeesn(), "websearchinfo_" + webSearchInfo.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			String url = "queryExplain/queryExplain.zul";
			ZkUtils.refurbishMethod(url);
			addWebSearchInfoWin.detach();
		} catch (Exception e) {
			log.error("webSearchInfo新增異常" + e);
		}
	}

	@Listen("onClick=#editBtn")
	public void updateSearchInfo() {
		try {
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			String tempNameZhTw = "";
			String tempIsOpen = "";
			Integer tempClickNum = 0;
			Integer tempSort = 0;
			String tempContentZhTw = "";

			if (nameZhTwtbx.getValue() != null && !"".equals(nameZhTwtbx.getValue().trim())) {
				tempNameZhTw = nameZhTwtbx.getValue().trim();
				tempNameZhTw = XSSStringEncoder.encodeXSSString(tempNameZhTw);
				webSearchInfo.setNameZhTw(tempNameZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("searchInfo.nameZhTw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				nameZhTwtbx.focus();
				return;
			}
			tempIsOpen = isOpenRdb.getSelectedItem().getValue().toString().trim();
			tempIsOpen = XSSStringEncoder.encodeXSSString(tempIsOpen);
			webSearchInfo.setIsDisplay(Integer.parseInt(tempIsOpen));

			tempSort = sortNumIbx.getValue();
			if (tempSort != null) {
				webSearchInfo.setSortNum(tempSort);
			}

			tempClickNum = clickNumIbx.getValue();
			if (tempClickNum != null) {
				webSearchInfo.setClickNum(tempClickNum);
			}

			if (contentZhTwEdt.getValue() != null && !"".equals(contentZhTwEdt.getValue().trim())) {
				tempContentZhTw = contentZhTwEdt.getValue().toString().trim();
				webSearchInfo.setContentZhTw(tempContentZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("searchInfo.contentZhTw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			webSearchInfo.setLatelyChangedUser(webEmployee.getEmployeesn());
			webSearchInfo.setLatelyChangedDate(new Date());
			((WebSearchInfoService) SpringUtil.getBean("webSearchInfoService")).updateSearchInfo(webSearchInfo);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
					"websearchinfo_" + webSearchInfo.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
			editSearchOrgList();
			editWebSearchInfoWin.detach();
		} catch (Exception e) {
			log.error("webSearchInfo修改異常" + e);
		}
	}

	public void editSearchOrgList() {
		Textbox titleNames = (Textbox) editWebSearchInfoWin.getParent().getFellowIfAny("titleName");
		Listbox webSearchInfolbx = (Listbox) editWebSearchInfoWin.getParent().getFellowIfAny("webSearchInfolbx");
		try {
			String titleName = titleNames.getValue().trim();
			titleName = XSSStringEncoder.encodeXSSString(titleName);
			List<WebSearchInfo> webSearchInfoList = ((WebSearchInfoService) SpringUtil.getBean("webSearchInfoService")).findSearchInfoByNameZhTw(
					titleName, webEmployee);
			ListModelList<WebSearchInfo> tmpLML = new ListModelList<WebSearchInfo>(webSearchInfoList);
			tmpLML.setMultiple(true);
			webSearchInfolbx.setModel(tmpLML);
			webSearchInfolbx.setActivePage(currentPage);
			titleNames.setValue(titleName);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("webSearchInfo集合出錯", e);
		}
	}
}
