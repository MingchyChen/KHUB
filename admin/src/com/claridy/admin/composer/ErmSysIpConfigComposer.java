package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysIpconfig;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.SystemSettingService;
import com.claridy.facade.ErmSysIpConfigService;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

public class ErmSysIpConfigComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8591720837169072811L;
	// SystemSetting
	@Wire
	private Textbox ipConfig;
	@Wire
	private Radiogroup isOpenRdo;
	@Wire
	private Combobox parentOrgName;
	@Wire
	private Window editSysIpConfigWin;
	@Wire
	private Window addSysIpConfigWin;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private WebOrg webOrg;
	@Wire
	private ErmSysIpconfig ermSysIpConfig;
	@WireVariable
	private List<WebOrg> webOrgList;
	@WireVariable
	private final Logger log = LoggerFactory.getLogger(getClass());

	private int currage;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊

			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			webOrgList = ((WebOrgListService) SpringUtil
					.getBean("webOrgListService")).findWebOrgList(null, null,
					webEmployee);
			List<WebOrg> webParOrgList = ((WebOrgListService) SpringUtil
					.getBean("webOrgListService")).findEdtAddWebOrgList(
					"orgIdParent", "0");
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			parentOrgName.appendChild(com);
			for (int i = 0; i < webParOrgList.size(); i++) {
				WebOrg webParOrg = new WebOrg();
				webParOrg = webParOrgList.get(i);
				com = new Comboitem();
				com.setLabel(webParOrg.getOrgName());
				com.setValue(webParOrg.getOrgId());
				parentOrgName.appendChild(com);
			}
			ListModelList<WebOrg> tmpLML = new ListModelList<WebOrg>(webOrgList);
			tmpLML.setMultiple(true);
			parentOrgName.setSelectedIndex(0);
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			String currpage=map.get("currpage");
			if (currpage!= null && !currpage.equals("")) {
				currage = Integer.parseInt(map.get("currpage"));
			}
			if (uuid != null) {
				ermSysIpConfig = ((ErmSysIpConfigService) SpringUtil
						.getBean("ermSysIpConfigService")).findedtAddList(
						"uuid", uuid).get(0);
			}
			if (ermSysIpConfig != null && !"".equals(ermSysIpConfig)) {
				ipConfig.setValue(ermSysIpConfig.getSysip());
				isOpenRdo.setSelectedIndex(ermSysIpConfig.getIsopen());
				String tempOrgId = ermSysIpConfig.getOrgId();
				webOrg = ((WebOrgListService) SpringUtil
						.getBean("webOrgListService")).findEdtAddWebOrgList(
						"orgId", tempOrgId).get(0);
				parentOrgName.setValue(webOrg.getOrgName());
			} else {
				isOpenRdo.setSelectedIndex(1);
			}
		} catch (Exception e) {
			log.error("初始化異常" + e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			ermSysIpConfig = new ErmSysIpconfig();
			ermSysIpConfig.setUuid(UUIDGenerator.getUUID());
			String tempIsOpen = XSSStringEncoder.encodeXSSString(isOpenRdo
					.getSelectedItem().getValue().toString().trim());
			String tempIpConfig = ipConfig.getValue();
			if (StringUtils.isBlank(parentOrgName.getSelectedItem().getValue().toString())) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermResUserList.unit")+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				parentOrgName.focus();
				return;
			} 
			if (StringUtils.isBlank(tempIpConfig)) {
				// 功能不能為空
				ZkUtils.showExclamation("IP "+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				ipConfig.focus();
				return;
			} 
			/*if (isboolIp(tempIpConfig) == false) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
				return;
			}*/
			String tempParentOrgName = parentOrgName.getSelectedItem().getValue();
			String reIpConfig;
			Date date = new Date();
			tempIpConfig = XSSStringEncoder.encodeXSSString(tempIpConfig);
			
			if (tempIpConfig.indexOf("*") != -1) {
				reIpConfig = replaceAsterisk(tempIpConfig);
			} else {
				reIpConfig = tempIpConfig;
			}
			long ipConfigMin = ipConfigToLongMin(reIpConfig);
			long ipConfigMax = ipConfigToLongMax(reIpConfig);
			if (ipConfigMin > ipConfigMax) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
			}
			if (isboolIp(ipLongToIp(ipConfigMin)) == false
					&& isboolIp(ipLongToIp(ipConfigMax)) == false) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
			} else {
				ermSysIpConfig.setWebEmployee(webEmployee);
				if (webEmployee.getWeborg() != null
						&& webEmployee.getWeborg().getOrgId() != null
						&& !"0".equals(webEmployee.getWeborg().getOrgId())) {
					ermSysIpConfig.setDataOwnerGroup(webEmployee.getWeborg()
							.getOrgId());
				} else {
					ermSysIpConfig.setDataOwnerGroup(webEmployee
							.getParentWebOrg().getOrgId());
				}
				try {
					ermSysIpConfig.setOrgId(tempParentOrgName);
					ermSysIpConfig.setCreateDate(date);
					ermSysIpConfig.setSysip(tempIpConfig);
					ermSysIpConfig.setIsopen(Integer.parseInt(tempIsOpen));
					ermSysIpConfig.setLatelyChangedUser(webEmployee
							.getEmployeesn());
					ermSysIpConfig.setLatelyChangedDate(new Date());
					((ErmSysIpConfigService) SpringUtil
							.getBean("ermSysIpConfigService"))
							.updateermSysIpConfig(ermSysIpConfig);
					((WebSysLogService) SpringUtil.getBean("webSysLogService"))
							.insertLog(
									ZkUtils.getRemoteAddr(),
									webEmployee.getEmployeesn(),
									"ermsysIpConfig_"
											+ ermSysIpConfig.getUuid());
					// 更新成功
					ZkUtils.showInformation(Labels.getLabel("saveOK"),
							Labels.getLabel("info"));
					String url = "ermSysIpConfig/ermSysIpConfig.zul";
					ZkUtils.refurbishMethod(url);
					addSysIpConfigWin.detach();
				} catch (Exception e) {
					log.error("保存失敗",e);
				}

			}

		} catch (WrongValueException e) {
			log.error("保存異常" + e);
		}
	}

	@Listen("onClick = #updateBtn")
	public void update() {

		try {
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			String tempIsOpen = XSSStringEncoder.encodeXSSString(isOpenRdo
					.getSelectedItem().getValue().toString().trim());
			String tempIpConfig = ipConfig.getValue();

			if (StringUtils.isBlank(parentOrgName.getSelectedItem().getValue().toString())) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermResUserList.unit")+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				parentOrgName.focus();
				return;
			} 
			if (StringUtils.isBlank(tempIpConfig)) {
				// 功能不能為空
				ZkUtils.showExclamation("IP "+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				ipConfig.focus();
				return;
			} 
			/*if (isboolIp(tempIpConfig) == false) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
				return;
			}*/
			
			String tempParentOrgName = parentOrgName.getSelectedItem().getValue();
			String reIpConfig;
			tempIpConfig = XSSStringEncoder.encodeXSSString(tempIpConfig);
			if (tempIpConfig.indexOf("*") != -1) {
				reIpConfig = replaceAsterisk(tempIpConfig);
			} else {
				reIpConfig = tempIpConfig;
			}
			long ipConfigMin = ipConfigToLongMin(reIpConfig);
			long ipConfigMax = ipConfigToLongMax(reIpConfig);
			if (ipConfigMin > ipConfigMax) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
				return;
			}
			if (isboolIp(ipLongToIp(ipConfigMin)) == false
					&& isboolIp(ipLongToIp(ipConfigMax)) == false) {
				ZkUtils.showInformation(Labels.getLabel("sysMenuIpConfigMenu")
						+ "不符合規範", Labels.getLabel("info"));
				return;
			} else {
				try {
					ermSysIpConfig.setSysip(tempIpConfig);
					ermSysIpConfig.setIsopen(Integer.parseInt(tempIsOpen));
					ermSysIpConfig.setOrgId(tempParentOrgName);
					ermSysIpConfig.setLatelyChangedUser(webEmployee
							.getEmployeesn());
					ermSysIpConfig.setLatelyChangedDate(new Date());
					((ErmSysIpConfigService) SpringUtil
							.getBean("ermSysIpConfigService"))
							.updateermSysIpConfig(ermSysIpConfig);
					((WebSysLogService) SpringUtil.getBean("webSysLogService"))
							.editLog(
									ZkUtils.getRemoteAddr(),
									webEmployee.getEmployeesn(),
									"ermsysIpConfig_"
											+ ermSysIpConfig.getUuid());
					// 更新成功
					ZkUtils.showInformation(Labels.getLabel("updateOK"),
							Labels.getLabel("info"));
					/*
					 * String url = "ermSysIpConfig/ermSysIpConfig.zul";
					 * ZkUtils.refurbishMethod(url);
					 */
					editSearchOrgList();
					editSysIpConfigWin.detach();
				} catch (Exception e) {
					log.error("保存異常" + e);
				}
			}
		} catch (WrongValueException e) {
			log.error("保存異常" + e);
		}
	}

	public String replaceAsterisk(String ipAddress) {
		return ipAddress.replace("*", "1~255");
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

	public String ipLongToIp(long ipaddress) {
		StringBuffer ip = new StringBuffer("");
		ip.append(String.valueOf((ipaddress >>> 24)));
		ip.append(".");
		ip.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
		ip.append(".");
		ip.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
		ip.append(".");
		ip.append(String.valueOf((ipaddress & 0x000000FF)));
		return ip.toString();
	}

	/* IP區間認證 */
	public boolean isboolIp(String ips) {
		String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ips);
		return matcher.matches();
	}

	public void editSearchOrgList() {
		Listbox ermSysIpConfigLbx = (Listbox) editSysIpConfigWin.getParent()
				.getFellowIfAny("ermSysIpConfigLbx");
		Textbox keywordBox = (Textbox) editSysIpConfigWin.getParent()
				.getFellowIfAny("keywordBox");
		Combobox isOpenRdos = (Combobox) editSysIpConfigWin.getParent()
				.getFellowIfAny("isOpenRdo");
		String keyword = keywordBox.getValue();
		String tempisOpenRdo = isOpenRdos.getSelectedItem().getValue();
		
		List<ErmSysIpconfig> resultall = ((ErmSysIpConfigService) SpringUtil
				.getBean("ermSysIpConfigService"))
				.findermSysIpConfigAll(null,null,webEmployee);
		for (int i = 0; i <= resultall.size(); i++) {
			if (i != resultall.size()) {
				String sysIp = resultall.get(i).getSysip();
				String tempsysIp;
				if (sysIp.indexOf("*") != -1) {
					tempsysIp = replaceAsterisk(sysIp);
				} else {
					tempsysIp = sysIp;
				}
				long ipConfigMin = ipConfigToLongMin(tempsysIp);
				long ipConfigMax = ipConfigToLongMax(tempsysIp);
				if (!keyword.equals("") && keyword != null
						||(!tempisOpenRdo.equals("")&&tempisOpenRdo!=null)) {
					String tempkeyword = replaceAsterisk(keyword);
					long keywordMin = ipConfigToLongMin(tempkeyword);
					long keywordMax = ipConfigToLongMax(tempkeyword);
					if (ipConfigMin <= keywordMin && keywordMax <= ipConfigMax) {
						if (tempisOpenRdo == "" || tempisOpenRdo == null) {
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
						} else {
							ermSysIpConfig = ((ErmSysIpConfigService) SpringUtil
									.getBean("ermSysIpConfigService"))
									.findedtAddList("sysip", sysIp).get(0);
							int tempisOpen = Integer.parseInt(tempisOpenRdo);
							if (ermSysIpConfig.getIsopen() == tempisOpen) {
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
							} else {
								List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
										.getBean("ermSysIpConfigService"))
										.findedtAddList("sysip", null);
								ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
										result);
								tmpLML.setMultiple(true);
								ermSysIpConfigLbx.setModel(tmpLML);
							}
						}
					} else {
						List<ErmSysIpconfig> result = ((ErmSysIpConfigService) SpringUtil
								.getBean("ermSysIpConfigService"))
								.findedtAddList("sysip", null);
						ListModelList<ErmSysIpconfig> tmpLML = new ListModelList<ErmSysIpconfig>(
								result);
						tmpLML.setMultiple(true);
						ermSysIpConfigLbx.setModel(tmpLML);
					}
				}
				ermSysIpConfigLbx.setActivePage(currage);
			}
		}
	}
}
