package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmNewsService;
import com.claridy.facade.WebPublicationService;
import com.claridy.facade.WebSysLogService;

/**
 * @author zjgao 最新消息 2014/08/05
 */

public class ErmNewsComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8962859725220866334L;
	@Wire
	private ErmNews ermNews;
	@Wire
	private Datebox startDatedbx;
	@Wire
	private Datebox endDatedbx;
	@Wire
	private Textbox titleTxt;
	@Wire
	private Row isTopRow;
	@Wire
	private Radiogroup isTopRdb;
	@Wire
	private Intbox sortNumIbx;
	@Wire
	private Intbox clickNumIbx;
	@Wire
	private Button upload;
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Textbox webSiteTbx;
	@Wire
	private CKeditor contentCedit;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private Window addErmNewsWin;
	@Wire
	private Window editErmNewsWin;
	@Wire
	private Button delFile;
	@Wire
	private int currentPage;

	private String uploadFileName;
	private String fileName;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		if (webEmployee.getIsnewstop() == null || webEmployee.getIsnewstop() == 0) {
			isTopRow.setVisible(false);
		} else {
			isTopRow.setVisible(true);
		}
		// 獲取用戶資訊
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			if (uuid != null) {
				ermNews = ((ErmNewsService) SpringUtil.getBean("ermNewsService")).findedtAddList("uuid", uuid).get(0);
			}
			if (ermNews != null && !"".equals(ermNews)) {
				// newsIdTxt.setValue(ermNews.getUuid());
				titleTxt.setValue(ermNews.getMatterZhTw());
				contentCedit.setValue(ermNews.getContentZhTw());
				startDatedbx.setValue(ermNews.getStartDate());
				endDatedbx.setValue(ermNews.getCloseDate());
				isTopRdb.setSelectedIndex(ermNews.getIsTop());
				sortNumIbx.setValue(ermNews.getSortNum());
				clickNumIbx.setValue(ermNews.getClickNum());
				uploadFileNameTxt.setValue(ermNews.getDisplayName());
				fileName = ermNews.getUpLoadNewFile();
				webSiteTbx.setValue(ermNews.getStrUrl());
				uploadFileName = ermNews.getUpLoadNewFile();
			} else {
				isTopRdb.setSelectedIndex(0);
			}
			if ("".equals(uploadFileNameTxt.getValue())) {
				upload.setVisible(true);
				delFile.setVisible(false);
			} else {
				upload.setVisible(false);
				delFile.setVisible(true);
			}
		} catch (Exception e) {
			log.error("ErmNews初始化異常" + e);
		}
	}

	@Listen("onClick=#saveBtn")
	public void addErmNews() throws Exception {
		// 獲取用戶資訊
		try {
			ermNews = new ErmNews();
			// String tempNewsId = "";
			String tempTitle = "";
			String tempContent = "";
			Date tempStartDate = new Date();
			Date tempEndDate = new Date();
			String tempisTop = "";
			Integer tempSortNum = 0;
			Integer tempClickNum = 0;
			String tempUpLoadFileName = "";
			String tempStrUrl = "";

			Date date = new Date();
			ermNews.setWebEmployee(webEmployee);
			ermNews.setUuid(UUIDGenerator.getUUID());

			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
				ermNews.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				ermNews.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			ermNews.setCreateDate(date);

			if (startDatedbx.getValue() != null && !"".equals(startDatedbx.getValue().toString().trim())) {
				tempStartDate = startDatedbx.getValue();
				// tempStartDate =
				// XSSStringEncoder.encodeXSSString(tempStartDate);
				ermNews.setStartDate(tempStartDate);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.OnDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			tempEndDate = endDatedbx.getValue();
			if (tempEndDate != null && !"".equals(tempEndDate)) {
				if (tempStartDate.getTime() > tempEndDate.getTime()) {
					ZkUtils.showExclamation(Labels.getLabel("ermNews.dateSNoThanDateE"), Labels.getLabel("warn"));
					return;
				}
			}
			ermNews.setCloseDate(tempEndDate);
			if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue().trim())) {
				tempTitle = titleTxt.getValue().toString().trim();
				tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
				ermNews.setMatterZhTw(tempTitle);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.title") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}
			tempisTop = isTopRdb.getSelectedItem().getValue();
			tempisTop = XSSStringEncoder.encodeXSSString(tempisTop);
			ermNews.setIsTop(Integer.parseInt(tempisTop));
			if (sortNumIbx.getValue() != null) {
				tempSortNum = sortNumIbx.getValue();
			}
			// tempSortNum = XSSStringEncoder.encodeXSSString(tempSortNum);
			if (tempSortNum != null) {
				ermNews.setSortNum(tempSortNum);
			}
			if (clickNumIbx.getValue() != null) {
				tempClickNum = clickNumIbx.getValue();
			}
			// tempClickNum = XSSStringEncoder.encodeXSSString(tempClickNum);
			if (tempClickNum != null) {
				ermNews.setClickNum(tempClickNum);
			}

			tempUpLoadFileName = uploadFileNameTxt.getValue().trim();
			tempUpLoadFileName = XSSStringEncoder.encodeXSSString(tempUpLoadFileName);
			ermNews.setDisplayName(tempUpLoadFileName);

			fileName = XSSStringEncoder.encodeXSSString(fileName);
			ermNews.setUpLoadNewFile(fileName);

			tempStrUrl = webSiteTbx.getValue();
			tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
			ermNews.setStrUrl(tempStrUrl);

			if (contentCedit.getValue() != null && !"".equals(contentCedit.getValue().toString())) {
				tempContent = contentCedit.getValue().toString().trim();
				ermNews.setContentZhTw(tempContent);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.content") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			ermNews.setLatelyChangedUser(webEmployee.getEmployeesn());
			ermNews.setLatelyChangedDate(new Date());
			((ErmNewsService) SpringUtil.getBean("ermNewsService")).addErmNews(ermNews);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), ermNews.getWebEmployee().getEmployeesn(),
					"webnews_" + ermNews.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			String url = "ermNews/ermNews.zul";
			ZkUtils.refurbishMethod(url);
			addErmNewsWin.detach();
		} catch (Exception e) {
			log.error("ErmNews新增異常" + e);
		}
	}

	@Listen("onClick=#editBtn")
	public void editErmNews() throws Exception {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			// ermNews = new ErmNews();
			// String tempNewsId = "";
			String tempTitle = "";
			String tempContent = "";
			Date tempStartDate = new Date();
			Date tempEndDate = new Date();
			String tempisTop = "";
			Integer tempSortNum = 0;
			Integer tempClickNum = 0;
			String tempUpLoadFileName = "";
			String tempStrUrl = "";

			Date date = new Date();
			// ermNews.setUuid(UUIDGenerator.getUUID());

			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
				ermNews.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				ermNews.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			ermNews.setCreateDate(date);

			if (startDatedbx.getValue() != null && !"".equals(startDatedbx.getValue().toString().trim())) {
				tempStartDate = startDatedbx.getValue();
				// tempStartDate =
				// XSSStringEncoder.encodeXSSString(tempStartDate);
				ermNews.setStartDate(tempStartDate);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.OnDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			tempEndDate = endDatedbx.getValue();
			if (tempEndDate != null && !"".equals(tempEndDate)) {
				if (tempStartDate.getTime() > tempEndDate.getTime()) {
					ZkUtils.showExclamation(Labels.getLabel("ermNews.dateSNoThanDateE"), Labels.getLabel("warn"));
					return;
				}
			}

			ermNews.setCloseDate(tempEndDate);
			if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue().trim())) {
				tempTitle = titleTxt.getValue().toString().trim();
				tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
				ermNews.setMatterZhTw(tempTitle);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.title") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}
			tempisTop = isTopRdb.getSelectedItem().getValue();
			tempisTop = XSSStringEncoder.encodeXSSString(tempisTop);
			ermNews.setIsTop(Integer.parseInt(tempisTop));

			if (sortNumIbx.getValue() != null) {
				tempSortNum = sortNumIbx.getValue();
			}

			// tempSortNum = XSSStringEncoder.encodeXSSString(tempSortNum);
			if (tempSortNum != null) {
				ermNews.setSortNum(tempSortNum);
			}
			if (clickNumIbx.getValue() != null) {
				tempClickNum = clickNumIbx.getValue();
			}
			// tempClickNum = XSSStringEncoder.encodeXSSString(tempClickNum);
			if (tempClickNum != null) {
				ermNews.setClickNum(tempClickNum);
			}

			tempUpLoadFileName = uploadFileNameTxt.getValue().trim();
			tempUpLoadFileName = XSSStringEncoder.encodeXSSString(tempUpLoadFileName);
			ermNews.setDisplayName(tempUpLoadFileName);

			fileName = XSSStringEncoder.encodeXSSString(fileName);
			ermNews.setUpLoadNewFile(fileName);

			tempStrUrl = webSiteTbx.getValue();
			tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
			ermNews.setStrUrl(tempStrUrl);

			if (contentCedit.getValue() != null && !"".equals(contentCedit.getValue().toString())) {
				tempContent = contentCedit.getValue().toString().trim();
				ermNews.setContentZhTw(tempContent);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("ermNews.content") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}

			ermNews.setLatelyChangedUser(webEmployee.getEmployeesn());
			ermNews.setLatelyChangedDate(new Date());
			((ErmNewsService) SpringUtil.getBean("ermNewsService")).addErmNews(ermNews);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), ermNews.getWebEmployee().getEmployeesn(),
					"webnews_" + ermNews.getUuid());

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			editSearchOrgList();
			editErmNewsWin.detach();
		} catch (Exception e) {
			log.error("ErmNews修改異常" + e);
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
								+ SystemVariable.ERM_NEWS_PATH;
						InputStream in = media.getStreamData();
						String oldFileName = media.getName();
						int suffixNameIndex = oldFileName.lastIndexOf(".");
						String suffixName = oldFileName.substring(suffixNameIndex);
						fileName = RandomIDGenerator.getRandomId() + suffixName;
						File file = new File(realPath + fileName);
						uploadFileNameTxt.setValue(media.getName());
						upload.setVisible(false);
						delFile.setVisible(true);
						Files.copy(file, in);
						Files.close(in);
						in.close();
					} else {
						ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"), Labels.getLabel("warn"));
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
				} else {
					String size = media.getStringData();
					if (size.length() <= 10485760) {
						String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
								+ SystemVariable.ERM_NEWS_PATH;
						File file = new File(realPath);
						String sr = media.getStringData();
						String type = media.getName().substring(media.getName().lastIndexOf("."));
						File files = File.createTempFile(RandomIDGenerator.getRandomId(), type, file);
						fileName = files.getName();
						uploadFileNameTxt.setValue(media.getName());
						upload.setVisible(false);
						delFile.setVisible(true);
						FileWriter fw = new FileWriter(files);
						fw.write(sr);
						fw.close();
					} else {
						ZkUtils.showExclamation(Labels.getLabel("eduTrain.fileType"), Labels.getLabel("warn"));
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
				}
			}
		} catch (Exception e) {
			log.error("上傳檔異常" + e);
		}
	}

	@Listen("onClick=#clearBtn")
	public void clearInput() throws Exception {
		titleTxt.setValue("");
		contentCedit.setValue("");
		startDatedbx.setValue(null);
		endDatedbx.setValue(null);
		isTopRdb.setSelectedIndex(0);
		sortNumIbx.setValue(null);
		clickNumIbx.setValue(null);
		uploadFileNameTxt.setValue("");
		webSiteTbx.setValue("");
	}

	@Listen("onClick=#delFile")
	public void delFile() {
		try {
			Desktop dtp = Executions.getCurrent().getDesktop();
			// String realPath = dtp.getSession().getWebApp()
			// .getRealPath("/document/uploadfile/ermNews/")
			// + "/";
			String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/" + SystemVariable.ERM_NEWS_PATH;
			File file = new File(realPath + fileName);
			if (fileName != null && !fileName.equals("") && file.exists()) {
				file.delete();
			}
			uploadFileNameTxt.setValue("");
			delFile.setVisible(false);
			upload.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常" + e);
		}
	}

	public void editSearchOrgList() {
		Textbox titleTxts = (Textbox) editErmNewsWin.getParent().getFellowIfAny("titleTxt");
		Radiogroup onDownRdb = (Radiogroup) editErmNewsWin.getParent().getFellowIfAny("onDownRdb");
		Listbox ermNewsLbx = (Listbox) editErmNewsWin.getParent().getFellowIfAny("ermNewsLbx");
		try {
			String tempTitle = titleTxts.getValue();
			int tempIndex = onDownRdb.getSelectedIndex();
			String onDown = "";
			if (tempIndex == 0 || tempIndex == 1) {
				onDown = onDownRdb.getSelectedItem().getValue();
			} else {
				onDown = "";
			}
			List<ErmNews> newsList = ((ErmNewsService) SpringUtil.getBean("ermNewsService")).findErmNewsByParam("%" + tempTitle + "%", onDown,
					webEmployee);
			ListModelList<ErmNews> tmpLML = new ListModelList<ErmNews>(newsList);
			tmpLML.setMultiple(true);
			ermNewsLbx.setModel(tmpLML);
			ermNewsLbx.setActivePage(currentPage);
			titleTxts.setValue(tempTitle);
			onDownRdb.setSelectedIndex(tempIndex);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("最新消息集合出錯", e);
		}
	}
}
