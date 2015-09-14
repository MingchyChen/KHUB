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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.claridy.common.util.ZkUtils;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesConfig;
import com.claridy.domain.WebEmployee;
import com.claridy.facade.ErmResourcesConfigService;
import com.claridy.facade.WebSysLogService;

/**
 * 電子資源前段顯示設定
 * @author Souing
 *
 */
public class ErmResourcesConfigComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8362550725280704844L;
	
	@Wire
	private Checkbox titleCKbox;
	@Wire
	private Intbox titletbox;
	
	@Wire
	private Checkbox urlCKbox;
	@Wire
	private Intbox urltbox;
	@Wire
	private Checkbox urlTwoCKbox;
	@Wire
	private Intbox urlTwotbox;
	@Wire
	private Checkbox remarkCKbox;
	@Wire
	private Intbox remarktbox;
	@Wire
	private Checkbox starorderdateCKbox;
	@Wire
	private Intbox starorderdatetbox;
	@Wire
	private Checkbox endorderdateCKbox;
	@Wire
	private Intbox endorderdatetbox;
	@Wire
	private Checkbox coverageCKbox;
	@Wire
	private Intbox coveragetbox;
	@Wire
	private Checkbox regllipCKbox;
	@Wire
	private Intbox reglliptbox;
	@Wire
	private Checkbox idpwdCKbox;
	@Wire
	private Intbox idpwdtbox;
	@Wire
	private Checkbox othersCKbox;
	@Wire
	private Intbox othertbox;
	@Wire
	private Checkbox eriefCKbox;
	@Wire
	private Intbox erieftbox;
	@Wire
	private Checkbox eriefEngCKbox;
	@Wire
	private Intbox eriefEngtbox;
	@Wire
	private Checkbox languageidCKbox;
	@Wire 
	private Intbox languageidtbox;
	@Wire
	private Checkbox agentedidCKbox;
	@Wire
	private Intbox agentedidtbox;
	@Wire
	private Checkbox publisheridCKbox;
	@Wire
	private Intbox publisheridtbox;
	@Wire
	private Checkbox frenquencyCKbox;
	@Wire
	private Intbox frenquencytbox;
	@Wire 
	private Checkbox introCKbox;
	@Wire
	private Intbox introtbox;
	@Wire 
	private Checkbox concurCKbox;
	@Wire
	private Intbox concurtbox;
	@Wire
	private Checkbox connectidCKbox;
	@Wire
	private Intbox connectidtbox;
	@Wire
	private Checkbox coreCKbox;
	@Wire
	private Intbox coretbox;
	@Wire
	private Checkbox pubplaceCKbox;
	@Wire
	private Intbox pubplacetbox;
	@Wire
	private Checkbox dbidCKbox;
	@Wire
	private Intbox dbidtbox;
	@Wire
	private Checkbox publisherurlCKbox;
	@Wire
	private Intbox publisherurltbox;
	@Wire
	private Checkbox issnprintedCKbox;
	@Wire
	private Intbox issnprintedtbox;
	@Wire
	private Checkbox ssnonlineCKbox;
	@Wire
	private Intbox ssnonlinetbox;
	@Wire
	private Checkbox isbnprintedCKbox;
	@Wire
	private Intbox isbnprintedtbox;
	@Wire
	private Checkbox isbnonlineCKbox;
	@Wire
	private Intbox isbnonlinetbox;
	@Wire
	private Checkbox embargoCKbox;
	@Wire
	private Intbox embargotbox;
	@Wire
	private Checkbox cnCKbox;
	@Wire
	private Intbox cntbox;
	@Wire
	private Checkbox callnCKbox;
	@Wire
	private Intbox callntbox;
	@Wire
	private Checkbox eholdingsCKbox;
	@Wire
	private Intbox eholdingstbox;
	@Wire
	private Checkbox libaryMoneyCKbox;
	@Wire
	private Intbox libaryMoneytbox;
	@Wire
	private Checkbox imgurlCKbox;
	@Wire
	private Intbox imgurltbox;
	@Wire
	private Checkbox authorCKbox;
	@Wire
	private Intbox authortbox;
	@Wire
	private Checkbox placeidCKbox;
	@Wire
	private Intbox placeidtbox;
	@Wire
	private Checkbox versionCKbox;
	@Wire
	private Intbox versiontbox;
	@Wire
	private Checkbox relatedtitleCKbox;
	@Wire
	private Intbox relatedtitletbox;
	@Wire
	private Checkbox subjectCKbox;
	@Wire
	private Intbox subjecttbox;
	@Wire
	private Checkbox typeCKbox;
	@Wire
	private Intbox typetbox;
	@Wire
	private Checkbox suitCollegeCKbox;
	@Wire
	private Intbox suitCollegetbox;
	@Wire
	private Checkbox suitdepCKbox;
	@Wire
	private Intbox suitdeptbox;
	@Wire
	private Checkbox orderCollegeCKbox;
	@Wire
	private Intbox orderCollegetbox;
	@Wire
	private Checkbox orderdepCKbox;
	@Wire
	private Intbox orderdeptbox;
	@Wire
	private Checkbox uploadfileCKbox;
	@Wire
	private Intbox uploadfiletbox;
	@Wire
	private Row publisherrow;
	@Wire
	private Row corerow;
	@Wire
	private Row dbidrow;
	@Wire
	private Row publisherurlrow;
	@Wire
	private Row embargorow;
	@Wire
	private Row issnprintedrow;
	@Wire
	private Row ssnonlinerow;
	@Wire
	private Row eholdingsrow;
	@Wire
	private Row isbnprintedrow;
	@Wire
	private Row isbnonlinerow;
	@Wire
	private Row cnrow;
	@Wire
	private Row callnrow;
	@Wire
	private Row authorrow;
	@Wire
	private Row placeidrow;
	@Wire
	private Row versionrow;
	@Wire
	private Row typerow;
	@Wire
	private Row suitdeprow;
	@Wire
	private Row orderColleerow;
	@Wire
	private Row orderdeprow;
	@Wire
	private Window ermResourcesConfigEditWin;
	@Wire
	private Combobox nameCBox;
	@Wire
	private Window ermResourcesConfigAddWin;
	
	ErmResourcesConfig ermResourcesConfig=null;

	private final Logger log = LoggerFactory.getLogger(getClass());
	WebEmployee webEmployee=(WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		try {
			super.doAfterCompose(comp);
			Map<String,String> map=new HashMap<String, String>();
			map=ZkUtils.getExecutionArgs();
			String uuid=map.get("uuid");
			if(uuid!=null&&!uuid.equals("0")){
				ermResourcesConfig=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findByTypeId(webEmployee, uuid);
			}
			if(nameCBox!=null){
				List<ErmCodeGeneralCode> ermCodegeneralCodeAll=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findCodeAll(webEmployee);
				Comboitem com;
				for(ErmCodeGeneralCode ermCodeGeneralCode:ermCodegeneralCodeAll){
					com=new Comboitem();
					com.setLabel(ermCodeGeneralCode.getName1());
					com.setValue(ermCodeGeneralCode.getGeneralcodeId());
					nameCBox.appendChild(com);
				}
				nameCBox.setSelectedIndex(0);
				if(nameCBox.getSelectedItem().getValue().equals("EJ")||nameCBox.getSelectedItem().getValue().equals("EB")||nameCBox.getSelectedItem().getValue().equals("DB")){
					libaryMoneyCKbox.setVisible(true);
					libaryMoneytbox.setVisible(true);
					placeidrow.setVisible(true);
				}
				if(nameCBox.getSelectedItem().getValue().equals("EB")||nameCBox.getSelectedItem().getValue().equals("WS")){
					versionrow.setVisible(true);
				}
				if(nameCBox.getSelectedItem().getValue().equals("DB")){
					typerow.setVisible(true);
				}
				if(nameCBox.getSelectedItem().getValue().equals("EJ")||nameCBox.getSelectedItem().getValue().equals("EB")){
					publisherrow.setVisible(true);
					corerow.setVisible(true);
					dbidrow.setVisible(true);
					publisherurlrow.setVisible(true);
					embargorow.setVisible(true);
					suitdeprow.setVisible(true);
					orderColleerow.setVisible(true);
					orderdeprow.setVisible(true);
				}
				if(nameCBox.getSelectedItem().getValue().equals("EJ")){
					issnprintedrow.setVisible(true);
					ssnonlinerow.setVisible(true);
					eholdingsrow.setVisible(true);
				}
				if(nameCBox.getSelectedItem().getValue().equals("EB")){
					isbnprintedrow.setVisible(true);
					isbnonlinerow.setVisible(true);
					cnrow.setVisible(true);
					callnrow.setVisible(true);
					authorrow.setVisible(true);
				}
			}
			if(ermResourcesConfig!=null){
				if(ermResourcesConfig.getTitle()!=0){
					titleCKbox.setChecked(true);
					titletbox.setValue(ermResourcesConfig.getTitle());
				}
				if(ermResourcesConfig.getUrl1()!=0){
					urlCKbox.setChecked(true);
					urltbox.setValue(ermResourcesConfig.getUrl1());
				}
				if(ermResourcesConfig.getUrl2()!=0){
					urlTwoCKbox.setChecked(true);
					urlTwotbox.setValue(ermResourcesConfig.getUrl2());
				}
				if(ermResourcesConfig.getRemarkId()!=0){
					remarkCKbox.setChecked(true);
					remarktbox.setValue(ermResourcesConfig.getRemarkId());
				}
				if(ermResourcesConfig.getStarOrderDate()!=0){
					starorderdateCKbox.setChecked(true);
					starorderdatetbox.setValue(ermResourcesConfig.getStarOrderDate());
				}
				if(ermResourcesConfig.getEndOrderDate()!=0){
					endorderdateCKbox.setChecked(true);
					endorderdatetbox.setValue(ermResourcesConfig.getEndOrderDate());
				}
				if(ermResourcesConfig.getCoverage()!=0){
					coverageCKbox.setChecked(true);
					coveragetbox.setValue(ermResourcesConfig.getCoverage());
				}
				if(ermResourcesConfig.getRegllyIp()!=0){
					regllipCKbox.setChecked(true);
					reglliptbox.setValue(ermResourcesConfig.getRegllyIp());
				}
				if(ermResourcesConfig.getIdpwd()!=0){
					idpwdCKbox.setChecked(true);
					idpwdtbox.setValue(ermResourcesConfig.getIdpwd());
				}
				if(ermResourcesConfig.getOthers()!=0){
					othersCKbox.setChecked(true);
					othertbox.setValue(ermResourcesConfig.getOthers());
				}
				if(ermResourcesConfig.getBrief1()!=0){
					eriefCKbox.setChecked(true);
					erieftbox.setValue(ermResourcesConfig.getBrief1());
				}
				if(ermResourcesConfig.getBrief2()!=0){
					eriefEngCKbox.setChecked(true);
					eriefEngtbox.setValue(ermResourcesConfig.getBrief2());
				}
				if(ermResourcesConfig.getLanguageId()!=0){
					languageidCKbox.setChecked(true);
					languageidtbox.setValue(ermResourcesConfig.getLanguageId());
				}
				if(ermResourcesConfig.getAgentedId()!=0){
					agentedidCKbox.setChecked(true);
					agentedidtbox.setValue(ermResourcesConfig.getAgentedId());
				}
				if(ermResourcesConfig.getPublisherId()!=0){
					publisheridCKbox.setChecked(true);
					publisheridtbox.setValue(ermResourcesConfig.getPublisherId());
				}
				if(ermResourcesConfig.getFrenquency()!=0){
					frenquencyCKbox.setChecked(true);
					frenquencytbox.setValue(ermResourcesConfig.getFrenquency());
				}
				if(ermResourcesConfig.getIntro()!=0){
					introCKbox.setChecked(true);
					introtbox.setValue(ermResourcesConfig.getIntro());
				}
				if(ermResourcesConfig.getConcur()!=0){
					concurCKbox.setChecked(true);
					concurtbox.setValue(ermResourcesConfig.getConcur());
				}
				if(ermResourcesConfig.getConnectId()!=0){
					connectidCKbox.setChecked(true);
					connectidtbox.setValue(ermResourcesConfig.getConnectId());
				}
				if(ermResourcesConfig.getCore()!=0){
					coreCKbox.setChecked(true);
					coretbox.setValue(ermResourcesConfig.getCore());
				}
				if(ermResourcesConfig.getPubPlace()!=0){
					pubplaceCKbox.setChecked(true);
					pubplacetbox.setValue(ermResourcesConfig.getPubPlace());
				}
				if(ermResourcesConfig.getDbId()!=0){
					dbidCKbox.setChecked(true);
					dbidtbox.setValue(ermResourcesConfig.getDbId());
				}
				if(ermResourcesConfig.getPublisherurl()!=0){
					publisherurlCKbox.setChecked(true);
					publisherurltbox.setValue(ermResourcesConfig.getPublisherurl());
				}
				if(ermResourcesConfig.getIssnprinted()!=0){
					issnprintedCKbox.setChecked(true);
					issnprintedtbox.setValue(ermResourcesConfig.getIssnprinted());
				}
				if(ermResourcesConfig.getIssnonline()!=0){
					ssnonlineCKbox.setChecked(true);
					ssnonlinetbox.setValue(ermResourcesConfig.getIssnonline());
				}
				if(ermResourcesConfig.getIsbnprinted()!=0){
					isbnprintedCKbox.setChecked(true);
					isbnprintedtbox.setValue(ermResourcesConfig.getIsbnprinted());
				}
				if(ermResourcesConfig.getIsbnonline()!=0){
					isbnonlineCKbox.setChecked(true);
					isbnonlinetbox.setValue(ermResourcesConfig.getIsbnonline());
				}
				if(ermResourcesConfig.getEmbargo()!=0){
					embargoCKbox.setChecked(true);
					embargotbox.setValue(ermResourcesConfig.getEmbargo());
				}
				if(ermResourcesConfig.getCn()!=0){
					cnCKbox.setChecked(true);
					cntbox.setValue(ermResourcesConfig.getCn());
				}
				if(ermResourcesConfig.getCalln()!=0){
					callnCKbox.setChecked(true);
					callntbox.setValue(ermResourcesConfig.getCalln());
				}
				if(ermResourcesConfig.getEholdings()!=0){
					eholdingsCKbox.setChecked(true);
					eholdingstbox.setValue(ermResourcesConfig.getEholdings());
				}
				if(ermResourcesConfig.getLibaryMoney()!=0){
					libaryMoneyCKbox.setChecked(true);
					libaryMoneytbox.setValue(ermResourcesConfig.getLibaryMoney());
				}
				if(ermResourcesConfig.getImgurl()!=0){
					imgurlCKbox.setChecked(true);
					imgurltbox.setValue(ermResourcesConfig.getImgurl());
				}
				if(ermResourcesConfig.getAuthor()!=0){
					authorCKbox.setChecked(true);
					authortbox.setValue(ermResourcesConfig.getAuthor());
				}
				if(ermResourcesConfig.getPlaceId()!=0){
					placeidCKbox.setChecked(true);
					placeidtbox.setValue(ermResourcesConfig.getPlaceId());
				}
				if(ermResourcesConfig.getVersion()!=0){
					versionCKbox.setChecked(true);
					versiontbox.setValue(ermResourcesConfig.getVersion());
				}
				if(ermResourcesConfig.getRelatedTitle()!=0){
					relatedtitleCKbox.setChecked(true);
					relatedtitletbox.setValue(ermResourcesConfig.getRelatedTitle());
				}
				if(ermResourcesConfig.getSubject()!=0){
					subjectCKbox.setChecked(true);
					subjecttbox.setValue(ermResourcesConfig.getSubject());
				}
				
				if(ermResourcesConfig.getType()!=0){
					typeCKbox.setChecked(true);
					typetbox.setValue(ermResourcesConfig.getType());
				}
				if(ermResourcesConfig.getSuitCollege()!=0){
					suitCollegeCKbox.setChecked(true);
					suitCollegetbox.setValue(ermResourcesConfig.getSuitCollege());
				}
				if(ermResourcesConfig.getSuitDep()!=0){
					suitdepCKbox.setChecked(true);
					suitdeptbox.setValue(ermResourcesConfig.getSuitDep());
				}
				if(ermResourcesConfig.getOrderCollege()!=0){
					orderCollegeCKbox.setChecked(true);
					orderCollegetbox.setValue(ermResourcesConfig.getOrderCollege());
				}
				if(ermResourcesConfig.getOrderDep()!=0){
					orderdepCKbox.setChecked(true);
					orderdeptbox.setValue(ermResourcesConfig.getOrderDep());
				}
				if(ermResourcesConfig.getUploadFile()!=0){
					uploadfileCKbox.setChecked(true);
					uploadfiletbox.setValue(ermResourcesConfig.getUploadFile());
				}
				if(uuid.equals("EJ")||uuid.equals("EB")||uuid.equals("DB")){
					libaryMoneyCKbox.setVisible(true);
					libaryMoneytbox.setVisible(true);
					placeidrow.setVisible(true);
				}
				if(uuid.equals("EB")||uuid.equals("WS")){
					versionrow.setVisible(true);
				}
				if(uuid.equals("DB")){
					typerow.setVisible(true);
				}
				if(uuid.equals("EJ")||uuid.equals("EB")){
					publisherrow.setVisible(true);
					corerow.setVisible(true);
					dbidrow.setVisible(true);
					publisherurlrow.setVisible(true);
					embargorow.setVisible(true);
					suitdeprow.setVisible(true);
					orderColleerow.setVisible(true);
					orderdeprow.setVisible(true);
				}
				if(uuid.equals("EJ")){
					issnprintedrow.setVisible(true);
					ssnonlinerow.setVisible(true);
					eholdingsrow.setVisible(true);
				}
				if(uuid.equals("EB")){
					isbnprintedrow.setVisible(true);
					isbnonlinerow.setVisible(true);
					cnrow.setVisible(true);
					callnrow.setVisible(true);
					authorrow.setVisible(true);
				}
			}
			
			
			
		} catch (Exception e) {
			log.error(""+e);
		}
		
	}
	@Listen("onChange=#nameCBox")
	public void select(){
		String uuid=nameCBox.getSelectedItem().getValue();
		if(uuid.equals("EJ")||uuid.equals("EB")||uuid.equals("DB")){
			libaryMoneyCKbox.setVisible(true);
			libaryMoneytbox.setVisible(true);
			placeidrow.setVisible(true);
		}else{
			libaryMoneyCKbox.setVisible(false);
			libaryMoneytbox.setVisible(false);
			placeidrow.setVisible(false);
		}
		if(uuid.equals("EB")||uuid.equals("WS")){
			versionrow.setVisible(true);
		}else{
			versionrow.setVisible(false);
		}
		if(uuid.equals("DB")){
			typerow.setVisible(true);
		}else{
			typerow.setVisible(false);
		}
		if(uuid.equals("EJ")||uuid.equals("EB")){
			publisherrow.setVisible(true);
			corerow.setVisible(true);
			dbidrow.setVisible(true);
			publisherurlrow.setVisible(true);
			embargorow.setVisible(true);
			suitdeprow.setVisible(true);
			orderColleerow.setVisible(true);
			orderdeprow.setVisible(true);
		}else{

			publisherrow.setVisible(false);
			corerow.setVisible(false);
			dbidrow.setVisible(false);
			publisherurlrow.setVisible(false);
			embargorow.setVisible(false);
			suitdeprow.setVisible(false);
			orderColleerow.setVisible(false);
			orderdeprow.setVisible(false);
		}
		if(uuid.equals("EJ")){
			issnprintedrow.setVisible(true);
			ssnonlinerow.setVisible(true);
			eholdingsrow.setVisible(true);
		}else{
			issnprintedrow.setVisible(false);
			ssnonlinerow.setVisible(false);
			eholdingsrow.setVisible(false);
		}
		if(uuid.equals("EB")){
			isbnprintedrow.setVisible(true);
			isbnonlinerow.setVisible(true);
			cnrow.setVisible(true);
			callnrow.setVisible(true);
			authorrow.setVisible(true);
		}else{
			isbnprintedrow.setVisible(false);
			isbnonlinerow.setVisible(false);
			cnrow.setVisible(false);
			callnrow.setVisible(false);
			authorrow.setVisible(false);
		}
	}
	
	@Listen("onClick=#updateBtn")
	public void update(){
		try {
			if(titleCKbox.isChecked()){
				if(titletbox.getValue()!=null&&!"0".equals(titletbox.getText())){
					ermResourcesConfig.setTitle(titletbox.getValue());
				}else if(titletbox.getValue()!=null&&titletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.title")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.title")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setTitle(0);
			}
			if(urlCKbox.isChecked()){
				if(urltbox.getValue()!=null&&!"0".equals(urltbox.getText())){
					ermResourcesConfig.setUrl1(urltbox.getValue());
				}else if(urltbox.getValue()!=null&&urltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig。url1")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig。url1")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setUrl1(0);
			}
			if(urlTwoCKbox.isChecked()){
				if(urlTwotbox.getValue()!=null&&!"0".equals(urlTwotbox.getText())){
					ermResourcesConfig.setUrl2(urlTwotbox.getValue());
				}else if(urlTwotbox.getValue()!=null&&urlTwotbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.url2")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.url2")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setUrl2(0);
			}
			if(remarkCKbox.isChecked()){
				if(remarktbox.getValue()!=null&&!"0".equals(remarktbox.getText())){
					ermResourcesConfig.setRemarkId(remarktbox.getValue());
				}else if(remarktbox.getValue()!=null&&remarktbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.remark")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.remark")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setRemarkId(0);
			}
			if(starorderdateCKbox.isChecked()){
				if(starorderdatetbox.getValue()!=null&&!"0".equals(starorderdatetbox.getText())){
					ermResourcesConfig.setStarOrderDate(starorderdatetbox.getValue());
				}else if(starorderdatetbox.getValue()!=null&&starorderdatetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.starorderdate")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.starorderdate")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setStarOrderDate(0);
			}
			if(endorderdateCKbox.isChecked()){
				if(endorderdatetbox.getValue()!=null&&!"0".equals(endorderdatetbox.getText())){
					ermResourcesConfig.setEndOrderDate(endorderdatetbox.getValue());
				}else if(endorderdatetbox.getValue()!=null&&endorderdatetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.endorderdate")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.endorderdate")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setEndOrderDate(0);
			}
			if(coverageCKbox.isChecked()){
				if(coveragetbox.getValue()!=null&&!"0".equals(coveragetbox.getText())){
					ermResourcesConfig.setCoverage(coveragetbox.getValue());
				}else if(coveragetbox.getValue()!=null&&coveragetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.coverage")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.coverage")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setCoverage(0);
			}
			if(regllipCKbox.isChecked()){
				if(reglliptbox.getValue()!=null&&!"0".equals(reglliptbox.getText())){
					ermResourcesConfig.setRegllyIp(reglliptbox.getValue());
				}else if(reglliptbox.getValue()!=null&&reglliptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.regllyip")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.regllyip")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setRegllyIp(0);
			}
			if(idpwdCKbox.isChecked()){
				if(idpwdtbox.getValue()!=null&&!"0".equals(idpwdtbox.getText())){
					ermResourcesConfig.setIdpwd(idpwdtbox.getValue());
				}else if(idpwdtbox.getValue()!=null&&idpwdtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.idpwd")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.idpwd")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIdpwd(0);
			}
			if(othersCKbox.isChecked()){
				if(othertbox.getValue()!=null&&!"0".equals(othertbox.getText())){
					ermResourcesConfig.setOthers(othertbox.getValue());
				}else if(othertbox.getValue()!=null&&othertbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.others")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.others")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setOthers(0);
			}
			if(eriefCKbox.isChecked()){
				if(erieftbox.getValue()!=null&&!"0".equals(erieftbox.getText())){
					ermResourcesConfig.setBrief1(erieftbox.getValue());
				}else if(erieftbox.getValue()!=null&&erieftbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief1")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief1")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setBrief1(0);
			}
			if(eriefEngCKbox.isChecked()){
				if(eriefEngtbox.getValue()!=null&&!"0".equals(eriefEngtbox.getText())){
					ermResourcesConfig.setBrief2(eriefEngtbox.getValue());
				}else if(eriefEngtbox.getValue()!=null&&eriefEngtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief2")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief2")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setBrief2(0);
			}
			if(languageidCKbox.isChecked()){
				if(languageidtbox.getValue()!=null&&!"0".equals(languageidtbox.getText())){
					ermResourcesConfig.setLanguageId(languageidtbox.getValue());
				}else if(languageidtbox.getValue()!=null&&languageidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.languageid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.languageid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setLanguageId(0);
			}
			if(agentedidCKbox.isChecked()){
				if(agentedidtbox.getValue()!=null&&!"0".equals(agentedidtbox.getText())){
					ermResourcesConfig.setAgentedId(agentedidtbox.getValue());
				}else if(agentedidtbox.getValue()!=null&&agentedidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.agentedid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.agentedid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setAgentedId(0);
			}
			if(publisheridCKbox.isChecked()){
				if(publisheridtbox.getValue()!=null&&!"0".equals(publisheridtbox.getText())){
					ermResourcesConfig.setPublisherId(publisheridtbox.getValue());
				}else if(publisheridtbox.getValue()!=null&&publisheridtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setPublisherId(0);
			}
			if(frenquencyCKbox.isChecked()){
				if(frenquencytbox.getValue()!=null&&!"0".equals(frenquencytbox.getText())){
					ermResourcesConfig.setFrenquency(frenquencytbox.getValue());
				}else if(frenquencytbox.getValue()!=null&&frenquencytbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.frenquency")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.frenquency")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setFrenquency(0);
			}
			if(introCKbox.isChecked()){
				if(introtbox.getValue()!=null&&!"0".equals(introtbox.getText())){
					ermResourcesConfig.setIntro(introtbox.getValue());
				}else if(introtbox.getValue()!=null&&introtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.intro")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.intro")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIntro(0);
			}
			if(concurCKbox.isChecked()){
				if(concurtbox.getValue()!=null&&!"0".equals(concurtbox.getText())){
					ermResourcesConfig.setConcur(concurtbox.getValue());
				}else if(concurtbox.getValue()!=null&&concurtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.concur")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.concur")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setConcur(0);
			}
			if(connectidCKbox.isChecked()){
				if(connectidtbox.getValue()!=null&&!"0".equals(connectidtbox.getText())){
					ermResourcesConfig.setConnectId(connectidtbox.getValue());
				}else if(connectidtbox.getValue()!=null&&connectidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.connectid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.connectid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setConnectId(0);
			}
			if(coreCKbox.isChecked()){
				if(coretbox.getValue()!=null&&!"0".equals(coretbox.getText())){
					ermResourcesConfig.setCore(coretbox.getValue());
				}else if(coretbox.getValue()!=null&&coretbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.core")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.core")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setCore(0);
			}
			if(pubplaceCKbox.isChecked()){
				if(pubplacetbox.getValue()!=null&&!"0".equals(pubplacetbox.getText())){
					ermResourcesConfig.setPubPlace(pubplacetbox.getValue());
				}else if(pubplacetbox.getValue()!=null&&pubplacetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.pubplace")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.pubplace")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setPubPlace(0);
			}
			if(dbidCKbox.isChecked()){
				if(dbidtbox.getValue()!=null&&!"0".equals(dbidtbox.getText())){
					ermResourcesConfig.setDbId(dbidtbox.getValue());
				}else if(dbidtbox.getValue()!=null&&dbidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.dbid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.dbid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setDbId(0);
			}
			if(publisherurlCKbox.isChecked()){
				if(publisherurltbox.getValue()!=null&&!"0".equals(publisherurltbox.getText())){
					ermResourcesConfig.setPublisherurl(publisherurltbox.getValue());
				}else if(publisherurltbox.getValue()!=null&&publisherurltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherurl")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherurl")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setPublisherurl(0);
			}
			if(issnprintedCKbox.isChecked()){
				if(issnprintedtbox.getValue()!=null&&!"0".equals(issnprintedtbox.getText())){
					ermResourcesConfig.setIssnprinted(issnprintedtbox.getValue());
				}else if(issnprintedtbox.getValue()!=null&&issnprintedtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.issnprinted")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.issnprinted")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIssnprinted(0);
			}
			if(ssnonlineCKbox.isChecked()){
				if(ssnonlinetbox.getValue()!=null&&!"0".equals(ssnonlinetbox.getText())){
					ermResourcesConfig.setIssnonline(ssnonlinetbox.getValue());
				}else if(ssnonlinetbox.getValue()!=null&&ssnonlinetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.ssnonline")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.ssnonline")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIssnonline(0);
			}
			if(isbnprintedCKbox.isChecked()){
				if(isbnprintedtbox.getValue()!=null&&!"0".equals(isbnprintedtbox.getText())){
					ermResourcesConfig.setIsbnprinted(isbnprintedtbox.getValue());
				}else if(isbnprintedtbox.getValue()!=null&&isbnprintedtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnprinted")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnprinted")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIsbnprinted(0);
			}
			if(isbnonlineCKbox.isChecked()){
				if(isbnonlinetbox.getValue()!=null&&!"0".equals(isbnonlinetbox.getText())){
					ermResourcesConfig.setIsbnonline(isbnonlinetbox.getValue());
				}else if(isbnonlinetbox.getValue()!=null&&isbnonlinetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnonline")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnonline")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setIsbnonline(0);
			}
			if(embargoCKbox.isChecked()){
				if(embargotbox.getValue()!=null&&!"0".equals(embargotbox.getText())){
					ermResourcesConfig.setEmbargo(embargotbox.getValue());
				}else if(embargotbox.getValue()!=null&&embargotbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.embargo")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.embargo")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setEmbargo(0);
			}
			if(cnCKbox.isChecked()){
				if(cntbox.getValue()!=null&&!"0".equals(cntbox.getText())){
					ermResourcesConfig.setCn(cntbox.getValue());
				}else if(cntbox.getValue()!=null&&cntbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.cn")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.cn")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setCn(0);
			}
			if(callnCKbox.isChecked()){
				if(callntbox.getValue()!=null&&!"0".equals(callntbox.getText())){
					ermResourcesConfig.setCalln(callntbox.getValue());
				}else if(callntbox.getValue()!=null&&callntbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.calln")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.calln")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setCalln(0);
			}
			if(eholdingsCKbox.isChecked()){
				if(eholdingstbox.getValue()!=null&&!"0".equals(eholdingstbox.getText())){
					ermResourcesConfig.setEholdings(eholdingstbox.getValue());
				}else if(eholdingstbox.getValue()!=null&&eholdingstbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.eholdings")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.eholdings")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setEholdings(0);
			}
			if(libaryMoneyCKbox.isChecked()){
				if(libaryMoneytbox.getValue()!=null&&!"0".equals(libaryMoneytbox.getText())){
					ermResourcesConfig.setLibaryMoney(libaryMoneytbox.getValue());
				}else if(libaryMoneytbox.getValue()!=null&&libaryMoneytbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.libaryMoney")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.libaryMoney")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setLibaryMoney(0);
			}
			if(imgurlCKbox.isChecked()){
				if(imgurltbox.getValue()!=null&&!"0".equals(imgurltbox.getText())){
					ermResourcesConfig.setImgurl(imgurltbox.getValue());
				}else if(imgurltbox.getValue()!=null&&imgurltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.imgurl")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.imgurl")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setImgurl(0);
			}
			if(authorCKbox.isChecked()){
				if(authortbox.getValue()!=null&&!"0".equals(authortbox.getText())){
					ermResourcesConfig.setAuthor(authortbox.getValue());
				}else if(authortbox.getValue()!=null&&authortbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.author")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.author")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setAuthor(0);
			}
			if(placeidCKbox.isChecked()){
				if(placeidtbox.getValue()!=null&&!"0".equals(placeidtbox.getText())){
					ermResourcesConfig.setPlaceId(placeidtbox.getValue());
				}else if(placeidtbox.getValue()!=null&&placeidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.placeid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.placeid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setPlaceId(0);
			}
			if(versionCKbox.isChecked()){
				if(versiontbox.getValue()!=null&&!"0".equals(versiontbox.getText())){
					ermResourcesConfig.setVersion(versiontbox.getValue());
				}else if(versiontbox.getValue()!=null&&versiontbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.version")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.version")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setVersion(0);
			}
			if(relatedtitleCKbox.isChecked()){
				if(relatedtitletbox.getValue()!=null&&!"0".equals(relatedtitletbox.getText())){
					ermResourcesConfig.setRelatedTitle(relatedtitletbox.getValue());
				}else if(relatedtitletbox.getValue()!=null&&relatedtitletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.relatedtitle")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.relatedtitle")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setRelatedTitle(0);
			}
			if(subjectCKbox.isChecked()){
				if(subjecttbox.getValue()!=null&&!"0".equals(subjecttbox.getText())){
					ermResourcesConfig.setSubject(subjecttbox.getValue());
				}else if(subjecttbox.getValue()!=null&&subjecttbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.subject")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.subject")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setSubject(0);
			}
			if(typeCKbox.isChecked()){
				if(typetbox.getValue()!=null&&!"0".equals(typetbox.getText())){
					ermResourcesConfig.setType(typetbox.getValue());
				}else if(typetbox.getValue()!=null&&typetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.type")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.type")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setType(0);
			}
			if(suitCollegeCKbox.isChecked()){
				if(suitCollegetbox.getValue()!=null&&!"0".equals(suitCollegetbox.getText())){
					ermResourcesConfig.setSuitCollege(suitCollegetbox.getValue());
				}else if(suitCollegetbox.getValue()!=null&&suitCollegetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitCollege")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitCollege")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setSuitCollege(0);
			}
			if(suitdepCKbox.isChecked()){
				if(suitdeptbox.getValue()!=null&&!"0".equals(suitdeptbox.getText())){
					ermResourcesConfig.setSuitDep(suitdeptbox.getValue());
				}else if(suitdeptbox.getValue()!=null&&suitdeptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitdep")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitdep")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setSuitDep(0);
			}
			if(orderCollegeCKbox.isChecked()){
				if(orderCollegetbox.getValue()!=null&&!"0".equals(orderCollegetbox.getText())){
					ermResourcesConfig.setOrderCollege(orderCollegetbox.getValue());
				}else if(orderCollegetbox.getValue()!=null&&orderCollegetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderCollege")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderCollege")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setOrderCollege(0);
			}
			if(orderdepCKbox.isChecked()){
				if(orderdeptbox.getValue()!=null&&!"0".equals(orderdeptbox.getText())){
					ermResourcesConfig.setOrderDep(orderdeptbox.getValue());
				}else if(orderdeptbox.getValue()!=null&&orderdeptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderdep")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderdep")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setOrderDep(0);
			}
			if(uploadfileCKbox.isChecked()){
				if(uploadfiletbox.getValue()!=null&&!"0".equals(uploadfiletbox.getText())){
					ermResourcesConfig.setUploadFile(uploadfiletbox.getValue());
				}else if(uploadfiletbox.getValue()!=null&&uploadfiletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.uploadfile")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.uploadfile")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}else{
				ermResourcesConfig.setUploadFile(0);
			}
			
			
			((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).update(ermResourcesConfig);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "ermResourcesConfig_"+ermResourcesConfig.getTypeId());
			ZkUtils.showInformation(Labels.getLabel("updateOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("ermResourcesConfig/ErmResourcesConfig.zul");
			ermResourcesConfigEditWin.detach();
		} catch (WrongValueException e) {
			log.error(""+e);
		}
		
	}
	
	@Listen("onClick=#saveBtn")
	public void save(){
		try {
			ermResourcesConfig=new ErmResourcesConfig();
			if(nameCBox!=null&&nameCBox.getSelectedItem().getValue()!=null&&!"0".equals(nameCBox.getSelectedItem().getValue())){
				ErmResourcesConfig ermResources=((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).findByTypeId(webEmployee, nameCBox.getSelectedItem().getValue().toString());
				if(ermResources!=null){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.typeIdrepetition"),Labels.getLabel("info"));
					nameCBox.focus();
					return;
				}else{
					ermResourcesConfig.setTypeId(nameCBox.getSelectedItem().getValue().toString());
				}
			}else{
				ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.typeIdisNull"),Labels.getLabel("info"));
				nameCBox.focus();
				return;
			}
			if(titleCKbox.isChecked()){
				if(titletbox.getValue()!=null&&!"0".equals(titletbox.getText())){
					ermResourcesConfig.setTitle(titletbox.getValue());
				}else if(titletbox.getValue()!=null&&titletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.title")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.title")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(urlCKbox.isChecked()){
				if(urltbox.getValue()!=null&&!"0".equals(urltbox.getText())){
					ermResourcesConfig.setUrl1(urltbox.getValue());
				}else if(urltbox.getValue()!=null&&urltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig。url1")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig。url1")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(urlTwoCKbox.isChecked()){
				if(urlTwotbox.getValue()!=null&&!"0".equals(urlTwotbox.getText())){
					ermResourcesConfig.setUrl2(urlTwotbox.getValue());
				}else if(urlTwotbox.getValue()!=null&&urlTwotbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.url2")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.url2")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(remarkCKbox.isChecked()){
				if(remarktbox.getValue()!=null&&!"0".equals(remarktbox.getText())){
					ermResourcesConfig.setRemarkId(remarktbox.getValue());
				}else if(remarktbox.getValue()!=null&&remarktbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.remark")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.remark")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(starorderdateCKbox.isChecked()){
				if(starorderdatetbox.getValue()!=null&&!"0".equals(starorderdatetbox.getText())){
					ermResourcesConfig.setStarOrderDate(starorderdatetbox.getValue());
				}else if(starorderdatetbox.getValue()!=null&&starorderdatetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.starorderdate")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.starorderdate")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(endorderdateCKbox.isChecked()){
				if(endorderdatetbox.getValue()!=null&&!"0".equals(endorderdatetbox.getText())){
					ermResourcesConfig.setEndOrderDate(endorderdatetbox.getValue());
				}else if(endorderdatetbox.getValue()!=null&&endorderdatetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.endorderdate")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.endorderdate")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(coverageCKbox.isChecked()){
				if(coveragetbox.getValue()!=null&&!"0".equals(coveragetbox.getText())){
					ermResourcesConfig.setCoverage(coveragetbox.getValue());
				}else if(coveragetbox.getValue()!=null&&coveragetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.coverage")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.coverage")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(regllipCKbox.isChecked()){
				if(reglliptbox.getValue()!=null&&!"0".equals(reglliptbox.getText())){
					ermResourcesConfig.setRegllyIp(reglliptbox.getValue());
				}else if(reglliptbox.getValue()!=null&&reglliptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.regllyip")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.regllyip")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(idpwdCKbox.isChecked()){
				if(idpwdtbox.getValue()!=null&&!"0".equals(idpwdtbox.getText())){
					ermResourcesConfig.setIdpwd(idpwdtbox.getValue());
				}else if(idpwdtbox.getValue()!=null&&idpwdtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.idpwd")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.idpwd")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(othersCKbox.isChecked()){
				if(othertbox.getValue()!=null&&!"0".equals(othertbox.getText())){
					ermResourcesConfig.setOthers(othertbox.getValue());
				}else if(othertbox.getValue()!=null&&othertbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.others")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.others")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(eriefCKbox.isChecked()){
				if(erieftbox.getValue()!=null&&!"0".equals(erieftbox.getText())){
					ermResourcesConfig.setBrief1(erieftbox.getValue());
				}else if(erieftbox.getValue()!=null&&erieftbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief1")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief1")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(eriefEngCKbox.isChecked()){
				if(eriefEngtbox.getValue()!=null&&!"0".equals(eriefEngtbox.getText())){
					ermResourcesConfig.setBrief2(eriefEngtbox.getValue());
				}else if(eriefEngtbox.getValue()!=null&&eriefEngtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief2")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.erief2")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(languageidCKbox.isChecked()){
				if(languageidtbox.getValue()!=null&&!"0".equals(languageidtbox.getText())){
					ermResourcesConfig.setLanguageId(languageidtbox.getValue());
				}else if(languageidtbox.getValue()!=null&&languageidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.languageid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.languageid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(agentedidCKbox.isChecked()){
				if(agentedidtbox.getValue()!=null&&!"0".equals(agentedidtbox.getText())){
					ermResourcesConfig.setAgentedId(agentedidtbox.getValue());
				}else if(agentedidtbox.getValue()!=null&&agentedidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.agentedid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.agentedid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(publisheridCKbox.isChecked()){
				if(publisheridtbox.getValue()!=null&&!"0".equals(publisheridtbox.getText())){
					ermResourcesConfig.setPublisherId(publisheridtbox.getValue());
				}else if(publisheridtbox.getValue()!=null&&publisheridtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(frenquencyCKbox.isChecked()){
				if(frenquencytbox.getValue()!=null&&!"0".equals(frenquencytbox.getText())){
					ermResourcesConfig.setFrenquency(frenquencytbox.getValue());
				}else if(frenquencytbox.getValue()!=null&&frenquencytbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.frenquency")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.frenquency")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(introCKbox.isChecked()){
				if(introtbox.getValue()!=null&&!"0".equals(introtbox.getText())){
					ermResourcesConfig.setIntro(introtbox.getValue());
				}else if(introtbox.getValue()!=null&&introtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.intro")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.intro")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(concurCKbox.isChecked()){
				if(concurtbox.getValue()!=null&&!"0".equals(concurtbox.getText())){
					ermResourcesConfig.setConcur(concurtbox.getValue());
				}else if(concurtbox.getValue()!=null&&concurtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.concur")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.concur")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(connectidCKbox.isChecked()){
				if(connectidtbox.getValue()!=null&&!"0".equals(connectidtbox.getText())){
					ermResourcesConfig.setConnectId(connectidtbox.getValue());
				}else if(connectidtbox.getValue()!=null&&connectidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.connectid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.connectid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(coreCKbox.isChecked()){
				if(coretbox.getValue()!=null&&!"0".equals(coretbox.getText())){
					ermResourcesConfig.setCore(coretbox.getValue());
				}else if(coretbox.getValue()!=null&&coretbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.core")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.core")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(pubplaceCKbox.isChecked()){
				if(pubplacetbox.getValue()!=null&&!"0".equals(pubplacetbox.getText())){
					ermResourcesConfig.setPubPlace(pubplacetbox.getValue());
				}else if(pubplacetbox.getValue()!=null&&pubplacetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.pubplace")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.pubplace")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(dbidCKbox.isChecked()){
				if(dbidtbox.getValue()!=null&&!"0".equals(dbidtbox.getText())){
					ermResourcesConfig.setDbId(dbidtbox.getValue());
				}else if(dbidtbox.getValue()!=null&&dbidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.dbid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.dbid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(publisherurlCKbox.isChecked()){
				if(publisherurltbox.getValue()!=null&&!"0".equals(publisherurltbox.getText())){
					ermResourcesConfig.setPublisherurl(publisherurltbox.getValue());
				}else if(publisherurltbox.getValue()!=null&&publisherurltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherurl")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.publisherurl")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(issnprintedCKbox.isChecked()){
				if(issnprintedtbox.getValue()!=null&&!"0".equals(issnprintedtbox.getText())){
					ermResourcesConfig.setIssnprinted(issnprintedtbox.getValue());
				}else if(issnprintedtbox.getValue()!=null&&issnprintedtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.issnprinted")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.issnprinted")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(ssnonlineCKbox.isChecked()){
				if(ssnonlinetbox.getValue()!=null&&!"0".equals(ssnonlinetbox.getText())){
					ermResourcesConfig.setIssnonline(ssnonlinetbox.getValue());
				}else if(ssnonlinetbox.getValue()!=null&&ssnonlinetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.ssnonline")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.ssnonline")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(isbnprintedCKbox.isChecked()){
				if(isbnprintedtbox.getValue()!=null&&!"0".equals(isbnprintedtbox.getText())){
					ermResourcesConfig.setIsbnprinted(isbnprintedtbox.getValue());
				}else if(isbnprintedtbox.getValue()!=null&&isbnprintedtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnprinted")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnprinted")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(isbnonlineCKbox.isChecked()){
				if(isbnonlinetbox.getValue()!=null&&!"0".equals(isbnonlinetbox.getText())){
					ermResourcesConfig.setIsbnonline(isbnonlinetbox.getValue());
				}else if(isbnonlinetbox.getValue()!=null&&isbnonlinetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnonline")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.isbnonline")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(embargoCKbox.isChecked()){
				if(embargotbox.getValue()!=null&&!"0".equals(embargotbox.getText())){
					ermResourcesConfig.setEmbargo(embargotbox.getValue());
				}else if(embargotbox.getValue()!=null&&embargotbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.embargo")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.embargo")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(cnCKbox.isChecked()){
				if(cntbox.getValue()!=null&&!"0".equals(cntbox.getText())){
					ermResourcesConfig.setCn(cntbox.getValue());
				}else if(cntbox.getValue()!=null&&cntbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.cn")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.cn")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(callnCKbox.isChecked()){
				if(callntbox.getValue()!=null&&!"0".equals(callntbox.getText())){
					ermResourcesConfig.setCalln(callntbox.getValue());
				}else if(callntbox.getValue()!=null&&callntbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.calln")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.calln")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(eholdingsCKbox.isChecked()){
				if(eholdingstbox.getValue()!=null&&!"0".equals(eholdingstbox.getText())){
					ermResourcesConfig.setEholdings(eholdingstbox.getValue());
				}else if(eholdingstbox.getValue()!=null&&eholdingstbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.eholdings")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.eholdings")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(libaryMoneyCKbox.isChecked()){
				if(libaryMoneytbox.getValue()!=null&&!"0".equals(libaryMoneytbox.getText())){
					ermResourcesConfig.setLibaryMoney(libaryMoneytbox.getValue());
				}else if(libaryMoneytbox.getValue()!=null&&libaryMoneytbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.libaryMoney")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.libaryMoney")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(imgurlCKbox.isChecked()){
				if(imgurltbox.getValue()!=null&&!"0".equals(imgurltbox.getText())){
					ermResourcesConfig.setImgurl(imgurltbox.getValue());
				}else if(imgurltbox.getValue()!=null&&imgurltbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.imgurl")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.imgurl")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(authorCKbox.isChecked()){
				if(authortbox.getValue()!=null&&!"0".equals(authortbox.getText())){
					ermResourcesConfig.setAuthor(authortbox.getValue());
				}else if(authortbox.getValue()!=null&&authortbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.author")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.author")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(placeidCKbox.isChecked()){
				if(placeidtbox.getValue()!=null&&!"0".equals(placeidtbox.getText())){
					ermResourcesConfig.setPlaceId(placeidtbox.getValue());
				}else if(placeidtbox.getValue()!=null&&placeidtbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.placeid")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.placeid")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(versionCKbox.isChecked()){
				if(versiontbox.getValue()!=null&&!"0".equals(versiontbox.getText())){
					ermResourcesConfig.setVersion(versiontbox.getValue());
				}else if(versiontbox.getValue()!=null&&versiontbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.version")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.version")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(relatedtitleCKbox.isChecked()){
				if(relatedtitletbox.getValue()!=null&&!"0".equals(relatedtitletbox.getText())){
					ermResourcesConfig.setRelatedTitle(relatedtitletbox.getValue());
				}else if(relatedtitletbox.getValue()!=null&&relatedtitletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.relatedtitle")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.relatedtitle")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(subjectCKbox.isChecked()){
				if(subjecttbox.getValue()!=null&&!"0".equals(subjecttbox.getText())){
					ermResourcesConfig.setSubject(subjecttbox.getValue());
				}else if(subjecttbox.getValue()!=null&&subjecttbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.subject")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.subject")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(typeCKbox.isChecked()){
				if(typetbox.getValue()!=null&&!"0".equals(typetbox.getText())){
					ermResourcesConfig.setType(typetbox.getValue());
				}else if(typetbox.getValue()!=null&&typetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.type")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.type")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(suitCollegeCKbox.isChecked()){
				if(suitCollegetbox.getValue()!=null&&!"0".equals(suitCollegetbox.getText())){
					ermResourcesConfig.setSuitCollege(suitCollegetbox.getValue());
				}else if(suitCollegetbox.getValue()!=null&&suitCollegetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitCollege")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitCollege")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(suitdepCKbox.isChecked()){
				if(suitdeptbox.getValue()!=null&&!"0".equals(suitdeptbox.getText())){
					ermResourcesConfig.setSuitDep(suitdeptbox.getValue());
				}else if(suitdeptbox.getValue()!=null&&suitdeptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitdep")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.suitdep")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(orderCollegeCKbox.isChecked()){
				if(orderCollegetbox.getValue()!=null&&!"0".equals(orderCollegetbox.getText())){
					ermResourcesConfig.setOrderCollege(orderCollegetbox.getValue());
				}else if(orderCollegetbox.getValue()!=null&&orderCollegetbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderCollege")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderCollege")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(orderdepCKbox.isChecked()){
				if(orderdeptbox.getValue()!=null&&!"0".equals(orderdeptbox.getText())){
					ermResourcesConfig.setOrderDep(orderdeptbox.getValue());
				}else if(orderdeptbox.getValue()!=null&&orderdeptbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderdep")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.orderdep")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			if(uploadfileCKbox.isChecked()){
				if(uploadfiletbox.getValue()!=null&&!"0".equals(uploadfiletbox.getText())){
					ermResourcesConfig.setUploadFile(uploadfiletbox.getValue());
				}else if(uploadfiletbox.getValue()!=null&&uploadfiletbox.getText().equals("0")){
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.uploadfile")+Labels.getLabel("ermResourcesConfig.isOne"),Labels.getLabel("info"));
					return;
				}else{
					ZkUtils.showExclamation(Labels.getLabel("ermResourcesConfig.uploadfile")+Labels.getLabel("ermResourcesConfig.isNull"),Labels.getLabel("info"));
					return;
				}
			}
			
			
			((ErmResourcesConfigService)SpringUtil.getBean("ermResourcesConfigService")).update(ermResourcesConfig);
			((WebSysLogService)SpringUtil.getBean("webSysLogService")).editLog(ZkUtils.getRemoteAddr(),webEmployee.getEmployeesn(), "ermResourcesConfig_"+ermResourcesConfig.getTypeId());
			ZkUtils.showInformation(Labels.getLabel("saveOK"),
					Labels.getLabel("info"));
			ZkUtils.refurbishMethod("ermResourcesConfig/ErmResourcesConfig.zul");
			ermResourcesConfigAddWin.detach();
		} catch (WrongValueException e) {
			log.error(""+e);
		}
	}
	
	

	
}
