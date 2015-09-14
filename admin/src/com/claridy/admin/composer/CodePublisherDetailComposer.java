package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodePublisherPerson;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCodePublisherpersonService;
import com.claridy.facade.WebSysLogService;
/**
 * 
 * engin
 * 代理商出版商資料明細Add,Edit
 * 2014/07/28
 *
 */
public class CodePublisherDetailComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1379084135571132414L;

	@Wire
	private Textbox tboxPublisherId;
	@Wire
	private Textbox tboxPersonname;
	@Wire
	private Textbox tboxEmail;
	@Wire
	private Textbox tboxTelephone;
	@Wire
	private Textbox tboxFax;
	@Wire
	private Textbox tboxTitle;
	@Wire
	private Window addErmCodePublisherpersonWin;
	@Wire
	private Window editErmCodePublisherpersonWin;
	private WebEmployee webEmployee;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// 獲取用戶資訊
		webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		tmpMap = ZkUtils.getExecutionArgs();
		String publisherId = (String) tmpMap.get("publisherId");
		String personName = (String) tmpMap.get("personName");
		
		ErmCodePublisherPerson tempErmCodePublisherPerson = ((ErmCodePublisherpersonService) SpringUtil
				.getBean("ermCodePublisherpersonService"))
				.findErmCodePublisherPersonById(publisherId,personName, webEmployee);
		tboxPublisherId.setValue(publisherId);		
		if (tempErmCodePublisherPerson!=null) {
			tboxPersonname.setValue(tempErmCodePublisherPerson.getPersonName());
			tboxEmail.setValue(tempErmCodePublisherPerson.getMail());
			tboxTelephone.setValue(tempErmCodePublisherPerson.getTelephone());
			tboxFax.setValue(tempErmCodePublisherPerson.getFax());
			tboxTitle.setValue(tempErmCodePublisherPerson.getTitle());
		}
	}
	
	@Listen("onClick = #saveBtn")
	public void save() {
		try {
			String publisherId = tboxPublisherId.getValue();
			String personName = tboxPersonname.getValue();
			String email = tboxEmail.getValue();
			if(!StringUtils.isBlank(email)) {
				if(!ZkUtils.isEmail(email)){
					ZkUtils.showExclamation(
							Labels.getLabel("emailError"),
							Labels.getLabel("warn"));
					tboxEmail.focus();
					return;
				}
			}
			String telephone = tboxTelephone.getValue();
			String fax = tboxFax.getValue();
			String title = tboxTitle.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			personName = XSSStringEncoder.encodeXSSString(personName);
			email = XSSStringEncoder.encodeXSSString(email);
			telephone = XSSStringEncoder.encodeXSSString(telephone);
			fax = XSSStringEncoder.encodeXSSString(fax);
			title = XSSStringEncoder.encodeXSSString(title);

			if (StringUtils.isBlank(publisherId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.publisherId") + " "
						+ Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(personName)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.personname")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPersonname.focus();
				return;
			}
			boolean saveStatus = ((ErmCodePublisherpersonService) SpringUtil
					.getBean("ermCodePublisherpersonService")).save(publisherId, personName, email, telephone, fax, title, webEmployee);
			if (saveStatus) {
				((WebSysLogService) SpringUtil.getBean("webSysLogService"))
						.insertLog(ZkUtils.getRemoteAddr(),
								webEmployee.getEmployeesn(), "ermCodePublisherperson_"
										+ publisherId +"_"+ personName);
				// 存儲成功
				ZkUtils.showInformation(Labels.getLabel("saveOK"),
						Labels.getLabel("info"));
				Map<String,String> params=new HashMap<String,String>();
				params.put("publisherId",  publisherId);
				if(addErmCodePublisherpersonWin != null){
					if(addErmCodePublisherpersonWin.getParent() != null)
						addErmCodePublisherpersonWin.getParent().detach();
				}
				Window newWindow = (Window) com.claridy.common.util.ZkUtils.createComponents(
						"/WEB-INF/pages/system/codePublisher/codePublisherEdit.zul", null,
						params);
				newWindow.doModal();
				addErmCodePublisherpersonWin.detach();
			} else {
				// "功能號已存在"
				ZkUtils.showError(
						Labels.getLabel("ermCodeItem.code")
								+ Labels.getLabel("isExist"),
						Labels.getLabel("error"));
				clearInput();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("存儲代理商出版商資料明細出錯：",e);
		}
	}
	
	/**
	 * 清空用戶輸入
	 */
	private void clearInput() {
		tboxPersonname.setText(StringUtils.EMPTY);
	}
	
	@Listen("onClick = #updateBtn")
	public void update() {
		try {
			String publisherId = tboxPublisherId.getValue();
			String personName = tboxPersonname.getValue();
			String email = tboxEmail.getValue();
			if(!StringUtils.isBlank(email)) {
				if(!ZkUtils.isEmail(email)){
					ZkUtils.showExclamation(
							Labels.getLabel("emailError"),
							Labels.getLabel("warn"));
					tboxEmail.focus();
					return;
				}
			}
			String telephone = tboxTelephone.getValue();
			String fax = tboxFax.getValue();
			String title = tboxTitle.getValue();
			publisherId = XSSStringEncoder.encodeXSSString(publisherId);
			personName = XSSStringEncoder.encodeXSSString(personName);
			email = XSSStringEncoder.encodeXSSString(email);
			telephone = XSSStringEncoder.encodeXSSString(telephone);
			fax = XSSStringEncoder.encodeXSSString(fax);
			title = XSSStringEncoder.encodeXSSString(title);

			if (StringUtils.isBlank(publisherId)) {
				// 功能不能為空
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.publisherId") + " "
						+ Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				tboxPublisherId.focus();
				return;
			} else if (StringUtils.isBlank(personName)) {
				// 參數值不能為空！
				ZkUtils.showExclamation(Labels.getLabel("ermCodePublisherperson.personname")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				tboxPersonname.focus();
				return;
			}
			boolean saveStatus = ((ErmCodePublisherpersonService) SpringUtil
					.getBean("ermCodePublisherpersonService")).update(publisherId, personName, email, telephone, fax, title, 1, webEmployee);
			
			if (saveStatus) {

				((WebSysLogService) SpringUtil.getBean("webSysLogService"))
				.insertLog(ZkUtils.getRemoteAddr(),
						webEmployee.getEmployeesn(), "ermCodePublisherperson_"
								+ publisherId +"_" + personName);
				// 更新成功
				ZkUtils.showInformation(Labels.getLabel("updateOK"),
						Labels.getLabel("info"));
				Map<String,String> params=new HashMap<String,String>();
				params.put("publisherId",  publisherId);
				if(editErmCodePublisherpersonWin != null){
					if(editErmCodePublisherpersonWin.getParent() != null)
						editErmCodePublisherpersonWin.getParent().detach();
				}
				Window newWindow = (Window) com.claridy.common.util.ZkUtils.createComponents(
						"/WEB-INF/pages/system/codePublisher/codePublisherEdit.zul", null,
						params);
				newWindow.doModal();
				editErmCodePublisherpersonWin.detach();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("更新代理商出版商資料明細出錯:",e);
		}
	}
}
