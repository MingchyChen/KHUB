package com.claridy.facade;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestTimerService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	protected SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void runTimeerTask(){
		try {
			System.out.println("排程測試--------------------"+format.format(new Date())+"--------------------");
		} catch (Exception e) {
			log.error("排程執行報錯",  e);
		}
	}
}