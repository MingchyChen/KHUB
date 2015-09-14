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
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
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
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodeItemService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author lwchen
 * 共用代碼類別
 * 2014/07/25
 */
public class ErmCodeItemListComposer extends SelectorComposer<Component> {
	
	@Wire
	private Textbox codeBox;
	@Wire
	private Textbox typeNameBox;
	@Wire
	private Textbox typeNameUSBox;
	@Wire
	private Radiogroup historyRdp;
	@Wire
	protected Listbox codeLix;
	@Wire
	private Window addErmCodeItemWin;
	@WireVariable
	private List<ErmCodeItem> ermCodeItemList;
	@WireVariable
	private ErmCodeItem ermCodeItem;
	private WebEmployee webEmployee;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1611595985756506276L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			ErmCodeItem ermCodeItem = new ErmCodeItem();
			String code = codeBox.getValue();
			code = XSSStringEncoder.encodeXSSString(code);
			String typeName = typeNameBox.getValue();
			typeName = XSSStringEncoder.encodeXSSString(typeName);
			String typeNameUS = typeNameUSBox.getValue();
			typeNameUS = XSSStringEncoder.encodeXSSString(typeNameUS);
			String history = (historyRdp.getSelectedItem()==null?null:historyRdp.getSelectedItem().getValue().toString());
			history = XSSStringEncoder.encodeXSSString(history);
			
			if (StringUtils.isBlank(code)&&StringUtils.isBlank(typeName)&&StringUtils.isBlank(typeNameUS)&&StringUtils.isBlank(history)) {
				ZkUtils.showExclamation(
						Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				codeBox.focus();
				return;
			}
			
			ermCodeItem.setItemId(code);
			ermCodeItem.setName1(typeName);
			ermCodeItem.setName2(typeNameUS);
			ermCodeItem.setHistory(history);
			
			List<ErmCodeItem> result = ((ErmCodeItemService) SpringUtil
					.getBean("ermCodeItemService")).findErmCodeItem(ermCodeItem,webEmployee);
			ListModelList<ErmCodeItem> tmpLML = new ListModelList<ErmCodeItem>(
					result);
			tmpLML.setMultiple(true);
			codeLix.setModel(tmpLML);
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別加載編輯頁面報錯",e);
		}
	}
	
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		try {
			codeBox.setValue("");
			typeNameBox.setValue("");
			typeNameUSBox.setValue("");
			historyRdp.setSelectedIndex(-1);
			ErmCodeItem eci = new ErmCodeItem();
			List<ErmCodeItem> result = ((ErmCodeItemService) SpringUtil
					.getBean("ermCodeItemService")).findErmCodeItem(eci,webEmployee);
			ListModelList<ErmCodeItem> tmpLML = new ListModelList<ErmCodeItem>(
					result);
			tmpLML.setMultiple(true);
			codeLix.setModel(tmpLML);
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別顯示全部報錯",e);
		}
	}
	
	@Listen("onClick = #addBtn")
	public void add() {
		try {
			addErmCodeItemWin = (Window) ZkUtils.createComponents(
					"/WEB-INF/pages/system/codeType/codeTypeAdd.zul", this.getSelf(),
					null);
			addErmCodeItemWin.doModal();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別新增跳轉報錯",e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick = #deleteBtn")
	public void delete() {
		try {
			int size = codeLix.getSelectedCount();
			if (size > 0) {
				//"您確定要刪除該筆資料嗎？"
				ZkUtils.showQuestion( Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
					public void onEvent(Event event) throws Exception {
						int clickedButton = (Integer) event.getData();
						if (clickedButton == Messagebox.OK) {
							Set<Listitem> selectedModels = codeLix
									.getSelectedItems();
							for (Listitem tmpEST : selectedModels) {
								ermCodeItem = tmpEST.getValue();
								((ErmCodeItemService) SpringUtil.getBean("ermCodeItemService")).delete(ermCodeItem.getItemId());
								((WebSysLogService) SpringUtil.getBean("webSysLogService")).delLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "ermCodeItem_"
												+ ermCodeItem.getItemId());
							}
							Desktop dkp = Executions.getCurrent().getDesktop();
							Page page = dkp.getPageIfAny("templatePage");
							Include contentInclude = (Include) page.getFellowIfAny("contentInclude");
							contentInclude.setSrc("home.zul");
							contentInclude.setSrc("codeType/codeType.zul");
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
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別刪除報錯",e);
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			//初始頁面加載
			ErmCodeItem e = new ErmCodeItem();
			ermCodeItemList = ((ErmCodeItemService) SpringUtil
					.getBean("ermCodeItemService")).findErmCodeItem(e, webEmployee);
			ListModelList<ErmCodeItem> tmpLML = new ListModelList<ErmCodeItem>(
					ermCodeItemList);
			tmpLML.setMultiple(true);
			codeLix.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別加載報錯",e);
		}
	}
	
	

}
