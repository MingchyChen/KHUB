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
import com.claridy.domain.LoginNumber;
import com.claridy.domain.WebOrg;
import com.claridy.facade.AccountNumberService;
import com.claridy.facade.ErmResUserListService;

/**
 * WebSysLogComposer 系統日誌Composer
 * 
 * @author RemberSu
 * @serial 2014-06-05
 * 
 */
public class LoginNumberListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox loginNumberLBX;
	@Wire
	private Label condition;
	@Wire
	private Label unit;
	@Wire
	private WebOrg webOrg;
	@WireVariable
	private List<LoginNumber> loginNList;

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions
					.getCurrent().getArg();
			String parentOrgId = (String) map.get("pUrl");
			// employeens=XSSStringEncoder.encodeXSSString(employeens);
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy/MM/dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			String tempparentOrgId = "";
			String tempOrgId = "";
			if (parentOrgId.indexOf("other") == -1) {
				webOrg = ((ErmResUserListService) SpringUtil
						.getBean("ermResUserListService"))
						.findOrgIdParent(parentOrgId);
				tempparentOrgId = webOrg.getOrgName();
				if (webOrg.getOrgIdParent().length() > 1) {
					tempOrgId = ((ErmResUserListService) SpringUtil
							.getBean("ermResUserListService")).findOrgName(
							webOrg.getOrgIdParent()).getOrgName();
				}

				if (webOrg.getOrgIdParent().equals("")
						|| webOrg.getOrgIdParent().equals("0")) {
					loginNList = ((AccountNumberService) SpringUtil
							.getBean("accountNumberService"))
							.findLogNumListByParentOrg(startDate, endDate,
									parentOrgId);
				} else {
					loginNList = ((AccountNumberService) SpringUtil
							.getBean("accountNumberService"))
							.findLogNumListByOrg(startDate, endDate,
									parentOrgId);
				}
			} else {
				String parentOrgId2 = parentOrgId.substring(5,
						parentOrgId.length());
				webOrg = ((ErmResUserListService) SpringUtil
						.getBean("ermResUserListService"))
						.findOrgIdParent(parentOrgId2);
				tempparentOrgId = webOrg.getOrgName();
				// tempparentOrgId = Labels
				// .getLabel("webEmployee.tboxIdType.other");
				loginNList = ((AccountNumberService) SpringUtil
						.getBean("accountNumberService"))
						.findLogNumListByParentOrgOther(startDate, endDate,
								parentOrgId2);
			}
			condition.setValue(Labels.getLabel("accountNumber.dateRange")
					+ ":" + tempstartDate + "~" + tempendDate);
			if (tempOrgId == "") {
				unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
						+ Labels.getLabel("orgid") + ":" + tempparentOrgId);
			} else {
				unit.setValue(Labels.getLabel("ermResUserList.unit") + "-"
						+ Labels.getLabel("orgid") + ":" + tempOrgId + "-"
						+ tempparentOrgId);
			}
			ListModelList<LoginNumber> tmpLML = new ListModelList<LoginNumber>(
					loginNList);
			loginNumberLBX.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("帳號統計登入量初始化頁面出錯", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Listen("onClick = #export")
	public void export() {
		try {
			String[] title = { Labels.getLabel("loginNumber.loginDate"),
					Labels.getLabel("loginNumber.accountId"),
					Labels.getLabel("loginNumber.accountName"),
					Labels.getLabel("loginNumber.loginIp") };
			String[] value = { "loginDate", "accountId", "accountName",
					"loginIp" };
			List<String[]> accountList = new ArrayList<String[]>();
			loginNList=(List<LoginNumber>) loginNumberLBX.getListModel();
			for (int i = 0; i < loginNList.size(); i++) {
				Format format = new SimpleDateFormat("yyyy-MM-dd");
				LoginNumber loginNumberTmp = loginNList.get(i);
				if (loginNumberTmp != null) {
					Date logDate = loginNumberTmp.getLoginDate();
					String logDateStr = "";
					if (logDate != null) {
						logDateStr = format.format(logDate);
					}
					String[] values = { logDateStr,
							loginNumberTmp.getAccountId(),
							loginNumberTmp.getAccountName(),
							loginNumberTmp.getLoginIp() };
					accountList.add(values);
				}
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(
					Labels.getLabel("report.loginNumber")
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
			String fileName = "resources_res_loginNumber_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("帳號統計登入量匯出異常" + e);
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
