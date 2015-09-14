package com.claridy.admin.composer;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.AccountApplyDetail;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUnitUseService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebSysLogService;

public class ErmResUnitUseUnitComposer extends SelectorComposer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private WebOrg webOrg;
	@Wire
	private Textbox unit;
	@Wire
	private Listbox unitLbx;
	@Wire
	private Window ermResUnitUseUnitWin;
	@Wire
	private Window ermResUnitUseWin;
	@WireVariable
	private List<Object> ermResUnitUseList;
	@WireVariable
	private List<WebOrg> webOrgList;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webOrgList=((WebOrgListService) SpringUtil.getBean("webOrgListService")).findWebOrgList();
			ListModelList<WebOrg> tmpLML = new ListModelList(webOrgList);
			tmpLML.setMultiple(true);
			unitLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("初始化異常"+e);
		}
	}

	@Listen("onClick = #searchBtn")
	public void search() throws InterruptedException {
		try {
			if (unit != null && unit.getValue() != "") {
				ListModelList<Object> tmpLML = new ListModelList();
				String tempUnit = unit.getValue();
				webOrgList = ((WebOrgListService) SpringUtil
						.getBean("webOrgListService")).findOrgList(tempUnit);
				if (webOrgList.size() > 0) {
					for (int i = 0; i < webOrgList.size(); i++) {
						tmpLML.add(webOrgList.get(i));
					}
					tmpLML.setMultiple(true);
					unitLbx.setModel(tmpLML);
				} else {
					tmpLML.setMultiple(true);
					unitLbx.setModel(tmpLML);
				}
			} else {
				webOrgList=((WebOrgListService) SpringUtil.getBean("webOrgListService")).findWebOrgList();
				ListModelList<WebOrg> tmpLML = new ListModelList(webOrgList);
				tmpLML.setMultiple(true);
				unitLbx.setModel(tmpLML);
			}
		} catch (WrongValueException e) {
			log.error("查詢異常"+e);
		}
	}

	@Listen("onClick = #clearBtn")
	public void clear() throws InterruptedException {
		try {
			unit.setValue(null);
			search();
		} catch (WrongValueException e) {
			log.error("清除異常"+e);
		}
	}

	@Listen("onClick = #confirmBtn")
	public void confirm() throws InterruptedException {
		try {
			int count = unitLbx.getSelectedCount();
			if (count > 0) {
				Set<Listitem> unitList = unitLbx.getSelectedItems();
//				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				ListModelList<String> WebOrgName=new ListModelList<String>();
				for(Listitem employee:unitList){
					webOrg=employee.getValue();
					WebOrgName.add(webOrg.getOrgName());
				}
				Listbox unitsLbx =(Listbox)ermResUnitUseUnitWin.getParent().getFellowIfAny("unitsLbx");
				unitsLbx.setVisible(true);
				unitsLbx.setModel(WebOrgName);
				ermResUnitUseUnitWin.detach();
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("select"),
						Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("確認異常" + e);
		}
	}
}
