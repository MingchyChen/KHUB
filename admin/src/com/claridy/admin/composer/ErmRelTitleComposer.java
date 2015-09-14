package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmResourcesReltitle;
import com.claridy.facade.ErmResourcesReltitleService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;

/**
 * 
 * sunchao nj
 * 相關題名新增編輯頁面
 * 2014/09/23
 */
public class ErmRelTitleComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox resIdBox;
	@Wire
	private Textbox relTitleIdBox;
	@Wire
	private Textbox relTitleNameBox;
	private ErmResourcesReltitle resRelTitle;
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			Map<String, String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String resTitleId=map.get("resTitleId");
			String resourcesId="";
			String relTitleId="";
			String[] str=resTitleId.split(",");
			if(str.length==1){
				resourcesId=str[0];
			}
			if(str.length==2){
				resourcesId=str[0];
				relTitleId=str[1];
			}
			if(relTitleId!=null&&!"".equals(relTitleId)){
				resRelTitle=((ErmResourcesReltitleService)SpringUtil.getBean("ermResourcesReltitleService")).getReltitle(resourcesId, relTitleId);
				resIdBox.setValue(resRelTitle.getResourcesId());
				relTitleIdBox.setValue(resRelTitle.getRelatedTitleId());
				relTitleNameBox.setValue(resRelTitle.getName());
			}else{
				resIdBox.setValue(str[0]);
				resIdBox.setReadonly(true);
				relTitleIdBox.setDisabled(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("相關題名進入新增或修改頁面報錯",e);
		}
	}
	@Listen("onClick=#saveBtn")
	public void addResourcesDbws(){
		try {
			resRelTitle=new ErmResourcesReltitle();
			String relTitleName=relTitleNameBox.getValue();
			if (StringUtils.isBlank(relTitleName)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.relatedTItleName") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				relTitleNameBox.focus();
				return;
			}
			resRelTitle.setResourcesId(resIdBox.getValue());
			resRelTitle.setRelatedTitleId(UUIDGenerator.getUUID());
			resRelTitle.setName(relTitleName);
			((ErmResourcesReltitleService)SpringUtil.getBean("ermResourcesReltitleService")).create(resRelTitle);
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
			String resourcesId=resRelTitle.getResourcesId();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
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
			log.error("相關題名新增方法報錯",e);
		}
	}
	@Listen("onClick=#updBtn")
	public void updateResourcesDbws(){
		try {
			String relTitleName=relTitleNameBox.getValue();
			if (StringUtils.isBlank(relTitleName)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.relatedTItleName") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				relTitleNameBox.focus();
				return;
			}
			resRelTitle.setName(relTitleName);
			((ErmResourcesReltitleService)SpringUtil.getBean("ermResourcesReltitleService")).update(resRelTitle);
			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("updateOK"),Labels.getLabel("info"));
			String resourcesId=resRelTitle.getResourcesId();
			String url="ermResMainDbws/ermTypeRes.zul?resourcesId="+resourcesId+"";
			ZkUtils.refurbishMethod(url);
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
			log.error("相關題名修改方法報錯",e);
		}
	}
}
