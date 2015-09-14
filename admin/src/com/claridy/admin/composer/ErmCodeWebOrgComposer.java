package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.WebOrgListService;

public class ErmCodeWebOrgComposer extends SelectorComposer<Component>{
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox openCode;
	@Wire
	private Textbox openName;
	@Wire
	private Listbox ermWebOrgOpenLix;
	@Wire
	private Window ermResWebOrgOpenWin;
	private WebEmployee webEmployee;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		search();
	}
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			String tempOrgId="";
			String tempOrgName="";
			if(openCode.getValue()!=null&&!"".equals(openCode.getValue()))
			{
				tempOrgId=openCode.getValue();
			}
			if(openName.getValue()!=null&&!"".equals(openName.getValue())){
				tempOrgName=openName.getValue();
			}
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebOrg> resWebOrgList=((WebOrgListService)SpringUtil.getBean("webOrgListService")).findWebOrgParam(tempOrgId, tempOrgName);
			ListModelList<WebOrg> listModel=new ListModelList<WebOrg>(resWebOrgList);
			listModel.setMultiple(true);
			ermWebOrgOpenLix.setModel(listModel);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢電子資料庫/網路資源集合出錯",e);
		}
	}
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		openCode.setValue("");
		openName.setValue("");
		search();
	}
	@Listen("onClick = #selectBtn")
	public void selectBtn() {
		int size = ermWebOrgOpenLix.getSelectedCount();
		if (size == 0) {
			//請選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("selectOneData"), Labels.getLabel("info"));
			return;
		} else if(size>1){
			//只能選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"), Labels.getLabel("info"));
			return;
		} else{
			Set<Listitem> selectedModels = ermWebOrgOpenLix
					.getSelectedItems();
			for (Listitem tmpEST : selectedModels) {
				WebOrg webOrg=tmpEST.getValue();
				Textbox resourceIdTxt=(Textbox) ermResWebOrgOpenWin.getParent().getFellowIfAny("resourceIdTxt");
				Textbox orgNameTxt=(Textbox) ermResWebOrgOpenWin.getParent().getFellowIfAny("orgNameTxt");
				resourceIdTxt.setValue(webOrg.getOrgId());
				orgNameTxt.setValue(webOrg.getOrgName());
				ermResWebOrgOpenWin.detach();
			}
		}
	}
	
}
