package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_dataSource2;
import com.claridy.common.util.Resources_rsbatch_report2;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResMonthUseService;

public class ErmResMonthUseListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3649243821538573200L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Window ermResMonthUseWin;
	@Wire
	private Window ermResMonthUseDBWin;
	@Wire
	private Window ermResMonthUseUnitWin;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private Radiogroup reportType;
	@Wire
	private Combobox firstmonth;
	@Wire
	private Combobox secondmonth;
	@Wire
	private Combobox firstyear;
	@Wire
	private Label onemonth;
	@Wire
	private Combobox secondyear;
	@Wire
	private Label twomonth;
	@Wire
	private Label dataBaseLb;
	@Wire
	private Button dataBaseBtn;
	@Wire
	private Listbox dataBasesLbx;
	@Wire
	private Listbox unitsLbx;
	@Wire
	private Listbox dataLbx;
	@Wire
	private Combobox resType;
	@Wire
	private Textbox resTitle;
	@WireVariable
	private List<String[]> modelist;
	private String[] reportMonth = null;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			Comboitem select = new Comboitem();
			select.setLabel(Labels.getLabel("select"));
			select.setValue("0");
			Comboitem db = new Comboitem();
			db.setLabel(Labels.getLabel("ermResUserList.electronicDatabase"));
			db.setValue("DB");
			resType.appendChild(db);
			Comboitem ej = new Comboitem();
			ej.setLabel(Labels.getLabel("ermResUserList.electronicPeriodicals"));
			ej.setValue("EJ");
			resType.appendChild(ej);
			resType.setSelectedIndex(0);
			Date nowDate = new Date();
			Format formatter = new SimpleDateFormat("yyyy-MM");
			String tempnowDate = formatter.format(nowDate);
			String year = tempnowDate.substring(0, tempnowDate.indexOf("-"));
			String month = tempnowDate.substring(tempnowDate.indexOf("-") + 1);
			int tempyear = Integer.valueOf(year);
			int tempmonth = Integer.valueOf(month);
			for (int i = 1; i <= 12; i++) {
				Comboitem comitem = new Comboitem();
				comitem.setLabel(String.valueOf(i));
				comitem.setValue(String.valueOf(i));
				firstmonth.appendChild(comitem);
			}
			firstmonth.setSelectedIndex(tempmonth - 1);
			for (int i = 1; i <= 12; i++) {
				Comboitem comitem = new Comboitem();
				comitem.setLabel(String.valueOf(i));
				comitem.setValue(String.valueOf(i));
				secondmonth.appendChild(comitem);
			}
			secondmonth.setSelectedIndex(tempmonth - 1);
			for (int i = tempyear; i > tempyear - 5; i--) {
				Comboitem comitem = new Comboitem();
				comitem.setLabel(String.valueOf(i));
				comitem.setValue(String.valueOf(i));
				firstyear.appendChild(comitem);
			}
			firstyear.setSelectedIndex(0);
			for (int i = tempyear; i > tempyear - 5; i--) {
				Comboitem comitem = new Comboitem();
				comitem.setLabel(String.valueOf(i));
				comitem.setValue(String.valueOf(i));
				secondyear.appendChild(comitem);

			}
			secondyear.setSelectedIndex(0);
			if (dataBasesLbx.getContext() == null || dataBasesLbx.getContext() == "") {
				dataBasesLbx.setVisible(false);
			}
			if (unitsLbx.getContext() == null || unitsLbx.getContext() == "") {
				unitsLbx.setVisible(false);
			}
			reportType.setSelectedIndex(1);
			dataBaseLb.setVisible(false);
			dataBaseBtn.setVisible(false);
		} catch (Exception e) {
			log.error("初始化異常" + e);
		}
	}

	@Listen("onClick = #export")
	public void export() throws JRException, InterruptedException {
		// 多語系
		try {
			List<String[]> modelist = this.getDtaList();
			if (modelist == null) {
				ZkUtils.showInformation(Labels.getLabel("report.select"), Labels.getLabel("info"));
				return;
			}
			List<Listitem> unitList = unitsLbx.getItems();
			ListModelList<String> webOrgName = new ListModelList<String>();
			for (Listitem employee : unitList) {
				String unitName = employee.getValue();
				webOrgName.add(unitName);
			}
			List<String[]> unitUseList = new ArrayList<String[]>();
			for (int i = 1; i < dataLbx.getChildren().size(); i++) {
				String[] values;
				try {
					Listitem listitem = (Listitem) dataLbx.getChildren().get(i);
					values = new String[listitem.getChildren().size()];
					for (int j = 0; j < listitem.getChildren().size(); j++) {
						Listcell listcell = (Listcell) listitem.getChildren().get(j);
						if ("".equals(listcell.getLabel())) {
							A a = (A) (listcell.getFirstChild());
							values[j] = a.getLabel();
						} else {
							values[j] = listcell.getLabel();
						}
					}
				} catch (Exception e) {
					Listhead listitem = (Listhead) dataLbx.getChildren().get(i);
					values = new String[listitem.getChildren().size()];
					for (int j = 0; j < listitem.getChildren().size(); j++) {
						Listheader listcell = (Listheader) listitem.getChildren().get(j);
						if ("".equals(listcell.getLabel())) {
							A a = (A) (listcell.getFirstChild());
							values[j] = a.getLabel();
						} else {
							values[j] = listcell.getLabel();
						}
					}
				}
				unitUseList.add(values);
			}
			String[] title = new String[reportMonth.length + 1];
			try {
				Listitem titleItem = (Listitem) dataLbx.getChildren().get(0);
				for (int i = 0; i < titleItem.getChildren().size(); i++) {
					Listcell listcell = (Listcell) titleItem.getChildren().get(i);
					if ("".equals(listcell.getLabel())) {
						A a = (A) (listcell.getFirstChild());
						title[i] = a.getLabel();
					} else {
						title[i] = listcell.getLabel();
					}
				}
			} catch (Exception e) {
				Listhead titleItem = (Listhead) dataLbx.getChildren().get(0);
				for (int i = 0; i < titleItem.getChildren().size(); i++) {
					Listheader listcell = (Listheader) titleItem.getChildren().get(i);
					if ("".equals(listcell.getLabel())) {
						A a = (A) (listcell.getFirstChild());
						title[i] = a.getLabel();
					} else {
						title[i] = listcell.getLabel();
					}
				}
			}
			String[] value = new String[reportMonth.length + 1];
			value[0] = Labels.getLabel("report.ermUnitUseLabel");
			for (int i = 0; i < reportMonth.length; i++) {
				value[i + 1] = reportMonth[i];
			}
			String tempResType = resType.getSelectedItem().getValue();
			Session session = Sessions.getCurrent();
			String tempstartDateDbx = (String) session.getAttribute("startDate");
			String tempendDateDbx = (String) session.getAttribute("endDate");
			String restype = null;
			if ("DB".equals(tempResType)) {
				restype = Labels.getLabel("sysMenuResMainDbws");
			} else {
				restype = Labels.getLabel("sysMenuResMainEjeb");
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report2 rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.ermResMonthUse")
					+ Labels.getLabel("report.report") + "\r" + Labels.getLabel("ermResUserList.musterDate") + ":" + tempstartDateDbx + "~"
					+ tempendDateDbx + "\r" + Labels.getLabel("ermResUserList.resourceType") + ":" + restype);
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value, 1);
			JRDataSource dataSource = new Resources_rsbatch_dataSource2(value, unitUseList);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JRXlsExporter excelExporter = new JRXlsExporter();
			excelExporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_userlist_report.xls";
			excelExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
			excelExporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
			excelExporter.exportReport();
			// Executions.sendRedirect(fileName);

			String filePath = realth + fileName;
			doEncoding(filePath, fileName);
		} catch (Exception e) {
			log.error("匯出異常", e);
		}
	}

	@Listen("onClick = #search")
	public void search() throws InterruptedException {
		if (dataLbx.getChildren() != null) {
			dataLbx.getChildren().clear();
		}
		try {
			modelist = getDtaList();
			if (modelist == null) {
				return;
			}
			String[][] model = new String[modelist.size()][8];
			for (int i = 0; i < modelist.size(); i++) {
				String[] data = modelist.get(i);
				for (int j = 0; j < data.length; j++) {
					if (data[j] == null) {
						model[i][j] = "";
					} else {
						model[i][j] = data[j];
					}
				}
			}

			Listhead listhead = new Listhead();
			Listheader listheader = new Listheader();
			listheader.setLabel(Labels.getLabel("report.ermMonthUseLabel"));
			listheader.setSort("auto");
			listheader.setParent(listhead);
			String tempreportType = reportType.getSelectedItem().getValue();
			if (tempreportType.equals("0")) {
				for (int i = 0; i < reportMonth.length; i++) {
					Listheader listheader2 = new Listheader();
					listheader2.setLabel(reportMonth[i] + Labels.getLabel("ermResReport.year"));
					listheader2.setSort("auto");
					listheader2.setParent(listhead);
				}
			} else {
				for (int i = 0; i < reportMonth.length; i++) {
					Listheader listheader2 = new Listheader();
					String year = reportMonth[i].substring(0, 4);
					String month = reportMonth[i].substring(4);
					listheader2.setLabel(year + Labels.getLabel("ermResReport.year") + month + Labels.getLabel("ermResReport.month"));
					listheader2.setSort("auto");
					listheader2.setParent(listhead);
				}
			}
			listhead.setParent(dataLbx);
			int k = 0;
			for (int i = 0; i < modelist.size();) {
				Listitem newItem = new Listitem();
				k = i;
				int allCount = 0;
				for (int m = k; m < k + reportMonth.length; m++) {
					if (m >= modelist.size()) {
					} else if (!"".equals(model[m][4])) {
						newItem.setLabel(model[m][4]);
					}
				}
				for (int j = i; j < k + reportMonth.length; j++) {
					if (j >= modelist.size()) {
						Listcell dataCell = new Listcell();
						dataCell.setLabel("0");
						dataCell.setParent(newItem);
					} else {
						String count = String.valueOf(model[j][0]);
						allCount += Integer.valueOf(count);
						if (count.equals("0")) {
							Listcell dataCell = new Listcell();
							dataCell.setLabel(count);
							dataCell.setParent(newItem);
						} else {
							Listcell dataCell = new Listcell();
							A acell = new A(count);
							acell.setParent(dataCell);
							acell.setZclass(model[j][3] + ":" + model[j][5] + "-" + model[j][6]);
							acell.addEventListener(Events.ON_CLICK, new ErmResUnitUseOnClick());
							dataCell.setParent(newItem);
						}
					}
					i = i + 1;
				}
				if (allCount != 0) {
					newItem.setParent(dataLbx);
				}
			}
			// item2.setParent(dataLbx);
		} catch (WrongValueException e) {
			log.error("送出異常" + e);
		}
	}

	public EventListener<? extends Event> event() {
		try {
			ermResMonthUseWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResMonthUseReport/ermResMonthUseNumber.zul",
					ermResMonthUseWin, null);
			ermResMonthUseWin.doModal();
		} catch (Exception e) {
			log.error("數據庫控件異常" + e);
		}
		return null;
	}

	public void doEncoding(String fileName, String flnm) {
		// 編碼後文件名
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(flnm, "UTF-8");// 轉換字符編碼
			Filedownload.save(new FileInputStream(fileName), "application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String[]> getDtaList() throws InterruptedException {
		// 判斷radio是否選中
		if (reportType.getSelectedIndex() == -1) {
			ZkUtils.showInformation(Labels.getLabel("ermResReport.reportType") + Labels.getLabel("ermResReport.notNull"), Labels.getLabel("info"));
			return null;
		}
		String tempreportType = reportType.getSelectedItem().getValue();
		int firstYear = Integer.valueOf((String) firstyear.getSelectedItem().getValue());// 第一個年份
		int secondYear = Integer.valueOf((String) secondyear.getSelectedItem().getValue());// 第二個年份
		if (firstYear > secondYear) {
			ZkUtils.showInformation(Labels.getLabel("ermResUserList.range"), Labels.getLabel("info"));
			return null;
		}
		Session session = Sessions.getCurrent();
		String tempResType = resType.getSelectedItem().getValue();
		String resName = null;
		if (resTitle != null) {
			resName = resTitle.getValue();
		} else {
			resName = "";
		}
		if (unitsLbx.getItemCount() == 0) {
			ZkUtils.showInformation(Labels.getLabel("select") + Labels.getLabel("ermResUserList.unit"), Labels.getLabel("info"));
			return null;
		}
		List<Listitem> dataBaseList = null;
		if (dataBasesLbx.getItemCount() != 0) {
			dataBaseList = dataBasesLbx.getItems();
		}
		String[] monthArray = null;// 存儲需要匯出的所有月份
		String[] yearArray = null;// 存儲需要匯出的所有年份
		List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
		List<Listitem> unitList = unitsLbx.getItems();
		String[] webOrgName = new String[unitList.size()];
		int a = 0;
		for (Listitem employee : unitList) {
			String unitName = employee.getValue();
			webOrgName[a] = unitName;
			a = a + 1;
		}
		String[] dataBase = null;
		if (dataBaseList != null) {
			dataBase = new String[dataBaseList.size()];
			int b = 0;
			for (Listitem employee : dataBaseList) {
				String database = employee.getValue();
				dataBase[b] = database;
				b = b + 1;
			}
		}
		session.setAttribute("webOrgName", webOrgName);
		session.setAttribute("dataBase", dataBase);
		session.setAttribute("tempResType", tempResType);
		if (tempreportType.equals("1")) {
			String tempfirstYear = String.valueOf(firstYear);
			String tempsecondYear = String.valueOf(secondYear);
			String tempfirstMonth = firstmonth.getSelectedItem().getLabel();
			String tempsecondMonth = secondmonth.getSelectedItem().getLabel();
			if (firstYear < secondYear) {// 如果不是同一年
				int yearIndex = 0;
				// 如果不是相鄰的年份
				if (secondYear - firstYear > 1) {
					yearIndex = secondYear - firstYear - 1;
				}
				int firstNum = Integer.parseInt(tempfirstMonth);// 第一個區間內的月份（轉化成整形）
				int allMonth = 12 - firstNum + Integer.parseInt(tempsecondMonth) + (yearIndex * 12);
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
				int fMonth = Integer.parseInt(tempfirstMonth);
				int sMonth = Integer.parseInt(tempsecondMonth);
				// 年份相同，判斷第一個月份是否大於第二個月份，如果大於則提示第一個日期應該小於第二個日期
				if (fMonth > sMonth) {
					ZkUtils.showInformation(Labels.getLabel("ermResReport.monthType") + Labels.getLabel("ermResReport.typeError"),
							Labels.getLabel("info"));
					return null;
				} else {
					if (fMonth == sMonth) {
						monthArray = new String[1];
						monthArray[0] = tempfirstMonth;
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
					monthArray[i] = String.valueOf(years) + "0" + String.valueOf(monthArray[i]);
				} else {
					if (monthArray[i].equals("12")) {
						monthArray[i] = String.valueOf(years) + String.valueOf(monthArray[i]);
						years++;
					} else {
						monthArray[i] = String.valueOf(years) + String.valueOf(monthArray[i]);
					}
				}

			}
			// 將報表所需的月份集賦值給reportMonth
			reportMonth = monthArray.clone();
			monthArray.clone();

			if (tempfirstMonth.length() == 1) {
				tempfirstMonth = "0" + tempfirstMonth;
			}
			if (tempsecondMonth.length() == 1) {
				tempsecondMonth = "0" + tempsecondMonth;
			}
			String tempstartDate = tempfirstYear + tempfirstMonth;
			String tempendDate = tempsecondYear + tempsecondMonth;
			session.setAttribute("startDate", tempstartDate);
			session.setAttribute("endDate", tempendDate);
			String[] allTitle = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findResIdAll(tempstartDate, tempendDate,
					resName);
			if (allTitle == null) {
				return dataList;
			}
			for (int i = 0; i < allTitle.length; i++) {
				String title = allTitle[i];
				if (reportMonth.length == 1) {
					if (dataBaseList == null) {
						String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
								reportMonth[0], String.valueOf(Integer.valueOf(reportMonth[0]) + 1), webOrgName, title, dataBase);
						if (ermResUser[3] != null) {
							if (tempResType.indexOf("DB") != -1) {
								if (ermResUser[3].startsWith("DB")) {
									dataList.add(ermResUser);
								}
							} else {
								if (ermResUser[3].startsWith("EJ")) {
									dataList.add(ermResUser);
								}
							}
						}
					} else {
						String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
								reportMonth[0], String.valueOf(Integer.valueOf(reportMonth[0]) + 1), webOrgName, title, dataBase);
						if (ermResUser[3] != null) {
							if (ermResUser[3].startsWith("EJ")) {
								dataList.add(ermResUser);
							}
						}
					}
				} else {
					for (int k = 0; k < reportMonth.length; k++) {
						if (dataBaseList == null) {
							String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
									reportMonth[k], String.valueOf(Integer.valueOf(reportMonth[k]) + 1), webOrgName, title, dataBase);
							if (ermResUser[3] != null) {
								if (tempResType.indexOf("DB") != -1) {
									if (ermResUser[3].startsWith("DB")) {
										dataList.add(ermResUser);
									}
								} else {
									if (ermResUser[3].startsWith("EJ")) {
										dataList.add(ermResUser);
									}
								}
							}
						} else {
							String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
									reportMonth[k], String.valueOf(Integer.valueOf(reportMonth[k]) + 1), webOrgName, title, dataBase);
							if (ermResUser[3] != null) {
								if (ermResUser[3].startsWith("EJ")) {
									dataList.add(ermResUser);
								}
							}
						}
					}
				}
			}
		} else {
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
			yearArray.clone();
			String tempstartDate = String.valueOf(firstYear) + "01";
			String tempendDate = String.valueOf(secondYear) + "12";
			session.setAttribute("startDate", tempstartDate);
			session.setAttribute("endDate", tempendDate);
			String[] allTitle = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findResIdAll(tempstartDate, tempendDate,
					resName);
			for (int i = 0; i < allTitle.length; i++) {
				String title = allTitle[i];
				for (int k = 0; k < reportMonth.length; k++) {
					if (dataBaseList == null) {
						String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
								reportMonth[k] + "01", reportMonth[k] + "12", webOrgName, title, dataBase);
						if (tempResType.indexOf("DB") != -1) {
							if (ermResUser[3].startsWith("DB")) {
								dataList.add(ermResUser);
							}
						} else {
							if (ermResUser[3].startsWith("EJ")) {
								dataList.add(ermResUser);
							}
						}

					} else {

						String[] ermResUser = ((ErmResMonthUseService) SpringUtil.getBean("ermResMonthUseService")).findErmResourcesRsconAll(
								reportMonth[k] + "01", reportMonth[k] + "12", webOrgName, title, dataBase);
						if (ermResUser[3].startsWith("EJ")) {
							dataList.add(ermResUser);
						}
					}
				}
			}
		}

		return dataList;
	}

	@Listen("onClick = #dataBaseBtn")
	public void dataBase() {
		try {
			String tempResType = resType.getSelectedItem().getValue();
			if (tempResType.indexOf("EJ") != -1) {
				ermResMonthUseDBWin = (Window) ZkUtils.createComponents(
						"/WEB-INF/pages/system/report/ermResMonthUseReport/ermResMonthUseDataBase.zul", ermResMonthUseWin, null);
				ermResMonthUseDBWin.doModal();
			}
		} catch (Exception e) {
			log.error("數據庫控件異常" + e);
		}
	}

	@Listen("onClick = #unitBtn")
	public void unit() {
		try {
			ermResMonthUseUnitWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResMonthUseReport/ermResMonthUseUnit.zul",
					ermResMonthUseWin, null);
			ermResMonthUseUnitWin.doModal();
		} catch (Exception e) {
			log.error("單位控件異常" + e);
		}
	}

	class ErmResUnitUseOnClick implements EventListener {

		public void onEvent(Event event) throws Exception {
			try {
				A a = (A) event.getTarget();
				String all = a.getZclass();
				String resId = all.substring(0, all.indexOf(":"));
				String tempstartDateDbx = all.substring(all.indexOf(":") + 1, all.indexOf("-"));
				String tempendDateDbx = all.substring(all.indexOf("-") + 1);
				Window ermResUserN = new Window();
				Map<String, String> map = new HashMap<String, String>();
				map.put("resId", resId);
				map.put("tempstartDateDbx", tempstartDateDbx);
				map.put("tempendDateDbx", tempendDateDbx);
				ermResUserN = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResMonthUseReport/ermResMonthUseNumber.zul",
						ermResMonthUseWin, map);
				ermResUserN.doModal();
			} catch (Exception e) {
				log.error("詳細頁面控件異常", e);
			}
		}
	}

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

	@Listen("onChange = #resType")
	public void resType() {
		dataBasesLbx.getChildren().clear();
		dataBasesLbx.setVisible(false);
		String tempResType = resType.getSelectedItem().getValue();
		if (tempResType.indexOf("EJ") != -1) {
			dataBaseLb.setVisible(true);
			dataBaseBtn.setVisible(true);
		} else {
			dataBaseLb.setVisible(false);
			dataBaseBtn.setVisible(false);
		}
	}

}
