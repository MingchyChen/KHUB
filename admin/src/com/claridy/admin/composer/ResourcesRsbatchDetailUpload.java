package com.claridy.admin.composer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFObjectData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.zkoss.util.Locales;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.ResourcesMainfileSolrSearch;

/********************************************************
 * 程序名稱：副檔上傳 作者： sunchao 創建時間：2014-10-17 15：31
 * 
 ********************************************************/
public class ResourcesRsbatchDetailUpload extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3369800884614058465L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Label pagePath;// 頁面路徑
	@Wire
	private Label detail_type;// 匯入資料
	@Wire
	private Listbox DBtype_listbox;// 電子資源類型
	@Wire
	private Textbox filePathAndNameShow;// 上傳文件名
	@Wire
	private Fileupload fileUpload;// 上傳
	@Wire
	private Button btn_fileUpload;
	@Wire
	private Label warn1;
	@Wire
	private Label warn3;
	@Wire
	private Toolbarbutton db_title;
	@Wire
	private Toolbarbutton db_subject;
	@Wire
	private Toolbarbutton db_type;
	@Wire
	private Toolbarbutton db_college_sy;
	
	@Wire
	private Toolbarbutton ej_title;
	@Wire
	private Toolbarbutton ej_subject;
	@Wire
	private Toolbarbutton ej_college_sy;
	@Wire
	private Toolbarbutton ej_department_sy;
	@Wire
	private Toolbarbutton ej_college_dg;
	@Wire
	private Toolbarbutton ej_department_dg;

	@Wire
	private Toolbarbutton eb_title;
	@Wire
	private Toolbarbutton eb_subject;
	@Wire
	private Toolbarbutton eb_college_sy;
	@Wire
	private Toolbarbutton eb_department_sy;
	@Wire
	private Toolbarbutton eb_college_dg;
	@Wire
	private Toolbarbutton eb_department_dg;

	@Wire
	private Toolbarbutton ws_title;
	@Wire
	private Toolbarbutton ws_subject;
	@Wire
	private Toolbarbutton ws_college_sy;
	@Wire
	private Toolbarbutton ws_department_sy;

	private String webprjpath=((SystemProperties) SpringUtil
			.getBean("systemProperties")).systemDocumentPath
			+ SystemVariable.RSBATCH_PATH;
	private String title = "Title.xls";// 副檔相關題名
	private String subject = "Subjects.xls";// 副檔主題分類
	private String type = "DBType.xls";// 副檔資料類型
	private String college_sy = "suitcol.xls";// 副檔適用學院
	private String department_sy = "Suitdep.xls";// 副檔適用系所
	private String college_dg = "Colleges.xls";// 副檔訂購學院
	private String department_dg = "Department.xls";// 副檔訂購系所
	
	private String languageTag = "zh_TW";// 記錄多語系
	// 多語系
	private String dbtitle, dbsubject, dbtype, dbcollege_sy, ejtitle,
			ejsubject, ejcollege_sy, ejdepartment_sy, ejcollege_dg,
			ejdepartment_dg, ebtitle, ebsubject, ebcollege_sy, ebdepartment_sy,
			ebcollege_dg, ebdepartment_dg, wstitle, wssubject, wscollege_sy,
			wsdepartment_sy;

	private List<String[]> ExcelList = null;// excel中的數據
	private List<String[]> existDBList = null;// 數據庫中已存在數據
	private List<String[]> nExistList = null;// 未存在數據
	private String resources_type = "";// 電子資源類型
	private String detailType = "";// 副檔類型
	private String pleasechoose, uploadfile, importfile, pleaseupload,
			warn1mess, warn2mess, dbFormats, ejFormats, ebFormats, wsFormats,
			uploadmessage, warnMessage, checkmessage, message1, message2,
			message3, message4, columnName, DBDetails, EJorEBDetails,
			WSDetails;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			pleasechoose = Labels.getLabel("select");
			uploadfile = Labels
					.getLabel("resources_rsbatch_detail_upload.uploadfile");
			importfile = Labels
					.getLabel("resources_rsbatch_detail_upload.importfile");
			pleaseupload = Labels
					.getLabel("resources_rsbatch_detail_upload.pleaseupload");
			warn1mess = Labels.getLabel("resources_rsbatch_detail_upload.warn1");
			warn2mess = Labels.getLabel("resources_rsbatch_detail_upload.warn2");
			
			uploadmessage = Labels.getLabel("resources_rsbatch_detail_upload.uploadmessage");
			warnMessage = Labels.getLabel("warnMessage");
			checkmessage = Labels
					.getLabel("resources_rsbatch_detail_upload.checkmessage");
			message1 = Labels.getLabel("resources_rsbatch_detail_upload.message1");
			message2 = Labels.getLabel("resources_rsbatch_detail_upload.message2");
			message3 = Labels.getLabel("resources_rsbatch_detail_upload.message3");
			message4 = Labels.getLabel("resources_rsbatch_detail_upload.message4");
			columnName = Labels.getLabel("resources_rsbatch_detail_upload.name");
			DBDetails = Labels
					.getLabel("resources_rsbatch_detail_upload.DBDetails");
			EJorEBDetails = Labels
					.getLabel("resources_rsbatch_detail_upload.EJorEBDetails");
			WSDetails = Labels
					.getLabel("resources_rsbatch_detail_upload.WSDetails");

			dbtitle = Labels.getLabel("resources_rsbatch_detail_upload.dbtitle");
			dbsubject = Labels
					.getLabel("resources_rsbatch_detail_upload.dbsubject");
			dbtype = Labels.getLabel("resources_rsbatch_detail_upload.dbtype");
			/*dbcollege_sy = Labels.getLabel("resources_rsbatch_detail_upload.dbcollege_sy");*/
			ejtitle = Labels.getLabel("resources_rsbatch_detail_upload.ejtitle");
			ejsubject = Labels.getLabel("resources_rsbatch_detail_upload.ejsubject");
			ejcollege_sy = Labels.getLabel("resources_rsbatch_detail_upload.ejcollege_sy");
			ejdepartment_sy = Labels.getLabel("resources_rsbatch_detail_upload.ejdepartment_sy");
			ejcollege_dg = Labels.getLabel("resources_rsbatch_detail_upload.ejcollege_dg");
			ejdepartment_dg = Labels.getLabel("resources_rsbatch_detail_upload.ejdepartment_dg");
			ebtitle = Labels.getLabel("resources_rsbatch_detail_upload.ebtitle");
			ebsubject = Labels.getLabel("resources_rsbatch_detail_upload.ebsubject");
			ebcollege_sy = Labels.getLabel("resources_rsbatch_detail_upload.ebcollege_sy");
			ebdepartment_sy = Labels.getLabel("resources_rsbatch_detail_upload.ebdepartment_sy");
			ebcollege_dg = Labels.getLabel("resources_rsbatch_detail_upload.ebcollege_dg");
			ebdepartment_dg = Labels.getLabel("resources_rsbatch_detail_upload.ebdepartment_dg");
			wstitle = Labels.getLabel("resources_rsbatch_detail_upload.wstitle");
			wssubject = Labels.getLabel("resources_rsbatch_detail_upload.wssubject");
			wscollege_sy = Labels.getLabel("resources_rsbatch_detail_upload.wscollege_sy");
			wsdepartment_sy = Labels.getLabel("resources_rsbatch_detail_upload.wsdepartment_sy");
			
			List<ErmCodeGeneralCode> ermCodeLAllist=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(webEmployee);
			if(ermCodeLAllist.size()>0){
				for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodeLAllist){
					Listitem item = new Listitem();
					item.setLabel(ermCodeGeneralCode.getName1());
					item.setValue(ermCodeGeneralCode.getGeneralcodeId());
					DBtype_listbox.appendChild(item);
				}
			}else{
				Listitem item = new Listitem();
				item.setLabel(pleasechoose);// 請選擇
				item.setValue("pleaseChoose");
				DBtype_listbox.appendChild(item);
			}
			DBtype_listbox.setSelectedIndex(0);
			
			pagePath.setValue(uploadfile);// 上傳檔案
			// 副檔類型
			detail_type.setValue(importfile);// 匯入資料
			DBtype_listbox.setMold("select");
			fileUpload.setLabel(pleaseupload);// 請上傳
			btn_fileUpload.setLabel(pleaseupload);
			warn1.setValue(warn1mess);// *為確保系統處理效能，建議一次上傳比對的的筆數，以1000筆為限
			warn3.setValue(warn2mess);// 下載上傳電子資源範例格式

			db_title.setLabel(dbtitle);
			db_subject.setLabel(dbsubject);
			db_type.setLabel(dbtype);
			db_college_sy.setLabel(dbcollege_sy);
			ej_title.setLabel(ejtitle);
			ej_subject.setLabel(ejsubject);
			ej_college_sy.setLabel(ejcollege_sy);
			ej_department_sy.setLabel(ejdepartment_sy);
			ej_college_dg.setLabel(ejcollege_dg);
			ej_department_dg.setLabel(ejdepartment_dg);
			eb_title.setLabel(ebtitle);
			eb_subject.setLabel(ebsubject);
			eb_college_sy.setLabel(ebcollege_sy);
			eb_department_sy.setLabel(ebdepartment_sy);
			eb_college_dg.setLabel(ebcollege_dg);
			eb_department_dg.setLabel(ebdepartment_dg);
			ws_title.setLabel(wstitle);
			ws_subject.setLabel(wssubject);
			ws_college_sy.setLabel(wscollege_sy);
			ws_department_sy.setLabel(wsdepartment_sy);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#db_title")
	public void onClick$db_title() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setTitle("Title_zh_cn.xls");
			doEncoding(this.getTitle(), "Title_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getTitle(), "Title" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#db_subject")
	public void onClick$db_subject() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setSubject("Subjects_zh_cn.xls");
			doEncoding(this.getSubject(), "Subjects_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getSubject(), "Subjects" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#db_type")
	public void onClick$db_type() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setType("DBType_zh_cn.xls");
			doEncoding(this.getType(), "DBType_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getType(), "DBType" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#db_college_sy")
	public void onClick$db_college_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_sy("Suitcol_zh_cn.xls");
			doEncoding(this.getCollege_sy(), "Suitcol_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_sy(), "Suitcol" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_title")
	public void onClick$ej_title() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setTitle("Title_zh_cn.xls");
			doEncoding(this.getTitle(), "Title_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getTitle(), "Title" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_subject")
	public void onClick$ej_subject() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setSubject("Subjects_zh_cn.xls");
			doEncoding(this.getSubject(), "Subjects_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getSubject(), "Subjects" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_college_sy")
	public void onClick$ej_college_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_sy("Suitcol_zh_cn.xls");
			doEncoding(this.getCollege_sy(), "Suitcol_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_sy(), "Suitcol" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_department_sy")
	public void onClick$ej_department_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setDepartment_sy("Suitdep_zh_cn.xls");
			doEncoding(this.getDepartment_sy(), "Suitdep_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getDepartment_sy(), "Suitdep" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_college_dg")
	public void onClick$ej_college_dg() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_dg("Colleges_zh_cn.xls");
			doEncoding(this.getCollege_dg(), "Colleges_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_dg(), "Colleges" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ej_department_dg")
	public void onClick$ej_department_dg() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setDepartment_dg("Department_zh_cn.xls");
			doEncoding(this.getDepartment_dg(), "Department_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getDepartment_dg(), "Department" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_title")
	public void onClick$eb_title() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setTitle("Title_zh_cn.xls");
			doEncoding(this.getTitle(), "Title_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getTitle(), "Title" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_subject")
	public void onClick$eb_subject() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setSubject("Subjects_zh_cn.xls");
			doEncoding(this.getSubject(), "Subjects_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getSubject(), "Subjects" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_college_sy")
	public void onClick$eb_college_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_sy("Suitcol_zh_cn.xls");
			doEncoding(this.getCollege_sy(), "Suitcol_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_sy(), "Suitcol" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_department_sy")
	public void onClick$eb_department_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setDepartment_sy("Suitdep_zh_cn.xls");
			doEncoding(this.getDepartment_sy(), "Suitdep_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getDepartment_sy(), "Suitdep" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_college_dg")
	public void onClick$eb_college_dg() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_dg("Colleges_zh_cn.xls");
			doEncoding(this.getCollege_dg(), "Colleges_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_dg(), "Colleges" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#eb_department_dg")
	public void onClick$eb_department_dg() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setDepartment_dg("Department_zh_cn.xls");
			doEncoding(this.getDepartment_dg(), "Department_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getDepartment_dg(), "Department" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ws_title")
	public void onClick$ws_title() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setTitle("Title_zh_cn.xls");
			doEncoding(this.getTitle(), "Title_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getTitle(), "Title" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ws_subject")
	public void onClick$ws_subject() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setSubject("Subjects_zh_cn.xls");
			doEncoding(this.getSubject(), "Subjects_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getSubject(), "Subjects" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ws_college_sy")
	public void onClick$ws_college_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setCollege_sy("Suitcol_zh_cn.xls");
			doEncoding(this.getCollege_sy(), "Suitcol_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getCollege_sy(), "Suitcol" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 下載範本
	 * 
	 * @throws FileNotFoundException
	 */
	@Listen("onClick=#ws_department_sy")
	public void onClick$ws_department_sy() throws FileNotFoundException {
		if (languageTag.equals("zh_CN")) {// 如果是簡體中文
			this.setDepartment_sy("Suitdep_zh_cn.xls");
			doEncoding(this.getDepartment_sy(), "Suitdep_zh_cn" + ".xls");// 多國語里的dbtitle
		} else
			doEncoding(this.getDepartment_sy(), "Suitdep" + ".xls");// 多國語里的dbtitle
	}

	/**
	 * 資源下載亂碼處理
	 */
	public void doEncoding(String fileName, String flnm) {
		// 編碼後文件名
		String encodedName = null;
		try {
			encodedName = URLEncoder.encode(flnm, "UTF-8");// 轉換字符編碼
			String path = webprjpath;// 獲取服務器路徑
			// Filedownload
			Filedownload.save(new FileInputStream(path + fileName),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 上傳
	 */
	@Listen("onUpload=#fileUpload")
	public void onUpload$fileUpload(UploadEvent event) {
		Desktop dt = Executions.getCurrent().getDesktop();
		Configuration config = dt.getWebApp().getConfiguration();
		// 单位 KB ，如果为负则不限制大小
		config.setMaxUploadSize(1024);
		// 中文亂碼
		config.setUploadCharset("UTF-8");
		Media media = event.getMedia();
		if (media == null) {
			return;
		}
		String fileName = media.getName();// 文件名
		filePathAndNameShow.setValue(fileName);
		if (!fileName.toLowerCase().endsWith(".xls")) {
			Messagebox.show(uploadmessage, warnMessage, Messagebox.OK,
					Messagebox.INFORMATION);// "上傳的文件格式不正確，請重新上傳","提示信息"
			return;
		}
		if (media.isBinary()) {
			InputStream ins = media.getStreamData();
			if (ins != null) {
				String result = readExcel(ins);
				if (result != null) {
					Messagebox.show(result, warnMessage, Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}
		} else {
			Messagebox.show(checkmessage, warnMessage, Messagebox.OK,
					Messagebox.INFORMATION);// "請檢查上傳文件的格式","提示信息"
			return;
			// Reader reader=media.getReaderData();
		}
		// 電子資源類型
		Sessions.getCurrent().setAttribute("resources_Type", resources_type);
		// 副檔類型
		Sessions.getCurrent().setAttribute("detail_Type", detailType);
		// 已存在數據
		Sessions.getCurrent().setAttribute("existDBList", existDBList);
		// 未存在數據
		Sessions.getCurrent().setAttribute("nExistList", nExistList);

		String url="resourcesFileImport/resourcesRsbatchDetailResult.zul";
		ZkUtils.refurbishMethod(url);
	}

	/**
	 * 處理Excel
	 */
	public String readExcel(InputStream ins) {
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;

		try {
			workbook = new HSSFWorkbook(ins);
			sheet = workbook.getSheetAt(0);
			ins.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		ExcelList = new ArrayList<String[]>();
		if (sheet != null) {
			int rowNum = 0;
			int cellNum = 0;

			rowNum = sheet.getLastRowNum();
			// 判斷Excel數據的筆數是否大於1000筆
			if (rowNum > 1000) {
				return message1;// 一次最多只能上傳1000筆數據！
			} else if (rowNum < 1) {
				return message2;// 請不要上傳空的Excel！
			} else {
				HSSFRow row = null;
				HSSFCell cell = null;
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

				// 檢查第一行第二列的表頭是否符合
				String cellValue = sheet.getRow(0).getCell(1)
						.getRichStringCellValue().toString().trim();
				boolean isTrue = checkDetailColumns(cellValue);
				// 如果上傳文檔與資源類型不匹配
				if (!isTrue) {
					return message3;// 請確認上傳的文檔與资料类型不符
				}
				getTagByDetail(cellValue);
				// TODO
				for (int i = 1; i <= rowNum; i++) {

					row = sheet.getRow(i);
					if (row != null) {
						// 儲存Excel數據
						String[] excelDateArray = new String[2];
						cellNum = row.getLastCellNum();

						boolean bool = true;
						for (int j = 0; j < cellNum; j++) {
							cell = row.getCell(j);

							if (cell != null) {
								switch (cell.getCellType()) {
								case HSSFCell.CELL_TYPE_STRING:
									excelDateArray[j] = cell
											.getStringCellValue();
									break;
								case HSSFCell.CELL_TYPE_NUMERIC:
									if (HSSFDateUtil.isCellDateFormatted(cell)) { // if
																					// date
										double d = cell.getNumericCellValue();
										excelDateArray[j] = dateFormat
												.format(HSSFDateUtil
														.getJavaDate(d));
									} else {
										// 數字類型轉換為int
										excelDateArray[j] = String
												.valueOf((int) cell
														.getNumericCellValue());
									}
									break;
								default:
									bool = false;
									// excelDateArray[j] =
									// cell.getStringCellValue();
									break;
								}
							}
						}
						// 如果數據都有值
						if (bool) {
							ExcelList.add(excelDateArray);
						}

					}
				}

			}
		}
		// 根據Excel的數據去數據庫中查詢是否有相同題名的數據存在
		isExistData(resources_type);
		return null;

	}

	/**
	 * 根據資料類型與副檔是否匹配 如果返回""則說明上傳的文檔不屬於此資料類型，否則返回資料類型
	 */
	public boolean checkDetailColumns(String column) {
		boolean bool = false;
		String[] DBDetail = DBDetails.split("[,]");// 相關題名,主題,類型,適用學院
		String[] EJorEBDetail = EJorEBDetails.split("[,]");// 相關題名,主題,適用學院,適用系所,訂購學院,訂購系所
		String[] WSDetail = WSDetails.split("[,]");// 相關題名,主題,適用學院,適用系所
		resources_type = DBtype_listbox.getSelectedItem().getValue().toString();
		String[] detailColumn = null;
		if (resources_type.equals("DB")) {
			detailColumn = DBDetail;
		} else if (resources_type.equals("EJ") || resources_type.equals("EB")) {
			detailColumn = EJorEBDetail;
		} else {
			detailColumn = WSDetail;
		}
		for (int i = 0; i < detailColumn.length; i++) {
			if (detailColumn[i].equals(column)) {
				// 如果進入此處，則說明此副檔和資源類型匹配
				bool = true;
				break;
			}
		}
		return bool;
	}

	/**
	 * 比對Excel與DB中的數據，查詢是否有相同題名的數據存在
	 */
	public void isExistData(String type) {
		existDBList = new ArrayList<String[]>();
		nExistList = new ArrayList<String[]>();
		if (ExcelList != null && ExcelList.size() > 0) {
			for (int i = 0; i < ExcelList.size(); i++) {
				String resources_id = "";
				String[] array = new String[3];
				String[] data = ExcelList.get(i);
				String title=data[0].replace("'", "''");
				String sql = "select resources_id,resources_id from erm_resources_mainfile_v where title='"
						+ title + "' and type_id='" + type + "'";
				try {
					List<Object> resourcesIdListTemp = ((ResourcesMainfileSolrSearch)SpringUtil.getBean("resourcesMainfileSolrSearch")).findObjectListBySql(sql);
					for (int t = 0; t < resourcesIdListTemp.size(); t++) {
						Object[] objTemp = (Object[]) resourcesIdListTemp.get(t);
						String r_id = (String)objTemp[0];
						resources_id = resources_id.equals("") ? r_id
								: (resources_id + "," + r_id);
					}
				} catch (Exception e) {
					log.error(e.getMessage(),e);
				}
				// 如果resources_id不為空，則說明此筆數據存在
				if (resources_id.equals("")) {
					array[0] = message4;// 沒有相同標題
					array[1] = data[0];
					array[2] = data[1];
					nExistList.add(array);
				} else {
					array[0] = resources_id;
					array[1] = data[0];
					array[2] = data[1];
					existDBList.add(array);
				}
			}
		}
	}

	/**
	 * 根據副檔的名稱來給定標識名稱
	 * 
	 * @return
	 */
	public void getTagByDetail(String column) {
		// 相關題名,主題,類型,適用學院,適用系所,訂購學院,訂購系所
		String[] name = columnName.split("[,]");
		String[] title = { "name", "subject", "type", "suitcollege", "suitdep",
				"orderCollege", "orderdep" };
		for (int i = 0; i < name.length; i++) {
			if (name[i].equals(column)) {
				detailType = title[i];
				break;
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCollege_sy() {
		return college_sy;
	}

	public void setCollege_sy(String college_sy) {
		this.college_sy = college_sy;
	}

	public String getDepartment_sy() {
		return department_sy;
	}

	public void setDepartment_sy(String department_sy) {
		this.department_sy = department_sy;
	}

	public String getCollege_dg() {
		return college_dg;
	}

	public void setCollege_dg(String college_dg) {
		this.college_dg = college_dg;
	}

	public String getDepartment_dg() {
		return department_dg;
	}

	public void setDepartment_dg(String department_dg) {
		this.department_dg = department_dg;
	}

}

