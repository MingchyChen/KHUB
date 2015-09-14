package com.claridy.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class Resources_rsbatch_dataSource2 implements JRDataSource{

	 private List<String[]> data=null;
	 private String[] title=null;
	 private int loop=-1;
	 
	 public Resources_rsbatch_dataSource2(String[] titleValue,List<String[]> modelList){
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
	       try {
	    	   int objInt=Integer.parseInt(obj.toString());
	    	   return objInt;
			} catch (Exception e) {
				return obj;
			}
	}
	public boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
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
