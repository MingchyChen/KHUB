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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;

/**
 * 
 * sunchao nj
 * 主題分類彈出框
 * 2014/08/14
 */
public class ErmResSubOpenListComposer extends SelectorComposer<Component> {
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
	private Listbox ermResSubOpenLix;
	@Wire
	private Window ermResSubOpenWin;
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
			List<ErmCodeGeneralCode> result = ((ErmCodeGeneralCodeService) SpringUtil
					.getBean("ermCodeGeneralCodeService")).findGeneralList(openValue,openType,name,code,webEmployee);
			ListModelList<ErmCodeGeneralCode> listModel = new ListModelList<ErmCodeGeneralCode>(
					result);
			listModel.setMultiple(true);
			ermResSubOpenLix.setModel(listModel);
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
		int size = ermResSubOpenLix.getSelectedCount();
		if (size == 0) {
			//請選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("selectOneData"), Labels.getLabel("info"));
			return;
		} else if(size>1){
			//只能選擇一筆資料
			ZkUtils.showExclamation(Labels.getLabel("onlyOneSelected"), Labels.getLabel("info"));
			return;
		} else{
			Set<Listitem> selectedModels = ermResSubOpenLix
					.getSelectedItems();
			for (Listitem tmpEST : selectedModels) {
				ErmCodeGeneralCode tmpGeneralCode = tmpEST.getValue();
				if(openValue!=null&&openValue.equals("DBSUB")){
					Textbox subjectIdBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectIdBox");
					Textbox subjectNameBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectNameBox");
					subjectIdBox.setValue(tmpGeneralCode.getGeneralcodeId());
					subjectNameBox.setValue(tmpGeneralCode.getName1());
				}
				if(openValue!=null&&openValue.equals("EJSUB")){
					Textbox subjectIdBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectIdBox");
					Textbox subjectNameBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectNameBox");
					subjectIdBox.setValue(tmpGeneralCode.getGeneralcodeId());
					subjectNameBox.setValue(tmpGeneralCode.getName1());
				}
				if(openValue!=null&&openValue.equals("EBSUB")){
					Textbox subjectIdBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectIdBox");
					Textbox subjectNameBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectNameBox");
					subjectIdBox.setValue(tmpGeneralCode.getGeneralcodeId());
					subjectNameBox.setValue(tmpGeneralCode.getName1());
				}
				if(openValue!=null&&openValue.equals("WSSUB")){
					Textbox subjectIdBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectIdBox");
					Textbox subjectNameBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("subjectNameBox");
					subjectIdBox.setValue(tmpGeneralCode.getGeneralcodeId());
					subjectNameBox.setValue(tmpGeneralCode.getName1());
				}
				if(openValue!=null&&openValue.equals("DBTYPE")){
					Textbox dbtypeIdBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("dbtypeIdBox");
					Textbox dbtypeNameBox =  (Textbox)ermResSubOpenWin.getParent().getFellowIfAny("dbtypeNameBox");
					dbtypeIdBox.setValue(tmpGeneralCode.getGeneralcodeId());
					dbtypeNameBox.setValue(tmpGeneralCode.getName1());
				}
				ermResSubOpenWin.detach();
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
			if(tmpMap.get("openValue")!=null){
				openValue=tmpMap.get("openValue").toString();
			}
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			search();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查詢語言集合出錯",e);
		}
	}
}
