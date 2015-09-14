package com.claridy.admin.composer;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmSysScheduleService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 排程 修改
 * 2014/8/6
 */
public class ErmSysScheduleComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	@Wire
	private Textbox nameBox;
	
	@Wire
	private Datebox timeDbox;
	ErmSysSchedule schedule;
	@Wire
	private Window ermSysScheduleEditWin;
	@Wire
	private Combobox hourCBbx;
	@Wire
	private Combobox minCBbx;
	
	private static final long serialVersionUID = 1450520387091126370L;
	
	private final Logger log=LoggerFactory.getLogger(getClass());
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat formattter=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String id=map.get("id");
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			schedule=((ErmSysScheduleService)SpringUtil.getBean("ermSysScheduleService")).findById(id,webEmployee);
			nameBox.setValue(schedule.getName());
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time=dateformat.format(schedule.getTime());
			String[] times=time.split(" ");
			
			timeDbox.setValue(schedule.getTime());
			for(int i=0;i<24;i++){
				if(i<10){
					if(times[1].substring(0,2).equals("0"+i)){
						hourCBbx.setSelectedIndex(i);
						break;
					}
				}else{
					if(times[1].substring(0,2).equals(Integer.toString(i))){
						hourCBbx.setSelectedIndex(i);
						break;
					}
				}
			}
			if(times[1].substring(3).equals("00")){
				minCBbx.setSelectedIndex(0);
			}
			if(times[1].substring(3).equals("30")){
				minCBbx.setSelectedIndex(1);
			}
			
			
			
		} catch (Exception e) {
			log.error("edit初始化異常"+e);
		}
	}
	
	@Listen("onClick=#editBtn")
	public void update(){
		try {
			String name=nameBox.getValue();
			if(name!=null&&!"".equals(name)){
				name=XSSStringEncoder.encodeXSSString(name);
				String timedb=format.format(timeDbox.getValue());
				String time=timedb+" "+hourCBbx.getSelectedItem().getValue().toString()+":"+minCBbx.getSelectedItem().getValue().toString();
				ParsePosition pos = new ParsePosition(0);
				Date date=formattter.parse(time,pos);
				schedule.setTime(date);
				schedule.setName(name);
				schedule.setStatus(0);
				WebEmployee loginWebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				((ErmSysScheduleService)SpringUtil.getBean("ermSysScheduleService")).updateErmSysSchedule(schedule);
				((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),loginWebEmployee.getEmployeesn(), "schedule_"+schedule.getId());
				ZkUtils.showInformation(Labels.getLabel("updateOK"),
						Labels.getLabel("info"));
				ZkUtils.refurbishMethod("ermSysSchedule/ermSysSchedule.zul");
				ermSysScheduleEditWin.detach();
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermSysschedule.nameIsNull"),Labels.getLabel("info"));
				nameBox.focus();
			}
		} catch (Exception e) {
			log.error("編輯schedule異常"+e);
		}
	}

	
	
}
