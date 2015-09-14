package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.ErmResourcesOdetails;
import com.claridy.domain.ErmResourcesOdetailsUploadfile;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.ErmCategoryService;
import com.claridy.facade.ErmResourcesOdetailsService;
import com.claridy.facade.ErmResourcesOdetailsUploadFileService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;

/**
 * 
 * sunchao nj
 * 採購細節新增編輯頁面
 * 2014/09/23
 */
public class ErmOdetailsComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox resIdBox;
	@Wire
	private Textbox yearBox;
	@Wire
	private Textbox moneyBox;
	@Wire
	private Combobox remarkCbx;
	@Wire
	private Datebox starOrderDateBox;
	@Wire
	private Datebox endOrderDateBox;
	@Wire
	private Label agentedIdBox;
	@Wire
	private Textbox agentedNameBox;
	@Wire
	private Textbox orderUserBox;
	@Wire
	private Textbox noteBox;
	@Wire
	private Textbox uploadFileNameTxt;
	@Wire
	private Button upload;
	@Wire
	private Button delFile;
	private String fileName;
	private String uploadFileName;
	private String resId;
	private String resourcesId="";
	private String yearId="";
	
	private ErmResourcesOdetails odetails;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String resYearId=map.get("resYearId");
			//String resourcesId="";
			//String yearId="";
			String[] str=resYearId.split(",");
			if(str.length==1){
				resourcesId=str[0];
				resId=str[0];
			}
			if(str.length==2){
				resourcesId=str[0];
				resId=str[0];
				yearId=str[1];
			}
			if(yearId!=null&&!"".equals(yearId)){
				odetails=((ErmResourcesOdetailsService)SpringUtil.getBean("ermResourcesOdetailsService")).getOdetails(resourcesId, yearId);
				resIdBox.setValue(odetails.getResourcesId());
				resIdBox.setReadonly(true);
				yearBox.setReadonly(true);
				yearBox.setValue(odetails.getYear());
				moneyBox.setValue(odetails.getMoney());
				starOrderDateBox.setValue(odetails.getStarOrderDate());
				endOrderDateBox.setValue(odetails.getEndOrderDate());
				agentedIdBox.setValue(odetails.getAgentedId());
				ErmResourcesOdetailsUploadfile ermResourcesUploadfile=((ErmResourcesOdetailsUploadFileService) SpringUtil
						.getBean("ermResourcesOdetailsUploadFileService"))
						.getUpLoadFileByResIdAndYear(resourcesId, yearId);
				if(ermResourcesUploadfile!=null){
					uploadFileNameTxt.setValue(ermResourcesUploadfile.getUploadFile());
					delFile.setVisible(true);
					upload.setVisible(false);
				}else{
					delFile.setVisible(false);
					upload.setVisible(true);
				}
				if(odetails.getAgentedId()!=null&&!"".equals(odetails.getAgentedId())){
					ErmCodePublisher publisherTemp=((CodePublisherService)SpringUtil.getBean("codePublisherService")).findByPublisherID(odetails.getAgentedId());
					agentedNameBox.setValue(publisherTemp.getName());
				}
				orderUserBox.setValue(odetails.getOrderUser());
				noteBox.setValue(odetails.getNote());
				agentedNameBox.setDisabled(true);
			}else{
				odetails=new ErmResourcesOdetails();
				resIdBox.setValue(str[0]);
				resIdBox.setReadonly(true);
				agentedNameBox.setDisabled(true);
			}
			//查詢採購註記
			List<ErmCodeGeneralCode> remarkLst=((ErmCategoryService) SpringUtil
					.getBean("ermCategoryService")).findGeneralCodeByItemId("PURE");
			Comboitem remarkCom=new Comboitem();
			remarkCom.setLabel(Labels.getLabel("select"));
			remarkCom.setValue("");
			remarkCbx.appendChild(remarkCom);
			for(int i=0;i<remarkLst.size();i++){
				remarkCom=new Comboitem();
				ErmCodeGeneralCode generalCodeTmp=remarkLst.get(i);
				remarkCom.setLabel(generalCodeTmp.getName1());
				remarkCom.setValue(generalCodeTmp.getGeneralcodeId());
				remarkCbx.appendChild(remarkCom);
				if(odetails.getYear()!=null&&!"".equals(odetails.getYear())){
					if(generalCodeTmp.getGeneralcodeId().equals(odetails.getRemarkId())){
						remarkCbx.setSelectedIndex(i+1);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("採購細節進入新增或修改頁面報錯",e);
		}
	}
	@Listen("onClick=#saveBtn")
	public void saveBtn(){
		try {
			odetails=new ErmResourcesOdetails();
			String year=yearBox.getValue();
			if (StringUtils.isBlank(year)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.odetailsYear") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				yearBox.focus();
				return;
			}
			odetails.setResourcesId(resIdBox.getValue());
			ErmResourcesOdetails odetailsTemp=((ErmResourcesOdetailsService)SpringUtil.getBean("ermResourcesOdetailsService")).getOdetails(resIdBox.getValue(), year);
			if(odetailsTemp.getYear()!=null&&!"".equals(odetailsTemp.getYear())){
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.dataExists"),
						Labels.getLabel("warn"));
				yearBox.focus();
				return;
			}else{
				odetails.setYear(yearBox.getValue());
				odetails.setMoney(moneyBox.getValue());
				if(remarkCbx.getSelectedItem()!=null){
					odetails.setRemarkId(remarkCbx.getSelectedItem().getValue().toString());
				}
				odetails.setStarOrderDate(starOrderDateBox.getValue());
				odetails.setEndOrderDate(endOrderDateBox.getValue());
				odetails.setAgentedId(agentedIdBox.getValue());
				odetails.setOrderUser(orderUserBox.getValue());
				odetails.setNote(noteBox.getValue());
				String tempUploadFileName = "";
				if (uploadFileNameTxt.getValue() != null
						&& !"".equals(uploadFileNameTxt.getValue())) {
					tempUploadFileName = uploadFileNameTxt.getValue();
					tempUploadFileName = XSSStringEncoder
							.encodeXSSString(tempUploadFileName);
					ErmResourcesOdetailsUploadfile ermResourcesUploadfile = new ErmResourcesOdetailsUploadfile();
					ermResourcesUploadfile.setIsDataEffid(1);
					ermResourcesUploadfile.setResourcesId(resId);
					ermResourcesUploadfile.setUuid(fileName);
					ermResourcesUploadfile.setYear(yearBox.getValue());
					ermResourcesUploadfile.setUploadFile(uploadFileNameTxt
							.getValue());
					((ErmResourcesOdetailsUploadFileService) SpringUtil
							.getBean("ermResourcesOdetailsUploadFileService"))
							.addErmResourceOderUploadFile(ermResourcesUploadfile);
				}
				((ErmResourcesOdetailsService)SpringUtil.getBean("ermResourcesOdetailsService")).create(odetails);
			}
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
			String resourcesId=odetails.getResourcesId();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
			//更新solr數據
			if(resourcesId!=null&&!"".equals(resourcesId)){
				String resStr=resourcesId.substring(0,2);
				if(resStr.equals("EJ")||resStr.equals("EB")){
					((ResourcesMainEjebSolrSearch) SpringUtil
							.getBean("resourcesMainEjebSolrSearch"))
							.resources_main_ejeb_editData(resourcesId);
				}else{
					((ResourcesMainDbwsSolrSearch) SpringUtil
							.getBean("resourcesMainDbwsSolrSearch"))
							.resources_main_dbws_editData(resourcesId);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("採購細節新增方法報錯",e);
		}
	}
	@Listen("onClick=#updBtn")
	public void updBtn(){
		try {
			odetails.setMoney(moneyBox.getValue());
			if(remarkCbx.getSelectedItem()!=null){
				odetails.setRemarkId(remarkCbx.getSelectedItem().getValue().toString());
			}
			odetails.setStarOrderDate(starOrderDateBox.getValue());
			odetails.setEndOrderDate(endOrderDateBox.getValue());
			odetails.setAgentedId(agentedIdBox.getValue());
			odetails.setOrderUser(orderUserBox.getValue());
			odetails.setNote(noteBox.getValue());
			String tempUploadFileName = "";
			if (uploadFileNameTxt.getValue() != null
					&& !"".equals(uploadFileNameTxt.getValue())) {
				tempUploadFileName = uploadFileNameTxt.getValue();
				tempUploadFileName = XSSStringEncoder
						.encodeXSSString(tempUploadFileName);
				ErmResourcesOdetailsUploadfile ermResourcesUploadfile = new ErmResourcesOdetailsUploadfile();
				ermResourcesUploadfile.setIsDataEffid(1);
				ermResourcesUploadfile.setResourcesId(resId);
				ermResourcesUploadfile.setUuid(fileName);
				ermResourcesUploadfile.setYear(yearBox.getValue());
				ermResourcesUploadfile.setUploadFile(uploadFileNameTxt
						.getValue());
				((ErmResourcesOdetailsUploadFileService) SpringUtil
						.getBean("ermResourcesOdetailsUploadFileService"))
						.addErmResourceOderUploadFile(ermResourcesUploadfile);
			}
			((ErmResourcesOdetailsService)SpringUtil.getBean("ermResourcesOdetailsService")).update(odetails);
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"),Labels.getLabel("info"));
			String resourcesId=odetails.getResourcesId();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
			//更新solr數據
			if(resourcesId!=null&&!"".equals(resourcesId)){
				String resStr=resourcesId.substring(0,2);
				if(resStr.equals("EJ")||resStr.equals("EB")){
					((ResourcesMainEjebSolrSearch) SpringUtil
							.getBean("resourcesMainEjebSolrSearch"))
							.resources_main_ejeb_editData(resourcesId);
				}else{
					((ResourcesMainDbwsSolrSearch) SpringUtil
							.getBean("resourcesMainDbwsSolrSearch"))
							.resources_main_dbws_editData(resourcesId);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("採購細節修改方法報錯",e);
		}
	}
	@Listen("onClick=#selectBtn")
	public void searchAgented(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("openValue", "agented");
		map.put("openType", "odetails");
		Executions.createComponents("/WEB-INF/pages/system/ermResMainDbws/ermResPubOpen.zul", this.getSelf(),map);
	}
	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) {
		try {
			Media media = event.getMedia();
			int size = media.getByteData().length;
			String oldUpStr = uploadFileNameTxt.getValue();
			String resType=resId.substring(0,2);
			String hzStr = oldUpStr.substring(oldUpStr.lastIndexOf(".") + 1);
			if (hzStr.equals("pdf") || hzStr.equals("doc")
					|| hzStr.equals("xls") || hzStr.equals("ppt")
					|| hzStr.equals("txt") || hzStr.equals("docx")
					|| hzStr.equals("pptx") || hzStr.equals("xlsx")) {
				if (size <= 10485760) {
					if (media != null) {
						if (media.isBinary()) {
							Desktop dtp = Executions.getCurrent().getDesktop();
							String realPath="";
							if(resType.equals("DB")){
								 realPath = ((SystemProperties) SpringUtil
											.getBean("systemProperties")).portalDocementPath
											+ "/"
											+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							}else{
								 realPath =((SystemProperties) SpringUtil
										.getBean("systemProperties")).portalDocementPath
										+ "/"
										+ SystemVariable.ERM_RES_EJEBFILE_PATH;
							}
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
							String realPath="";
							if(resType.equals("DB")){
								 realPath = ((SystemProperties) SpringUtil
											.getBean("systemProperties")).portalDocementPath
											+ "/"
											+ SystemVariable.ERM_RES_DBWSFILE_PATH;
							}else{
								 realPath =((SystemProperties) SpringUtil
										.getBean("systemProperties")).portalDocementPath
										+ "/"
										+ SystemVariable.ERM_RES_EJEBFILE_PATH;
							}
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
	@Listen("onClick=#delFile")
	public void delFile() {
		try {
			Desktop dtp = Executions.getCurrent().getDesktop();
			String resType=resId.substring(0,2);
			/*
			 * String realPath = dtp .getSession() .getWebApp() .getRealPath(
			 * "/document/uploadfile/webEduTraining/") + "/";
			 */
//			String realPath = ((SystemProperties) SpringUtil
//					.getBean("systemProperties")).webEduTrainingDocementPath
//					+ "/";
			String realPath ="";
			if(resType.equals("DB")){
				 realPath = ((SystemProperties) SpringUtil
							.getBean("systemProperties")).portalDocementPath
							+ "/"
							+ SystemVariable.ERM_RES_DBWSFILE_PATH;
			}else{
				 realPath =((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"
						+ SystemVariable.ERM_RES_EJEBFILE_PATH;
			}
			File file = new File(realPath + fileName);
			if (fileName != null && !fileName.equals("") && file.exists()) {
				file.delete();
			}
			ErmResourcesOdetailsUploadfile ermResourcesUploadfile=((ErmResourcesOdetailsUploadFileService) SpringUtil
					.getBean("ermResourcesOdetailsUploadFileService"))
					.getUpLoadFileByResIdAndYear(resourcesId, yearId);
			if(ermResourcesUploadfile!=null){
				((ErmResourcesOdetailsUploadFileService) SpringUtil
						.getBean("ermResourcesOdetailsUploadFileService")).delErmResourceOderUploadFile(ermResourcesUploadfile);
			}
			uploadFileNameTxt.setValue("");
			delFile.setVisible(false);
			upload.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常" + e);
		}
	}
}
