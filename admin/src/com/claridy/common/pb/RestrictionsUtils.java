package com.claridy.common.pb;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

public class RestrictionsUtils {

	 public RestrictionsUtils(){
	 }
	 /**
	  * 
	  * @description:處理字串中含轉義字元問題
	  * @return
	  */
	 public static Criterion ilike(final String propertyName, String value, MatchMode matchMode) {
	  return new IlikeExpressionEx(propertyName, value, matchMode);
	 }

}
