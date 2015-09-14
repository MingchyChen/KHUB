package com.claridy.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IERMCodePublisherDAO;
import com.claridy.dao.IErmCodeDbDAO;
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodePublisher;

/**
 * 驗證數據格式
 * 
 * @author junnywang
 * 
 */
@Service
public class ResourcesValidate {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public IErmCodeGeneralCodeDAO ermCodeGeneralCodeDAO;
	@Autowired
	public IERMCodePublisherDAO ermCodePublisherDAO;
	@Autowired
	private IErmCodeDbDAO codeDbDAO;
	/**
	 * 停用注記   N
	 */
	private String history ="N";
	/**
	 * 類別
	 */
	private String item_id;
	/**
	 * 驗證必填項是否為空值
	 * 
	 * @param column
	 * @return
	 */
	public boolean isEmpty(String column) {
		if (column == null || "".equals(column)) {
			return false;
		}
		return true;
	}
	/**
	 * 驗證起訂日期、迄訂日期的大小
	 * 
	 * @param date_start、date_end
	 * @return
	 */
	public boolean isdate(String date_start,String date_end) {
		boolean bool=true;
		java.text.SimpleDateFormat dd = new java.text.SimpleDateFormat("yyyy/MM/dd");
		try {
			Date start_date = dd.parse(date_start);
			Date end_date = dd.parse(date_end);
			if (start_date.compareTo(end_date)>0) {
				bool=false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bool;
	}
	/**
	 * 驗證時間類型的數據
	 * @param date
	 * @return
	 */
	public boolean isdateFormat(String date) {
		
		boolean bool=true;
		java.text.SimpleDateFormat dd = new java.text.SimpleDateFormat("yyyy/MM/dd");
		try {
			Date dates = dd.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			bool=false;
		}
		return bool;
	}

	/**
	 * 根据输入的ISBN号，检验ISBN的有效性。依据 GB/T 5795-2006 和 ISO 2108:2005 ISBN
	 * 10位标准和13位标准实现（13位标准自2007年1月1日开始实行，在此之前采用10位标准）。
	 * 
	 * @param String
	 *            isbn：需要进行校验的ISBN字符串
	 * @return true：所输入的ISBN校验正确；false：所输入的ISBN校验错误
	 */

	public boolean checkIsbn(String isbn) {
		int count = 0;
		int checkBitInt = 0;

		// 将ISBN数据全取大写字母
		// isbn = isbn.toUpperCase();

		char[] cs = isbn.toCharArray();
		switch (isbn.length()) {
		case 10:
			// ****************************************************************
			// 当ISBN为10位时，进行的校验，用于2007年1月1日前的出版物
			// 数据格式：从左至右前9位为ISBN数据，第10位为校验位
			// 校验方法：
			// (1) 从左至右将前9位数据从10开始至2进行编号，作为位权
			// (2) 将9位数据与各位位权进行加权，并求其9位和（称为加权和，记作M）
			// (3) 第10位校验位计算方法，校验位为C：
			// M + C ≡ 0 (mod 11)
			// C为10时，记作“X”
			// ****************************************************************

			// 取出前9位数字进行加权和计算
			for (int i = 0; i < 9; i++) {
				// 若前9位数据中有非数字字符，则抛出异常
				if (cs[i] < '0' || cs[i] > '9') {
					return false;
				}
				int c = cs[i] - '0';
				// 求加权和
				count += c * (10 - i);
			}

			// 取出校验位数据0～9和X符合校验字符要求
			if (cs[9] >= '0' && cs[9] <= '9') {
				checkBitInt = cs[9] - '0';
			} else if (cs[9] == 'X' || cs[9] == 'x') {
				// 校验位中的“X”表示数据“10”
				checkBitInt = 10;
			} else {
				// 非0～9或X时抛出异常
				return false;
			}

			// 进行校验
			if ((count + checkBitInt) % 11 == 0) {
				return true; // 校验成功
			} else {
				return false; // 校验失败
			}
		case 13:
			// ****************************************************************
			// 当ISBN为13位时，进行的校验，用于2007年1月1日后的出版物
			// 数据格式：从左至右前12位为ISBN数据，第13位为校验位
			// 校验方法：
			// (1) 从左至右将前12位数的取其奇位数和和偶位数和
			// (2) 将偶位数和乘3，并其与奇位数和的和，得加权和
			// (3) 第13位校验位计算方法，校验位为C：
			// M + C ≡ 0 (mod 10)
			// ****************************************************************

			// ISBN为13位数据时，前3位目前只能是“978”（已实行）或“979”（暂未实行）
			if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
				return false;
			}
			// 取出前12位数字进行加权和计算
			int countEven = 0;
			int countOdd = 0;
			for (int i = 0; i < 12; i++) {
				int c = cs[i] - '0';
				// 若前12位数据中有非数字字符，则抛出异常
				if (c < 0 || c > 9) {
					return false;
				}
				// 分别计算奇位数和偶位数的和
				if ((i & 0x1) == 0) {
					countOdd += c;
				} else {
					countEven += c;
				}
			}
			// 求加权和
			count = countOdd + (countEven * 3);

			// 取出校验位数据
			if (cs[12] < '0' || cs[12] > '9') {
				// 校验位为非0~9字符时，抛出异常
				return false;
			}

			checkBitInt = cs[12] - '0';
			// 进行校验
			if ((count + checkBitInt) % 10 == 0) {
				return true; // 校验成功
			} else {
				return false; // 校验失败
			}
		default:
			return false;
		}
	}
	/**
	 * 根据输入的ISSN号，检验ISSN的有效性
	 * 
	 * @param String
	 *            issn：需要进行校验的ISSN字符串
	 * @return true：所输入的ISSN校验正确；false：所输入的ISSN校验错误
	 */
	public boolean checkIssn(String issn){
		int count = 0;
		int checkBitInt = 0;
	
		switch(issn.length()){
		case 8:
			char[] cs=issn.toCharArray();
			for (int i = 0; i < 7; i++) {
				// 若前7位数据中有非数字字符，则抛出异常
				if (cs[i] < '0' || cs[i] > '9') {
					return false;
				}
				int c = cs[i] - '0';
				// 求加权和
				count += c * (8 - i);
			}
			
			if (cs[7] >= '0' && cs[7] <= '9') {
				checkBitInt = cs[7] - '0';
			} else if (cs[7] == 'X' || cs[7] == 'x') {
				// 校验位中的“X”表示数据“10”
				checkBitInt = 10;
			} else {
				// 非0～9或X时抛出异常	
				return false;
			}
			
			// 进行校验	
			if ((count + checkBitInt) % 11 == 0) {
				return true; // 校验成功
			} else {
				return false; // 校验失败
			}
		default:return false;
		}
		
	}
	/**
	 * 驗證IP格式是否正確
	 * @param ip
	 * @return
	 */
	public boolean checkIp(String ip){
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip); 
		return matcher.matches();
	}
	/**
	 * 驗證url
	 * @param url
	 * @return
	 */
	public boolean checkUrl(String url){
		Pattern pattern = Pattern.compile("(http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?",Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url); 
		return matcher.matches();
	}
	/**
	 * URL驗證
	 * 
	 */
	public boolean urlValidata(String urlPath){
		boolean bool=true;
		  try{
	              URL url = new URL(urlPath);
	              URLConnection uc = url.openConnection();//創建連接
	              //用IO讀取內容
	              BufferedReader br = new BufferedReader(
	                new InputStreamReader(
	                 uc.getInputStream()));
	              br.close();
	          }catch (Exception e) {
	            bool=false;//如果讀取異常則返回false;
	          } 
		return bool;
	}
	
