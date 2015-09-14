package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.claridy.common.util.Resources_rsbatch_dataSource2;
import com.claridy.common.util.Resources_rsbatch_report2;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.AccountNumber;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.AccountNumberService;
import com.claridy.facade.WebOrgListService;

public class AccountNumberListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2598686169364117293L;
	@Wire
	private Datebox startDateDbx;
	@Wire
	private Datebox endDateDbx;
	@Wire
	private Combobox parentOrgId;
	@Wire
	private Listbox accountNLbx;
	@Wire
	private WebEmployee webEmployee;
	@WireVariable
	private List<AccountNumber> accountNumberList;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			((WebOrgListService) SpringUtil.getBean("webOrgListService")).findWebOrgList(null, null, webEmployee);
			List<WebOrg> webParOrgList = ((WebOrgListService) SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgIdParent", "0");
			Comboitem select = new Comboitem();
			select.setLabel(Labels.getLabel("select"));
			select.setValue("");
			parentOrgId.appendChild(select);
			for (int i = 0; i < webParOrgList.size(); i++) {
				WebOrg webParOrg = new WebOrg();
				webParOrg = webParOrgList.get(i);
				Comboitem co = new Comboitem();
				co.setLabel(webParOrg.getOrgName());
				co.setValue(webParOrg.getOrgId());
				parentOrgId.appendChild(co);
			}
			parentOrgId.setSelectedIndex(0);
			startDateDbx.setValue(new Date());
			endDateDbx.setValue(new Date());
		} catch (Exception e) {
			log.error("帳號統計初始化異常" + e);
		}
	}

	@Listen("onClick = #export")
	public void export() {
		try {

			String[] title = { Labels.getLabel("accountNumber.parentOrgName"), Labels.getLabel("accountNumber.loginNumber"),
					Labels.getLabel("accountNumber.cooperNumber") };
			String[] value = { "parentOrgName", "loginNumber", "cooperNumber" };
			if (accountNumberList == null) {
				ZkUtils.showInformation(Labels.getLabel("report.select"), Labels.getLabel("info"));
				return;
			}
			accountNumberList = (List<AccountNumber>) accountNLbx.getListModel();
			List<String[]> accountList = new ArrayList<String[]>();
			for (AccountNumber accountNumber : accountNumberList) {
				String[] values = { accountNumber.getParentOrgName(), String.valueOf(accountNumber.getLoginNumber()),
						String.valueOf(accountNumber.getCooperNumber()) };
				accountList.add(values);
			}
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy-MM-dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			String tempParentOrgId = parentOrgId.getSelectedItem().getValue();
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report2 rsbatch_report = null;
			if (tempParentOrgId == null || "".equals(tempParentOrgId)) {
				rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.acconutNumber") + Labels.getLabel("report.report") + "\r"
						+ Labels.getLabel("accountNumber.dateRange") + ":" + tempstartDate + "~" + tempendDate + "\r"
						+ Labels.getLabel("ermResUserList.unit") + ":" + Labels.getLabel("accountNumber.all"));
			} else {
				if (accountNumberList.size() > 0) {
					tempParentOrgId = accountNumberList.get(0).getParentOrgName();
				} else {
					tempParentOrgId = "";
				}
				rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.acconutNumber") + Labels.getLabel("report.report") + "\r"
						+ Labels.getLabel("accountNumber.dateRange") + ":" + tempstartDate + "~" + tempendDate + "\r"
						+ Labels.getLabel("ermResUserList.unit") + ":" + tempParentOrgId);
			}

			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value, 1);
			JRDataSource dataSource = new Resources_rsbatch_dataSource2(value, accountList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JRXlsExporter excelExporter = new JRXlsExporter();
			excelExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_accountNumber_report.xls";
			excelExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			excelExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("帳號統計匯出異常" + e);
		}
	}

	@Listen("onClick = #search")
	public void send() throws InterruptedException {
		try {
			ListModelList<AccountNumber> tmpLML = new ListModelList<AccountNumber>(getDtaList());
			tmpLML.setMultiple(true);
			accountNLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("帳號統計差查询异常" + e);
		}
	}

	public List<AccountNumber> getDtaList() throws InterruptedException {
		Session session = Sessions.getCurrent();
		Date startDate = startDateDbx.getValue();
		Date endDate = endDateDbx.getValue();
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
		Format format = new SimpleDateFormat("yyyyMMdd");
		int tempstartDate = Integer.parseInt(format.format(startDate));
		int tempendDate = Integer.parseInt(format.format(endDate));
		if (tempstartDate > tempendDate) {
			ZkUtils.showInformation(Labels.getLabel("accountNumber.dateWarn"), Labels.getLabel("info"));
			return new ArrayList<AccountNumber>();
		}
		String tempParentOrgId = parentOrgId.getSelectedItem().getValue();
		accountNumberList = ((AccountNumberService) SpringUtil.getBean("accountNumberService")).findAccNumListByParentOrg(startDate, endDate,
				tempParentOrgId);
		return accountNumberList;
	}

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
}
