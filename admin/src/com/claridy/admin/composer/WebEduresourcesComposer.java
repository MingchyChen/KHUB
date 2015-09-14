package com.claridy.admin.composer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
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
import com.claridy.domain.WebEduresource;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebEduresourcesService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author sunchao 2015/07/29 線上教學資源新增編輯頁
 */
public class WebEduresourcesComposer extends SelectorComposer<Component> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 8591720837169072811L;
	@Wire
	private Textbox titlezhtwTxt;
	@Wire
	private Textbox titleenusTxt;
	@Wire
	private Radiogroup linkTypeRdo;
	@Wire
	private Radiogroup isShowRdo;
	@Wire
	private Intbox sortIxt;
	@Wire
	private Textbox linkUrltxt;
	@Wire
	private Intbox clickNumIxt;
	@Wire
	private Datebox startDateDbx;
	@Wire
	private Datebox endDateDbx;
	@Wire
	private Label uploadNameTbx;
	@Wire
	private Button upload;
	@Wire
	private Button delLoadFile;
	@Wire
	private CKeditor descZhTwCdr;
	@Wire
	private CKeditor descEnUsCdr;
	@Wire
	private Window editWebadwallWin;
	@Wire
	private Window addWebadwallWin;
	@Wire
	private int currentPage;

	private WebEduresource webadwall = null;
	private WebEmployee webEmployee;
	private String fileName;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		Map<String, String> tmpMap = new HashMap<String, String>();
		tmpMap = ZkUtils.getExecutionArgs();
		try {
			webadwall = ((WebEduresourcesService) SpringUtil.getBean("webEduresourcesService")).getWebAdwallById(tmpMap.get("uuid"));
			if (null != webadwall) {
				currentPage = Integer.parseInt(tmpMap.get("currentPage"));
				titlezhtwTxt.setValue(webadwall.getTitleZhTw());
				titleenusTxt.setValue(webadwall.getTitleEnUs());
				linkTypeRdo.setSelectedIndex(webadwall.getIsblank());
				isShowRdo.setSelectedIndex(webadwall.getIsdisplay());
				sortIxt.setValue(webadwall.getSortnum());
				linkUrltxt.setValue(webadwall.getStrurl());
				clickNumIxt.setValue(webadwall.getClicknum());
				startDateDbx.setValue(webadwall.getStartdate());
				endDateDbx.setValue(webadwall.getEnddate());
				if (!StringUtils.isBlank(webadwall.getFilellink())) {
					uploadNameTbx.setValue(webadwall.getFilellink());
					fileName = webadwall.getFilellink();
					upload.setVisible(false);
					delLoadFile.setVisible(true);
				}
				descZhTwCdr.setValue(webadwall.getDescZhTw());
				descEnUsCdr.setValue(webadwall.getDescEnUs());
			} else {
				linkTypeRdo.setSelectedIndex(1);
				isShowRdo.setSelectedIndex(1);
			}
		} catch (Exception e) {
			log.error("加載首頁輪播AD管理新增編輯頁面報錯", e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		String titlezhtw = titlezhtwTxt.getValue();
		String titleenus = titleenusTxt.getValue();
		String isblank = linkTypeRdo.getSelectedItem().getValue();
		String isdisplay = isShowRdo.getSelectedItem().getValue();
		Integer sortnum = sortIxt.getValue();
		String strurl = linkUrltxt.getValue();
		Integer clicknum = clickNumIxt.getValue();
		Date startDate = startDateDbx.getValue();
		Date endDate = endDateDbx.getValue();
		String filellink = uploadNameTbx.getValue();
		String descZhTw = descZhTwCdr.getValue();
		String descEnUs = descEnUsCdr.getValue();
		titlezhtw = XSSStringEncoder.encodeXSSString(titlezhtw);
		titleenus = XSSStringEncoder.encodeXSSString(titleenus);
		filellink = XSSStringEncoder.encodeXSSString(filellink);

		if (StringUtils.isBlank(titlezhtw)) {
			// 標題不能為空
			ZkUtils.showExclamation(Labels.getLabel("webeduresource.titlezhtw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}
		if (StringUtils.isBlank(titleenus)) {
			// 標題不能為空
			ZkUtils.showExclamation(Labels.getLabel("webeduresource.titleenus") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}
		if (startDate == null) {
			// 上架日期不能為空！
			ZkUtils.showExclamation(Labels.getLabel("ermNews.shelves") + Labels.getLabel("ermNews.zxDate") + " " + Labels.getLabel("cannottNull"),
					Labels.getLabel("warn"));
			return;
		}
		if (endDate != null && startDate.getTime() > endDate.getTime()) {
			// 上架日期不能大於下架日期！
			ZkUtils.showExclamation(Labels.getLabel("webeduresource.datetimes"), Labels.getLabel("warn"));
			return;
		}
		if (StringUtils.isBlank(descZhTw)) {
			// 內文中
			ZkUtils.showExclamation(Labels.getLabel("webadwall.contentZhTw") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}
		if (StringUtils.isBlank(descEnUs)) {
			// 內文英
			ZkUtils.showExclamation(Labels.getLabel("webadwall.contentEnUs") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}
		if (!StringUtils.isBlank(strurl)) {
			if (!ZkUtils.isUrl(strurl)) {
				ZkUtils.showExclamation(Labels.getLabel("urlError"), Labels.getLabel("warn"));
				return;
			}
		}
		if (webadwall == null) {
			try {
				webadwall = new WebEduresource();
				webadwall.setWebEmployee(webEmployee);
				if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
					webadwall.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
				} else {
					webadwall.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
				}
				webadwall.setCreateDate(new Date());
				webadwall.setIsDataEffid(1);
				webadwall.setUuid(UUIDGenerator.getUUID());
				webadwall.setTitleZhTw(titlezhtw);
				webadwall.setTitleEnUs(titleenus);
				webadwall.setIsblank(Integer.parseInt(isblank));
				webadwall.setIsdisplay(Integer.parseInt(isdisplay));
				webadwall.setSortnum(sortnum);
				webadwall.setStrurl(strurl);
				webadwall.setClicknum(clicknum);
				webadwall.setStartdate(startDate);
				webadwall.setEnddate(endDate);
				webadwall.setFilellink(filellink);
				webadwall.setDescZhTw(descZhTw);
				webadwall.setDescEnUs(descEnUs);
				((WebEduresourcesService) SpringUtil.getBean("webEduresourcesService")).save(webadwall);
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"webeduresource_" + webadwall.getUuid());
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "webeduresource/webeduresource.zul";
				ZkUtils.refurbishMethod(url);
				addWebadwallWin.detach();
			} catch (Exception e) {
				log.error("新增首頁輪播AD管理報錯", e);
			}
		} else {
			try {
				webadwall.setLatelyChangedUser(webEmployee.getEmployeesn());
				webadwall.setLatelyChangedDate(new Date());
				webadwall.setTitleZhTw(titlezhtw);
				webadwall.setTitleEnUs(titleenus);
				webadwall.setIsblank(Integer.parseInt(isblank));
				webadwall.setIsdisplay(Integer.parseInt(isdisplay));
				webadwall.setSortnum(sortnum);
				webadwall.setStrurl(strurl);
				webadwall.setClicknum(clicknum);
				webadwall.setStartdate(startDate);
				webadwall.setEnddate(endDate);
				webadwall.setFilellink(filellink);
				webadwall.setDescZhTw(descZhTw);
				webadwall.setDescEnUs(descEnUs);
				((WebEduresourcesService) SpringUtil.getBean("webEduresourcesService")).update(webadwall);
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"webeduresource_" + webadwall.getUuid());
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				editSearchOrgList();
				editWebadwallWin.detach();
			} catch (Exception e) {
				log.error("編輯首頁輪播AD管理報錯", e);
			}
		}
	}

	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) throws IOException {
		try {
			Media media = event.getMedia();
			if (media != null) {
				if (media.isBinary()) {
					String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath.trim() + "/"
							+ SystemVariable.EDURES_PATH;
					InputStream in = media.getStreamData();
					String uploadName = media.getName();
					if (uploadName != null && !"".equals(uploadName)) {
						String images = uploadName.substring(uploadName.lastIndexOf(".") + 1, uploadName.length());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
						fileName = sdf.format(new Date()) + "." + images;
						uploadNameTbx.setValue(fileName);
						File file = new File(realPath + fileName);
						Files.copy(file, in);
						Files.close(in);
						in.close();
						delLoadFile.setVisible(true);
						upload.setVisible(false);
					}
				} else {
					String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath.trim() + "/"
							+ SystemVariable.EDURES_PATH;
					String sr = media.getStringData();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
					fileName = sdf.format(new Date()) + media.getName().substring(media.getName().lastIndexOf("."));
					uploadNameTbx.setValue(fileName);
					File file = new File(realPath + fileName);
					OutputStreamWriter sw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
					sw.write(sr);
					sw.close();
					delLoadFile.setVisible(true);
					upload.setVisible(false);
				}
			}
		} catch (Exception e) {
			log.error("上传線上教學資源檔案失敗", e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#delLoadFile")
	public void delFile() {
		try {
			ZkUtils.showQuestion(Labels.getLabel("ermResUnitUse.confirm") + Labels.getLabel("delFile") + "?", Labels.getLabel("info"),
					new EventListener() {
						public void onEvent(Event event) {
							int clickButton = (Integer) event.getData();
							if (clickButton == Messagebox.OK) {
								String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath
										+ SystemVariable.EDURES_PATH;
								File file = new File(realPath + fileName);
								if (file.exists()) {
									file.delete();
								}
								delLoadFile.setVisible(false);
								upload.setVisible(true);
								uploadNameTbx.setValue("");
								if (webadwall != null) {
									webadwall.setFilellink("");
									((WebEduresourcesService) SpringUtil.getBean("webEduresourcesService")).update(webadwall);
								}
							}
						}
					});
		} catch (Exception e) {
			log.error("刪除線上教學資源檔案異常", e);
		}
	}

	@Listen("onClick = #uploadNameTbx")
	public void uploadNameTbx() {
		String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath.trim() + "/" + SystemVariable.EDURES_PATH
				+ "/" + fileName;
		doEncoding(realPath, fileName);
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
		Textbox keywordBoxs = (Textbox) editWebadwallWin.getParent().getFellowIfAny("keywordBox");
		Listbox webAdwallLbx = (Listbox) editWebadwallWin.getParent().getFellowIfAny("webAdwallLbx");
		try {
			String keyword = keywordBoxs.getValue().trim();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			List<WebEduresource> result = ((WebEduresourcesService) SpringUtil.getBean("webEduresourcesService")).search(keyword, webEmployee);
			ListModelList<WebEduresource> tmpLML = new ListModelList<WebEduresource>(result);
			tmpLML.setMultiple(true);
			webAdwallLbx.setModel(tmpLML);
			webAdwallLbx.setActivePage(currentPage);
			keywordBoxs.setValue(keyword);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("線上教學資源集合出錯", e);
		}
	}
}
