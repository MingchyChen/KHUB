package com.claridy.admin.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUserListService;
import com.claridy.facade.ErmSysIpConfigService;
import com.claridy.facade.ResourcesMainfileSolrSearch;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

public class ErmSysIpConfigListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8994020132060546141L;
	// systemSetting
	@Wire
	private Textbox keywordBox;
	@Wire
	private Combobox parentOrgName;
	@Wire
	private Combobox isOpenRdo;
	@Wire
	protected Listbox ermSysIpConfigLbx;
	@Wire
	private Window addSysIpConfigWin;
	@Wire
	private Window searchSysIpConfigWin;
	@Wire
	private Window webSysLogWin;
	@WireVariable
	private List<ErmSysIpconfig> ermSysIpConfigList;
	@WireVariable
	private ErmSysIpconfig ermSysIpConfig;
	@WireVariable
	private List<WebOrg> webOrgList;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private WebOrg webOrg;

	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		String keyword = keywordBox.getValue();
		String parentOrgNameValue = parentOrgName.getSelectedItem().getValue()
				.toString();
		String isOpenRdoValue = isOpenRdo.getSelectedItem().getValue()
				.toString();
		String tempisOpenRdo = null;
		if (StringUtils.isBlank(keyword)
				&& StringUtils.isBlank(parentOrgNameValue)
				&& StringUtils.isBlank(isOpenRdoValue)) {
			ZkUtils.showExclamation(Labels.getLabel("inputString"),
					Labels.getLabel("warn"));
			keywordBox.focus();
			return;
		}
		List<ErmSysIpconfig> resultall = ((ErmSysIpConfigService) SpringUtil
				.getBean("ermSysIpConfigService")).findermSysIpConfigAll(
				isOpenRdoValue, parentOrgNameValue, webEmployee);

		if (!StringUtils.isBlank(keyword)) {
			if (isboolIp(keyword) == false) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
				return;
			}
			if (resultall != null && !"".equals(resultall)) {
				for (int i = 0; i < resultall.size(); i++) {
					String sysIp = resultall.get(i).getSysip();
					String tempsysIp;
					if (sysIp.indexOf("*") != -1) {
						tempsysIp = replaceAsterisk(sysIp);
					} else {
						tempsysIp = sysIp;
					}
					long ipConfigMin = ipConfigToLongMin(tempsysIp);
					long ipConfigMax = ipConfigToLongMax(tempsysIp);
					if (keyword != "" && keyword != null) {
						String tempkeyword = replaceAsterisk(keyword);
						long keywordMin = ipConfigToLongMin(tempkeyword);
						long keywordMax = ipConfigToLongMax(tempkeyword);
						if (ipConfigMin <= keywordMin
								&& keywordMax <= ipConfigMax) {
							List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
									.getBean("ermSysIpConfigService"))
									.findedtAddList("sysip", sysIp);
							ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
									result);
							tmpLML.setMultiple(true);
							ermSysIpConfigLbx.setModel(tmpLML);
							break;
						} else {
							List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
									.getBean("ermSysIpConfigService"))
									.findedtAddList(null, null);
							ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>();
							tmpLML.setMultiple(true);
							ermSysIpConfigLbx.setModel(tmpLML);
						}
					} else {
						if (keyword != "" && keyword != null) {
							String tempkeyword = replaceAsterisk(keyword);
							long keywordMin = ipConfigToLongMin(tempkeyword);
							long keywordMax = ipConfigToLongMax(tempkeyword);
							if (ipConfigMin <= keywordMin
									&& keywordMax <= ipConfigMax) {
								List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
										.getBean("ermSysIpConfigService"))
										.findedtAddList("sysip", sysIp);
								if (result != null) {
									ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
											result);
									tmpLML.setMultiple(true);
									ermSysIpConfigLbx.setModel(tmpLML);
									break;
								}
							}
						} else {
							List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
									.getBean("ermSysIpConfigService"))
									.findedtAddList("sysip", null);
							ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>();
							tmpLML.setMultiple(true);
							ermSysIpConfigLbx.setModel(tmpLML);
						}
					}
				}

			} else {
				ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
						resultall);
				tmpLML.setMultiple(true);
				ermSysIpConfigLbx.setModel(tmpLML);
			}
		} else {
			ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
					resultall);
			tmpLML.setMultiple(true);
			ermSysIpConfigLbx.setModel(tmpLML);
		}
	}

	@Listen("onClick = #addSolr")
	public void addSolr() {
		((ResourcesMainfileSolrSearch) SpringUtil
				.getBean("resourcesMainfileSolrSearch")).addData();
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		keywordBox.setValue("");
		isOpenRdo.setSelectedIndex(0);
		parentOrgName.setSelectedIndex(0);
		List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
				.getBean("ermSysIpConfigService")).findermSysIpConfigAll("",
				"", webEmployee);
		ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
				result);
		tmpLML.setMultiple(true);
		ermSysIpConfigLbx.setModel(tmpLML);
	}

	@Listen("onClick = #addBtn")
	public void add() {
		addSysIpConfigWin = (Window) ZkUtils.createComponents(
				"/WEB-INF/pages/system/ermSysIpConfig/ermSysIpConfigAdd.zul",
				null, null);
		addSysIpConfigWin.doModal();
	}

	/*
	 * @Listen("onClick = #editBtn") public void edit() { int sumChecked =
	 * ermSysIpConfigLbx.getSelectedCount(); if (sumChecked == 1) {
	 * ermSystemSetting = ermSysIpConfigLbx.getSelectedItem().getValue(); String
	 * funcId= ermSystemSetting.getFunc_id(); String
	 * funcValue=ermSystemSetting.getFunc_value(); String
	 * funcName=ermSystemSetting.getFunc_name();
	 * 
	 * Map<String, String> params = new HashMap<String, String>();
	 * params.put("func_id",funcId); params.put("func_value", funcValue);
	 * params.put("func_name",funcName ); editSysSettingWin = (Window)
	 * ZkUtils.createComponents(
	 * "/WEB-INF/pages/system/sysSetting/systemSettingEdit.zul", null, params);
	 * editSysSettingWin.doModal(); } else if (sumChecked > 1) {
	 * //"只能選擇一筆資料進行編輯"
	 * ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"),
	 * Labels.getLabel("warn")); return; } else { //"請先選擇一筆資料進行編輯"
	 * ZkUtils.showExclamation(Labels.getLabel("selectOneData"),
	 * Labels.getLabel("info")); return; } }
	 */

	@Command
	public void viewLog() {
		ermSysIpConfig = ermSysIpConfigLbx.getSelectedItem().getValue();
		Map<String, String> params = new HashMap<String, String>();
		params.put("nlocate", "sysIpConfig_" + ermSysIpConfig.getUuid());
		webSysLogWin = (Window) ZkUtils.createComponents(
				"/WEB-INF/pages/common/webSysLog.zul", null, params);
		webSysLogWin.doModal();
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		int size = ermSysIpConfigLbx.getSelectedCount();
		if (size > 0) {
			// "您確定要刪除該筆資料嗎？"
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener() {
						public void onEvent(Event event) throws Exception {
							int clickedButton = (Integer) event.getData();
							if (clickedButton == Messagebox.OK) {
								Set<Listitem> selectedModels = ermSysIpConfigLbx
										.getSelectedItems();
								for (Listitem tmpEST : selectedModels) {
									ermSysIpConfig = tmpEST.getValue();
									((ErmSysIpConfigService) SpringUtil
											.getBean("ermSysIpConfigService"))
											.deleteermSysIpConfig(ermSysIpConfig
													.getUuid());
									((WebSysLogService) SpringUtil
											.getBean("webSysLogService"))
											.delLog(ZkUtils.getRemoteAddr(),
													webEmployee
															.getEmployeesn(),
													"ermsysIpConfig_"
															+ ermSysIpConfig
																	.getUuid());
								}
								Desktop dkp = Executions.getCurrent()
										.getDesktop();
								Page page = dkp.getPageIfAny("templatePage");
								Include contentInclude = (Include) page
										.getFellowIfAny("contentInclude");
								contentInclude.setSrc("home.zul");
								contentInclude
										.setSrc("ermSysIpConfig/ermSysIpConfig.zul");
								ZkUtils.showInformation(
										Labels.getLabel("updateOK"),
										Labels.getLabel("info"));
							} else {
								// 取消刪除
								return;
							}
						}
					});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
					Labels.getLabel("info"));
			return;
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		// 初始頁面加載
		ermSysIpConfigList = ((ErmSysIpConfigService) SpringUtil
				.getBean("ermSysIpConfigService")).findermSysIpConfigAll("",
				"", webEmployee);
		ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
				ermSysIpConfigList);
		tmpLML.setMultiple(true);
		ermSysIpConfigLbx.setModel(tmpLML);
		set();
	}

	public void set() {
		Comboitem aom = new Comboitem();
		aom.setLabel(Labels.getLabel("select"));
		aom.setValue("");
		isOpenRdo.appendChild(aom);
		Comboitem bom = new Comboitem();
		bom.setLabel(Labels.getLabel("firstTopLink.close"));
		bom.setValue("0");
		isOpenRdo.appendChild(bom);
		Comboitem com = new Comboitem();
		com.setLabel(Labels.getLabel("firstTopLink.open"));
		com.setValue("1");
		isOpenRdo.appendChild(com);
		isOpenRdo.setSelectedIndex(0);
		Comboitem co = new Comboitem();
		webOrgList = ((WebOrgListService) SpringUtil
				.getBean("webOrgListService")).findWebOrgList(null, null,
				webEmployee);
		List<WebOrg> webParOrgList = ((WebOrgListService) SpringUtil
				.getBean("webOrgListService")).findEdtAddWebOrgList(
				"orgIdParent", "0");
		Comboitem ao = new Comboitem();
		ao.setLabel(Labels.getLabel("select"));
		ao.setValue("");
		parentOrgName.appendChild(ao);
		for (int i = 0; i < webParOrgList.size(); i++) {
			WebOrg webParOrg = new WebOrg();
			webParOrg = webParOrgList.get(i);
			co = new Comboitem();
			co.setLabel(webParOrg.getOrgName());
			co.setValue(webParOrg.getOrgId());
			parentOrgName.appendChild(co);
		}
		parentOrgName.setSelectedIndex(0);
	}

	/**
	 * @return the ermSystemSetting
	 */
	public ErmSysIpconfig getErmSysIpConfig() {

		return ermSysIpConfig;
	}

	/**
	 * @param ermSystemSetting
	 *            the ermSystemSetting to set
	 */
	public void setErmSysIpConfig(ErmSysIpconfig ermSysIpConfig) {
		this.ermSysIpConfig = ermSysIpConfig;
	}

	public String replaceAsterisk(String ipAddress) {
		return ipAddress.replace("*", "1~255");
	}

	/* IP區間認證 */
	public boolean isboolIp(String ips) {
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ips);
		return matcher.matches();
	}

	public long ipConfigToLongMin(String ipAddress) {
		long ipMin[] = new long[4];
		int ipOne = ipAddress.indexOf(".");
		int ipTwo = ipAddress.indexOf(".", ipOne + 1);
		int ipThree = ipAddress.indexOf(".", ipTwo + 1);
		String ipyi = ipAddress.substring(0, ipOne);
		if (ipyi.indexOf("~") != -1) {
			int tilde = ipyi.indexOf("~");
			ipMin[0] = Long.parseLong(ipyi.substring(0, tilde));
		} else {
			ipMin[0] = Long.parseLong(ipyi);
		}
		String iper = ipAddress.substring(ipOne + 1, ipTwo);
		if (iper.indexOf("~") != -1) {
			int tilde = iper.indexOf("~");
			ipMin[1] = Long.parseLong(iper.substring(0, tilde));
		} else {
			ipMin[1] = Long.parseLong(iper);
		}
		String ipsan = ipAddress.substring(ipTwo + 1, ipThree);
		if (ipsan.indexOf("~") != -1) {
			int tilde = ipsan.indexOf("~");
			ipMin[2] = Long.parseLong(ipsan.substring(0, tilde));
		} else {
			ipMin[2] = Long.parseLong(ipsan);
		}
		String ipsi = ipAddress.substring(ipThree + 1);
		if (ipsi.indexOf("~") != -1) {
			int tilde = ipsi.indexOf("~");
			ipMin[3] = Long.parseLong(ipsi.substring(0, tilde));
		} else {
			ipMin[3] = Long.parseLong(ipsi);
		}
		long ipConfigMin = (ipMin[0] << 24) + (ipMin[1] << 16)
				+ (ipMin[2] << 8) + ipMin[3];
		return ipConfigMin;
	}

	public long ipConfigToLongMax(String ipAddress) {
		long ipMax[] = new long[4];
		int ipOne = ipAddress.indexOf(".");
		int ipTwo = ipAddress.indexOf(".", ipOne + 1);
		int ipThree = ipAddress.indexOf(".", ipTwo + 1);
		String ipyi = ipAddress.substring(0, ipOne);
		if (ipyi.indexOf("~") != -1) {
			int tilde = ipyi.indexOf("~");
			ipMax[0] = Long.parseLong(ipyi.substring(tilde + 1));
		} else {
			ipMax[0] = Long.parseLong(ipyi);
		}
		String iper = ipAddress.substring(ipOne + 1, ipTwo);
		if (iper.indexOf("~") != -1) {
			int tilde = iper.indexOf("~");
			ipMax[1] = Long.parseLong(iper.substring(tilde + 1));
		} else {
			ipMax[1] = Long.parseLong(iper);
		}
		String ipsan = ipAddress.substring(ipTwo + 1, ipThree);
		if (ipsan.indexOf("~") != -1) {
			int tilde = ipsan.indexOf("~");
			ipMax[2] = Long.parseLong(ipsan.substring(tilde + 1));
		} else {
			ipMax[2] = Long.parseLong(ipsan);
		}
		String ipsi = ipAddress.substring(ipThree + 1);
		if (ipsi.indexOf("~") != -1) {
			int tilde = ipsi.indexOf("~");
			ipMax[3] = Long.parseLong(ipsi.substring(tilde + 1));
		} else {
			ipMax[3] = Long.parseLong(ipsi);
		}
		long ipConfigMax = (ipMax[0] << 24) + (ipMax[1] << 16)
				+ (ipMax[2] << 8) + ipMax[3];
		return ipConfigMax;
	}
}
