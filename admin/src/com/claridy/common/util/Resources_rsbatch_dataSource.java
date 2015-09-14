package com.claridy.common.util;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class Resources_rsbatch_dataSource implements JRDataSource{

	 private List<String[]> data=null;
	 private String[] title=null;
	 private int loop=-1;
	 
	 public Resources_rsbatch_dataSource(String[] titleValue,List<String[]> modelList){
		 data=modelList;
		 title=titleValue;
	 }
	public Object getFieldValue(JRField field) throws JRException {
		String[] model=this.data.get(loop);
		Object obj="";
       for(int i=0;i<model.length;i++){
    	   if(title[i].equals(field.getName())){
    			obj=model[i];
    	   }
       
       }
		return obj;
	}

	public boolean next() throws JRException {
		loop++;
		if(loop>=data.size()){
			return false;
		}else{
		return true;
		}
	}
	
}
