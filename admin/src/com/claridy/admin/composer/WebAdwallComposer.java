package com.claridy.admin.composer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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
import org.zkoss.zul.Image;
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
import com.claridy.domain.WebAdwall;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebAdwallService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author sunchao 2015/07/29 首頁輪播AD管理新增編輯頁
 */
public class WebAdwallComposer extends SelectorComposer<Component> {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 8591720837169072811L;
	@Wire
	private Textbox adnameZhTwTxt;
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
	private Textbox uploadNameTbx;
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
	private Image uploadImg;
	@Wire
	private int currentPage;

	private WebAdwall webadwall = null;
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
		currentPage = Integer.parseInt(tmpMap.get("currentPage"));
		try {
			webadwall = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).getWebAdwallById(tmpMap.get("uuid"));
			if (null != webadwall) {
				adnameZhTwTxt.setValue(webadwall.getAdnameZhTw());
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
					// img控件显示
					uploadImg.setVisible(true);
					// img控件图片路径
					String portalDocementPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath
							+ SystemVariable.ADWALL_PATH;
					File file = new File(portalDocementPath + fileName);
					if (file.exists()) {
						BufferedImage image = ImageIO.read(file);
						uploadImg.setContent(image);
					}
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
		String adnameZhTw = adnameZhTwTxt.getValue();
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
		adnameZhTw = XSSStringEncoder.encodeXSSString(adnameZhTw);
		filellink = XSSStringEncoder.encodeXSSString(filellink);

		if (StringUtils.isBlank(adnameZhTw)) {
			// 標題不能為空
			ZkUtils.showExclamation(Labels.getLabel("ermNews.title") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}
		if (startDate == null) {
			// 上架日期不能為空！
			ZkUtils.showExclamation(Labels.getLabel("ermNews.shelves") + Labels.getLabel("ermNews.zxDate") + " " + Labels.getLabel("cannottNull"),
					Labels.getLabel("warn"));
			return;
		}
		if (StringUtils.isBlank(filellink)) {
			// 上傳圖片不能為空
			ZkUtils.showExclamation(Labels.getLabel("fileuploud") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
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
				webadwall = new WebAdwall();
				webadwall.setWebEmployee(webEmployee);
				if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
					webadwall.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
				} else {
					webadwall.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
				}
				webadwall.setCreateDate(new Date());
				webadwall.setIsDataEffid(1);
				webadwall.setUuid(UUIDGenerator.getUUID());
				webadwall.setAdnameZhTw(adnameZhTw);
				webadwall.setIsblank(Integer.parseInt(isblank));
				webadwall.setIsdisplay(Integer.parseInt(isdisplay));
				webadwall.setSortnum(sortnum == null ? 0 : sortnum);
				webadwall.setStrurl(strurl);
				webadwall.setClicknum(clicknum == null ? 0 : clicknum);
				webadwall.setStartdate(startDate);
				webadwall.setEnddate(endDate);
				webadwall.setFilellink(filellink);
				webadwall.setDescZhTw(descZhTw);
				webadwall.setDescEnUs(descEnUs);
				((WebAdwallService) SpringUtil.getBean("webAdwallService")).save(webadwall);
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"webadwall_" + webadwall.getUuid());
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
				String url = "webadwall/webadwall.zul";
				ZkUtils.refurbishMethod(url);
				addWebadwallWin.detach();
			} catch (Exception e) {
				log.error("新增首頁輪播AD管理報錯", e);
			}
		} else {
			try {
				webadwall.setLatelyChangedUser(webEmployee.getEmployeesn());
				webadwall.setLatelyChangedDate(new Date());
				webadwall.setAdnameZhTw(adnameZhTw);
				webadwall.setIsblank(Integer.parseInt(isblank));
				webadwall.setIsdisplay(Integer.parseInt(isdisplay));
				webadwall.setSortnum(sortnum == null ? 0 : sortnum);
				webadwall.setStrurl(strurl);
				webadwall.setClicknum(clicknum == null ? 0 : clicknum);
				webadwall.setStartdate(startDate);
				webadwall.setEnddate(endDate);
				webadwall.setFilellink(filellink);
				webadwall.setDescZhTw(descZhTw);
				webadwall.setDescEnUs(descEnUs);
				((WebAdwallService) SpringUtil.getBean("webAdwallService")).update(webadwall);
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(),
						"webadwall_" + webadwall.getUuid());
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
					String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + SystemVariable.ADWALL_PATH;
					InputStream in = media.getStreamData();
					String uploadName = media.getName();
					if (uploadName != null && !"".equals(uploadName)) {
						String images = uploadName.substring(uploadName.lastIndexOf(".") + 1, uploadName.length());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
						if ("png".equals(images) || "jpg".equals(images) || "gif".equals(images) || "bmp".equals(images) || "tga".equals(images)) {
							fileName = sdf.format(new Date()) + "." + images;
							uploadNameTbx.setValue(fileName);
							File file = new File(realPath + fileName);
							Files.copy(file, in);
							Files.close(in);
							in.close();
							delLoadFile.setVisible(true);
							upload.setVisible(false);
							// img控件显示
							uploadImg.setVisible(true);
							if (file.exists()) {
								BufferedImage image = ImageIO.read(file);
								uploadImg.setContent(image);
							}
						} else {
							ZkUtils.showError(Labels.getLabel("webAccount.notImge"), Labels.getLabel("info"));
							uploadNameTbx.setValue("");
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("上传首頁輪播AD管理圖片失敗", e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Listen("onClick=#delLoadFile")
	public void delFile() {
		try {
			ZkUtils.showQuestion(Labels.getLabel("ermResUnitUse.confirm") + Labels.getLabel("deleteImg") + "?", Labels.getLabel("info"),
					new EventListener() {
						public void onEvent(Event event) {
							int clickButton = (Integer) event.getData();
							if (clickButton == Messagebox.OK) {
								String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath
										+ SystemVariable.ADWALL_PATH;
								File file = new File(realPath + fileName);
								if (file.exists()) {
									file.delete();
								}
								delLoadFile.setVisible(false);
								upload.setVisible(true);
								uploadNameTbx.setValue("");
								uploadImg.setSrc("");
								uploadImg.setVisible(false);
							}
						}
					});
		} catch (Exception e) {
			log.error("刪除首頁輪播AD管理圖片異常", e);
		}
	}

	public void editSearchOrgList() {
		Textbox keywordBoxs = (Textbox) editWebadwallWin.getParent().getFellowIfAny("keywordBox");
		Listbox webAdwallLbxs = (Listbox) editWebadwallWin.getParent().getFellowIfAny("webAdwallLbx");
		try {
			String keyword = keywordBoxs.getValue();
			keyword = XSSStringEncoder.encodeXSSString(keyword);
			List<WebAdwall> webAdwallList = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).findAll(webEmployee);
			List<WebAdwall> result = ((WebAdwallService) SpringUtil.getBean("webAdwallService")).search(keyword, webEmployee);
			if (webAdwallList != null && webAdwallList.size() > 0) {
				for (WebAdwall webAdwall : webAdwallList) {
					// img控件图片路径
					String imgPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).agridlURL.trim() + "/" + SystemVariable.ADWALL_PATH
							+ "/" + webAdwall.getFilellink();
					webAdwall.setFilellink(imgPath);
				}
			}
			ListModelList<WebAdwall> tmpLML = new ListModelList<WebAdwall>(result);
			tmpLML.setMultiple(true);
			webAdwallLbxs.setModel(tmpLML);
			webAdwallLbxs.setActivePage(currentPage);
			keywordBoxs.setValue(keyword);

		} catch (Exception e) {
			// TODO: handle exception
			log.error("Phone集合出錯", e);
		}
	}
}
