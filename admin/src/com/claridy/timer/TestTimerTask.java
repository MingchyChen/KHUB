package com.claridy.timer;

import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.claridy.facade.TestTimerService;


public class TestTimerTask extends TimerTask {

	@Autowired
	private TestTimerService testTimerService;
	
	
	// 避免兩個Thread同時執行
	private static boolean isRunning = false;

	@Override
	public void run() {
		
		if (!isRunning) {
			isRunning = true;
			System.out.println("------------------------------------");
			// 加入判斷要執行的時間，例如:凌晨2~3點才執行
			testTimerService.runTimeerTask();
			
			isRunning = false;
		}
	}
}
