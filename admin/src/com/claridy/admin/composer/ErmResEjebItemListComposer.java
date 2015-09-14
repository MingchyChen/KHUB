package com.claridy.admin.composer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainEjeb;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmResourcesReltitle;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.CodePublisherService;
import com.claridy.facade.ErmCodeDbService;
import com.claridy.facade.ErmResMainEjebService;
import com.claridy.facade.ErmResourcesReltitleService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;

public class ErmResEjebItemListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private String resourcesId;
	private static final long serialVersionUID = -7456762873355613964L;
	@Wire
	private Listbox resMainEjebItemLix;
	@Wire
	private Textbox resourcesIdTxt;
	@Wire
	private Textbox titleTxt;
	@Wire
	private Window ermResEjebItemListWin;
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
//		Map<String, String> map = new HashMap<String, String>();
//		map = ZkUtils.getExecutionArgs();
		webEmployee = (WebEmployee) ZkUtils
				.getSessionAttribute("webEmployee");
		resourcesId = Executions.getCurrent().getParameter("resourcesId");
		ErmResourcesMainEjeb mainEjeb = ((ErmResMainEjebService) SpringUtil
				.getBean("ermResMainEjebService"))
				.getResMainEjebByResId(resourcesId);
		List<ErmResourcesEjebItem> ejebItemList=((ErmResMainEjebService) SpringUtil
				.getBean("ermResMainEjebService")).findResMainEjebItemByResId(resourcesId);

		resourcesIdTxt.setValue(resourcesId);//
		titleTxt.setValue(mainEjeb.getTitle());
		
		List<ErmCodeDb> dbList=((ErmCodeDbService) SpringUtil
				.getBean("ermCodeDbService")).findAllCodeDb(webEmployee);
		for(int i=0;i<ejebItemList.size();i++){
			for(int j=0;j<dbList.size();j++){
				if(ejebItemList.get(i).getDbId().equals(dbList.get(j).getDbId())){
					//ejebItemList.get(i).setDbId(dbList.get(j).getName());
					ejebItemList.get(i).setDbIdShowStr(dbList.get(j).getName());
				}
			}
		}
		List<ErmCodePublisher> publishList=((CodePublisherService) SpringUtil
				.getBean("codePublisherService")).findAll(webEmployee);
		for(int i=0;i<ejebItemList.size();i++){
			for(int j=0;j<publishList.size();j++){
				if(ejebItemList.get(i).getPublisherId().equals(publishList.get(j).getPublisherId())){
					ejebItemList.get(i).setPublisherId(publishList.get(j).getName());
				}
			}
		}
		
		ListModelList<ErmResourcesEjebItem> listModel = new ListModelList<ErmResourcesEjebItem>(ejebItemList);
		listModel.setMultiple(true);
		resMainEjebItemLix.setModel(listModel);
		

	}
	@Listen("onClick=#addDataBaseBtn")
	public void toAddDataBase() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("resourceId", resourcesId);
			ermResEjebItemListWin = (Window) ZkUtils.createComponents(
					"/WEB-INF/pages/system/ermResMainEjeb/ermCodeDbAdd.zul",
					null, map);
			ermResEjebItemListWin.doModal();
		} catch (Exception e) {
			log.error("ermResMainEjeb新增加載異常" + e);
		}
	}
	
	@Listen("onClick=#editTitleNameBtn")
	public void toEditTitleName(){
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("resourceId", resourcesId);
			ermResEjebItemListWin = (Window) ZkUtils.createComponents(
					"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebEditTitle.zul",
					null, map);
			ermResEjebItemListWin.doModal();
		} catch (Exception e) {
			log.error("ermResMainEjeb跳轉異常" + e);
		}
	}
	@Listen("onClick=#ejebItemCancelBtn")
	public void toMainFileVList(){
		String url="ermResMainEjeb/ermResMainEjeb.zul";
		ZkUtils.refurbishMethod(url);
	}
	@Listen("onClick=#addResBtn")
	public void toResourcesPg(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourcesId",resourcesId);
		ermResEjebItemListWin.detach();
		ermResEjebItemListWin=(Window) ZkUtils
		.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResEjebMainten.zul",
				null, map);
		ermResEjebItemListWin.doModal();
	}
	//刪除所屬資料庫
	@Listen("onClick=#delEjebItemBtn")
	public void delRelTitleBtn(){
		try {
			int updateChecked=resMainEjebItemLix.getSelectedCount();
			if(updateChecked>0){
				//“你確定要刪除該資料嗎？”
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"),new EventListener() {
					public void onEvent(Event event){
						int clickButton=(Integer) event.getData();
						if(clickButton==Messagebox.OK){
							Set<Listitem> listitem=resMainEjebItemLix.getSelectedItems();
							for(Listitem ejebItem:listitem){
								ErmResourcesEjebItem ermEjebItem=ejebItem.getValue();
								((ErmResMainEjebService)SpringUtil.getBean("ermResMainEjebService")).deleteErmEjebItem(ermEjebItem);
							}
							//更新solr數據
							try {
								((ResourcesMainEjebSolrSearch) SpringUtil
										.getBean("resourcesMainEjebSolrSearch"))
										.resources_main_ejeb_editData(resourcesId);
							} catch (SQLException e) {
								log.error("更新solr數據報錯",e);
							}
							List<ErmResourcesEjebItem> ejebItemList=((ErmResMainEjebService) SpringUtil
									.getBean("ermResMainEjebService")).findResMainEjebItemByResId(resourcesId);
							
							List<ErmCodeDb> dbList=((ErmCodeDbService) SpringUtil
									.getBean("ermCodeDbService")).findAllCodeDb(webEmployee);
							for(int i=0;i<ejebItemList.size();i++){
								for(int j=0;j<dbList.size();j++){
									if(ejebItemList.get(i).getDbId().equals(dbList.get(j).getDbId())){
										//ejebItemList.get(i).setDbId(dbList.get(j).getName());
										ejebItemList.get(i).setDbIdShowStr(dbList.get(j).getName());
									}
								}
							}
							List<ErmCodePublisher> publishList=((CodePublisherService) SpringUtil
									.getBean("codePublisherService")).findAll(webEmployee);
							for(int i=0;i<ejebItemList.size();i++){
								for(int j=0;j<publishList.size();j++){
									if(ejebItemList.get(i).getPublisherId().equals(publishList.get(j).getPublisherId())){
										ejebItemList.get(i).setPublisherId(publishList.get(j).getName());
									}
								}
							}
							
							ListModelList<ErmResourcesEjebItem> listModel = new ListModelList<ErmResourcesEjebItem>(ejebItemList);
							listModel.setMultiple(true);
							resMainEjebItemLix.setModel(listModel);
						}
					}
				});
			}else{
				// "請先選擇一筆資料"
				ZkUtils.showExclamation(Labels.getLabel("selectOneData"),
						Labels.getLabel("info"));
				return;
			}
		} catch (Exception e) {
			log.error("刪除所屬資料庫報錯"+e);
		}
	}
}
