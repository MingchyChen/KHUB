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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_dataSource2;
import com.claridy.common.util.Resources_rsbatch_report2;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUnitUseService;
import com.claridy.facade.ErmResUserListService;

public class ErmResUnitUseListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1752752301804827327L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Window ermResUnitUseWin;
	@Wire
	private Window ermResUnitUseDBWin;
	@Wire
	private Window ermResUnitUseUnitWin;
	@Wire
	private Window ermResUserNumberWin;
	@Wire
	private Datebox startDateDbx;
	@Wire
	private Datebox endDateDbx;
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

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
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
			if (dataBasesLbx.getContext() == null || dataBasesLbx.getContext() == "") {
				dataBasesLbx.setVisible(false);
			}
			if (unitsLbx.getContext() == null || unitsLbx.getContext() == "") {
				unitsLbx.setVisible(false);
			}
			startDateDbx.setValue(new Date());
			endDateDbx.setValue(new Date());
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
			if (dataLbx.getChildren().size() < 1) {
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
				try {
					Listitem listitem = (Listitem) dataLbx.getChildren().get(i);
					String[] values = new String[listitem.getChildren().size()];
					for (int j = 0; j < listitem.getChildren().size(); j++) {
						Listcell listcell = (Listcell) listitem.getChildren().get(j);
						if ("".equals(listcell.getLabel())) {
							A a = (A) (listcell.getFirstChild());
							values[j] = a.getLabel();
						} else {
							values[j] = listcell.getLabel();
						}
					}
					unitUseList.add(values);
				} catch (Exception e) {
					Listhead listitem = (Listhead) dataLbx.getChildren().get(i);
					String[] values = new String[listitem.getChildren().size()];
					for (int j = 0; j < listitem.getChildren().size(); j++) {
						Listcell listcell = (Listcell) listitem.getChildren().get(j);
						if ("".equals(listcell.getLabel())) {
							A a = (A) (listcell.getFirstChild());
							values[j] = a.getLabel();
						} else {
							values[j] = listcell.getLabel();
						}
					}
					unitUseList.add(values);
				}
			}
			String[] title = new String[webOrgName.size() + 1];
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
			String[] value = new String[webOrgName.size() + 1];
			value[0] = Labels.getLabel("report.ermUnitUseLabel");
			for (int i = 0; i < webOrgName.size(); i++) {
				value[i + 1] = webOrgName.get(i);
			}
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report2 rsbatch_report = new Resources_rsbatch_report2(Labels.getLabel("report.ermUnitUseList")
					+ Labels.getLabel("report.report"));
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
			String[][] model = new String[modelist.size()][10];
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
			List<Listitem> unitList = unitsLbx.getItems();
			ListModelList<String> webOrgName = new ListModelList<String>();
			for (Listitem employee : unitList) {
				String unitName = employee.getValue();
				webOrgName.add(unitName);
			}
			Listhead listhead = new Listhead();
			Listheader listheader = new Listheader();
			listheader.setLabel(Labels.getLabel("report.ermUnitUseLabel"));
			listheader.setSort("auto");
			listheader.setParent(listhead);
			for (int i = 0; i < webOrgName.size(); i++) {
				listheader = new Listheader();
				listheader.setLabel(webOrgName.get(i));
				listheader.setSort("auto");
				listheader.setParent(listhead);
			}
			listhead.setParent(dataLbx);
			int k = 0;
			for (int i = 0; i < modelist.size();) {
				Listitem newItem = new Listitem();
				newItem.setLabel(model[i][4]);
				k = i;
				int allCount = 0;
				for (int j = i; j < k + webOrgName.size(); j++) {
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
						acell.setZclass(model[j][2] + "-" + model[j][3]);
						acell.addEventListener(Events.ON_CLICK, new ErmResUnitUseOnClick());
						dataCell.setParent(newItem);
					}
					i = i + 1;
				}
				if (allCount != 0) {
					newItem.setParent(dataLbx);
				}
			}
		} catch (WrongValueException e) {
			log.error("送出異常" + e);
		}
	}

	public EventListener<? extends Event> event() {
		try {
			ermResUserNumberWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResUnitUseReport/ermResUnitUseNumber.zul",
					ermResUserNumberWin, null);
			ermResUserNumberWin.doModal();
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
			// Filedownload
			Filedownload.save(new FileInputStream(fileName), "application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String[]> getDtaList() throws InterruptedException {
		Session session = Sessions.getCurrent();
		Date startDate = startDateDbx.getValue();
		Date endDate = endDateDbx.getValue();
		String tempResType = resType.getSelectedItem().getValue();
		String resName = null;
		if (resTitle != null) {
			resName = resTitle.getValue();
		} else {
			resName = "";
		}
		session.setAttribute("startDate", startDate);
		session.setAttribute("endDate", endDate);
		if (unitsLbx.getItemCount() == 0) {
			ZkUtils.showInformation(Labels.getLabel("select") + Labels.getLabel("ermResUserList.unit"), Labels.getLabel("info"));
			return null;
		}
		List<Listitem> dataBaseList = null;
		if (dataBasesLbx.getItemCount() != 0) {
			dataBaseList = dataBasesLbx.getItems();
		}
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String tempstartDateDbx = formatter.format(startDateDbx.getValue());
		String tempendDateDbx = formatter.format(endDateDbx.getValue());
		if (Integer.parseInt(tempstartDateDbx) > Integer.parseInt(tempendDateDbx)) {
			ZkUtils.showInformation(Labels.getLabel("ermResUserList.musterDate") + Labels.getLabel("ermResUserList.range"), Labels.getLabel("info"));
			return null;
		}
		String[] dataBase = null;
		if (dataBaseList != null) {
			dataBase = new String[dataBaseList.size()];
			int b = 0;
			for (Listitem data : dataBaseList) {
				String database = data.getValue();
				dataBase[b] = database;
				b = b + 1;
			}
		}
		List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
		List<Listitem> unitdataList = unitsLbx.getItems();
		String[] allTitle = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findResIdAll(tempstartDateDbx, tempendDateDbx,
				resName);
		if (allTitle.length == 0) {
			return null;
		}
		List<ErmResourcesMainfileV> ermResourcesMainfileVs2 = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findResAll();
		List<WebOrg> webOrgs2 = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findedOrgName(null);
		for (int i = 0; i < allTitle.length; i++) {
			for (Listitem employee : unitdataList) {
				String title = allTitle[i];
				String unitName = employee.getValue();
				if (dataBaseList == null) {
					String[] ermResUser = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findErmResourcesRsconAll(
							tempstartDateDbx, tempendDateDbx, unitName, title, dataBase, ermResourcesMainfileVs2, webOrgs2);
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
					String[] ermResUser = ((ErmResUnitUseService) SpringUtil.getBean("ermResUnitUseService")).findErmResourcesRsconAll(
							tempstartDateDbx, tempendDateDbx, unitName, title, dataBase, ermResourcesMainfileVs2, webOrgs2);
					if (ermResUser[3] != null) {
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
				ermResUnitUseDBWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResUnitUseReport/ermResUnitUseDataBase.zul",
						ermResUnitUseWin, null);
				ermResUnitUseDBWin.doModal();
			}
		} catch (Exception e) {
			log.error("數據庫控件異常" + e);
		}
	}

	@Listen("onClick = #unitBtn")
	public void unit() {
		try {
			ermResUnitUseUnitWin = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResUnitUseReport/ermResUnitUseUnit.zul",
					ermResUnitUseWin, null);
			ermResUnitUseUnitWin.doModal();
		} catch (Exception e) {
			log.error("單位控件異常" + e);
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

	class ErmResUnitUseOnClick implements EventListener {

		public void onEvent(Event event) throws Exception {
			try {
				A a = (A) event.getTarget();
				String condition = a.getZclass();
				String unit = condition.substring(0, condition.indexOf("-"));
				String resName = condition.substring(condition.indexOf("-") + 1);
				Window ermResUserN = new Window();
				Map<String, String> map = new HashMap<String, String>();
				map.put("unit", unit);
				map.put("resName", resName);
				ermResUserN = (Window) ZkUtils.createComponents("/WEB-INF/pages/system/report/ermResUnitUseReport/ermResUnitUseNumber.zul",
						ermResUserNumberWin, map);
				ermResUserN.doModal();
			} catch (Exception e) {
				log.error("詳細頁面控件異常" + e);
			}
		}
	}

}
