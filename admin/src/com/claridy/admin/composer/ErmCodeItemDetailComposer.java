package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
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
 * 
 * @author lwchen
 * 共用代碼類別
 * 2014/07/25
 */
public class ErmCodeItemDetailComposer extends SelectorComposer<Component> {
	
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 8591720837169072811L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox itemIdBox;
	@Wire
	private Textbox generalcode_IdValue;
	@Wire
	private Textbox name1Value;
	@Wire
	private Textbox name2Value;
	@Wire
	private Radiogroup yesOrNoRdp;
	@Wire
	private Textbox noteValue;	
	@Wire
	private Window addGeneralCodeWin;
	private WebEmployee webEmployee;
	private String item_id;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			tmpMap = ZkUtils.getExecutionArgs();
			ErmCodeGeneralCode tempErmCodeGeneralCode =  ((ErmCodeGeneralCodeService) SpringUtil
						.getBean("ermCodeGeneralCodeService")).findByItemIDAndGeneralcodeId((String) tmpMap
					.get("itemId"),(String)tmpMap.get("generalcodeId"));
			ErmCodeItem tempErmCodeItem = ((ErmCodeItemService) SpringUtil
					.getBean("ermCodeItemService")).findByItemId((String) tmpMap.get("item_id"));
			
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			
			if(tempErmCodeGeneralCode.getGeneralcodeId()!=null&&!"".equals(tempErmCodeGeneralCode)){
				item_id = tempErmCodeGeneralCode.getErmCodeItem().getItemId();
				itemIdBox.setValue(tempErmCodeGeneralCode.getErmCodeItem().getName1());
				generalcode_IdValue.setValue(tempErmCodeGeneralCode.getGeneralcodeId());
				name1Value.setValue(tempErmCodeGeneralCode.getName1());
				name2Value.setValue(tempErmCodeGeneralCode.getName2());
				if("N".equals(tempErmCodeGeneralCode.getHistory())){
					yesOrNoRdp.setSelectedIndex(0);
				}else{
					yesOrNoRdp.setSelectedIndex(1);
				}
				noteValue.setValue(tempErmCodeGeneralCode.getNote());
			} else if(tempErmCodeItem!=null&&tempErmCodeItem.getItemId()!=null) {
				itemIdBox.setValue(tempErmCodeItem.getName1());
				item_id = tempErmCodeItem.getItemId();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("共用代碼類別加載明細報錯",e);
		}
	}

	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String itemId = item_id;
			String generalcode_Id = generalcode_IdValue.getValue();
			String name1 = name1Value.getValue();
			String name2 = name2Value.getValue();
			String yesOrNo = (yesOrNoRdp.getSelectedItem() == null ? null
							: yesOrNoRdp.getSelectedItem().getValue().toString());
			String note = noteValue.getValue();
			itemId = XSSStringEncoder.encodeXSSString(itemId);
			generalcode_Id = XSSStringEncoder.encodeXSSString(generalcode_Id);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			yesOrNo = XSSStringEncoder.encodeXSSString(yesOrNo);
			note = XSSStringEncoder.encodeXSSString(note);
			if (StringUtils.isBlank(itemId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.itemid") + " "
						+ Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			} else if (StringUtils.isBlank(generalcode_Id)){
				ZkUtils.showExclamation(Labels.getLabel("generalcode.generalcode_id")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				generalcode_IdValue.focus();
				return;
			}
			else if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name1")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				name1Value.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name2")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				name2Value.focus();
				return;
			}else if (StringUtils.isBlank(yesOrNo)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.history")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				return;
			}
			boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil
					.getBean("ermCodeGeneralCodeService")).save(itemId,generalcode_Id, name1, name2,
							yesOrNo, note,webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService"))
						.insertLog(ZkUtils.getRemoteAddr(),
								webEmployee.getEmployeesn(), "generalcode_"
										+ itemId+generalcode_Id);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"),
						Labels.getLabel("info"));
				Map<String,String> map=new HashMap<String,String>();
				map.put("itemId",  itemId);
				if(addGeneralCodeWin != null){
					if(addGeneralCodeWin.getParent() != null)
						addGeneralCodeWin.getParent().detach();
				}
				Window newAdd=(Window)com.claridy.common.util.ZkUtils.createComponents("/WEB-INF/pages/system/codeType/codeTypeEdit.zul", null,
						map);
				newAdd.doModal();
				addGeneralCodeWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(
						Labels.getLabel("generalcode.itemid")+Labels.getLabel("generalcode.generalcode_id")
								+ Labels.getLabel("isExist"),
						Labels.getLabel("error"));
				clearInput();
			    return;
				
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別明細新增報錯",e);
		}
	}
	
	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		itemIdBox.setText(StringUtils.EMPTY);
		generalcode_IdValue.setText(StringUtils.EMPTY);
	}
	
	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String itemId = item_id;
			String generalcode_Id = generalcode_IdValue.getValue();
			String name1 = name1Value.getValue();
			String name2 = name2Value.getValue();
			String yesOrNo = (yesOrNoRdp.getSelectedItem() == null ? null
							: yesOrNoRdp.getSelectedItem().getValue().toString());
			String note = noteValue.getValue();
			itemId = XSSStringEncoder.encodeXSSString(itemId);
			generalcode_Id = XSSStringEncoder.encodeXSSString(generalcode_Id);
			name1 = XSSStringEncoder.encodeXSSString(name1);
			name2 = XSSStringEncoder.encodeXSSString(name2);
			yesOrNo = XSSStringEncoder.encodeXSSString(yesOrNo);
			note = XSSStringEncoder.encodeXSSString(note);
			if (StringUtils.isBlank(name1)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name1")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				name1Value.focus();
				return;
			} else if (StringUtils.isBlank(name2)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.name2")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				name2Value.focus();
				return;
			}else if (StringUtils.isBlank(yesOrNo)) {
				// 參數名稱不能為空
				ZkUtils.showExclamation(Labels.getLabel("generalcode.history")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				return;
			}
			boolean saveStatus = ((ErmCodeGeneralCodeService) SpringUtil
					.getBean("ermCodeGeneralCodeService")).update(itemId,generalcode_Id, name1, name2,
							yesOrNo, note,webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService"))
				.editLog(ZkUtils.getRemoteAddr(),
						webEmployee.getEmployeesn(), "generalcode_"
								+ itemId+generalcode_Id);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"),
						Labels.getLabel("info"));
				Map<String,String> map=new HashMap<String,String>();
				map.put("itemId",  itemId);
				if(addGeneralCodeWin != null){
					if(addGeneralCodeWin.getParent() != null)
						addGeneralCodeWin.getParent().detach();
				}
				Window newAdd=(Window)com.claridy.common.util.ZkUtils.createComponents("/WEB-INF/pages/system/codeType/codeTypeEdit.zul", null,
						map);
				newAdd.doModal();
				addGeneralCodeWin.detach();
			}
		} catch (WrongValueException e) {
			// TODO: handle exception
			log.error("共用代碼類別明細修改報錯",e);
		}
	}
}
