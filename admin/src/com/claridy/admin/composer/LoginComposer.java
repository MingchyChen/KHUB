package com.claridy.admin.composer;


import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.DesEncryption;
import com.claridy.common.util.SystemProperties;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;
import com.claridy.facade.WebIndexInfoService;
import com.claridy.facade.WebLoginService;
import com.claridy.facade.WebSysLogService;

public class LoginComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3369391370563245215L;
	@Wire
	private Textbox textField;//用戶名
	@Wire
	private Textbox textField2;//密碼


	
	@Listen("onClick=#loginBtn")
	public void login() throws Exception{
		boolean ifSucess=false;
		String userName=textField.getValue();
		String passWord=textField2.getValue();
		userName= XSSStringEncoder.encodeXSSString(userName);
		passWord= XSSStringEncoder.encodeXSSString(passWord);
		userName=userName.toLowerCase();
		if(StringUtils.isBlank(userName)){
			ZkUtils.showExclamation(Labels.getLabel("useJudge"),Labels.getLabel("info"));
			textField.focus();
			return;
		}
		if(StringUtils.isBlank(passWord)){
			ZkUtils.showExclamation(Labels.getLabel("passJudge"),Labels.getLabel("info"));
			textField2.focus();
			return;
		}
		String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
		String jiami = DesEncryption.desEncrypt(passWord, deskey);
		WebEmployee webEmployee = ((WebLoginService) SpringUtil.getBean("webLoginService")).login(userName, jiami);
		if (webEmployee!=null) {
			
			ZkUtils.setSessionAttribute("webEmployee", webEmployee);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "login _"+webEmployee.getEmployeesn(), "登入後臺");
			WebIndexInfo footerinfo=((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).getFooterInfo();
			ZkUtils.setSessionAttribute("footerinfo", footerinfo.getNewsContentZhTw());

			ifSucess=true;
			ZkUtils.setSessionAttribute("loginOK", ifSucess);
			ZkUtils.sendRedirect("/home");

		} else {
			ZkUtils.showError(Labels.getLabel("withUserPass"), Labels.getLabel("error"));
		}
	}
	@Listen("onOK=#main")
	public void onOK$loginBtn() throws Exception{
		boolean ifSucess=false;
		String userName=textField.getValue();
		String passWord=textField2.getValue();
		userName= XSSStringEncoder.encodeXSSString(userName);
		passWord= XSSStringEncoder.encodeXSSString(passWord);
		
		if(StringUtils.isBlank(userName)){
			
			ZkUtils.showExclamation(Labels.getLabel("useJudge"),Labels.getLabel("info"));
			textField.focus();
			return;
		}
		if(StringUtils.isBlank(passWord)){
			ZkUtils.showExclamation(Labels.getLabel("passJudge"),Labels.getLabel("info"));
			textField2.focus();
			return;
		}
		String deskey=((SystemProperties)SpringUtil.getBean("systemProperties")).systemDeskey;
		String jiami = DesEncryption.desEncrypt(passWord, deskey);
		WebEmployee webEmployee = ((WebLoginService) SpringUtil.getBean("webLoginService")).login(userName, jiami);
		if (webEmployee!=null) {
			
			ZkUtils.setSessionAttribute("webEmployee", webEmployee);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "login _"+webEmployee.getEmployeesn(), "登入後臺");
			//獲取頁尾資訊
			WebIndexInfo footerinfo=((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).getFooterInfo();
			ZkUtils.setSessionAttribute("footerinfo", footerinfo.getNewsContentZhTw());
			
			ifSucess=true;
			ZkUtils.setSessionAttribute("loginOK", ifSucess);
			ZkUtils.sendRedirect("/home");

		} else {
			ZkUtils.showError(Labels.getLabel("withUserPass"), Labels.getLabel("error"));
		}
	}
}
