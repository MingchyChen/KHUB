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
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;

public class ErmResMainDbwsOpenAddComposer extends SelectorComposer<Component>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2196713328213021676L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox openCode;
	@Wire
	private Textbox openName;
	//儲存solr的sql
	String solrQuerySQL="";
	private WebEmployee webEmployee;
	@Wire
	private Listbox ermResMainDbwsOpenLix;
	@Wire
	private Window ermResMainDbwsOpenAddWin;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		search();
	}
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		//solr查詢語句
		String solrQuery="";
		try {
			solrQuery+="type_id:DB";
			//resourceId
			if (openCode.getValue() != null && !openCode.getValue().equals("")) {
				solrQuery+=" resources_id:"+openCode.getValue();
			}
			//名稱
			if (openName.getValue() != null && !openName.getValue().equals("")) {
				solrQuery+=" title:"+openName.getValue();
			}
			//將查詢的sql語句儲存起來
			solrQuerySQL=solrQuery;
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmResourcesMainfileV> resMainDbwsList=((ResourcesMainDbwsSolrSearch)SpringUtil.getBean("resourcesMainDbwsSolrSearch")).resourcesMainSearch(solrQuerySQL);
			ListModelList<ErmResourcesMainfileV> listModel=new ListModelList<ErmResourcesMainfileV>(resMainDbwsList);
			listModel.setMultiple(true);
			ermResMainDbwsOpenLix.setModel(listModel);
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
		int size = ermResMainDbwsOpenLix.getSelectedCount();
		if (size == 0) {
			//請選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("selectOneData"), Labels.getLabel("info"));
			return;
		} else if(size>1){
			//只能選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"), Labels.getLabel("info"));
			return;
		} else{
			Set<Listitem> selectedModels = ermResMainDbwsOpenLix
					.getSelectedItems();
			for (Listitem tmpEST : selectedModels) {
				//ErmResourcesMainDbws ermResourcesMainDbws=tmpEST.getValue();
				ErmResourcesMainfileV  ermResourcesMainDbws=tmpEST.getValue();
				Textbox resourcesId=(Textbox) ermResMainDbwsOpenAddWin.getParent().getFellowIfAny("resourcesIdTxt");
				Textbox resourcesName=(Textbox) ermResMainDbwsOpenAddWin.getParent().getFellowIfAny("resourcesNameTxt");
				resourcesId.setValue(ermResourcesMainDbws.getResourcesId());
				resourcesName.setValue(ermResourcesMainDbws.getTitle());
				ermResMainDbwsOpenAddWin.detach();
			}
		}
	}


}
