package com.claridy.admin.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.claridy.common.util.RandomIDGenerator;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.ErmResourcesSuunit;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmCodeDbService;
import com.claridy.facade.ErmResourcesSuunitIdService;
import com.claridy.facade.WebOrgListService;

public class ErmCodeDbComposer extends SelectorComposer<Component> {
	@Wire
	private Textbox dbIdTxt;
	@Wire
	private Textbox dbNameTxt;
	@Wire
	private Textbox resourcesIdTxt;
	@Wire
	private Textbox resourcesNameTxt;
	@Wire
	private Radiogroup ezproxyRdo; 
	@Wire
	private Textbox urlTxt;
	@Wire
	private Listbox resMainSuunitLix;
	@Wire
	private Window addErmResDataBaseWin;
	@Wire
	private Button addSuunitBtn;
	@Wire
	private Button delSuunitBtn;
	private WebEmployee webEmployee;
	private String aDbId;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		webEmployee = (WebEmployee) ZkUtils
				.getSessionAttribute("webEmployee");
		Map<String, String> map = new HashMap<String, String>();
		map=ZkUtils.getExecutionArgs();
		String dbId=map.get("dbId");
		if(dbIdTxt.getValue()==null||dbIdTxt.getValue().equals("")){
			addSuunitBtn.setDisabled(true);
			delSuunitBtn.setDisabled(true);
		}
//		List<ErmResourcesSuunit> sunitList=new ArrayList<ErmResourcesSuunit>();
//		sunitList=((ErmResourcesSuunitIdService) SpringUtil
//				.getBean("ermResourcesSuunitIdService")).findBySuunitResId(dbId);
//		if (sunitList.size() > 0) {
//			ListModelList<ErmResourcesSuunit> listModel = new ListModelList<ErmResourcesSuunit>(sunitList);
//			listModel.setMultiple(true);
//			resMainSuunitLix.setModel(listModel);
//		}
		
	}

	@Listen("onClick=#resourcesDbwsBtn")
	public void searchPublisher() {
		openSearch();
	}

	public void openSearch() {
		Map<String, String> map = new HashMap<String, String>();
		Executions.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResMainDbwsOpen.zul",
				this.getSelf(), map);
	}

	@Listen("onClick=#saveBtn")
	public void addCodeDb(){
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = ZkUtils.getExecutionArgs();
			ErmCodeDb ermCodeDb=new ErmCodeDb();
			List<ErmCodeDb> codeDbList= ((ErmCodeDbService) SpringUtil
					.getBean("ermCodeDbService"))
					.findAllCodeDb(webEmployee);
			String tempId = "OB";
			Long number = (long) codeDbList.size();
			number = number + 1;
			String dbId = tempId + RandomIDGenerator.fmtLong(number, 9);
			ermCodeDb.setDbId(dbId);
			aDbId=dbId;
			dbIdTxt.setValue(dbId);
			if(dbNameTxt.getValue()!=null&&!"".equals(dbNameTxt.getValue())){
				ermCodeDb.setName(dbNameTxt.getValue());
			}else{
				ZkUtils.showExclamation(Labels.getLabel("webErwSource.nameZhTw") + " "
						+ Labels.getLabel("cannottNull"), Labels.getLabel("warn"));
				dbNameTxt.focus();
				return;
			}
			if(resourcesIdTxt.getValue()!=null){
				ermCodeDb.setResourcesId(resourcesIdTxt.getValue());
			}
			ermCodeDb.setHistory("N");
			if(ezproxyRdo.getSelectedItem().getValue()!=null){
				ermCodeDb.setEzproxy(ezproxyRdo.getSelectedItem().getValue().toString());
			}
			
			ermCodeDb.setIsDataEffid(1);
			ermCodeDb.setWebEmployee(webEmployee);
			ermCodeDb.setCreateDate(new Date());
			((ErmCodeDbService) SpringUtil
					.getBean("ermCodeDbService")).saveCodeDb(ermCodeDb);
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			dbIdTxt.setValue(ermCodeDb.getDbId());
			addSuunitBtn.setDisabled(false);
			delSuunitBtn.setDisabled(false);
			
		} catch (WrongValueException e) {
			log.error("新增資料庫異常"+e);
		}
		
	}
	@Listen("onClick=#addSuunitBtn")
	public void openAddSunnit(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("dbId", aDbId);
		Executions.createComponents(
				"/WEB-INF/pages/system/ermResMainEjeb/ermResSuunitAdd.zul",
				this.getSelf(), map);
	}//
	@Listen("onClick=#delSuunitBtn")
	public void delSunnit() {
		int count = resMainSuunitLix.getSelectedCount();
		if (count > 0) {
			// “你確定要刪除該資料嗎？”
			ZkUtils.showQuestion(Labels.getLabel("sureDel"),
					Labels.getLabel("info"), new EventListener<Event>() {

						public void onEvent(Event event) throws Exception {
							int ckickButton = (Integer) event.getData();
							if (ckickButton == Messagebox.OK) {
								Set<Listitem> listitems = resMainSuunitLix
										.getSelectedItems();
								WebEmployee loginwebEmployee = (WebEmployee) ZkUtils
										.getSessionAttribute("webEmployee");
								for (Listitem listitem : listitems) {
									ErmResourcesSuunit ermResourcesSuunit=listitem.getValue();
									((ErmResourcesSuunitIdService) SpringUtil
											.getBean("ermResourcesSuunitIdService")).delSunnit(ermResourcesSuunit);
								}
								flushList();
							}

						}
					});
		} else {
			// "請先選擇一筆資料"
			ZkUtils.showExclamation(Labels.getLabel("selectMultData"),
					Labels.getLabel("info"));
			return;
		}
		
	}
	public void flushList() {
		List<WebOrg> orgList=((WebOrgListService) SpringUtil
				.getBean("webOrgListService"))
				.findWebOrgParam("", "");
		List<ErmResourcesSuunit> sunnitList = ((ErmResourcesSuunitIdService) SpringUtil
				.getBean("ermResourcesSuunitIdService"))
				.findBySuunitResId(dbIdTxt.getValue());
		for (ErmResourcesSuunit ermResourcesSuunit : sunnitList) {
			for (int i = 0; i < orgList.size(); i++) {
				if(ermResourcesSuunit.getSuunitId().equals(orgList.get(i).getOrgId())){
					ermResourcesSuunit.setOrgName(orgList.get(i).getOrgName());
				}
			}
		}
		ListModelList<ErmResourcesSuunit> ermSunnitModel=new ListModelList<ErmResourcesSuunit>(sunnitList);
		ermSunnitModel.setMultiple(true);
		resMainSuunitLix.setModel(ermSunnitModel);
	}
}
