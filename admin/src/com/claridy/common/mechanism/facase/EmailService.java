package com.claridy.common.mechanism.facase;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.ISys_ParamDAO;
import com.claridy.common.util.SendMail;
import com.claridy.common.util.SystemProperties;
import com.claridy.dao.IWebOrgDAO;
import com.claridy.dao.hibernateimpl.WebOrgDAO;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.domain.WebOrg;
import com.claridy.domain.WebPhrase;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebNoticeTemplatesService;
import com.claridy.facade.WebOrgListService;
import com.claridy.facade.WebSysLogService;

/**
 * 
 * @author RemberSu
 * 
 */
@Service
public class EmailService {
	@Autowired
	private SystemProperties systemProperties;

	@Autowired
	private SendMail sendMail;

	@Autowired
	private SysParamService sysParamService;

	@Autowired
	private ISys_ParamDAO sys_ParamDAO;
	@Autowired
	private WebAccountService webAccountService;
	@Autowired
	private WebSysLogService webSysLogService;
	@Autowired
	private WebNoticeTemplatesService webNoticeTemplatesService;
	@Autowired
	private WebOrgListService weborgSerivce;

	@Autowired
	private IWebOrgDAO webOrgDAO;
	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 根據通知單範本給使用者發送通知單
	 * 
	 * @param noticeID
	 * @param webAccount
	 * @return true則成功,false則失敗
	 */
	public boolean sendNoticeToAccount(String noticeID, WebAccount webAccount,
			String url) {
		boolean retStatus = false;
		WebNoticeTemplates webnotices = webNoticeTemplatesService
				.getWebNoticeTempByUuid(noticeID);
		// sendMail.setFrom(loginwebEmployee.getEmail());
		sendMail.setMail_head(webnotices.getMailsubjectZhTw());
		String body = webnotices.getContentZhTw();
		body = body.replace("[name]", webAccount.getNameZhTw());
		body = body.replace("[email]", webAccount.getEmail());
		body = body.replace("[pwd]", webAccount.getPwd());
		if (url != null) {
			body = body.replace("[url]", url);
		}

		if (webAccount.getOrgid() != null
				&& !"".equals(webAccount.getOrgid())
				) {
			WebOrg webOrgtmp=webOrgDAO.getOrgById( webAccount.getOrgid());
			body = body.replace("[orgname]", webOrgtmp.getOrgName());
		} else {
			WebOrg webOrgtmp=webOrgDAO.getOrgById(webAccount.getParentorgid());
			body = body.replace("[orgname]",webOrgtmp.getOrgName());
		}

		sendMail.setMail_body(body);
		sendMail.setTo(webAccount.getEmail());
		try {
			sendMail.sendMail();
			retStatus = true;
			log.debug("根據通知單範本[" + noticeID + "]給使用者發送通知單成功");
		} catch (Exception e) {
			retStatus = false;
			log.error("根據通知單範本[" + noticeID + "]給使用者發送通知單拋出異常", e);
		}

		return retStatus;

	}

	public boolean sendNoticeToWebCooperationIsOk(String noticeID,
			WebEmployee webEmployeeLogin, WebEmployee MangerWebEmployee,
			WebCooperation webCooperation, String url, String fileName,
			int isManger) {
		boolean retStatus = false;
		WebNoticeTemplates webnotices = webNoticeTemplatesService
				.getWebNoticeTempByUuid(noticeID);
		// sendMail.setFrom(loginwebEmployee.getEmail());
		sendMail.setMail_head(webnotices.getMailsubjectZhTw());
		String body = webnotices.getContentZhTw();
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String checkdate = "";
		if (webCooperation.getLatelyChangedDate() != null
				&& !"".equals(webCooperation.getLatelyChangedDate())) {
			checkdate = sformat.format(webCooperation.getLatelyChangedDate());

		} else {
			checkdate = sformat.format(new Date());
		}
		body = body.replace("[checkdate]", checkdate);
		body = body.replace("[emporgname]", webCooperation.getAcceptEmployee()
				.getParentWebOrg().getOrgName());
		String applydate = "";
		if (webCooperation.getCreateDate() != null
				&& !"".equals(webCooperation.getCreateDate())) {
			applydate = sformat.format(webCooperation.getCreateDate());
		}
		body = body.replace("[applydate]", applydate);
		body = body.replace("[accname]", webCooperation.getApplyAccount()
				.getNameZhTw());
		WebOrg webOrgtmp=webOrgDAO.getOrgById(webCooperation.getApplyAccount().getParentorgid());
		body = body.replace("[accorgname]", webOrgtmp.getOrgName());
		body = body.replace("[empname]", webCooperation.getAcceptEmployee()
				.getEmployeeName());
		body = body.replace("[journal]", webCooperation.getTitleZhTw());
		body = body.replace("[title]", webCooperation.getAtitle());
		body = body.replace("[author]", webCooperation.getPid());
		body = body.replace("[issn]", webCooperation.getIssn());
		body = body.replace("[volume]", webCooperation.getVolume());
		body = body.replace("[issue]", webCooperation.getIssue());
		body = body.replace("[pagestart]", webCooperation.getSpage());
		body = body.replace("[pageend]", webCooperation.getEpage());
		if (webCooperation.getDoi() != null
				&& !webCooperation.getDoi().equals("")) {
			body = body.replace("[doi]", webCooperation.getDoi());
		} else {
			body = body.replace("[doi]", "");
		}
		String publishDate = "";
		if (webCooperation.getPublishDate() != null
				&& !"".equals(webCooperation.getPublishDate())) {
			publishDate = sformat.format(webCooperation.getPublishDate());
		}
		body = body.replace("[publisherdate]", publishDate);

		if (isManger == 1) {
			body = body.replace("[applyorgmanage]",
					MangerWebEmployee.getEmployeeName());
			body = body.replace("[empname]", webCooperation.getAcceptEmployee()
					.getEmployeeName());
			url = "<a href=\"" + systemProperties.agridlURL
					+ "/download?path=cooperation&filename=" + fileName + "\">"
					+ webCooperation.getDisplayName() + "</a>";
			if (url != null) {
				body = body.replace("[url]", url);
				sendMail.setMail_body(body + url);
			}
		} else {
			sendMail.setMail_body(body);
		}

		sendMail.setFileURL(url);
		if (isManger == 1) {
			sendMail.setTo(MangerWebEmployee.getEmail());
		} else if (isManger == 0) {
			sendMail.setTo(webCooperation.getApplyAccount().getEmail());
		} else if (isManger == 2) {//檢視
			sendMail.setMail_head(webnotices.getMailsubjectZhTw()+"(檢視用)");
			sendMail.setTo(webEmployeeLogin.getEmail());
		}
		try {
			sendMail.sendMail();
			retStatus = true;
			log.debug("根據通知單範本[" + noticeID + "]給使用者發送通知單成功");
		} catch (Exception e) {
			retStatus = false;
			log.error("根據通知單範本[" + noticeID + "]給使用者發送通知單拋出異常", e);
		}

		return retStatus;
	}

