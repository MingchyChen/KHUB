package com.claridy.admin.composer;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesSuunit;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResourcesSuunitIdService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

public class ErmCodeSunnitComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5133650645384630141L;
	@Wire
	private Textbox resourceIdTxt;
	@Wire
	private Textbox orgNameTxt;
	private String dbId;
	private WebEmployee webEmployee;
	@Wire
	private Window addErmResSuunitWin;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Map<String, String> map = new HashMap<String, String>();
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		map = ZkUtils.getExecutionArgs();
		String tempdbId = map.get("dbId");
		dbId = tempdbId;
	}

	@Listen("onClick=#openSuunitBtn")
	public void openWebOrg() {
		Executions.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResWebOrgOpen.zul",
				this.getSelf(), null);
	}

	@Listen("onClick=#saveBtn")
	public void saveSunnit() {
		try {
			String suunitId = resourceIdTxt.getValue();
			if(StringUtils.isBlank(suunitId)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermCodeDb.dw") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameTxt.focus();
				return;
			}
			ErmResourcesSuunit ermResourcesSuunit = new ErmResourcesSuunit();
			ermResourcesSuunit.setDbId(dbId);
			if (resourceIdTxt.getValue() != null
					&& !"".equals(resourceIdTxt.getValue())) {
				ermResourcesSuunit.setSuunitId(resourceIdTxt.getValue());
			}
			ermResourcesSuunit.setIsDataEffid(1);
			ermResourcesSuunit.setWebEmployee(webEmployee);
			ermResourcesSuunit.setCreateDate(new Date());//
			((ErmResourcesSuunitIdService) SpringUtil
					.getBean("ermResourcesSuunitIdService"))
					.addSuunit(ermResourcesSuunit);
			if(dbId!=null){
				String str=dbId.substring(0, 2);
				if(str.equals("DB")){
					try {
						((ResourcesMainDbwsSolrSearch) SpringUtil
								.getBean("resourcesMainDbwsSolrSearch"))
								.resources_main_dbws_editData(dbId);
					} catch (SQLException e) {
						log.error("solr更新電子資料庫報錯",e);
					}
				}
			}
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "ermResSuunit_"+dbId+suunitId);
			flushList();
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("dbId", dbId);
			addErmResSuunitWin.detach();
		} catch (WrongValueException e) {
			log.error("提供單位新增異常" + e);
		}
	}

	public void flushList() {
		Listbox sunnitLix = (Listbox) addErmResSuunitWin.getParent()
				.getFellowIfAny("resMainSuunitLix");
		List<WebOrg> orgList=((WebOrgListService) SpringUtil
				.getBean("webOrgListService"))
				.findWebOrgParam("", "");
		List<ErmResourcesSuunit> sunnitList = ((ErmResourcesSuunitIdService) SpringUtil
				.getBean("ermResourcesSuunitIdService"))
				.findBySuunitResId(dbId);//findWebOrgParam
		for (ErmResourcesSuunit ermResourcesSuunit : sunnitList) {
			for (int i = 0; i < orgList.size(); i++) {
				if(ermResourcesSuunit.getSuunitId().equals(orgList.get(i).getOrgId())){
					ermResourcesSuunit.setOrgName(orgList.get(i).getOrgName());
				}
			}
		}
		ListModelList<ErmResourcesSuunit> ermSunnitModel=new ListModelList<ErmResourcesSuunit>(sunnitList);
		ermSunnitModel.setMultiple(true);
		sunnitLix.setModel(ermSunnitModel);
	}
}
