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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

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

import com.claridy.common.util.Resources_rsbatch_dataSource2;
import com.claridy.common.util.Resources_rsbatch_report2;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.domain.AccountNumber;
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
public class ParentOrgNameListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox parentOrgNameLbx;
	@Wire
	private WebOrg webOrg;
	@Wire
	private Label pcondition;
	@Wire
	private Label punit;
	@WireVariable
	private List<AccountNumber> accountNList;

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions.getCurrent().getArg();
			String parentOrgId = (String) map.get("pUrl");
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy/MM/dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			webOrg = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgIdParent(parentOrgId);
			String tempparentOrgId = webOrg.getOrgName();
			pcondition.setValue(Labels.getLabel("ermResUserList.musterDate") + ":" + tempstartDate + "~" + tempendDate);
			punit.setValue(Labels.getLabel("accountNumber.parentOrgName") + ":" + tempparentOrgId);
			accountNList = ((AccountNumberService) SpringUtil.getBean("accountNumberService")).findAccNumListByOrg(startDate, endDate, parentOrgId);
			ListModelList<AccountNumber> tmpLML = new ListModelList<AccountNumber>(accountNList);
			parentOrgNameLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("帳號統計單位組室初始化頁面出錯", e);
		}
	}

	@Listen("onClick = #export")
	public void export() {
		try {

			String[] title = { Labels.getLabel("accountNumber.parentOrgName"), Labels.getLabel("accountNumber.loginNumber"),
					Labels.getLabel("accountNumber.cooperNumber"), };
			String[] value = { "parentOrgName", "loginNumber", "cooperNumber", };
			List<String[]> accountList = new ArrayList<String[]>();
			accountNList = (List<AccountNumber>) parentOrgNameLbx.getListModel();
			for (int i = 0; i < accountNList.size(); i++) {
				AccountNumber accountNumber = accountNList.get(i);
				String[] strs = { accountNumber.getParentOrgName(), String.valueOf(accountNumber.getLoginNumber()),
						String.valueOf(accountNumber.getCooperNumber()) };
				accountList.add(strs);
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report2 rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.parentOrgName")
					+ Labels.getLabel("report.report") + "\r" + pcondition.getValue() + "\r" + punit.getValue());
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value, 1);
			JRDataSource dataSource = new Resources_rsbatch_dataSource2(value, accountList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JRXlsExporter excelExporter = new JRXlsExporter();
			excelExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_accountNumberPON_report.xls";
			excelExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			excelExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
			excelExporter.exportReport();

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("帳號統計單位組室匯出異常" + e);
		}
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
