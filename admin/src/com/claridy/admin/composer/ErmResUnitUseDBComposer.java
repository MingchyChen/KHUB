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
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;
import com.claridy.facade.ErmResUnitUseService;

public class ErmResUnitUseDBComposer extends SelectorComposer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Wire
	private WebEmployee webEmployee;
	@Wire
	private ErmCodeDb ermCodeDb;
	@Wire
	private Textbox dataBase;
	@Wire
	private Listbox dataBaseLbx;
	@Wire
	private Window ermResUnitUseDBWin;
	@WireVariable
	private List<Object> ermResDBUseList;
	@WireVariable
	private List<ErmCodeDb> ermCodeDbList;

	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			// 獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			ermResDBUseList = ((ErmResUnitUseService) SpringUtil
					.getBean("ermResUnitUseService")).findAllDataBase();
			ListModelList<Object> tmpLML = new ListModelList();
			for (int i = 0; i < ermResDBUseList.size(); i++) {
				String dbId = ((String) ermResDBUseList.get(i));
				ermCodeDb = ((ErmResUnitUseService) SpringUtil
						.getBean("ermResUnitUseService"))
						.findDb(dbId);
				tmpLML.add(ermCodeDb);
			}
			tmpLML.setMultiple(true);
			dataBaseLbx.setModel(tmpLML);
		} catch (Exception e) {
			log.error(""+e);
		}
	}

	@Listen("onClick = #searchBtn")
	public void search() throws InterruptedException {
		try {
			if (dataBase != null && dataBase.getValue() != "") {
				ListModelList<Object> tmpLML = new ListModelList();
				String tempDb = dataBase.getValue();
				ermCodeDbList = ((ErmResUnitUseService) SpringUtil
						.getBean("ermResUnitUseService")).findedDb(tempDb);
				if (ermCodeDbList.size() > 0) {
					tmpLML.add(ermCodeDbList.get(0));
					tmpLML.setMultiple(true);
					dataBaseLbx.setModel(tmpLML);
				}else{
					tmpLML.setMultiple(true);
					dataBaseLbx.setModel(tmpLML);
				}
			} else {
				ermResDBUseList = ((ErmResUnitUseService) SpringUtil
						.getBean("ermResUnitUseService")).findAllDataBase();
				ListModelList<Object> tmpLML = new ListModelList();
				for (int i = 0; i < ermResDBUseList.size(); i++) {
					String dbId = ((String) ermResDBUseList.get(i));
					ermCodeDb = ((ErmResUnitUseService) SpringUtil
							.getBean("ermResUnitUseService"))
							.findDb(dbId);
					tmpLML.add(ermCodeDb);
				}
				tmpLML.setMultiple(true);
				dataBaseLbx.setModel(tmpLML);
			}
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}

	@Listen("onClick = #clearBtn")
	public void clear() throws InterruptedException {
		try {
			dataBase.setValue(null);
			search();
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	@Listen("onClick = #confirmBtn")
	public void confirm() throws InterruptedException {
		try {
			int count = dataBaseLbx.getSelectedCount();
			if (count > 0) {
				Set<Listitem> dataList = dataBaseLbx.getSelectedItems();
//				WebEmployee loginwebEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				ListModelList<String> WebOrgName=new ListModelList<String>();
				for(Listitem employee:dataList){
					ermCodeDb=employee.getValue();
					WebOrgName.add(ermCodeDb.getName());
				}
				Listbox dataBasesLbx =(Listbox)ermResUnitUseDBWin.getParent().getFellowIfAny("dataBasesLbx");
				dataBasesLbx.setVisible(true);
				dataBasesLbx.setModel(WebOrgName);
				ermResUnitUseDBWin.detach();
			} else {
				ZkUtils.showExclamation(
						Labels.getLabel("select"),
						Labels.getLabel("info"));
			}
		} catch (Exception e) {
			log.error("確認異常" + e);
		}
		
		
	}

}
