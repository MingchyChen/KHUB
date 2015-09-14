package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import com.claridy.common.mechanism.facase.SysMenuService;
import com.claridy.common.util.DesEncryption;
import com.claridy.common.util.PowerTreeModel;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunction;
import com.claridy.domain.WebFunctionEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebEmployeeListService;
import com.claridy.facade.WebFunctionEmployeeService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 管理管理者權限管理作業 新增 修改
 * 2014/8/6
 */
public class WebEmployeeComposer extends SelectorComposer<Component> {
	@Wire
	private Textbox tboxEmployeeId;
	@Wire
	private Textbox tboxEmployeeName;
	@Wire
	private Textbox tboxPwd;
	@Wire
	private Textbox tboxPwdtwo;
	@Wire
	private Textbox tboxEMail;
	@Wire
	private Textbox tboxTel;
	@Wire
	private Combobox tboxIdType;
	@Wire
	private Combobox orgNameBox;
	@Wire
	private Radiogroup rgroupstate;
	@Wire
	private Radiogroup rgroupIsManager;
	@Wire
	private Radiogroup rdoIsNewsTop;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private Combobox sorgNameBox;
	@Wire
	private Window editWebEmployeeWin;
	@Wire
	private Window addWebEmployeeWin;
	@Wire
	private Tree tree;
	
	@Wire
	private Checkbox checkAll;
	@Wire
	private PowerTreeModel btm;
	@Wire
	private SysMenuService menuService;
	
	private WebEmployee loginwebEmployee;
	
	private List<WebFunction> webFunctionList;
	
	private String employeesn;
	
	private int currentPage;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	
	public WebEmployeeComposer(){
		String languageType = null;
		Session session = Sessions.getCurrent();
		if (session.getAttribute("now_Locale") != null) {
			languageType = session.getAttribute("now_Locale").toString();
		}
		menuService = (SysMenuService) SpringUtil.getBean("sysMenuService");
		webFunctionList = menuService.findMenuList(languageType);
		btm = new PowerTreeModel(webFunctionList, menuService);
	}
	
	public void editSearchOrgList(){
		Listbox employeeLix =  (Listbox)editWebEmployeeWin.getParent().getFellowIfAny("employeeLix");
		Textbox employeeNameBox=(Textbox)editWebEmployeeWin.getParent().getFellowIfAny("employeeNameBox");	
		Textbox employeeIdBox=(Textbox)editWebEmployeeWin.getParent().getFellowIfAny("employeeIdBox");	
		Combobox orgNameBox=(Combobox)editWebEmployeeWin.getParent().getFellowIfAny("orgNameBox");
		Combobox idTypeBox=(Combobox)editWebEmployeeWin.getParent().getFellowIfAny("idTypeBox");
		Radiogroup orOrAnd=(Radiogroup) editWebEmployeeWin.getParent().getFellowIfAny("orOrAnd");
		Radiogroup isMangergop=(Radiogroup) editWebEmployeeWin.getParent().getFellowIfAny("rgroupIsManager");
		try{
			String employeeId=employeeIdBox.getValue().trim();
			String employeeName=employeeNameBox.getValue().trim();
			employeeId=XSSStringEncoder.encodeXSSString(employeeId);
			employeeName=XSSStringEncoder.encodeXSSString(employeeName);
			int isManger=-1;
			WebEmployee webEmployee=new WebEmployee();
			webEmployee.setEmployeeId(employeeId);
			webEmployee.setEmployeeName(employeeName);
			int orOrAn=0;
			if(orgNameBox.getSelectedItem()!=null&&!orgNameBox.getSelectedItem().getValue().equals("0")){
				List<WebOrg> parentOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(orgNameBox.getSelectedItem().getValue().toString());
				webEmployee.setParentWebOrg(parentOrg.get(0));
			}
			if(idTypeBox.getSelectedItem()!=null&&!idTypeBox.getSelectedItem().getValue().equals("0")){
				webEmployee.setIdType(Integer.parseInt((String) idTypeBox.getSelectedItem().getValue()));
			}
			if(orOrAnd.getSelectedItem()!=null){
				String sorOrAn=orOrAnd.getSelectedItem().getValue();
				orOrAn=Integer.parseInt(sorOrAn);
			}
			if(isMangergop.getSelectedItem()!=null){
				isManger=Integer.parseInt(isMangergop.getSelectedItem().getValue().toString());
			}
			webEmployee.setIsManager(isManger);
			WebEmployee webEmp=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployee,webEmp,orOrAn,1);
			WebEmployee webEmployeeTmp=new WebEmployee();
			for(int i=0;i<webEmployeeList.size();i++){
				if(webEmployeeList.get(i).getWeborg()!=null&&webEmployeeList.get(i).getWeborg().getOrgId()==null){
					webEmployeeList.get(i).getWeborg().setOrgId("");
				}
				
				webEmployeeTmp.setEmployeesn(webEmployeeList.get(i).getDataOwner());
				List<WebEmployee> webEmployeeListTemp=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployeeTmp, webEmp, 0, 1);
				if(webEmployeeListTemp.size()>0){
					webEmployeeList.get(i).setDataOwner(webEmployeeListTemp.get(0).getEmployeeName());
				}else{
					webEmployeeList.get(i).setDataOwner("");
				}
				
				
			}
			
