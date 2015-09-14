package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesUploadfile;
import com.claridy.domain.ErmSysNotifyConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmResMainEjebService;
import com.claridy.facade.ErmResourcesUploadfileService;
import com.claridy.facade.ResourcesMainEjebSolrSearch;
import com.claridy.facade.WebSysLogService;

public class ErmResMainEjebMaintenComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6808819993227772974L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox title;// 題名
	@Wire
	private Radiogroup isOpen;// 核心
	@Wire
	private Textbox reportLink;// url
	@Wire
	private Textbox remarks;// 相關URL
	@Wire
	private Textbox issnPringtedTxt;
	@Wire
	private Textbox issnOnlineTxt;
	@Wire
	private Textbox langTbx;// 語言
	@Wire
	private Textbox pubTbx;// 出版商
	@Wire
	private Button uploadFile;
	@Wire
	private Textbox agenTbx;// 所屬資料庫
	@Wire
	private Datebox startOrderDateDtx;// 起訂開始日期
	@Wire
	private Datebox endOrderDateDtx;// 訖訂結束日期
	@Wire
	private Combobox procurementNoteCbx;// 採購註記
	@Wire
	private Radiogroup libaryMoneyRdo;// 圖書館經費
	@Wire
	private Textbox publishedInTxt;// 出版地
	@Wire
	private Textbox idPwdTxt;// 賬號與密碼
	@Wire
	private Combobox locationCbx;// 存放地點
	@Wire
	private Textbox includedTxt;// 收錄年代
	@Wire
	private Textbox updateFrequency;// 更新頻率/刊期
	@Wire
	private Textbox legitimateIPTxt;// 合法IP
	@Wire
	private Textbox embargoTxt;// embargo
	@Wire
	private Textbox collectionInfoTxt;// 館藏資訊
	@Wire
	private Textbox authorTxt;// 作者
	@Wire
	private Textbox callnTxt;// 電子書索書號
	@Wire
	private Textbox cnTxt;// 電子書分類號
	@Wire
	private Textbox versionTxt;// 版本
	@Wire
	private Textbox publisherUrlTxt;// 出版商首頁
	@Wire
	private Textbox othersTxt;// 其他資訊
	@Wire
	private Textbox brief1CKtxt;// 資源簡述摘要
	@Wire
	private Textbox brief2CKtxt;// 資源簡述摘要(英文)
	@Wire
	private ErmSysNotifyConfig ermSysNotifyConfig;
	@Wire
	private Window saveEjebWin;
	@Wire
	private Window maintenXgErmResEjebWin;
	@Wire
	private Image imgAccountPic;
	@Wire
	private Button upload;
	@Wire
	private Button deleeImg;
	@Wire
	private ErmResourcesMainEjeb resourcesMainEjeb;
	@Wire
	private ErmResourcesEjebItem resourcesEjebItem;
	@Wire
	private Label languageSearch;
	@Wire
	private Label publisherSearch;
	@Wire
	private Label agentedSearch;
	@Wire
	private Window addErmResDataBaseWin;
	@Wire
	private Button delFile;
	@Wire
	private Textbox uploadFileNameTxt;
	private WebEmployee webEmployee;
	private String resourcesId;
	private String currentPage;
	private String uploadFileName;
	private String fileName;
	private ErmResourcesMainfileV resourcesMainfile;
	// 儲存solr的sql
	private String solrQuerySQL = "";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		delFile.setVisible(false);
		uploadFile.setVisible(true);
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		Map<String, String> map = new HashMap<String, String>();
		map = ZkUtils.getExecutionArgs();
		// 初始化
		int tempResourceIndex = 0;
		int tempreceiptIndex = 0;
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

		resourcesId = map.get("resourcesId");
		resourcesMainfile = ((ErmResMainEjebService) SpringUtil
				.getBean("ermResMainEjebService"))
				.getResMainfileByResId(resourcesId);
		title.setValue(resourcesMainfile.getTitle());
		issnPringtedTxt.setValue(resourcesMainfile.getIssnprinted());
		issnOnlineTxt.setValue(resourcesMainfile.getIssnonline());
		languageSearch.setValue(resourcesMainfile.getLanguageId());
		ErmCodeGeneralCode langCode = ((ErmCodeGeneralCodeService) SpringUtil
				.getBean("ermCodeGeneralCodeService"))
				.findByItemIDAndGeneralcodeId("RELAN",
						resourcesMainfile.getLanguageId());
		if (langCode.getName1() != null && !"".equals(langCode.getName1())) {
			langTbx.setValue(langCode.getName1());
		}

	}

	@Listen("onClick=#editBtn")
	public void editResourcesDbws() {
		try {
			resourcesMainEjeb = new ErmResourcesMainEjeb();
			resourcesEjebItem = new ErmResourcesEjebItem();
			List<ErmResourcesMainEjeb> ejebList = ((ErmResMainEjebService) SpringUtil
					.getBean("ermResMainEjebService")).findAllEjeb();
			// String tempId = "EJ";
			// Long number = (long) ejebList.size();
			// number = number + 1;
			// String resourcesId = RandomIDGenerator.getSerialNumber("EJ");
			// String resourcesId = tempId + RandomIDGenerator.fmtLong(number,
			// 9);
			String resourcesId = resourcesMainfile.getResourcesId();
			resourcesMainEjeb.setResourcesId(resourcesId);
			resourcesEjebItem.setResourcesId(resourcesId);
			String tempTitle = "";
			if (title.getValue() != null && !"".equals(title.getValue())) {
				tempTitle = title.getValue();
				tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
				resourcesMainEjeb.setTitle(tempTitle);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermResMainDbws.title")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				title.focus();
				return;
			}
			String tempIsOpen = "";
			if (isOpen.getSelectedItem().getValue() != null) {
				tempIsOpen = isOpen.getSelectedItem().getValue();
			}
			String tempUrl = "";
			if (reportLink.getValue() != null
					&& !"".equals(reportLink.getValue())) {
				tempUrl = reportLink.getValue();
				tempUrl = XSSStringEncoder.encodeXSSString(tempUrl);
				resourcesEjebItem.setUrl1(tempUrl);
			} else {
				ZkUtils.showExclamation(
						"url" + " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				reportLink.focus();
				return;
			}
			String tempUrl2 = "";
			if (remarks.getValue() != null && !"".equals(remarks.getValue())) {
				tempUrl2 = remarks.getValue();
			}
			String tempLangTbx = "";
			if (langTbx.getValue() != null) {
				tempLangTbx = langTbx.getValue();
			}
			String tempPubTbx = "";
			if (pubTbx.getValue() != null) {
				tempPubTbx = pubTbx.getValue();
			}
			String tempAgenTbx = "";
			if (agenTbx.getValue() != null && !"".equals(agenTbx.getValue())) {
				tempAgenTbx = agenTbx.getValue();
				tempAgenTbx = XSSStringEncoder.encodeXSSString(tempAgenTbx);
				resourcesEjebItem.setDbId(agentedSearch.getValue());
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermResourcesConfig.dbid") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				agenTbx.focus();
				return;
			}
			Date tempStartOrderDateDtx = new Date();
			if (startOrderDateDtx.getValue() != null
					&& !"".equals(startOrderDateDtx.getValue())) {
				tempStartOrderDateDtx = startOrderDateDtx.getValue();
				resourcesEjebItem.setStarOrderDate(tempStartOrderDateDtx);
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("ermResMainEjeb.startDate") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				agenTbx.focus();
				return;
			}
			// if (startOrderDateDtx.getValue() != null) {
			// tempStartOrderDateDtx = startOrderDateDtx.getValue();
			// }
			Date tempEndOrderDateDtx = new Date();
			if (endOrderDateDtx.getValue() != null) {
				tempEndOrderDateDtx = endOrderDateDtx.getValue();
			} else {
				tempEndOrderDateDtx = null;
			}
			String tempProcurementNoteCbx = "";
			if (procurementNoteCbx.getValue() != null) {
				// tempProcurementNoteCbx = procurementNoteCbx.getValue();
				tempProcurementNoteCbx = procurementNoteCbx.getSelectedItem()
						.getValue();
			}
			String tempLibaryMoneyRdo = "";
			if (libaryMoneyRdo.getSelectedItem().getValue() != null) {
				tempLibaryMoneyRdo = libaryMoneyRdo.getSelectedItem()
						.getValue();
			}
			String tempPublishedInTxt = "";
			if (publishedInTxt.getValue() != null) {
				tempPublishedInTxt = publishedInTxt.getValue();
			}
			String tempIdPwdTxt = "";
			if (idPwdTxt.getValue() != null) {
				tempIdPwdTxt = idPwdTxt.getValue();
			}
			String tempLocationCbx = "";
			if (locationCbx.getValue() != null) {
				tempLocationCbx = locationCbx.getSelectedItem().getValue();
			}
			String tempIncludedTxt = "";
			if (includedTxt.getValue() != null) {
				tempIncludedTxt = includedTxt.getValue();
			}
			String tempUpdateFrequency = "";
			if (updateFrequency.getValue() != null) {
				tempUpdateFrequency = updateFrequency.getValue();
			}
			String tempLegitimateIPTxt = "";
			if (legitimateIPTxt.getValue() != null) {
				tempLegitimateIPTxt = legitimateIPTxt.getValue();
			}
			String tempEmbargoTxt = "";
			if (embargoTxt.getValue() != null) {
				tempEmbargoTxt = embargoTxt.getValue();
			}
			String tempCollectionInfoTxt = "";
			if (collectionInfoTxt.getValue() != null) {
				tempCollectionInfoTxt = collectionInfoTxt.getValue();
			}
			String tempAuthorTxt = "";
			if (authorTxt.getValue() != null) {
				tempAuthorTxt = authorTxt.getValue();
			}
			String tempCallnTxt = "";
			if (callnTxt.getValue() != null) {
				tempCallnTxt = callnTxt.getValue();
			}
			String tempCnTxt = "";
			if (cnTxt.getValue() != null) {
				tempCnTxt = cnTxt.getValue();
			}
			String tempVersionTxt = "";
			if (versionTxt.getValue() != null) {
				tempVersionTxt = versionTxt.getValue();
			}
			String tempPublisherUrlTxt = "";
			if (publisherUrlTxt.getValue() != null) {
				tempPublisherUrlTxt = publisherUrlTxt.getValue();
			}
			String tempOthersTxt = "";
			if (othersTxt.getValue() != null) {
				tempOthersTxt = othersTxt.getValue();
			}
			String tempBrief1CKtxt = "";
			if (brief1CKtxt.getValue() != null) {
				tempBrief1CKtxt = brief1CKtxt.getValue();
			}
			String tempBrief2CKtxt = "";
			if (brief2CKtxt.getValue() != null) {
				tempBrief2CKtxt = brief2CKtxt.getValue();
			}
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils
					.getSessionAttribute("webEmployee");
			resourcesMainEjeb.setLatelyChangedUser(webEmployee.getEmployeesn());
			resourcesMainEjeb.setLatelyChangedDate(new Date());
			resourcesEjebItem.setLatelyChangedUser(webEmployee.getEmployeesn());
			resourcesEjebItem.setLatelyChangedDate(new Date());
			// 題名
			// tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
			// resourcesMainEjeb.setTitle(tempTitle);
			// 核心
			tempIsOpen = XSSStringEncoder.encodeXSSString(tempIsOpen);
			resourcesMainEjeb.setCore(tempIsOpen);
			// url
			// tempUrl = XSSStringEncoder.encodeXSSString(tempUrl);
			// resourcesEjebItem.setUrl1(tempUrl);
			// 相關Url
			tempUrl2 = XSSStringEncoder.encodeXSSString(tempUrl2);
			resourcesEjebItem.setUrl2(tempUrl2);
			// 語言
			tempLangTbx = XSSStringEncoder.encodeXSSString(tempLangTbx);
			// resourcesMainEjeb.setLanguageId(tempLangTbx);
			resourcesMainEjeb.setLanguageId(languageSearch.getValue());
			// 出版商
			tempPubTbx = XSSStringEncoder.encodeXSSString(tempPubTbx);
			// resourcesEjebItem.setPublisherId(tempPubTbx);
			resourcesEjebItem.setPublisherId(publisherSearch.getValue());
			// 所屬資料庫
			// tempAgenTbx = XSSStringEncoder.encodeXSSString(tempAgenTbx);
			// resourcesEjebItem.setDbId(tempAgenTbx);
			// resourcesEjebItem.setDbId(agentedSearch.getValue());
			// 起訂日期
			// resourcesEjebItem.setStarOrderDate(tempStartOrderDateDtx);
			// 訖訂日期
			if (tempEndOrderDateDtx != null) {
				resourcesEjebItem.setEndOrderDate(tempEndOrderDateDtx);
			}
			// 採購註記
			tempProcurementNoteCbx = XSSStringEncoder
					.encodeXSSString(tempProcurementNoteCbx);
			resourcesEjebItem.setRemarkId(tempProcurementNoteCbx);
			// 圖書館經費
			tempLibaryMoneyRdo = XSSStringEncoder
					.encodeXSSString(tempLibaryMoneyRdo);
			resourcesEjebItem.setLibaryMoney(tempLibaryMoneyRdo);
			// 出版地
			tempPublishedInTxt = XSSStringEncoder
					.encodeXSSString(tempPublishedInTxt);
			resourcesEjebItem.setPubPlace(tempPublishedInTxt);
			// 賬號與密碼
			tempIdPwdTxt = XSSStringEncoder.encodeXSSString(tempIdPwdTxt);
			resourcesEjebItem.setIdpwd(tempIdPwdTxt);
			// 存放地點
			tempLocationCbx = XSSStringEncoder.encodeXSSString(tempLocationCbx);
			resourcesEjebItem.setPlaceId(tempLocationCbx);
			// 收錄年代
			tempIncludedTxt = XSSStringEncoder.encodeXSSString(tempIncludedTxt);
			resourcesEjebItem.setCoverage(tempIncludedTxt);
			// 更新頻率/刊期
			tempUpdateFrequency = XSSStringEncoder
					.encodeXSSString(tempUpdateFrequency);
			resourcesEjebItem.setFrenquency(tempUpdateFrequency);
			// 合法IP
			tempLegitimateIPTxt = XSSStringEncoder
					.encodeXSSString(tempLegitimateIPTxt);
			resourcesEjebItem.setRegllyIp(tempLegitimateIPTxt);
			// embargo
			tempEmbargoTxt = XSSStringEncoder.encodeXSSString(tempEmbargoTxt);
			resourcesEjebItem.setEmbargo(tempEmbargoTxt);
			// 館藏資訊
			tempCollectionInfoTxt = XSSStringEncoder
					.encodeXSSString(tempCollectionInfoTxt);
			resourcesEjebItem.setEholdings(tempCollectionInfoTxt);
			// 作者
			tempAuthorTxt = XSSStringEncoder.encodeXSSString(tempAuthorTxt);
			resourcesMainEjeb.setAuthor(tempAuthorTxt);
			// 電子書索書號
			tempCallnTxt = XSSStringEncoder.encodeXSSString(tempCallnTxt);
			resourcesMainEjeb.setCalln(tempCallnTxt);
			// 電子書分類號
			tempCnTxt = XSSStringEncoder.encodeXSSString(tempCnTxt);
			resourcesMainEjeb.setCn(tempCnTxt);
			// 版本號
			tempVersionTxt = XSSStringEncoder.encodeXSSString(tempVersionTxt);
			resourcesEjebItem.setVersion(tempVersionTxt);
			// 出版商首頁
			tempPublisherUrlTxt = XSSStringEncoder
					.encodeXSSString(tempPublisherUrlTxt);
			resourcesEjebItem.setPublisherUrl(tempPublisherUrlTxt);
			// 其他資訊
			tempOthersTxt = XSSStringEncoder.encodeXSSString(tempOthersTxt);
			resourcesEjebItem.setOthers(tempOthersTxt);
			// 資源簡述摘要
			tempBrief1CKtxt = XSSStringEncoder.encodeXSSString(tempBrief1CKtxt);
			resourcesMainEjeb.setBrief1(tempBrief1CKtxt);
			// 資源簡述摘要(英文)
			tempBrief2CKtxt = XSSStringEncoder.encodeXSSString(tempBrief2CKtxt);
			resourcesMainEjeb.setBrief2(tempBrief2CKtxt);
			resourcesMainEjeb.setTypeId("EJ");
			resourcesEjebItem.setHistory("N");
			resourcesEjebItem.setState("1");

			resourcesMainEjeb.setIsDataEffid(1);
			resourcesEjebItem.setIsDataEffid(1);

			Media media = imgAccountPic.getContent();
			if (media != null) {
				/*
				 * String portalDocementPath = ((SystemProperties) SpringUtil
				 * .getBean("systemProperties")).ermMainEjebDocementPath;
				 */
				String portalDocementPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"
						+ SystemVariable.ERM_RESMAIN_EJEB_PATH;
				org.zkoss.image.Image image = (org.zkoss.image.Image) media;
				String fileName = Long.toString(System.currentTimeMillis())
						+ "_" + image.getName();
				File file = new File(portalDocementPath + fileName);
				InputStream memberPhotoInputStream = image.getStreamData();
				Files.copy(file, memberPhotoInputStream);
				Files.close(memberPhotoInputStream);
				memberPhotoInputStream.close();
				resourcesMainEjeb.setImgurl(fileName);
			}

			String tempUploadFileName = "";
			if (uploadFileNameTxt.getValue() != null
					&& !"".equals(uploadFileNameTxt.getValue())) {
				tempUploadFileName = uploadFileNameTxt.getValue();
				tempUploadFileName = XSSStringEncoder
						.encodeXSSString(tempUploadFileName);
				ErmResourcesUploadfile ermResourcesUploadfile = new ErmResourcesUploadfile();
				ermResourcesUploadfile.setResourcesId(resourcesId);
				ermResourcesUploadfile.setIsDataEffid(1);
				ermResourcesUploadfile.setUuid(fileName);
				ermResourcesUploadfile.setUploadFile(uploadFileNameTxt
						.getValue());
				((ErmResourcesUploadfileService) SpringUtil
						.getBean("ermResourcesUploadfileService"))
						.addUploadFile(ermResourcesUploadfile);
			}

			((WebSysLogService) SpringUtil.getBean("webSysLogService"))
					.editLog(ZkUtils.getRemoteAddr(),
							webEmployee.getEmployeesn(), "resourcesmainejebItem_"
									+ resourcesMainEjeb.getResourcesId());
//			((ErmResMainEjebService) SpringUtil
//					.getBean("ermResMainEjebService")).updateResMainEjeb(
//					resourcesMainEjeb, resourcesEjebItem);
			((ErmResMainEjebService) SpringUtil
					.getBean("ermResMainEjebService")).addResMainEjeb(resourcesEjebItem);
			((ResourcesMainEjebSolrSearch) SpringUtil
					.getBean("resourcesMainEjebSolrSearch"))
					.resources_main_ejeb_editData(resourcesMainEjeb
							.getResourcesId());
			// 存儲成功 resources_main_ejeb_editData
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			
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
		maintenXgErmResEjebWin.detach();
	}

	public void editErmResEjebList() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Listbox webEjebLix = (Listbox) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("resMainEjebLix");
		Label languageSearch = (Label) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("languageSearch");
		Textbox titleBox = (Textbox) maintenXgErmResEjebWin.getParent().getFellow(
				"titleBox");
		Label publisherSearch = (Label) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("publisherSearch");
		Label agentedSearch = (Label) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("agentedSearch");
		Datebox startDateDbx = (Datebox) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("startDateDbx");
		Datebox endDateDbx = (Datebox) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("endDateDbx");
		Combobox remarkCbx = (Combobox) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("remarkCbx");
		Combobox stateCbx = (Combobox) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("stateCbx");
		Radiogroup historyRgp = (Radiogroup) maintenXgErmResEjebWin.getParent()
				.getFellowIfAny("historyRgp");

		try {

			search(languageSearch, titleBox, publisherSearch, agentedSearch,
					startDateDbx, endDateDbx, remarkCbx, historyRgp, stateCbx,
					webEjebLix, Integer.parseInt(currentPage));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯", e);
		}
	}

	@Listen("onClick=#searchLangBtn")
	public void searchLanguage() {
		String langType = "RELAN";
		openSearch("language", langType);
	}

	@Listen("onClick=#searchPubBtn")
	public void searchPublisher() {
		openSearch("publisher", "");
	}

	@Listen("onClick=#searchDataBaseBtn")
	public void searchAgented() {
		openSearch("agented", "");
	}

	public void openSearch(String openValue, String openType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("openValue", openValue);
		map.put("openType", openType);
		if (openValue.trim().equals("language")) {
			Executions.createComponents(
					"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebOpen.zul",
					this.getSelf(), map);
		} else if (openValue.trim().equals("publisher")) {
			Executions
					.createComponents(
							"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebPubOpen.zul",
							this.getSelf(), map);
		} else {
			Executions
					.createComponents(
							"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebDataBase.zul",
							this.getSelf(), map);
		}
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

	@Listen("onClick=#deleeImg")
	public void deleteImg() {
		try {
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener<Event>() {

						public void onEvent(Event event) throws Exception {
							int clickButton = (Integer) event.getData();
							if (clickButton == Messagebox.OK) {
								Desktop dtp = Executions.getCurrent()
										.getDesktop();
								Media media = imgAccountPic.getContent();
								if (media != null) {
									String portalDocementPath = ((SystemProperties) SpringUtil
											.getBean("systemProperties")).portalDocementPath;
									org.zkoss.image.Image image = (org.zkoss.image.Image) media;
									File file = new File(portalDocementPath
											+ resourcesMainfile.getImgurl());
									InputStream memberPhotoInputStream = image
											.getStreamData();
									Files.deleteAll(file);
									Files.close(memberPhotoInputStream);
									memberPhotoInputStream.close();
									deleeImg.setVisible(false);
									upload.setVisible(true);
									// deleeImg.setStyle("display:none;");
									// upload.setStyle("display:block");
									imgAccountPic.setSrc("");
									resourcesMainfile.setImgurl("");
									// ((WebAccountService)SpringUtil.getBean("webAccountService")).deleteWebAccount(webAccount);
									((ErmResMainEjebService) SpringUtil
											.getBean("ermResMainEjebService"))
											.updMainEjebOne(resourcesMainEjeb);
								}
							}

						}

					});
		} catch (Exception e) {
			log.error("刪除圖片異常" + e);
		}

	}

	// addDataBaseBtn
	@Listen("onClick=#addDataBaseBtn")
	public void toAddDataBase() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("resourceId", resourcesId);
			addErmResDataBaseWin = (Window) ZkUtils.createComponents(
					"/WEB-INF/pages/system/ermResMainEjeb/ermCodeDbAdd.zul",
					null, map);
			addErmResDataBaseWin.doModal();
		} catch (Exception e) {
			log.error("ermResMainEjeb新增加載異常" + e);
		}
	}

	public void search(Label languageSearch, Textbox titleBox,
			Label publisherSearch, Label agentedSearch, Datebox startDateDbx,
			Datebox endDateDbx, Combobox remarkCbx, Radiogroup historyNo,
			Combobox stateCbx, Listbox resMainEjebLix, int page) {
		// solr查詢語句
		String solrQuery = "";
		try {
			solrQuery += "type_id:EJ";
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
			// List<ErmResourcesMainDbws>
			// resMainDbwsList=((ResourcesMainDbwsSolrSearch)SpringUtil.getBean("resourcesMainDbwsSolrSearch")).resourcesMainSearch(solrQuerySQL);
			List<ErmResourcesMainfileV> resourcesMainEjebsList = ((ResourcesMainEjebSolrSearch) SpringUtil
					.getBean("resourcesMainEjebSolrSearch"))
					.ResourcesMainEjebSearch(solrQuerySQL);
			// for (ErmResourcesMainfileV ermResourcesMainfileV :
			// resourcesMainEjebsList) {
			// ErmCodeGeneralCode langCode = ((ErmCodeGeneralCodeService)
			// SpringUtil
			// .getBean("ermCodeGeneralCodeService"))
			// .findByItemIDAndGeneralcodeId("RELAN",
			// ermResourcesMainfileV.getLanguageId());
			// ermResourcesMainfileV.setLanguageCn(langCode.getName1());
			// // ErmCodeGeneralCodeService
			// ErmCodeGeneralCode remarkCode = ((ErmCodeGeneralCodeService)
			// SpringUtil
			// .getBean("ermCodeGeneralCodeService"))
			// .findByItemIDAndGeneralcodeId("PURE",
			// ermResourcesMainfileV.getRemarkId());
			// ermResourcesMainfileV.setRemarkCn(remarkCode.getName1());
			// }
			ListModelList<ErmResourcesMainfileV> listModel = new ListModelList<ErmResourcesMainfileV>(
					resourcesMainEjebsList);
			listModel.setMultiple(true);
			resMainEjebLix.setModel(listModel);
			resMainEjebLix.setActivePage(page);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢電子資料庫/網路資源集合出錯", e);
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
									+ SystemVariable.ERM_RES_EJEBFILE_PATH;
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
									+ SystemVariable.ERM_RES_EJEBFILE_PATH;
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

	@Listen("onClick=#addResBtn")
	public void toResourcesPg() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourcesId", resourcesId);
		maintenXgErmResEjebWin.detach();
		maintenXgErmResEjebWin = (Window) ZkUtils.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebMainten.zul",
				null, map);
		maintenXgErmResEjebWin.doModal();
	}
	@Listen("onClick=#cancelBtn")
	public void leavePage(){
		List<ErmResourcesEjebItem> ejebItemList=((ErmResMainEjebService) SpringUtil
				.getBean("ermResMainEjebService")).findResMainEjebItemByResId(resourcesId);

		// String url = "ermResMainEjeb/ermResMainEjeb.zul";
		// ZkUtils.refurbishMethod(url); editErmResEjebWin.detach();

	//	editErmResEjebList();
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourcesId", resourcesId);
		if(ejebItemList.size()>1){
			String url="ermResMainEjeb/ermResEjebItemList.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
		}else{
			String url="ermResMainEjeb/ermResMainEjeb.zul";
			ZkUtils.refurbishMethod(url);
		}
		maintenXgErmResEjebWin.detach();
	}
	@Listen("onClick=#delFile")
	public void delFile() {
		try {
			Desktop dtp = Executions.getCurrent().getDesktop();
			/*
			 * String realPath = dtp .getSession() .getWebApp() .getRealPath(
			 * "/document/uploadfile/webEduTraining/") + "/";
			 */
//			String realPath = ((SystemProperties) SpringUtil
//					.getBean("systemProperties")).webEduTrainingDocementPath
//					+ "/";
			String realPath = ((SystemProperties) SpringUtil
					.getBean("systemProperties")).portalDocementPath
					+ "/"
					+ SystemVariable.ERM_RES_EJEBFILE_PATH;
			File file = new File(realPath + fileName);
			if (fileName != null && !fileName.equals("") && file.exists()) {
				file.delete();
			}
			uploadFileNameTxt.setValue("");
			delFile.setVisible(false);
			uploadFile.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常" + e);
		}
	}

}
