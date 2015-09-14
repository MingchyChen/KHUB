package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.Resources_rscon_datasource;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesRscon;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResReportService;

public class ErmResReportListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1553826813378537574L;
	@Wire
	private Window ermResReportWin;
	@Wire
	private Radiogroup reportType;
	@Wire
	private Intbox firstyear;
	@Wire
	private Label onemonth;
	@Wire
	private Intbox secondyear;
	@Wire
	private Label twomonth;
	@Wire
	private Combobox firstmonth;
	@Wire
	private Combobox secondmonth;
	@WireVariable
	private WebEmployee webEmployee;
	@Wire
	private ErmResourcesRscon ermResourcesRscon;
	@WireVariable
	private List<Object> ermResRList;
	@Wire
	private ErmCodeGeneralCode ermCodeGeneralCode;
	@Wire
	private Grid grid;
	private String[] reportMonth = null;

	// /**
	// *
	// * 加載方法
	// *
	// */
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		// 默認選中年報表
		reportType.setSelectedIndex(1);
		// 加載firstmonth月份
		for (int i = 1; i <= 12; i++) {
			Comboitem comitem = new Comboitem();
			comitem.setLabel(String.valueOf(i));
			comitem.setValue(String.valueOf(i));
			firstmonth.appendChild(comitem);
		}
		firstmonth.setSelectedIndex(0);
		// 加載secondmonth月份
		for (int i = 1; i <= 12; i++) {
			Comboitem comitem = new Comboitem();
			comitem.setLabel(String.valueOf(i));
			comitem.setValue(String.valueOf(i));
			secondmonth.appendChild(comitem);
		}
		secondmonth.setSelectedIndex(0);
		firstyear.setWidth("70px");
		secondyear.setWidth("70px");
		firstmonth.setWidth("50px");
		secondmonth.setWidth("50px");

	}

	// /**
	// * radioGroup變動事件
	// */
	@Listen("onCheck = #reportType")
	public void onCheck$reportType() {
		// 獲取報表類型
		String tempReportType = reportType.getSelectedItem().getValue();
		if ("1".equals(tempReportType)) {
			firstmonth.setVisible(true);
			secondmonth.setVisible(true);
			onemonth.setVisible(true);
			twomonth.setVisible(true);

		} else if ("0".equals(tempReportType)) {
			firstmonth.setVisible(false);
			secondmonth.setVisible(false);
			onemonth.setVisible(false);
			twomonth.setVisible(false);
		}
	}

	/**
	 * 匯出
	 * 
	 * @throws JRException
	 * @throws InterruptedException
	 */
	@Listen("onClick = #export")
	public void export() throws JRException, InterruptedException {
		// 多語系
		String[] lang = new String[6];
		lang[0] = Labels.getLabel("sysMenuErmResReport");// 電子資源狀況報表
		lang[1] = Labels.getLabel("ermResReport.reportType");// 資源類型
		lang[2] = Labels.getLabel("ermResReport.subject");// 題名
		lang[3] = Labels.getLabel("ermResReport.right");// 連線狀態(正常)
		lang[4] = Labels.getLabel("ermResReport.error");// 連線狀態(異常)
		lang[5] = Labels.getLabel("ermResReport.total");// 合計
		List<String[]> modelist = this.getDtaList();
		if (modelist == null) {
			return;
		}
		// String reportDate[] = reportMonth;
		// 得到日期範圍
		String date = this.getDate(reportMonth);
		List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		// 報表內容
		Resources_rsbatch_report create_report = new Resources_rsbatch_report(
				"");

		JasperReport jasperReport = create_report.getJasperReport(date,
				reportMonth, lang);
		// 報表賦值
		JRDataSource jrdsMain = new Resources_rscon_datasource(modelist);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				null, jrdsMain);
		jasperPrintList.add(jasperPrint);
		// 生成excel報表
		JExcelApiExporter excelExporter = new JExcelApiExporter();
		// 報表路徑
		String realth = ((SystemProperties) SpringUtil
				.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
		// 報表名稱
		String fileName = "resources_ckrs_report.xls";
		// 傳參
		excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,
				jasperPrintList);
		excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth
				+ fileName);
		excelExporter.exportReport();
		// 報表跳轉
		//Executions.sendRedirect(fileName);
		String filePath=realth+fileName;
		doEncoding(filePath, fileName);
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
			Filedownload.save(new FileInputStream(fileName),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 送出、查詢
	 * 
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Listen("onClick = #send")
	public void send() throws InterruptedException {
		String report_type = reportType.getSelectedItem().getValue();
		List<String[]> modelist = new ArrayList<String[]>();
		modelist = getDtaList();
		if (modelist == null) {
			return;
		}

		String[][] model = new String[modelist.size()][reportMonth.length * 2 + 4];
		for (int i = 0; i < modelist.size(); i++) {
			String[] data = modelist.get(i);
			for (int j = 0; j < data.length; j++) {
				if (data[j] == null) {
					model[i][j] = "0";
				} else {
					model[i][j] = data[j];
				}
			}
		}
		if (model == null) {
			model = new String[0][0];
		}

		// 獲取報表類型
		String tempreportType = reportType.getSelectedItem().getValue();
		String type = "";
		if ("monthReport".equals(report_type)) {
			type = "MONTH";
		} else if ("yearReport".equals(report_type)) {
			type = "YEAR";
		}

		// 加載表頭資訊
		loadGridColumns(reportMonth, tempreportType);
		int width = (reportMonth.length * 2 + 2) * 150 + 400;
		String wwidth = String.valueOf(width);
		ermResReportWin.setWidth(String.valueOf(width) + "px");
		grid.setWidth(String.valueOf(width) + "px");// 設置grid的寬度
		ListModel listModel = new SimpleListModel(model);
		grid.setModel(listModel);// 添加數據源到grid中
		grid.setRowRenderer(new Resources_printReport_Renderer());// 將數據加載到grid中
		grid.setMold("paging");// 啟動分頁功能
		grid.setPageSize(20);// 每頁現實數據條數

	}

	// /**
	// * 得到報表的日期範圍
	// * @return
	// */
	public String getDate(String reportDate[]) {
		// //獲取報表類型
		String tempreportType = reportType.getSelectedItem().getValue();
		int firstYear = firstyear.getValue();// 第一個年份
		int secondYear = secondyear.getValue();// 第二個年份
		// 年報
		if ("0".equals(tempreportType)) {
			// 年月
			for (int i = 0; i < reportDate.length; i++) {
				reportDate[i] = reportDate[i]
						+ Labels.getLabel("ermResReport.year");
			}
			return firstYear + "~" + secondYear;
		}
		String firstMonth = firstmonth.getSelectedItem().getLabel();
		;// 第一個月份
		String secondMonth = secondmonth.getSelectedItem().getLabel();// 第二個月份
		// 年月
		for (int i = 0; i < reportDate.length; i++) {
			reportDate[i] = reportDate[i].substring(0, 4)
					+ Labels.getLabel("ermResReport.year")
					+ reportDate[i].substring(4)
					+ Labels.getLabel("ermResReport.month");
		}
		return firstYear + "/" + firstMonth + "~" + secondYear + "/"
				+ secondMonth;
	}

	/**
	 * 返回Grid和Report所需要的數據
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public List<String[]> getDtaList() throws InterruptedException {
		// 判斷radio是否選中
		if (reportType.getSelectedIndex() == -1) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.reportType")
					+ Labels.getLabel("ermResReport.notNull"),
					Labels.getLabel("info"));
			return null;
		}
		// 判斷第一個年份是否為空
		if (firstyear.getValue() == null) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.firstYear")
					+ Labels.getLabel("ermResReport.notNull"),
					Labels.getLabel("info"));
			return null;
		}
		// 判斷第二個年份是否為空
		if (secondyear.getValue() == null) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.secondYear")
					+ Labels.getLabel("ermResReport.notNull"),
					Labels.getLabel("info"));
			return null;
		}
		// 判斷第一個年份的格式是否正確
		if (String.valueOf(firstyear.getValue()).length() != 4) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.firstYear")
					+ Labels.getLabel("ermResReport.typeError"),
					Labels.getLabel("info"));
			return null;
		}
		// 判斷第二個年份的格式是否正確
		if (String.valueOf(secondyear.getValue()).length() != 4) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.secondYear")
					+ Labels.getLabel("ermResReport.typeError"),
					Labels.getLabel("info"));
			return null;
		}
		// //獲取報表類型
		String report_type = reportType.getSelectedItem().getValue();
		int firstYear = firstyear.getValue();// 第一個年份
		int secondYear = secondyear.getValue();// 第二個年份
		if (firstYear > secondYear) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.yearType")
					+ Labels.getLabel("ermResReport.typeError"),
					Labels.getLabel("info"));
			return null;
		}
		// String sql="";
		// int monthNum=0;//區間日期的總月數
		String[] monthArray = null;// 存儲需要匯出的所有月份
		String[] yearArray = null;// 存儲需要匯出的所有年份
		String yearOrMonthArray[] = null;// 數組長度
		String firstMonth = "";// 第一個月份
		String secondMonth = "";// 第二個月份
		// 月報
		if ("1".equals(report_type)) {

			firstMonth = firstmonth.getSelectedItem().getLabel();
			secondMonth = secondmonth.getSelectedItem().getLabel();
			if (firstYear < secondYear) {// 如果不是同一年
				int yearIndex = 0;
				// 如果不是相鄰的年份
				if (secondYear - firstYear > 1) {
					yearIndex = secondYear - firstYear - 1;
				}
				int firstNum = Integer.parseInt(firstMonth);// 第一個區間內的月份（轉化成整形）
				int allMonth = 12 - firstNum + Integer.parseInt(secondMonth)
						+ (yearIndex * 12);
				monthArray = new String[allMonth + 1];
				int index = 0;// 索引
				int monthIndex = 1;// 變量
				for (int i = firstNum; i <= (firstNum + allMonth); i++) {
					if (i <= 12) {
						monthArray[index] = String.valueOf(i);
					} else {
						if (monthIndex == 13) {
							monthIndex = 1;
						}
						monthArray[index] = String.valueOf(monthIndex);
						monthIndex++;
					}
					index++;
				}
			} else if (firstYear == secondYear) {// 如果年份相同
				int fMonth = Integer.parseInt(firstMonth);
				int sMonth = Integer.parseInt(secondMonth);
				// 年份相同，判斷第一個月份是否大於第二個月份，如果大於則提示第一個日期應該小於第二個日期
				if (fMonth > sMonth) {
					ZkUtils.showInformation(Labels.getLabel("ermResReport.monthType")
							+ Labels.getLabel("ermResReport.typeError"),
							Labels.getLabel("info"));
					return null;
				} else {
					if (fMonth == sMonth) {
						monthArray = new String[1];
						monthArray[0] = firstMonth;
					} else {
						int number = sMonth - fMonth;
						monthArray = new String[number + 1];
						for (int i = 0; i < monthArray.length; i++) {
							monthArray[i] = String.valueOf(fMonth + i);
						}
					}
				}
			}
			int years = firstYear;
			// 循環月份將時間日期格式轉換成年+月的格式
			for (int i = 0; i < monthArray.length; i++) {
				if (monthArray[i].length() == 1) {
					monthArray[i] = String.valueOf(years) + "0"
							+ String.valueOf(monthArray[i]);
				} else {
					if (monthArray[i].equals("12")) {
						monthArray[i] = String.valueOf(years)
								+ String.valueOf(monthArray[i]);
						years++;
					} else {
						monthArray[i] = String.valueOf(years)
								+ String.valueOf(monthArray[i]);
					}
				}

			}
			// 將報表所需的月份集賦值給reportMonth
			reportMonth = monthArray.clone();
			yearOrMonthArray = monthArray.clone();

			if (firstMonth.length() == 1) {
				firstMonth = "0" + firstMonth;
			}
			if (secondMonth.length() == 1) {
				secondMonth = "0" + secondMonth;
			}
			ermResRList = ((ErmResReportService) SpringUtil
					.getBean("ermResReportService")).findSendReport(firstYear,
					firstMonth, secondYear, secondMonth);
		} else if ("0".equals(report_type)) {
			int yearReportIndex = 0;// 變量
			// 計算年報的數組長度
			if (secondYear - firstYear == 0) {
				yearReportIndex = 1;
			} else if (secondYear - firstYear > 0) {
				yearReportIndex = secondYear - firstYear + 1;
			}
			// 初始化存儲總年份的數組長度
			yearArray = new String[yearReportIndex];
			yearReportIndex = 0;
			// 向數組中存值
			for (int i = firstYear; i <= secondYear; i++) {
				yearArray[yearReportIndex] = String.valueOf(i);
				yearReportIndex++;
			}
			// 將報表所需的年份集賦值給reportYear
			reportMonth = yearArray.clone();
			yearOrMonthArray = yearArray.clone();
			ermResRList = ((ErmResReportService) SpringUtil
					.getBean("ermResReportService")).findErmResourcesRsconAll(
					firstYear, secondYear);
		}
		// 加載數據到Grid中
		List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
		for (int i = 0; i < ermResRList.size(); i++) {
			Object[] obj = (Object[]) ermResRList.get(i);
			String resId = (String) obj[0];
			if (resId != null && resId != "") {
				String[] data = new String[6];
				data[0] = (String) obj[0];
				data[1] = (String) obj[1];
				data[2] = (String) obj[2];

				if (resId.indexOf("EJ") != -1) {
					data[3] = "電子期刊";
				} else {
					data[3] = "電子資料庫";
				}
				data[4] = (String) obj[3];
				data[5] = obj[4].toString();
				dataList.add(data);
			}
		}

		List<String[]> modelist = new ArrayList<String[]>();
		String[] dataArray = null;
		if (dataList.size() > 0) {
			String temp_resources_id = "";// 臨時記錄resources_id
			String temp_monthDate = "";// 臨時記錄月份
			int monthUrlcon1 = 0;// 記錄每個月的連接狀態為1的數量
			int monthUrlcon2 = 0;// 記錄每個月的連接狀態為2的數量
			int allNormalNum = 0;// 記錄總的正常狀體
			int allNoNormalNum = 0;// 記錄總的異常狀體
			for (int i = 0; i < dataList.size(); i++) {
				String[] data = dataList.get(i);
				String resources_id = data[0];// 資源類型
				String datetime = data[1];// 日期
				String urlcon = data[2];// 連線狀態
				String resources_name = data[3];// 資源類型name
				String title = data[4];// 題名
				int numbers = Integer.parseInt(data[5]);// 狀態書目
				// 第一次進入並且是第一條數據
				if ("".equals(temp_resources_id)) {
					dataArray = new String[yearOrMonthArray.length * 2 + 4];
					// 資源類型name
					dataArray[0] = resources_name;
					// 題名
					dataArray[1] = title;
					if ("1".equals(urlcon)) {
						monthUrlcon1 = monthUrlcon1 + numbers;
					} else if ("2".equals(urlcon)) {
						monthUrlcon2 = monthUrlcon2 + numbers;
					}
				} else {
					// 如果本次的resources_id與上一條數據的resources_id不同
					if (!temp_resources_id.equals(resources_id)) {
						for (int j = 0; j < yearOrMonthArray.length; j++) {
							if (yearOrMonthArray[j].equals(temp_monthDate)) {

								dataArray[j * 2 + 2] = String
										.valueOf(monthUrlcon1);
								allNormalNum += monthUrlcon1;

								dataArray[j * 2 + 3] = String
										.valueOf(monthUrlcon2);
								allNoNormalNum += monthUrlcon2;

							}
						}
						dataArray[yearOrMonthArray.length * 2 + 2] = String
								.valueOf(allNormalNum);
						dataArray[yearOrMonthArray.length * 2 + 3] = String
								.valueOf(allNoNormalNum);
						modelist.add(dataArray);
						dataArray = new String[yearOrMonthArray.length * 2 + 4];
						allNormalNum = 0;
						allNoNormalNum = 0;
						monthUrlcon1 = 0;
						monthUrlcon2 = 0;
						// 資源類型name
						dataArray[0] = resources_name;

						// 題名
						dataArray[1] = title;
						if ("1".equals(urlcon)) {
							monthUrlcon1 = monthUrlcon1 + numbers;
						} else if ("2".equals(urlcon)) {
							monthUrlcon2 = monthUrlcon2 + numbers;
						}

					} else {// 資源類型相同
						// 如果日期與上一條數據相同
						if (temp_monthDate.equals(datetime)) {
							if ("1".equals(urlcon)) {
								monthUrlcon1 = monthUrlcon1 + numbers;
							} else if ("2".equals(urlcon)) {
								monthUrlcon2 = monthUrlcon2 + numbers;
							}

						} else {
							for (int j = 0; j < yearOrMonthArray.length; j++) {
								if (yearOrMonthArray[j].equals(temp_monthDate)) {

									dataArray[j * 2 + 2] = String
											.valueOf(monthUrlcon1);
									allNormalNum += monthUrlcon1;

									dataArray[j * 2 + 3] = String
											.valueOf(monthUrlcon2);
									allNoNormalNum += monthUrlcon2;

								}
							}
							monthUrlcon1 = 0;
							monthUrlcon2 = 0;
							if ("1".equals(urlcon)) {
								monthUrlcon1 = monthUrlcon1 + numbers;
							} else if ("2".equals(urlcon)) {
								monthUrlcon2 = monthUrlcon2 + numbers;
							}
						}
					}
				}

				// 如果是最後一個數據
				if (i == dataList.size() - 1) {
					for (int j = 0; j < yearOrMonthArray.length; j++) {
						if (yearOrMonthArray[j].equals(datetime)) {

							dataArray[j * 2 + 2] = String.valueOf(monthUrlcon1);
							allNormalNum += monthUrlcon1;

							dataArray[j * 2 + 3] = String.valueOf(monthUrlcon2);
							allNoNormalNum += monthUrlcon2;

						}
					}
					dataArray[yearOrMonthArray.length * 2 + 2] = String
							.valueOf(allNormalNum);
					dataArray[yearOrMonthArray.length * 2 + 3] = String
							.valueOf(allNoNormalNum);
					modelist.add(dataArray);
				}
				// 臨時記錄resources_id
				temp_resources_id = resources_id;
				// 臨時記錄datetime
				temp_monthDate = datetime;
			}
		}
		return modelist;
	}

	/**
	 * 加載表頭資訊
	 * 
	 */
	public void loadGridColumns(String[] array, String type) {
		// 清空grid內的部分存在的組件
		if (grid.getChildren() != null && grid.getChildren().size() > 0) {
			// System.out.println(grid.getChildren().size());
			for (int i = 0; i < grid.getChildren().size(); i++) {
				// 獲取每個組件的Class
				String compoentClass = grid.getChildren().get(i).getClass()
						.toString();
				if (compoentClass.equals("class org.zkoss.zul.Columns")) {
					grid.getChildren().remove(i);
				} else if (compoentClass.equals("class org.zkoss.zul.Auxhead")) {
					Auxhead a = (Auxhead) grid.getChildren().get(i);
					a.getChildren().clear();
				}
			}
		}
		Auxhead auxhead = new Auxhead();
		int index = array.length + 3;
		for (int i = 0; i < index; i++) {
			Auxheader auxheader = new Auxheader();
			auxheader.setAlign("center");
			if (i == 0) {
				auxheader.setRowspan(2);
				auxheader.setLabel(Labels.getLabel("ermResReport.resType"));
				auxheader.setWidth("100px");
			} else if (i == 1) {
				auxheader.setRowspan(2);
				auxheader.setLabel(Labels.getLabel("ermResReport.subject"));
				auxheader.setWidth("200px");
			} else if (i > 1 && i < (index - 1)) {
				auxheader.setColspan(2);
				if ("0".equals(type)) {
					auxheader.setLabel((array[i - 2])
							+ Labels.getLabel("ermResReport.year"));
				} else if ("1".equals(type)) {
					String year = array[i - 2].substring(0, 4);// 如果是月報表，則得到年份
					String month = array[i - 2].substring(4);// 如果是月報表得到月份
					auxheader.setLabel(year
							+ Labels.getLabel("ermResReport.year") + month
							+ Labels.getLabel("ermResReport.month"));
				}

				auxheader.setWidth("300px");
			} else if (i == (index - 1)) {
				auxheader.setColspan(2);
				auxheader.setLabel(Labels.getLabel("ermResReport.total"));
				auxheader.setWidth("300px");
			}

			auxhead.appendChild(auxheader);
		}
		grid.appendChild(auxhead);
		Auxhead nextAuxhead = new Auxhead();
		for (int i = 0; i < (array.length * 2 + 2); i++) {
			Auxheader nextAuxheader = new Auxheader();
			nextAuxheader.setAlign("center");
			if (i % 2 != 0) {
				nextAuxheader.setLabel(Labels.getLabel("ermResReport.error"));
			} else {
				nextAuxheader.setLabel(Labels.getLabel("ermResReport.right"));
			}

			nextAuxheader.setWidth("100px");
			nextAuxhead.appendChild(nextAuxheader);
		}
		grid.appendChild(nextAuxhead);

		Columns columns = new Columns();
		columns.setVisible(false);
		for (int i = 0; i < (array.length * 2 + 4); i++) {

			Column column = new Column();

			if (i == 0) {
				column.setLabel(Labels.getLabel("ermResReport.resources"));
				column.setWidth("200px");
			} else if (i == 1) {
				column.setLabel(Labels.getLabel("ermResReport.subject"));
				column.setWidth("200px");
			} else {
				if (i % 2 != 0) {
					column.setLabel(Labels.getLabel("ermResReport.error"));
				} else {
					column.setLabel(Labels.getLabel("ermResReport.right"));
				}
				column.setWidth("150px");
			}

			columns.appendChild(column);
		}
		grid.appendChild(columns);
	}
}

/**
 * 加載數據到Grid中
 * 
 * @author nj
 * 
 */
class Resources_printReport_Renderer implements RowRenderer {
	public void render(Row row, Object data, int arg2) throws Exception {
		// TODO Auto-generated method stub
		String[] row_data = (String[]) data;
		// 將數據循環放到row裏面
		for (int i = 0; i < row_data.length; i++) {
			Label label = new Label(row_data[i]);
			label.setParent(row);
		}
	}
}
