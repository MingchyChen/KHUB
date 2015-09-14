package com.claridy.admin.composer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.io.Files;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
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
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import org.zkoss.util.media.Media;

import com.claridy.common.mechanism.facase.EmailService;
import com.claridy.common.util.DesEncryption;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;
import org.zkoss.zk.ui.Desktop;
/**
 * zfdong nj
 * 使用者賬號管理 新增 修改
 * 2014/8/6
 */
public class WebAccountComposer extends SelectorComposer<Component> {
	@Wire
	private Textbox tboxAccountId;
	@Wire
	private Textbox tboxNameZhTw;
	@Wire
	private Textbox tboxPwd;
	@Wire
	private Textbox tboxPwdtwo;
	@Wire
	private Textbox tboxEMail;
	@Wire
	private Textbox tboxTel;
	@Wire
	private Combobox tboxType;
	@Wire
	private Combobox orgNameBox;
	@Wire
	private Radiogroup rgroupstate;
	@Wire
	private Button upload;
	@Wire
	private Combobox sorgNameBox;
	
	@Wire
	private WebAccount webAccount;
	@Wire
	private Window addWebAccountWin;
	@Wire
	private Window editWebAccountWin;
	@Wire
	private Image imgAccountPic;
	@Wire
	private Button deleeImg;
	
	private int currentPage;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = -6389863475361756604L;
	
	private WebEmployee LoginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String uuid=map.get("uuid");
			if(map.get("currentPage")!=null&&!map.get("currentPage").equals("")){
				currentPage=Integer.parseInt(map.get("currentPage"));	
			}
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if(uuid!=null){
				webAccount=((WebAccountService)SpringUtil.getBean("webAccountService")).getAccount(uuid);
			}
			List<WebOrg> webPOrgList=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(null);
			Comboitem com = new Comboitem();
			com.setValue("0");
			com.setLabel(Labels.getLabel("select"));
			orgNameBox.appendChild(com);
			int parentIndex = 0;
			
			for(int i=0;i<webPOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=webPOrgList.get(i);
				com=new Comboitem();
				com.setLabel(webOrg.getOrgName());
				com.setValue(webOrg.getOrgId());
				orgNameBox.appendChild(com);
				if(webAccount!=null){
					if(webAccount.getParentorgid().equals(webOrg.getOrgId())){
						parentIndex=i;
					}
				}
			}
			
