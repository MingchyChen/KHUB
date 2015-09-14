package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesScollegeLog;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeDbService;
import com.claridy.facade.ErmResMainEjebService;
import com.claridy.facade.ErmResourcesScollegeLogService;
import com.claridy.facade.WebSysLogService;

public class ErmRecommonComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5133650645384630141L;
	@Wire
	private Textbox resourceIdTxt;
	@Wire
	private Textbox orgNameTxt;
	@Wire
	private Label titleLbl;
	@Wire
	private Row dbRow;
	@Wire
	private Label dbNameLbl;
	private String resourcesId;
	private String dbId;
	private WebEmployee webEmployee;
	@Wire
	private Window addErmRecommonWin;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Map<String, String> map = new HashMap<String, String>();
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		map = ZkUtils.getExecutionArgs();
		String tempresourcesId = map.get("resourcesId");
		resourcesId = tempresourcesId;
		dbId=map.get("dbId");
		ErmResourcesMainfileV resourcesMainfile = ((ErmResMainEjebService) SpringUtil
				.getBean("ermResMainEjebService"))
				.getResMainfileByResId(resourcesId);
		ErmCodeDb ermCodeDb=((ErmCodeDbService) SpringUtil
				.getBean("ermCodeDbService")).findcodeDbByDbId(dbId, webEmployee);
		titleLbl.setValue(resourcesMainfile.getTitle());
		dbNameLbl.setValue(ermCodeDb.getName());
		if(resourcesId!=null&&!"".equals(resourcesId)){
			String resStr=resourcesId.substring(0,2);
			if(resStr.equals("DB")||resStr.equals("WS")){
				dbRow.setVisible(false);
			}
		}
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
						Labels.getLabel("ermResUserList.unit") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameTxt.focus();
				return;
			}
			int num=((ErmResourcesScollegeLogService) SpringUtil
					.getBean("ermResourcesScollegeLogService")).getScollegeLog(resourcesId, suunitId,dbId);
			if(num>0){
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.dataExists"),
						Labels.getLabel("warn"));
				orgNameTxt.focus();
				return;
			}
			ErmResourcesScollegeLog recommon = new ErmResourcesScollegeLog();
			recommon.setResourcesId(resourcesId);
			recommon.setSuitcollegeId(suunitId);
			if(!StringUtils.isBlank(dbId)){
				recommon.setDbId(dbId);
			}else{
				recommon.setDbId("0");
			}
			recommon.setIsDataEffid(1);
			recommon.setWebEmployee(webEmployee);
			recommon.setCreateDate(new Date());
			((ErmResourcesScollegeLogService) SpringUtil
					.getBean("ermResourcesScollegeLogService")).create(recommon);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeeName(), "ermRecommon_"+resourcesId+suunitId);
			addErmRecommonWin.detach();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
		} catch (WrongValueException e) {
			log.error("推薦資源單位新增異常" + e);
		}
	}
}
