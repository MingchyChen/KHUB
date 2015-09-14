package com.claridy.admin.composer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.mechanism.facase.EmailService;
import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.SystemVariable;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebErwSource;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebPhrase;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebCooperationService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebPhraseService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 館際合作申請管理 審核
 * 2014/8/18
 */
public class WebCooperationComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2283198248542073889L;
	@Wire
	private Label applyDateLbl;
	@Wire
	private Label applyName;
	@Wire
	private Label applyOrgNameLbl;
	@Wire
	private Label titleZhTwLbl;
	@Wire
	private Label atitleLbl;
	@Wire
	private Label pidLbl;
	@Wire
	private Label issnLbl;
	@Wire
	private Label volumeLbl;
	@Wire
	private Label issueLbl;
	@Wire
	private Label spageLbl;
	@Wire
	private Label epageLbl;
	@Wire
	private Label doiLbl;
	@Wire
	private Label publishDateLbl;
	@Wire
	private Label acceptEmployeeLbl;
	@Wire
	private Label replyDateLbl;
	@Wire
	private Label uploadFileLbl;
	@Wire
	private Label downoadDbidLbl;
	@Wire
	private Label statusLbl;
	@Wire
	private Textbox uploadFiletbox;
	@Wire
	private Button deleeNewsFile;
	@Wire
	private Button upload;
	@Wire
	private Textbox rejectTbox;
	@Wire
	private Window webCooperationPassWin;
	@Wire
	private Combobox downoadDbidCbox;
	@Wire
	private Window webCooperationRejectWin;
	@Wire
	private Button webPhraseBtn;
	@Wire
	private Label rejectLbl;
	@Wire
	private Label uploadDispalyFileLbl;
	@Wire
	private Label agridlURLLbl;
	@Wire
	private Row rejectRow;
	@Wire
	private Row uploadRow;
	@Wire
	private Textbox rejectUuidTbox;
	@Wire
	private Textbox uuidTBox;
	
	private int currpage;
	
	WebCooperation webCooperation;
	
	private Window webCooperationWin;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	WebEmployee webEmployeeLogin=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap = ZkUtils.getExecutionArgs();
			String uuid=(String) tmpMap.get("uuid");
			String webctype=(String) tmpMap.get("webctype");
			String reject=(String) tmpMap.get("reject");
			String currpages=(String) tmpMap.get("currpage");
			if(uuidTBox!=null){
				uuidTBox.setValue(uuid);
			}
			
			if(currpages!=null&&!currpages.equals("")){
				currpage=Integer.parseInt(currpages);
			}
			if(webCooperationRejectWin!=null){
				webCooperationWin=(Window) webCooperationRejectWin.getParent();
			}
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webCooperation=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findById(uuid,loginwebEmployee);
			SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(webCooperation.getCreateDate()!=null){
				applyDateLbl.setValue(formate.format(webCooperation.getCreateDate()));
			}
			applyName.setValue(webCooperation.getApplyAccount().getNameZhTw());
			WebOrg webParent=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperation.getApplyAccount().getParentorgid());
			applyOrgNameLbl.setValue(webParent.getOrgName());
			titleZhTwLbl.setValue(webCooperation.getTitleZhTw());
			atitleLbl.setValue(webCooperation.getAtitle());
			pidLbl.setValue(webCooperation.getPid());
			issnLbl.setValue(webCooperation.getIssn());
			volumeLbl.setValue(webCooperation.getVolume());
			issueLbl.setValue(webCooperation.getIssue());
			spageLbl.setValue(webCooperation.getSpage());
			epageLbl.setValue(webCooperation.getEpage());
			doiLbl.setValue(webCooperation.getDoi());
			if(webCooperation.getPublishDate()!=null){
				publishDateLbl.setValue(formate.format(webCooperation.getPublishDate()));
			}
			acceptEmployeeLbl.setValue(webCooperation.getAcceptEmployee().getEmployeeName());
			if(webCooperation.getLatelyChangedDate()!=null){
				replyDateLbl.setValue(formate.format(webCooperation.getLatelyChangedDate()));
			}
			if(uploadFileLbl!=null){

				uploadFileLbl.setValue(webCooperation.getDisplayName());
			}
			if(uploadDispalyFileLbl!=null){
				uploadDispalyFileLbl.setValue(webCooperation.getUploadFile());
			}
			if(agridlURLLbl!=null){
				
				agridlURLLbl.setValue(((SystemProperties)SpringUtil.getBean("systemProperties")).agridlURL);
			}
			if(webCooperation.getUploadFile()!=null&&!webCooperation.getUploadFile().equals("")){
				if(uploadFiletbox!=null){
					uploadFiletbox.setValue(webCooperation.getDisplayName());
					deleeNewsFile.setVisible(true);
					upload.setVisible(false);
				}
			}
			if(webPhraseBtn!=null){
				webPhraseBtn.setTarget(webCooperation.getUuid());
			}
			if(rejectUuidTbox!=null){
				webCooperation.setRejectReason(rejectUuidTbox.getValue());
			}
			if(downoadDbidLbl!=null){
				downoadDbidLbl.setValue(webCooperation.getDbid());
			}
			if(downoadDbidCbox!=null){
				Comboitem com=new Comboitem();
				com.setLabel(webCooperation.getDbid());
				com.setValue(webCooperation.getUuid());
				downoadDbidCbox.appendChild(com);
				downoadDbidCbox.setSelectedIndex(0);
			}
			
			if(webCooperation.getStatus()==0&&statusLbl!=null){
				statusLbl.setValue(Labels.getLabel("webCooperation.statusNoDispose"));
			}
			if(webCooperation.getStatus()==1&&statusLbl!=null){
				uploadRow.setVisible(true);
				statusLbl.setValue(Labels.getLabel("webCooperation.statusOK"));
			}
			if(webCooperation.getStatus()==2&&statusLbl!=null){
				rejectRow.setVisible(true);
				rejectLbl.setVisible(true);
				WebPhrase webPhraseTmp=((WebPhraseService)SpringUtil.getBean("webPhraseService")).findById(webCooperation.getRejectReason());
				rejectLbl.setValue(webPhraseTmp.getPhraseZhTw());
				statusLbl.setValue(Labels.getLabel("webCooperation.statusNO"));
			}
			if(webctype!=null&&Integer.parseInt(webctype)==1){
				uploadRow.setVisible(true);
				rejectRow.setVisible(false);
				rejectLbl.setVisible(false);
				statusLbl.setValue(Labels.getLabel("webCooperation.statusOK"));
				boolean isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsOk("6",webEmployeeLogin,null, webCooperation,null,webCooperation.getUploadFile(),2);
			}
			if(webctype!=null&&Integer.parseInt(webctype)==2){
				rejectRow.setVisible(true);
				statusLbl.setValue(Labels.getLabel("webCooperation.statusNO"));
				rejectLbl.setVisible(true);
				if(reject!=null){
					rejectLbl.setValue(reject);
					uploadRow.setVisible(false);
					webCooperation.setRejectReason(reject);
				}
				WebPhrase webPhrase=new WebPhrase();
				webPhrase.setPhraseZhTw(reject);
				boolean isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsNO("8",webPhrase,webEmployeeLogin,null, webCooperation,2);
			}
			
		} catch (Exception e) {
			log.error("webCooperation初始化異常"+e);
		}
  		
	}
		
	public void editSearchOrgList(){
		Combobox applyOrgCBbox=null;
		Combobox acceptOrgCBbox=null;
		Textbox applyOddTbox=null;
		Textbox applyPeopleTbox=null;
		Combobox retStatusCBbox=null;
		Datebox startDate=null;
		Datebox endDate=null;
		Listbox webCooperationLix=null;
		if(webCooperationRejectWin!=null){
			applyOrgCBbox=(Combobox) webCooperationWin.getFellowIfAny("applyOrgCBbox");
			acceptOrgCBbox=(Combobox) webCooperationWin.getFellowIfAny("acceptOrgCBbox");
			applyOddTbox=(Textbox) webCooperationWin.getFellowIfAny("applyOddTbox");
			applyPeopleTbox=(Textbox) webCooperationWin.getFellowIfAny("applyPeopleTbox");
			retStatusCBbox=(Combobox) webCooperationWin.getFellowIfAny("retStatusCBbox");
			startDate=(Datebox) webCooperationWin.getFellowIfAny("startDate");
			endDate=(Datebox) webCooperationWin.getFellowIfAny("endDate");
			webCooperationLix=(Listbox) webCooperationWin.getFellowIfAny("webCooperationLix");
		}
		if(webCooperationPassWin!=null){
			applyOrgCBbox=(Combobox) webCooperationPassWin.getParent().getFellowIfAny("applyOrgCBbox");
			acceptOrgCBbox=(Combobox) webCooperationPassWin.getParent().getFellowIfAny("acceptOrgCBbox");
			applyOddTbox=(Textbox) webCooperationPassWin.getParent().getFellowIfAny("applyOddTbox");
			applyPeopleTbox=(Textbox) webCooperationPassWin.getParent().getFellowIfAny("applyPeopleTbox");
			retStatusCBbox=(Combobox) webCooperationPassWin.getParent().getFellowIfAny("retStatusCBbox");
			startDate=(Datebox) webCooperationPassWin.getParent().getFellowIfAny("startDate");
			endDate=(Datebox) webCooperationPassWin.getParent().getFellowIfAny("endDate");
			webCooperationLix=(Listbox) webCooperationPassWin.getParent().getFellowIfAny("webCooperationLix");
		}
		
		try {
			if(applyOrgCBbox!=null&&applyOrgCBbox.getSelectedItem()!=null&&!applyOrgCBbox.getSelectedItem().getValue().equals("-1")
					||(acceptOrgCBbox!=null&&acceptOrgCBbox.getSelectedItem()!=null&&!acceptOrgCBbox.getSelectedItem().getValue().equals("-1"))
					||(applyOddTbox!=null&&applyOddTbox.getValue()!=null&&!applyOddTbox.getValue().equals(""))
					||(applyPeopleTbox!=null&&applyPeopleTbox.getValue()!=null&&!applyPeopleTbox.getValue().equals(""))
					||(retStatusCBbox!=null&&retStatusCBbox.getSelectedItem()!=null&&!retStatusCBbox.getSelectedItem().getValue().equals("-1"))
					||(startDate!=null&&startDate.getValue()!=null&&!startDate.getValue().equals(""))
					||(endDate!=null&&endDate.getValue()!=null&&!endDate.getValue().equals(""))){
				
				
			}
			WebOrg applyAccount=null;
			WebOrg acceptEmployee=null;
			String uuid;	
			int status=-1;
			if(applyOrgCBbox.getSelectedItem()!=null&&!applyOrgCBbox.getSelectedItem().getValue().equals("-1")){
				WebOrg webOrg=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(applyOrgCBbox.getSelectedItem().getValue().toString()).get(0);
				applyAccount=webOrg;
			}
			if(acceptOrgCBbox.getSelectedItem()!=null&&!acceptOrgCBbox.getSelectedItem().getValue().equals("-1")){
				WebOrg webOrgAccpet=((WebAccountService)SpringUtil.getBean("webAccountService")).findParentOrg(acceptOrgCBbox.getSelectedItem().getValue().toString()).get(0);
				acceptEmployee=webOrgAccpet;
			}
			String applyOdd=applyOddTbox.getValue().trim();
			applyOdd=XSSStringEncoder.encodeXSSString(applyOdd);
			uuid=applyOdd;
			
			String applyPeople=applyPeopleTbox.getValue().trim();
			applyPeople=XSSStringEncoder.encodeXSSString(applyPeople);
			/*if(applyPeople!=null&&!"".equals(applyPeople)){
				applyPeople=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findById(applyPeople).getUuid();
			}*/
			
			if(retStatusCBbox.getSelectedItem()!=null&&!retStatusCBbox.getSelectedItem().getValue().equals("-1")){
				String statusS=retStatusCBbox.getSelectedItem().getValue();
				status=Integer.parseInt(statusS);
			}
			
			 
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebCooperation> webCooperationList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findByWebCooperation(applyAccount,loginwebEmployee, acceptEmployee, uuid,applyPeople, status, startDate.getValue(), endDate.getValue());
			if(webCooperationList.size()>0&&applyPeople!=null&&!"".equals(applyPeople)){
				for(int i=0;i<webCooperationList.size();i++){
					if(webCooperationList.get(i).getApplyAccount().getNameZhTw().indexOf(applyPeople)==-1){
						webCooperationList.remove(i);
						i=i-1;
					}
				}
			}
			ListModelList<WebCooperation> model=new ListModelList<WebCooperation>(webCooperationList);
			model.setMultiple(true);
			webCooperationLix.setModel(model);
			webCooperationLix.setActivePage(currpage);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	
	@Listen("onUpload=#upload")
	public void upload(UploadEvent event){
		try {
			Media media = event.getMedia();
			
			if(media.isBinary()){
				//String realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webCooperationFileURL+"/";
				String realPath = ((SystemProperties) SpringUtil
						.getBean("systemProperties")).portalDocementPath
						+ "/"+SystemVariable.WEB_COOPERIATION_PATH;
				InputStream stream = media.getStreamData();
				String name=media.getName();
				if(stream.available()<=10485760){
					String type=name.substring(name.lastIndexOf(".")+1);
					if(type.equals("txt")||type.equals("pdf")||type.equals("doc")||type.equals("docx")||type.equals("xlsx")||type.equals("xls")||type.equals("ppt")||type.equals("pptx")){
						 
						 String FileName=RandomIDGenerator.getRandomId()+"."+type;
						 File file=new File(realPath+FileName);
						 uploadFiletbox.setValue(media.getName());
						 webCooperation.setUploadFile(FileName);
						 Files.copy(file, stream);
						 Files.close(stream);
						 stream.close();
						 
					}else{
						ZkUtils.showExclamation(Labels.getLabel("webCooperation.askSelectFile"),Labels.getLabel("info"));
						return;
					}
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.askSelectFile"),Labels.getLabel("info"));
					return;
				}
				
			}else{
				String size=media.getStringData();
				if(size.length()<=10485760){
					//String realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webCooperationFileURL+"/";
					String realPath = ((SystemProperties) SpringUtil
							.getBean("systemProperties")).portalDocementPath
							+ "/"+SystemVariable.WEB_COOPERIATION_PATH; 
					File file=new File(realPath);
					String sr=media.getStringData();
					String fileName=RandomIDGenerator.getRandomId();
					String type=media.getName().substring(media.getName().lastIndexOf("."));
					File files=File.createTempFile(fileName, type,file);
					webCooperation.setUploadFile(files.getName());
					uploadFiletbox.setValue(media.getName());
					FileWriter fw=new FileWriter(files);
					fw.write(sr);
					fw.close();
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.askSelectFile"),Labels.getLabel("info"));
					return;
				}
			}
			
			deleeNewsFile.setVisible(true);
			upload.setVisible(false);
			
		} catch (IOException e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#deleeNewsFile")
	public void lists() throws IOException{
		try {
			//String realPath=((SystemProperties)SpringUtil.getBean("systemProperties")).webCooperationFileURL+"/";
			String realPath = ((SystemProperties) SpringUtil
					.getBean("systemProperties")).portalDocementPath
					+ "/"+SystemVariable.WEB_COOPERIATION_PATH;
			File file=new File(realPath+uploadFiletbox.getValue());
			if(uploadFiletbox.getValue()!=null&&!uploadFiletbox.getValue().equals("")&&file.exists()){
				file.delete();
			}
			uploadFiletbox.setValue("");
			deleeNewsFile.setVisible(false);
			upload.setVisible(true);
		} catch (Exception e) {
			log.error("刪除檔異常"+e);
		}
	}
	
	@Listen("onClick=#outBtn")
	public void save(){
		try {
			if(uploadFiletbox.getValue()!=null&&!uploadFiletbox.getValue().equals("")){

				webCooperation.setDisplayName(uploadFiletbox.getValue());
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.selectFile"), Labels.getLabel("info"));
				uploadFiletbox.focus();
				return;
			}
			if(webCooperation.getAcceptEmployee()==null){
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.isNotManage"),Labels.getLabel("info"));
				return;
			}
			WebOrg webParent=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperation.getApplyAccount().getParentorgid());
			List<WebEmployee> MangerWebEmployeeList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findWebEmployeeListByParentOrgId(webParent.getOrgId());
			if(MangerWebEmployeeList.size()==0){
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.isNotManage"),Labels.getLabel("info"));
				return;
			}
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webCooperation.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
			webCooperation.setLatelyChangedDate(new Date());
			webCooperation.setStatus(1);
			webCooperation.setDownoadDbid(downoadDbidCbox.getSelectedItem().getValue().toString());
			
			boolean isOk=false;
			for(int i=0;i<MangerWebEmployeeList.size();i++){
				isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsOk("6",webEmployeeLogin,MangerWebEmployeeList.get(i), webCooperation,null,webCooperation.getUploadFile(),1);
			}
			
			boolean isOkApply=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsOk("7",webEmployeeLogin,null, webCooperation,null,webCooperation.getUploadFile(),0);
			((WebCooperationService)SpringUtil.getBean("webCooperationService")).update(webCooperation);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "webilc_"+webCooperation.getUuid(), "核可館際合作申請案件: "+webCooperation.getUuid());
			if(isOk&&isOkApply){
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.outOK"),Labels.getLabel("info"));
				//ZkUtils.refurbishMethod("webCooperation/webCooperation.zul");
				editSearchOrgList();
				webCooperationPassWin.detach();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.outNO"),Labels.getLabel("info"));
			}
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#rejectBtn")
	public void reject(){
		try {
			if(rejectTbox.getValue()!=null&&!rejectTbox.getValue().equals("")){
				if(webCooperation.getAcceptEmployee()==null){
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.isNotManage"),Labels.getLabel("info"));
					return;
				}
				WebOrg webParent=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgById(webCooperation.getApplyAccount().getParentorgid());
				List<WebEmployee> MangerWebEmployeeList=((WebCooperationService)SpringUtil.getBean("webCooperationService")).findWebEmployeeListByParentOrgId(webParent.getOrgId());
				if(MangerWebEmployeeList.size()==0){
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.isNotManage"),Labels.getLabel("info"));
					return;
				}
				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				webCooperation.setLatelyChangedUser(loginwebEmployee.getEmployeesn());
				webCooperation.setLatelyChangedDate(new Date());
				webCooperation.setStatus(2);
				((WebCooperationService)SpringUtil.getBean("webCooperationService")).update(webCooperation);
				
				List<WebPhrase> webPhraseList=((WebPhraseService)SpringUtil.getBean("webPhraseService")).findAll();
				WebPhrase webPhrase=null;
				boolean isnotWebPhrase=true;
				for(int i=0;i<webPhraseList.size();i++){
					if(webPhraseList.get(i).getPhraseZhTw().equals(rejectTbox.getValue())){
						isnotWebPhrase=false;
						webCooperation.setRejectReason(webPhraseList.get(i).getUuid());
						webPhrase=webPhraseList.get(i);
						break;
					}
				}
				if(isnotWebPhrase){
					webPhrase=new WebPhrase();
					webPhrase.setUuid(UUIDGenerator.getUUID());
					webPhrase.setCreateDate(new Date());
					webPhrase.setIsDataEffid(1);
					webPhrase.setDataOwnerWeb(loginwebEmployee);
					webPhrase.setDataOwnerGroup(loginwebEmployee.getParentWebOrg().getOrgId());
					webPhrase.setPhraseZhTw(rejectTbox.getValue());
					((WebPhraseService)SpringUtil.getBean("webPhraseService")).insert(webPhrase);
					webCooperation.setRejectReason(webPhrase.getUuid());
				}
				
				
				boolean isOk=false;
				for(int i=0;i<MangerWebEmployeeList.size();i++){
					 isOk=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsNO("8",webPhrase,webEmployeeLogin,MangerWebEmployeeList.get(i), webCooperation,1);
				}
				
				boolean isOkApply=((EmailService)SpringUtil.getBean("emailService")).sendNoticeToWebCooperationIsNO("8",webPhrase,webEmployeeLogin,null, webCooperation,0);
				
				
				if(isOk&&isOkApply){
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), loginwebEmployee.getEmployeesn(), "webilc_"+webCooperation.getUuid(), "駁回館際合作申請案件:"+webCooperation.getUuid());
					((WebCooperationService)SpringUtil.getBean("webCooperationService")).update(webCooperation);
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.outOK"),Labels.getLabel("info"));
					//ZkUtils.refurbishMethod("webCooperation/webCooperation.zul");
					editSearchOrgList();
					webCooperationRejectWin.detach();
				}else{
					ZkUtils.showExclamation(Labels.getLabel("webCooperation.outOK"),Labels.getLabel("info"));
				}
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webCooperation.rejectIsNull"),Labels.getLabel("info"));
				return;
			}
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	

}
