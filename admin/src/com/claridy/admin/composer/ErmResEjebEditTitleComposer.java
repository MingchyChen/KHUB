package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
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

public class ErmResEjebEditTitleComposer extends SelectorComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2435010135443202771L;
	@Wire
	private Textbox title;// 題名
	@Wire
	private Textbox resourceIdTxt;//編號
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
	private ErmResourcesMainEjeb ejeb;
	@Wire
	private Window ermResEjebEditTitleWin;
	// 儲存solr的sql
	private String solrQuerySQL = "";
	private final Logger log = LoggerFactory.getLogger(getClass());

	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.select.SelectorComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		Map<String, String> map=new HashMap<String, String>();
		map=ZkUtils.getExecutionArgs();
		resourcesId=map.get("resourceId");
		ejeb=((ErmResMainEjebService) SpringUtil
						.getBean("ermResMainEjebService")).getResMainEjebByResId(resourcesId);
		if(ejeb!=null){
			resourceIdTxt.setValue(ejeb.getResourcesId());
			title.setValue(ejeb.getTitle());
			issnPringtedTxt.setValue(ejeb.getIssnprinted());
			issnOnlineTxt.setValue(ejeb.getIssnonline());
			brief1CKtxt.setValue(ejeb.getBrief1());
			brief2CKtxt.setValue(ejeb.getBrief2());
			if ("Y".equals("ejeb.getCore()")) {
				isOpen.setSelectedIndex(0);// 核心
			} else {
				isOpen.setSelectedIndex(1);
			}
			languageSearch.setValue(ejeb.getLanguageId());
			ErmCodeGeneralCode langCode = ((ErmCodeGeneralCodeService) SpringUtil
					.getBean("ermCodeGeneralCodeService"))
					.findByItemIDAndGeneralcodeId("RELAN",
							ejeb.getLanguageId());
			if (langCode.getName1() != null
					&& !"".equals(langCode.getName1())) {
				langTbx.setValue(langCode.getName1());
			}
			if (ejeb.getImgurl() != null
					&& !"".equals(ejeb.getImgurl())) {
				String src = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"
						+ SystemVariable.ERM_RESMAIN_EJEB_PATH
						+ ejeb.getImgurl();
				AImage aImage = new AImage(src);
				imgAccountPic.setContent(aImage);
			}
			authorTxt.setValue(ejeb.getAuthor());
			callnTxt.setValue(ejeb.getCalln());
			cnTxt.setValue(ejeb.getCn());
			List<ErmResourcesUploadfile> ermResourcesUploadfileList=((ErmResourcesUploadfileService) SpringUtil
					.getBean("ermResourcesUploadfileService")).findByResourcesId(ejeb.getResourcesId());
			if(ermResourcesUploadfileList.size()>0){
				for (ErmResourcesUploadfile ermResourcesUploadfile : ermResourcesUploadfileList) {
					uploadFileNameTxt.setValue(ermResourcesUploadfile.getUploadFile());
					fileName=ermResourcesUploadfile.getUuid();
				}
			}
		}
	}
	@Listen("onClick=#editBtn")
	public void editEjebItem(){
		try {
			ErmResourcesMainEjeb ermResourcesMainEjeb=new ErmResourcesMainEjeb();
			ermResourcesMainEjeb=ejeb;
			String titleName=title.getValue();
			if(titleName!=null&&!"".equals(titleName)){
				titleName=XSSStringEncoder.encodeXSSString(titleName);
				ermResourcesMainEjeb.setTitle(titleName);
			}
			String issnPrint=issnPringtedTxt.getValue();
			if(issnPrint!=null&&!"".equals(issnPrint)){
				issnPrint=XSSStringEncoder.encodeXSSString(issnPrint);
				ermResourcesMainEjeb.setIssnprinted(issnPrint);
			}
			String issnOnline=issnOnlineTxt.getValue();
			if(issnOnline!=null&&!"".equals(issnOnline)){
				issnOnline=XSSStringEncoder.encodeXSSString(issnOnline);
				ermResourcesMainEjeb.setIssnonline(issnOnline);
			}
			String brief1=brief1CKtxt.getValue();
			if(brief1!=null&&!"".equals(brief1)){
				brief1=XSSStringEncoder.encodeXSSString(brief1);
				ermResourcesMainEjeb.setBrief1(brief1);
			}
			String brief2=brief2CKtxt.getValue();
			if(brief2!=null&&!"".equals(brief2)){
				brief2=XSSStringEncoder.encodeXSSString(brief2);
				ermResourcesMainEjeb.setBrief2(brief2);
			}
			String core=isOpen.getSelectedItem().getValue();
			if(core!=null&&!"".equals(core)){
				core=XSSStringEncoder.encodeXSSString(core);
				ermResourcesMainEjeb.setCore(core);
			}
			String language=languageSearch.getValue();
			if(language!=null&&!"".equals(language)){
				language=XSSStringEncoder.encodeXSSString(language);
				ermResourcesMainEjeb.setLanguageId(language);
			}
			String author=authorTxt.getValue();
			if(author!=null&&!"".equals(author)){
				author=XSSStringEncoder.encodeXSSString(author);
				ermResourcesMainEjeb.setAuthor(author);
			}
			String calln=callnTxt.getValue();
			if(calln!=null&&!"".equals(calln)){
				calln=XSSStringEncoder.encodeXSSString(calln);
				ermResourcesMainEjeb.setCalln(calln);
			}
			String cn=cnTxt.getValue();
			if(cn!=null&&!"".equals(cn)){
				ermResourcesMainEjeb.setCn(cn);
			}
			
			Media media = imgAccountPic.getContent();
			if (media != null) {
				// String portalDocementPath = ((SystemProperties) SpringUtil
				// .getBean("systemProperties")).ermMainEjebDocementPath;
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
				ermResourcesMainEjeb.setImgurl(fileName);
			}
			String tempUploadFileName = "";
			if (uploadFileNameTxt.getValue() != null
					&& !"".equals(uploadFileNameTxt.getValue())) {
				tempUploadFileName = uploadFileNameTxt.getValue();
				tempUploadFileName = XSSStringEncoder
						.encodeXSSString(tempUploadFileName);
				ErmResourcesUploadfile ermResourcesUploadfile = new ErmResourcesUploadfile();
				ermResourcesUploadfile.setIsDataEffid(1);
				ermResourcesUploadfile.setCreateDate(new Date());
				ermResourcesUploadfile.setLatelyChangedDate(new Date());
				ermResourcesUploadfile.setWebEmployee(webEmployee);
				ermResourcesUploadfile.setResourcesId(resourcesId);
				ermResourcesUploadfile.setUuid(fileName);
				ermResourcesUploadfile.setUploadFile(uploadFileNameTxt.getValue());
				((ErmResourcesUploadfileService) SpringUtil
						.getBean("ermResourcesUploadfileService"))
						.addUploadFile(ermResourcesUploadfile);
			}
			((ErmResMainEjebService) SpringUtil
					.getBean("ermResMainEjebService")).updMainEjebOne(
							ermResourcesMainEjeb);
			((ResourcesMainEjebSolrSearch) SpringUtil
					.getBean("resourcesMainEjebSolrSearch"))
					.resources_main_ejeb_editData(ermResourcesMainEjeb
							.getResourcesId());
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			ermResEjebEditTitleWin.detach();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("電子期刊主檔編輯錯誤"+e);
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
							upload.setVisible(false);
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
									+ SystemVariable.WEB_EDU_TRAINING_PATH;
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
							upload.setVisible(false);
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
				upload.setVisible(true);
				return;
			}
		} catch (Exception e) {
			log.error("下載檔異常" + e);
		}

	}
	
}
