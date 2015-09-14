package com.claridy.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.util.resource.Labels;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.dao.IWebEmployeeDAO;
import com.claridy.dao.IWebSysLogDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebSysLog;

@Service
public class WebSysLogService {

	@Autowired
	private IWebSysLogDAO webSysLogDAO;
	@Autowired
	private IWebEmployeeDAO webEmployeeDAO;

	/***
	 * saveOrUpdateLog 保存日誌
	 * 
	 * @param userIP
	 *            登入者IP
	 * @param employeeSN
	 *            登入者帳戶
	 * @param nLocate
	 *            系統編號
	 * @param nNote
	 *            觸發動作
	 * 
	 */
	public void saveOrUpdateLog(String userIP, String employeeSN,
			String nLocate, String nNote) {
		userIP = XSSStringEncoder.encodeXSSString(userIP);
		employeeSN = XSSStringEncoder.encodeXSSString(employeeSN);
		nLocate = XSSStringEncoder.encodeXSSString(nLocate);
		nNote = XSSStringEncoder.encodeXSSString(nNote);

		WebSysLog tmpWebSysLog = new WebSysLog();
		tmpWebSysLog.setNip(userIP);
		tmpWebSysLog.setEmployeesn(employeeSN);
		tmpWebSysLog.setNlocate(nLocate);
		tmpWebSysLog.setNnote(nNote);
		tmpWebSysLog.setNkey(UUIDGenerator.getUUID());
		tmpWebSysLog.setNdate(new Date());
		webSysLogDAO.saveOrUpdate(tmpWebSysLog);
	}

	/***
	 * insertLog 保存日誌
	 * 
	 * @param userIP
	 *            登入者IP
	 * @param employeeSN
	 *            登入者帳戶
	 * @param nLocate
	 *            系統編號
	 * 
	 * 
	 */
	public void insertLog(String userIP, String employeeSN, String nLocate) {
		userIP = XSSStringEncoder.encodeXSSString(userIP);
		employeeSN = XSSStringEncoder.encodeXSSString(employeeSN);
		nLocate = XSSStringEncoder.encodeXSSString(nLocate);

		WebSysLog tmpWebSysLog = new WebSysLog();
		tmpWebSysLog.setNip(userIP);
		tmpWebSysLog.setEmployeesn(employeeSN);
		tmpWebSysLog.setNlocate(nLocate);
		tmpWebSysLog.setNnote(Labels.getLabel("add"));
		tmpWebSysLog.setNkey(UUIDGenerator.getUUID());
		tmpWebSysLog.setNdate(new Date());
		webSysLogDAO.saveOrUpdate(tmpWebSysLog);
	}

	/***
	 * editLog 存儲修改動作日誌
	 * 
	 * @param userIP
	 *            登入者IP
	 * @param employeeSN
	 *            登入者帳戶
	 * @param nLocate
	 *            系統編號
	 * 
	 * 
	 */
	public void editLog(String userIP, String employeeSN, String nLocate) {
		userIP = XSSStringEncoder.encodeXSSString(userIP);
		employeeSN = XSSStringEncoder.encodeXSSString(employeeSN);
		nLocate = XSSStringEncoder.encodeXSSString(nLocate);

		WebSysLog tmpWebSysLog = new WebSysLog();
		tmpWebSysLog.setNip(userIP);
		tmpWebSysLog.setEmployeesn(employeeSN);
		tmpWebSysLog.setNlocate(nLocate);
		tmpWebSysLog.setNnote(Labels.getLabel("edit"));
		tmpWebSysLog.setNkey(UUIDGenerator.getUUID());
		tmpWebSysLog.setNdate(new Date());
		webSysLogDAO.saveOrUpdate(tmpWebSysLog);
	}

	/***
	 * delLog 存儲刪除動作日誌
	 * 
	 * @param userIP
	 *            登入者IP
	 * @param employeeSN
	 *            登入者帳戶
	 * @param nLocate
	 *            系統編號
	 * 
	 * 
	 */
	public void delLog(String userIP, String employeeSN, String nLocate) {
		userIP = XSSStringEncoder.encodeXSSString(userIP);
		employeeSN = XSSStringEncoder.encodeXSSString(employeeSN);
		nLocate = XSSStringEncoder.encodeXSSString(nLocate);

		WebSysLog tmpWebSysLog = new WebSysLog();
		tmpWebSysLog.setNip(userIP);
		tmpWebSysLog.setEmployeesn(employeeSN);
		tmpWebSysLog.setNlocate(nLocate);
		tmpWebSysLog.setNnote(Labels.getLabel("del"));
		tmpWebSysLog.setNkey(UUIDGenerator.getUUID());
		tmpWebSysLog.setNdate(new Date());
		webSysLogDAO.saveOrUpdate(tmpWebSysLog);
	}

	/**
	 * search 根據系統編號獲取日誌列表
	 * 
	 * @param nLocate
	 *            系統編號
	 * @return List<WebSysLog>
	 */
	public List<WebSysLog> search(String nLocate) {
		 List<WebSysLog> sysLogList=webSysLogDAO.search(nLocate);
		 for(WebSysLog webSysLog:sysLogList){
			 WebEmployee webEmployee=webEmployeeDAO.getWebEmployee(webSysLog.getEmployeesn());
			 if(webEmployee!=null&&webEmployee.getEmployeesn()!=null){
				 webSysLog.setEmployeesn(webEmployee.getEmployeeName());
			 }
		 }
		 return sysLogList;
	}
}
