package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUnitUseService;

public class ErmResUnitUseNumberComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7421384233225689086L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Listbox accountLix;
	@Wire
	private String unitVa;
	@Wire
	private String resIdva;
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

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			List<ErmPersonalizeRescount> modelist = new ArrayList<ErmPersonalizeRescount>(1);
			modelist = getDtaList();
			if (modelist.size() < 1) {
				return;
			}
			ListModelList<ErmPersonalizeRescount> modelList = new ListModelList<ErmPersonalizeRescount>(modelist);
			modelList.setMultiple(true);
			accountLix.setModel(modelList);
		} catch (Exception e) {
			log.error("初始化異常" + e);
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
				str[1] = String.valueOf(rescount.getShows2());
				str[2] = String.valueOf(rescount.getShows3());
				str[3] = String.valueOf(rescount.getShows4());
				str[4] = String.valueOf(rescount.getShows5());
				str[5] = String.valueOf(rescount.getShows6());
				str[6] = String.valueOf(rescount.getShows7());
				str[7] = String.valueOf(rescount.getShows8());
				modelist.add(str);
			}

			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(Labels.getLabel("report.ermUserList")
					+ Labels.getLabel("report.report"));
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
			// Executions.sendRedirect(fileName);

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
	 * 返回Grid和Report所需要的數據
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
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format formatter = new SimpleDateFormat("yyyyMMdd");
			String tempstartDateDbx = formatter.format(startDate);
			String tempendDateDbx = formatter.format(endDate);
			Map<String, String> map = (Map<String, String>) Executions.getCurrent().getArg();
			String unit = map.get("unit");
			if (unit != null) {
				unitVa = unit;
			} else {
				unit = unitVa;
			}
			String resId = map.get("resName");
			if (resId != null) {
				resIdva = resId;
			} else {
				resId = resIdva;
			}
			ermResUserList = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findErmResUser(tempstartDateDbx, tempendDateDbx);
			// 加載數據到Grid中
			List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
			for (int i = 0; i < ermResUserList.size(); i++) {
				Object[] obj = (Object[]) ermResUserList.get(i);
				String resourseId = (String) obj[1];
				if (((String) obj[5]) == null) {
					obj[5] = "";
				}
				if (resourseId != null && resourseId != "" && ((String) obj[4]).equals(unit) && resourseId.equals(resId)) {
					String[] data = new String[8];
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
					ermResourcesMainfileV = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findResName((String) obj[1]);
					if (ermResourcesMainfileV != null) {
						data[5] = ermResourcesMainfileV.getTitle();
					}
					ermCodeDb = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findDb((String) obj[3]);
					if (ermCodeDb != null) {
						data[6] = ermCodeDb.getName();
					}
					data[7] = (String) obj[2];
					dataList.add(data);
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
