package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinionReply;
import com.claridy.facade.WebOpinionService;
import com.claridy.facade.WebSysLogService;

public class WebReplyListComposer extends SelectorComposer<Component>{

	private static final long serialVersionUID = -8472355162742043344L;
	@Wire
	protected WebEmployee webEmployee;
	@Wire
	protected WebOpinionReply webopinionreply;
	@Wire
	protected Textbox contentZhTwtbx;
	@Wire
	private Label namela;
	@Wire
	private Label employeela;
	@Wire
	private Label datela;
	@Wire
	protected CKeditor replyZhTwEdt;
	@Wire
	protected Window replynWin;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try{
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webopinionreply=((WebOpinionService)SpringUtil.getBean("webOpinionService")).findReply("uuid", uuid);
			if (webopinionreply != null && !"".equals(webopinionreply)) {
				namela.setValue(webopinionreply.getWebOpinion().getTitleZhTw());
				contentZhTwtbx.setValue(webopinionreply.getWebOpinion().getContentZhTw());
				replyZhTwEdt.setValue(webopinionreply.getReplyZhTw());
				employeela.setValue(webopinionreply.getReplyWebEmployee().getEmployeesn());
				datela.setValue(webopinionreply.getLatelyChangedDate().toString());
			}
		} catch (Exception e) {
			log.error("WebOpinionList初始化異常"+e);
		}
	}
	
	
	@Listen("onClick=#editBtn")
	public void updateReply() throws Exception {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			String replyZhTw="";
			webopinionreply.setReplyWebEmployee(webEmployee);

			if (replyZhTwEdt.getValue() != null
					&& !"".equals(replyZhTwEdt.getValue().trim())) {
				replyZhTw = replyZhTwEdt.getValue().toString().trim();
				webopinionreply.setReplyZhTw(replyZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("webopinionreply.replyZhTw")
						+ " " + Labels.getLabel("cannottNull"),
						Labels.getLabel("warn"));
				return;
			}
			webopinionreply.setLatelyChangedUser(webEmployee.getEmployeesn());
			webopinionreply.setLatelyChangedDate(new Date());
			webopinionreply.setIsDataEffid(1);
			((WebOpinionService) SpringUtil.getBean("webOpinionService"))
					.updateOpinionReply(webopinionreply);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(
					ZkUtils.getRemoteAddr(), webopinionreply.getWebEmployee().getEmployeesn(),
					"webopinionreply_" + webopinionreply.getWebOpinion().getUuid(),Labels.getLabel("opinion.cha"));

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			String url = "opinionreply/opinionReply.zul";
			ZkUtils.refurbishMethod(url);
			replynWin.detach();
		} catch (Exception e) {
			log.error("webopinionreply新增異常"+e);
		}
	}
}
