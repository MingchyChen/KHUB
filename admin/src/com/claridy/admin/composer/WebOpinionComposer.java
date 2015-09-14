package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.ckez.CKeditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.UUIDGenerator;
import com.claridy.common.util.XSSStringEncoder;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinion;
import com.claridy.domain.WebOpinionReply;
import com.claridy.facade.WebOpinionService;
import com.claridy.facade.WebSysLogService;

public class WebOpinionComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1907278458589137776L;

	@Wire
	private WebOpinion webopinion;
	@Wire
	private WebEmployee webEmployee;
	@Wire
	protected Textbox contentZhTwtbx;
	@Wire
	private Label namela;
	@Wire
	protected List<WebOpinionReply> webOpinionReplyList;
	@Wire
	protected Listbox webOpinionReplylbx;
	@Wire
	protected WebOpinionReply webopinionreply;
	@Wire
	protected CKeditor replyZhTwEdt;
	@Wire
	protected Window editWebOpinionWin;
	@Wire
	private int currentPage;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			String uuid = map.get("uuid");
			currentPage = Integer.parseInt(map.get("currentPage"));
			if (uuid != null) {
				webopinion = ((WebOpinionService) SpringUtil.getBean("webOpinionService")).findedtAddList("uuid", uuid).get(0);
				webOpinionReplyList = ((WebOpinionService) SpringUtil.getBean("webOpinionService")).findReplyList("webOpinion", uuid);
			}
			if (webopinion != null && !"".equals(webopinion)) {
				namela.setValue(webopinion.getTitleZhTw());
				contentZhTwtbx.setValue(webopinion.getContentZhTw());
			}
			ListModelList<WebOpinionReply> tmpLML = new ListModelList<WebOpinionReply>(webOpinionReplyList);
			tmpLML.setMultiple(true);
			webOpinionReplylbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error("webOpinion初始化異常" + e);
		}

	}

	@Listen("onClick=#editBtn")
	public void updateEmployee() throws Exception {
		try {
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			webopinionreply = new WebOpinionReply();
			webopinionreply.setUuid(UUIDGenerator.getUUID());
			String replyZhTw = "";
			Date date = new Date();

			webopinionreply.setWebOpinion(webopinion);
			webopinionreply.setWebEmployee(webEmployee);
			webopinionreply.setReplyWebEmployee(webEmployee);
			if (webEmployee.getWeborg() != null && webEmployee.getWeborg().getOrgId() != null && !"0".equals(webEmployee.getWeborg().getOrgId())) {
				webopinionreply.setDataOwnerGroup(webEmployee.getWeborg().getOrgId());
			} else {
				webopinionreply.setDataOwnerGroup(webEmployee.getParentWebOrg().getOrgId());
			}
			webopinionreply.setCreateDate(date);

			if (replyZhTwEdt.getValue() != null && !"".equals(replyZhTwEdt.getValue().trim())) {
				replyZhTw = replyZhTwEdt.getValue().toString().trim();
				webopinionreply.setReplyZhTw(replyZhTw);
			} else {
				ZkUtils.showExclamation(Labels.getLabel("opinion.now") + " " + Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				return;
			}
			webopinionreply.setLatelyChangedUser(webEmployee.getEmployeesn());
			webopinionreply.setLatelyChangedDate(new Date());
			webopinionreply.setIsDataEffid(1);
			((WebOpinionService) SpringUtil.getBean("webOpinionService")).addOpinionReply(webopinionreply);
			((WebSysLogService) SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), webopinionreply.getWebEmployee()
					.getEmployeesn(), "webopinionreply_" + webopinionreply.getWebOpinion().getUuid(), Labels.getLabel("opinion.new"));

			// 更新成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"), Labels.getLabel("info"));
			editSearchOrgList();
			editWebOpinionWin.detach();
		} catch (Exception e) {
			log.error("webopinionreply新增異常" + e);
		}
	}

	@Listen("onClick=.deleteTest")
	public void testA(Event en) throws Exception {
		try {
			final Button tmpButton = (Button) en.getTarget();
			// tmpButton=(Button) en.getTarget();
			ZkUtils.showQuestion(Labels.getLabel("sureDel"), Labels.getLabel("info"), new EventListener() {
				public void onEvent(Event event) {
					int clickButton = (Integer) event.getData();
					if (clickButton == Messagebox.OK) {

						// System.out.println("======================================");
						// System.out.println(tmpButton.getTarget());
						String uuid = tmpButton.getTarget();
						WebOpinionReply re = new WebOpinionReply();
						re = ((WebOpinionService) SpringUtil.getBean("webOpinionService")).findReply("uuid", uuid);
						re.setLatelyChangedDate(new Date());
						re.setIsDataEffid(0);
						if (re != null) {
							((WebOpinionService) SpringUtil.getBean("webOpinionService")).updateOpinionReply(re);
							((WebSysLogService) SpringUtil.getBean("webSysLogService")).saveOrUpdateLog(ZkUtils.getRemoteAddr(), re.getWebEmployee()
									.getEmployeesn(), "webopinionreply_" + re.getWebOpinion().getUuid(), Labels.getLabel("opinion.del"));
						}
						String url = "opinionreply/opinionReply.zul";
						ZkUtils.refurbishMethod(url);
						editWebOpinionWin.detach();
					}
				}

			});
		} catch (Exception e) {
			log.error("webopinionreply修改異常" + e);
		}
	}

	public void editSearchOrgList() {
		Textbox titleTxts = (Textbox) editWebOpinionWin.getParent().getFellowIfAny("titleTxt");
		Listbox webOpinionlbx = (Listbox) editWebOpinionWin.getParent().getFellowIfAny("webOpinionlbx");
		try {
			String titleTxt = titleTxts.getValue().trim();
			titleTxt = XSSStringEncoder.encodeXSSString(titleTxt);
			List<WebOpinion> webOpinionList = ((WebOpinionService) SpringUtil.getBean("webOpinionService")).findOpinionByTitle(webEmployee, titleTxt);
			ListModelList<WebOpinion> tmpLML = new ListModelList<WebOpinion>(webOpinionList);
			tmpLML.setMultiple(true);
			webOpinionlbx.setModel(tmpLML);
			webOpinionlbx.setActivePage(currentPage);
			titleTxts.setValue(titleTxt);
		} catch (Exception e) {
			log.error("webopinionreply集合出錯", e);
		}
	}
}
