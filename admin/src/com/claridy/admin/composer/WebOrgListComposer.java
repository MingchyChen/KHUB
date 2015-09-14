package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * sunchao nj
 * 單位權限管理作業功能
 * 2014/07/17
 */
public class WebOrgListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4495610820701239659L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Listbox weborgLix;
	@Wire
	private Textbox orgNameBox;
	@Wire
	private Combobox orgParentNameBox;
	@WireVariable
	private List<WebOrg> webOrgList;
	
	@Wire
	private Window addWebOrgWin;
	
	@Wire
	private Window webOrgWin;
	
	@WireVariable
	private WebOrg webOrg;
	
	WebEmployee webEmployee;

	public WebOrg getWebOrg() {
		return webOrg;
	}

	public void setWebOrg(WebOrg webOrg) {
		this.webOrg = webOrg;
	}

	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try{
			String orgName=orgNameBox.getValue().trim();	
			orgName=XSSStringEncoder.encodeXSSString(orgName);
			
			String orgParentName="";
			if(orgParentNameBox.getSelectedItem()!=null&&!orgParentNameBox.getSelectedItem().getValue().equals("0")){
				orgParentName=orgParentNameBox.getSelectedItem().getValue().toString();
			}
			
			if (StringUtils.isBlank(orgName)&&StringUtils.isBlank(orgParentName)) {
				ZkUtils.showExclamation(
						Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				orgNameBox.focus();
				return;
			}
			
			List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList(orgName,orgParentName,webEmployee);
			
			ListModelList<WebOrg> WebOrgModel=new ListModelList<WebOrg>(webOrgList);
			WebOrgModel.setMultiple(true);
			weborgLix.setModel(WebOrgModel);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}
	
	@Listen("onClick=#addBtn")
	public void addWebEmployee(){
		int currentPage=weborgLix.getActivePage();
		Map map = new HashMap();
		map.put("currentPage",  String.valueOf(currentPage));
		addWebOrgWin=(Window)ZkUtils.createComponents("/WEB-INF/pages/system/webOrg/webOrgAdd.zul", this.getSelf(), map);
		addWebOrgWin.doModal();
	}
	
	@Listen("onClick=#deleteBtn")
	public void deleteWebEmployee(){
		try{
			int updateChecked=weborgLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=weborgLix.getSelectedItems();
							for(Listitem employee:listitem){
								webOrg=employee.getValue();
								List<WebOrg> webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgIdParent",webOrg.getOrgId());
								if(webOrgList.size()>0){
									ZkUtils.showExclamation(
											Labels.getLabel("webOrg.weborgLix.existOrg"),
											Labels.getLabel("warn"));
									return;
								}
								int sum=((WebOrgListService)SpringUtil.getBean("webOrgListService")).getEmpAndAccOrgSum(webOrg.getOrgId());
								if(sum>0){
									ZkUtils.showExclamation(
											Labels.getLabel("webOrg.weborgLix.existOrg"),
											Labels.getLabel("warn"));
									return;
								}
								((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteWebOrg(webOrg);
								//根據OrgId刪除webfunction_org關聯表數據
								((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteWebFuncOrg(webOrg.getOrgId());
								//根據OrgId刪除frontwebfunction_org關聯表數據
								((WebOrgListService)SpringUtil.getBean("webOrgListService")).deleteFrontFuncOrg(webOrg.getOrgId());
								((WebSysLogService)SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webOrg.getOrgId(),"org_"+webOrg.getOrgId());
							}
							String url="webOrg/webOrg.zul";
							ZkUtils.refurbishMethod(url);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("刪除單位權限管理作業集合出錯",e);
		}
	}
	
	@Listen("onClick=#showAll")
	public void shoaAll(){
		webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList(null,null,webEmployee);
		ListModelList<WebOrg> tmpLML=new ListModelList<WebOrg>(webOrgList);
		tmpLML.setMultiple(true);
		orgNameBox.setValue("");
		orgParentNameBox.setSelectedIndex(0);
		weborgLix.setModel(tmpLML);	
	}
	@Override
	public void doAfterCompose(Component comp) {
		try{
			super.doAfterCompose(comp);
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgList(null,null,webEmployee);
			List<WebOrg> webParOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findEdtAddWebOrgList("orgIdParent","0");
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			orgParentNameBox.appendChild(com);
			for(int i=0;i<webParOrgList.size();i++){
				WebOrg webParOrg=new WebOrg();
				webParOrg=webParOrgList.get(i);
				com=new Comboitem();
				com.setLabel(webParOrg.getOrgName());
				com.setValue(webParOrg.getOrgId());
				orgParentNameBox.appendChild(com);
			}
			ListModelList<WebOrg> tmpLML=new ListModelList<WebOrg>(webOrgList);
			tmpLML.setMultiple(true);
			orgParentNameBox.setSelectedIndex(0);
			weborgLix.setModel(tmpLML);	
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢單位權限管理作業集合出錯",e);
		}
	}
}
