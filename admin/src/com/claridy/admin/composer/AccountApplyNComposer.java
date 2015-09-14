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

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.AccountApplyDetail;
import com.claridy.domain.WebOrg;
import com.claridy.facade.AccountApplyNumberService;
import com.claridy.facade.ErmResUserListService;

/**
 * WebSysLogComposer 系統日誌Composer
 * 
 * @author RemberSu
 * @serial 2014-06-05
 * 
 */
public class AccountApplyNComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox accountApplyNLBX;
	@Wire
	private Label condition;
	@Wire
	private Label unit;
	@Wire
	private WebOrg webOrg;
	@WireVariable
	private List<AccountApplyDetail> cooperNList;

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions
					.getCurrent().getArg();
			String parentOrgId = (String) map.get("parentId");
			String orgId = (String) map.get("orgId");
			// employeens=XSSStringEncoder.encodeXSSString(employeens);
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy/MM/dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			String tempparentOrgId = "";
			String tempOrgId = "";
			if (orgId == "" || orgId == null) {
				orgId = "";
			}
			if (orgId.indexOf("other") == -1) {
				webOrg = ((ErmResUserListService) SpringUtil
						.getBean("ermResUserListService"))
						.findOrgIdParent(parentOrgId);
				tempparentOrgId = webOrg.getOrgName();

				if (orgId == "") {
					cooperNList = ((AccountApplyNumberService) SpringUtil
							.getBean("accountApplyNumberService"))
							.findAccAppDetByParentOrg(startDate, endDate,
									parentOrgId);
				} else {
					tempOrgId = ((ErmResUserListService) SpringUtil
							.getBean("ermResUserListService")).findOrgName(
							orgId).getOrgName();
					cooperNList = ((AccountApplyNumberService) SpringUtil
							.getBean("accountApplyNumberService"))
							.findAccAppDetByOrg(startDate, endDate,
									parentOrgId, orgId);
				}
			} else {
				String parentOrgId2 = orgId.substring(5, orgId.length());
				webOrg = ((ErmResUserListService) SpringUtil
						.getBean("ermResUserListService"))
						.findOrgIdParent(parentOrgId2);
				tempparentOrgId = webOrg.getOrgName();
				// tempparentOrgId = Labels
				// .getLabel("webEmployee.tboxIdType.other");
				cooperNList = ((AccountApplyNumberService) SpringUtil
						.getBean("accountApplyNumberService"))
						.findAccAppDetByParentOrgOther(startDate, endDate,
								parentOrgId2);
			}
			condition.setValue(Labels.getLabel("accountNumber.dateRange") + ":"
					+ tempstartDate + "~" + tempendDate);
			if (tempOrgId == "") {
				unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
						+ Labels.getLabel("orgid") + ":" + tempparentOrgId);
			} else {
				unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
						+ Labels.getLabel("orgid") + ":" + tempparentOrgId
						+ "-" + tempOrgId);
			}
			ListModelList<AccountApplyDetail> tmpLML = new ListModelList<AccountApplyDetail>(
					cooperNList);
			accountApplyNLBX.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("帳號申請量統計詳細資料初始化頁面出錯"+ e);
		}
	}

	@Listen("onClick = #export")
	public void export() {
		try {

			String[] title = { Labels.getLabel("accountApplyNumber.time"),
					Labels.getLabel("accountApplyNumber.accountId"),
					Labels.getLabel("accountApplyNumber.accountName"),
					Labels.getLabel("accountApplyNumber.unit"),
					Labels.getLabel("accountApplyNumber.org"),
					Labels.getLabel("accountApplyNumber.result"), };
			String[] value = { "time", "accountId", "accountName", "unit",
					"org", "result", };
			if (cooperNList==null) {
				ZkUtils.showInformation(Labels.getLabel("report.select"),
						Labels.getLabel("info"));
				return;
			}
			cooperNList = (List<AccountApplyDetail>) accountApplyNLBX.getListModel();
			List<String[]> accountList = new ArrayList<String[]>();
			for (int i = 0; i < cooperNList.size(); i++) {
				Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				AccountApplyDetail cooperNTmp = cooperNList.get(i);
				if (cooperNTmp != null) {
					Date logDate = cooperNTmp.getCreateDate();
					String logDateStr = "";
					if (logDate != null) {
						logDateStr = format.format(logDate);
					}
					String[] values = { logDateStr,
							cooperNTmp.getApplyAccountId(),
							cooperNTmp.getApplyAccountName(),
							cooperNTmp.getApplyParentOrgName(),
							cooperNTmp.getApplyOrgName(),
							cooperNTmp.getCheckResult(), };
					accountList.add(values);
				}
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(
					Labels.getLabel("report.accountApplyN")
							+ Labels.getLabel("report.report") + "\r"
							+ condition.getValue() + "\r" + unit.getValue());
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
			String fileName = "resources_res_accountApplyN_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("帳號申請量詳細資料統計會出異常" + e);
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
