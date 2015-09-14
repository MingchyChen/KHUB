package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesSubject;
import com.claridy.facade.ErmResourcesSubjectService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;

/**
 * 
 * sunchao nj
 * 主題分類新增頁面
 * 2014/09/26
 */
public class ErmSubjectComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox resIdBox;
	@Wire
	private Textbox subjectIdBox;
	@Wire
	private Textbox subjectNameBox;
	private ErmResourcesSubject subject;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String resourcesId=map.get("resourcesId");
			resIdBox.setValue(resourcesId);
			resIdBox.setReadonly(true);
			subjectNameBox.setDisabled(false);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("主題分類進入新增頁面報錯",e);
		}
	}
	@Listen("onClick=#saveBtn")
	public void addSubject(){
		try {
			subject=new ErmResourcesSubject();
			String subjectId=subjectIdBox.getValue();
			if (StringUtils.isBlank(subjectId)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.subjectId") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				subjectIdBox.focus();
				return;
			}
			subject.setResourcesId(resIdBox.getValue());
			subject.setSubjectId(subjectId);
			ErmResourcesSubject subjectTemp=((ErmResourcesSubjectService)SpringUtil.getBean("ermResourcesSubjectService")).getSubject(resIdBox.getValue(), subjectId);
			if(subjectTemp.getSubjectId()!=null&&!"".equals(subjectTemp.getSubjectId())){
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.dataExists"),
						Labels.getLabel("warn"));
				subjectIdBox.focus();
				return;
			}else{
				((ErmResourcesSubjectService)SpringUtil.getBean("ermResourcesSubjectService")).create(subject);
			}
			String resourcesId=subject.getResourcesId();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
			//更新solr數據
			if(resourcesId!=null&&!"".equals(resourcesId)){
				String resStr=resourcesId.substring(0,2);
				if(resStr.equals("EJ")||resStr.equals("EB")){
					((ResourcesMainEjebSolrSearch) SpringUtil
							.getBean("resourcesMainEjebSolrSearch"))
							.resources_main_ejeb_editData(resourcesId);
				}else{
					((ResourcesMainDbwsSolrSearch) SpringUtil
							.getBean("resourcesMainDbwsSolrSearch"))
							.resources_main_dbws_editData(resourcesId);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("主題分類新增方法報錯",e);
		}
	}
	@Listen("onClick=#selectBtn")
	public void openSearch(){
		Map<String,String> map = new HashMap<String,String>();
		String subType="DBSUB";
		String resourcesId=resIdBox.getValue();
		if(resourcesId!=null&&!"".equals(resourcesId)){
			String resType=resourcesId.substring(0,2);
			if(resType.equals("EJ")){
				subType="EJSUB";
			}
			if(resType.equals("EB")){
				subType="EBSUB";
			}
			if(resType.equals("WS")){
				subType="WSSUB";
			}
		}
		map.put("openValue", subType);
		Executions.createComponents("/WEB-INF/pages/system/ermResMainDbws/ermResSubOpen.zul", this.getSelf(),map);
	}
}
