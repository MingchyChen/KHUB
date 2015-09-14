package com.claridy.timer;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.claridy.domain.ErmSysSchedule;
import com.claridy.facade.ErmSysScheduleService;

public class ErmSysscheduleTimerOneTask extends TimerTask {
	@Autowired
	private ErmSysScheduleService erSysChedService;
	// 避免兩個Thread同時執行
	private static boolean isRunning = false;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void run() {
		try {
			
			ErmSysSchedule ermSysChedule =erSysChedService.findAllByStatus("201112131712535216c98b7d22bfafd948b7381c9d59f41274");
			String sysTime=format.format(System.currentTimeMillis());
			Date sysTimeDate=(Date) format.parseObject(sysTime);
			if(!isRunning){
				isRunning=true;
				if(ermSysChedule!=null){
					String time=format.format(ermSysChedule.getTime());
					Date timeDate=(Date) format.parseObject(time);
					if(time.equals(sysTime)||timeDate.before(sysTimeDate)){
						erSysChedService.promo_schedule();
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
