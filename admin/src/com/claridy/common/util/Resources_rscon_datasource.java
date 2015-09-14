package com.claridy.common.util;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class Resources_rscon_datasource implements JRDataSource {
	private List<String []> datas = null ;
	private int loop = -1 ; 
	public Resources_rscon_datasource(List<String[]> dataList){
		datas = dataList;
	}
	public Object getFieldValue(JRField field) throws JRException {
		String []model = (String[]) this.datas.get(loop);
		Object rs = "";
		if ("resources_id".equals(field.getName())) {
			rs = model[0];
		} else if ("title".equals(field.getName())) {
			rs = model[1];
		}
		for(int j = 2;j<model.length;j++){
			int count = model[j]==null?0:Integer.valueOf(model[j]).intValue();
			if (("month_status"+j).equals(field.getName())) {
				rs = count;
			}
		}
		model = null;
		return rs;

	}

	public boolean next() throws JRException {
		loop++;
		if (loop >= datas.size()) {
			return false;
		} else {
			return true;
		}

	}

}
