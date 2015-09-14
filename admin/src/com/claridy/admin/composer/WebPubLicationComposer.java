package com.claridy.admin.composer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ProcessImage;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebPublication;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebPublicationService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 農業出版品 新增 修改
 * 2014/8/6
 */
public class WebPubLicationComposer extends SelectorComposer<Component> {

	
	private static final long serialVersionUID = 4736697491405942793L;
	@Wire
	private Textbox tboxtitle;
	@Wire
	private Textbox tboxAuthor;
	@Wire
	private Textbox isbntbox;
	@Wire
	private Radiogroup isShowRdb;
	@Wire
	private Textbox publishertbox;
	@Wire
	private Datebox byeartdbox;
	@Wire
	private Image img;
	@Wire
	private Intbox clickNumtbox;
	@Wire
	private Textbox destbox;
	@Wire
	private Window addWenPubLicationWin;
	@Wire
	private Window editWebPubLicationWin;
	@Wire
	private Button deleeImg;
	@Wire
	private Button upload;
	@Wire
	private Textbox issntbox;
	@Wire
	private Textbox url;
	@Wire
	private Textbox ebookURL;
	@Wire
	private Textbox pdfurl;
	
	private int currentPage;
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	private WebPublication webPubLication;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String uuid=map.get("uuid");
			if(map.get("currentPage")!=null&&!map.get("currentPage").equals("")){
				currentPage=Integer.parseInt(map.get("currentPage"));	
			}
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if(uuid!=null){
				webPubLication=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findById(uuid,loginwebEmployee);
				tboxtitle.setValue(webPubLication.getTitleZhTw());
				tboxAuthor.setValue(webPubLication.getAuthorZhTw());
				isbntbox.setValue(webPubLication.getIsbn());
				url.setValue(webPubLication.getStrurl());
				ebookURL.setValue(webPubLication.getEbookurl());
				pdfurl.setValue(webPubLication.getPdfurl());
				if(webPubLication.getIsDisplay()==1){
					isShowRdb.setSelectedIndex(0);
				}else if(webPubLication.getIsDisplay()==0){
					isShowRdb.setSelectedIndex(1);
				}
				publishertbox.setValue(webPubLication.getPublisherZhTw());
				byeartdbox.setValue(webPubLication.getPyear());
				//String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).pageing+"/";
				String portalDocementPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
				File file=new File(portalDocementPath+webPubLication.getImg());
				if(webPubLication.getImg()!=null&&!webPubLication.getImg().equals("")&&file.exists()){
					BufferedImage image=ImageIO.read(file);
					img.setContent(image);
					deleeImg.setVisible(true);
					upload.setVisible(false);
				}else{
					deleeImg.setVisible(false);
					upload.setVisible(true);
				}
				clickNumtbox.setValue(webPubLication.getClickNum());
				destbox.setValue(webPubLication.getDesZhTw());
			}
		} catch (Exception e) {
			log.error("update初始化異常"+e);
		}
	}
	
	@Listen("onClick=#deleeImg")
	public void deleteImg(){
		try {
			ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener<Event>() {
				
				public void onEvent(Event event) throws Exception {
					int clickButton=(Integer) event.getData();
					if(clickButton==Messagebox.OK){
						Media media=img.getContent();
						if(media!=null&&webPubLication!=null&&webPubLication.getImg()!=null&&!"".equals(webPubLication.getImg())){
							String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).portalDocementPath
									+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
							File file=new File(portalDocementPath+webPubLication.getImg());
							if(file.exists()){
								file.delete();
							}
							/*
							org.zkoss.image.Image image =(org.zkoss.image.Image)media;
							InputStream 	memberPhotoInputStream=image.getStreamData();
							Files.deleteAll(file);
							Files.close(memberPhotoInputStream);
							memberPhotoInputStream.close();*/
							//deleeImg.setStyle("display:none;");
							//upload.setStyle("display:block");
							webPubLication.setImg("");
							webPubLication.setIsDisplay(0);
							((WebPublicationService)SpringUtil.getBean("webPublicationService")).deleteWebPubLication(webPubLication);
						}
						img.setSrc("");
						deleeImg.setVisible(false);
						upload.setVisible(true);
					}
					
				}
			});
		} catch (Exception e) {
			log.error("刪除圖片異常"+e);
		}
	}
	
	@Listen("onClick=#saveBtn")
	public void insert(){
		try {
			webPubLication=new WebPublication();
			if(tboxtitle.getValue()!=null&&!"".equals(tboxtitle.getValue().trim())){
				String title=tboxtitle.getValue().trim();
				title=XSSStringEncoder.encodeXSSString(title);
				webPubLication.setTitleZhTw(title);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.titleIsNull"),Labels.getLabel("info")); 
				tboxtitle.focus();
				return;
			}
			if(tboxAuthor.getValue()!=null&&!"".equals(tboxAuthor.getValue().trim())){
				String authoer=tboxAuthor.getValue().trim();
				authoer=XSSStringEncoder.encodeXSSString(authoer);
				webPubLication.setAuthorZhTw(authoer);
			}
			if(isbntbox.getValue()!=null&&!"".equals(isbntbox.getValue().trim())){
				String isbn=isbntbox.getValue().trim();
				System.out.println(isbn.replace("-","").length());
				if(isbn.replace("-", "").length()==10||isbn.replace("-", "").length()==13){
					isbn=XSSStringEncoder.encodeXSSString(isbn);
					webPubLication.setIsbn(isbn);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.isbnFormat"),Labels.getLabel("info"));
					isbntbox.focus();
					return;
				}
			}
			if(url.getValue()!=null&&!url.getValue().trim().equals("")){
				webPubLication.setStrurl(XSSStringEncoder.encodeXSSString(url.getValue().trim()));
			}
			if(ebookURL!=null&&!ebookURL.getValue().trim().equals("")){
				webPubLication.setEbookurl(XSSStringEncoder.encodeXSSString(ebookURL.getValue().trim()));
			}
			if(pdfurl.getValue()!=null&&!pdfurl.getValue().trim().equals("")){
				webPubLication.setPdfurl(XSSStringEncoder.encodeXSSString(pdfurl.getValue().trim()));
			}
			if(issntbox.getValue()!=null&&!"".equals(issntbox.getValue().trim())){
				String issn=issntbox.getValue().trim();
				if(issn.replace("-","").length()==8){
					issn=XSSStringEncoder.encodeXSSString(issn);
					webPubLication.setIssn(issn);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.issnFormat"),Labels.getLabel("info"));
					issntbox.focus();
					return;
				} 
			}
			if(isShowRdb.getSelectedItem()!=null){
				webPubLication.setIsDisplay(Integer.parseInt(isShowRdb.getSelectedItem().getValue().toString()));
			}else{
				webPubLication.setIsDisplay(0);
			}
			if(publishertbox.getValue()!=null&&!"".equals(publishertbox.getValue().trim())){
				String publisher=publishertbox.getValue().trim();
				publisher=XSSStringEncoder.encodeXSSString(publisher);
				webPubLication.setPublisherZhTw(publisher);
			}
			if(byeartdbox.getValue()!=null&&!"".equals(byeartdbox.getValue())){
				Date pyear=byeartdbox.getValue();
				webPubLication.setPyear(pyear);
			}
			String isShow="";
			if(isShowRdb.getSelectedItem()!=null){
				isShow=isShowRdb.getSelectedItem().getValue();
			}
			if(isShow.equals("1")){
				if(img.getContent()!=null||webPubLication.getImg()!=null){
					Media media=img.getContent();
					if(media instanceof org.zkoss.image.Image){
						//String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).pageing+"/";
						String portalDocementPath = ((SystemProperties) SpringUtil
								.getBean("systemProperties")).portalDocementPath
								+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
						org.zkoss.image.Image image =(org.zkoss.image.Image)media;
						String fileName=Long.toString(System.currentTimeMillis())+"_"+image.getName();
						File file=new File(portalDocementPath+fileName);
						InputStream memberPhotoInputStream=image.getStreamData();
						if(media.getByteData().length/1024>300){
							OutputStream os = new FileOutputStream(portalDocementPath+fileName);
							ProcessImage.scaleImage(memberPhotoInputStream,os);
						}else{
							Files.copy(file,memberPhotoInputStream);
							Files.close(memberPhotoInputStream);
							memberPhotoInputStream.close();
						}
						webPubLication.setImg(fileName);
					}else{
						ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
						byeartdbox.focus();
						return;
					}
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
					byeartdbox.focus();
					return;
				}
			}else{
				if(img.getContent()!=null||webPubLication.getImg()!=null){
					Media media=img.getContent();
					if(media!=null){
						if(media instanceof org.zkoss.image.Image){
							//String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).pageing+"/";
							String portalDocementPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
							org.zkoss.image.Image image =(org.zkoss.image.Image)media;
							String fileName=Long.toString(System.currentTimeMillis())+"_"+image.getName();
							File file=new File(portalDocementPath+fileName);
							InputStream memberPhotoInputStream=image.getStreamData();
							if(media.getByteData().length/1024>300){
								OutputStream os = new FileOutputStream(portalDocementPath+fileName);
								ProcessImage.scaleImage(memberPhotoInputStream,os);
							}else{
								Files.copy(file,memberPhotoInputStream);
								Files.close(memberPhotoInputStream);
								memberPhotoInputStream.close();
							}
							webPubLication.setImg(fileName);
						}else{
							ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
							byeartdbox.focus();
							return;
						}
					}else{
						webPubLication.setImg("");
					}
				}else{
					webPubLication.setImg("");
				}
			}
			if(clickNumtbox.getValue()!=null&&!"".equals(clickNumtbox.getValue())){
				
				webPubLication.setClickNum(clickNumtbox.getValue());
			}
			if(destbox.getValue()!=null&&!"".equals(destbox.getValue().trim())){
				String des=destbox.getValue();
				des=XSSStringEncoder.encodeXSSString(des);
				webPubLication.setDesZhTw(des);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.desIsNull"),Labels.getLabel("info"));
				destbox.focus();
				return;
			}
			
			
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			
			webPubLication.setWebEmployee(loginwebEmployee);
			
			webPubLication.setIsDataEffid(1);
			webPubLication.setCreateDate(new Date());
			if(loginwebEmployee.getWeborg()!=null&&loginwebEmployee.getWeborg().getOrgId()!=null){
				webPubLication.setDataOwnerGroup(loginwebEmployee.getWeborg().getOrgId());
			}else{
				webPubLication.setDataOwnerGroup(loginwebEmployee.getParentWebOrg().getOrgId());
			}
			webPubLication.setUuid(UUIDGenerator.getUUID());
			((WebPublicationService)SpringUtil.getBean("webPublicationService")).saveWebPubLication(webPubLication);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(),"webPubLication_"+webPubLication.getUuid());
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("webPubLication/webPubLication.zul");
			addWenPubLicationWin.detach();
		} catch (WrongValueException e) {
			log.error("異常"+e);
		} catch (NumberFormatException e) {
			log.error("轉化異常"+e);
		} catch (IOException e) {
			log.error("io異常"+e);
		}
	}
	
	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) throws IOException{
		try {
			Media media=event.getMedia();
			/*if(media.getByteData().length>1049057){
				ZkUtils.showExclamation(Labels.getLabel("webpublic.uploadimginfo"),Labels.getLabel("info"));
				return;
			}*/
			if (media instanceof org.zkoss.image.Image) {
				img.setContent((org.zkoss.image.Image)media);
				if(deleeImg!=null){
					deleeImg.setVisible(true);
				}
				upload.setVisible(false);					
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webAccount.notImge"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("上傳圖片異常"+e);
		}
	}
	
	@Listen("onClick=#updateBtn")
	public void update(){
		try {
			if(tboxtitle.getValue()!=null&&!"".equals(tboxtitle.getValue().trim())){
				String title=tboxtitle.getValue().trim();
				title=XSSStringEncoder.encodeXSSString(title);
				webPubLication.setTitleZhTw(title);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.titleIsNull"),Labels.getLabel("info")); 
				tboxtitle.focus();
				return;
			}
			if(tboxAuthor.getValue()!=null&&!"".equals(tboxAuthor.getValue().trim())){
				String authoer=tboxAuthor.getValue().trim();
				authoer=XSSStringEncoder.encodeXSSString(authoer);
				webPubLication.setAuthorZhTw(authoer);
			}
			if(isbntbox.getValue()!=null&&!"".equals(isbntbox.getValue().trim())){
				String isbn=isbntbox.getValue().trim();
				if(isbn.replace("-", "").length()==10||isbn.replace("-", "").length()==13){
					isbn=XSSStringEncoder.encodeXSSString(isbn);
					webPubLication.setIsbn(isbn);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.isbnFormat"),Labels.getLabel("info"));
					isbntbox.focus();
					return;
				}
			}
			if(issntbox.getValue()!=null&&!"".equals(issntbox.getValue().trim())){
				String issn=issntbox.getValue().trim();
				if(issn.replace("-","").length()==8){
					issn=XSSStringEncoder.encodeXSSString(issn);
					webPubLication.setIssn(issn);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.issnFormat"),Labels.getLabel("info"));
					issntbox.focus();
					return;
				}
			}
			if(url.getValue()!=null&&!url.getValue().trim().equals("")){
				webPubLication.setStrurl(XSSStringEncoder.encodeXSSString(url.getValue().trim()));
			}
			if(ebookURL!=null&&!ebookURL.getValue().trim().equals("")){
				webPubLication.setEbookurl(XSSStringEncoder.encodeXSSString(ebookURL.getValue().trim()));
			}
			if(pdfurl.getValue()!=null&&!pdfurl.getValue().trim().equals("")){
				webPubLication.setPdfurl(XSSStringEncoder.encodeXSSString(pdfurl.getValue().trim()));
			}
			if(isShowRdb.getSelectedItem()!=null){
				webPubLication.setIsDisplay(Integer.parseInt(isShowRdb.getSelectedItem().getValue().toString()));
			}
			if(publishertbox.getValue()!=null&&!"".equals(publishertbox.getValue().trim())){
				String publisher=publishertbox.getValue().trim();
				publisher=XSSStringEncoder.encodeXSSString(publisher);
				webPubLication.setPublisherZhTw(publisher);
			}
			if(byeartdbox.getValue()!=null&&!"".equals(byeartdbox.getValue())){
				Date pyear=byeartdbox.getValue();
				webPubLication.setPyear(pyear);
			}
			String isShow="";
			if(isShowRdb.getSelectedItem()!=null){
				isShow=isShowRdb.getSelectedItem().getValue();
			}
			if(isShow.equals("1")){
				if(img.getContent()!=null||webPubLication.getImg()!=null){
					Media media=img.getContent();
					if(media instanceof org.zkoss.image.Image){
						//String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).pageing+"/";
						String portalDocementPath = ((SystemProperties) SpringUtil
								.getBean("systemProperties")).portalDocementPath
								+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
						org.zkoss.image.Image image =(org.zkoss.image.Image)media;
						String fileName=Long.toString(System.currentTimeMillis())+"_"+image.getName();
						File file=new File(portalDocementPath+fileName);
						InputStream memberPhotoInputStream=image.getStreamData();
						if(media.getByteData().length/1024>300){
							OutputStream os = new FileOutputStream(portalDocementPath+fileName);
							ProcessImage.scaleImage(memberPhotoInputStream,os);
						}else{
							Files.copy(file,memberPhotoInputStream);
							Files.close(memberPhotoInputStream);
							memberPhotoInputStream.close();
						}
						webPubLication.setImg(fileName);
					}else{
						ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
						byeartdbox.focus();
						return;
					}
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
					byeartdbox.focus();
					return;
				}
			}else{
				if(img.getContent()!=null||webPubLication.getImg()!=null){
					Media media=img.getContent();
					if(media!=null){
						if(media instanceof org.zkoss.image.Image){
							//String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).pageing+"/";
							String portalDocementPath = ((SystemProperties) SpringUtil
									.getBean("systemProperties")).portalDocementPath
									+ "/"+SystemVariable.WEB_PUBLICATION_PATH;
							org.zkoss.image.Image image =(org.zkoss.image.Image)media;
							String fileName=Long.toString(System.currentTimeMillis())+"_"+image.getName();
							File file=new File(portalDocementPath+fileName);
							InputStream memberPhotoInputStream=image.getStreamData();
							if(media.getByteData().length/1024>300){
								OutputStream os = new FileOutputStream(portalDocementPath+fileName);
								ProcessImage.scaleImage(memberPhotoInputStream,os);
							}else{
								Files.copy(file,memberPhotoInputStream);
								Files.close(memberPhotoInputStream);
								memberPhotoInputStream.close();
							}
							webPubLication.setImg(fileName);
						}else{
							ZkUtils.showExclamation(Labels.getLabel("webPubLication.selectImg"),Labels.getLabel("info"));
							byeartdbox.focus();
							return;
						}
					}else{
						webPubLication.setImg("");
					}
				}else{
					webPubLication.setImg("");
				}
			}
			
			if(clickNumtbox.getValue()!=null&&!"".equals(clickNumtbox.getValue())){
				
				webPubLication.setClickNum(clickNumtbox.getValue());
			}
			if(destbox.getValue()!=null&&!"".equals(destbox.getValue().trim())){
				String des=destbox.getValue();
				des=XSSStringEncoder.encodeXSSString(des);
				webPubLication.setDesZhTw(des); 
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webPubLication.desIsNull"),Labels.getLabel("info"));
				destbox.focus();
				return;
			}
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webPubLication.setLatelyChangedDate(new Date());
			webPubLication.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
			((WebPublicationService)SpringUtil.getBean("webPublicationService")).updateWebPubLication(webPubLication);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(),"webPubLication_"+webPubLication.getUuid());
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			//ZkUtils.refurbishMethod("webPubLication/webPubLication.zul");
			editPublicList();
			editWebPubLicationWin.detach();
		} catch (WrongValueException e) {
			log.error("異常"+e);
		} catch (NumberFormatException e) {
			log.error("轉化異常"+e);
		} catch (IOException e) {
			log.error("io異常"+e);
		}
	}
	public void editPublicList(){
		Listbox webpubLix =  (Listbox)editWebPubLicationWin.getParent().getFellowIfAny("WebPubLicationLix");
		Textbox titletbox=(Textbox)editWebPubLicationWin.getParent().getFellowIfAny("titletbox");	
		Radiogroup displayrdb=(Radiogroup)editWebPubLicationWin.getParent().getFellowIfAny("displayrdb");
		try {
			String title=titletbox.getValue().trim();
			title=XSSStringEncoder.encodeXSSString(title);
			WebPublication webPubLication=new WebPublication();
			webPubLication.setTitleZhTw(title);
			if(displayrdb.getSelectedItem()!=null){
				String isDisplay=displayrdb.getSelectedItem().getValue();
				webPubLication.setIsDisplay(Integer.parseInt(isDisplay));
			}else{
				webPubLication.setIsDisplay(-1);
			}
			List<WebPublication> pubLicationList=null;
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if(title!=null&&!title.equals("")||(displayrdb.getSelectedItem()!=null)){
				pubLicationList=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findByConditions(webPubLication,webEmployee);
			}else{
				pubLicationList=((WebPublicationService)SpringUtil.getBean("webPublicationService")).findAll(webEmployee);
			}
			ListModelList<WebPublication> model=new ListModelList<WebPublication>(pubLicationList);
			model.setMultiple(true);
			webpubLix.setModel(model);
			webpubLix.setActivePage(currentPage);
		} catch (Exception e) {
			log.error("查詢農業出版品集合出錯",e);
		}
	}
}
