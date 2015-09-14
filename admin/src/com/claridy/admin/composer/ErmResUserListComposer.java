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
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;

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
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmPersonalizeRescount;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUserListService;

public class ErmResUserListComposer extends SelectorComposer<Component> {
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
	private Combobox sortType;
	@Wire
	private Radiogroup sort;
	@Wire
	private Combobox resType;
	@Wire
	private Textbox resTitle;
	@Wire
	private Listbox ResUserlistLix;
	@WireVariable
	private WebEmployee webEmployee;
	@WireVariable
	private List<Object> ermResUserList;
	@WireVariable
	private List<Object> ermResourcesMainfileVList;
	@WireVariable
	private ErmResourcesMainfileV ermResourcesMainfileV;
	@WireVariable
	private WebOrg webOrg;
	@WireVariable
	private WebAccount webAccount;
	@WireVariable
	private ErmCodeDb ermCodeDb;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			Comboitem select = new Comboitem();
			select.setLabel(Labels.getLabel("select"));
			select.setValue("0");
			// resType.appendChild(select);
			Comboitem db = new Comboitem();
			db.setLabel(Labels.getLabel("ermResUserList.electronicDatabase"));
			db.setValue("DB");
			resType.appendChild(db);
			Comboitem ej = new Comboitem();
			ej.setLabel(Labels.getLabel("ermResUserList.electronicPeriodicals"));
			ej.setValue("EJ");
			resType.appendChild(ej);
			resType.setSelectedIndex(0);
			Comboitem account = new Comboitem();
			account.setLabel(Labels.getLabel("ermResUserList.account"));
			account.setValue("accountid");
			sortType.appendChild(account);
			Comboitem musterDate = new Comboitem();
			musterDate.setLabel(Labels.getLabel("ermResUserList.musterDate"));
			musterDate.setValue("CONVERT(VARCHAR(8),createdate,112)");
			sortType.appendChild(musterDate);
			sortType.setSelectedIndex(0);
			sort.setSelectedIndex(0);
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
			String[] title = new String[8];
			title[0] = Labels.getLabel("ermResUserList.no");// No
			title[1] = Labels.getLabel("ermResUserList.account");// 使用者賬號
			title[2] = Labels.getLabel("ermResUserList.userName");// 使用者姓名
			title[3] = Labels.getLabel("ermResUserList.unit");// 單位
			title[4] = Labels.getLabel("ermResUserList.group");// 組室
			title[5] = Labels.getLabel("ermResUserList.resourceTitle");// 資源題名
			title[6] = Labels.getLabel("ermResUserList.dataBase");// 所屬資料庫
			title[7] = Labels.getLabel("ermResUserList.musterDate");// 點閱日期
			String[] value = { "no", "account", "userName", "unit", "group", "resourceTitle", "dataBase", "musterDate" };
			String tempResType = resType.getSelectedItem().getValue();
			List<ErmPersonalizeRescount> modelists = (List<ErmPersonalizeRescount>) ResUserlistLix.getListModel();
			List<String[]> modelist = new ArrayList<String[]>(1);
			if (modelists.size() < 1) {
				ZkUtils.showInformation(Labels.getLabel("report.select"), Labels.getLabel("info"));
				return;
			}
			for (int i = 0; i < modelists.size(); i++) {
				ErmPersonalizeRescount rescount = modelists.get(i);
				String[] strs = new String[8];
				strs[0] = String.valueOf(rescount.getShow1());
				strs[1] = String.valueOf(rescount.getShows2()) == null ? "" : rescount.getShows2();
				strs[2] = String.valueOf(rescount.getShows3()) == null ? "" : rescount.getShows3();
				strs[3] = String.valueOf(rescount.getShows4()) == null ? "" : rescount.getShows4();
				strs[4] = String.valueOf(rescount.getShows5()) == null ? "" : rescount.getShows5();
				strs[5] = String.valueOf(rescount.getShows6()) == null ? "" : rescount.getShows6();
				strs[6] = String.valueOf(rescount.getShows7()) == null ? "" : rescount.getShows7();
				strs[7] = String.valueOf(rescount.getShows8()) == null ? "" : rescount.getShows8();
				modelist.add(strs);
			}