	public boolean sendNoticeToWebCooperationIsNO(String noticeID,
			WebPhrase webPhrase, WebEmployee webEmployeeLogin,
			WebEmployee MangerWebEmployee, WebCooperation webCooperation,
			int isManger) {
		boolean retStatus = false;
		WebNoticeTemplates webnotices = webNoticeTemplatesService
				.getWebNoticeTempByUuid(noticeID);
		// sendMail.setFrom(loginwebEmployee.getEmail());
		sendMail.setMail_head(webnotices.getMailsubjectZhTw());
		String body = webnotices.getContentZhTw();
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String checkdate = "";
		if (webCooperation.getLatelyChangedDate() != null
				&& !"".equals(webCooperation.getLatelyChangedDate())) {
			checkdate = sformat.format(webCooperation.getLatelyChangedDate());

		} else {
			checkdate = sformat.format(new Date());
		}
		body = body.replace("[checkdate]", checkdate);
		body = body.replace("[emporgname]", webCooperation.getAcceptEmployee()
				.getParentWebOrg().getOrgName());
		body = body.replace("[applydate]", webCooperation.getCreateDate()
				.toString());
		body = body.replace("[accname]", webCooperation.getApplyAccount()
				.getNameZhTw());

		WebOrg webOrgtmp=webOrgDAO.getOrgById(webCooperation.getApplyAccount().getParentorgid());
		body = body.replace("[accorgname]",webOrgtmp.getOrgName());
		body = body.replace("[journal]", webCooperation.getTitleZhTw());
		body = body.replace("[title]", webCooperation.getAtitle());
		body = body.replace("[author]", webCooperation.getPid());
		body = body.replace("[issn]", webCooperation.getIssn());
		body = body.replace("[volume]", webCooperation.getVolume());
		body = body.replace("[issue]", webCooperation.getIssue());
		body = body.replace("[pagestart]", webCooperation.getSpage());
		body = body.replace("[pageend]", webCooperation.getEpage());
		body = body.replace("[doi]", webCooperation.getDoi());
		body = body.replace("[empname]", webCooperation.getAcceptEmployee()
				.getEmployeeName());
		if (MangerWebEmployee != null) {
			body = body.replace("[applyorgmanage]",
					MangerWebEmployee.getEmployeeName());

		} else {
			body = body.replace("[applyorgmanage]", "");
		}
		String publishDate = "";
		if (webCooperation.getPublishDate() != null
				&& !"".equals(webCooperation.getPublishDate())) {
			publishDate = sformat.format(webCooperation.getPublishDate());
		}
		body = body.replace("[publisherdate]", publishDate);
		if (webPhrase != null) {
			body = body.replace("[rejectreason]", webPhrase.getPhraseZhTw());
		} else {
			body = body.replace("[rejectreason]", "");
		}

		sendMail.setMail_body(body);
		if (isManger == 1) {

			sendMail.setTo(MangerWebEmployee.getEmail());
		} else if (isManger == 0) {
			sendMail.setTo(webCooperation.getApplyAccount().getEmail());
		} else if (isManger == 2) {
			sendMail.setMail_head(webnotices.getMailsubjectZhTw()+"(檢視用)");
			sendMail.setTo(webEmployeeLogin.getEmail());
		}
		try {
			sendMail.sendMail();
			retStatus = true;
			log.debug("根據通知單範本[" + noticeID + "]給使用者發送通知單成功");
		} catch (Exception e) {
			retStatus = false;
			log.error("根據通知單範本[" + noticeID + "]給使用者發送通知單拋出異常", e);
		}

		return retStatus;
	}

}