package com.claridy.common.util;

import java.util.HashMap;

import com.claridy.common.mechanism.domain.Sys_JSONParam;

public class SystemJSONUtil {
	public static HashMap<String, String> convertToMap(Sys_JSONParam[] params) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Sys_JSONParam param : params) {
			map.put(XSSStringEncoder.encodeXSSString( param.getName() ), XSSStringEncoder.encodeXSSString( param.getValue() ));
		}
		return map;
	}
}
