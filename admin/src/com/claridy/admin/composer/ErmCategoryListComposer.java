package com.claridy.admin.composer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeCategory;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmCategoryService;

/**
 * 
 * sunchao nj
 * 2014/07/26
 * 資源瀏覽/後分類管理
 */
public class ErmCategoryListComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 8994020132060546141L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	// systemSetting
	@Wire
	private Combobox resTypeCbx;
	@Wire
	private Radiogroup categoryTypeRdg;
	@Wire
	protected Listbox ermCategoryLbx;
	@WireVariable
	private ErmCodeCategory ermCodeCategory;
	private WebEmployee webEmployee;
	
	@Listen("onClick = #pagSearchBtn")
	public void pagSearch() {
		try {
			String resType = resTypeCbx.getSelectedItem().getValue();
			String categoryType=categoryTypeRdg.getSelectedItem().getValue();
			List<ErmCodeCategory> result = ((ErmCategoryService) SpringUtil
					.getBean("ermCategoryService")).findAll(resType, categoryType, webEmployee);
			ListModelList<ErmCodeCategory> tmpLML = new ListModelList<ErmCodeCategory>(
					result);
			tmpLML.setMultiple(true);
			ermCategoryLbx.setModel(tmpLML);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢資源瀏覽/候分類管理集合出錯",e);
		}
	}
	@Listen("onClick = #saveBtn")
	public void doSave() {
		try {
			List<Listitem> listItem=ermCategoryLbx.getItems();
			for(Listitem tmpItem:listItem){
				ermCodeCategory=tmpItem.getValue();
				if(tmpItem.isSelected()==true){
					ermCodeCategory.setHistory("N");
				}else{
					ermCodeCategory.setHistory("Y");
				}
				((ErmCategoryService) SpringUtil.getBean("ermCategoryService")).deleteWebOrg(ermCodeCategory);
			}
			// 存儲成功
			ZkUtils.showInformation(Labels.getLabel("saveOK"),Labels.getLabel("info"));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("保存資源瀏覽/候分類管理集合出錯",e);
		}
	}
	@Listen("onClick = #backBtn")
	public void doBack() {
		String url="home.zul";
		ZkUtils.refurbishMethod(url);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); 
		try {
			//獲取用戶資訊
			webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
			//查詢資源類別
			List<ErmCodeGeneralCode> generalCodeLst=((ErmCategoryService) SpringUtil
					.getBean("ermCategoryService")).findGeneralCodeByItemId("RETYPE");
			Comboitem com=new Comboitem();
			for(ErmCodeGeneralCode generalCodeTmp:generalCodeLst){
				com=new Comboitem();
				com.setLabel(generalCodeTmp.getName1());
				com.setValue(generalCodeTmp.getGeneralcodeId());
				resTypeCbx.appendChild(com);
			}
			resTypeCbx.setSelectedIndex(0);
			//初始頁面加載
			pagSearch();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查詢資源瀏覽/候分類管理集合出錯",e);
		}
	}
}

