package com.claridy.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemProperties {

	// SendMail
	public @Value("#{systemproperties['Properties.mail_server']}")
	String mail_server;
	public @Value("#{systemproperties['Properties.mail_from_addr']}")
	String mail_from_addr;
	public @Value("#{systemproperties['Properties.mail_from_addr_name']}")
	String mail_from_addr_name;
	public @Value("#{systemproperties['Properties.mail_auth']}")
	String mail_auth;
	public @Value("#{systemproperties['Properties.mail_timeout']}")
	String mail_timeout;
	public @Value("#{systemproperties['Properties.mail_auth_user']}")
	String mail_auth_user;
	public @Value("#{systemproperties['Properties.mail_auth_pswd']}")
	String mail_auth_pswd;

	// System Document Path
	public @Value("#{systemproperties['Properties.systemDocumentPath']}")
	String systemDocumentPath;

	public @Value("#{systemproperties['Properties.portalDocementPath']}")
	String portalDocementPath;

	// Ezproxy Manage
	public @Value("#{systemproperties['Properties.ezproxyLogin']}")
	String ezproxyLogin;
	public @Value("#{systemproperties['Properties.ezproxyRestart']}")
	String ezproxyRestart;
	public @Value("#{systemproperties['Properties.ezproxyUser']}")
	String ezproxyUser;
	public @Value("#{systemproperties['Properties.ezproxyPass']}")
	String ezproxyPass;
	public @Value("#{systemproperties['Properties.ezproxyConfigTxt']}")
	String ezproxyConfigTxt;

	// System Deskey
	public @Value("#{systemproperties['Properties.deskey']}")
	String systemDeskey;

	// 0 not use data acess, 1 use data acess
	public @Value("#{systemproperties['Properties.dataauth']}")
	String dataAuth;

	// agridl URL
	public @Value("#{systemproperties['Properties.agridlURL']}")
	String agridlURL;

}
