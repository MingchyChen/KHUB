package com.claridy.common.pb;

import org.springframework.util.StringUtils;



public class StringUtil {

	  public static String escapeSQLLike(String likeStr) {   
	        String str = StringUtils.replace(likeStr, "_", "\\_");   
	        str = StringUtils.replace(str, "%",    "\\%");   
//	        str = StringUtils.replace(str, "?", "\\?");   
//	        str = StringUtils.replace(str, "*", "\\*");   
	        return str;   
	    }  
	  

}