			String restype = null;
			if ("DB".equals(tempResType)) {
				restype = Labels.getLabel("sysMenuResMainDbws");
			} else {
				restype = Labels.getLabel("sysMenuResMainEjeb");
			}
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String tempstartDateDbx = formatter.format(startDateDbx.getValue());
			String tempendDateDbx = formatter.format(endDateDbx.getValue());
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			Resources_rsbatch_report rsbatch_report = new Resources_rsbatch_report(Labels.getLabel("report.ermUserList")
					+ Labels.getLabel("report.report") + "\r" + Labels.getLabel("ermResUserList.musterDate") + ":" + tempstartDateDbx + "~"
					+ tempendDateDbx + "\r" + Labels.getLabel("ermResUserList.resourceType") + ":" + restype);
			JasperReport jasperReport = rsbatch_report.getJasperReport(title, value);
			JRDataSource dataSource = new Resources_rsbatch_dataSource(value, modelist);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
			jasperPrintList.add(jasperPrint);
			JExcelApiExporter excelExporter = new JExcelApiExporter();
			excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			// 報表路徑
			String realth = ((SystemProperties) SpringUtil.getBean("systemProperties")).systemDocumentPath + SystemVariable.RSBATCH_PATH;
			// 報表名稱
			String fileName = "resources_res_ermUserList_report.xls";
			excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realth + fileName);
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

	@Listen("onClick = #send")
	public void send() throws InterruptedException {
		try {
			List<ErmPersonalizeRescount> modelist = new ArrayList<ErmPersonalizeRescount>(1);
			modelist = getDtaList();
			if (modelist.size() < 1) {
				return;
			}
			ListModelList<ErmPersonalizeRescount> listModel = new ListModelList<ErmPersonalizeRescount>(modelist);
			ResUserlistLix.setModel(listModel);
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
		List<ErmPersonalizeRescount> newDataList = new ArrayList<ErmPersonalizeRescount>(1);// 儲存數據
		try {
			if (sortType.getSelectedItem().getValue() == "0") {
				ZkUtils.showInformation(Labels.getLabel("select") + Labels.getLabel("ermResUserList.firstSort"), Labels.getLabel("info"));
				return null;
			}
			// 判斷資源類型是否選中
			if (resType.getSelectedIndex() == -1) {
				ZkUtils.showInformation(Labels.getLabel("ermResUserList.write") + Labels.getLabel("ermResUserList.startDate"),
						Labels.getLabel("info"));
				return null;
			}
			// 判斷開始日期是否為空
			if (startDateDbx.getValue() == null) {
				ZkUtils.showInformation(Labels.getLabel("ermResReport.write") + Labels.getLabel("ermResUserList.startDate"), Labels.getLabel("info"));
				return null;
			}
			// 判斷結束日期是否為空
			if (endDateDbx.getValue() == null) {
				ZkUtils.showInformation(Labels.getLabel("ermResUserList.write") + Labels.getLabel("ermResUserList.endDate"), Labels.getLabel("info"));
				return null;
			}
			// //獲取報表類型
			Format formatter = new SimpleDateFormat("yyyyMMdd");
			String tempstartDateDbx = formatter.format(startDateDbx.getValue());
			String tempendDateDbx = formatter.format(endDateDbx.getValue());
			if (Integer.parseInt(tempstartDateDbx) > Integer.parseInt(tempendDateDbx)) {
				ZkUtils.showInformation(Labels.getLabel("ermResUserList.range"), Labels.getLabel("info"));
				return null;
			}
			String tempResType = resType.getSelectedItem().getValue();
			String tempSortType = sortType.getSelectedItem().getValue();
			String tempSort = sort.getSelectedItem().getValue();
			ermResUserList = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findErmResourcesRsconAll(tempstartDateDbx,
					tempendDateDbx, tempSortType, tempSort);
			List<Object[]> ermResTypeList = new ArrayList<Object[]>();
			for (int i = 0; i < ermResUserList.size(); i++) {
				Object[] data = new Object[8];
				Object[] obj = (Object[]) ermResUserList.get(i);
				if (resTitle.getValue() == null) {
					if (((String) obj[1]).indexOf(tempResType) != -1) {
						data[0] = obj[0];
						data[1] = obj[1];
						data[2] = obj[2];
						data[3] = obj[3];
						data[4] = obj[4];
						data[5] = obj[5];
					}
				} else if (resTitle.getValue() == "") {
					if (((String) obj[1]).indexOf(tempResType) != -1) {
						data[0] = obj[0];
						data[1] = obj[1];
						data[2] = obj[2];
						data[3] = obj[3];
						data[4] = obj[4];
						data[5] = obj[5];
					}
				} else {
					if (((String) obj[1]).indexOf(tempResType) != -1) {
						ermResourcesMainfileVList = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findResName(
								(String) obj[1], (String) obj[3]);
						if (ermResourcesMainfileVList.size() > 0) {
							ermResourcesMainfileV = (ErmResourcesMainfileV) ermResourcesMainfileVList.get(0);
							if (ermResourcesMainfileV.getTitle().toLowerCase().indexOf(resTitle.getValue().toString().toLowerCase()) != -1) {
								data[0] = obj[0];
								data[1] = obj[1];
								data[2] = obj[2];
								data[3] = obj[3];
								data[4] = obj[4];
								data[5] = obj[5];
							}
						}
					}
				}
				ermResTypeList.add(data);
			}
			// 加載數據到Grid中
			List<String[]> dataList = new ArrayList<String[]>();// 儲存數據
			for (int i = 0; i < ermResTypeList.size(); i++) {
				Object[] obj = (Object[]) ermResTypeList.get(i);
				String accountId = (String) obj[1];
				if (accountId != null && accountId != "") {
					String[] data = new String[8];
					data[0] = String.valueOf(0);
					data[1] = (String) obj[0];
					webAccount = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findAccountName((String) obj[0]);
					data[2] = webAccount.getNameZhTw();
					webOrg = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgIdParent((String) obj[4]);
					data[3] = webOrg.getOrgName();
					webOrg = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findOrgName((String) obj[5]);
					if (webOrg != null && !"".equals(webOrg)) {
						data[4] = webOrg.getOrgName();
					} else {
						data[4] = "";
					}

					ermResourcesMainfileVList = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findResName((String) obj[1],
							(String) obj[3]);
					if (ermResourcesMainfileVList.size() > 0) {
						ermResourcesMainfileV = (ErmResourcesMainfileV) ermResourcesMainfileVList.get(0);
						data[5] = ermResourcesMainfileV.getTitle();
					}
					ermCodeDb = ((ErmResUserListService) SpringUtil.getBean("ermResUserListService")).findDb((String) obj[3]);
					if (ermCodeDb != null) {
						data[6] = ermCodeDb.getName();
					}
					data[7] = (String) obj[2];
					dataList.add(data);
				}
			}

			for (int i = 0; i < dataList.size(); i++) {
				String[] data = dataList.get(i);
				ErmPersonalizeRescount rescount = new ErmPersonalizeRescount();
				rescount.setShow1((i + 1));
				rescount.setShows2(data[1]);
				rescount.setShows3(data[2]);
				rescount.setShows4(data[3]);
				rescount.setShows5(data[4]);
				rescount.setShows6(data[5]);
				rescount.setShows7(data[6]);
				String year = data[7].substring(0, 4);
				String month = data[7].substring(4, 6);
				String day = data[7].substring(6);
				rescount.setShows8(year + "/" + month + "/" + day);
				newDataList.add(rescount);
			}
		} catch (Exception e) {
			log.error("getDtaList:" + e);
		}
		return newDataList;
	}
}