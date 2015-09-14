package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeGeneralCodeService;
import com.claridy.facade.ErmCodeItemService;
import com.claridy.facade.WebSysLogService;


/**
 * 共用代碼類別 lixiangfan
 * @author nj
 *
 */
public class GeneralCodeListComposer extends SelectorComposer<Component>{
	
	private static final long serialVersionUID = 8994020132060546141L;
	
	@Wire
	protected Listbox generalCodeLbx;
	@Wire
	private Combobox itemIdBox;	
	@Wire
	private Textbox generalcodeIdBox;	
	@Wire
	private Textbox name1Box;	
	@Wire
	private Textbox name2Box;	
	@Wire
	private Radiogroup yesOrNoRdp;
	@Wire
	private List<ErmCodeGeneralCode> ErmCodeGeneralCodeList;
	@Wire
	private List<ErmCodeItem> ErmCodeItemList;
	@Wire
	private ErmCodeGeneralCode ErmCodeGeneralCode;
	@Wire Window addgeneralCodeWin;
	
	private WebEmployee webEmployee;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		String itemId = itemIdBox.getSelectedItem().getValue();
		String generalcodeId = generalcodeIdBox.getValue();
		String name1 = name1Box.getValue();
		String name2 = name2Box.getValue();
		String yesOrNo = null;
		if(yesOrNoRdp.getSelectedItem()!=null){
			yesOrNo=yesOrNoRdp.getSelectedItem().getValue();
		}
		itemId= XSSStringEncoder.encodeXSSString(itemId);
		generalcodeId= XSSStringEncoder.encodeXSSString(generalcodeId);
		name1= XSSStringEncoder.encodeXSSString(name1);
		name2= XSSStringEncoder.encodeXSSString(name2);
		yesOrNo= XSSStringEncoder.encodeXSSString(yesOrNo);
		
		if (StringUtils.isBlank(itemId)&&StringUtils.isBlank(generalcodeId)&&StringUtils.isBlank(name1)&&StringUtils.isBlank(name2)&&StringUtils.isBlank(yesOrNo)) {
			ZkUtils.showExclamation(
					Labels.getLabel("inputString"),
					Labels.getLabel("warn"));
			itemIdBox.focus();
			return;
		}
		
		List<ErmCodeGeneralCode> result = ((ErmCodeGeneralCodeService) SpringUtil
				.getBean("ermCodeGeneralCodeService")).search(itemId,generalcodeId,name1,name2,yesOrNo,webEmployee);
		ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(
				result);
		tmpLML.setMultiple(true);
		generalCodeLbx.setModel(tmpLML);
	}
	
	
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		itemIdBox.setSelectedIndex(0);	
		generalcodeIdBox.setValue("");
		name1Box.setValue("");
		name2Box.setValue("");
		yesOrNoRdp.setSelectedItem(null);
		List<ErmCodeGeneralCode> result = ((ErmCodeGeneralCodeService) SpringUtil
				.getBean("ermCodeGeneralCodeService")).findAll(webEmployee);
		ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(
				result);
		tmpLML.setMultiple(true);
		generalCodeLbx.setModel(tmpLML);
	}
	
	@Listen("onClick = #addBtn")
	public void add() {
		addgeneralCodeWin = (Window) ZkUtils.createComponents(
				"/WEB-INF/pages/system/generalCode/generalCodeAdd.zul", null,
				null);
		addgeneralCodeWin.doModal();
	}
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		int size = generalCodeLbx.getSelectedCount();
		if (size > 0) {
			//"您確定要刪除該筆資料嗎？"
			ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
				public void onEvent(Event event) throws Exception {
					int clickedButton = (Integer) event.getData();
					if (clickedButton == Messagebox.OK) {
						Set<Listitem> selectedModels = generalCodeLbx
								.getSelectedItems();
						for (Listitem tmpEST : selectedModels) {
							ErmCodeGeneralCode = tmpEST.getValue();
							((ErmCodeGeneralCodeService) SpringUtil.getBean("ermCodeGeneralCodeService")).delete(ErmCodeGeneralCode.getErmCodeItem().getItemId(),ErmCodeGeneralCode.getGeneralcodeId());
							((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "generalcode_"
									+ ErmCodeGeneralCode.getErmCodeItem().getItemId()+ErmCodeGeneralCode.getGeneralcodeId());
										
						}
						Desktop dkp = Executions.getCurrent().getDesktop();
						Page page = dkp.getPageIfAny("templatePage");
						Include contentInclude = (Include) page.getFellowIfAny("contentInclude");
						contentInclude.setSrc("home.zul");
						contentInclude.setSrc("generalCode/generalCode.zul");
					} else {
						// 取消刪除
						return;
					}
				}
			});
		} else {
			//"請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"), Labels.getLabel("info"));
			return;
		}
	}
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp); 
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ErmCodeItemList=((ErmCodeItemService)SpringUtil.getBean("ermCodeItemService")).findByhistory(webEmployee);
			Comboitem com=new Comboitem();
			com.setLabel(Labels.getLabel("select"));
			com.setValue("");
			itemIdBox.appendChild(com);
			for(int i=0;i<ErmCodeItemList.size();i++){
				ErmCodeItem ErmCodeItem=new ErmCodeItem();
				ErmCodeItem=ErmCodeItemList.get(i);
				com=new Comboitem();
				com.setLabel(ErmCodeItem.getName1());
				com.setValue(ErmCodeItem.getItemId());
				itemIdBox.appendChild(com);
			}
			itemIdBox.setSelectedIndex(0);	
			//初始頁面加載
			ErmCodeGeneralCodeList = ((ErmCodeGeneralCodeService) SpringUtil
					.getBean("ermCodeGeneralCodeService")).findAll(webEmployee);
			ListModelList<ErmCodeGeneralCode> tmpLML = new ListModelList<ErmCodeGeneralCode>(
					ErmCodeGeneralCodeList);
			tmpLML.setMultiple(true);
			generalCodeLbx.setModel(tmpLML);
		}catch (Exception e) {
			log.error("初始化generalCode異常"+e);
	}
  }
}
