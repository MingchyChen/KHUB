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
import com.claridy.domain.ErmResourcesDbtype;
import com.claridy.facade.ErmResourcesDbtypeService;
import com.claridy.facade.ResourcesMainDbwsSolrSearch;
import com.claridy.facade.ResourcesMainEjebSolrSearch;

/**
 * 
 * sunchao nj
 * 資料類型新增頁面
 * 2014/09/28
 */
public class ErmDbtypeComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6389863475361756604L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Textbox resIdBox;
	@Wire
	private Textbox dbtypeIdBox;
	@Wire
	private Textbox dbtypeNameBox;
	private ErmResourcesDbtype dbtype;
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
			dbtypeNameBox.setDisabled(false);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("資料類型進入新增頁面報錯",e);
		}
	}
	@Listen("onClick=#saveBtn")
	public void addResourcesDbws(){
		try {
			dbtype=new ErmResourcesDbtype();
			String dbtypeId=dbtypeIdBox.getValue();
			if (StringUtils.isBlank(dbtypeId)) {
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.dbtypeId") + " "
								+ Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				dbtypeIdBox.focus();
				return;
			}
			dbtype.setResourcesId(resIdBox.getValue());
			dbtype.setDbtypeId(dbtypeId);
			ErmResourcesDbtype dbtypeTemp=((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).getDbtype(resIdBox.getValue(), dbtypeId);
			if(!"".equals(dbtypeTemp.getDbtypeId())&&dbtypeTemp.getDbtypeId()!=null){
				ZkUtils.showExclamation(
						Labels.getLabel("ermTypeRes.dataExists"),
						Labels.getLabel("warn"));
				dbtypeIdBox.focus();
				return;
			}else{
				((ErmResourcesDbtypeService)SpringUtil.getBean("ermResourcesDbtypeService")).create(dbtype);
			}
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
			String resourcesId=dbtype.getResourcesId();
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
			log.error("資料類型新增方法報錯",e);
		}
	}
	@Listen("onClick=#selectBtn")
	public void openSearch(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("openValue", "DBTYPE");
		Executions.createComponents("/WEB-INF/pages/system/ermResMainDbws/ermResSubOpen.zul", this.getSelf(),map);
	}
}