			ListModelList<WebEmployee> WebEmployeeModel=new ListModelList<WebEmployee>(webEmployeeList);
			WebEmployeeModel.setMultiple(true);
			employeeLix.setModel(WebEmployeeModel);
			employeeLix.setActivePage(currentPage);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			employeesn=map.get("employeesn");
			if(map.get("currentPage")!=null&&!map.get("currentPage").equals("")){
				currentPage=Integer.parseInt(map.get("currentPage"));
			}
			WebEmployee employe=new WebEmployee();
			employe.setEmployeesn(employeesn);
			tree.setModel(btm);
			tree.setTreeitemRenderer(new TreeitemRenderer<WebFunction>() {
				public void render(Treeitem item, WebFunction data, int index)
						throws Exception {
					item.setOpen(true);
					Treerow treerow = new Treerow();
					item.appendChild(treerow);
					final Treecell treecell = new Treecell();
					treerow.appendChild(treecell);
					final Checkbox checkbox = new Checkbox();
					if(employeesn!=null&&!"".equals(employeesn)){
						List<WebFunctionEmployee> webFuncEmp=((WebFunctionEmployeeService)SpringUtil.getBean("webFunctionEmployeeService")).getWebFunctionEmployee(employeesn);
						String languageType = null;
						Session session = Sessions.getCurrent();
						if (session.getAttribute("now_Locale") != null) {
							languageType = session.getAttribute("now_Locale").toString();
						}
						menuService = (SysMenuService) SpringUtil.getBean("sysMenuService");
						List<WebFunction> funcList = menuService.findAllList(languageType);
						if(webFuncEmp.size()==funcList.size()){
							checkAll.setChecked(true);
						}else{
							checkAll.setChecked(false);
						}
						for(WebFunctionEmployee funcOrg:webFuncEmp){
							if(funcOrg.getFuncUuid().equals(data.getUuid())){
								checkbox.setChecked(true);
							}
						}
					}
					checkbox.setValue(data.getUuid());
					checkbox.setLabel(Labels.getLabel(data.getMutiFuncId()));
					checkbox.addEventListener("onCheck", new EventListener<CheckEvent>() {

						public void onEvent(CheckEvent event) throws Exception {
							Checkbox checkbox = (Checkbox)event.getTarget();
							Treeitem ref = (Treeitem)checkbox.getParent().getParent().getParent();
					    	checkChild(ref,checkbox.isChecked());
					    	
					    	Boolean bflag = checkbox.isChecked();
					    	for(Component treeComponent : ref.getParent().getChildren()){
					    		Component treecellComponent = treeComponent.getFirstChild().getFirstChild();
					    		if(treecellComponent instanceof Treecell){
					    			Treecell treecell = (Treecell)treeComponent.getFirstChild().getFirstChild();
									Component checkboxComponent = treecell.getFirstChild();
									if(checkboxComponent instanceof Checkbox){
										Boolean bnear = ((Checkbox)checkboxComponent).isChecked();
							    		if(bflag != bnear){
							    			bflag = bnear;
							    			break;
							    		}	
									}
					    		}
					    	}
							if (bflag == checkbox.isChecked()) {
								if (ref.getParentItem() != null) {
									Treecell treecell = (Treecell) ref.getParentItem().getLastChild().getFirstChild();
									if (treecell != null) {
										Component checkboxComponent = treecell.getFirstChild();
										if (checkboxComponent instanceof Checkbox) {
											Checkbox parentCheckbox = (Checkbox) checkboxComponent;
											parentCheckbox.setChecked(bflag);
										}
									}
								}
							}
					    	if(bflag != checkbox.isChecked() && checkbox.isChecked() == false){
					    		if(ref.getParentItem() != null){
					    			Treecell treecell = (Treecell) ref.getParentItem().getLastChild().getFirstChild();
					    			if(treecell != null){
					    				Component checkboxComponent = treecell.getFirstChild();
					    				if(checkboxComponent instanceof Checkbox){
					    					((Checkbox)checkboxComponent).setChecked(false);
					    				}
					    			}
					    		}
					    	}
					    	
					    	checkAllSelected();
						}
					  });
					treecell.appendChild(checkbox);
				}
			});
			loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			if(employeesn!=null){
				webEmployee=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).getWebEmployee(employeesn);
			}
			List<WebOrg> webPOrgList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(null);
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
				if(loginwebEmployee.getParentWebOrg().getIsAuth()==1){
					orgNameBox.appendChild(com);
				}else if(loginwebEmployee.getParentWebOrg().getOrgId().endsWith(webOrg.getOrgId())){
					orgNameBox.appendChild(com);
					break;
				}
				if(webEmployee!=null){
					if(webEmployee.getParentWebOrg()!=null&&webEmployee.getParentWebOrg().getOrgId().equals(webOrg.getOrgId())){
						parentIndex=i;
					}
				}
			}
			if(webEmployee!=null&&webEmployee.getParentWebOrg()!=null){
				orgNameBox.setSelectedIndex(parentIndex+1);
			}else{
				orgNameBox.setSelectedIndex(0);
			}
			tboxIdType.setSelectedIndex(0);
			 
			if(webEmployee!=null){
				

				Comboitem scom=new Comboitem();
				scom.setValue("0");
				scom.setLabel(Labels.getLabel("select"));
				sorgNameBox.appendChild(scom);
				if(webEmployee.getParentWebOrg()!=null){
					List<WebOrg> webOrgList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findOrg(webEmployee.getParentWebOrg().getOrgId());
					int orgIndex=0;
					for(int i=0;i<webOrgList.size();i++){
						WebOrg webOrg=new WebOrg();
						webOrg=webOrgList.get(i);
						scom=new Comboitem();
						scom.setLabel(webOrg.getOrgName());
						scom.setValue(webOrg.getOrgId());
						sorgNameBox.appendChild(scom);
						if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&webEmployee.getWeborg().getOrgId().equals(webOrg.getOrgId())){
							orgIndex=i;
						}
					}
					
					if(webOrgList.size()>0){
						sorgNameBox.setSelectedIndex(orgIndex+1);
					}else{
						sorgNameBox.setSelectedIndex(0);
					}
				}
				if(webEmployee.getWeborg()==null||webEmployee.getWeborg().getOrgId()==null||webEmployee.getWeborg().getOrgId().equals("")){
					sorgNameBox.setSelectedIndex(0);
				}
				
				tboxIdType.setSelectedIndex(webEmployee.getIdType());
				rgroupstate.setSelectedIndex(webEmployee.getIsLock());
				if(webEmployee.getIsManager()==1){
					rgroupIsManager.setSelectedIndex(0);
				}else{
					rgroupIsManager.setSelectedIndex(1);
				}
				if(webEmployee.getIsnewstop()!=null){
					if(webEmployee.getIsnewstop()==1){
						rdoIsNewsTop.setSelectedIndex(0);
					}else{
						rdoIsNewsTop.setSelectedIndex(1);
					}
				}
				
				tboxEmployeeId.setValue(webEmployee.getEmployeeId().toLowerCase());
				tboxEmployeeName.setValue(webEmployee.getEmployeeName());
				tboxEMail.setValue(webEmployee.getEmail());
				tboxTel.setValue(webEmployee.getTel());
				String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
				String jiemi = DesEncryption.desDecrypt(webEmployee.getPwd(), deskey);
				tboxPwd.setValue(jiemi);
				tboxPwdtwo.setValue(jiemi);
				
			}
		} catch (Exception e) {
			log.error("初始化webEmployee異常",e);
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
	@Listen("onClick=#updateBtn")
	public void updateEmployee(){
		String employeeName;
		String parentorgId;
		String orgId;
		String email;
		String tel;
		String idType;
		String state;
		String isManager;
		String pwd;
		if(tboxEmployeeName.getValue()!=null||!tboxEmployeeName.getValue().trim().equals("")){
			employeeName=tboxEmployeeName.getValue().trim();
			employeeName=XSSStringEncoder.encodeXSSString(employeeName);
			webEmployee.setEmployeeName(employeeName);
		}else{
			ZkUtils.showExclamation(Labels.getLabel("nameIsNull"),Labels.getLabel("info"));
			tboxEmployeeName.focus();
			return;
		}
		if(tboxPwd.getValue()!=null||!tboxPwd.getValue().trim().equals("")){
			try {
				if(tboxPwd.getValue().equals(tboxPwdtwo.getValue())){
					pwd=tboxPwd.getValue().toString().trim();
					String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
					String jiami = DesEncryption.desEncrypt(pwd, deskey);
					pwd=XSSStringEncoder.encodeXSSString(pwd);
					webEmployee.setPwd(jiami);
				}else{
					ZkUtils.showExclamation(Labels.getLabel("passNotEqualPassTwo"),Labels.getLabel("info"));
					tboxPwdtwo.focus();
					return;
				}
			}catch (Exception e) {
				log.error("",e);
			}
		}else{
			ZkUtils.showExclamation(Labels.getLabel("agarinPass"),Labels.getLabel("info"));
			tboxPwdtwo.focus();
			return;
		}
		if(!orgNameBox.getSelectedItem().equals("0")){
			parentorgId=orgNameBox.getSelectedItem().getValue().toString().trim();
			parentorgId=XSSStringEncoder.encodeXSSString(parentorgId);
			List<WebOrg> parentOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(parentorgId);
			webEmployee.setParentWebOrg(parentOrg.get(0));
		}else{
			ZkUtils.showExclamation(Labels.getLabel("selectParentOrgName"),Labels.getLabel("info"));
			orgNameBox.focus();
			return;
		}
		if(tboxEMail.getValue()!=null||!tboxEMail.getValue().trim().equals("")){
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findById();
			for(WebEmployee webE:webEmployeeList){
				if(webE.getEmail().equals(tboxEMail.getValue().trim())){
					if(webE.getIsDataEffid()==0){
						break;
					}else{
						if(webE.getEmployeeId().equals(tboxEmployeeId.getValue())){
							break;
						}
						ZkUtils.showExclamation(Labels.getLabel("webAccount.emailHasBeingUse"),Labels.getLabel("info"));
						tboxEMail.focus();
						return;
					}
				}
			}
			email=tboxEMail.getValue().trim();
			email=XSSStringEncoder.encodeXSSString(email);
			webEmployee.setEmail(email);
		}else{
			ZkUtils.showExclamation(Labels.getLabel("emailIsNull"),Labels.getLabel("info"));
			tboxEMail.focus();
			return;
		}
		if(tboxTel.getValue()!=null||!tboxTel.getValue().trim().equals("")){
			tel=tboxTel.getValue().trim();
			tel=XSSStringEncoder.encodeXSSString(tel);
			webEmployee.setTel(tel);
		}else{
			ZkUtils.showExclamation(Labels.getLabel("telIsNull"),Labels.getLabel("info"));
			tboxTel.focus();
			return;
		}
		if(rgroupstate.getSelectedItem()!=null){
			state=rgroupstate.getSelectedItem().getValue().toString().trim();
			state=XSSStringEncoder.encodeXSSString(state);
			webEmployee.setIsLock(Integer.parseInt(state));
		}else{
			ZkUtils.showExclamation(Labels.getLabel("sateIsNull"),Labels.getLabel("info"));
			rgroupstate.focus();	
			return;
		}
		if(!tboxIdType.getSelectedItem().getValue().equals("0")){
			idType=tboxIdType.getSelectedItem().getValue().toString().trim();
			idType=XSSStringEncoder.encodeXSSString(idType);
			webEmployee.setIdType(Integer.parseInt(idType));
		}else{
			ZkUtils.showExclamation(Labels.getLabel("selectIdType"),Labels.getLabel("info"));
			tboxIdType.focus();
			return;
		}
		
		if(rgroupIsManager.getSelectedItem()!=null){
			isManager=rgroupIsManager.getSelectedItem().getValue().toString().trim();
			isManager=XSSStringEncoder.encodeXSSString(isManager);
			webEmployee.setIsManager(Integer.parseInt(isManager));
		}else{
			ZkUtils.showExclamation(Labels.getLabel("selectManager"), Labels.getLabel("info"));
			rgroupIsManager.focus();
			return;
		}
		webEmployee.setIsnewstop(Integer.parseInt(rdoIsNewsTop.getSelectedItem().getValue().toString()));
		
		if(sorgNameBox.getSelectedItem()!=null){
			if(!sorgNameBox.getSelectedItem().getValue().equals("0")){
				orgId=sorgNameBox.getSelectedItem().getValue();
				orgId=XSSStringEncoder.encodeXSSString(orgId);
				List<WebOrg> webOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(orgId);
				webEmployee.setWeborg(webOrg.get(0));
			}else{
				webEmployee.setWeborg(null);
			}
		}else{
			webEmployee.setWeborg(null);
		}
		try {
			
			
			WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webEmployee.setLatelyChangeDuser(loginwebEmployee.getEmployeesn());
			webEmployee.setLatelyChangedDate(new Date());
			((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).update(webEmployee);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "employee_"+webEmployee.getEmployeesn());
			((WebFunctionEmployeeService)SpringUtil.getBean("webFunctionEmployeeService")).deleteWebFunctionEmp(webEmployee.getEmployeesn());
			Treechildren treechildren = (Treechildren) tree.getTreechildren();
			List<Component> list = treechildren.getChildren();
			for (Component treeitem : list) {
				if(treeitem instanceof Treeitem){
					checkWebEmployeeChild((Treeitem)treeitem);
				}
			}
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			/*ZkUtils.refurbishMethod("webemployee/webEmployee.zul");*/
			editSearchOrgList();
			editWebEmployeeWin.detach();
		} catch (Exception e) {
			log.error("編輯employee異常",e);
		}
	}
	
	@Listen("onClick=#btnExamine")
	public void examine(){
		if(tboxEmployeeId.getValue()==null||tboxEmployeeId.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("useJudge"),Labels.getLabel("info"));
		}else{
			webEmployee=new WebEmployee();
			webEmployee.setEmployeeId(tboxEmployeeId.getValue().trim());
			WebEmployee webEmp=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployee,webEmp, 0,1);
			if(webEmployeeList.size()==0){
				ZkUtils.showExclamation(Labels.getLabel("accountOk"),Labels.getLabel("info"));
			}else{
				ZkUtils.showExclamation(Labels.getLabel("accountNo"),Labels.getLabel("info"));
			}
		}
	}
	
	@Listen("onClick=#saveBtn")
	public void saveEmployee(){
		webEmployee=new WebEmployee();
		if(tboxEmployeeName.getValue()==null||tboxEmployeeName.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("nameIsNull"),Labels.getLabel("info"));
			tboxEmployeeName.focus();
			return;
		} if(tboxEmployeeId.getValue()==null||tboxEmployeeId.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("useJudge"),Labels.getLabel("info"));
			tboxEmployeeId.focus();
			return;
		}
		if(tboxPwd.getValue()==null||tboxPwd.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("passJudge"),Labels.getLabel("info"));
			tboxPwd.focus();
			return;
		}
		if(tboxPwdtwo.getValue()==null||tboxPwdtwo.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("agarinPass"),Labels.getLabel("info"));
			tboxPwdtwo.focus();
			return;
		}else if(!tboxPwd.getValue().toString().trim().equals(tboxPwdtwo.getValue().trim())){
			ZkUtils.showExclamation(Labels.getLabel("passNotEqualPassTwo"),Labels.getLabel("info"));
			tboxPwdtwo.focus();
			return;
		}
		if(orgNameBox.getSelectedItem().getValue().equals("0")){
			ZkUtils.showExclamation(Labels.getLabel("selectParentOrgName"),Labels.getLabel("info"));
			orgNameBox.focus();
			return;
		} 
		if(sorgNameBox.getSelectedItem()!=null){
			if(!sorgNameBox.getSelectedItem().getValue().equals("0")){
				String orgId=sorgNameBox.getSelectedItem().getValue();
				orgId=XSSStringEncoder.encodeXSSString(orgId);
				List<WebOrg> webOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(orgId);
				webEmployee.setWeborg(webOrg.get(0));
			}
		}
		if(tboxEMail.getValue()==null||tboxEMail.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("emailIsNull"),Labels.getLabel("info"));
			tboxEMail.focus();
			return;
		}
		if(tboxTel.getValue()==null||tboxTel.getValue().trim().equals("")){
			ZkUtils.showExclamation(Labels.getLabel("telIsNull"),Labels.getLabel("info"));
			tboxTel.focus();
			return;
		}
		if(tboxIdType.getSelectedItem().getValue().equals("0")){
			ZkUtils.showExclamation(Labels.getLabel("selectIdType"),Labels.getLabel("info"));
			tboxIdType.focus();
			return;
		}
		if(rgroupstate.getSelectedItem()==null){
			ZkUtils.showExclamation(Labels.getLabel("sateIsNull"),Labels.getLabel("info"));
			rgroupstate.focus();	
			return;
		}
		
		
		if(rgroupIsManager.getSelectedItem()==null){
			ZkUtils.showExclamation(Labels.getLabel("selectManager"), Labels.getLabel("info"));
			rgroupIsManager.focus();
			return;
		}
		
		try {
			

			
			webEmployee.setEmployeeId(tboxEmployeeId.getValue().trim().toLowerCase());
			WebEmployee webEmp=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebEmployee> list=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployee,webEmp, 0,1);
			if(list.size()>0){
				ZkUtils.showExclamation(Labels.getLabel("accountNo"),Labels.getLabel("info"));
				return;
			}
			List<WebEmployee> webEmployeeList=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findWebEmployee(webEmployee,webEmp, 0,0);
			List<WebEmployee> webEmployeeListAll=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findAll();
			for(WebEmployee webE:webEmployeeListAll){
				if(webE.getEmail().equals(tboxEMail.getValue().trim())){
					if(webE.getIsDataEffid()==0){
						continue;
					}else{
						ZkUtils.showExclamation(Labels.getLabel("webAccount.emailHasBeingUse"),Labels.getLabel("info"));
						tboxEMail.focus();
						return;
					}
				}
			}
			if(webEmployeeList.size()==0){
				String employeeId=tboxEmployeeId.getValue().trim();
				String employeeName=tboxEmployeeName.getValue().trim();
				
				String email=tboxEMail.getValue().trim();
				String tel=tboxTel.getValue().trim();
				String idType=tboxIdType.getSelectedItem().getValue().toString().trim();
				String state=rgroupstate.getSelectedItem().getValue().toString().trim();
				String isManager=rgroupIsManager.getSelectedItem().getValue().toString().trim();
				String pwd=tboxPwd.getValue().toString().trim();
				String parentOrgId=orgNameBox.getSelectedItem().getValue();
				employeeId=XSSStringEncoder.encodeXSSString(employeeId);
				employeeName=XSSStringEncoder.encodeXSSString(employeeName);
				
				email=XSSStringEncoder.encodeXSSString(email);
				tel=XSSStringEncoder.encodeXSSString(tel);
				state=XSSStringEncoder.encodeXSSString(state);
				idType=XSSStringEncoder.encodeXSSString(idType);
				isManager=XSSStringEncoder.encodeXSSString(isManager);
				parentOrgId=XSSStringEncoder.encodeXSSString(parentOrgId);
				pwd=XSSStringEncoder.encodeXSSString(pwd);
				String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
				String jiami = DesEncryption.desEncrypt(pwd, deskey);
				
				webEmployee.setEmployeeId(employeeId.toLowerCase());
				webEmployee.setEmployeeName(employeeName);
				
				webEmployee.setEmail(email);
				webEmployee.setTel(tel);
				webEmployee.setIdType(Integer.parseInt(idType));
				Date date=new Date();
				WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
				webEmployee.setDataOwner(loginWebEmployee.getEmployeesn());
				if(loginWebEmployee.getWeborg()!=null&&loginWebEmployee.getWeborg().getOrgId()!=null&&!loginWebEmployee.getWeborg().getOrgId().equals("")){
					webEmployee.setDataOwnerGroup(loginWebEmployee.getWeborg().getOrgId());
				}else{
					webEmployee.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
				}
				webEmployee.setLatelyChangedDate(date);
				
				webEmployee.setCreateDate(date);
				webEmployee.setIsManager(Integer.parseInt(isManager));
				webEmployee.setIsnewstop(Integer.parseInt(rdoIsNewsTop.getSelectedItem().getValue().toString()));
				webEmployee.setIsLock(Integer.parseInt(state));
				webEmployee.setPwd(jiami);
				webEmployee.setDataOwner(loginWebEmployee.getEmployeesn());
				//webEmployee.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
				webEmployee.setEmployeesn(UUIDGenerator.getUUID());
				webEmployee.setIsDataEffid(1);
				List<WebOrg> parentOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(parentOrgId);
				webEmployee.setParentWebOrg(parentOrg.get(0));
				loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).addWebEmployee(webEmployee);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "employee_"+webEmployee.getEmployeesn());
				((WebFunctionEmployeeService)SpringUtil.getBean("webFunctionEmployeeService")).deleteWebFunctionEmp(employeesn);
				Treechildren treechildren = (Treechildren) tree.getTreechildren();
				List<Component> Complist = treechildren.getChildren();
				for (Component treeitem : Complist) {
					if(treeitem instanceof Treeitem){
						checkWebEmployeeChild((Treeitem)treeitem);
					}
				}
				
			}else{
				WebEmployee webEmployeeTmp=webEmployeeList.get(0);
				String employeeId=tboxEmployeeId.getValue().trim();
				String employeeName=tboxEmployeeName.getValue().trim();
				
				String email=tboxEMail.getValue().trim();
				String tel=tboxTel.getValue().trim();
				String idType=tboxIdType.getSelectedItem().getValue().toString().trim();
				String state=rgroupstate.getSelectedItem().getValue().toString().trim();
				String isManager=rgroupIsManager.getSelectedItem().getValue().toString().trim();
				String pwd=tboxPwd.getValue().toString().trim();
				String parentOrgId=orgNameBox.getSelectedItem().getValue();
				employeeId=XSSStringEncoder.encodeXSSString(employeeId);
				
				employeeName=XSSStringEncoder.encodeXSSString(employeeName);
				
				email=XSSStringEncoder.encodeXSSString(email);
				tel=XSSStringEncoder.encodeXSSString(tel);
				state=XSSStringEncoder.encodeXSSString(state);
				idType=XSSStringEncoder.encodeXSSString(idType);
				isManager=XSSStringEncoder.encodeXSSString(isManager);
				parentOrgId=XSSStringEncoder.encodeXSSString(parentOrgId);
				pwd=XSSStringEncoder.encodeXSSString(pwd);
				String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
				String jiami = DesEncryption.desEncrypt(pwd, deskey);
				
				webEmployeeTmp.setEmployeeId(employeeId);
				webEmployeeTmp.setEmployeeName(employeeName);
				webEmployeeTmp.setEmail(email);
				webEmployeeTmp.setTel(tel);
				webEmployeeTmp.setIdType(Integer.parseInt(idType));
				Date date=new Date();
				WebEmployee loginWebEmployee=(WebEmployee)ZkUtils.getSessionAttribute("webEmployee");
				webEmployeeTmp.setDataOwner(loginWebEmployee.getEmployeesn());
				if(loginWebEmployee.getWeborg()!=null&&loginWebEmployee.getWeborg().getOrgId()!=null&&!loginWebEmployee.getWeborg().getOrgId().equals("")){
					webEmployeeTmp.setDataOwnerGroup(loginWebEmployee.getWeborg().getOrgId());
				}else{
					webEmployeeTmp.setDataOwnerGroup(loginWebEmployee.getParentWebOrg().getOrgId());
				}

				webEmployeeTmp.setIsManager(Integer.parseInt(isManager));
				webEmployee.setIsnewstop(Integer.parseInt(rdoIsNewsTop.getSelectedItem().getValue().toString()));
				webEmployeeTmp.setIsLock(Integer.parseInt(state));
				webEmployeeTmp.setPwd(jiami);
				List<WebOrg> parentOrg=((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).findParentOrg(parentOrgId);
				webEmployeeTmp.setParentWebOrg(parentOrg.get(0));
				webEmployeeTmp.setIsDataEffid(1);
				loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				((WebEmployeeListService)SpringUtil.getBean("webEmployeeListService")).update(webEmployeeTmp);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),loginwebEmployee.getEmployeesn(), "employee_"+webEmployee.getEmployeesn());
				((WebFunctionEmployeeService)SpringUtil.getBean("webFunctionEmployeeService")).deleteWebFunctionEmp(employeesn);
				Treechildren treechildren = (Treechildren) tree.getTreechildren();
				List<Component> Complist = treechildren.getChildren();
				for (Component treeitem : Complist) {
					if(treeitem instanceof Treeitem){
						checkWebEmployeeChild((Treeitem)treeitem);
					}
				}
			}
		
			
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("webemployee/webEmployee.zul");
			addWebEmployeeWin.detach();
		} catch (Exception e) {
			log.error("新增employee異常",e);
		}
	}
	@Listen("onCheck=#checkAll")
	public void checkAll() {
		Treechildren treechildren = (Treechildren) tree.getTreechildren();
		List<Component> list = treechildren.getChildren();
		for (Component treeitem : list) {
			if(treeitem instanceof Treeitem){
				checkChild((Treeitem)treeitem,checkAll.isChecked());
			}
		}
	}
	
	private void checkChild(Treeitem treeitem,Boolean checked){
		for(Component treerow : treeitem.getChildren()){
			for(Component treecellComponent : treerow.getChildren()){
				if(treecellComponent instanceof Treeitem){
					checkChild((Treeitem)treecellComponent,checked);
				}else if(treecellComponent instanceof Treecell){
					Treecell treecell = (Treecell)treecellComponent;
					if(treecell.getChildren() != null && treecell.getChildren().size() > 0){
						Component checkboxComponent = treecell.getChildren().get(0);
						if(checkboxComponent instanceof Checkbox){
							Checkbox checkbox = (Checkbox)checkboxComponent;
							checkbox.setChecked(checked);	
						}
					}
					
				}
			}
		}
	}
	
	private void checkAllSelected(){
		Boolean bflag = true;
		Treechildren treechildren = (Treechildren) tree.getTreechildren();
		List<Component> list = treechildren.getChildren();
		for (Component treeitem : list) {
			if(treeitem instanceof Treeitem){
				Checkbox checkbox = (Checkbox)treeitem.getLastChild().getFirstChild().getFirstChild();
				if(!checkbox.isChecked()){
					bflag = false;
					break;
				}
			}			
		}
		checkAll.setChecked(bflag);
	}
	private void checkWebEmployeeChild(Treeitem treeitem){
		for(Component treerow : treeitem.getChildren()){
			for(Component treecellComponent : treerow.getChildren()){
				if(treecellComponent instanceof Treeitem){
					checkWebEmployeeChild((Treeitem)treecellComponent);
				}else if(treecellComponent instanceof Treecell){
					Treecell treecell = (Treecell)treecellComponent;
					if(treecell.getChildren() != null && treecell.getChildren().size() > 0){
						Component checkboxComponent = treecell.getChildren().get(0);
						if(checkboxComponent instanceof Checkbox){
							Checkbox checkbox = (Checkbox)checkboxComponent;
							if(checkbox.isChecked()){
								WebFunctionEmployee webFuncEmp=new WebFunctionEmployee();
								webFuncEmp.setUuid(UUIDGenerator.getUUID());
								webFuncEmp.setEmployeesn(webEmployee.getEmployeesn());
								webFuncEmp.setFuncUuid(checkbox.getValue().toString());
								webFuncEmp.setDataOwner(loginwebEmployee.getEmployeesn());
								webFuncEmp.setLatelyChangeDuser(loginwebEmployee.getEmployeesn());
								webFuncEmp.setLatelyChangedDate(new Date());
								if(webEmployee.getWeborg()!=null&&!webEmployee.getWeborg().getOrgId().equals("0")&&webEmployee.getWeborg().getOrgId()!=null){
									webFuncEmp.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
								}else{
									webFuncEmp.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
								}
								webFuncEmp.setCreateDate(new Date());
								((WebFunctionEmployeeService)SpringUtil.getBean("webFunctionEmployeeService")).addWebFunctionEmployee(webFuncEmp);
							}	
						}
					}
					
				}
			}
		}
	}
	
	@Override
	public void doFinally() throws Exception {
		super.doFinally();
		//openAll();
	}
	
	

}
