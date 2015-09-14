package com.claridy.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date getHalfYear(){
		
		long time = 180 * 24L * 60 * 60 * 1000;
		long time1 = new Date().getTime() - time;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(time1);
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getStart(){ 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = "1970-01-01";
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) { 
			e.printStackTrace();
		}
		return date;
	}
	
	//通過日期獲得星期幾
		public static String getWeekStr(Date date){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int week = c.get(Calendar.DAY_OF_WEEK);
			  String str = week+"";
			  if("1".equals(str)){
			   str = "7";
			  }else if("2".equals(str)){
			   str = "1";
			  }else if("3".equals(str)){
			   str = "2";
			  }else if("4".equals(str)){
			   str = "3";
			  }else if("5".equals(str)){
			   str = "4";
			  }else if("6".equals(str)){
			   str = "5";
			  }else if("7".equals(str)){
			   str = "6";
			  }
			  return str;
			 }
}
