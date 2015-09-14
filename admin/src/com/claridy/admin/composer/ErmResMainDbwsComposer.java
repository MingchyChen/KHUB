package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesSuunit;
import com.claridy.domain.ErmResourcesUploadfile;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmResMainDbwsService;
import com.claridy.facade.ErmResourcesSuunitIdService;
import com.claridy.facade.ErmResourcesUploadfileService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * sunchao nj 電子資料庫/網路資源 2014/08/06
 */
public class ErmResMainDbwsComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String resourcesId;
	private WebEmployee webEmployee;
	private ErmResourcesMainfileV resourcesMainfile;
	@Wire
	private Radiogroup isOpen;
	@Wire
	private Textbox titleTxt;// 題名
	@Wire
	private Textbox urlTxt;// url
	@Wire
	private Textbox url2Txt;// 相關Url
	@Wire
	private Label languageSearch;// 語言
	@Wire
	private Textbox langTbx;
	@Wire
	private Label publisherSearch;// 出版商
	@Wire
	private Textbox pubTbx;
	@Wire
	private Button uploadFile;
	@Wire
	private Label agentedSearch;// 代理商
	@Wire
	private Textbox agenTbx;
	@Wire
	private Datebox startOrderDateDtx;// 起訂日期
	@Wire
	private Datebox endOrderDateDtx;// 起訂日期
	@Wire
	private Combobox procurementNoteCbx;// 採購註記
	@Wire
	private Radiogroup libaryMoneyRdo;// 圖書館經費
	@Wire
	private Textbox idPwdTxt;// 賬號與密碼
	@Wire
	private Textbox updateFrequency;// 更新頻率/刊期
	@Wire
	private Textbox instructionUseTxt;// 使用說明
	@Wire
	private Textbox onLineNumberTxt;// 同事線上人數
	@Wire
	private Combobox connectModelCbx;// 連線方式
	@Wire
	private Combobox locationCbx;// 存放地點
	@Wire
	private Textbox includedTxt;// 收錄年代
	@Wire
	private Textbox legitimateIPTxt;// 合法ip
	@Wire
	private Textbox versionTxt;// 版本
	@Wire
	private Textbox brief1CKtxt;// 資源簡述摘要
	@Wire
	private Textbox brief2CKtxt;// 資源簡述摘要(英文)
	@Wire
	private Textbox othersTxt;// 其他資訊
	@Wire
	private ErmSysNotifyConfig ermSysNotifyConfig;
	@Wire
	private Image imgAccountPic;// 圖片
	@Wire
	private ErmResourcesMainDbws ermResourcesMainDbws;
	@Wire
	private Button upload;
	@Wire
	private Window addErmResMainDbwsWin;
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Button delFile;
	@Wire
	private Window editErmResMainDbwsWin;
	@Wire
	private Listbox resMainSuunitLix;
	@Wire
	private Window ermResMainDbwsWin;
	private String currentPage;
	private String fileName;
	private String uploadFileName;
	private String solrQuerySQL = "";

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			int tempResourceIndex = 0;
			int tempreceiptIndex = 0;
			resourcesId = map.get("resourcesId");
			resourcesMainfile = ((ErmResMainDbwsService) SpringUtil
					.getBean("ermResMainDbwsService"))
					.getResMainfileByResId(resourcesId);
			if (resourcesMainfile != null) {
				currentPage = map.get("currentPage");
				List<ErmCodeGeneralCode> procuGeneralCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("PURE");
				Comboitem com = new Comboitem();
				com.setLabel(Labels.getLabel("ermSysNotifyConfig.noLimit"));
				com.setValue("0");
				procurementNoteCbx.appendChild(com);
				for (int i = 0; i < procuGeneralCodeList.size(); i++) {
					ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
					ermCodeGeneralCode = procuGeneralCodeList.get(i);
					com = new Comboitem();
					com.setLabel(ermCodeGeneralCode.getName1());
					com.setValue(ermCodeGeneralCode.getGeneralcodeId());
					procurementNoteCbx.appendChild(com);
					if (ermSysNotifyConfig != null
							&& ermSysNotifyConfig.getTypeId() != null) {
						if (ermCodeGeneralCode.getGeneralcodeId().equals(
								ermSysNotifyConfig.getTypeId())) {
							tempResourceIndex = i + 1;
						}
					}

				}
				procurementNoteCbx.setSelectedIndex(tempResourceIndex);

				List<ErmCodeGeneralCode> placeGeneralCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("PLACE");
				Comboitem com2 = new Comboitem();
				com2.setLabel(Labels.getLabel("ermSysNotifyConfig.noLimit"));
				com2.setValue("0");
				locationCbx.appendChild(com2);
				for (int i = 0; i < placeGeneralCodeList.size(); i++) {
					ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
					ermCodeGeneralCode = placeGeneralCodeList.get(i);
					com2 = new Comboitem();
					com2.setLabel(ermCodeGeneralCode.getName1());
					com2.setValue(ermCodeGeneralCode.getGeneralcodeId());
					locationCbx.appendChild(com2);
					if (ermSysNotifyConfig != null
							&& ermSysNotifyConfig.getTypeId() != null) {
						if (ermCodeGeneralCode.getGeneralcodeId().equals(
								ermSysNotifyConfig.getTypeId())) {
							tempResourceIndex = i + 1;
						}
					}

				}
				locationCbx.setSelectedIndex(tempResourceIndex);

				List<ErmCodeGeneralCode> connectGeneralCodeList = ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService"))
						.findErmCodeGeneralCodeByItemId("ACCESS");
				Comboitem com3 = new Comboitem();
				com3.setLabel(Labels.getLabel("ermSysNotifyConfig.noLimit"));
				com3.setValue("0");
				connectModelCbx.appendChild(com3);
				for (int i = 0; i < connectGeneralCodeList.size(); i++) {
					ErmCodeGeneralCode ermCodeGeneralCode = new ErmCodeGeneralCode();
					ermCodeGeneralCode = connectGeneralCodeList.get(i);
					com3 = new Comboitem();
					com3.setLabel(ermCodeGeneralCode.getName1());
					com3.setValue(ermCodeGeneralCode.getGeneralcodeId());
					connectModelCbx.appendChild(com3);
					if (ermSysNotifyConfig != null
							&& ermSysNotifyConfig.getTypeId() != null) {
						if (ermCodeGeneralCode.getGeneralcodeId().equals(
								ermSysNotifyConfig.getTypeId())) {
							tempResourceIndex = i + 1;
						}
					}

				}
				connectModelCbx.setSelectedIndex(tempResourceIndex);

				resourcesId = map.get("resourcesId");// ErmResMainDbwsService
				ermResourcesMainDbws = ((ErmResMainDbwsService) SpringUtil
						.getBean("ermResMainDbwsService"))
						.getResMainDbwsByResId(resourcesId);
				if (ermResourcesMainDbws != null) {
					currentPage = map.get("currentPage");
					if (ermResourcesMainDbws.getTitle() != null) {
						titleTxt.setValue(ermResourcesMainDbws.getTitle());// 標題
					}
					if (ermResourcesMainDbws.getUrl1() != null) {
						urlTxt.setValue(ermResourcesMainDbws.getUrl1());// url
					}
					if (ermResourcesMainDbws.getUrl2() != null) {
						url2Txt.setValue(ermResourcesMainDbws.getUrl2());// 相關Url
					}
					if (ermResourcesMainDbws.getLanguageId() != null
							&& !"".equals(ermResourcesMainDbws.getLanguageId())) {
						languageSearch.setValue(ermResourcesMainDbws
								.getLanguageId());
						ErmCodeGeneralCode langCode = ((ErmCodeGeneralCodeService) SpringUtil
								.getBean("ermCodeGeneralCodeService"))
								.findByItemIDAndGeneralcodeId("RELAN",
										ermResourcesMainDbws.getLanguageId());
						langTbx.setValue(langCode.getName1());// 語言
					}
					if (ermResourcesMainDbws.getPublisherId() != null
							&& !"".equals(ermResourcesMainDbws.getPublisherId())) {
						publisherSearch.setValue(ermResourcesMainDbws
								.getPublisherId());// 出版商
						ErmCodePublisher ermCodePublisher = ((CodePublisherService) SpringUtil
								.getBean("codePublisherService"))
								.findByPublisherID(ermResourcesMainDbws
										.getPublisherId());
						pubTbx.setValue(ermCodePublisher.getName());
					}
					if (ermResourcesMainDbws.getAgentedId() != null
							&& !"".equals(ermResourcesMainDbws.getAgentedId())) {// 代理商
						agentedSearch.setValue(ermResourcesMainDbws
								.getAgentedId());
						ErmCodePublisher ermCodeAgent = ((CodePublisherService) SpringUtil
								.getBean("codePublisherService"))
								.findByPublisherID(ermResourcesMainDbws
										.getAgentedId());
						agenTbx.setValue(ermCodeAgent.getName());
					}
					if (ermResourcesMainDbws.getStarOrderDate() != null) {// 起訂日期
						startOrderDateDtx.setValue(ermResourcesMainDbws
								.getStarOrderDate());
					}
					if (ermResourcesMainDbws.getEndOrderDate() != null) {// 訖訂日期
						endOrderDateDtx.setValue(ermResourcesMainDbws
								.getEndOrderDate());
					}
					if (ermResourcesMainDbws.getRemarkId() != null
							&& !"".equals(ermResourcesMainDbws.getRemarkId())) {// 採購註記
						procurementNoteCbx.setSelectedIndex(Integer
								.parseInt(ermResourcesMainDbws.getRemarkId()));
					}
					if (ermResourcesMainDbws.getLibaryMoney() != null) {// 圖書館經費
						libaryMoneyRdo
								.setSelectedIndex(Integer
										.parseInt(ermResourcesMainDbws
												.getLibaryMoney()));
					}
					if (ermResourcesMainDbws.getIdpwd() != null) {// 賬號密碼
						idPwdTxt.setValue(ermResourcesMainDbws.getIdpwd());
					}
					if (ermResourcesMainDbws.getFrenquency() != null) {// 更新頻率/刊期
						updateFrequency.setValue(ermResourcesMainDbws
								.getFrenquency());
					}
					if (ermResourcesMainDbws.getIntro() != null) {// 使用說明
						instructionUseTxt.setValue(ermResourcesMainDbws
								.getIntro());
					}
					if (ermResourcesMainDbws.getConcur() != null) {// 同時上線人數
						onLineNumberTxt.setValue(ermResourcesMainDbws
								.getConcur());
					}
					if (ermResourcesMainDbws.getConnectId() != null
							&& !"".equals(ermResourcesMainDbws.getConnectId())) {// 連線方式
						connectModelCbx.setSelectedIndex(Integer
								.parseInt(ermResourcesMainDbws.getConnectId()));
					}
					if (ermResourcesMainDbws.getPlaceId() != null
							&& !"".equals(ermResourcesMainDbws.getPlaceId())) {// 存放地點
						locationCbx.setSelectedIndex(Integer
								.parseInt(ermResourcesMainDbws.getPlaceId()));
					}
					if (ermResourcesMainDbws.getCoverage() != null
							&& !"".equals(ermResourcesMainDbws.getCoverage())) {// 搜錄年代
						includedTxt
								.setValue(ermResourcesMainDbws.getCoverage());
					}
					if (ermResourcesMainDbws.getRegllyIp() != null
							&& !"".equals(ermResourcesMainDbws.getRegllyIp())) {
						legitimateIPTxt.setValue(ermResourcesMainDbws
								.getRegllyIp());
					}
					if (ermResourcesMainDbws.getVersion() != null
							&& !"".equals(ermResourcesMainDbws.getVersion())) {
						versionTxt.setValue(ermResourcesMainDbws.getVersion());
					}
					if (ermResourcesMainDbws.getBrief1() != null
							&& !"".equals(ermResourcesMainDbws.getBrief1())) {
						brief1CKtxt.setValue(ermResourcesMainDbws.getBrief1());
					}
					if (ermResourcesMainDbws.getBrief2() != null
							&& !"".equals(ermResourcesMainDbws.getBrief2())) {
						brief2CKtxt.setValue(ermResourcesMainDbws.getBrief2());
					}
					if (ermResourcesMainDbws.getOthers() != null
							&& !"".equals(ermResourcesMainDbws.getOthers())) {
						othersTxt.setValue(ermResourcesMainDbws.getOthers());
					}
					if (ermResourcesMainDbws.getImgUrl() != null
							&& !"".equals(ermResourcesMainDbws.getImgUrl())) {
						// String src = ((SystemProperties) SpringUtil
						// .getBean("systemProperties")).ermMainEjebDocementPath
						// + resourcesMainfile.getImgurl();
						String portalDocementPath = ((SystemProperties) SpringUtil
								.getBean("systemProperties")).portalDocementPath
								+ "/" + SystemVariable.ERM_RESMAIN_DBWS_PATH;
						String src = portalDocementPath
								+ resourcesMainfile.getImgurl();
						try {
							AImage aImage = new AImage(src);
							imgAccountPic.setContent(aImage);
						} catch (Exception e) {
							log.error("圖片加載異常" + e);
						}
					}
					List<ErmResourcesUploadfile> ermResourcesUploadfileList = ((ErmResourcesUploadfileService) SpringUtil
							.getBean("ermResourcesUploadfileService"))
							.findByResourcesId(resourcesMainfile
									.getResourcesId());
					if (ermResourcesUploadfileList.size() > 0) {
						delFile.setVisible(true);
						uploadFile.setVisible(false);
						for (ErmResourcesUploadfile ermResourcesUploadfile : ermResourcesUploadfileList) {
							uploadFileNameTxt.setValue(ermResourcesUploadfile
									.getUploadFile());
							fileName = ermResourcesUploadfile.getUuid();
						}
					} else {
						delFile.setVisible(false);
						uploadFile.setVisible(true);
					}

				}
			}
			flushList();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("電子資料庫進入新增或修改頁面報錯", e);
		}
	}

	public void flushList() {
		List<WebOrg> orgList = ((WebOrgListService) SpringUtil
				.getBean("webOrgListService")).findWebOrgParam("", "");
		List<ErmResourcesSuunit> sunnitList = ((ErmResourcesSuunitIdService) SpringUtil
				.getBean("ermResourcesSuunitIdService"))
				.findBySuunitResId(resourcesId);
		for (ErmResourcesSuunit ermResourcesSuunit : sunnitList) {
			for (int i = 0; i < orgList.size(); i++) {
				if (ermResourcesSuunit.getSuunitId().equals(
						orgList.get(i).getOrgId())) {
					ermResourcesSuunit.setOrgName(orgList.get(i).getOrgName());
				}
			}
		}
		ListModelList<ErmResourcesSuunit> ermSunnitModel = new ListModelList<ErmResourcesSuunit>(
				sunnitList);
		ermSunnitModel.setMultiple(true);
		resMainSuunitLix.setModel(ermSunnitModel);
	}

	@Listen("onClick=#addSuunitBtn")
	public void openAddSunnit() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dbId", resourcesId);
		Executions.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResSuunitAdd.zul",
				this.getSelf(), map);
	}//

	@Listen("onClick=#delSuunitBtn")
	public void delSunnit() {
		int count = resMainSuunitLix.getSelectedCount();
		if (count > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener<Event>() {

						public void onEvent(Event event) throws Exception {
							int ckickButton = (Integer) event.getData();
							if (ckickButton == Messagebox.OK) {
								Set<Listitem> listitems = resMainSuunitLix
										.getSelectedItems();
								WebEmployee loginwebEmployee = (WebEmployee) ZkUtils
										.getSessionAttribute("webEmployee");
								for (Listitem listitem : listitems) {
									ErmResourcesSuunit ermResourcesSuunit = listitem
											.getValue();
									((ErmResourcesSuunitIdService) SpringUtil
											.getBean("ermResourcesSuunitIdService"))
											.delSunnit(ermResourcesSuunit);
								}
								flushList();
								try {
									((ResourcesMainDbwsSolrSearch) SpringUtil
											.getBean("resourcesMainDbwsSolrSearch"))
											.resources_main_dbws_editData(resourcesId);
								} catch (SQLException e) {
									log.error("solr更新電子資料庫報錯", e);
								}
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

	@Listen("onClick=#updBtn")
	public void updateResourcesDbws() {
		try {
			ErmResourcesMainDbws resourcesMainDbws = ((ErmResMainDbwsService) SpringUtil
					.getBean("ermResMainDbwsService"))
					.getResMainDbwsByResId(resourcesId);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			resourcesMainDbws.setLatelyChangedUser(webEmployee.getEmployeesn());
			resourcesMainDbws.setLatelyChangedDate(new Date());
			resourcesMainDbws.setLatelyChangedUser(webEmployee.getEmployeesn());
			resourcesMainDbws.setLatelyChangedDate(new Date());
			// List<ErmResourcesMainDbws> dbwsList=((ErmResMainDbwsService)
			// SpringUtil.getBean("ermResMainDbwsService")).findAllResMainDbwsList(ermResourcesMainDbws,webEmployee);
			// String tempId="DB";
			// Long number = (long) dbwsList.size();
			// number = number + 1;
			// // String resourcesId = RandomIDGenerator.getSerialNumber("EJ");
			// String resourcesId = tempId + RandomIDGenerator.fmtLong(number,
			// 9);

			ermResourcesMainDbws = resourcesMainDbws;
			String tempTitle = "";// 標題
			if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue())) {
				tempTitle = titleTxt.getValue();
				// tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
				ermResourcesMainDbws.setTitle(tempTitle);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermResMainDbws.title")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				titleTxt.focus();
				return;
			}
			String tempUrl = "";// url
			if (urlTxt.getValue() != null && !"".equals(urlTxt.getValue())) {
				tempUrl = urlTxt.getValue();
				tempUrl = XSSStringEncoder.encodeXSSString(tempUrl);
				ermResourcesMainDbws.setUrl1(tempUrl);
			} else {
				ZkUtils.showExclamation(
						"url" + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				urlTxt.focus();
				return;
			}
			String tempUrl2 = "";// 相關url
			if (url2Txt.getValue() != null && !"".equals(url2Txt.getValue())) {
				tempUrl2 = url2Txt.getValue();
				tempUrl2 = XSSStringEncoder.encodeXSSString(tempUrl2);
				ermResourcesMainDbws.setUrl2(tempUrl2);
			}
			String templanguage = "";
			if (langTbx.getValue() != null && !"".equals(langTbx.getValue())) {
				templanguage = languageSearch.getValue();
				templanguage = XSSStringEncoder.encodeXSSString(templanguage);
				ermResourcesMainDbws.setLanguageId(templanguage);
			}
			String tempPublisher = "";
			if (pubTbx.getValue() != null && !"".equals(pubTbx.getValue())) {
				tempPublisher = publisherSearch.getValue();
				tempPublisher = XSSStringEncoder.encodeXSSString(tempPublisher);
				ermResourcesMainDbws.setPublisherId(tempPublisher);
			}
			String tempAgent = "";
			if (agenTbx.getValue() != null && !"".equals(agenTbx.getValue())) {
				tempAgent = agentedSearch.getValue();
				tempAgent = XSSStringEncoder.encodeXSSString(tempAgent);
				ermResourcesMainDbws.setAgentedId(tempAgent);
			}
			Date tempStartOrderDate = new Date();
			if (startOrderDateDtx.getValue() != null
					&& !"".equals(startOrderDateDtx.getValue())) {
				tempStartOrderDate = startOrderDateDtx.getValue();
				ermResourcesMainDbws.setStarOrderDate(tempStartOrderDate);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermResMainEjeb.startDate") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				startOrderDateDtx.focus();
				return;
			}
			if (endOrderDateDtx.getValue() != null
					&& !"".equals(endOrderDateDtx.getValue())) {
				Date endOrderDate = endOrderDateDtx.getValue();
				Date startOrderDate = startOrderDateDtx.getValue();
				if (startOrderDate.after(endOrderDate)) {
					ZkUtils.showExclamation(Labels
							.getLabel("ermResMainEjeb.startDateNotDyEndDate"),
							Labels.getLabel("warn"));
					startOrderDateDtx.focus();
					return;
				}
			}
			Date tempEndOrderDate = new Date();
			if (endOrderDateDtx.getValue() != null
					&& !"".equals(endOrderDateDtx.getValue())) {
				tempEndOrderDate = endOrderDateDtx.getValue();
				ermResourcesMainDbws.setEndOrderDate(tempEndOrderDate);
			}

			// 採購註記
			String tempProcurementNote = "";
			if (procurementNoteCbx.getSelectedItem().getValue() != null
					&& !"".equals(procurementNoteCbx.getSelectedItem()
							.getValue())) {
				tempProcurementNote = procurementNoteCbx.getSelectedItem()
						.getValue();
				tempProcurementNote = XSSStringEncoder
						.encodeXSSString(tempProcurementNote);
				ermResourcesMainDbws.setRemarkId(tempProcurementNote);
			}
			// 圖書館經費
			String tempLibaryMoney = "";
			if (libaryMoneyRdo.getSelectedItem().getValue() != null
					&& !"".equals(libaryMoneyRdo.getSelectedItem().getValue())) {
				tempLibaryMoney = libaryMoneyRdo.getSelectedItem().getValue();
				tempLibaryMoney = XSSStringEncoder
						.encodeXSSString(tempLibaryMoney);
				ermResourcesMainDbws.setLibaryMoney(tempLibaryMoney);
			}
			// 賬號與密碼
			String tempIdPwd = "";
			if (idPwdTxt.getValue() != null && !"".equals(idPwdTxt.getValue())) {
				tempIdPwd = idPwdTxt.getValue();
				tempIdPwd = XSSStringEncoder.encodeXSSString(tempIdPwd);
				ermResourcesMainDbws.setIdpwd(tempIdPwd);
			}
			// 更新頻率/刊期
			String tempUpdateFrequency = "";
			if (updateFrequency.getValue() != null
					&& !"".equals(updateFrequency.getValue())) {
				tempUpdateFrequency = updateFrequency.getValue();
				tempUpdateFrequency = XSSStringEncoder
						.encodeXSSString(tempUpdateFrequency);
				ermResourcesMainDbws.setFrenquency(tempUpdateFrequency);
			}
			// 使用說明
			String tempInstructionUse = "";
			if (instructionUseTxt.getValue() != null
					&& !"".equals(instructionUseTxt.getValue())) {
				tempInstructionUse = instructionUseTxt.getValue();
				tempInstructionUse = XSSStringEncoder
						.encodeXSSString(tempInstructionUse);
				ermResourcesMainDbws.setIntro(tempInstructionUse);
			}
			// 同時線上人數
			String tempOnLineNumber = "";
			if (onLineNumberTxt.getValue() != null
					&& !"".equals(onLineNumberTxt.getValue())) {
				tempOnLineNumber = onLineNumberTxt.getValue();
				tempOnLineNumber = XSSStringEncoder
						.encodeXSSString(tempOnLineNumber);
				ermResourcesMainDbws.setConcur(tempOnLineNumber);
			}
			// 連線方式
			String tempConnectModel = "";
			if (connectModelCbx.getSelectedItem().getValue() != null
					&& !"".equals(connectModelCbx.getSelectedItem().getValue())) {
				tempConnectModel = connectModelCbx.getSelectedItem().getValue();
				tempConnectModel = XSSStringEncoder
						.encodeXSSString(tempConnectModel);
				ermResourcesMainDbws.setConnectId(tempConnectModel);
			}
			// 存放地點
			String tempLocation = "";
			if (locationCbx.getValue() != null
					&& !"".equals(locationCbx.getValue())) {
				tempLocation = locationCbx.getSelectedItem().getValue();
				tempLocation = XSSStringEncoder.encodeXSSString(tempLocation);
				ermResourcesMainDbws.setPlaceId(tempLocation);
			}
			// 收錄年代
			String tempIncluded = "";
			if (includedTxt.getValue() != null
					&& !"".equals(includedTxt.getValue())) {
				tempIncluded = includedTxt.getValue();
				tempIncluded = XSSStringEncoder.encodeXSSString(tempIncluded);
				ermResourcesMainDbws.setCoverage(tempIncluded);
			}
			// 合法IP
			String tempReallyIp = "";
			if (legitimateIPTxt.getValue() != null
					&& !"".equals(legitimateIPTxt.getValue())) {
				tempReallyIp = legitimateIPTxt.getValue();
				tempReallyIp = XSSStringEncoder.encodeXSSString(tempReallyIp);
				ermResourcesMainDbws.setRegllyIp(tempReallyIp);
			}
			// 版本
			String tempVersion = "";
			if (versionTxt.getValue() != null
					&& !"".equals(versionTxt.getValue())) {
				tempVersion = versionTxt.getValue();
				tempVersion = XSSStringEncoder.encodeXSSString(tempVersion);
				ermResourcesMainDbws.setVersion(tempVersion);
			}
			// 資源簡述介紹
			String tempBrief1 = "";
			if (brief1CKtxt.getValue() != null
					&& !"".equals(brief1CKtxt.getValue())) {
				tempBrief1 = brief1CKtxt.getValue();
				ermResourcesMainDbws.setBrief1(tempBrief1);
			}
			// 資源簡述介紹(英文)
			String tempBrief2 = "";
			if (brief2CKtxt.getValue() != null
					&& !"".equals(brief2CKtxt.getValue())) {
				tempBrief2 = brief2CKtxt.getValue();
				ermResourcesMainDbws.setBrief2(tempBrief2);
			}

			Media media = imgAccountPic.getContent();
			if (media != null) {
				// String portalDocementPath = ((SystemProperties) SpringUtil
				// .getBean("systemProperties")).ermMainDbwsDocementPath;
				String portalDocementPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"
						+ SystemVariable.ERM_RESMAIN_DBWS_PATH;
				org.zkoss.image.Image image = (org.zkoss.image.Image) media;
				String fileName = Long.toString(System.currentTimeMillis())
						+ "_" + image.getName();
				File file = new File(portalDocementPath + fileName);
				InputStream memberPhotoInputStream = image.getStreamData();
				Files.copy(file, memberPhotoInputStream);
				Files.close(memberPhotoInputStream);
				memberPhotoInputStream.close();
				ermResourcesMainDbws.setImgUrl(fileName);
			}
			String tempUploadFileName = "";
			if (uploadFileNameTxt.getValue() != null
					&& !"".equals(uploadFileNameTxt.getValue())) {
				tempUploadFileName = uploadFileNameTxt.getValue();
				tempUploadFileName = XSSStringEncoder
						.encodeXSSString(tempUploadFileName);
				ErmResourcesUploadfile ermResourcesUploadfile = new ErmResourcesUploadfile();
				ermResourcesUploadfile.setIsDataEffid(1);
				ermResourcesUploadfile.setResourcesId(resourcesId);
				ermResourcesUploadfile.setUuid(fileName);
				ermResourcesUploadfile.setUploadFile(uploadFileNameTxt
						.getValue());
				((ErmResourcesUploadfileService) SpringUtil
						.getBean("ermResourcesUploadfileService"))
						.addUploadFile(ermResourcesUploadfile);
			}
			ermResourcesMainDbws.setIsDataEffid(1);
			ermResourcesMainDbws.setTypeId("DB");
			ermResourcesMainDbws.setHistory("N");
			ermResourcesMainDbws.setState("1");

			((WebSysLogService) SpringUtil.getBean("webSysLogService"))
					.editLog(ZkUtils.getRemoteAddr(),
							webEmployee.getEmployeesn(), "resourcesmaindbws_"
									+ ermResourcesMainDbws.getResourcesId());
			((ErmResMainDbwsService) SpringUtil
					.getBean("ermResMainDbwsService"))
					.UpdResMainDbws(ermResourcesMainDbws);

			((ResourcesMainDbwsSolrSearch) SpringUtil
					.getBean("resourcesMainDbwsSolrSearch"))
					.resources_main_dbws_editData(ermResourcesMainDbws
							.getResourcesId());
			((WebSysLogService) SpringUtil.getBean("webSysLogService"))
					.editLog(ZkUtils.getRemoteAddr(),
							webEmployee.getEmployeesn(), "resourcesmaindbws_"
									+ resourcesMainDbws.getResourcesId());
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			// String url = "ermResMainDbws/ermResMainDbws.zul";
			// ZkUtils.refurbishMethod(url);
			editErmResDbwsList();
			// editErmResMainDbwsWin.detach();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("電子資料庫/網絡資源修改方法報錯", e);
		}
	}

	@Listen("onClick=#saveBtn")
	public void addResourcesDbws() {
		try {
			ErmResourcesMainDbws resourcesMainDbws = new ErmResourcesMainDbws();
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			resourcesMainDbws.setLatelyChangedUser(webEmployee.getEmployeesn());
			resourcesMainDbws.setLatelyChangedDate(new Date());
			List<ErmResourcesMainDbws> dbwsList = ((ErmResMainDbwsService) SpringUtil
					.getBean("ermResMainDbwsService")).findAllResMainDbwsList(
					ermResourcesMainDbws, webEmployee);
			String tempId = "DB";
			Long number = (long) dbwsList.size();
			number = number + 1;
			String resourcesId = tempId + RandomIDGenerator.fmtLong(number, 9);

			ermResourcesMainDbws = new ErmResourcesMainDbws();
			ermResourcesMainDbws.setResourcesId(resourcesId);
			String tempTitle = "";// 標題
			if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue())) {
				tempTitle = titleTxt.getValue();
				// tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
				ermResourcesMainDbws.setTitle(tempTitle);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermResMainDbws.title")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				titleTxt.focus();
				return;
			}
			String tempUrl = "";// url
			if (urlTxt.getValue() != null && !"".equals(urlTxt.getValue())) {
				tempUrl = urlTxt.getValue();
				tempUrl = XSSStringEncoder.encodeXSSString(tempUrl);
				ermResourcesMainDbws.setUrl1(tempUrl);
			} else {
				ZkUtils.showExclamation(
						"url" + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				urlTxt.focus();
				return;
			}
			String tempUrl2 = "";// 相關url
			if (url2Txt.getValue() != null && !"".equals(url2Txt.getValue())) {
				tempUrl2 = url2Txt.getValue();
				tempUrl2 = XSSStringEncoder.encodeXSSString(tempUrl2);
				ermResourcesMainDbws.setUrl2(tempUrl2);
			}
			String templanguage = "";
			if (langTbx.getValue() != null && !"".equals(langTbx.getValue())) {
				templanguage = languageSearch.getValue();
				templanguage = XSSStringEncoder.encodeXSSString(templanguage);
				ermResourcesMainDbws.setLanguageId(templanguage);
			}
			String tempPublisher = "";
			if (pubTbx.getValue() != null && !"".equals(pubTbx.getValue())) {
				tempPublisher = publisherSearch.getValue();
				tempPublisher = XSSStringEncoder.encodeXSSString(tempPublisher);
				ermResourcesMainDbws.setPublisherId(tempPublisher);
			}
			String tempAgent = "";
			if (agenTbx.getValue() != null && !"".equals(agenTbx.getValue())) {
				tempAgent = agentedSearch.getValue();
				tempAgent = XSSStringEncoder.encodeXSSString(tempAgent);
				ermResourcesMainDbws.setAgentedId(tempAgent);
			}
			Date tempStartOrderDate = new Date();
			if (startOrderDateDtx.getValue() != null
					&& !"".equals(startOrderDateDtx.getValue())) {
				tempStartOrderDate = startOrderDateDtx.getValue();
				ermResourcesMainDbws.setStarOrderDate(tempStartOrderDate);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermResMainEjeb.startDate") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				agenTbx.focus();
				return;
			}
			if (endOrderDateDtx.getValue() != null
					&& !"".equals(endOrderDateDtx.getValue())) {
				Date endOrderDate = endOrderDateDtx.getValue();
				Date startOrderDate = startOrderDateDtx.getValue();
				if (startOrderDate.after(endOrderDate)) {
					ZkUtils.showExclamation(Labels
							.getLabel("ermResMainEjeb.startDateNotDyEndDate"),
							Labels.getLabel("warn"));
					startOrderDateDtx.focus();
					return;
				}
			}
			Date tempEndOrderDate = new Date();
			if (endOrderDateDtx.getValue() != null
					&& !"".equals(endOrderDateDtx.getValue())) {
				tempEndOrderDate = endOrderDateDtx.getValue();
				ermResourcesMainDbws.setEndOrderDate(tempEndOrderDate);
			}
			// 採購註記
			String tempProcurementNote = "";
			if (procurementNoteCbx.getSelectedItem().getValue() != null
					&& !"".equals(procurementNoteCbx.getSelectedItem()
							.getValue())) {
				tempProcurementNote = procurementNoteCbx.getSelectedItem()
						.getValue();
				tempProcurementNote = XSSStringEncoder
						.encodeXSSString(tempProcurementNote);
				ermResourcesMainDbws.setRemarkId(tempProcurementNote);
			}
			// 圖書館經費
			String tempLibaryMoney = "";
			if (libaryMoneyRdo.getSelectedItem().getValue() != null
					&& !"".equals(libaryMoneyRdo.getSelectedItem().getValue())) {
				tempLibaryMoney = libaryMoneyRdo.getSelectedItem().getValue();
				tempLibaryMoney = XSSStringEncoder
						.encodeXSSString(tempLibaryMoney);
				ermResourcesMainDbws.setLibaryMoney(tempLibaryMoney);
			}
			// 賬號與密碼
			String tempIdPwd = "";
			if (idPwdTxt.getValue() != null && !"".equals(idPwdTxt.getValue())) {
				tempIdPwd = idPwdTxt.getValue();
				tempIdPwd = XSSStringEncoder.encodeXSSString(tempIdPwd);
				ermResourcesMainDbws.setIdpwd(tempIdPwd);
			}
			// 更新頻率/刊期
			String tempUpdateFrequency = "";
			if (updateFrequency.getValue() != null
					&& !"".equals(updateFrequency.getValue())) {
				tempUpdateFrequency = updateFrequency.getValue();
				tempUpdateFrequency = XSSStringEncoder
						.encodeXSSString(tempUpdateFrequency);
				ermResourcesMainDbws.setFrenquency(tempUpdateFrequency);
			}
			// 使用說明
			String tempInstructionUse = "";
			if (instructionUseTxt.getValue() != null
					&& !"".equals(instructionUseTxt.getValue())) {
				tempInstructionUse = instructionUseTxt.getValue();
				tempInstructionUse = XSSStringEncoder
						.encodeXSSString(tempInstructionUse);
				ermResourcesMainDbws.setIntro(tempInstructionUse);
			}
			// 同時線上人數
			String tempOnLineNumber = "";
			if (onLineNumberTxt.getValue() != null
					&& !"".equals(onLineNumberTxt.getValue())) {
				tempOnLineNumber = onLineNumberTxt.getValue();
				tempOnLineNumber = XSSStringEncoder
						.encodeXSSString(tempOnLineNumber);
				ermResourcesMainDbws.setConcur(tempOnLineNumber);
			}
			// 連線方式
			String tempConnectModel = "";
			if (connectModelCbx.getSelectedItem().getValue() != null
					&& !"".equals(connectModelCbx.getSelectedItem().getValue())) {
				tempConnectModel = connectModelCbx.getSelectedItem().getValue();
				tempConnectModel = XSSStringEncoder
						.encodeXSSString(tempConnectModel);
				ermResourcesMainDbws.setConnectId(tempConnectModel);
			}
			// 存放地點
			String tempLocation = "";
			if (locationCbx.getValue() != null
					&& !"".equals(locationCbx.getValue())) {
				tempLocation = locationCbx.getSelectedItem().getValue();
				tempLocation = XSSStringEncoder.encodeXSSString(tempLocation);
				ermResourcesMainDbws.setPlaceId(tempLocation);
			}
			// 收錄年代
			String tempIncluded = "";
			if (includedTxt.getValue() != null
					&& !"".equals(includedTxt.getValue())) {
				tempIncluded = includedTxt.getValue();
				tempIncluded = XSSStringEncoder.encodeXSSString(tempIncluded);
				ermResourcesMainDbws.setCoverage(tempIncluded);
			}
			// 合法IP
			String tempReallyIp = "";
			if (legitimateIPTxt.getValue() != null
					&& !"".equals(legitimateIPTxt.getValue())) {
				tempReallyIp = legitimateIPTxt.getValue();
				tempReallyIp = XSSStringEncoder.encodeXSSString(tempReallyIp);
				ermResourcesMainDbws.setRegllyIp(tempReallyIp);
			}
			// 版本
			String tempVersion = "";
			if (versionTxt.getValue() != null
					&& !"".equals(versionTxt.getValue())) {
				tempVersion = versionTxt.getValue();
				tempVersion = XSSStringEncoder.encodeXSSString(tempVersion);
				ermResourcesMainDbws.setVersion(tempVersion);
			}
			// 資源簡述介紹
			String tempBrief1 = "";
			if (brief1CKtxt.getValue() != null
					&& !"".equals(brief1CKtxt.getValue())) {
				tempBrief1 = brief1CKtxt.getValue();
				ermResourcesMainDbws.setBrief1(tempBrief1);
			}
			// 資源簡述介紹(英文)
			String tempBrief2 = "";
			if (brief2CKtxt.getValue() != null
					&& !"".equals(brief2CKtxt.getValue())) {
				tempBrief2 = brief2CKtxt.getValue();
				ermResourcesMainDbws.setBrief2(tempBrief2);
			}

			Media media = imgAccountPic.getContent();
			if (media != null) {
				// String portalDocementPath = ((SystemProperties) SpringUtil
				// .getBean("systemProperties")).ermMainDbwsDocementPath;
				String portalDocementPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"
						+ SystemVariable.ERM_RESMAIN_DBWS_PATH;
				org.zkoss.image.Image image = (org.zkoss.image.Image) media;
				String fileName = Long.toString(System.currentTimeMillis())
						+ "_" + image.getName();
				File file = new File(portalDocementPath + fileName);
				InputStream memberPhotoInputStream = image.getStreamData();
				Files.copy(file, memberPhotoInputStream);
				Files.close(memberPhotoInputStream);
				memberPhotoInputStream.close();
				ermResourcesMainDbws.setImgUrl(fileName);
			}
			String tempUploadFileName = "";
			if (uploadFileNameTxt.getValue() != null
					&& !"".equals(uploadFileNameTxt.getValue())) {
				tempUploadFileName = uploadFileNameTxt.getValue();
				tempUploadFileName = XSSStringEncoder
						.encodeXSSString(tempUploadFileName);
				ErmResourcesUploadfile ermResourcesUploadfile = new ErmResourcesUploadfile();
				ermResourcesUploadfile.setIsDataEffid(1);
				ermResourcesUploadfile.setResourcesId(resourcesId);
				ermResourcesUploadfile.setUuid(fileName);
				ermResourcesUploadfile.setUploadFile(uploadFileNameTxt
						.getValue());
				((ErmResourcesUploadfileService) SpringUtil
						.getBean("ermResourcesUploadfileService"))
						.addUploadFile(ermResourcesUploadfile);
			}
			ermResourcesMainDbws.setIsDataEffid(1);
			ermResourcesMainDbws.setTypeId("DB");
			ermResourcesMainDbws.setHistory("N");
			ermResourcesMainDbws.setState("1");
			ermResourcesMainDbws.setCreateDate(new Date());
			ermResourcesMainDbws.setWebEmployee(webEmployee);
			((WebSysLogService) SpringUtil.getBean("webSysLogService"))
					.editLog(ZkUtils.getRemoteAddr(),
							webEmployee.getEmployeesn(), "resourcesmaindbws_"
									+ ermResourcesMainDbws.getResourcesId());
			((ErmResMainDbwsService) SpringUtil
					.getBean("ermResMainDbwsService"))
					.addResMainDbws(ermResourcesMainDbws);

			((ResourcesMainDbwsSolrSearch) SpringUtil
					.getBean("resourcesMainDbwsSolrSearch"))
					.resources_main_dbws_editData(ermResourcesMainDbws
							.getResourcesId());
			((WebSysLogService) SpringUtil.getBean("webSysLogService"))
					.insertLog(ZkUtils.getRemoteAddr(),
							webEmployee.getEmployeesn(), "resourcesmaindbws_"
									+ resourcesMainDbws.getResourcesId());
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			String url = "ermResMainDbws/ermResMainDbws.zul";
			ZkUtils.refurbishMethod(url);
			addErmResMainDbwsWin.detach();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("電子資料庫/網絡資源新增方法報錯", e);
		}
	}

	@Listen("onClick=#checkTypeRes")
	public void checkTypeRes() {
		String url = "ermResMainDbws/ermTypeRes.zul?resourcesId=" + resourcesId
				+ "";
		ZkUtils.refurbishMethod(url);
		editErmResMainDbwsWin.detach();
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

	@Listen("onClick=#searchLangBtn")
	public void searchLanguage() {
		String langType = "DBLAN";
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

	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) throws IOException {
		try {
			Media media = event.getMedia();
			if (media instanceof org.zkoss.image.Image) {
				imgAccountPic.setContent((org.zkoss.image.Image) media);
				// deleeImg.setVisible(false) ;
				upload.setVisible(true);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("webAccount.notImge"),
						Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("上傳圖片異常" + e);
		}
	}

	@Listen("onUpload=#uploadFile")
	public void uploadFile(UploadEvent event) {
		try {
			Media media = event.getMedia();
			int size = media.getByteData().length;
			String oldUpStr = uploadFileNameTxt.getValue();
			String hzStr = oldUpStr.substring(oldUpStr.lastIndexOf(".") + 1);
			if (hzStr.equals("pdf") || hzStr.equals("doc")
					|| hzStr.equals("xls") || hzStr.equals("ppt")
					|| hzStr.equals("txt") || hzStr.equals("docx")
					|| hzStr.equals("pptx") || hzStr.equals("xlsx")) {
				if (size <= 10485760) {
					if (media != null) {
						if (media.isBinary()) {
							Desktop dtp = Executions.getCurrent().getDesktop();
							String realPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ "/"
									+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							InputStream in = media.getStreamData();
							String oldFileName = media.getName();
							int suffixNameIndex = oldFileName.lastIndexOf(".");
							String suffixName = oldFileName
									.substring(suffixNameIndex);
							fileName = RandomIDGenerator.getRandomId()
									+ suffixName; /*
												 * System.currentTimeMillis() +
												 * "_" +
												 * uploadFileNameTxt.getValue();
												 */
							File file = new File(realPath + fileName);
							uploadFileNameTxt.setValue(media.getName());
							uploadFile.setVisible(false);
							delFile.setVisible(true);
							Files.copy(file, in);
							Files.close(in);
							in.close();
						} else {
							Desktop dtp = Executions.getCurrent().getDesktop();
							// String realPath = ((SystemProperties) SpringUtil
							// .getBean("systemProperties")).webEduTrainingDocementPath
							// + "/";
							String realPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ "/"
									+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							// System.out.println(realPath);
							File file = new File(realPath);
							String sr = media.getStringData();
							/*
							 * String fileName = System.currentTimeMillis() +
							 * "_" + media.getName().substring(0,
							 * media.getName().indexOf("."));
							 */
							String type = uploadFileNameTxt.getValue();
							type = type.substring(type.lastIndexOf("."));
							File files = File.createTempFile(fileName, type,
									file);
							fileName = files.getName();
							uploadFileNameTxt.setValue(media.getName());
							uploadFile.setVisible(false);
							delFile.setVisible(true);
							FileWriter fw = new FileWriter(files);
							fw.write(sr);
							fw.close();

						}
					}
				} else {
					ZkUtils.showExclamation("檔大小不能超過10M",
							Labels.getLabel("warn"));
					if (uploadFileName != null && !"".equals(uploadFileName)) {
						uploadFileNameTxt.setValue(uploadFileName);
					} else {
						uploadFileNameTxt.setValue("");
					}
					uploadFileNameTxt.setValue("");
					return;
				}
			} else {
				ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"),
						Labels.getLabel("warn"));
				if (uploadFileName != null && !"".equals(uploadFileName)) {
					uploadFileNameTxt.setValue(uploadFileName);
				} else {
					uploadFileNameTxt.setValue("");
				}
				uploadFileNameTxt.setValue("");
				delFile.setVisible(false);
				uploadFile.setVisible(true);
				return;
			}
		} catch (Exception e) {
			log.error("下載檔異常" + e);
		}
	}

	public void editErmResDbwsList() {
		Listbox webDbwsLix = (Listbox) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("resMainDbwsLix");
		Label languageSearch = (Label) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("languageSearch");
		Textbox titleBox = (Textbox) editErmResMainDbwsWin.getParent()
				.getFellow("titleBox");
		Label publisherSearch = (Label) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("publisherSearch");
		Label agentedSearch = (Label) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("agentedSearch");
		Datebox startDateDbx = (Datebox) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("startDateDbx");
		Datebox endDateDbx = (Datebox) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("endDateDbx");
		Combobox remarkCbx = (Combobox) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("remarkCbx");
		Combobox stateCbx = (Combobox) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("stateCbx");
		Radiogroup historyRgp = (Radiogroup) editErmResMainDbwsWin.getParent()
				.getFellowIfAny("historyRgp");
		Textbox subjectIdBox = (Textbox) editErmResMainDbwsWin.getParent()
				.getFellow("subjectIdBox");
		Combobox providerCbx = (Combobox) editErmResMainDbwsWin.getParent()
				.getFellow("providerCbx");
		search(languageSearch, titleBox, publisherSearch, agentedSearch,
				startDateDbx, endDateDbx, remarkCbx, historyRgp, stateCbx,
				webDbwsLix, Integer.parseInt(currentPage), subjectIdBox,
				providerCbx);
	}

	public void search(Label languageSearch, Textbox titleBox,
			Label publisherSearch, Label agentedSearch, Datebox startDateDbx,
			Datebox endDateDbx, Combobox remarkCbx, Radiogroup historyNo,
			Combobox stateCbx, Listbox resMainDbwsLix, int page,
			Textbox subjectIdBox, Combobox providerCbx) {
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
				solrQuery += " suunit_id:"
						+ providerCbx.getSelectedItem().getValue();
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
			if (historyNo.getSelectedItem().isChecked()) {
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
			ListModelList<ErmResourcesMainfileV> listModel = new ListModelList<ErmResourcesMainfileV>(
					resMainDbwsList);
			listModel.setMultiple(true);
			resMainDbwsLix.setModel(listModel);
			resMainDbwsLix.setActivePage(page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢電子資料庫/網路資源集合出錯", e);
		}
	}

	@Listen("onClick=#delFile")
	public void delFile() {
		try {
			Desktop dtp = Executions.getCurrent().getDesktop();
			/*
			 * String realPath = dtp .getSession() .getWebApp() .getRealPath(
			 * "/document/uploadfile/webEduTraining/") + "/";
			 */
			// String realPath = ((SystemProperties) SpringUtil
			// .getBean("systemProperties")).webEduTrainingDocementPath
			// + "/";
			String realPath = ((SystemProperties) SpringUtil
					.getBean("systemProperties")).portalDocementPath
					+ "/"
					+ SystemVariable.ERM_RES_DBWSFILE_PATH;
			File file = new File(realPath + fileName);
			if (fileName != null && !fileName.equals("") && file.exists()) {
				file.delete();
			}
			List<ErmResourcesUploadfile> ermResourcesUploadfileList = ((ErmResourcesUploadfileService) SpringUtil
					.getBean("ermResourcesUploadfileService"))
					.findByResourcesId(resourcesMainfile.getResourcesId());
			if (ermResourcesUploadfileList.size() > 0) {
				((ErmResourcesUploadfileService) SpringUtil
						.getBean("ermResourcesUploadfileService"))
						.delUploadFile(ermResourcesUploadfileList.get(0));
			}
			uploadFileNameTxt.setValue("");
			delFile.setVisible(false);
			uploadFile.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常" + e);
		}
	}
}
