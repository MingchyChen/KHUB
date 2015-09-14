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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.SysUseDetail;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUserListService;
import com.claridy.facade.SystUseNumberService;

/**
 * WebSysLogComposer 系統日誌Composer
 * 
 * @author RemberSu
 * @serial 2014-06-05
 * 
 */
public class SysUseNComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox applyCooperNumberLbx;
	@Wire
	private Listheader sysTitle;
	@Wire
	private Label unit;
	@Wire
	private Label condition;
	@Wire
	private Label titleType;
	@Wire
	private WebOrg webOrg;
	@WireVariable
	private List<SysUseDetail> applyCooperNumberList;

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions
					.getCurrent().getArg();
			String parentOrgId = (String) map.get("parentId");
			if (parentOrgId.equals("title")) {
				parentOrgId = "";
			}
			String orgId = (String) map.get("orgId");
			String status = map.get("status");
			if (status == "new") {
				sysTitle.setLabel(Labels.getLabel("sysUseNumber.newstitle")
						+ Labels.getLabel("sysUseNumber.title"));
				titleType.setValue(Labels.getLabel("sysUseNumber.newstitle"));
			}
			if (status == "edu") {
				sysTitle.setLabel(Labels.getLabel("sysUseNumber.edutitle")
						+ Labels.getLabel("sysUseNumber.title"));
				titleType.setValue(Labels.getLabel("sysUseNumber.edutitle"));
			}
			if (status == "farm") {
				sysTitle.setLabel(Labels.getLabel("sysUseNumber.farmtitle")
						+ Labels.getLabel("sysUseNumber.title"));
				titleType.setValue(Labels.getLabel("sysUseNumber.farmtitle"));
			}
			if (status == "report") {
				sysTitle.setLabel(Labels.getLabel("sysUseNumber.reporttitle")
						+ Labels.getLabel("sysUseNumber.title"));
				titleType.setValue(Labels.getLabel("sysUseNumber.reporttitle"));
			}
			if (status == "pub") {
				sysTitle.setLabel(Labels.getLabel("sysUseNumber.pubtitle")
						+ Labels.getLabel("sysUseNumber.title"));
				titleType.setValue(Labels.getLabel("sysUseNumber.pubtitle"));
			}
			if (orgId == null) {
				orgId = "";
			}
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy/MM/dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			String tempparentOrgId = "";
			String tempOrgId = "";
			if (orgId == "") {
				applyCooperNumberList = ((SystUseNumberService) SpringUtil
						.getBean("systUseNumberService"))
						.findSysUseDetListByParentOrg(startDate, endDate,
								parentOrgId, status);
			} else if (orgId.indexOf("other") != -1) {
				applyCooperNumberList = ((SystUseNumberService) SpringUtil
						.getBean("systUseNumberService"))
						.findSysUseDetailListByParentOrgOther(startDate,
								endDate, parentOrgId, status);
			} else {
				applyCooperNumberList = ((SystUseNumberService) SpringUtil
						.getBean("systUseNumberService"))
						.findSysUseDetListByOrg(startDate, endDate, orgId,
								status);
			}
			condition.setValue(Labels.getLabel("accountNumber.dateRange") + ":"
					+ tempstartDate + "~" + tempendDate);

			webOrg = ((ErmResUserListService) SpringUtil
					.getBean("ermResUserListService"))
					.findOrgIdParent(parentOrgId);
			tempparentOrgId = webOrg.getOrgName();
			if (orgId == "") {

				unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
						+ Labels.getLabel("orgid") + ":" + tempparentOrgId);
			} else {
				if (orgId.indexOf("other") != -1) {
					String parentOrgId2 = orgId.substring(5, orgId.length());
					webOrg = ((ErmResUserListService) SpringUtil
							.getBean("ermResUserListService"))
							.findOrgIdParent(parentOrgId2);
					tempOrgId = webOrg.getOrgName();
					unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
							+ Labels.getLabel("orgid") + ":" + tempparentOrgId);
				} else {
					webOrg = ((ErmResUserListService) SpringUtil
							.getBean("ermResUserListService"))
							.findOrgIdParent(orgId);
					tempOrgId = webOrg.getOrgName();
					unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
							+ Labels.getLabel("orgid") + ":" + tempparentOrgId
							+ "-" + tempOrgId);
				}
			}
			ListModelList<SysUseDetail> tmpLML = new ListModelList<SysUseDetail>(
					applyCooperNumberList);
			applyCooperNumberLbx.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("系統使用量統計詳細資料初始化頁面出錯", e);
		}
	}

	@Listen("onClick = #export")
	public void export() {
		try {
			String[] title = { Labels.getLabel("sysUseNumber.time"),
					Labels.getLabel("sysUseNumber.accountId"),
					Labels.getLabel("sysUseNumber.accountName"),
					Labels.getLabel("sysUseNumber.ip"), sysTitle.getLabel() };
			String[] value = { "time", "accountId", "accountName", "ip",
					"title" };
			if (applyCooperNumberList == null) {
				ZkUtils.showInformation(Labels.getLabel("report.select"),
						Labels.getLabel("info"));
				return;
			}
			applyCooperNumberList = (List<SysUseDetail>) applyCooperNumberLbx.getListModel();
			List<String[]> accountList = new ArrayList<String[]>();
			for (int i = 0; i < applyCooperNumberList.size(); i++) {
				Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				SysUseDetail applyCooperN = applyCooperNumberList.get(i);
				if (applyCooperN != null) {
					Date logDate = applyCooperN.getCreateDate();
					String logDateStr = "";
					if (logDate != null) {
						logDateStr = format.format(logDate);
					}
					String[] values = { logDateStr,
							applyCooperN.getAccountId(),
							applyCooperN.getAccountName(),
							applyCooperN.getAccountIp(),
							applyCooperN.getTitle() };
					accountList.add(values);
				}
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(
					Labels.getLabel("report.sysUseN")
							+ Labels.getLabel("report.report") + "\r"
							+ condition.getValue() + "\r"
							+ titleType.getValue() + "\r" + unit.getValue());
			JasperReport jasperReport = rsbatch_report.getJasperReport(title,
					value);
			JRDataSource dataSource = new Resources_rsbatch_dataSource(value,
					accountList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JExcelApiExporter excelExporter = new JExcelApiExporter();
			excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
					jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil
					.getBean("systemProperties")).systemDocumentPath
					+ SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_sysUseN_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("系統使用量統計詳細資料會出異常" + e);
		}
	}

	public void doEncoding(String fileName, String flnm) {
		// 編碼後文件名
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(flnm, "UTF-8");// 轉換字符編碼
			// Filedownload
			Filedownload.save(new FileInputStream(fileName),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
