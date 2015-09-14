/**
 * @author RemberSu
 */
package com.claridy.common.mechanism.facase;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.ISys_UserDAO;
import com.claridy.domain.Sys_User;

@Service
public class LoginService {
	@Autowired
	protected ISys_UserDAO sysuserdao;
	private Logger log = Logger.getLogger(getClass());
	/**
	 * 檢驗後臺登錄
	 * @param username
	 * @param password
	 * @return
	 */
	public Sys_User loginCheck(String username,String password){
		return sysuserdao.getUser(username, password);
	}
}