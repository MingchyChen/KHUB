/*
 * Created on 2005/10/4
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.claridy.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.claridy.common.mechanism.domain.Sys_File;

@Component
public class SendMail implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	public SystemProperties systemProperties = new SystemProperties();
	private final Logger log = LoggerFactory.getLogger(getClass());

	private String from = null; // 寄件者
	private String to = null; // 收件者，多收件人請以逗號隔開
	private String cc = null; // cc收件者，多cc收件人請以逗號隔開
	private String bcc = null; // bcc收件者，多bcc收件人請以逗號隔開
	private String mail_head = null;// 信件標題
	private String mail_body = null;// 信件主體，可以是純文字或是一個html網頁
	private String fileURL = null;// 附加附件
	private Set<Sys_File> sys_Files = null;// 附加檔案

	/**
	 * @return Returns the obj_from.
	 */
	public String getFrom() {
		return from;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	/**
	 * @param obj_from
	 *            The obj_from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return Returns the mail_body.
	 */
	public String getMail_body() {
		return mail_body;
	}

	/**
	 * @param mail_body
	 *            The mail_body to set.
	 */
	public void setMail_body(String mail_body) {
		this.mail_body = mail_body;
	}

	/**
	 * @return Returns the mail_head.
	 */
	public String getMail_head() {
		return mail_head;
	}

	/**
	 * @param mail_head
	 *            The mail_head to set.
	 */
	public void setMail_head(String mail_head) {
		this.mail_head = mail_head;
	}

	/**
	 * @return Returns the obj_to.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param obj_to
	 *            The obj_to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public Set<Sys_File> getSys_Files() {
		return sys_Files;
	}

	public void setSys_Files(Set<Sys_File> sys_Files) {
		this.sys_Files = sys_Files;
	}

	public String sendMail() throws Exception {
		// System.out.println("开始发邮件喽");
		log.debug("開始發郵件...");
		String returnValue = "SendMailFailded";
		String mailserver = null;
		String[] address = null;
		java.util.Properties pro = null;
		javax.mail.Session mailSession = null;
		javax.mail.Message mesg = null;
		InternetAddress internetAddress = null;
		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart context = null;
		FileDataSource fileAttach = null;
		Transport transport = null;
		Set<Sys_File> sys_Files = null;
		try {
			mailserver = systemProperties.mail_server;
			pro = System.getProperties();
			/** Define MailServer **/
			pro.put("mail.smtp.host", mailserver);
			pro.put("mail.transport.protocol", "smtp");
			pro.put("mail.smtp.auth", systemProperties.mail_auth);
			mailSession = javax.mail.Session.getDefaultInstance(pro, null);
			mailSession.setDebug(false);
			mesg = new MimeMessage(mailSession);
			/** 設定寄件人 **/
			try {
				if (systemProperties.mail_from_addr_name != null
						&& !"".equals(systemProperties.mail_from_addr_name)) {
					String addrName = toUtf8(systemProperties.mail_from_addr_name);
					mesg.setFrom(new InternetAddress(
							systemProperties.mail_from_addr, addrName));

				} else {
					if (this.getFrom() != null && !"".equals(this.getFrom())) {
						mesg.setFrom(new InternetAddress(this.getFrom()));// 改為只設定寄件人地址
					} else {
						mesg.setFrom(new InternetAddress(
								systemProperties.mail_from_addr));// 改為只設定寄件人地址
					}
				}
			} catch (UnsupportedEncodingException e)// 若發生無法支援編碼的狀況
			{
				if (this.getFrom() != null && !"".equals(this.getFrom())) {
					mesg.setFrom(new InternetAddress(this.getFrom()));// 改為只設定寄件人地址
				} else {
					mesg.setFrom(new InternetAddress(
							systemProperties.mail_from_addr));// 改為只設定寄件人地址
				}
			}
			/** 設定收信人信箱 **/
			if (this.getTo() != null && this.getTo().indexOf(",") != -1) {// 多收件人，分隔符號號','
				address = this.getTo().split(",");
			}
			if (address == null) {// 單一收件人
				address = new String[1];
				address[0] = this.getTo();
			}
			for (int i = 0; i < address.length; i++) {
				try {
					internetAddress = new InternetAddress(address[i]);
					mesg.addRecipient(javax.mail.Message.RecipientType.TO,
							internetAddress);
				} catch (NullPointerException n) {
					log.error("設定收信人信箱NullPointerException", n);
				} catch (Exception e) {
					log.error("設定收信人信箱異常", e);
				}
			}
			/** 設定cc收信人信箱 **/
			address = null;
			if (this.getCc() != null) {
				if (this.getCc().indexOf(",") != -1) {// 多cc收件人，分隔符號號','
					address = this.getCc().split(",");
				} else {// 單一cc收件人
					address = new String[1];
					address[0] = this.getCc();
				}
				for (int i = 0; i < address.length; i++) {
					try {
						internetAddress = new InternetAddress(address[i]);
						mesg.addRecipient(javax.mail.Message.RecipientType.CC,
								internetAddress);
					} catch (Exception e) {
						log.error("設定CC 異常", e);
					}
				}
			}
			/** 設定bcc收信人信箱 **/
			address = null;
			if (this.getBcc() != null) {
				if (this.getBcc().indexOf(",") != -1) {// 多bcc收件人，分隔符號號','
					address = this.getBcc().split(",");
				} else {// 單一bcc收件人
					address = new String[1];
					address[0] = this.getBcc();
				}
				for (int i = 0; i < address.length; i++) {
					try {
						internetAddress = new InternetAddress(address[i]);
						mesg.addRecipient(javax.mail.Message.RecipientType.BCC,
								internetAddress);
					} catch (Exception e) {
						log.error("設定BCC 異常", e);
					}
				}
			}
			/** 設定郵件主題 **/
			try {
				mesg.setHeader("Subject", MimeUtility.encodeText(
						this.getMail_head(), "UTF-8", null));
			} catch (UnsupportedEncodingException e) {
				log.error("設定郵件主題 異常", e);
			}
			/** 設定發信時間 **/
			mesg.setSentDate(new java.util.Date());
			/** 設定發送編碼格式和內容 **/
			context = new MimeBodyPart();
			context.setContent(this.getMail_body(), "text/html;charset=UTF-8");
			multipart.addBodyPart(context);
			sys_Files = this.getSys_Files();
			/** 設定發送附件 **/
			if (sys_Files != null) {
				for (Sys_File sys_File : sys_Files) {
					context = new MimeBodyPart();
					String filePath = systemProperties.systemDocumentPath + "/"
							+ sys_File.getFile_name();
					fileAttach = new FileDataSource(filePath);
					context.setDataHandler(new DataHandler(fileAttach));
					context.setFileName(MimeUtility.encodeText(sys_File
							.getDisplay_file_name()));
					multipart.addBodyPart(context);

					fileAttach = null;
					context = null;
				}
			}
			mesg.setContent(multipart);
			mesg.saveChanges();
			transport = mailSession.getTransport("smtp");
			log.debug("SendMail systemProperties.mail_auth ="
					+ systemProperties.mail_auth);
			if ("true".equalsIgnoreCase(systemProperties.mail_auth)) {
				transport.connect(mailserver, systemProperties.mail_auth_user,
						systemProperties.mail_auth_pswd);
				transport.sendMessage(mesg, mesg.getAllRecipients());
			} else {
				transport.send(mesg);
			}

			transport.close();
			returnValue = "SendMailSuccess";
		} catch (Exception e) {
			log.error("發郵件異常", e);
		} finally {
			mailserver = null;
			address = null;
			pro = null;
			mailSession = null;
			mesg = null;
			internetAddress = null;
			multipart = null;
			transport = null;
			sys_Files = null;
		}
		return returnValue;
	}

	public String toUtf8(String str) {
		try {
			return new String(str.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

}
