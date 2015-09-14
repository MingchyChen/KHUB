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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.WebEduTrainingService;
import com.claridy.facade.WebSysLogService;

/**
 * zjgao nj 查詢說明管理 2014/08/06
 */
public class WebEduTrainingComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2331258621634389720L;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private Window addWebEduTrainWin;
	@Wire
	private Window editWebEduTrainWin;
	@Wire
	private WebEduTraining webEduTraining;
	@Wire
	private Datebox startDatedbx;
	@Wire
	private Datebox closeDateDbx;
	@Wire
	private Textbox titleTxt;
	// @Wire
	// private Textbox clickNumtbx;
	@Wire
	private Intbox clickNumtbx;
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Textbox webSiteTbx;
	@Wire
	private CKeditor contentCedit;
	@Wire
	private Button upload;
	@Wire
	private Button delFile;
	@Wire
	private Datebox trainingDatedbx;
	@Wire
	private int currentPage;

	private String uploadFileName;

	private String fileName;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			if (uuid != null) {
				webEduTraining = ((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).findedtAddList("uuid", uuid).get(0);
			}
			if (webEduTraining != null && !"".equals(webEduTraining)) {
				startDatedbx.setValue(webEduTraining.getStartDate());
				closeDateDbx.setValue(webEduTraining.getCloseDate());
				trainingDatedbx.setValue(webEduTraining.getTrainingDate());
				titleTxt.setValue(webEduTraining.getMatterZhTw());
				// clickNumtbx.setValue(webEduTraining.getClickNum() + "");
				clickNumtbx.setValue(webEduTraining.getClickNum());
				// uploadFileNameTxt.setValue(webEduTraining.getUploadNewsFile());
				uploadFileNameTxt.setValue(webEduTraining.getDisplayName());
				fileName = webEduTraining.getUploadNewsFile();
				uploadFileName = webEduTraining.getUploadNewsFile();
				webSiteTbx.setValue(webEduTraining.getStrurl());
				contentCedit.setValue(webEduTraining.getConTextZhTw());
			}
			if ("".equals(uploadFileNameTxt.getValue())) {
				upload.setVisible(true);
				delFile.setVisible(false);
			} else {
				upload.setVisible(false);
				delFile.setVisible(true);
			}
		} catch (Exception e) {
			log.error("加載異常:" + e);
		}
	}

	@Listen("onClick=#saveBtn")
	public void saveEmployee() throws Exception {
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		webEduTraining = new WebEduTraining();
		webEduTraining.setUuid(UUIDGenerator.getUUID());
		String tempTitle = "";
		Integer tempClickNum = 0;
		String tempContentZhTw = "";
		String tempUpLoadFileName = "";
		String tempStrUrl = "";
		String displayName = fileName;
		Date temptrainingDate = new Date();
		Date tempStartDate = new Date();
		Date date = new Date();

		webEduTraining.setWebEmployee(webEmployee);
		webEduTraining.setCreateDate(date);

		if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
			webEduTraining.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
		} else {
			webEduTraining.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
		}

		if (startDatedbx.getValue() != null && !"".equals(startDatedbx.getValue())) {
			tempStartDate = startDatedbx.getValue();
			webEduTraining.setStartDate(tempStartDate);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.onDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			startDatedbx.focus();
			return;
		}
		if (closeDateDbx.getValue() != null && !"".equals(closeDateDbx.getValue())) {
			webEduTraining.setCloseDate(closeDateDbx.getValue());
		} else {
			webEduTraining.setCloseDate(null);
		}
		if (trainingDatedbx.getValue() != null && !"".equals(trainingDatedbx.getValue())) {
			temptrainingDate = trainingDatedbx.getValue();
			webEduTraining.setTrainingDate(temptrainingDate);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.trainDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			trainingDatedbx.focus();
			return;
		}

		if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue().trim())) {
			tempTitle = titleTxt.getValue().toString().trim();
			tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
			webEduTraining.setMatterZhTw(tempTitle);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.title") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}

		tempClickNum = clickNumtbx.getValue();
		if (tempClickNum != null) {
			webEduTraining.setClickNum(tempClickNum);
		}

		// boolean tempBoolClickNum=isNumber(tempClickNum);
		// if(tempBoolClickNum){
		// tempClickNum = XSSStringEncoder.encodeXSSString(tempClickNum);
		// if ("".equals(tempClickNum)) {
		// tempClickNum = "0";
		// }
		// webEduTraining.setClickNum(Integer.parseInt(tempClickNum));
		// }else {
		// ZkUtils.showExclamation(Labels.getLabel("eduTrain.clickNum")
		// + " " + Labels.getLabel("ermSysNotifyConfig.isNumber"),
		// Labels.getLabel("warn"));
		// clickNumtbx.focus();
		// return;
		// }

		tempUpLoadFileName = uploadFileNameTxt.getValue().trim();
		tempUpLoadFileName = XSSStringEncoder.encodeXSSString(tempUpLoadFileName);
		webEduTraining.setDisplayName(tempUpLoadFileName);

		// displayName = uploadFileNameTxt.getValue().trim();
		displayName = XSSStringEncoder.encodeXSSString(displayName);
		webEduTraining.setUploadNewsFile(displayName);

		tempStrUrl = webSiteTbx.getValue();
		tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
		webEduTraining.setStrurl(tempStrUrl);

		if (contentCedit.getValue() != null && !"".equals(contentCedit.getValue().toString())) {
			tempContentZhTw = contentCedit.getValue().toString().trim();
			webEduTraining.setConTextZhTw(tempContentZhTw);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.content") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}

		webEduTraining.setLatelyChangedUser(webEmployee.getEmployeesn());
		webEduTraining.setLatelyChangedDate(new Date());
		((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).save(webEduTraining);
		((WebSysLogService) SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(), webEduTraining.getWebEmployee()
				.getEmployeesn(), "webedutraining_" + webEduTraining.getUuid());

		// 更新成功
		ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
		String url = "webEduTraining/webEduTraining.zul";
		ZkUtils.refurbishMethod(url);
		addWebEduTrainWin.detach();
	}

	@Listen("onClick=#editBtn")
	public void updateEmployee() throws Exception {
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		String tempTitle = "";
		Integer tempClickNum = 0;
		String tempContentZhTw = "";
		String tempUpLoadFileName = "";
		String tempStrUrl = "";
		String displayName = fileName;
		Date tempStartDate = new Date();
		Date temptrainingDate = new Date();

		if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
			webEduTraining.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
		} else {
			webEduTraining.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
		}

		if (startDatedbx.getValue() != null && !"".equals(startDatedbx.getValue())) {
			tempStartDate = startDatedbx.getValue();
			webEduTraining.setStartDate(tempStartDate);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.onDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			startDatedbx.focus();
			return;
		}
		if (closeDateDbx.getValue() != null && !"".equals(closeDateDbx.getValue())) {
			webEduTraining.setCloseDate(closeDateDbx.getValue());
		} else {
			webEduTraining.setCloseDate(null);
		}
		if (trainingDatedbx.getValue() != null && !"".equals(trainingDatedbx.getValue())) {
			temptrainingDate = trainingDatedbx.getValue();
			webEduTraining.setTrainingDate(temptrainingDate);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.trainDate") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			trainingDatedbx.focus();
			return;
		}
		if (titleTxt.getValue() != null && !"".equals(titleTxt.getValue().trim())) {
			tempTitle = titleTxt.getValue().toString().trim();
			tempTitle = XSSStringEncoder.encodeXSSString(tempTitle);
			webEduTraining.setMatterZhTw(tempTitle);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.title") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}

		tempClickNum = clickNumtbx.getValue();
		if (tempClickNum != null) {
			webEduTraining.setClickNum(tempClickNum);
		}

		tempUpLoadFileName = uploadFileNameTxt.getValue().trim();
		tempUpLoadFileName = XSSStringEncoder.encodeXSSString(tempUpLoadFileName);
		webEduTraining.setDisplayName(tempUpLoadFileName);

		displayName = XSSStringEncoder.encodeXSSString(displayName);
		webEduTraining.setUploadNewsFile(displayName);

		tempStrUrl = webSiteTbx.getValue();
		tempStrUrl = XSSStringEncoder.encodeXSSString(tempStrUrl);
		webEduTraining.setStrurl(tempStrUrl);

		if (contentCedit.getValue() != null && !"".equals(contentCedit.getValue().toString())) {
			tempContentZhTw = contentCedit.getValue().toString().trim();
			webEduTraining.setConTextZhTw(tempContentZhTw);
		} else {
			ZkUtils.showExclamation(Labels.getLabel("eduTrain.content") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
			return;
		}

		webEduTraining.setLatelyChangedUser(webEmployee.getEmployeesn());
		webEduTraining.setLatelyChangedDate(new Date());
		((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).update(webEduTraining);
		((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEduTraining.getWebEmployee()
				.getEmployeeName(), "webedutraining_" + webEduTraining.getUuid());

		// 更新成功
		ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
		editSearchOrgList();
		editWebEduTrainWin.detach();
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
								+ SystemVariable.WEB_EDU_TRAINING_PATH;
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
								+ SystemVariable.WEB_EDU_TRAINING_PATH;
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
			log.error("下載檔異常" + e);
		}

	}

	@Listen("onClick=#clearBtn")
	public void clearInput() throws Exception {
		startDatedbx.setValue(null);
		titleTxt.setValue("");
		clickNumtbx.setValue(null);
		uploadFileNameTxt.setValue("");
		webSiteTbx.setValue("");
		contentCedit.setValue("");
	}

	@Listen("onClick=#delFile")
	public void delFile() {
		try {
			String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
					+ SystemVariable.WEB_EDU_TRAINING_PATH;
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
		Datebox startDateDboxs = (Datebox) editWebEduTrainWin.getParent().getFellowIfAny("startDateDbox");
		Datebox endDateDboxs = (Datebox) editWebEduTrainWin.getParent().getFellowIfAny("endDateDbox");
		Listbox WebEduTrainLix = (Listbox) editWebEduTrainWin.getParent().getFellowIfAny("WebEduTrainLix");
		try {
			Date startDate = startDateDboxs.getValue();
			Date endDate = endDateDboxs.getValue();

			List<WebEduTraining> webEduTrainList = ((WebEduTrainingService) SpringUtil.getBean("webEduTrainingService")).findByDate(startDate,
					endDate, webEmployee);
			ListModelList<WebEduTraining> modelList = new ListModelList<WebEduTraining>(webEduTrainList);
			modelList.setMultiple(true);
			WebEduTrainLix.setModel(modelList);
			WebEduTrainLix.setActivePage(currentPage);
			startDateDboxs.setValue(startDate);
			endDateDboxs.setValue(endDate);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("教育訓練訊息集合出錯", e);
		}
	}
}
