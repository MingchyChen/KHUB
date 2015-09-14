package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmPersonalizeRescount;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUnitUseService;

public class ErmResMonthUseNumberComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -8956285649713645068L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Listbox accountLix;
	@Wire
	private String resIdva;
	@WireVariable
	private WebEmployee webEmployee;
	@WireVariable
	private List<Object> ermResUserList;
	@WireVariable
	private ErmResourcesMainfileV ermResourcesMainfileV;
	@WireVariable
	private WebOrg webOrg;
	@WireVariable
	private WebAccount webAccount;
	@WireVariable
	private ErmCodeDb ermCodeDb;
	@WireVariable
	private String tmpstartDateDbx;
	@WireVariable
	private String tmpendDateDbx;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			List<ErmPersonalizeRescount> modelist = new ArrayList<ErmPersonalizeRescount>(1);
			modelist = this.getDtaList();
			if (modelist.size() < 1) {
				return;
			}
			ListModelList<ErmPersonalizeRescount> listModel = new ListModelList<ErmPersonalizeRescount>(modelist);
			listModel.setMultiple(true);
			accountLix.setModel(listModel);
		} catch (Exception e) {
			log.error("初始化異常", e);
		}
	}

	@Listen("onClick = #export")
	public void export() throws JRException, InterruptedException {
		// 多語系
		try {
			String[] title = new String[8];
			title[0] = Labels.getLabel("ermResUserList.no");// No
			title[1] = Labels.getLabel("ermResUserList.account");// 使用者賬號
			title[2] = Labels.getLabel("ermResUserList.userName");// 使用者姓名
			title[3] = Labels.getLabel("ermResUserList.unit");// 單位
			title[4] = Labels.getLabel("ermResUserList.group");// 組室
			title[5] = Labels.getLabel("ermResUserList.resourceTitle");// 資源題名
			title[6] = Labels.getLabel("ermResUserList.dataBase");// 所屬資料庫
			title[7] = Labels.getLabel("ermResUserList.musterDate");// 點閱日期
			String[] value = { "no", "account", "userName", "unit", "group", "resourceTitle", "dataBase", "musterDate" };
			List<ErmPersonalizeRescount> rescounts = (List<ErmPersonalizeRescount>) accountLix.getListModel();
			List<String[]> modelist = new ArrayList<String[]>(1);
			for (int i = 0; i < rescounts.size(); i++) {
				ErmPersonalizeRescount rescount = rescounts.get(i);
				String[] str = new String[title.length];
				str[0] = String.valueOf(rescount.getShow1());
				str[1] = rescount.getShows2() == null ? "" : rescount.getShows2();
				str[2] = rescount.getShows3() == null ? "" : rescount.getShows3();
				str[3] = rescount.getShows4() == null ? "" : rescount.getShows4();
				str[4] = rescount.getShows5() == null ? "" : rescount.getShows5();
				str[5] = rescount.getShows6() == null ? "" : rescount.getShows6();
				str[6] = rescount.getShows7() == null ? "" : rescount.getShows7();
				str[7] = rescount.getShows8() == null ? "" : rescount.getShows8();
				modelist.add(str);
			}
			Session session = Sessions.getCurrent();
			String tempstartDateDbx = (String) session.getAttribute("startDate");
			String tempendDateDbx = (String) session.getAttribute("endDate");
			String tempResType = (String) session.getAttribute("tempResType");
			String restype = null;
			if ("DB".equals(tempResType)) {
				restype = Labels.getLabel("sysMenuResMainDbws");
			} else {
				restype = Labels.getLabel("sysMenuResMainEjeb");
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(Labels.getLabel("report.ermResMonthUseNumber")
					+ Labels.getLabel("report.report") + "\r" + Labels.getLabel("ermResUserList.musterDate") + ":" + tempstartDateDbx + "~"
					+ tempendDateDbx + "\r" + Labels.getLabel("ermResUserList.resourceType") + ":" + restype);
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value);
			JRDataSource dataSource = new Resources_rsbatch_dataSource(value, modelist);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JExcelApiExporter excelExporter = new JExcelApiExporter();
			excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_userlist_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
			excelExporter.exportReport();

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (Exception e) {
			log.error("匯出異常" + e);
		}
	}

	/**
	 * 資源下載亂碼處理
	 */
	public void doEncoding(String fileName, String flnm) {
		// 編碼後文件名
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(flnm, "UTF-8");// 轉換字符編碼
			// Filedownload
			Filedownload.save(new FileInputStream(fileName), "application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回listbox和Report所需要的數據
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public List<ErmPersonalizeRescount> getDtaList() throws InterruptedException {
		List<String[]> newDataList = new ArrayList<String[]>();// 儲存數據
		List<ErmPersonalizeRescount> rescounts = new ArrayList<ErmPersonalizeRescount>(1);
		try {
			// //獲取報表類型
			Session session = Sessions.getCurrent();
			String[] webOrgName = (String[]) session.getAttribute("webOrgName");
			String[] dataBase = (String[]) session.getAttribute("dataBase");
			Map<String, String> map = (Map<String, String>) Executions.getCurrent().getArg();
			String resId = map.get("resId");
			String tempstartDateDbx = map.get("tempstartDateDbx");
			String tempendDateDbx = map.get("tempendDateDbx");
			if (tempstartDateDbx != null) {
				tmpstartDateDbx = tempstartDateDbx;
			}
			if (tempendDateDbx != null) {
				tmpendDateDbx = tempendDateDbx;
			}
			if (resId != null) {
				resIdva = resId;
			} else {
				resId = resIdva;
			}
			if (tmpstartDateDbx.equals(tmpendDateDbx)) {
				ermResUserList = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findMonthErmResUser(tmpstartDateDbx,
						String.valueOf(Integer.valueOf(tmpstartDateDbx) + 1), resId);
			} else {
				ermResUserList = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findMonthErmResUser(tmpstartDateDbx,
						tmpendDateDbx, resId);
			}
			// 加載數據到Grid中
			List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
			for (int i = 0; i < ermResUserList.size(); i++) {
				try {
					Object[] obj = (Object[]) ermResUserList.get(i);
					String resourseId = (String) obj[1];
					if (((String) obj[5]) == null) {
						obj[5] = "";
					}
					if (resourseId != null && resourseId != "" && resourseId.equals(resId)) {
						String[] data = new String[8];
						try {
							data[0] = String.valueOf(0);
							data[1] = (String) obj[0];
							webAccount = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findAccountName((String) obj[0]);
							data[2] = webAccount.getNameZhTw();
							webOrg = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findOrgIdParent((String) obj[4]);
							data[3] = webOrg.getOrgName();
							webOrg = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findOrgName((String) obj[5]);
							if (webOrg != null && !"".equals(webOrg)) {
								data[4] = webOrg.getOrgName();
							} else {
								data[4] = "";
							}
						} catch (Exception e) {
							log.error("ermDataListTop" + e);
						}
						ermResourcesMainfileV = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findResName((String) obj[1]);
						if (ermResourcesMainfileV != null) {
							data[5] = ermResourcesMainfileV.getTitle();
						}
						ermCodeDb = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findDb((String) obj[3]);
						if (ermCodeDb != null) {
							data[6] = ermCodeDb.getName();
						}
						data[7] = (String) obj[2];
						try {
							if (dataBase == null) {
								for (int j = 0; j < webOrgName.length; j++) {
									if (data[3] != null && !"".equals(data[3]) && data[3].equals(webOrgName[j])) {
										dataList.add(data);
									}
								}
							} else {
								for (int j = 0; j < webOrgName.length; j++) {
									for (int j2 = 0; j2 < dataBase.length; j2++) {
										if ((data[3] != null && !"".equals(data[3]) && data[3].equals(webOrgName[j]))
												&& (data[6].equals(dataBase[j2])))
											dataList.add(data);
									}
								}
							}
						} catch (Exception e) {
							log.error("dataBase error:" + e);
						}

					}
				} catch (Exception e) {
					log.error("ermDataListProblem" + e);
				}
			}

			for (int i = 0; i < dataList.size(); i++) {
				String[] data = dataList.get(i);
				ErmPersonalizeRescount rescount = new ErmPersonalizeRescount();
				rescount.setShow1((i + 1));
				rescount.setShows2(data[1]);
				rescount.setShows3(data[2]);
				rescount.setShows4(data[3]);
				rescount.setShows5(data[4]);
				rescount.setShows6(data[5]);
				rescount.setShows7(data[6]);
				String year = data[7].substring(0, 4);
				String month = data[7].substring(4, 6);
				String day = data[7].substring(6);
				rescount.setShows8(year + "/" + month + "/" + day);
				rescounts.add(rescount);
			}
		} catch (Exception e) {
			log.error("getDtaList:" + e);
		}
		return rescounts;
	}

}
