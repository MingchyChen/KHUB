package com.claridy.admin.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebPhrase;
import com.claridy.facade.WebPhraseService;
/**
 * dongzhifu nj
 * 駁回原因清單列
 * 2014/8/20
 */
public class WebPhraseListComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8759730572405617804L;
	@Wire
	private Window rejectWin;
	@Wire
	private Textbox rejectKeyWord;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private Listbox rejectLix;
	@Wire
	private Label hidVal;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			Map<String,String> arg=new HashMap<String, String>();
			arg=ZkUtils.getExecutionArgs();
			hidVal.setValue(arg.get("uuid"));
			List<WebPhrase> webPhraseList=((WebPhraseService)SpringUtil.getBean("webPhraseService")).findAll();
			ListModelList<WebPhrase> model=new ListModelList<WebPhrase>(webPhraseList);
			rejectLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	@Listen("onClick=#showAllBtn")
	public void showAll(){
		try {
			Map<String,String> arg=new HashMap<String, String>();
			arg=ZkUtils.getExecutionArgs();
			hidVal.setValue(arg.get("uuid"));
			List<WebPhrase> webPhraseList=((WebPhraseService)SpringUtil.getBean("webPhraseService")).findAll();
			ListModelList<WebPhrase> model=new ListModelList<WebPhrase>(webPhraseList);
			rejectLix.setModel(model);
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick=#searchBtn")
	public void search(){
		try {
			if(rejectKeyWord.getValue()!=null&&!"".equals(rejectKeyWord.getValue())){
				String keyWord=rejectKeyWord.getValue().trim();
				keyWord=XSSStringEncoder.encodeXSSString(keyWord);
				List<WebPhrase> webPhraseList=((WebPhraseService)SpringUtil.getBean("webPhraseService")).findByKeyWord(keyWord);
				ListModelList<WebPhrase> model=new ListModelList<WebPhrase>(webPhraseList);
				rejectLix.setModel(model);
			}else{
				ZkUtils.showExclamation(Labels.getLabel("inputString"),Labels.getLabel("info"));
				return;
			}
		} catch (WrongValueException e) {
			log.error(e+"");
		}
	}
	
	
	@Listen("onClick=#isOk")
	public void insert(){
		try {
			if(rejectLix.getSelectedCount()>0){
				Listitem listitem=rejectLix.getSelectedItem();
				WebPhrase webPhrase=listitem.getValue();
				ZkUtils.setSessionAttribute("webPhraseUuid",  webPhrase.getUuid());
				Textbox rejectTbox=(Textbox) rejectWin.getParent().getFellowIfAny("rejectTbox");
				Textbox rejectUuidTbox=(Textbox) rejectWin.getParent().getFellowIfAny("rejectUuidTbox");
				rejectTbox.setValue(webPhrase.getPhraseZhTw());
				rejectUuidTbox.setValue(webPhrase.getUuid());
				if(rejectWin!=null){
					rejectWin.detach();
				}
			}
		} catch (Exception e) {
			log.error(""+e);
		}
	}
	
	

}
