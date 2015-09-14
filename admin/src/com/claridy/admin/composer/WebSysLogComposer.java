package com.claridy.admin.composer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebSysLog;
import com.claridy.facade.WebSysLogService;

/**
 * WebSysLogComposer 系統日誌Composer
 * 
 * @author RemberSu
 * @serial 2014-06-05
 * 
 */
public class WebSysLogComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 6499706039429142367L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	protected Listbox webSysLogLBX;

	@WireVariable
	private List<WebSysLog> webSysLogList;

	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) Executions.getCurrent()
					.getArg();
			String employeens = (String) map.get("nlocate");
			employeens=XSSStringEncoder.encodeXSSString(employeens);
			
			webSysLogList = ((WebSysLogService) SpringUtil
					.getBean("webSysLogService")).search(employeens);
			ListModelList<WebSysLog> tmpLML = new ListModelList<WebSysLog>(
					webSysLogList);
			webSysLogLBX.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("初始化頁面出錯",e);
		}
	}

}
