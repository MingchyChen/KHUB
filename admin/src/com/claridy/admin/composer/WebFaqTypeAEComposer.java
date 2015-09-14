package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
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
import com.claridy.domain.WebFaqType;
import com.claridy.facade.WebFaqTypeService;
import com.claridy.facade.WebSysLogService;

public class WebFaqTypeAEComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6030440470655849087L;
	@Wire
	private Window webFaqTypeAEWin;

	@Wire
	private Button cancelBtn;
	@Wire
	private Textbox titleZhtbox;
	@Wire
	private Textbox titleUstbox;
	@Wire
	private Intbox sortNumibox;
	@Wire
	private Radiogroup isdisplayrdgroup;
	@Wire
	private Button updateBtn;
	@Wire
	private Button saveBtn;
	@Wire
	private int currentPage;

	private WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	private final Logger log = LoggerFactory.getLogger(getClass());

	private WebFaqType webFaqType;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			if (uuid != null && !"".equals(uuid)) {
				webFaqType = ((WebFaqTypeService) SpringUtil.getBean("webFaqTypeService")).findById(uuid);
				titleZhtbox.setValue(webFaqType.getTitleZhTw());
				titleUstbox.setValue(webFaqType.getTitleEnUs());
				sortNumibox.setValue(webFaqType.getSortnum());
				cancelBtn.setLabel(Labels.getLabel("updCancel"));
				updateBtn.setVisible(true);
				saveBtn.setVisible(false);
				webFaqTypeAEWin.setTitle(Labels.getLabel("edit"));
				if (webFaqType.getIsdisplay() != null && webFaqType.getIsdisplay() == 1) {
					isdisplayrdgroup.setSelectedIndex(0);
				} else if (webFaqType.getIsdisplay() != null && webFaqType.getIsdisplay() == 0) {
					isdisplayrdgroup.setSelectedIndex(1);
				}
			} else {
				cancelBtn.setLabel(Labels.getLabel("saveCancel"));
				updateBtn.setVisible(false);
				saveBtn.setVisible(true);
				webFaqTypeAEWin.setTitle(Labels.getLabel("add"));
			}
		} catch (Exception e) {
			log.error("faqtype新增編輯報錯", e);
		}

	}

	@Listen("onClick=#saveBtn")
	public void save() {
		try {
			String titleZh = XSSStringEncoder.encodeXSSString(titleZhtbox.getValue().trim());
			String titleUs = XSSStringEncoder.encodeXSSString(titleUstbox.getValue().trim());
			int sortNum = sortNumibox.getValue() != null ? sortNumibox.getValue() : -1;
			int display = -1;
			if (isdisplayrdgroup.getSelectedIndex() == 0) {
				display = 1;
			} else if (isdisplayrdgroup.getSelectedIndex() == 1) {
				display = 0;
			}
			if (titleZh == null || "".equals(titleZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitlezh"), Labels.getLabel("info"));
				titleZhtbox.focus();
				return;
			}
			if (titleUs == null || "".equals(titleUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitleus"), Labels.getLabel("info"));
				titleUstbox.focus();
				return;
			}
			WebFaqType webFaqType = new WebFaqType();
			webFaqType.setCreateDate(new Date());
			webFaqType.setDataOwnerGroup(loginwebEmployee.getDataOwnerGroup());
			webFaqType.setIsDataEffid(1);
			if (display != -1) {
				webFaqType.setIsdisplay(display);
			}
			if (sortNum != -1) {
				webFaqType.setSortnum(sortNum);
			}
			webFaqType.setTitleEnUs(titleUs);
			webFaqType.setTitleZhTw(titleZh);
			webFaqType.setUuid(UUIDGenerator.getUUID());
			webFaqType.setWebEmployee(loginwebEmployee);
			((WebFaqTypeService) SpringUtil.getBean("webFaqTypeService")).save(webFaqType);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(),
					"webfaqtype_" + webFaqType.getUuid());
			ZkUtils.refurbishMethod("webfaqtype/webfaqtype.zul");
			webFaqTypeAEWin.detach();
		} catch (WrongValueException e) {
			log.error("新增faqtype報錯",e);
		}

	}

	@Listen("onClick=#updateBtn")
	public void update() {
		try {
			String titleZh = XSSStringEncoder.encodeXSSString(titleZhtbox.getValue().trim());
			String titleUs = XSSStringEncoder.encodeXSSString(titleUstbox.getValue().trim());
			int sortNum = sortNumibox.getValue() != null ? sortNumibox.getValue() : -1;
			int display = 0;
			if (isdisplayrdgroup.getSelectedIndex() == 0) {
				display = 1;
			} else if (isdisplayrdgroup.getSelectedIndex() == 1) {
				display = 0;
			}
			if (titleZh == null || "".equals(titleZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitlezh"), Labels.getLabel("info"));
				titleZhtbox.focus();
				return;
			}
			if (titleUs == null || "".equals(titleUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitleus"), Labels.getLabel("info"));
				titleUstbox.focus();
				return;
			}
			webFaqType.setIsDataEffid(1);
			webFaqType.setIsdisplay(display);
			if (sortNum != -1) {
				webFaqType.setSortnum(sortNum);
			}
			webFaqType.setTitleEnUs(titleUs);
			webFaqType.setTitleZhTw(titleZh);
			webFaqType.setLatelyChangedDate(new Date());
			webFaqType.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
			((WebFaqTypeService) SpringUtil.getBean("webFaqTypeService")).update(webFaqType);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(),
					"webfaqtype_" + webFaqType.getUuid());
			editSearchOrgList();
			webFaqTypeAEWin.detach();
		} catch (WrongValueException e) {
			log.error("編輯faqtype報錯" , e);
		}
	}

	public void editSearchOrgList() {
		Textbox webfaqtypeLixs = (Textbox) webFaqTypeAEWin.getParent().getFellowIfAny("keywordtbox");
		Listbox webfaqtypeLix = (Listbox) webFaqTypeAEWin.getParent().getFellowIfAny("webfaqtypeLix");
		try {
			String keyWord = XSSStringEncoder.encodeXSSString(webfaqtypeLixs.getValue().trim());
			List<WebFaqType> webFaqTypeList = ((WebFaqTypeService) SpringUtil.getBean("webFaqTypeService")).findBy(keyWord, loginwebEmployee);
			ListModelList<WebFaqType> model = new ListModelList<WebFaqType>(webFaqTypeList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
			webfaqtypeLix.setActivePage(currentPage);
			webfaqtypeLixs.setValue(keyWord);
		} catch (Exception e) {
			log.error("FAQ類別管理集合出錯", e);
		}
	}
}