			if(webAccount!=null){
				WebOrg webOrgTmp=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccount.getParentorgid());
				if(webOrgTmp!=null){
					orgNameBox.setSelectedIndex(parentIndex+1);
				}else{
					orgNameBox.setSelectedIndex(0);
				}
			}else{
				orgNameBox.setSelectedIndex(0);
			}
			tboxType.setSelectedIndex(0);
			
			if(webAccount!=null){
				
				
				List<WebOrg> webOrgList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findOrg(orgNameBox.getSelectedItem().getValue().toString());
				int orgIndex=0;
				Comboitem scom=new Comboitem();
				scom.setValue("0");
				scom.setLabel(Labels.getLabel("select"));
				sorgNameBox.appendChild(scom);
				WebOrg weborg=null;
				for(int i=0;i<webOrgList.size();i++){
					WebOrg webOrg=new WebOrg();
					webOrg=webOrgList.get(i);
					scom=new Comboitem();
					scom.setLabel(webOrg.getOrgName());
					scom.setValue(webOrg.getOrgId());
					sorgNameBox.appendChild(scom);
					if(webAccount.getOrgid()!=null){
						if(webAccount.getOrgid().equals(webOrg.getOrgId())){
							orgIndex=i;
						}
					}
				}
				
				if(webOrgList.size()>0&&webAccount.getOrgid()!=null){
					WebOrg webOrgTmp=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccount.getOrgid());
					if(webOrgTmp!=null){
						sorgNameBox.setSelectedIndex(orgIndex+1);
					}else{
						sorgNameBox.setSelectedIndex(0);
					}
				}else{
					sorgNameBox.setSelectedIndex(0);
				}
				tboxType.setSelectedIndex(webAccount.getType()-2);
				if(webAccount.getStatus()==1){
					rgroupstate.setSelectedIndex(0);
				}else{
					rgroupstate.setSelectedIndex(1);
				}
				tboxAccountId.setValue(webAccount.getAccountId().toLowerCase());
				tboxNameZhTw.setValue(webAccount.getNameZhTw());
				tboxEMail.setValue(webAccount.getEmail());
				tboxTel.setValue(webAccount.getTel());	
				
				String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).portalDocementPath;
				File file=new File(portalDocementPath+webAccount.getAccountPic());
				if(webAccount.getAccountPic()!=null&&!webAccount.getAccountPic().equals("")){
					if(file.exists()){
						BufferedImage image=ImageIO.read(file);
						imgAccountPic.setContent(image);
					}else{
						deleeImg.setVisible(false);
						upload.setVisible(true);
					}
				}else{
					deleeImg.setVisible(false);
					upload.setVisible(true);
				}

				
				String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
				String jiemi = DesEncryption.desDecrypt(webAccount.getPwd(), deskey);
				tboxPwd.setValue(jiemi);
				tboxPwdtwo.setValue(jiemi);
				
			}
		} catch (Exception e) {
			log.error("初始化webAccount異常"+e);
		}
		
	}
	
	public void editSearchOrgList(){
		Listbox accountLix =  (Listbox)editWebAccountWin.getParent().getFellowIfAny("accountLix");
		Textbox accountIdBox=(Textbox)editWebAccountWin.getParent().getFellowIfAny("accountIdBox");	
		Textbox accountNameBox=(Textbox)editWebAccountWin.getParent().getFellowIfAny("accountNameBox");	
		Combobox orgNameBox=(Combobox)editWebAccountWin.getParent().getFellowIfAny("orgNameBox");
		Combobox idTypeBox=(Combobox)editWebAccountWin.getParent().getFellowIfAny("idTypeBox");
		Combobox isCheckCBbox=(Combobox)editWebAccountWin.getParent().getFellowIfAny("isCheckCBbox");
		Radiogroup rgroupstate=(Radiogroup) editWebAccountWin.getParent().getFellowIfAny("rgroupstate");
		Radiogroup registerRgp=(Radiogroup)editWebAccountWin.getParent().getFellowIfAny("registerRgp");
		try {
			String accountId=accountIdBox.getValue().trim();
			String accountName=accountNameBox.getValue().trim();
			String orgName=orgNameBox.getSelectedItem().getValue();
			String isCheck=isCheckCBbox.getSelectedItem().getValue();
			accountId=XSSStringEncoder.encodeXSSString(accountId);
			accountName=XSSStringEncoder.encodeXSSString(accountName);
			orgName=XSSStringEncoder.encodeXSSString(orgName);
			Integer isregister=-1;
			if((accountId!=null&&!accountId.equals(""))
					||(accountName!=null&&!"".equals(accountName))
					||(orgName!=null&&!"0".equals(orgName))
					||(isCheck!=null&&!"-1".equals(isCheck))
					||(!orgNameBox.getSelectedItem().getValue().equals("0"))
					||(rgroupstate.getSelectedItem()!=null)
					||(!idTypeBox.getSelectedItem().getValue().equals("0"))
					||(registerRgp.getSelectedItem()!=null)){
				
			}
			if(registerRgp.getSelectedItem()!=null){
				isregister=Integer.parseInt(registerRgp.getSelectedItem().getValue().toString());
			}
			WebAccount webAccount=new WebAccount();
			webAccount.setAccountId(accountId);
			webAccount.setNameZhTw(accountName);
			if(rgroupstate.getSelectedItem()!=null){
				webAccount.setStatus(Integer.parseInt(rgroupstate.getSelectedItem().getValue().toString()));
			}else{
				webAccount.setStatus(-1);
			}
			if(!orgNameBox.getSelectedItem().getValue().equals("0")){
				
				
				webAccount.setParentorgid(orgNameBox.getSelectedItem().getValue().toString());
			}
			if(idTypeBox.getSelectedItem()!=null){
				webAccount.setType(Integer.parseInt((String) idTypeBox.getSelectedItem().getValue()));
			}
			webAccount.setIsCheck(Integer.parseInt(isCheck));
			List<WebAccount> webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(webAccount,isregister,1,LoginwebEmployee);
			//webAccount.getWebOrg().getOrgName()+"-"+webAccount.getWebParentorg().getOrgName();
			/*for(int i=0;i<webAccountList.size();i++){
				if(webAccountList.get(i).getWebOrg()!=null){
					
					webAccountList.get(i).setOrgId();
					
				}
			}*/
			WebOrg webPraentOrg=null;
			WebOrg webOrg=null;
			WebEmployee webemployeeTmp=new WebEmployee();
			for(int i=0;i<webAccountList.size();i++){
				webPraentOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getParentorgid());
				if(webPraentOrg!=null){
					webAccountList.get(i).setParentOrgName(webPraentOrg.getOrgName());
				}else{
					webAccountList.get(i).setParentOrgName("");
				}
				
				webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webAccountList.get(i).getOrgid());
				if(webOrg!=null){
					webAccountList.get(i).setOrgName(webOrg.getOrgName());
				}else{
					webAccountList.get(i).setOrgName("");
				}
				if(webAccountList.get(i).getIsRegister()!=null&&webAccountList.get(i).getIsRegister()==1){
					webemployeeTmp.setEmployeeName(webAccountList.get(i).getNameZhTw());
					webAccountList.get(i).setWebEmployee(webemployeeTmp);
				}
				
			}
			
			ListModelList<WebAccount> WebAccountModel=new ListModelList<WebAccount>(webAccountList);
			WebAccountModel.setMultiple(true);
			accountLix.setModel(WebAccountModel);
			accountLix.setActivePage(currentPage);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}

	
	@Listen("onClick=#deleeImg")
	public void deleteImg(){
		try {
			ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener<Event>() {
				
				public void onEvent(Event event) throws Exception {
					int clickButton=(Integer) event.getData();
					if(clickButton==Messagebox.OK){
						Desktop dtp=Executions.getCurrent().getDesktop();
						Media media=imgAccountPic.getContent();
						if(media!=null){
							String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).portalDocementPath;
							org.zkoss.image.Image image =(org.zkoss.image.Image)media;
							File file=new File(portalDocementPath+webAccount.getAccountPic());
							InputStream memberPhotoInputStream=image.getStreamData();
							Files.deleteAll(file);
							Files.close(memberPhotoInputStream);
							memberPhotoInputStream.close();
							deleeImg.setVisible(false);
							upload.setVisible(true);
							//deleeImg.setStyle("display:none;");
							//upload.setStyle("display:block");
							imgAccountPic.setSrc("");
							webAccount.setAccountPic("");
							((WebAccountService)SpringUtil.getBean("webAccountService")).deleteWebAccount(webAccount);
						}
					}
					
				}

				
			});
		} catch (Exception e) {
			log.error("刪除圖片異常"+e);
		}
		
		
	}
	
	@Listen("onClick=#updateBtn")
	public void updateEmployee(){
		String NameZhTw;
		String orgId;
		String email;
		String tel;
		String idType;
		String state;
		String pwd;
		String parentOrgId;
		String titleZhTw;
		if(tboxNameZhTw.getValue()!=null||!tboxNameZhTw.getValue().trim().equals("")){
			NameZhTw=tboxNameZhTw.getValue().trim();
			NameZhTw=XSSStringEncoder.encodeXSSString(NameZhTw);
			webAccount.setNameZhTw(NameZhTw);
		}else{
			ZkUtils.showExclamation(Labels.getLabel("nameIsNull"),Labels.getLabel("info"));
			tboxNameZhTw.focus();
			return;
		}
		if(tboxPwd.getValue()!=null||!tboxPwd.getValue().trim().equals("")){
			try {
				if(tboxPwd.getValue().toString().trim().equals(tboxPwdtwo.getValue().trim())){
					pwd=tboxPwd.getValue().toString().trim();
					String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
					String jiami = DesEncryption.desEncrypt(pwd, deskey);
					pwd=XSSStringEncoder.encodeXSSString(pwd);
					webAccount.setPwd(jiami);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("passNotEqualPassTwo"),Labels.getLabel("info"));
					tboxPwdtwo.focus();
					return;
				}
			} catch (Exception e) {
				log.error(""+e);
			}
		}else{
			ZkUtils.showExclamation(Labels.getLabel("passJudge"),Labels.getLabel("info"));
			tboxPwd.focus();
			return;
		}
		if(orgNameBox.getSelectedItem()!=null){
			parentOrgId=orgNameBox.getSelectedItem().getValue().toString().trim();
			parentOrgId=XSSStringEncoder.encodeXSSString(parentOrgId);
			webAccount.setParentorgid(parentOrgId);
			
		}else{
			ZkUtils.showExclamation(Labels.getLabel("selectParentOrgName"),Labels.getLabel("info"));
			orgNameBox.focus();
			return;
		}
		if(tboxType.getSelectedItem()!=null&&!tboxType.getSelectedItem().getValue().equals("0")){
			idType=tboxType.getSelectedItem().getValue().toString().trim();
			idType=XSSStringEncoder.encodeXSSString(idType);
			webAccount.setType(Integer.parseInt(idType));
		}else{
			ZkUtils.showExclamation(Labels.getLabel("selectType"),Labels.getLabel("info"));
			tboxType.focus();
			return;
		}
		if(sorgNameBox.getSelectedItem()!=null){
			if(!sorgNameBox.getSelectedItem().getValue().equals("")&&!sorgNameBox.getSelectedItem().getValue().equals("0")){
				orgId=sorgNameBox.getSelectedItem().getValue().toString().trim();
				orgId=XSSStringEncoder.encodeXSSString(orgId);
				webAccount.setOrgid(orgId);
				
			}else{
				webAccount.setOrgid(null);
			}
		}
		if(tboxEMail.getValue()!=null||!tboxEMail.getValue().trim().equals("")){
			List<WebAccount> webAccountListAll=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(null,-1,1,LoginwebEmployee);
			for(WebAccount webAccounttmp:webAccountListAll){
				if(webAccounttmp.getEmail().equals(tboxEMail.getValue().trim())){
					if(webAccounttmp.getAccountId().equals(tboxAccountId.getValue())){
						break;
					}
					ZkUtils.showExclamation(Labels.getLabel("webAccount.emailHasBeingUse"),Labels.getLabel("info"));
					tboxEMail.focus();
					return;
					
				}
			}
			email=tboxEMail.getValue().trim();
			email=XSSStringEncoder.encodeXSSString(email);
			webAccount.setEmail(email);
		}else{
			ZkUtils.showExclamation(Labels.getLabel("emailIsNull"),Labels.getLabel("info"));
			tboxEMail.focus();
			return;
		}
		if(tboxTel.getValue()!=null||!tboxTel.getValue().trim().equals("")){
			tel=tboxTel.getValue().trim();
			tel=XSSStringEncoder.encodeXSSString(tel);
			webAccount.setTel(tel);
		}
		
		if(rgroupstate.getSelectedItem()!=null){
			state=rgroupstate.getSelectedItem().getValue().toString().trim();
			state=XSSStringEncoder.encodeXSSString(state);
			webAccount.setStatus(Integer.parseInt(state));
		}
		try {
			
			
			Media media=imgAccountPic.getContent();
			if(media!=null){
				String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).portalDocementPath+"document/uploadfile/webmemberpic/";
				org.zkoss.image.Image image =(org.zkoss.image.Image)media;
				String fileName=Long.toString(System.currentTimeMillis())+"-"+image.getName();
				File file=new File(portalDocementPath+fileName);
				InputStream memberPhotoInputStream=image.getStreamData();
				Files.copy(file,memberPhotoInputStream);
				Files.close(memberPhotoInputStream);
				memberPhotoInputStream.close();
				webAccount.setAccountPic("document/uploadfile/webmemberpic/"+fileName);
			}
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if(webAccount.getStatus()==1){
				webAccount.setIsCheck(1);
				webAccount.setApprovePeople(loginwebEmployee.getEmployeesn());
				webAccount.setApproveDate(new Date());
			}
			
			webAccount.setLatelyChangedDate(new Date());
			webAccount.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
			((WebAccountService)SpringUtil.getBean("webAccountService")).update(webAccount);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "account_"+webAccount.getUuid());
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			/*ZkUtils.refurbishMethod("webAccount/webAccount.zul");*/
			editSearchOrgList();
			editWebAccountWin.detach();
		} catch (Exception e) {
			log.error("編輯webAccount異常"+e);
		}
	}
	@Listen("onChange=#orgNameBox")
	public void showOrg(){
		sorgNameBox.getChildren().clear();
		if(!orgNameBox.getSelectedItem().getValue().equals("0")){
			String parentorgid=orgNameBox.getSelectedItem().getValue().toString();
			List<WebOrg> websOrgList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findOrg(parentorgid);
			Comboitem scom = new Comboitem();
			scom.setValue("0");
			scom.setLabel(Labels.getLabel("select"));
			sorgNameBox.appendChild(scom);
			for(int i=0;i<websOrgList.size();i++){
				WebOrg webOrg=new WebOrg();
				webOrg=websOrgList.get(i);
				scom=new Comboitem();
				scom.setLabel(webOrg.getOrgName());
				scom.setValue(webOrg.getOrgId());
				sorgNameBox.appendChild(scom);
			}
			sorgNameBox.setSelectedIndex(0);
		}
	}
	@Listen("onClick=#btnExamine")
	public void examine(){
		try {
			if(tboxAccountId.getValue()==null||tboxAccountId.getValue().trim().equals("")){
				ZkUtils.showExclamation(Labels.getLabel("useJudge"), Labels.getLabel("info"));
			}else{
				webAccount=new WebAccount();
				webAccount.setAccountId(tboxAccountId.getValue().trim());
				List<WebAccount> webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findAccount(webAccount,LoginwebEmployee);
				if(webAccountList.size()==0){	
					ZkUtils.showExclamation(Labels.getLabel("accountOk"),Labels.getLabel("info"));
					tboxAccountId.focus();
					return;
				}else{
					
					if(webAccountList.get(0).getIsCheck()==2){
						ZkUtils.showExclamation(Labels.getLabel("accountOk"),Labels.getLabel("info"));
						tboxAccountId.focus();
						return;
					}else if(webAccountList.get(0).getStatus()==0){
						ZkUtils.showExclamation(Labels.getLabel("accountOk"),Labels.getLabel("info"));
						tboxAccountId.focus();
						return;
					}else if(webAccountList.get(0).getIsDataEffid()==0){
						ZkUtils.showExclamation(Labels.getLabel("accountOk"),Labels.getLabel("info"));
						tboxAccountId.focus();
						return;
					}else{
						ZkUtils.showExclamation(Labels.getLabel("accountNo"),Labels.getLabel("info"));
						tboxAccountId.focus();
						return;
					}
				}
			}
		} catch (WrongValueException e) {
			log.error("查詢帳號是否存在錯誤"+e);
		}
	}
	
	@Listen("onUpload=#upload")
	public void upload(UploadEvent event) throws IOException{
		try {
			Media media=event.getMedia();
			if (media instanceof org.zkoss.image.Image) {
				imgAccountPic.setContent((org.zkoss.image.Image)media);
				deleeImg.setVisible(true);
				upload.setVisible(false);		
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webAccount.notImge"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("上傳圖片異常"+e);
		}
	}
	@Listen("onClick=#saveBtn")
	public void saveWebAccount(){
		String tel=null;
		String state="1";
		String orgId=null;
		webAccount=new WebAccount();
		if(tboxAccountId.getValue()==null||tboxAccountId.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("useJudge"),Labels.getLabel("info"));
			tboxAccountId.focus();
			return;	
		}else if(tboxNameZhTw.getValue()==null||tboxNameZhTw.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("nameIsNull"),Labels.getLabel("info"));
			tboxNameZhTw.focus();
			return;
		}else if(tboxPwd.getValue()==null||tboxPwd.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("passJudge"),Labels.getLabel("info"));
			tboxPwd.focus();
			return;
		}else if(tboxPwdtwo.getValue()==null||tboxPwdtwo.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("agarinPass"),Labels.getLabel("info"));
			tboxPwdtwo.focus();
			return;
		}else if(!tboxPwd.getValue().toString().trim().equals(tboxPwdtwo.getValue().trim())){
			ZkUtils.showExclamation(Labels.getLabel("passNotEqualPassTwo"),Labels.getLabel("info"));
			tboxPwdtwo.focus();
			return;
		}else if(orgNameBox.getSelectedItem()==null||orgNameBox.getSelectedItem().getValue().equals("0")){
			ZkUtils.showExclamation(Labels.getLabel("selectParentOrgName"),Labels.getLabel("info"));
			orgNameBox.focus();
			return;
		}else if(tboxType.getSelectedItem()==null||tboxType.getSelectedItem().getValue().equals("0")){
			ZkUtils.showExclamation(Labels.getLabel("selectType"),Labels.getLabel("info"));
			tboxType.focus();
			return;
		}else if(tboxEMail==null||tboxEMail.getValue()==null||tboxEMail.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("emailIsNull"),Labels.getLabel("info"));
			tboxEMail.focus();
			return;
		}else if(tboxTel.getValue()!=null&&!tboxTel.getValue().trim().equals("")){
			tel=tboxTel.getValue().trim();
			tel=XSSStringEncoder.encodeXSSString(tel);
		}else if(rgroupstate.getSelectedItem()!=null){
			state=rgroupstate.getSelectedItem().getValue().toString().trim();
		}else{
			state="1";
		}
		if(sorgNameBox.getSelectedItem()!=null){
			if(!sorgNameBox.getSelectedItem().getValue().equals("0")){
				orgId=sorgNameBox.getSelectedItem().getValue();
			}
			
		}
		
		try {
			
			
			if(imgAccountPic.getContent()!=null){
				Media media=imgAccountPic.getContent();
				String portalDocementPath=((SystemProperties)SpringUtil.getBean("systemProperties")).portalDocementPath+"document/uploadfile/webmemberpic/";
				org.zkoss.image.Image image =(org.zkoss.image.Image)media;
				String fileName=Long.toString(System.currentTimeMillis())+"-"+image.getName();
				File file=new File(portalDocementPath+fileName);
				InputStream memberPhotoInputStream=image.getStreamData();
				Files.copy(file,memberPhotoInputStream);
				Files.close(memberPhotoInputStream);
				memberPhotoInputStream.close();
				webAccount.setAccountPic("document/uploadfile/webmemberpic/"+fileName);
				
			}
				
			webAccount.setAccountId(tboxAccountId.getValue().trim());
			webAccount.setIsCheck(2);
			webAccount.setStatus(0);
			List<WebAccount> webAccountList=((WebAccountService)SpringUtil.getBean("webAccountService")).findAccount(webAccount,LoginwebEmployee);
			List<WebAccount> webAccountListAll=((WebAccountService)SpringUtil.getBean("webAccountService")).findWebAccount(null,-1,1,LoginwebEmployee);
			boolean isOk=false;
			if(webAccountList.size()==0){
				isOk=true;
				for(WebAccount webAccount:webAccountListAll){
					if(webAccount.getEmail().equals(tboxEMail.getValue().trim())){
						ZkUtils.showExclamation(Labels.getLabel("webAccount.emailHasBeingUse"),Labels.getLabel("info"));
						tboxEMail.focus();
						return;
						
					}
				}
			}else{
				for(WebAccount webAccount:webAccountListAll){
					if(webAccountList.get(0).getEmail().equals(tboxEMail.getValue().trim())){
						isOk=false;
						break;
					}
					if(webAccount.getEmail().equals(tboxEMail.getValue().trim())){
						if(webAccount.getIsDataEffid()==0){
							break;
						}else{
							ZkUtils.showExclamation(Labels.getLabel("webAccount.emailHasBeingUse"),Labels.getLabel("info"));
							tboxEMail.focus();
							return;
						}
					}else{
						isOk=false;
					}
				}
				
				if(webAccountList.get(0).getIsCheck()==2){
					isOk=false;
				}else if(webAccountList.get(0).getStatus()==0){
					isOk=false;
				}else if(webAccountList.get(0).getIsDataEffid()==0){
					isOk=false;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("accountNo"),Labels.getLabel("info"));
					tboxAccountId.focus();
					return;
				}
			}
			
			if(isOk){
				String AccountId=tboxAccountId.getValue().trim();
				String NameZhTw=tboxNameZhTw.getValue().trim();
				
				String email=tboxEMail.getValue().trim();
				
				String idType=tboxType.getSelectedItem().getValue().toString().trim();
				
				String pwd=tboxPwd.getValue().toString().trim();
				String parentOrgId=orgNameBox.getSelectedItem().getValue();
				
				AccountId=XSSStringEncoder.encodeXSSString(AccountId);
				NameZhTw=XSSStringEncoder.encodeXSSString(NameZhTw);
				email=XSSStringEncoder.encodeXSSString(email);
				
				pwd=XSSStringEncoder.encodeXSSString(pwd);
				
				String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
				String jiami = DesEncryption.desEncrypt(pwd, deskey);
				webAccount.setAccountId(AccountId);
				webAccount.setNameZhTw(NameZhTw);
				if(orgId!=null&&!orgId.equals("")){
					webAccount.setOrgid(orgId);
				}

				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				webAccount.setStatus(Integer.parseInt(state));
				webAccount.setIsCheck(0);
				if(webAccount.getStatus()==1){
					webAccount.setIsCheck(1);
					webAccount.setApprovePeople(loginwebEmployee.getEmployeesn());
					webAccount.setApproveDate(new Date());
				}
				webAccount.setEmail(email.trim());
				webAccount.setTel(tel);
				webAccount.setType(Integer.parseInt(idType));
				Date date=new Date();
				WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
				webAccount.setWebEmployee(loginWebEmployee);
				webAccount.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
				webAccount.setLatelyChangedDate(date);
				webAccount.setLatelyChangedUser(loginWebEmployee.getEmployeesn());
				webAccount.setCreateDate(date);
				webAccount.setPwd(jiami);
				webAccount.setUuid(UUIDGenerator.getUUID());
				webAccount.setIsDataEffid(1);
				webAccount.setParentorgid(parentOrgId);
				
				
				webAccount.setAccountId(webAccount.getAccountId().toLowerCase());
				
				((WebAccountService)SpringUtil.getBean("webAccountService")).addWebAccount(webAccount);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "account_"+webAccount.getUuid());
			}else{
				String employeeName;
				String email="";
				String idType;
				String pwd;
				String parentOrgId;
				
				webAccount.setUuid(webAccountList.get(0).getUuid());
				if(tboxNameZhTw.getValue()!=null||!tboxNameZhTw.getValue().equals("")){
					employeeName=tboxNameZhTw.getValue().trim();
					employeeName=XSSStringEncoder.encodeXSSString(employeeName);
					webAccount.setNameZhTw(employeeName);
				}else{
					webAccount.setNameZhTw("");
				}
				if(sorgNameBox.getSelectedItem()!=null&&!sorgNameBox.getSelectedItem().getValue().equals("0")){
					orgId=sorgNameBox.getSelectedItem().getValue().toString().trim();
					orgId=XSSStringEncoder.encodeXSSString(orgId);
					webAccount.setOrgid(orgId);
				}else{
					webAccount.setOrgid("");
				}
				if(tboxEMail.getValue()!=null||!tboxEMail.getValue().trim().equals("")){
					email=tboxEMail.getValue().trim();
					email=XSSStringEncoder.encodeXSSString(email);
					webAccount.setEmail(email);
				}
				if(tboxTel.getValue()!=null||!tboxTel.getValue().trim().equals("")){
					tel=tboxTel.getValue().trim();
					tel=XSSStringEncoder.encodeXSSString(tel);
					webAccount.setTel(tel);
				}else{
					webAccount.setTel("");
				}
				if(tboxType.getSelectedItem()!=null){
					idType=tboxType.getSelectedItem().getValue().toString().trim();
					idType=XSSStringEncoder.encodeXSSString(idType);
					webAccount.setType(Integer.parseInt(idType));
				}
				if(rgroupstate.getSelectedItem()!=null){
					state=rgroupstate.getSelectedItem().getValue().toString().trim();
					state=XSSStringEncoder.encodeXSSString(state);
					webAccount.setStatus(Integer.parseInt(state));
				}else{
					webAccount.setStatus(1);
				}
				if(tboxPwd.getValue()!=null||!tboxPwd.getValue().trim().equals("")){
					pwd=tboxPwd.getValue().toString().trim();
					String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
					String jiami = DesEncryption.desEncrypt(pwd, deskey);
					pwd=XSSStringEncoder.encodeXSSString(pwd);
					webAccount.setPwd(jiami);
				}
				if(orgNameBox.getSelectedItem()!=null){
					parentOrgId=orgNameBox.getSelectedItem().getValue();
					parentOrgId=XSSStringEncoder.encodeXSSString(parentOrgId);
					webAccount.setParentorgid(parentOrgId);
				}
				webAccount.setIsCheck(0);
				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				if(webAccount.getStatus()==1){
					webAccount.setIsCheck(1);
					webAccount.setApprovePeople(loginwebEmployee.getEmployeesn());
					webAccount.setApproveDate(new Date());
				}
				webAccount.setIsDataEffid(1);
				webAccount.setWebEmployee(loginwebEmployee);
				webAccount.setDataOwnerGroup(loginwebEmployee.getParentWebOrg().getOrgId());
				webAccount.setCreateDate(new Date());
				webAccount.setAccountId(webAccount.getAccountId().toLowerCase());
				((WebAccountService)SpringUtil.getBean("webAccountService")).update(webAccount);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "account_"+webAccount.getUuid());
				
			}
			
			if(!StringUtils.isBlank(tboxEMail.getValue())) {
				if(!ZkUtils.isEmail(tboxEMail.getValue())){
					ZkUtils.showExclamation(
							Labels.getLabel("emailError"),
							Labels.getLabel("warn"));
					tboxEMail.focus();
					return;
				}
			}
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("webAccount/webAccount.zul");
			addWebAccountWin.detach();
			
		} catch (Exception e) {
			log.error("新增webAccount異常"+e);
		}
	}
}
