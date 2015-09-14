package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.Resources_rsbatch_dataSource;
import com.claridy.common.util.Resources_rsbatch_report;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmCategoryService;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmResMainDbwsService;
import com.claridy.facade.ErmResMainEjebService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;
import com.claridy.facade.WebOrgListService;

/**
 * 
 * sunchao nj 電子資料庫/網路資源 2014/08/06
 */
public class ErmResMainDbwsListComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -4495610820701239659L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	/*
	 * //資源類型
	 * 
	 * @Wire private Combobox resourcesCbx;
	 */
	// 題名
	@Wire
	private Textbox titleBox;
	@Wire
	// 主題
	private Textbox subjectIdBox;
	// 提供單位
	@Wire
	private Combobox providerCbx;
	// 語言
	@Wire
	private Textbox langTbx;
	@Wire
	private Button searchLangBtn;
	// 出版商
	@Wire
	private Textbox pubTbx;
	@Wire
	private Button searchPubBtn;
	// 代理商
	@Wire
	private Textbox agenTbx;
	@Wire
	private Button searchAgenBtn;
	// 起訂日期
	@Wire
	private Datebox startDateDbx;
	// 迄訂日期
	@Wire
	private Datebox endDateDbx;
	// 採購注記
	@Wire
	private Combobox remarkCbx;
	// 狀態
	@Wire
	private Combobox stateCbx;
	@Wire
	private Comboitem stateYes;
	@Wire
	private Comboitem stateNo;
	// 停用註記
	@Wire
	private Radiogroup historyRgp;
	@Wire
	private Radio historyYes;
	@Wire
	private Radio historyNo;
	@Wire
	private Label languageSearch;
	@Wire
	private Label publisherSearch;
	@Wire
	private Label agentedSearch;
	// 儲存solr的sql
	String solrQuerySQL = "";
	@Wire
	private Listbox resMainDbwsLix;
	@Wire
	private Window ermResMainDbwsWin;
	@Wire
	private Textbox subjectNameBox;
	@WireVariable
	private List<ErmResourcesMainDbws> resMainDbwsList;
	private WebEmployee webEmployee;
	public static boolean iscanOpen = true;
	public static boolean iscanLoad = true;
	private List<ErmResourcesMainfileV> mainfileVsList;

	@Listen("onClick=#pagSearchBtn")
	public void search() {
		// solr查詢語句
		String solrQuery = "";
		try {
			solrQuery += "type_id:DB";
			// 語言
			if (languageSearch.getValue() != null
					&& !languageSearch.getValue().equals("")) {
				solrQuery += " language_id:" + languageSearch.getValue();
			}
			// 出版商
			if (publisherSearch.getValue() != null
					&& !publisherSearch.getValue().equals("")) {
				solrQuery += " publisher_id:" + publisherSearch.getValue();
			}
			// 代理商
			if (agentedSearch.getValue() != null
					&& !agentedSearch.getValue().equals("")) {
				solrQuery += " agented_id:" + agentedSearch.getValue();
			}
			// 主題
			if (subjectIdBox.getValue() != null
					&& !subjectIdBox.getValue().equals("")) {
				solrQuery += " subject_id:" + subjectIdBox.getValue();
			}
			// 提供單位
			if (providerCbx.getSelectedItem() != null
					&& !providerCbx.getSelectedItem().getValue().equals("")) {
				solrQuery += " suunit_id:" + providerCbx.getSelectedItem().getValue();
			}
			// 判斷查詢條件題名是否有值
			if (!"".equals(titleBox.getValue().trim())) {
				String title = titleBox.getValue().trim().toLowerCase();// 題名
				title = title
						.replaceAll("[\\(\\)\\<\\>\\[\\]\\{\\}]", "\\\\$0");
				if ("".equals(solrQuery)) {
					solrQuery += "(title:" + title + "*";
					solrQuery += " OR title:" + title + ")";
				} else {
					solrQuery += " (title:" + title + "*";
					solrQuery += " OR title:" + title + ")";
				}
			}
			// 判斷查詢條件起訂日期是否有值
			if (!"".equals(startDateDbx.getText())) {
				// 起訂日期
				String starOrderDate = new SimpleDateFormat("yyyy-MM-dd")
						.format(startDateDbx.getValue());
				if ("".equals(solrQuery)) {
					solrQuery += "starorderdate:[" + starOrderDate
							+ "T00:00:00.000Z-8HOUR TO " + starOrderDate
							+ "T00:00:00.000Z-8HOUR]";
				} else {
					solrQuery += " starorderdate:[" + starOrderDate
							+ "T00:00:00.000Z-8HOUR TO " + starOrderDate
							+ "T00:00:00.000Z-8HOUR]";
				}
			}
			// 判斷查詢條件迄訂日期是否有值
			if (!"".equals(endDateDbx.getText())) {
				// 迄訂日期
				String endorderdate = new SimpleDateFormat("yyyy-MM-dd")
						.format(endDateDbx.getValue());
				if ("".equals(solrQuery)) {
					solrQuery += "endorderdate:[" + endorderdate
							+ "T00:00:00.000Z-8HOUR TO " + endorderdate
							+ "T00:00:00.000Z-8HOUR]";
				} else {
					solrQuery += " endorderdate:[" + endorderdate
							+ "T00:00:00.000Z-8HOUR TO " + endorderdate
							+ "T00:00:00.000Z-8HOUR]";
				}
			}
			// 判斷查詢條件採購注記是否有值
			if (remarkCbx.getSelectedItem() != null
					&& !"".equals(remarkCbx.getSelectedItem().getValue()
							.toString().trim())) {
				String remarkId = remarkCbx.getSelectedItem().getValue()
						.toString().trim();// 採購注記
				solrQuery += " remark_id:" + remarkId;
			}
			// 根據停用注記查
			if (historyNo.isChecked()) {
				solrQuery += (" !history:Y");
			} else {
				solrQuery += " history:Y";
			}

			// 狀態
			if (!"".equals(stateCbx.getSelectedItem().getValue())) {
				solrQuery += " state:" + stateCbx.getSelectedItem().getValue();
			}
			// 將查詢的sql語句儲存起來
			solrQuerySQL = solrQuery;
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			List<ErmResourcesMainfileV> resMainDbwsList = ((ResourcesMainDbwsSolrSearch) SpringUtil
					.getBean("resourcesMainDbwsSolrSearch"))
					.resourcesMainSearch(solrQuerySQL);
			mainfileVsList=resMainDbwsList;
			ListModelList<ErmResourcesMainfileV> listModel = new ListModelList<ErmResourcesMainfileV>(
					resMainDbwsList);
			listModel.setMultiple(true);
			resMainDbwsLix.setModel(listModel);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢電子資料庫/網路資源集合出錯", e);
		}
	}

	@Listen("onClick=#addBtn")
	public void toAddErmResMainDbws() {
		Window ermResMainDbwsAddWin = (Window) ZkUtils.createComponents(
				"/WEB-INF/pages/system/ermResMainDbws/ermResMainDbwsAdd.zul",
				null, null);
		ermResMainDbwsAddWin.doModal();
		// String toUrl="ermResMainDbws/ermResMainDbwsAdd.zul";
		// ZkUtils.refurbishMethod(toUrl);
		// ermResMainDbwsWin.detach();
	}

	@Listen("onClick=#showAllBtn")
	public void showAll() {
		// solr查詢語句
		try {
			String solrQuery = "";
			solrQuery += "type_id:DB";
			// 根據停用注記查
			if (historyNo.isChecked()) {
				solrQuery += (" !history:Y");
			} else {
				solrQuery += " history:Y";
			}
			// 將查詢的sql語句儲存起來
			solrQuerySQL = solrQuery;
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmResourcesMainfileV> resMainDbwsList = ((ResourcesMainDbwsSolrSearch) SpringUtil
					.getBean("resourcesMainDbwsSolrSearch"))
					.resourcesMainSearch(solrQuerySQL);
			mainfileVsList=resMainDbwsList;
			ListModelList<ErmResourcesMainfileV> listModel = new ListModelList<ErmResourcesMainfileV>(
					resMainDbwsList);
			listModel.setMultiple(true);
			resMainDbwsLix.setModel(listModel);
			titleBox.setValue("");
			langTbx.setValue("");
			languageSearch.setValue("");
			pubTbx.setValue("");
			publisherSearch.setValue("");
			agenTbx.setValue("");
			agentedSearch.setValue("");
			startDateDbx.setValue(null);
			endDateDbx.setValue(null);
			remarkCbx.setSelectedIndex(0);
			stateCbx.setSelectedIndex(0);
			subjectNameBox.setValue("");
			subjectIdBox.setValue("");
			providerCbx.setSelectedIndex(0);
			//historyRgp.setSelectedIndex(0);
		} catch (ParseException e) {
			//e.printStackTrace();
			log.error("顯示全部異常+e");
		}
		// search();
	}
	

	@Listen("onClick=#clearBtn")
	public void clearTj() {
		titleBox.setValue("");
		langTbx.setValue("");
		languageSearch.setValue("");
		pubTbx.setValue("");
		publisherSearch.setValue("");
		agenTbx.setValue("");
		agentedSearch.setValue("");
		startDateDbx.setValue(null);
		endDateDbx.setValue(null);
		remarkCbx.setSelectedIndex(0);
		stateCbx.setSelectedIndex(0);
		subjectNameBox.setValue("");
		subjectIdBox.setValue("");
		providerCbx.setSelectedIndex(0);
	}

	@Listen("onClick=#searchLangBtn")
	public void searchLanguage() {
		String langType = "DBLAN";
		/*
		 * if
		 * ("WS".equals(resourcesCbx.getSelectedItem().getValue().toString())) {
		 * langType="RELAN"; }
		 */
		openSearch("language", langType);
	}

	@Listen("onClick=#searchPubBtn")
	public void searchPublisher() {
		openSearch("publisher", "");
	}

	@Listen("onClick=#searchAgenBtn")
	public void searchAgented() {
		openSearch("agented", "");
	}

	public void openSearch(String openValue, String openType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("openValue", openValue);
		map.put("openType", openType);
		if (openValue.trim().equals("language")) {
			Executions.createComponents(
					"/WEB-INF/pages/system/ermResMainDbws/ermResOpen.zul",
					this.getSelf(), map);
		} else {
			Executions.createComponents(
					"/WEB-INF/pages/system/ermResMainDbws/ermResPubOpen.zul",
					this.getSelf(), map);
		}
	}

	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			/*
			 * //查詢資源類別 List<ErmCodeGeneralCode>
			 * generalCodeLst=((ErmCategoryService) SpringUtil
			 * .getBean("ermCategoryService"
			 * )).findGeneralCodeByItemId("RETYPE"); for(ErmCodeGeneralCode
			 * generalCodeTmp:generalCodeLst){
			 * if(generalCodeTmp.getGeneralcodeId
			 * ().trim().toUpperCase().equals("DB"
			 * )||generalCodeTmp.getGeneralcodeId
			 * ().trim().toUpperCase().equals("WS")){ Comboitem com=new
			 * Comboitem(); com.setLabel(generalCodeTmp.getName1());
			 * com.setValue(generalCodeTmp.getGeneralcodeId());
			 * resourcesCbx.appendChild(com); } }
			 * resourcesCbx.setSelectedIndex(0);
			 */
			// 查詢採購註記
			List<ErmCodeGeneralCode> remarkLst = ((ErmCategoryService) SpringUtil
					.getBean("ermCategoryService"))
					.findGeneralCodeByItemId("PURE");
			Comboitem remarkCom = new Comboitem();
			remarkCom.setLabel(Labels.getLabel("select"));
			remarkCom.setValue("");
			remarkCbx.appendChild(remarkCom);
			for (ErmCodeGeneralCode generalCodeTmp : remarkLst) {
				remarkCom = new Comboitem();
				remarkCom.setLabel(generalCodeTmp.getName1());
				remarkCom.setValue(generalCodeTmp.getGeneralcodeId());
				remarkCbx.appendChild(remarkCom);
			}
			remarkCbx.setSelectedIndex(0);
			// 查詢狀態
			List<ErmCodeGeneralCode> stateLst = ((ErmCategoryService) SpringUtil
					.getBean("ermCategoryService"))
					.findGeneralCodeByItemId("STATE");
			for (ErmCodeGeneralCode generalCodeTmp : stateLst) {
				Comboitem stateCom = new Comboitem();
				stateCom.setLabel(generalCodeTmp.getName1());
				stateCom.setValue(generalCodeTmp.getGeneralcodeId());
				stateCbx.appendChild(stateCom);
			}
			stateCbx.setSelectedIndex(0);

			List<WebOrg> providerList = ((WebOrgListService) SpringUtil
					.getBean("webOrgListService")).findWebOrgList();
			Comboitem provideCom = new Comboitem();
			provideCom.setLabel(Labels.getLabel("select"));
			provideCom.setValue("");
			providerCbx.appendChild(provideCom);
			for (WebOrg generalCodeTmp : providerList) {
				provideCom = new Comboitem();
				provideCom.setLabel(generalCodeTmp.getOrgName());
				provideCom.setValue(generalCodeTmp.getOrgId());
				providerCbx.appendChild(provideCom);
			}
			providerCbx.setSelectedIndex(0);
			search();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查詢電子資料庫/網路資源集合出錯", e);
		}
	}

	@Listen("onClick=#deleteBtn")
	public void deleteDbws() {
		int count = resMainDbwsLix.getSelectedCount();
		if (count > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener<Event>() {

						public void onEvent(Event event) throws Exception {
							int ckickButton = (Integer) event.getData();
							if (ckickButton == Messagebox.OK) {
								Set<Listitem> listitems = resMainDbwsLix
										.getSelectedItems();
								WebEmployee loginwebEmployee = (WebEmployee) ZkUtils
										.getSessionAttribute("webEmployee");
								for (Listitem listitem : listitems) {
//									ErmResourcesMainDbws ermResourcesMainfileDbwsV = listitem
//											.getValue();
									ErmResourcesMainfileV ermResourcesMainfileDbwsV=listitem.getValue();
									ErmResourcesMainDbws ermResourcesMainDbws = ((ErmResMainDbwsService) SpringUtil
											.getBean("ermResMainDbwsService"))
											.getResMainDbwsByResId(ermResourcesMainfileDbwsV
													.getResourcesId());
									// ermMainEjebItem.setIsDataEffid(0);
									// ermResourcesMainEjeb.setIsDataEffid(0);
									ermResourcesMainDbws.setHistory("Y");
									((ErmResMainDbwsService) SpringUtil
											.getBean("ermResMainDbwsService"))
											.UpdResMainDbws(ermResourcesMainDbws);
									ermResourcesMainfileDbwsV.setHistory("Y");
									((ResourcesMainDbwsSolrSearch) SpringUtil
											.getBean("resourcesMainDbwsSolrSearch"))
											.resources_main_dbws_editData(ermResourcesMainfileDbwsV
													.getResourcesId());
								}
								ZkUtils.refurbishMethod("ermResMainDbws/ermResMainDbws.zul");
							}

						}
					});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
					Labels.getLabel("info"));
			return;
		}
	}

	@Listen("onClick=#selectBtn")
	public void openSearch() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("openValue", "DBSUB");
		Executions.createComponents(
				"/WEB-INF/pages/system/ermResMainDbws/ermResSubOpen.zul",
				this.getSelf(), map);
	}
	@Listen("onClick=#eptExcelBtn")
	public void excelPrint(){
		try {
			int count = resMainDbwsLix.getSelectedCount();
			if (count > 0) {
				String[] title = { Labels.getLabel("ermResMainDbws.title"),
						Labels.getLabel("ermResMainDbws.endOrderDate"),
						Labels.getLabel("ermResMainDbws.remarkId"),
						Labels.getLabel("ermResMainDbws.languageId"),
						Labels.getLabel("ermResMainDbws.history") };
				String[] value = { "title", "endOrderDate", "remarkId",
						"languageId", "history" };
				Set<Listitem> listiem = resMainDbwsLix.getSelectedItems();
				/*String realPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ SystemVariable.ERM_RES_EJEBFILE_PATH;*/
				String realPath=((SystemProperties) SpringUtil
						.getBean("systemProperties")).systemDocumentPath+SystemVariable.RSBATCH_PATH;
				String fileName = "dbwsExcel.xls";
				List<String[]> dbwsList = new ArrayList<String[]>();
				List<ErmCodeGeneralCode> langCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("DBLAN");
				
				List<ErmCodeGeneralCode> remarkCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("PURE");
				for (Listitem ejeb : listiem) {
					ErmResourcesMainfileV tempEjebV = ejeb.getValue();
					String history="";
					if(tempEjebV.getHistory()!=null){
						if (tempEjebV.getHistory().toUpperCase().equals("N")) {
							//tempEjebV.setHistory("否");
							history=Labels.getLabel("no");
						} else {
							//tempEjebV.setHistory("是");
							history=Labels.getLabel("yes");
						}
					}else{
						history=Labels.getLabel("no");
					}
					for (int i = 0; i < langCodeList.size(); i++) {
						ErmCodeGeneralCode langCode = langCodeList.get(i);
						if (langCode.getGeneralcodeId().equals(
								tempEjebV.getLanguageId())) {
							tempEjebV.setLanguageCn(langCode.getName1());
						}
					}
					for (int i = 0; i < remarkCodeList.size(); i++) {
						ErmCodeGeneralCode remarkCode = remarkCodeList.get(i);
						if (remarkCode.getGeneralcodeId().equals(
								tempEjebV.getRemarkId())) {
							tempEjebV.setRemarkCn(remarkCode.getName1());
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					String dateT="";
					if(tempEjebV.getEndOrderDate()!=null){
						dateT=sdf.format(tempEjebV.getEndOrderDate());
					}
					String[] values = { tempEjebV.getTitle(),
							dateT, tempEjebV.getRemarkCn(),
							tempEjebV.getLanguageCn(),
							history };
					dbwsList.add(values);
				}
				List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
				Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report("電子資料庫報表");
				JasperReport jasperReport=rsbatch_report.getJasperReport(title, value);
				JRDataSource dataSource=new Resources_rsbatch_dataSource(value,dbwsList);
				JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
				jasperPrintList.add(jasperPrint);
				JExcelApiExporter excelExporter=new JExcelApiExporter();
				excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
				excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realPath+fileName);
				excelExporter.exportReport();
				//Executions.sendRedirect(SystemVariable.RSBATCH_PATH+fileName);
				doEncoding(realPath+fileName,fileName);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("selectOut"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("匯出文件異常",e);
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
			Filedownload.save(new FileInputStream(fileName),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Listen("onClick=#eptExcelAllBtn")
	public void excelAllPrint(){
		try {
				String[] title = { Labels.getLabel("ermResMainDbws.title"),
						Labels.getLabel("ermResMainDbws.endOrderDate"),
						Labels.getLabel("ermResMainDbws.remarkId"),
						Labels.getLabel("ermResMainDbws.languageId"),
						Labels.getLabel("ermResMainDbws.history") };
				String[] value = { "title", "endOrderDate", "remarkId",
						"languageId", "history" };
				//Set<Listitem> listiem = resMainDbwsLix.getSelectedItems();
				/*String realPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ SystemVariable.ERM_RES_EJEBFILE_PATH;*/
				String realPath=((SystemProperties) SpringUtil
						.getBean("systemProperties")).systemDocumentPath+SystemVariable.RSBATCH_PATH;
				String fileName = "dbwsAllExcel.xls";
				List<String[]> dbwsList = new ArrayList<String[]>();
				List<ErmCodeGeneralCode> langCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("DBLAN");
				
				List<ErmCodeGeneralCode> remarkCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("PURE");
				for (ErmResourcesMainfileV dbws : mainfileVsList) {
					ErmResourcesMainfileV tempEjebV = dbws;
					String history="";
					if(tempEjebV.getHistory()!=null){
						if (tempEjebV.getHistory().toUpperCase().equals("N")) {
							//tempEjebV.setHistory("否");
							history=Labels.getLabel("no");
						} else {
							//tempEjebV.setHistory("是");
							history=Labels.getLabel("yes");
						}
					}else{
						history=Labels.getLabel("no");
					}
					for (int i = 0; i < langCodeList.size(); i++) {
						ErmCodeGeneralCode langCode = langCodeList.get(i);
						if (langCode.getGeneralcodeId().equals(
								tempEjebV.getLanguageId())) {
							tempEjebV.setLanguageCn(langCode.getName1());
						}
					}
					for (int i = 0; i < remarkCodeList.size(); i++) {
						ErmCodeGeneralCode remarkCode = remarkCodeList.get(i);
						if (remarkCode.getGeneralcodeId().equals(
								tempEjebV.getRemarkId())) {
							tempEjebV.setRemarkCn(remarkCode.getName1());
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					String dateT="";
					if(tempEjebV.getEndOrderDate()!=null){
						dateT=sdf.format(tempEjebV.getEndOrderDate());
					}
					String[] values = { tempEjebV.getTitle(),
							dateT, tempEjebV.getRemarkCn(),
							tempEjebV.getLanguageCn(),
							history };
					dbwsList.add(values);
				}
				List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
				Resources_rsbatch_report rsbatch_report=new Resources_rsbatch_report("電子資料庫報表");
				JasperReport jasperReport=rsbatch_report.getJasperReport(title, value);
				JRDataSource dataSource=new Resources_rsbatch_dataSource(value,dbwsList);
				JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport, null,dataSource);
				jasperPrintList.add(jasperPrint);
				JExcelApiExporter excelExporter=new JExcelApiExporter();
				excelExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrintList);
				excelExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, realPath+fileName);
				excelExporter.exportReport();
				//Executions.sendRedirect(SystemVariable.RSBATCH_PATH+fileName);
				doEncoding(realPath+fileName,fileName);
		} catch (Exception e) {
			log.error("匯出文件異常",e);
		}
	}
}
