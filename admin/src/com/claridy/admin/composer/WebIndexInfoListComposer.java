package com.claridy.admin.composer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebIndexInfo;
import com.claridy.facade.WebIndexInfoService;
import com.claridy.facade.WebSysLogService;
/**
 * zfdong nj
 * 管理端登入頁資訊管理作業 清單列
 * 2014/8/6
 */
public class WebIndexInfoListComposer extends SelectorComposer<Component> {

	@Wire
	private Textbox tboxmatterZhTw;
	@WireVariable
	private WebIndexInfo webIndexInfo;
	@Wire
	private Listbox webIndexLix;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8734862829509839610L;

	
	
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		try {
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebIndexInfo> webIndexList=((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).findByName(null,webEmployee);
			ListModelList<WebIndexInfo> tmp=new ListModelList<WebIndexInfo>(webIndexList);
			tmp.setMultiple(true);
			webIndexLix.setModel(tmp);
		} catch (Exception e) {
			log.error("初始化webIndexInfo異常"+e);
		}
	}

	@Listen("onClick=#pageSearchBtn")
	public void search(){
		try {
			
			String name=tboxmatterZhTw.getValue().trim();
			if(name==null||name.equals("")){
				ZkUtils.showExclamation(Labels.getLabel("searchIsNull"),Labels.getLabel("info"));
				return;
			}
			name=XSSStringEncoder.encodeXSSString(name);
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebIndexInfo> webIndexList=((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).findByName(name,webEmployee);
			ListModelList<WebIndexInfo> tmp=new ListModelList<WebIndexInfo>(webIndexList);
			tmp.setMultiple(true);
			webIndexLix.setModel(tmp);
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#showAll")
	public void showAll(){
		try {
			WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			tboxmatterZhTw.setValue("");
			List<WebIndexInfo> webIndexList=((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).findByName(null,webEmployee);
			ListModelList<WebIndexInfo> tmp=new ListModelList<WebIndexInfo>(webIndexList);
			tmp.setMultiple(true);
			webIndexLix.setModel(tmp);
		} catch (WrongValueException e) {
			log.error("index顯示全部異常"+e);
		}
	}
	@Listen("onClick=#startBtn")
	public void startIsDisplary(){
		try {
			int updateChecked=webIndexLix.getSelectedCount();
			if(updateChecked>0){
				Set<Listitem> listitem=webIndexLix.getSelectedItems();
				WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				for(Listitem employee:listitem){
					webIndexInfo=employee.getValue();
					webIndexInfo.setIsDisplay(1);
					((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).updateIsDisplay(webIndexInfo);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "index_"+webIndexInfo.getUuid());
				}
				ZkUtils.refurbishMethod("webIndexInfo/webIndexInfo.zul");
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webIndexInfo.startIsNull"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("開啟webIndexInfo狀態異常"+e);
		}
	}
	@Listen("onClick=#closeBtn")
	public void closeIsDisplay(){
		try {
			int updateChecked=webIndexLix.getSelectedCount();
			if(updateChecked>0){
				Set<Listitem> listitem=webIndexLix.getSelectedItems();
				WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				for(Listitem employee:listitem){
					webIndexInfo=employee.getValue();
					webIndexInfo.setIsDisplay(0);
					((WebIndexInfoService)SpringUtil.getBean("webIndexInfoService")).updateIsDisplay(webIndexInfo);
					((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "index_"+webIndexInfo.getUuid());
				}
				ZkUtils.refurbishMethod("webIndexInfo/webIndexInfo.zul");
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webIndexInfo.closeIsNull"),Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("關閉webIndexInfo狀態異常"+e);
		}
	}
	
	

}
