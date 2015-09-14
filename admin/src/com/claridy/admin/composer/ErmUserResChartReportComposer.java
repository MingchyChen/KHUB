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
import org.zkoss.zk.ui.WrongValueException;
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
import com.claridy.domain.ErmPersonalizeRescount;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmUserChartService;

public class ErmUserResChartReportComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -862780389484912518L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Datebox startDateDbx;
	@Wire
	private Datebox endDateDbx;
	@Wire
	private Combobox resType;
	@Wire
	private Combobox countChart;
	@Wire
	private Listbox ResCharklbx;
	@WireVariable
	private WebEmployee webEmployee;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			Comboitem all = new Comboitem();
			all.setLabel(Labels.getLabel("selUseNumber.all"));
			all.setValue("0");
			Comboitem db = new Comboitem();
			db.setLabel(Labels.getLabel("ermResUserList.electronicDatabase"));
			db.setValue("DB");
			resType.appendChild(db);
			Comboitem ej = new Comboitem();
			ej.setLabel(Labels.getLabel("ermResUserList.electronicPeriodicals"));
			ej.setValue("EJ");
			resType.appendChild(all);
			resType.appendChild(db);
			resType.appendChild(ej);
			resType.setSelectedIndex(0);
			Comboitem ten = new Comboitem();
			ten.setLabel("10");
			ten.setValue(10);
			countChart.appendChild(ten);
			Comboitem fiv = new Comboitem();
			fiv.setLabel("50");
			fiv.setValue(50);
			countChart.appendChild(fiv);
			Comboitem hun = new Comboitem();
			hun.setLabel("100");
			hun.setValue(100);
			countChart.appendChild(hun);
			countChart.setSelectedIndex(0);
			startDateDbx.setValue(new Date());
			endDateDbx.setValue(new Date());
		} catch (Exception e) {
			log.error("初始化異常" + e);
		}
	}

	@Listen("onClick = #export")
	public void export() throws JRException, InterruptedException {
		// 多語系
		try {
			String[] title = new String[5];
			title[0] = Labels.getLabel("ermResUserList.account");
			title[1] = Labels.getLabel("ermResUserList.userName");
			title[2] = Labels.getLabel("ermResUserList.unit");
			title[3] = Labels.getLabel("ermResUserList.group");
			title[4] = Labels.getLabel("ermUserResChart.count");
			String[] value = { "account", "userName", "unit", "group", "count" };
			List<ErmPersonalizeRescount> modelists = (List<ErmPersonalizeRescount>) ResCharklbx.getListModel();
			if (modelists.size() < 1) {
				ZkUtils.showInformation(Labels.getLabel("report.select"), Labels.getLabel("info"));
				return;
			}
			List<String[]> modelist = new ArrayList<String[]>(1);
			for (int i = 0; i < modelists.size(); i++) {
				ErmPersonalizeRescount rescount = modelists.get(i);
				String[] strs = new String[5];
				strs[0] = rescount.getShows1() == null ? "" : rescount.getShows1();
				strs[1] = rescount.getShows2() == null ? "" : rescount.getShows2();
				strs[2] = rescount.getShows3() == null ? "" : rescount.getShows3();
				strs[3] = rescount.getShows4() == null ? "" : rescount.getShows4();
				strs[4] = String.valueOf(rescount.getShow5());
				modelist.add(strs);
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report2 rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.ermUserResChart")
					+ Labels.getLabel("report.report") + "\r" + Labels.getLabel("ermResUserList.resourceType") + ":"
					+ resType.getSelectedItem().getLabel() + "\r" + Labels.getLabel("ermUserResChart.countChart") + ":"
					+ Labels.getLabel("ermUserResChart.chart") + countChart.getSelectedItem().getLabel());
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value, 4);
			JRDataSource dataSource = new Resources_rsbatch_dataSource2(value, modelist);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JRXlsExporter excelExporter = new JRXlsExporter();
			excelExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_ermUserResChart_report.xls";
			excelExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			excelExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
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

	@Listen("onClick = #search")
	public void send() throws InterruptedException {
		try {
			List<ErmPersonalizeRescount> modelist = new ArrayList<ErmPersonalizeRescount>();
			modelist = getDtaList();
			if (modelist.size() < 1) {
				return;
			}
			ListModelList<ErmPersonalizeRescount> listModel = new ListModelList<ErmPersonalizeRescount>(modelist);
			listModel.setMultiple(true);
			ResCharklbx.setModel(listModel);
		} catch (WrongValueException e) {
			log.error("送出異常" + e);
		}
	}

	/**
	 * 返回Grid和Report所需要的數據
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public List<ErmPersonalizeRescount> getDtaList() throws InterruptedException {
		List<ErmPersonalizeRescount> newDataList = new ArrayList<ErmPersonalizeRescount>();// 儲存數據
		try {
			// 判斷資源類型是否選中
			Format formatter = new SimpleDateFormat("yyyyMMdd");
			String tempstartDateDbx = formatter.format(startDateDbx.getValue());
			String tempendDateDbx = formatter.format(endDateDbx.getValue());
			if (Integer.parseInt(tempstartDateDbx) > Integer.parseInt(tempendDateDbx)) {
				ZkUtils.showInformation(Labels.getLabel("ermResUserList.musterDate") + Labels.getLabel("ermResUserList.range"),
						Labels.getLabel("info"));
				return null;
			}
			String tempResType = resType.getSelectedItem().getValue();
			int chart = countChart.getSelectedItem().getValue();
			newDataList = ((ErmUserChartService) SpringUtil.getBean("ermUserChartService")).findUserResChart(tempstartDateDbx, tempendDateDbx, chart,
					tempResType);
		} catch (Exception e) {
			log.error("getDtaList:" + e);
		}
		return newDataList;
	}
}