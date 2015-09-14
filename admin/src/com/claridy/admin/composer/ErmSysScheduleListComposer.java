package com.claridy.admin.composer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmSysScheduleService;
/**
 * zfdong nj
 * 排程 清單列
 * 2014/8/6
 */
public class ErmSysScheduleListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1450520387091126370L;
	@Wire
	private Listbox scheduleLix;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		try {
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<ErmSysSchedule> cheduleList=((ErmSysScheduleService)SpringUtil.getBean("ermSysScheduleService")).findAll(webEmployee);
			ListModelList<ErmSysSchedule> model=new ListModelList<ErmSysSchedule>(cheduleList);
			model.setMultiple(true);
			scheduleLix.setModel(model);
		} catch (Exception e) {
			log.error("初始化異常"+e);
		}
		
	}

	
	
}
