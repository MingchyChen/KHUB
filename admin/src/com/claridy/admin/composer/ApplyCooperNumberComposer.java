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
import com.claridy.domain.WebCooperation;
import com.claridy.facade.ApplyCooperResService;
import com.claridy.facade.ErmResUserListService;
import com.claridy.facade.WebErwSourceService;

/**
 * WebSysLogComposer 系統日誌Composer
 * 
 * @author RemberSu
 * @serial 2014-06-05
 * 
 */
public class ApplyCooperNumberComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox applyCooperNumberLbx;
	@Wire
	private Label db;
	@Wire
	private Label unit;
	@Wire
	private Label condition;
	@WireVariable
	private List<WebCooperation> applyCooperNumberList;

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions.getCurrent().getArg();
			String parentOrgId = (String) map.get("parentId");
			if (parentOrgId.equals("title")) {
				parentOrgId = "";
			}
			String dbId = (String) map.get("dbId");
			String status = map.get("status");
			// employeens=XSSStringEncoder.encodeXSSString(employeens);
			Session session = Sessions.getCurrent();
			Date startDate = (Date) session.getAttribute("startDate");
			Date endDate = (Date) session.getAttribute("endDate");
			Format format = new SimpleDateFormat("yyyy/MM/dd");
			String tempstartDate = format.format(startDate);
			String tempendDate = format.format(endDate);
			String tempOrgId = "";
			String tempDbName = "";
			System.out.println(status);
			applyCooperNumberList = ((ApplyCooperResService) SpringUtil.getBean("applyCooperResService")).findWebCooperationListByStatus(startDate,
					endDate, parentOrgId, dbId, status);
			condition.setValue(Labels.getLabel("accountNumber.dateRange") + ":" + tempstartDate + "~" + tempendDate);
			if (parentOrgId != "") {
				tempDbName = ((WebErwSourceService) SpringUtil.getBean("webErwSourceService")).getWebErwSourceByDBID(dbId).getNameZhTw();
				tempOrgId = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgName(parentOrgId).getOrgName();
				unit.setValue(Labels.getLabel("applyCooperRes.unit") + ":" + tempOrgId);
				db.setValue(Labels.getLabel("applyCooperRes.dataBase") + ":" + tempDbName);
			} else {
				unit.setValue(Labels.getLabel("applyCooperRes.unit") + ":" + Labels.getLabel("accountNumber.all"));
				db.setValue(Labels.getLabel("applyCooperRes.dataBase") + ":" + Labels.getLabel("accountNumber.all"));
			}

			ListModelList<WebCooperation> tmpLML = new ListModelList<WebCooperation>(applyCooperNumberList);
			applyCooperNumberLbx.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("申請館合資料庫統計詳細資料初始化頁面出錯", e);
		}
	}

	@Listen("onClick = #export")
	public void export() {
		try {

			String[] title = { Labels.getLabel("applyCooper.applyOdd"), Labels.getLabel("webErwSource.applyTitle"),
					Labels.getLabel("webErwSource.apployPeople"), Labels.getLabel("webErwSource.applyOrg"),
					Labels.getLabel("webErwSource.applyDate"), Labels.getLabel("webErwSource.acceptOrg"), Labels.getLabel("applyCooper.retStatus") };
			String[] value = { "applyOdd", "apployPeople", "applyOrg", "applyTitle", "applyDate", "acceptOrg", "retStatus" };
			if (applyCooperNumberList == null) {
				ZkUtils.showInformation(Labels.getLabel("report.select"), Labels.getLabel("info"));
				return;
			}
			applyCooperNumberList = (List<WebCooperation>) applyCooperNumberLbx.getListModel();
			List<String[]> accountList = new ArrayList<String[]>();
			for (int i = 0; i < applyCooperNumberList.size(); i++) {
				Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				WebCooperation applyCooperN = applyCooperNumberList.get(i);
				if (applyCooperN != null) {
					Date logDate = applyCooperN.getCreateDate();
					String logDateStr = "";
					if (logDate != null) {
						logDateStr = format.format(logDate);
					}
					String status = "";
					if (applyCooperN.getStatus() == 0) {
						status = Labels.getLabel("webCooperation.statusNoDispose");
					} else if (applyCooperN.getStatus() == 1) {
						status = Labels.getLabel("webCooperation.statusOK");
					} else if (applyCooperN.getStatus() == 2) {
						status = Labels.getLabel("webCooperation.statusNO");
					}
					String[] values = { applyCooperN.getUuid(), applyCooperN.getAtitle(), applyCooperN.getApplyAccount().getNameZhTw(),
							applyCooperN.getApplyAccount().getParentOrgName(), logDateStr,
							applyCooperN.getAcceptEmployee().getParentWebOrg().getOrgName(), status };
					accountList.add(values);
				}
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(Labels.getLabel("report.applyCooperNumber")
					+ Labels.getLabel("report.report") + "\r" + condition.getValue() + "\r" + db.getValue() + "\r" + unit.getValue());
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value);
			JRDataSource dataSource = new Resources_rsbatch_dataSource(value, accountList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JExcelApiExporter excelExporter = new JExcelApiExporter();
			excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_cooperNumber_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (JRException e) {
			log.error("申請館合資料庫統計詳細資料匯出異常" + e);
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
