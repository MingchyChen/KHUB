package com.claridy.common.util;

public class XSSStringEncoder {

	/**
	 * 排除XSS(Cross Site Scripting)和SQL injection攻擊字元
	 * 
	 * @param data
	 * */
	public static String encodeXSSString(String data) {
		if (data == null || "".equals(data)) {
			return data;
		}

		final StringBuffer buf = new StringBuffer();
		final char[] chars = data.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			// > < ) ( " ' % ; & +
			if ((int) chars[i] == 62 || (int) chars[i] == 60
					|| (int) chars[i] == 41 || (int) chars[i] == 40
					|| (int) chars[i] == 34 || (int) chars[i] == 39
					|| (int) chars[i] == 37 || (int) chars[i] == 59
					|| (int) chars[i] == 38 || (int) chars[i] == 43) {
				buf.append("&#" + (int) chars[i]);
			} else {
				buf.append((char) chars[i]);
			}
		}

		return buf.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(encodeXSSString("#"));
	}

}
