package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodePublisher;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.CodePublisherService;

/**
 * 
 * sunchao nj
 * 出版商/代理商彈出框
 * 2014/08/14
 */
public class ErmResPubOpenListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4495610820701239659L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Wire
	private Textbox openCode;
	@Wire
	private Textbox openName;
	@Wire
	private Listbox ermResPubOpenLix;
	@Wire
	private Window ermResPubOpenWin;
	private WebEmployee webEmployee;
	private String openValue;
	private String openType;
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			String code=openCode.getValue();
			String name=openName.getValue();
			code= XSSStringEncoder.encodeXSSString(code);
			name= XSSStringEncoder.encodeXSSString(name);
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmCodePublisher> result = ((CodePublisherService) SpringUtil
					.getBean("codePublisherService")).findPublisherList(openValue,openType,name,code,webEmployee);
			ListModelList<ErmCodePublisher> listModel = new ListModelList<ErmCodePublisher>(
					result);
			listModel.setMultiple(true);
			ermResPubOpenLix.setModel(listModel);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢語言集合出錯",e);
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
		int size = ermResPubOpenLix.getSelectedCount();
		if (size == 0) {
			//請選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("selectOneData"), Labels.getLabel("info"));
			return;
		} else if(size>1){
			//只能選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"), Labels.getLabel("info"));
			return;
		} else{
			Set<Listitem> selectedModels = ermResPubOpenLix.getSelectedItems();
			for (Listitem tmpEST : selectedModels) {
				ErmCodePublisher tmpCodePublisher = tmpEST.getValue();
				if (openValue.trim().equals("publisher")) {
					Label publisherSearch =  (Label)ermResPubOpenWin.getParent().getFellowIfAny("publisherSearch");
					Textbox pubTbx =  (Textbox)ermResPubOpenWin.getParent().getFellowIfAny("pubTbx");
					publisherSearch.setValue(tmpCodePublisher.getPublisherId());
					pubTbx.setValue(tmpCodePublisher.getName());
				}else{
					if(openType!=null&&openType.equals("odetails")){
						Label agentedIdBox =  (Label)ermResPubOpenWin.getParent().getFellowIfAny("agentedIdBox");
						Textbox agentedNameBox =  (Textbox)ermResPubOpenWin.getParent().getFellowIfAny("agentedNameBox");
						agentedIdBox.setValue(tmpCodePublisher.getPublisherId());
						agentedNameBox.setValue(tmpCodePublisher.getName());
					}else{
						Label agentedSearch =  (Label)ermResPubOpenWin.getParent().getFellowIfAny("agentedSearch");
						Textbox agenTbx =  (Textbox)ermResPubOpenWin.getParent().getFellowIfAny("agenTbx");
						agentedSearch.setValue(tmpCodePublisher.getPublisherId());
						agenTbx.setValue(tmpCodePublisher.getName());
					}
				}
				ermResPubOpenWin.detach();
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap = ZkUtils.getExecutionArgs();
			openValue=tmpMap.get("openValue").toString();
			openType=tmpMap.get("openType").toString();
			if (openValue.trim().equals("publisher")) {
				ermResPubOpenWin.setTitle(Labels.getLabel("ermResMainDbws.publisherId"));
			}else {
				ermResPubOpenWin.setTitle(Labels.getLabel("ermResMainDbws.agentedId"));
			}
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			search();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查詢通知單範本管理集合出錯",e);
		}
	}
}
