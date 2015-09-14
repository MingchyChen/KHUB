/***
 * ClassName:RandomIDGenerator
 * Description:ID產生器
 * Creator:RemberSu
 * CreateDate:2014-07-05
 * Modified:
 * MocifiedDate:
 * ModifiedDes:
 */
package com.claridy.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author RemberSu
 * 
 */
public class RandomIDGenerator {
	public RandomIDGenerator() {
	}

	/**
	 * 獲得一個RandomsID
	 * 
	 * @return String RandomsID
	 */
	public static String getRandomId() {
		Date tmpDate = new Date();
		SimpleDateFormat tmpFmtDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String retStr = String.valueOf(tmpFmtDate.format(tmpDate));
		System.currentTimeMillis();

		int tmpInt = (int) (Math.random() * 900 + 100);
		retStr = retStr + String.valueOf(tmpInt);
		return retStr;
	}

	/**
	 * 獲得一個RandomsID
	 * 
	 * @return String RandomsID
	 */
	public static String getQuestionRandomId() {
		Date tmpDate = new Date();
		SimpleDateFormat tmpFmtDate = new SimpleDateFormat("yyyyMMdd");
		String retStr = "Q-" + String.valueOf(tmpFmtDate.format(tmpDate)) + "-";

		int tmpInt = (int) (Math.random() * 900 + 100);
		retStr = retStr + String.valueOf(tmpInt);
		return retStr;
	}

	/**
	 * 獲得指定數量的RandomsIDs
	 * 
	 * @param number
	 *            int 需要獲得的RandomsIDs數量
	 * @return String[] RandomsIDs陣列
	 */
	public static String[] getRandomId(int number) {
		if (number < 1) {
			return null;
		}
		String[] RandomsIDs = new String[number];
		for (int i = number - 1; i >= 0; i--) {
			RandomsIDs[i] = getRandomId();
		}
		return RandomsIDs;
	}

	public static void main(String[] args) {
		/*String[] ss = getRandomId(3);
		for (int i = 0; i < ss.length; i++) {
			System.out.println(ss[i]);
		}*/
		System.out.println(getSerialNumber("PU"));
	}
	
	
	/**
	 * 獲得一個getQuestionHistoryRandomId
	 * 
	 * @return String RandomsID
	 */
	public static String getQuestionHistoryRandomId() {
		Date tmpDate = new Date();
		SimpleDateFormat tmpFmtDate = new SimpleDateFormat("yyyyMMdd");
		String retStr = "H-" + String.valueOf(tmpFmtDate.format(tmpDate)) + "-";

		int tmpInt = (int) (Math.random() * 900 + 100);
		retStr = retStr + String.valueOf(tmpInt);
		return retStr;
	}
	/**
	 * 獲得一個getQuestionHistoryRandomId
	 * 
	 * @return String RandomsID
	 */
	public static String getSerialNumber(String parameter) {
		Date tmpDate = new Date();
		SimpleDateFormat tmpFmtDate = new SimpleDateFormat("yyyyMMdd");
		String retStr = parameter + String.valueOf(tmpFmtDate.format(tmpDate));

		int tmpInt = (int) (Math.random() * 900 + 100);
		retStr = retStr + String.valueOf(tmpInt);
		return retStr;
	}
	/***
	 * 根據輸入值產生流水號 ,例如1,4,則產生流水號為0001
	 * @param val
	 * @param size
	 * @return
	 */
	public static String fmtLong(Long val, int size) {
		StringBuilder sb = new StringBuilder("");
		sb.append(val);

		if (sb.length() < size) {

			int cnt = size - sb.length();

			for (int i = 0; i < cnt; i++) {
				sb.insert(0, "0");
			}

			return sb.toString();
		} else

		if (sb.length() > size) {

			return sb.substring(sb.length() - size, size);
		} else {

			return sb.toString();
		}
	}
}
