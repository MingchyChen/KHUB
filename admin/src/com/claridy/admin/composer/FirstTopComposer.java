package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.claridy.domain.WebRellink;
import com.claridy.facade.WebRelLinkService;
import com.claridy.facade.WebSysLogService;

/**
 * zjgao nj 首頁上方鏈接管理 2014/07/11
 */
public class FirstTopComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 2677175271365519885L;
	@Wire
	protected WebRellink webRellink;
	@Wire
	protected Textbox tboxNameZhTwtxt;
	@Wire
	protected Radiogroup isOpenRdo;
	@Wire
	protected Radiogroup linkTypeRdo;
	@Wire
	private Radiogroup isEzproxyRdo;
	@Wire
	protected Intbox sortIxt;
	@Wire
	protected Textbox linkUrltxt;
	@Wire
	protected Intbox clickNumIxt;
	@Wire
	private Window firstTopLinkAddWin;
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected Window firstTopLinkEditWin;
	@Wire
	private int currentPage;
	@Wire
	protected Radiogroup menuTypeRdo;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			// TODO Auto-generated method stub
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			if (uuid != null) {
				webRellink = ((WebRelLinkService) SpringUtil.getBean("webRelLinkService")).findedtAddList("uuid", uuid).get(0);
			}
			if (webRellink != null && !"".equals(webRellink)) {
				tboxNameZhTwtxt.setValue(webRellink.getNameZhTw());
				isOpenRdo.setSelectedIndex(webRellink.getIsDisplay());
				linkTypeRdo.setSelectedIndex(webRellink.getIsBlank());
				menuTypeRdo.setSelectedIndex(webRellink.getMenuType() - 1);
				sortIxt.setValue(webRellink.getSortNum());
				linkUrltxt.setValue(webRellink.getStrurl());
				clickNumIxt.setValue(webRellink.getClickNum());
				isEzproxyRdo.setSelectedIndex(webRellink.getIsezproxy());
			} else {
				isOpenRdo.setSelectedIndex(1);
				linkTypeRdo.setSelectedIndex(1);
				menuTypeRdo.setSelectedIndex(1);
			}
		} catch (Exception e) {
			log.error("FirstTop初始化異常" + e);
		}
	}

	@Listen("onClick=#saveBtn")
	public void updateEmployee() throws Exception {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webRellink = new WebRellink();
			webRellink.setUuid(UUIDGenerator.getUUID());
			String tempNameZhTw = "";
			String tempIsOpen;
			String tempLinkType;
			Integer tempSort = 0;
			String tempStrUrl;
			Integer tempClickNum = 0;
			String tempMenuType;
			Date date = new Date();

			webRellink.setWebEmployee(webEmployee);
			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
				webRellink.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				webRellink.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			webRellink.setCreatedate(date);

			if (tboxNameZhTwtxt.getValue() != null && !"".equals(tboxNameZhTwtxt.getValue().trim())) {
				tempNameZhTw = tboxNameZhTwtxt.getValue().trim();
				tempNameZhTw = XSSStringEncoder.encodeXSSString(tempNameZhTw);
				webRellink.setNameZhTw(tempNameZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("firstTopLink.ljmc") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxNameZhTwtxt.focus();
				return;
			}
			tempIsOpen = isOpenRdo.getSelectedItem().getValue().toString().trim();
			tempIsOpen = XSSStringEncoder.encodeXSSString(tempIsOpen);
			webRellink.setIsDisplay(Integer.parseInt(tempIsOpen));

			tempLinkType = linkTypeRdo.getSelectedItem().getValue().toString().trim();
			tempLinkType = XSSStringEncoder.encodeXSSString(tempLinkType);
			webRellink.setIsBlank(Integer.parseInt(tempLinkType));

			tempMenuType = menuTypeRdo.getSelectedItem().getValue().toString().trim();
			tempMenuType = XSSStringEncoder.encodeXSSString(tempMenuType);
			webRellink.setMenuType(Integer.parseInt(tempMenuType));

			webRellink.setIsezproxy(Integer.parseInt(isEzproxyRdo.getSelectedItem().getValue().toString()));
			tempSort = sortIxt.getValue();
			if (tempSort != null) {
				webRellink.setSortNum(tempSort);
			}

			if (linkUrltxt.getValue() != null && !"".equals(linkUrltxt.getValue())) {
				tempStrUrl = linkUrltxt.getValue().trim();
				tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
				webRellink.setStrurl(tempStrUrl);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("firstTopLink.ljwz") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxNameZhTwtxt.focus();
				return;
			}

			tempClickNum = clickNumIxt.getValue();
			if (tempClickNum != null) {
				webRellink.setClickNum(tempClickNum);
			}

			webRellink.setLatelyChangedUser(webEmployee.getEmployeesn());
			webRellink.setLatelyChangedDate(new Date());
			((WebRelLinkService) SpringUtil.getBean("webRelLinkService")).addWebRelLink(webRellink);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webRellink.getWebEmployee()
					.getEmployeesn(), "webrellink_" + webRellink.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
			String url = "topLink/firstTopLink.zul";
			ZkUtils.refurbishMethod(url);
			firstTopLinkAddWin.detach();
		} catch (Exception e) {
			log.error("FirstTop新增異常" + e);
		}
	}

	@Listen("onClick=#editBtn")
	public void editEmployee() throws Exception {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// WebRellink wr=new WebRellink();
			// webRellink=new WebRellink();
			// webRellink.setUuid(UUIDGenerator.getUUID());
			String tempNameZhTw = "";
			String tempIsOpen = "";
			String tempLinkType = "";
			Integer tempSort = 0;
			String tempStrUrl = "";
			Integer tempClickNum = 0;
			String tempMenuType = "";
			if (tboxNameZhTwtxt.getValue() != null && !"".equals(tboxNameZhTwtxt.getValue().trim())) {
				tempNameZhTw = tboxNameZhTwtxt.getValue().trim();
				tempNameZhTw = XSSStringEncoder.encodeXSSString(tempNameZhTw);
				webRellink.setNameZhTw(tempNameZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("firstTopLink.ljmc") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxNameZhTwtxt.focus();
				return;
			}
			tempIsOpen = isOpenRdo.getSelectedItem().getValue().toString().trim();
			tempIsOpen = XSSStringEncoder.encodeXSSString(tempIsOpen);
			webRellink.setIsDisplay(Integer.parseInt(tempIsOpen));

			tempLinkType = linkTypeRdo.getSelectedItem().getValue().toString().trim();
			tempLinkType = XSSStringEncoder.encodeXSSString(tempLinkType);
			webRellink.setIsBlank(Integer.parseInt(tempLinkType));

			tempMenuType = menuTypeRdo.getSelectedItem().getValue().toString().trim();
			tempMenuType = XSSStringEncoder.encodeXSSString(tempMenuType);
			webRellink.setMenuType(Integer.parseInt(tempMenuType));

			if (isEzproxyRdo.getSelectedItem() != null) {
				webRellink.setIsezproxy(Integer.parseInt(isEzproxyRdo.getSelectedItem().getValue().toString()));
			}

			tempSort = sortIxt.getValue();
			if (tempSort != null) {
				webRellink.setSortNum(tempSort);
			}

			if (linkUrltxt.getValue() != null && !"".equals(linkUrltxt.getValue().trim())) {
				tempStrUrl = linkUrltxt.getValue().trim();
				tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
				webRellink.setStrurl(tempStrUrl);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("firstTopLink.ljwz") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxNameZhTwtxt.focus();
				return;
			}
			tempClickNum = clickNumIxt.getValue();
			if (tempClickNum != null) {
				webRellink.setClickNum(tempClickNum);
			}

			webRellink.setLatelyChangedUser(webEmployee.getEmployeesn());
			webRellink.setLatelyChangedDate(new Date());
			((WebRelLinkService) SpringUtil.getBean("webRelLinkService")).updateWebRelLink(webRellink);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webRellink.getWebEmployee().getEmployeesn(),
					"webrellink_" + webRellink.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
			editSearchOrgList();
			firstTopLinkEditWin.detach();
		} catch (Exception e) {
			log.error("FirstTop修改異常" + e);
		}
	}

	public void editSearchOrgList() {
		Textbox linkName = (Textbox) firstTopLinkEditWin.getParent().getFellowIfAny("linkName");
		Listbox ermSysPhoneticLix = (Listbox) firstTopLinkEditWin.getParent().getFellowIfAny("webRelLinklbx");
		try {
			String linkNames = linkName.getValue().trim();
			linkNames = XSSStringEncoder.encodeXSSString(linkNames);
			List<WebRellink> webRelLinkList = ((WebRelLinkService) SpringUtil.getBean("webRelLinkService")).findwenRelLinkBynameZhTw(linkNames,
					webEmployee);
			ListModelList<WebRellink> tmpLML = new ListModelList<WebRellink>(webRelLinkList);
			tmpLML.setMultiple(true);
			ermSysPhoneticLix.setModel(tmpLML);
			ermSysPhoneticLix.setActivePage(currentPage);
			linkName.setValue(linkNames);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("FirstTop集合出錯", e);
		}
	}
}
