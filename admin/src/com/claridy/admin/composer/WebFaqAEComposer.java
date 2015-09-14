package com.claridy.admin.composer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFaq;
import com.claridy.domain.WebFaqType;
import com.claridy.facade.WebFaqService;
import com.claridy.facade.WebSysLogService;

public class WebFaqAEComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6307494713115066251L;

	@Wire
	private Textbox titleZhtbox;
	@Wire
	private Textbox titleUstbox;
	@Wire
	private Combobox webfaqTypecbox;
	@Wire
	private Radiogroup isdisplayrdgroup;
	@Wire
	private Intbox sortNumibox;
	@Wire
	private CKeditor descriptionZhCedit;
	@Wire
	private CKeditor descriptionUsCedit;
	@Wire
	private CKeditor answerZhCedit;
	@Wire
	private CKeditor answerUsCedit;
	@Wire
	private Button saveBtn;
	@Wire
	private Button updateBtn;
	@Wire
	private Button cancelBtn;
	@Wire
	private Window webFaqAEWin;
	@Wire
	private Button deletebtn;
	@Wire
	private Button upload;
	@Wire
	private A upfilea;
	@Wire
	private int currentPage;

	private WebFaq webFaq;

	private String fileName;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private WebEmployee loginwebEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			if (uuid != null && !"".equals(uuid)) {
				currentPage = Integer.parseInt(map.get("currentPage"));
				webFaq = ((WebFaqService) SpringUtil.getBean("webFaqService")).findById(uuid);
				titleZhtbox.setValue(webFaq.getTitleZhTw());
				titleUstbox.setValue(webFaq.getTitleEnUs());
				sortNumibox.setValue(webFaq.getSortnum());
				if (webFaq.getUploadnewsfile() != null && !"".equals(webFaq.getUploadnewsfile())) {
					deletebtn.setVisible(true);
					upload.setVisible(false);
				}
				upfilea.setLabel(webFaq.getUploadnewsfile());
				descriptionZhCedit.setValue(webFaq.getContextlobZhTw());
				descriptionUsCedit.setValue(webFaq.getContextlobEnUs());
				answerZhCedit.setValue(webFaq.getAnswerZhTw());
				answerUsCedit.setValue(webFaq.getAnswerEnUs());
				cancelBtn.setLabel(Labels.getLabel("updCancel"));
				updateBtn.setVisible(true);
				saveBtn.setVisible(false);
				webFaqAEWin.setTitle(Labels.getLabel("edit"));
				if (webFaq.getIsdisplay() != null && webFaq.getIsdisplay() == 1) {
					isdisplayrdgroup.setSelectedIndex(0);
				} else if (webFaq.getIsdisplay() != null && webFaq.getIsdisplay() == 0) {
					isdisplayrdgroup.setSelectedIndex(1);
				}
			} else {
				cancelBtn.setLabel(Labels.getLabel("saveCancel"));
				updateBtn.setVisible(false);
				saveBtn.setVisible(true);
				webFaqAEWin.setTitle(Labels.getLabel("add"));
			}

			Comboitem item = new Comboitem();
			item.setLabel(Labels.getLabel("select"));
			item.setValue("0");
			webfaqTypecbox.appendChild(item);
			webfaqTypecbox.setSelectedIndex(0);
			List<WebFaqType> webFaqTypeList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findTypeAll();
			for (int i = 0; i < webFaqTypeList.size(); i++) {
				item = new Comboitem();
				item.setLabel(webFaqTypeList.get(i).getTitleZhTw());
				item.setValue(webFaqTypeList.get(i).getUuid());
				webfaqTypecbox.appendChild(item);
				if (webFaq != null && webFaqTypeList.get(i).getUuid().equals(webFaq.getWebFaqType().getUuid())) {
					webfaqTypecbox.setSelectedIndex(i + 1);
				}
			}
		} catch (Exception e) {
			log.error("faq新增編輯報錯", e);
		}

	}

	@Listen("onClick=#saveBtn")
	public void save() {
		webFaq = new WebFaq();
		try {
			String titleZh = XSSStringEncoder.encodeXSSString(titleZhtbox.getValue().trim());
			String titleUs = XSSStringEncoder.encodeXSSString(titleUstbox.getValue().trim());
			String faqType = webfaqTypecbox.getSelectedItem().getValue();
			String fileName = upfilea.getLabel();
			String descriptionZh = descriptionZhCedit.getValue();
			String descriptionUs = descriptionUsCedit.getValue();
			String answerZh = answerZhCedit.getValue();
			String answerUs = answerUsCedit.getValue();
			int sortNum = sortNumibox.getValue() != null ? sortNumibox.getValue() : -1;
			int display = -1;
			if (isdisplayrdgroup.getSelectedIndex() == 0) {
				display = 1;
			} else if (isdisplayrdgroup.getSelectedIndex() == 1) {
				display = 0;
			}
			if (titleZh == null || "".equals(titleZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitlezh"), Labels.getLabel("info"));
				titleZhtbox.focus();
				return;
			}
			if (titleUs == null || "".equals(titleUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitleus"), Labels.getLabel("info"));
				titleUstbox.focus();
				return;
			}
			if (faqType == null || "".equals(faqType) || "0".equals(faqType)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.faqTypeisnull"), Labels.getLabel("info"));
				webfaqTypecbox.focus();
				return;
			}
			if (descriptionZh == null || "".equals(descriptionZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.descriptionzhisnull"), Labels.getLabel("info"));
				return;
			}
			if (descriptionUs == null || "".equals(descriptionUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.descriptionusisnull"), Labels.getLabel("info"));
				return;
			}
			if (answerZh == null || "".equals(answerZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.answerzhisnull"), Labels.getLabel("info"));
				return;
			}
			if (answerUs == null || "".equals(answerUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.answerusisnull"), Labels.getLabel("info"));
				return;
			}
			webFaq.setTitleEnUs(titleUs);
			webFaq.setTitleZhTw(titleZh);
			webFaq.setContextlobEnUs(descriptionUs);
			webFaq.setContextlobZhTw(descriptionZh);
			webFaq.setAnswerEnUs(answerUs);
			webFaq.setAnswerZhTw(answerZh);
			webFaq.setCreateDate(new Date());
			webFaq.setDataOwnerGroup(loginwebEmployee.getDataOwnerGroup());
			WebFaqType webFaqType = ((WebFaqService) SpringUtil.getBean("webFaqService")).findTypeById(faqType);
			webFaq.setWebFaqType(webFaqType);
			webFaq.setIsDataEffid(1);
			webFaq.setIsdisplay(display);
			if (sortNum != -1) {
				webFaq.setSortnum(sortNum);
			}
			webFaq.setUploadnewsfile(fileName);
			webFaq.setUuid(UUIDGenerator.getUUID());
			webFaq.setWebEmployee(loginwebEmployee);
			((WebFaqService) SpringUtil.getBean("webFaqService")).save(webFaq);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(),
					"webfaq_" + webFaq.getUuid());
			ZkUtils.refurbishMethod("webfaq/webfaq.zul");
			webFaqAEWin.detach();
		} catch (WrongValueException e) {
			log.error("新增faq報錯",e);
		}

	}

	@Listen("onClick=#updateBtn")
	public void update() {
		try {
			String titleZh = XSSStringEncoder.encodeXSSString(titleZhtbox.getValue().trim());
			String titleUs = XSSStringEncoder.encodeXSSString(titleUstbox.getValue().trim());
			String faqType = webfaqTypecbox.getSelectedItem().getValue();
			String fileName = upfilea.getLabel();
			String descriptionZh = descriptionZhCedit.getValue();
			String descriptionUs = descriptionUsCedit.getValue();
			String answerZh = answerZhCedit.getValue();
			String answerUs = answerUsCedit.getValue();
			int sortNum = sortNumibox.getValue() != null ? sortNumibox.getValue() : -1;
			int display = -1;
			if (isdisplayrdgroup.getSelectedIndex() == 0) {
				display = 1;
			} else if (isdisplayrdgroup.getSelectedIndex() == 1) {
				display = 0;
			}
			if (titleZh == null || "".equals(titleZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitlezh"), Labels.getLabel("info"));
				titleZhtbox.focus();
				return;
			}
			if (titleUs == null || "".equals(titleUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaqtype.nulltitleus"), Labels.getLabel("info"));
				titleUstbox.focus();
				return;
			}
			if (faqType == null || "".equals(faqType) || "0".equals(faqType)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.faqTypeisnull"), Labels.getLabel("info"));
				webfaqTypecbox.focus();
				return;
			}
			if (descriptionZh == null || "".equals(descriptionZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.descriptionzhisnull"), Labels.getLabel("info"));
				return;
			}
			if (descriptionUs == null || "".equals(descriptionUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.descriptionusisnull"), Labels.getLabel("info"));
				return;
			}
			if (answerZh == null || "".equals(answerZh)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.answerzhisnull"), Labels.getLabel("info"));
				return;
			}
			if (answerUs == null || "".equals(answerUs)) {
				ZkUtils.showExclamation(Labels.getLabel("webfaq.answerusisnull"), Labels.getLabel("info"));
				return;
			}
			webFaq.setTitleEnUs(titleUs);
			webFaq.setTitleZhTw(titleZh);
			webFaq.setContextlobEnUs(descriptionUs);
			webFaq.setContextlobZhTw(descriptionZh);
			webFaq.setAnswerEnUs(answerUs);
			webFaq.setAnswerZhTw(answerZh);
			webFaq.setLatelyChangedDate(new Date());
			webFaq.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
			WebFaqType webFaqType = ((WebFaqService) SpringUtil.getBean("webFaqService")).findTypeById(faqType);
			webFaq.setWebFaqType(webFaqType);
			webFaq.setIsDataEffid(1);
			webFaq.setIsdisplay(display);
			if (sortNum != -1) {
				webFaq.setSortnum(sortNum);
			}
			webFaq.setUploadnewsfile(fileName);
			webFaq.setWebEmployee(loginwebEmployee);
			((WebFaqService) SpringUtil.getBean("webFaqService")).update(webFaq);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "webfaq_"
					+ webFaq.getUuid());
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			editSearchOrgList();
			webFaqAEWin.detach();
		} catch (WrongValueException e) {
			log.error("編輯faq報錯" , e);
		}
	}

	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) {
		try {
			Media media = event.getMedia();
			if (media != null) {
				if (media.isBinary()) {
					InputStream stream = media.getStreamData();
					if (stream.available() <= 10485760) {
						String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
								+ SystemVariable.WEB_FAQ_PATH;
						InputStream in = media.getStreamData();
						// String fileName = media.getName();
						String images = media.getName().substring(media.getName().lastIndexOf(".") + 1, media.getName().length());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
						fileName = sdf.format(new Date()) + "." + images;
						File file = new File(realPath + fileName);
						upfilea.setLabel(fileName);
						upload.setVisible(false);
						deletebtn.setVisible(true);
						Files.copy(file, in);
						Files.close(in);
						in.close();
					} else {
						ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"), Labels.getLabel("warn"));
						if (fileName != null && !"".equals(fileName)) {
							upfilea.setLabel(fileName);
						} else {
							upfilea.setLabel("");
						}
						upfilea.setLabel("");
						return;
					}
				} else {
					String size = media.getStringData();
					if (size.length() <= 10485760) {
						String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
								+ SystemVariable.WEB_FAQ_PATH;
						// File file = new File(realPath+media.getName());
						String sr = media.getStringData();
						// upfilea.setLabel(media.getName());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
						fileName = sdf.format(new Date()) + media.getName().substring(media.getName().lastIndexOf("."));
						upfilea.setLabel(fileName);
						File file = new File(realPath + fileName);
						OutputStreamWriter sw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
						upload.setVisible(false);
						deletebtn.setVisible(true);
						sw.write(sr);
						sw.close();
					} else {
						ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"), Labels.getLabel("warn"));
						if (fileName != null && !"".equals(fileName)) {
							upfilea.setLabel(fileName);
						} else {
							upfilea.setLabel("");
						}
						upfilea.setLabel("");
						return;
					}
				}
			}
		} catch (Exception e) {
			log.error("上傳檔異常" , e);
		}
	}

	@Listen("onClick=#upfilea")
	public void upFile() {
		try {
			if (upfilea.getLabel() != null && !"".equals(upfilea.getLabel())) {
				doEncoding(((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/" + SystemVariable.WEB_FAQ_PATH
						+ upfilea.getLabel(), upfilea.getLabel());
			}
		} catch (WrongValueException e) {
			log.error("下載檔案異常" , e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#deletebtn")
	public void delete() {
		try {
			ZkUtils.showQuestion(Labels.getLabel("ermResUnitUse.confirm") + Labels.getLabel("delFile") + "?", Labels.getLabel("info"),
					new EventListener() {
						public void onEvent(Event event) {
							int clickButton = (Integer) event.getData();
							if (clickButton == Messagebox.OK) {
								if (webFaq != null) {
									webFaq.setUploadnewsfile(null);
									((WebFaqService) SpringUtil.getBean("webFaqService")).update(webFaq);
									((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),
											loginwebEmployee.getEmployeesn(), "webfaq_" + webFaq.getUuid());

								}
								String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath
										+ SystemVariable.WEB_FAQ_PATH;
								File file = new File(realPath + fileName);
								if (file.exists()) {
									file.delete();
								}
								deletebtn.setVisible(false);
								upload.setVisible(true);
								upfilea.setLabel("");
							}
						}
					});
		} catch (Exception e) {
			log.error("刪除faq檔案異常", e);
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

	public void editSearchOrgList() {
		Textbox keywordtboxs = (Textbox) webFaqAEWin.getParent().getFellowIfAny("keywordtbox");
		Listbox webfaqtypeLix = (Listbox) webFaqAEWin.getParent().getFellowIfAny("webfaqtypeLix");
		try {
			String keyWord = XSSStringEncoder.encodeXSSString(keywordtboxs.getValue().trim());
			List<WebFaq> webFaqList = ((WebFaqService) SpringUtil.getBean("webFaqService")).findBy(keyWord, loginwebEmployee);
			ListModelList<WebFaq> model = new ListModelList<WebFaq>(webFaqList);
			model.setMultiple(true);
			webfaqtypeLix.setModel(model);
			webfaqtypeLix.setActivePage(currentPage);
			keywordtboxs.setValue(keyWord);
		} catch (Exception e) {
			log.error("FAQ集合出錯", e);
		}
	}
}
