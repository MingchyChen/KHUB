package com.claridy.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.claridy.domain.ErmSysSchedule;
import com.claridy.facade.ErmSysScheduleService;
import com.claridy.facade.ResourcesMainfileSolrSearch;

public class ErmSysscheduleTimerThreeTask extends TimerTask {
	@Autowired
	private ErmSysScheduleService erSysChedService;
	@Autowired
	private ResourcesMainfileSolrSearch mainfileSolrSearch;
	// 避免兩個Thread同時執行
	private static boolean isRunning = false;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void run() {
		try {
			ErmSysSchedule ermSysChedule =erSysChedService.findAllByStatus("201112140908561f7e17f8a1a5a7ce07eea93adbd1f62f407f");
			String sysTime=format.format(System.currentTimeMillis());
			Date sysTimeDate=(Date) format.parseObject(sysTime);
			if(!isRunning){
				isRunning=true;
				if(ermSysChedule!=null){
					
					String time=format.format(ermSysChedule.getTime());
					Date timeDate=(Date) format.parseObject(time);
					if(time.equals(sysTime)||timeDate.before(sysTimeDate)){
						mainfileSolrSearch.addData();
						ermSysChedule.setStatus(1);
						erSysChedService.updateErmSysSchedule(ermSysChedule);
					}
				}
				isRunning=false;
			}
		}catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
