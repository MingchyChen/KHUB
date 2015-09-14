package com.claridy.admin.composer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinion;
import com.claridy.domain.WebOpinionReply;
import com.claridy.facade.WebAccountService;
import com.claridy.facade.WebOpinionService;


public class WebOpinionListComposer extends SelectorComposer<Component>{
	
	private static final long serialVersionUID = 5341221994393084660L;
	
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected Listbox webOpinionlbx;
	@Wire
	protected List<WebOpinionReply> webOpinionReplyList;
	@Wire
	protected List<WebOpinion> webOpinionList;
	@Wire
	protected Textbox titleTxt;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		try{
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webOpinionList=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findOpinionInfoAll(webEmployee);
			ListModelList<WebOpinion> tmpLML = new ListModelList<WebOpinion>(
					webOpinionList);
			for (WebOpinion op : tmpLML) {
				List<WebOpinionReply> list=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findReplyList("webOpinion",op.getUuid());
				String type="";
				if(list.size()>0){
					type=Labels.getLabel("opinion.red");
				}else{
					type=Labels.getLabel("opinion.nre");
				}
				op.setType(type);
				if(op.getDataowner()!=null&&!"".equals(op.getDataowner())){
					WebAccount webAccount=((WebAccountService)SpringUtil.getBean("webAccountService")).getAccount(op.getDataowner());
					op.setCreateUser(webAccount.getNameZhTw());
				}
			}
			tmpLML.setMultiple(true);
			webOpinionlbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("WebOpinionList初始化異常"+e);
		}
	}
	
	@Listen("onClick = #showAll")
	public void searchAll(){
		try {
			webOpinionList=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findOpinionInfoAll(webEmployee);
			ListModelList<WebOpinion> tmpLML = new ListModelList<WebOpinion>(
					webOpinionList);
			for (WebOpinion op : tmpLML) {
				List<WebOpinionReply> list=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findReplyList("webOpinion",op.getUuid());
				String type="";
				if(list.size()>0){
					type=Labels.getLabel("opinion.red");
				}else{
					type=Labels.getLabel("opinion.nre");
				}
				op.setType(type);
				if(op.getDataowner()!=null&&!"".equals(op.getDataowner())){
					WebAccount webAccount=((WebAccountService)SpringUtil.getBean("webAccountService")).getAccount(op.getDataowner());
					op.setCreateUser(webAccount.getNameZhTw());
				}
			}
			tmpLML.setMultiple(true);
			webOpinionlbx.setModel(tmpLML);
			titleTxt.setValue("");
		} catch (WrongValueException e) {
			log.error("WebOpinionList顯示全部異常"+e);
		}
	}
	@Listen("onClick=#opinionBtn")
	public void findwebOpinionByNameZhTw(){
		String tempNameZhTw=titleTxt.getText().trim().toString();
		if (StringUtils.isBlank(tempNameZhTw)) {
			ZkUtils.showExclamation(Labels.getLabel("inputString"),
					Labels.getLabel("warn"));
			titleTxt.focus();
			return;
		}
		webOpinionList = ((WebOpinionService)SpringUtil.getBean("webOpinionService"))
				.findOpinionByTitle(webEmployee, tempNameZhTw);
		ListModelList<WebOpinion> tmpLML = new ListModelList<WebOpinion>(
				webOpinionList);
		for (WebOpinion op : tmpLML) {
			List<WebOpinionReply> list=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findReplyList("webOpinion",op.getUuid());
			String type="";
			if(list.size()>0){
				type=Labels.getLabel("opinion.red");
			}else{
				type=Labels.getLabel("opinion.nre");
			}
			op.setType(type);
			if(op.getDataowner()!=null&&!"".equals(op.getDataowner())){
				WebAccount webAccount=((WebAccountService)SpringUtil.getBean("webAccountService")).getAccount(op.getDataowner());
				op.setCreateUser(webAccount.getNameZhTw());
			}
		}
		tmpLML.setMultiple(true);
		webOpinionlbx.setModel(tmpLML);
	}

}
