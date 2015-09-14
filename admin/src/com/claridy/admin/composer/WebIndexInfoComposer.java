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
import org.zkforge.ckez.CKeditor;
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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;
import com.claridy.facade.WebIndexInfoService;
import com.claridy.facade.WebSysLogService;

/**
 * zfdong nj 管理端登入頁資訊管理作業 新增 修改 2014/8/6
 */
public class WebIndexInfoComposer extends SelectorComposer<Component> {

	@Wire
	private Textbox tboxMatterZhTw;
	@Wire
	private Radiogroup rgroupstate;
	@Wire
	private Textbox tboxUploadFileName;
	@Wire
	private CKeditor newscontentZhTwTor;
	@Wire
	private Window editWebIndexInfoWin;
	@Wire
	private Button deleeNewsFile;
	@Wire
	private Button upload;
	@Wire
	private int currentPage;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@WireVariable
	private WebIndexInfo webIndexInfo;

	private static final long serialVersionUID = -3027143733569943497L;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String MatterZhTw = map.get("matterZhTw");
			currentPage = Integer.parseInt(map.get("currentPage"));
			webIndexInfo = ((WebIndexInfoService) SpringUtil.getBean("webIndexInfoService")).findByMatter(MatterZhTw);
			tboxMatterZhTw.setValue(MatterZhTw);
			if (webIndexInfo.getIsDisplay() == 0) {
				rgroupstate.setSelectedIndex(1);
			} else {
				rgroupstate.setSelectedIndex(0);
			}
			newscontentZhTwTor.setValue(webIndexInfo.getNewsContentZhTw());

			if (webIndexInfo.getUploadNewsFile() != null && !webIndexInfo.getUploadNewsFile().equals("")
					&& webIndexInfo.getUploadFileDisplayName() != null && !webIndexInfo.getUploadFileDisplayName().equals("")) {
				tboxUploadFileName.setValue(webIndexInfo.getUploadFileDisplayName());
			} else {
				deleeNewsFile.setVisible(false);
				upload.setVisible(true);
			}

		} catch (Exception e) {
			log.error("初始化webIndexInfo異常" + e);
		}
	}

	@Listen("onClick=#deleeNewsFile")
	public void lists() throws IOException {
		try {
			// String
			// realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webindexFileURL;
			String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
					+ SystemVariable.WEB_INDEXFILE_PATH;
			File file = new File(realPath + webIndexInfo.getUploadNewsFile());
			if (webIndexInfo.getUploadNewsFile() != null && !webIndexInfo.getUploadNewsFile().equals("") && file.exists()) {
				file.delete();
			}
			tboxUploadFileName.setValue("");
			webIndexInfo.setUploadNewsFile("");
			deleeNewsFile.setVisible(false);
			upload.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常" + e);
		}
	}

	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) throws IOException {
		Media media = event.getMedia();

		try {
			if (media != null) {
				if (media.isBinary()) {
					// String
					// realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webindexFileURL;
					String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
							+ SystemVariable.WEB_INDEXFILE_PATH;
					InputStream in = media.getStreamData();
					String uploadname = tboxUploadFileName.getValue();
					String FileName = RandomIDGenerator.getRandomId() + uploadname.substring(uploadname.lastIndexOf("."));
					File file = new File(realPath + FileName);
					tboxUploadFileName.setValue(uploadname);
					webIndexInfo.setUploadNewsFile(FileName);
					Files.copy(file, in);
					Files.close(in);
					in.close();
				} else {
					// String
					// realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webindexFileURL;
					String realPath = ((SystemProperties) SpringUtil.getBean("systemProperties")).portalDocementPath + "/"
							+ SystemVariable.WEB_INDEXFILE_PATH;
					File file = new File(realPath);
					String sr = media.getStringData();
					String fileName = RandomIDGenerator.getRandomId();
					String type = tboxUploadFileName.getValue();
					type = type.substring(type.lastIndexOf("."));
					File files = File.createTempFile(fileName, type, file);
					webIndexInfo.setUploadNewsFile(files.getName());
					FileWriter fw = new FileWriter(files);
					fw.write(sr);
					fw.close();

				}
				deleeNewsFile.setVisible(true);
				upload.setVisible(false);
			}
		} catch (WrongValueException e) {
			log.error("下載檔異常" + e);
		}

	}

	@Listen("onClick=#updateBtn")
	public void update() {
		try {
			if (newscontentZhTwTor.getValue() == null || newscontentZhTwTor.getValue().equals("")) {
				ZkUtils.showExclamation(Labels.getLabel("notNewsConten"), Labels.getLabel("info"));
				return;
			} else {
				String matterZHTw = tboxMatterZhTw.getValue().trim();
				String upLoadFileName = tboxUploadFileName.getValue().trim();
				String newscontentZhTw = newscontentZhTwTor.getValue().trim();
				String state = rgroupstate.getSelectedItem().getValue();
				matterZHTw = XSSStringEncoder.encodeXSSString(matterZHTw);
				upLoadFileName = XSSStringEncoder.encodeXSSString(upLoadFileName);
				webIndexInfo.setMatterZhTw(matterZHTw);
				webIndexInfo.setUploadFileDisplayName(upLoadFileName);
				webIndexInfo.setNewsContentZhTw(newscontentZhTw);
				if (Integer.parseInt(state) == 0) {
					webIndexInfo.setIsDisplay(1);
				} else {
					webIndexInfo.setIsDisplay(0);
				}

				WebEmployee webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				webIndexInfo.setLatelyChangedDate(new Date());
				webIndexInfo.setLatelyChangedUser(webEmployee.getEmployeesn());
				((WebSysLogService) SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(), webEmployee.getEmployeesn(), "index_"
						+ webIndexInfo.getUuid());
				((WebIndexInfoService) SpringUtil.getBean("webIndexInfoService")).updatewebIndexInfo(webIndexInfo);
				ZkUtils.showInformation(Labels.getLabel("updateOK"), Labels.getLabel("info"));
				editSearchOrgList();
				editWebIndexInfoWin.detach();
			}
		} catch (Exception e) {
			log.error("編輯webIndexInfo異常" + e);
		}
	}

	public void editSearchOrgList() {
		Textbox tboxMatterZhTw = (Textbox) editWebIndexInfoWin.getParent().getFellowIfAny("tboxmatterZhTw");
		Listbox ermSysPhoneticLix = (Listbox) editWebIndexInfoWin.getParent().getFellowIfAny("webIndexLix");
		try {
			String tboxMatter = "";
			if(tboxMatterZhTw!=null){
				tboxMatter=tboxMatterZhTw.getValue().trim();
			}
			tboxMatter = XSSStringEncoder.encodeXSSString(tboxMatter);
			WebEmployee webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebIndexInfo> webIndexList = ((WebIndexInfoService) SpringUtil.getBean("webIndexInfoService")).findByName(tboxMatter, webEmployee);
			ListModelList<WebIndexInfo> tmp = new ListModelList<WebIndexInfo>(webIndexList);
			tmp.setMultiple(true);
			ermSysPhoneticLix.setModel(tmp);
			ermSysPhoneticLix.setActivePage(currentPage);
			if(tboxMatterZhTw!=null){
				tboxMatterZhTw.setValue(tboxMatter);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("webIndexInfo集合出錯", e);
		}
	}

}
