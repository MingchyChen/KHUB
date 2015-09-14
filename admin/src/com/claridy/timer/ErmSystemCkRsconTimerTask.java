package com.claridy.timer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSystemCkrscon;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesCkrsService;
import com.claridy.facade.ErmSystemCkrsconService;
import com.claridy.facade.ResourcesCkrsSolrSearch;

public class ErmSystemCkRsconTimerTask extends TimerTask {
	
	@Autowired
	private ErmSystemCkrsconService ermSystemCkrsconService;
	@Autowired
	private ResourcesCkrsSolrSearch resourcesCkrsSolrSearch;
	@Autowired
	private ErmResourcesCkrsService ermResourcesCkrsService;
	// 避免兩個Thread同時執行
	private static boolean isRunning = false;
	
	@Override
	public void run() {
		ErmSystemCkrscon ermSystemCkrscon=ermSystemCkrsconService.getErmSystemCkrsconAll();
		String[] checkitems=ermSystemCkrscon.getCheckItem().split(":");
		
		if(checkitems.length>0){
			if(!isRunning){
				isRunning=true;
				Calendar calendar=Calendar.getInstance();
				int hour=calendar.get(Calendar.HOUR_OF_DAY);
				int minute=calendar.get(Calendar.MINUTE);
				if(Integer.parseInt(checkitems[0])==hour&&Integer.parseInt(checkitems[1])==minute){
					resourcesCkrsSolrSearch.addData();
					ermResourcesCkrsService.searchCkrs();
				}
				isRunning=false;
			}
		}
	}
	
	

}
