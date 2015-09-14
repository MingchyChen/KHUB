package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;

public class ResourcesRsBatchUploadComposer extends SelectorComposer<Component>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382042212446708465L;
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Button upload;
	@Wire
	private Button delFile;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private String uploadFileName;
	private String fileName;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
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
									.getBean("systemProperties")).systemDocumentPath
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
									.getBean("systemProperties")).systemDocumentPath
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
