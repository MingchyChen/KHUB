package com.claridy.admin.composer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebNoticeTemplates;
import com.claridy.facade.WebNoticeTemplatesService;

/**
 * 
 * sunchao nj
 * 通知單範本管理
 *
 */
public class WebNoticeTempListComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4495610820701239659L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Listbox noticeTempLix;
	@Wire
	private Textbox nameZhTw;
	@WireVariable
	private List<WebNoticeTemplates> webNoticeTempList;
	private WebEmployee webEmployee;
	@Listen("onClick=#pagSearchBtn")
	public void search(){
		try {
			String sNameZhTw=nameZhTw.getValue();
			sNameZhTw= XSSStringEncoder.encodeXSSString(sNameZhTw);
			if (StringUtils.isBlank(sNameZhTw)) {
				ZkUtils.showExclamation(
						Labels.getLabel("inputString"),
						Labels.getLabel("warn"));
				nameZhTw.focus();
				return;
			}
			String nameZhTW=XSSStringEncoder.encodeXSSString(nameZhTw.getValue().trim());
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			List<WebNoticeTemplates> webNoticeTemplist=((WebNoticeTemplatesService)SpringUtil.getBean("webNoticeTemplatesService")).findAllWebNoticeTemp(nameZhTW,webEmployee);
			ListModelList<WebNoticeTemplates> webNoticeTempModel=new ListModelList<WebNoticeTemplates>(webNoticeTemplist);
			webNoticeTempModel.setMultiple(true);
			noticeTempLix.setModel(webNoticeTempModel);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢通知單範本管理集合出錯",e);
		}
	}
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		List<WebNoticeTemplates> webNoticeTemplist=((WebNoticeTemplatesService)SpringUtil.getBean("webNoticeTemplatesService")).findAllWebNoticeTemp("",webEmployee);
		ListModelList<WebNoticeTemplates> webNoticeTempModel=new ListModelList<WebNoticeTemplates>(webNoticeTemplist);
		webNoticeTempModel.setMultiple(true);
		nameZhTw.setValue("");
		noticeTempLix.setModel(webNoticeTempModel);
	}
	@Override
	public void doAfterCompose(Component comp){
		try {
			super.doAfterCompose(comp);
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			//查詢通知單範本管理集合
			webNoticeTempList=((WebNoticeTemplatesService)SpringUtil.getBean("webNoticeTemplatesService")).findAllWebNoticeTemp("",webEmployee);
			ListModelList<WebNoticeTemplates> tmpLML=new ListModelList<WebNoticeTemplates>(webNoticeTempList);
			tmpLML.setMultiple(true);
			noticeTempLix.setModel(tmpLML);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查詢通知單範本管理集合出錯",e);
		}
	}
}
