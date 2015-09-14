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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
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
import com.claridy.common.util.PowerTreeModel;
import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.FrontWebFuncOrg;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunction;
import com.claridy.domain.WebFunctionOrg;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebRellink;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * sunchao nj
 *單位權限管理作業功能
 *
 */
public class WebOrgComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox orgNameBox;
	@Wire
	private Combobox orgParentNameBox;
	@Wire
	private Intbox sort;
	@Wire
	private Radiogroup isAuthGrp;
	@Wire
	private Tree tree;
	@Wire
	private Checkbox checkAll;
	@Wire
	private Cell webTopCell;
	@Wire
	private Cell webMenuCell;
	@Wire
	private PowerTreeModel btm;
	@Wire
	private SysMenuService menuService;

	private List<WebFunction> webFunctionList;
	@WireVariable
	private WebOrg webOrg;
	private String orgId;
	private int currentPage;
	private WebEmployee webEmployee;
	@Wire
	private Window editWebOrgWin;
	@Wire
	private Window addWebOrgWin;

	public WebOrgComposer() {
		String languageType = null;
		Session session = Sessions.getCurrent();
		if (session.getAttribute("now_Locale") != null) {
			languageType = session.getAttribute("now_Locale").toString();
		}
		menuService = (SysMenuService) SpringUtil.getBean("sysMenuService");
		webFunctionList = menuService.findMenuList(languageType);
		btm = new PowerTreeModel(webFunctionList, menuService);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			orgId=map.get("orgId");
			currentPage=Integer.parseInt(map.get("currentPage"));
			//獲取前臺首頁上方連接集合
			List<WebRellink> topPowerList=((WebOrgListService) SpringUtil.getBean("webOrgListService")).findRelLinkByMenu("1");
			//獲取前臺菜單集合
			List<WebRellink> menuPowerList=((WebOrgListService) SpringUtil.getBean("webOrgListService")).findRelLinkByMenu("2");
			if(orgId!=null&&!"".equals(orgId)){
				
				//根據OrgId獲取首頁上方連接菜單關聯表數據集合
				List<FrontWebFuncOrg> frontOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getFrontOrgList(orgId);
				//遍歷首頁上方連接集合生成checkbox
				for(WebRellink tmpRelLink:topPowerList){
					Checkbox tmpCheck=new Checkbox();
					tmpCheck.setLabel(tmpRelLink.getNameZhTw());
					tmpCheck.setValue(tmpRelLink.getUuid());
					//遍歷關聯表如果存在首頁上方連接則checkbox默認被勾選
					for(FrontWebFuncOrg tmpFrontOrg:frontOrgList){
						if(tmpRelLink.getUuid().equals(tmpFrontOrg.getFuncUuid())){
							tmpCheck.setChecked(true);
						}
					}
					webTopCell.appendChild(tmpCheck);
				}
				//遍歷菜單集合生成checkbox
				for(WebRellink tmpRelLink:menuPowerList){
					Checkbox tmpCheck=new Checkbox();
					tmpCheck.setLabel(tmpRelLink.getNameZhTw());
					tmpCheck.setValue(tmpRelLink.getUuid());
					//遍歷關聯表如果存在菜單則checkbox默認被勾選
					for(FrontWebFuncOrg tmpFrontOrg:frontOrgList){
						if(tmpRelLink.getUuid().equals(tmpFrontOrg.getFuncUuid())){
							tmpCheck.setChecked(true);
						}
					}
					webMenuCell.appendChild(tmpCheck);
				}
				
			}else{
				sort.setValue(0);
				//遍歷首頁上方連接集合生成checkbox
				for(WebRellink tmpRelLink:topPowerList){
					Checkbox tmpCheck=new Checkbox();
					tmpCheck.setLabel(tmpRelLink.getNameZhTw());
					tmpCheck.setValue(tmpRelLink.getUuid());
					webTopCell.appendChild(tmpCheck);
				}
				//遍歷菜單集合生成checkbox
				for(WebRellink tmpRelLink:menuPowerList){
					Checkbox tmpCheck=new Checkbox();
					tmpCheck.setLabel(tmpRelLink.getNameZhTw());
					tmpCheck.setValue(tmpRelLink.getUuid());
					webMenuCell.appendChild(tmpCheck);
				}
			}
			//生成菜單權限tree
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
					//點擊修改進入下麵判斷
					if(orgId!=null&&!"".equals(orgId)){
						//根據OrgId獲取單位菜單關聯表集合
						List<WebFunctionOrg> webFuncOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getFunctionOrgList(orgId);
						String languageType = null;
						Session session = Sessions.getCurrent();
						if (session.getAttribute("now_Locale") != null) {
							languageType = session.getAttribute("now_Locale").toString();
						}
						menuService = (SysMenuService) SpringUtil.getBean("sysMenuService");
						//獲取所有菜單集合
						List<WebFunction> funcList = menuService.findAllList(languageType);
						//如果關聯表數據集合與菜單集合相等，那麼全選菜單默認被勾選
						if(webFuncOrgList.size()==funcList.size()){
							checkAll.setChecked(true);
						}else{
							checkAll.setChecked(false);
						}
						//如果關聯表存在菜單數據，那麼默認被選中
						for(WebFunctionOrg funcOrg:webFuncOrgList){
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
			
			if(orgId!=null){
				webOrg=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgId",orgId).get(0);
			}
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgIdParent","0");
			Comboitem com = new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("0");
			orgParentNameBox.appendChild(com);
			for(int i=0;i<webOrgList.size();i++){
				WebOrg webOrg1=new WebOrg();
				webOrg1=webOrgList.get(i);
				com=new Comboitem();
				com.setLabel(webOrg1.getOrgName());
				com.setValue(webOrg1.getOrgId());
				orgParentNameBox.appendChild(com);
				if(webOrg!=null&&!"".equals(webOrg)){
					List<WebOrg> webOrgList1=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgId",webOrg.getOrgIdParent());
					if(webOrgList1.size()>0){
						WebOrg webOrgParent=webOrgList1.get(0);
						if(webOrg1.getOrgId().equals(webOrgParent.getOrgId())){
							orgParentNameBox.setSelectedIndex(i+1);
						}
					}
				}
			}
			if(webOrg!=null&&!"".equals(webOrg)){
				orgNameBox.setValue(webOrg.getOrgName());
				isAuthGrp.setSelectedIndex(webOrg.getIsAuth());
				sort.setValue(webOrg.getSort());
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("單位權限管理作業進入新增或修改頁面報錯",e);
		}
	}
	@Listen("onClick=#updateBtn")
	public void updateOrg(){
		try {
			String orgName;
			String orgParentName;
			String isAuth;
			if(orgNameBox.getValue()!=null&&!orgNameBox.getValue().trim().equals("")){
				String orgIdParent="0";
				if(orgParentNameBox.getSelectedItem()!=null&&!"".equals(orgParentNameBox.getSelectedItem())){
					orgIdParent=orgParentNameBox.getSelectedItem().getValue();
				}
				if(webOrg.getOrgName().equals(orgNameBox.getValue())&&webOrg.getOrgIdParent().equals(orgIdParent)){
					orgName=orgNameBox.getValue().trim();
					orgName=XSSStringEncoder.encodeXSSString(orgName);
					webOrg.setOrgName(orgName);
				}else{
					WebOrg webOrgTemp=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgByName(orgNameBox.getValue(),orgIdParent);
					if(webOrgTemp.getOrgId()!=null&&!"".equals(webOrgTemp.getOrgId())){
						ZkUtils.showExclamation(
								Labels.getLabel("webOrg.OrgNameMess"),
								Labels.getLabel("warn"));
						orgNameBox.focus();
						return;
					}else{
						orgName=orgNameBox.getValue().trim();
						orgName=XSSStringEncoder.encodeXSSString(orgName);
						webOrg.setOrgName(orgName);
					}
				}
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("webOrg.weborgLix.orgName") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameBox.focus();
				return;
			}
			if(orgParentNameBox.getSelectedItem()!=null){
				orgParentName=orgParentNameBox.getSelectedItem().getValue().toString().trim();
				orgParentName=XSSStringEncoder.encodeXSSString(orgParentName);
				webOrg.setOrgIdParent(orgParentName);
			}else{
				webOrg.setOrgIdParent("0");
			}
			if(isAuthGrp.getSelectedItem()!=null){
				isAuth=isAuthGrp.getSelectedItem().getValue().toString().trim();
				isAuth=XSSStringEncoder.encodeXSSString(isAuth);
				webOrg.setIsAuth(Integer.parseInt(isAuth));
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("webOrg.weborgLix.isSelect") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameBox.focus();
				return;
			}
			if(sort.getValue()!=null){
				int sortVal=sort.getValue();
				webOrg.setSort(sortVal);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("firstTopLink.sort") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				sort.focus();
				return;
			}
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webOrg.setLatelyChangedUser(webEmployee.getEmployeesn());
			webOrg.setLatelyChangedDate(new Date());
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).eidtWebOrg(webOrg);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "org_"+webOrg.getOrgId());
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteWebFuncOrg(webOrg.getOrgId());
			//把選擇的菜單插入webfunction_org關聯表
			Treechildren treechildren = (Treechildren) tree.getTreechildren();
			List<Component> list = treechildren.getChildren();
			for (Component treeitem : list) {
				if(treeitem instanceof Treeitem){
					checkOrgChild((Treeitem)treeitem);
				}
			}
			//把選擇的菜單插入frontwebfunction_org關聯表
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteFrontFuncOrg(webOrg.getOrgId());
			List<Component> topCellList=webTopCell.getChildren();
			for (Component tmpCheck : topCellList) {
				Checkbox tmpCheckbox=(Checkbox)tmpCheck;
				if(tmpCheckbox.isChecked()==true){
					FrontWebFuncOrg tmpFuncOrg=new FrontWebFuncOrg();
					tmpFuncOrg.setUuid(UUIDGenerator.getUUID());
					tmpFuncOrg.setFuncUuid(tmpCheckbox.getValue().toString());
					tmpFuncOrg.setOrgId(webOrg.getOrgId());
					tmpFuncOrg.setDataOwner(webEmployee.getEmployeesn());
					if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
					}else{
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
					}
					tmpFuncOrg.setCreateDate(new Date());
					((WebOrgListService)SpringUtil.getBean("webOrgListService")).addFrontFunctOrg(tmpFuncOrg);
				}
			}
			//把選擇的菜單插入frontwebfunction_org關聯表
			List<Component> menuCellList=webMenuCell.getChildren();
			for (Component tmpCheck : menuCellList) {
				Checkbox tmpCheckbox=(Checkbox)tmpCheck;
				if(tmpCheckbox.isChecked()==true){
					FrontWebFuncOrg tmpFuncOrg=new FrontWebFuncOrg();
					tmpFuncOrg.setUuid(UUIDGenerator.getUUID());
					tmpFuncOrg.setFuncUuid(tmpCheckbox.getValue().toString());
					tmpFuncOrg.setOrgId(webOrg.getOrgId());
					tmpFuncOrg.setDataOwner(webEmployee.getEmployeesn());
					if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
					}else{
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
					}
					tmpFuncOrg.setCreateDate(new Date());
					((WebOrgListService)SpringUtil.getBean("webOrgListService")).addFrontFunctOrg(tmpFuncOrg);
				}
			}
			
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			/*String url="webOrg/webOrg.zul?currentPage=5";
			ZkUtils.refurbishMethod(url);*/
			editSearchOrgList();
			editWebOrgWin.detach();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("單位權限管理作業執行修改方法報錯",e);
		}
	}
	
	@Listen("onClick=#saveBtn")
	public void saveOrg(){
		try {
			String orgName;
			String orgParentName;
			String isAuth;
			webOrg=new WebOrg();
			webOrg.setOrgId(UUIDGenerator.getUUID());
			if(orgNameBox.getValue()!=null&&!orgNameBox.getValue().trim().equals("")){
				String orgIdParent="0";
				if(orgParentNameBox.getSelectedItem()!=null&&!"".equals(orgParentNameBox.getSelectedItem())){
					orgIdParent=orgParentNameBox.getSelectedItem().getValue();
				}
				WebOrg webOrgTemp=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getOrgByName(orgNameBox.getValue(),orgIdParent);
				if(webOrgTemp.getOrgId()!=null&&!"".equals(webOrgTemp.getOrgId())){
					ZkUtils.showExclamation(
							Labels.getLabel("webOrg.OrgNameMess"),
							Labels.getLabel("warn"));
					orgNameBox.focus();
					return;
				}else{
					orgName=orgNameBox.getValue().trim();
					orgName=XSSStringEncoder.encodeXSSString(orgName);
					webOrg.setOrgName(orgName);
				}
				
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("webOrg.weborgLix.orgName") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameBox.focus();
				return;
			}
			if(orgParentNameBox.getSelectedItem()!=null){
				orgParentName=orgParentNameBox.getSelectedItem().getValue().toString().trim();
				orgParentName=XSSStringEncoder.encodeXSSString(orgParentName);
				webOrg.setOrgIdParent(orgParentName);
			}else{
				webOrg.setOrgIdParent("0");
			}
			if(isAuthGrp.getSelectedItem()!=null){
				isAuth=isAuthGrp.getSelectedItem().getValue().toString().trim();
				isAuth=XSSStringEncoder.encodeXSSString(isAuth);
				webOrg.setIsAuth(Integer.parseInt(isAuth));
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("webOrg.weborgLix.isSelect") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				orgNameBox.focus();
				return;
			}
			if(sort.getValue()!=null){
				int sortVal=sort.getValue();
				webOrg.setSort(sortVal);
			}else{
				ZkUtils.showExclamation(
						Labels.getLabel("firstTopLink.sort") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				sort.focus();
				return;
			}
			Date date=new Date();
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webOrg.setWebEmployee(webEmployee);
			if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
				webOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			}else{
				webOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			webOrg.setCreateDate(date);
			webOrg.setIsDataEffid(1);
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).addWebOrg(webOrg);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).insertLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "org_"+webOrg.getOrgId());
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteWebFuncOrg(webOrg.getOrgId());
			//把選擇的菜單插入webfunction_org關聯表
			Treechildren treechildren = (Treechildren) tree.getTreechildren();
			List<Component> list = treechildren.getChildren();
			for (Component treeitem : list) {
				if(treeitem instanceof Treeitem){
					checkOrgChild((Treeitem)treeitem);
				}
			}
			//把選擇的菜單插入frontwebfunction_org關聯表
			((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteFrontFuncOrg(webOrg.getOrgId());
			List<Component> topCellList=webTopCell.getChildren();
			for (Component tmpCheck : topCellList) {
				Checkbox tmpCheckbox=(Checkbox)tmpCheck;
				if(tmpCheckbox.isChecked()==true){
					FrontWebFuncOrg tmpFuncOrg=new FrontWebFuncOrg();
					tmpFuncOrg.setUuid(UUIDGenerator.getUUID());
					tmpFuncOrg.setFuncUuid(tmpCheckbox.getValue().toString());
					tmpFuncOrg.setOrgId(webOrg.getOrgId());
					tmpFuncOrg.setDataOwner(webEmployee.getEmployeesn());
					if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
					}else{
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
					}
					tmpFuncOrg.setCreateDate(new Date());
					((WebOrgListService)SpringUtil.getBean("webOrgListService")).addFrontFunctOrg(tmpFuncOrg);
				}
			}
			//把選擇的菜單插入frontwebfunction_org關聯表
			List<Component> menuCellList=webMenuCell.getChildren();
			for (Component tmpCheck : menuCellList) {
				Checkbox tmpCheckbox=(Checkbox)tmpCheck;
				if(tmpCheckbox.isChecked()==true){
					FrontWebFuncOrg tmpFuncOrg=new FrontWebFuncOrg();
					tmpFuncOrg.setUuid(UUIDGenerator.getUUID());
					tmpFuncOrg.setFuncUuid(tmpCheckbox.getValue().toString());
					tmpFuncOrg.setOrgId(webOrg.getOrgId());
					tmpFuncOrg.setDataOwner(webEmployee.getEmployeesn());
					if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
					}else{
						tmpFuncOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
					}
					tmpFuncOrg.setCreateDate(new Date());
					((WebOrgListService)SpringUtil.getBean("webOrgListService")).addFrontFunctOrg(tmpFuncOrg);
				}
			}
			
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
			String url="webOrg/webOrg.zul";
			ZkUtils.refurbishMethod(url);
			//addSearchOrgList();
			addWebOrgWin.detach();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("單位權限管理作業執行新增方法報錯",e);
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
	private void checkOrgChild(Treeitem treeitem){
		for(Component treerow : treeitem.getChildren()){
			for(Component treecellComponent : treerow.getChildren()){
				if(treecellComponent instanceof Treeitem){
					checkOrgChild((Treeitem)treecellComponent);
				}else if(treecellComponent instanceof Treecell){
					Treecell treecell = (Treecell)treecellComponent;
					if(treecell.getChildren() != null && treecell.getChildren().size() > 0){
						Component checkboxComponent = treecell.getChildren().get(0);
						if(checkboxComponent instanceof Checkbox){
							Checkbox checkbox = (Checkbox)checkboxComponent;
							if(checkbox.isChecked()){
								WebFunctionOrg webFuncOrg=new WebFunctionOrg();
								webFuncOrg.setUuid(UUIDGenerator.getUUID());
								webFuncOrg.setFuncUuid(checkbox.getValue().toString());
								webFuncOrg.setOrgId(webOrg.getOrgId());
								webFuncOrg.setDataOwner(webEmployee.getEmployeesn());
								if(webEmployee.getWeborg()!=null&&webEmployee.getWeborg().getOrgId()!=null&&!"0".equals(webEmployee.getWeborg().getOrgId())){
									webFuncOrg.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
								}else{
									webFuncOrg.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
								}
								webFuncOrg.setCreateDate(new Date());
								((WebOrgListService)SpringUtil.getBean("webOrgListService")).addWebFunctionOrg(webFuncOrg);
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
	public void addSearchOrgList(){
		Listbox weborgLix =  (Listbox)addWebOrgWin.getParent().getFellowIfAny("weborgLix");
		Textbox orgNameBox=(Textbox)addWebOrgWin.getParent().getFellowIfAny("orgNameBox");	
		Combobox orgParentNameBox=(Combobox)addWebOrgWin.getParent().getFellowIfAny("orgParentNameBox");
		try{
			String orgName=orgNameBox.getValue().trim();	
			orgName=XSSStringEncoder.encodeXSSString(orgName);
			
			String orgParentName="";
			if(orgParentNameBox.getSelectedItem()!=null&&!orgParentNameBox.getSelectedItem().getValue().equals("0")){
				orgParentName=orgParentNameBox.getSelectedItem().getValue().toString();
			}
			
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList(orgName,orgParentName,webEmployee);
			
			ListModelList<WebOrg> WebOrgModel=new ListModelList<WebOrg>(webOrgList);
			WebOrgModel.setMultiple(true);
			weborgLix.setModel(WebOrgModel);
			weborgLix.setActivePage(currentPage);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}
	public void editSearchOrgList(){
		Listbox weborgLix =  (Listbox)editWebOrgWin.getParent().getFellowIfAny("weborgLix");
		Textbox orgNameBox=(Textbox)editWebOrgWin.getParent().getFellowIfAny("orgNameBox");	
		Combobox orgParentNameBox=(Combobox)editWebOrgWin.getParent().getFellowIfAny("orgParentNameBox");
		try{
			String orgName=orgNameBox.getValue().trim();	
			orgName=XSSStringEncoder.encodeXSSString(orgName);
			
			String orgParentName="";
			if(orgParentNameBox.getSelectedItem()!=null&&!orgParentNameBox.getSelectedItem().getValue().equals("0")){
				orgParentName=orgParentNameBox.getSelectedItem().getValue().toString();
			}
			
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList(orgName,orgParentName,webEmployee);
			
			ListModelList<WebOrg> WebOrgModel=new ListModelList<WebOrg>(webOrgList);
			WebOrgModel.setMultiple(true);
			weborgLix.setModel(WebOrgModel);
			weborgLix.setActivePage(currentPage);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}
	public PowerTreeModel getBtm() {
		return btm;
	}

	public List<WebFunction> getWebFunctionList() {
		return webFunctionList;
	}

	public void setWebFunctionList(List<WebFunction> webFunctionList) {
		this.webFunctionList = webFunctionList;
	}
}
