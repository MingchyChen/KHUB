package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.facade.WebNoticeTemplatesService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * sunchao nj 通知單範本管理
 * 
 */
public class WebNoticeTempComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox nameZhTw;
	@Wire
	private Radiogroup isOpen;
	@Wire
	private Textbox tboxSubject;
	@Wire
	private Textbox remarks;
	@Wire
	private CKeditor emailCon;
	@Wire
	private int currentPage;

	private WebEmployee webEmployee;
	private WebNoticeTemplates noticetemp;

	@Wire
	private Window editWebNoticeTempWin;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			noticetemp = ((WebNoticeTemplatesService) SpringUtil.getBean("webNoticeTemplatesService")).getWebNoticeTempByUuid(uuid);
			if (noticetemp != null) {
				nameZhTw.setValue(noticetemp.getNameZhTw());
				if (noticetemp.getIsopen() == 1) {
					isOpen.setSelectedIndex(0);
				} else {
					isOpen.setSelectedIndex(1);
				}
				tboxSubject.setValue(noticetemp.getMailsubjectZhTw());
				remarks.setValue(noticetemp.getRemarks());
				emailCon.setValue(noticetemp.getContentZhTw());
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("通知單範本管理進入新增或修改頁面報錯", e);
		}
	}

	@Listen("onClick=#updBtn")
	public void updateNoticeTemp() {
		try {
			String sNameZhTw = nameZhTw.getValue();
			int sIsOpen = Integer.parseInt(isOpen.getSelectedItem().getValue().toString());
			String subject = tboxSubject.getValue();
			String sRemarks = remarks.getValue();
			String sEmailCon = emailCon.getValue();
			sNameZhTw = XSSStringEncoder.encodeXSSString(sNameZhTw);
			subject = XSSStringEncoder.encodeXSSString(subject);
			sRemarks = XSSStringEncoder.encodeXSSString(sRemarks);
			if (StringUtils.isBlank(sNameZhTw)) {
				ZkUtils.showExclamation(Labels.getLabel("webNoticeTemplatesEdit.nameZhTw") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				nameZhTw.focus();
				return;
			}
			if (StringUtils.isBlank(subject)) {
				ZkUtils.showExclamation(Labels.getLabel("webNoticeTemplatesEdit.tboxSubject") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxSubject.focus();
				return;
			}
			if (StringUtils.isBlank(sEmailCon)) {
				ZkUtils.showExclamation(Labels.getLabel("webNoticeTemplatesEdit.emailCon") + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				return;
			}
			noticetemp.setNameZhTw(sNameZhTw);
			noticetemp.setIsopen(sIsOpen);
			noticetemp.setMailsubjectZhTw(subject);
			noticetemp.setRemarks(sRemarks);
			noticetemp.setContentZhTw(sEmailCon);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			noticetemp.setLatelyChangedUser(webEmployee.getEmployeesn());
			noticetemp.setLatelyChangedDate(new Date());
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(), "templates_"
					+ noticetemp.getUuid());
			((WebNoticeTemplatesService) SpringUtil.getBean("webNoticeTemplatesService")).SaveOrUpdNoticeTemp(noticetemp);
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
			editSearchOrgList();
			editWebNoticeTempWin.detach();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("通知單範本管理執行修改方法報錯", e);
		}
	}

	public void editSearchOrgList() {
		Textbox nameZhTws = (Textbox) editWebNoticeTempWin.getParent().getFellowIfAny("nameZhTw");
		Listbox ermSysPhoneticLix = (Listbox) editWebNoticeTempWin.getParent().getFellowIfAny("noticeTempLix");
		try {
			String nameZhTw = nameZhTws.getValue().trim();
			nameZhTw = XSSStringEncoder.encodeXSSString(nameZhTw);
			List<WebNoticeTemplates> webNoticeTemplist = ((WebNoticeTemplatesService) SpringUtil.getBean("webNoticeTemplatesService"))
					.findAllWebNoticeTemp(nameZhTw, webEmployee);
			ListModelList<WebNoticeTemplates> modelList = new ListModelList<WebNoticeTemplates>(webNoticeTemplist);
			modelList.setMultiple(true);
			ermSysPhoneticLix.setModel(modelList);
			ermSysPhoneticLix.setActivePage(currentPage);
			nameZhTws.setValue(nameZhTw);
		} catch (Exception e) {
			log.error("通知單範本管理集合出錯", e);
		}
	}
}