	/**
	 * 驗證主題、資料類型、語言、狀態、採購注記、連線方式、存放地點是否存在
	 * item_id:DBSUB、DBTYPE、decode(:erm_resources_mainfile_m.type_id,'DB','DBLAN','RELAN')、STATE、PURE、ACCESS、PLACE
	 * @param name 名稱(name1)
	 * @return generalcode_id 代碼
	 */
 
	public String checkResources_subject(String name){
		String generalcode_id = null;
		try {
			List<ErmCodeGeneralCode> code_generalcodeList = ermCodeGeneralCodeDAO.findGeneralCodeList(name.trim(), item_id);
			if(code_generalcodeList!=null&&code_generalcodeList.size()>0){
				ErmCodeGeneralCode erm_code_generalcode = code_generalcodeList.get(0);
				generalcode_id = erm_code_generalcode.getGeneralcodeId();
			}
			
		} catch (Exception e) {
			log.error("查詢共用代碼當報錯",e);
		}
		return generalcode_id;
	}
	/**
	 * 驗證適用學院名稱、訂購學院名稱是否存在
	 * @param name 名稱(name1)
	 * @return college_id 學院代碼
	 */
	/*public String checkCollege(String name){
		String college_id = null;
		UbictechConnectFactory connFactory = new UbictechConnectFactory();
		Connection conn = connFactory.getUbictechConn();
		IOerm_code_college erm_code_college = new IOerm_code_college();
		erm_code_college.setName1_sc(name);
		erm_code_college.setHistory_sc(history);
		erm_code_college.setFilter("trim(name1)='"+name.trim()+"' and history='"+history+"'");
		List<IOerm_code_college> code_collegeList = erm_code_college.search(connFactory, conn);
		if(code_collegeList!=null&&code_collegeList.size()>0){
			erm_code_college = code_collegeList.get(0);
			college_id = erm_code_college.getCollege_id();
		}
		// 如果connFactory没有关闭
		if (connFactory != null) {
			connFactory.closeConn(conn);
			conn = null;
			connFactory = null;
		}
		return college_id;
	}*/
	
	
	/**
	 * 驗證適用系所名稱、訂購系所名稱是否存在
	 * @param name 名稱(name1)
	 * @return department_id 系所代碼
	 */
	/*public String checkDep(String name){
		String department_id = null;
		UbictechConnectFactory connFactory = new UbictechConnectFactory();
		Connection conn = connFactory.getUbictechConn();
		IOerm_code_department erm_code_department = new IOerm_code_department();
		erm_code_department.setName1_sc(name);
		erm_code_department.setHistory_sc(history);
		erm_code_department.setFilter("trim(name1)='"+name.trim()+"' and history='"+history+"'");
		List<IOerm_code_department> code_departmentList = erm_code_department.search(connFactory, conn);
		if(code_departmentList!=null&&code_departmentList.size()>0){
			erm_code_department = code_departmentList.get(0);
			department_id = erm_code_department.getDepartment_id();
		}
		// 如果connFactory没有关闭
		if (connFactory != null) {
			connFactory.closeConn(conn);
			conn = null;
			connFactory = null;
		}
		return department_id;
	}*/
	/**
	 * 驗證代理商、出版商是否存在
	 * @param name 代理商(name)、出版商(name)
	 * @return publisher_id 代碼
	 */
	public String checkAgentedPublisher(String name){
		name=name.replace("'", "''");
		String publisher_id = null;
		List<ErmCodePublisher> code_publisherList = ermCodePublisherDAO.findPublisherList(name.trim());
		if(code_publisherList!=null&&code_publisherList.size()>0){
			ErmCodePublisher erm_code_publisher = code_publisherList.get(0);
			publisher_id = erm_code_publisher.getPublisherId();
		}
		return publisher_id;
	}
	
	/**
	 * 驗證所屬資料庫是否存在
	 * @param name 名稱(name)
	 * @return db_id 代碼
	 */
	public String checkDb(String name){
		name=name.replace("'", "''");
		String db_id = null;
		List<ErmCodeDb> code_dbList = codeDbDAO.findErmCodeDbList(name.trim());
		if(code_dbList!=null&&code_dbList.size()>0){
			ErmCodeDb erm_code_db = code_dbList.get(0);
			db_id = erm_code_db.getDbId();
		}
		return db_id;
	}
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	
}
