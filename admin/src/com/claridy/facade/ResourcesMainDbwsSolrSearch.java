package com.claridy.facade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zkplus.spring.SpringUtil;

import com.claridy.dao.IERMSystemSettingDAO;
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmResMainDbwsDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmResourcesMainDbws;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmSystemSetting;
@Service
public class ResourcesMainDbwsSolrSearch {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IErmCodeGeneralCodeDAO ermCodeGeneralDao;
	@Autowired
	private IErmResourcesMainfileVDAO ermResMainfileVDao;
	@Autowired
	private IErmResMainDbwsDAO ermResMainDbwsDao;
	@Autowired
	private IERMSystemSettingDAO ermSysSettingDao;
	
	private String SOLR_URL;

	private CommonsHttpSolrServer solrServer = null;

	public void resourcesMainDbwsSolrSearch() {
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SOLRIP");
		String ip = ermSysSetting.getFuncValue();
		SOLR_URL = "http://" + ip + "/solr/core0";
		try {
			solrServer = new CommonsHttpSolrServer(SOLR_URL);
			solrServer.setMaxTotalConnections(500);
			solrServer.setSoTimeout(200000);// socket read time
			solrServer.setConnectionTimeout(200000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查詢索引檔
	 * 
	 * @param queryStr
	 * @return
	 */
	public List<ErmResourcesMainDbws> search(String queryStr) {
		resourcesMainDbwsSolrSearch();
		List<ErmResourcesMainDbws> resourcesCkrsList = new ArrayList<ErmResourcesMainDbws>();
		SolrQuery query = new SolrQuery();
		QueryResponse response = null;
		query.setHighlight(true);
		query.addHighlightField("title");
		query.setHighlightSimplePre("<font color='red'>");
		query.setHighlightSimplePost("</font>");
		query.setQuery(queryStr);
		query.addSortField("title_sort", SolrQuery.ORDER.asc);
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SolrCount");
		query.setRows(Integer.parseInt(ermSysSetting.getFuncValue()));
		query.setStart(0);
		try {
			response = solrServer.query(query);
		} catch (SolrServerException e) {
			log.error(e.getMessage(),e);
			return resourcesCkrsList;
		}
		// 輸出結果
		for (SolrDocument doc : response.getResults()) {
			ErmResourcesMainDbws ermResourcesMainfile = new ErmResourcesMainDbws();
			ermResourcesMainfile.setResourcesId(doc
					.getFieldValue("resources_id") == null ? null : doc
					.getFieldValue("resources_id").toString());
			ermResourcesMainfile
					.setTitle(doc.getFieldValue("title") == null ? null : doc
							.getFieldValue("title").toString());
			ermResourcesMainfile.setPublisherId(doc
					.getFieldValues("publisher_id") == null ? null : doc
					.getFieldValue("publisher_id").toString());
			ermResourcesMainfile.setBrief1(doc.getFieldValue("brief1")==null?null:doc.getFieldValue("brief1").toString());

			ermResourcesMainfile.setCreateName(doc.getFieldValue("dataowner") == null ? null
							: doc.getFieldValue("dataowner").toString());
			Date createDate = doc.getFieldValue("createdate") == null ? null
					: (Date) doc.getFieldValue("createdate");
			ermResourcesMainfile.setCreateDate(createDate);
			resourcesCkrsList.add(ermResourcesMainfile);
		}
		return resourcesCkrsList;
	}

	/**
	 * 電子資源管理頁面查詢
	 * 
	 * @param solrSql
	 * @return
	 * @throws ParseException
	 */
	public List<ErmResourcesMainfileV> resourcesMainSearch(String solrSql) throws ParseException {
		resourcesMainDbwsSolrSearch();
		List<ErmResourcesMainfileV> resourcesMaindbwsList = new ArrayList<ErmResourcesMainfileV>();
		List<ErmCodeGeneralCode> langCodeList = ((ErmCodeGeneralCodeService) SpringUtil
				.getBean("ermCodeGeneralCodeService"))
				.findErmCodeGeneralCodeByItemId("DBLAN");
		
		List<ErmCodeGeneralCode> remarkCodeList = ((ErmCodeGeneralCodeService) SpringUtil
				.getBean("ermCodeGeneralCodeService"))
				.findErmCodeGeneralCodeByItemId("PURE");
		QueryResponse response = null;
		SolrDocumentList docs = null;
		SolrQuery query = new SolrQuery();
		query.setQuery(solrSql);
		query.addSortField("title_sort", SolrQuery.ORDER.asc);// 排序
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SolrCount");
		query.setRows(Integer.parseInt(ermSysSetting.getFuncValue()));
		query.setStart(0);
		// 如果傳遞過來的pageSize大於solrCount
		/*if (pageSize > resources_common.getSolrCount()) {
			pageSize = resources_common.getSolrCount();
		}
		query.setRows(pageSize);
		int startSize = (nowPage - 1) * pageSize;
		query.setStart(startSize);*/
		try {
			response = solrServer.query(query);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return resourcesMaindbwsList;
		}
		docs = response.getResults();
		if (docs != null && docs.size() > 0) {
			for (SolrDocument doc : docs) {
				ErmResourcesMainfileV resourcesMainDbws = new ErmResourcesMainfileV();
				resourcesMainDbws.setResourcesId(doc
						.getFieldValue("resources_id") == null ? null : doc
						.getFieldValue("resources_id").toString());
				resourcesMainDbws
						.setTypeId(doc.getFieldValue("type_id") == null ? null
								: doc.getFieldValue("type_id").toString());
				resourcesMainDbws
						.setTitle(doc.getFieldValue("title") == null ? null
								: doc.getFieldValue("title").toString());
				resourcesMainDbws
						.setRemarkId(doc.getFieldValue("remark_id") == null ? null
								: doc.getFieldValue("remark_id").toString());
				resourcesMainDbws.setLanguageId(doc
						.getFieldValue("language_id") == null ? null : doc
						.getFieldValue("language_id").toString());
				resourcesMainDbws.setStarOrderDate(doc
						.getFieldValue("starorderdate") == null ? null
						: (Date) doc.getFieldValue("starorderdate"));
				resourcesMainDbws.setEndOrderDate(doc
						.getFieldValue("endorderdate") == null ? null
						: (Date) doc.getFieldValue("endorderdate"));
				resourcesMainDbws
						.setHistory(doc.getFieldValue("history") == null ? null
						: doc.getFieldValue("history").toString());
				for(int i=0;i<langCodeList.size();i++){
					ErmCodeGeneralCode langCode=langCodeList.get(i);
					if(langCode.getGeneralcodeId().equals(resourcesMainDbws.getLanguageId())){
						resourcesMainDbws.setLanguageCn(langCode.getName1());
					}
				}
				for(int i=0;i<remarkCodeList.size();i++){
					ErmCodeGeneralCode remarkCode=remarkCodeList.get(i);
					if(remarkCode.getGeneralcodeId().equals(resourcesMainDbws.getRemarkId())){
						resourcesMainDbws.setRemarkCn(remarkCode.getName1());
					}
				}
				resourcesMainDbws
						.setState(doc.getFieldValue("state") == null ? null
								: doc.getFieldValue("state").toString());
				resourcesMainDbws
						.setImgurl(doc.getFieldValue("imgurl") == null ? null
								: doc.getFieldValue("imgurl").toString());
				
				resourcesMainDbws.setCreateName(doc.getFieldValue("dataowner") == null ? null
						: doc.getFieldValue("dataowner").toString());
				Date createDate = doc.getFieldValue("createdate") == null ? null
				: (Date) doc.getFieldValue("createdate");
				resourcesMainDbws.setCreateDate(createDate);
				
				resourcesMaindbwsList.add(resourcesMainDbws);
			}
		}
		return resourcesMaindbwsList;
	}

	public void resources_main_dbws_deleteData(String resources_id) {
		resourcesMainDbwsSolrSearch();
		try {
			solrServer.deleteById(resources_id);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 類型查詢
	 * 
	 * @param solrSql
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	public List<String[]> findTypeList(String title, String brief1,
		String advanced, String lang_type) {
		resourcesMainDbwsSolrSearch();
		List<String[]> typeList = new ArrayList<String[]>();
		SolrQuery query = new SolrQuery();
		QueryResponse response = null;
		String searchStr = "";
		// 系統日期
		java.util.Date date = new java.util.Date();
		// 設置轉義字元
		// title=transferredStr(title);
		if (!"".equals(title)) {
			title = transferredStr(title);
			searchStr = "((title:" + title + "* OR title:" + title + ") ";
		}
		if (!"".equals(brief1)) {
			brief1 = transferredStr(brief1);
			searchStr = searchStr + " OR (brief1:" + brief1 + "* OR brief1:"
					+ brief1 + ")) AND";
		}

		if (!"".equals(advanced)) {
			searchStr = searchStr + "(" + advanced + ")";
		}
		// 起訂日期
		String sdate = new SimpleDateFormat("yyyy-MM-dd").format(date);

		searchStr = searchStr
				+ " ((starorderdate:[* TO "
				+ sdate
				+ "T00:00:00.000Z-8HOUR] AND  endorderdate:["
				+ sdate
				+ "T00:00:00.000Z-8HOUR TO *]) OR  (starorderdate:[* TO "
				+ sdate
				+ "T00:00:00.000Z-8HOUR] AND  -endorderdate:[* TO *]))  AND state:1";
		query.setQuery(searchStr);
		query.addSortField("title_sort", SolrQuery.ORDER.asc);
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SolrCount");
		query.setRows(Integer.parseInt(ermSysSetting.getFuncValue()));

		query.setFacet(true);
		query.addFacetField("type_id");

		SolrDocumentList docs = null;
		try {
			response = solrServer.query(query);
		} catch (SolrServerException e) {
			return typeList;
		}
		docs = response.getResults();

		List list1 = response.getFacetFields();
		for (int i = 0; i < list1.size(); i++) {
			FacetField facetField = (FacetField) list1.get(i);
			if (!"null".equals(facetField.toString().split(":")[1])) {
				List list2 = facetField.getValues();

				for (int j = 0; j < list2.size(); j++) {
					String type[] = new String[3];
					Count count = (Count) list2.get(j);
					type[0] = count.getName();
					String typeName = "";
					if ("en_us".equals(lang_type)) {// 英文語系
						typeName = ermCodeGeneralDao.getNameBySql("select name2 from erm_code_generalcode where item_id='RETYPE' and history='N' and generalcode_id='"
										+ count.getName() + "'");
					} else {
						typeName = ermCodeGeneralDao.getNameBySql("select name1 from erm_code_generalcode where item_id='RETYPE'  AND history='N' and generalcode_id='"
										+ count.getName() + "'");
					}
					type[1] = typeName;
					type[2] = "(" + String.valueOf(count.getCount()) + ")";
					typeList.add(type);
				}
			}

		}

		return typeList;
	}

	/**
	 * 新增或編輯方法 
	 * 
	 * @param maindbws
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void resources_main_dbws_editData_add(String resourcesId)
			throws SQLException {
		resourcesMainDbwsSolrSearch();
		Collection docs = new ArrayList();
		SolrInputDocument doc1 = null;
		ErmResourcesMainfileV mainfile = ermResMainfileVDao.getErmResMainfileVByResId(resourcesId);
		doc1 = new SolrInputDocument();
		doc1.addField("resources_id", mainfile.getResourcesId());
		doc1.addField("type_id", mainfile.getTypeId());
		doc1.addField("title", encodeXSSString(mainfile.getTitle()));
		doc1.addField("title_sort", mainfile.getTitle());
		doc1.addField("remark_id", mainfile.getRemarkId());
		doc1.addField("brief1", mainfile.getBrief1());
		doc1.addField("coverage", mainfile.getCoverage());
		
		// 副檔 提供單位
		List<Object> suunitListTemp = ermResMainfileVDao
				.findObjectListBySql("select suunit_id,suunit_id from erm_resources_suunit where resources_id='"
						+ mainfile.getResourcesId() + "'");
		for (int t = 0; t < suunitListTemp.size(); t++) {
			Object[] objTemp = (Object[]) suunitListTemp.get(t);
			doc1.addField("suunit_id", objTemp[0]);
		}
		
		doc1.addField("url1", mainfile.getUrl1());
		doc1.addField("isbnonline", mainfile.getIsbnonline());
		doc1.addField("isbnprinted", mainfile.getIsbnprinted());
		doc1.addField("issnprinted", mainfile.getIssnprinted());
		doc1.addField("issnonline", mainfile.getIssnonline());

		doc1.addField("core", mainfile.getCore());
		doc1.addField("imgurl", mainfile.getImgurl());
		doc1.addField("language_id", mainfile.getLanguageId());

		doc1.addField("starorderdate", mainfile.getStarOrderDate());
		doc1.addField("endorderdate", mainfile.getEndOrderDate());
		doc1.addField("state", mainfile.getState());
		
		doc1.addField("jcr", mainfile.getJcr());
		doc1.addField("jcrreportlink", mainfile.getJcrReportLink());
		doc1.addField("history", "N");
		
		doc1.addField("connect_id", mainfile.getConnectId());

		doc1.addField("publisher_id", mainfile.getPublisherId());
		String publisherName = ermCodeGeneralDao.getNameBySql("select name from erm_code_publisher where publisher_id='"
						+ mainfile.getPublisherId() + "'");
		doc1.addField("publisher_name", publisherName);

		doc1.addField("agented_id", mainfile.getAgentedId());
		String agentedName = ermCodeGeneralDao.getNameBySql("select name from erm_code_publisher where publisher_id='"
						+ mainfile.getAgentedId() + "'");
		doc1.addField("agented_name", agentedName);

		// 副檔 主題
		List<Object> subjectListTemp=ermResMainfileVDao.findObjectListBySql("select subject_id,subject_id from erm_resources_subject where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<subjectListTemp.size();i++){
			Object[] objTemp = (Object[])subjectListTemp.get(i);
			doc1.addField("subject_id", objTemp[0]);
		}
		// 副檔 資料庫類型
		List<Object> dbtypeListTemp=ermResMainfileVDao.findObjectListBySql("select dbtype_id,dbtype_id from erm_resources_dbtype where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<dbtypeListTemp.size();i++){
			Object[] objTemp = (Object[])dbtypeListTemp.get(i);
			doc1.addField("dbtype_id", objTemp[0]);
		}
		// 副檔 適用學院
		List<Object> suitcollegeListTemp=ermResMainfileVDao.findObjectListBySql("select suitcollege_id,suitcollege_id from erm_resources_scollege where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<suitcollegeListTemp.size();i++){
			Object[] objTemp = (Object[])suitcollegeListTemp.get(i);
			doc1.addField("suitcollege_id", objTemp[0]);
		}
		// 副檔 適用系所
		List<Object> suitdepListTemp=ermResMainfileVDao.findObjectListBySql("select suitdep_id,suitdep_id from erm_resources_suitdep where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<suitdepListTemp.size();i++){
			Object[] objTemp = (Object[])suitdepListTemp.get(i);
			doc1.addField("suitdep_id", objTemp[0]);
		}
		
		//添加創建人員和創建日期
		String dataowner = ermCodeGeneralDao
				.getNameBySql("select employeename from webemployee where employeesn in ( select dataowner from erm_resources_main_dbws where resources_id='"+mainfile.getResourcesId()+"')");
		if(dataowner!=null&&!"".equals(dataowner)){
			doc1.addField("dataowner", dataowner);
		}
		Object createdate = ermCodeGeneralDao
				.getObjectBySql("select createdate from erm_resources_main_dbws where resources_id='"+mainfile.getResourcesId()+"'");
		if(createdate!=null&&!"".equals(createdate)){
			doc1.addField("createdate", createdate);
		}
		
		String titleString=encodeXSSString(mainfile.getTitle());
		// 首字
		String character_cn="";
		if(titleString.length()>=1){
			character_cn = titleString.substring(0, 1).trim();
		}
		String character_cn2="";
		if(titleString.length()>=2){
			// Aa to AZ
			character_cn2 = titleString.substring(0, 2).trim();
		}
		// 驗證數字
		Pattern pattern = Pattern.compile("^\\d$");
		// 驗證字母
		Pattern pattern1 = Pattern.compile("[A-Za-z]");
		// 驗證字母
		Pattern pattern2 = Pattern.compile("[A-Za-z]{2}");
		//後添加
		if (pattern2.matcher(character_cn2).matches()) {
			doc1.addField("character_cn2", character_cn2.toUpperCase());
		}else{
			doc1.addField("character_cn2", "");
		}
		if (pattern.matcher(character_cn).matches()) {
			doc1.addField("character_cn", "0-9");
		} else if (pattern1.matcher(character_cn).matches()) {
			doc1.addField("character_cn", character_cn);
		} else {
			doc1.addField("character_cn", "0");
			int index = 0;
			List<Object> campareListTemp=ermResMainfileVDao.findObjectListBySql("select character_num,phonetic_one,phonetic_two from erm_sys_phonetic_compare where character_cn='"
					+ character_cn + "'");
			for(int i=0;i<campareListTemp.size();i++){
				Object[] objTemp = (Object[])campareListTemp.get(i);
				if (index == 0) {
					// 筆劃
					String character_num = objTemp[0].toString();
					if (Integer.parseInt(character_num) >= 20) {
						character_num = "20+";
					}
					doc1.addField("character_num", character_num);

					index = 1;
				}
				// 拼音（臺灣）
				doc1.addField("phonetic_one",objTemp[1]);
				// 拼音（大陸）
				doc1.addField("phonetic_two",objTemp[2]);
			}
		}
		docs.add(doc1);
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 新增或編輯方法
	 * 
	 * @param mainfile
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void resources_main_dbws_editData(String resourcesId)
			throws SQLException {
		ErmResourcesMainfileV mainfile = ermResMainfileVDao.getErmResMainfileVByResId(resourcesId);
		resourcesMainDbwsSolrSearch();
		Collection docs = new ArrayList();
		SolrInputDocument doc1 = null;
		doc1 = new SolrInputDocument();
		doc1.addField("resources_id", mainfile.getResourcesId());
		doc1.addField("type_id", mainfile.getTypeId());
		doc1.addField("title", encodeXSSString(mainfile.getTitle()));
		doc1.addField("title_sort", mainfile.getTitle());
		doc1.addField("title_search", mainfile.getTitle().toLowerCase());
		doc1.addField("remark_id", mainfile.getRemarkId());
		doc1.addField("brief1", mainfile.getBrief1());

		// 副檔 提供單位
		List<Object> suunitListTemp = ermResMainfileVDao
				.findObjectListBySql("select suunit_id,suunit_id from erm_resources_suunit where resources_id='"
						+ mainfile.getResourcesId() + "'");
		for (int t = 0; t < suunitListTemp.size(); t++) {
			Object[] objTemp = (Object[]) suunitListTemp.get(t);
			doc1.addField("suunit_id", objTemp[0]);
		}
		
		doc1.addField("url1", mainfile.getUrl1());

		doc1.addField("core", mainfile.getCore());
		doc1.addField("imgurl", mainfile.getImgurl());
		doc1.addField("language_id", mainfile.getLanguageId());

		doc1.addField("starorderdate", mainfile.getStarOrderDate());
		doc1.addField("endorderdate", mainfile.getEndOrderDate());
		doc1.addField("state", mainfile.getState());
		
		doc1.addField("history", mainfile.getHistory());
		
		doc1.addField("connect_id", mainfile.getConnectId());
		doc1.addField("publisher_id", mainfile.getPublisherId());
		
		// 出版商名稱
		String publisherName = ermCodeGeneralDao.getNameBySql("select name from erm_code_publisher where publisher_id='"
						+ mainfile.getPublisherId() + "'");
		doc1.addField("publisher_name", publisherName);

		doc1.addField("agented_id", mainfile.getAgentedId());
		// 代理商名稱
		String agentedName = ermCodeGeneralDao.getNameBySql("select name from erm_code_publisher where publisher_id='"
						+ mainfile.getAgentedId() + "'");
		doc1.addField("agented_name", agentedName);
		
		
		// 副檔 主題
		List<Object> subjectListTemp=ermResMainfileVDao.findObjectListBySql("select subject_id,subject_id from erm_resources_subject where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<subjectListTemp.size();i++){
			Object[] objTemp = (Object[])subjectListTemp.get(i);
			doc1.addField("subject_id", objTemp[0]);
		}
		// 副檔 資料庫類型
		List<Object> dbtypeListTemp=ermResMainfileVDao.findObjectListBySql("select dbtype_id,dbtype_id from erm_resources_dbtype where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<dbtypeListTemp.size();i++){
			Object[] objTemp = (Object[])dbtypeListTemp.get(i);
			doc1.addField("dbtype_id", objTemp[0]);
		}
		// 副檔 適用學院
		List<Object> suitcollegeListTemp=ermResMainfileVDao.findObjectListBySql("select suitcollege_id,suitcollege_id from erm_resources_scollege where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<suitcollegeListTemp.size();i++){
			Object[] objTemp = (Object[])suitcollegeListTemp.get(i);
			doc1.addField("suitcollege_id", objTemp[0]);
		}
		// 副檔 適用系所
		List<Object> suitdepListTemp=ermResMainfileVDao.findObjectListBySql("select suitdep_id,suitdep_id from erm_resources_suitdep where resources_id='"
				+ resourcesId + "'");
		for(int i=0;i<suitdepListTemp.size();i++){
			Object[] objTemp = (Object[])suitdepListTemp.get(i);
			doc1.addField("suitdep_id", objTemp[0]);
		}
		
		//添加創建人員和創建日期
		String dataowner = ermCodeGeneralDao
				.getNameBySql("select employeename from webemployee where employeesn in ( select dataowner from erm_resources_main_dbws where resources_id='"+mainfile.getResourcesId()+"')");
		if(dataowner!=null&&!"".equals(dataowner)){
			doc1.addField("dataowner", dataowner);
		}
		Object createdate = ermCodeGeneralDao
				.getObjectBySql("select createdate from erm_resources_main_dbws where resources_id='"+mainfile.getResourcesId()+"'");
		if(createdate!=null&&!"".equals(createdate)){
			doc1.addField("createdate", createdate);
		}
		
		String titleString=mainfile.getTitle();
		// 首字
		String character_cn="";
		String character_cn2="";
		if (titleString != null) {
			if(titleString.length()>=1){
				character_cn = titleString.substring(0, 1).trim();
			}
			if(titleString.length()>=2){
				// Aa to AZ
				character_cn2 = titleString.substring(0, 2).trim();
			}
		}
		// 驗證數字
		Pattern pattern = Pattern.compile("^\\d$");
		// 驗證字母
		Pattern pattern1 = Pattern.compile("[A-Za-z]");
		// 驗證字母
		Pattern pattern2 = Pattern.compile("[A-Za-z]{2}");
		//後添加
		if (pattern2.matcher(character_cn2).matches()) {
			doc1.addField("character_cn2", character_cn2.toUpperCase());
		}else{
			doc1.addField("character_cn2", "");
		}
		if (pattern.matcher(character_cn).matches()) {
			doc1.addField("character_cn", "0-9");
		} else if (pattern1.matcher(character_cn).matches()) {
			doc1.addField("character_cn", character_cn);
		} else {
			doc1.addField("character_cn", "0");
			int index = 0;

			List<Object> campareListTemp=ermResMainfileVDao.findObjectListBySql("select character_num,phonetic_one,phonetic_two from erm_sys_phonetic_compare where character_cn='"
					+ character_cn + "'");
			for(int i=0;i<campareListTemp.size();i++){
				Object[] objTemp = (Object[])campareListTemp.get(i);
				if (index == 0) {
					// 筆劃
					String character_num = objTemp[0].toString();
					if (Integer.parseInt(character_num) >= 20) {
						character_num = "20+";
					}
					doc1.addField("character_num", character_num);

					index = 1;
				}
				// 拼音（臺灣）
				doc1.addField("phonetic_one",objTemp[1]);
				// 拼音（大陸）
				doc1.addField("phonetic_two",objTemp[2]);
			}
		}

		docs.add(doc1);
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查詢
	 * 
	 * @param queryStr
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public List promo_search(String queryStr) {
		resourcesMainDbwsSolrSearch();
		List resourcesListRet = new ArrayList();
		SolrQuery query = new SolrQuery();
		QueryResponse response = null;
		// query.setHighlight(true);
		// query.addHighlightField("title");
		// query.setHighlightSimplePre("<font color=\"red\">");
		// query.setHighlightSimplePost("</font>");
		query.setQuery(queryStr);
		query.addSortField("title_sort", SolrQuery.ORDER.asc);
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SolrCount");
		query.setRows(Integer.parseInt(ermSysSetting.getFuncValue()));
		SolrDocumentList docs = null;
		try {
			response = solrServer.query(query);
		} catch (SolrServerException e) {
			return resourcesListRet;
		}
		docs = response.getResults();
		// 輸出結果
		for (SolrDocument doc : response.getResults()) {
			ErmResourcesMainfileV ermResourcesMainfile = new ErmResourcesMainfileV();
			ermResourcesMainfile.setResourcesId(doc
					.getFieldValue("resources_id") == null ? null : doc
					.getFieldValue("resources_id").toString());
			ermResourcesMainfile
					.setTitle(doc.getFieldValue("title") == null ? null : doc
							.getFieldValue("title").toString());
			ermResourcesMainfile
					.setBrief1(doc.getFieldValue("brief1") == null ? null : doc
							.getFieldValue("brief1").toString());
			ermResourcesMainfile
					.setImgurl(doc.getFieldValue("imgurl") == null ? null : doc
							.getFieldValue("imgurl").toString());
			resourcesListRet.add(ermResourcesMainfile);
		}
		return resourcesListRet;
	}

	// 刪除resources_mainfile表及其關聯的表中數據和SOLR中的數據
	@SuppressWarnings("unused")
	public void deteleData(String resourcesId) {
		resourcesMainDbwsSolrSearch();
		try{
			// 刪除erm_resources_main_dbws內的數據
			ErmResourcesMainDbws resourcesMainDbws = ermResMainDbwsDao.getResMainDbwsByResId(resourcesId); 
			resourcesMainDbws.setHistory("Y");
			resourcesMainDbws.setIsDataEffid(0);
			ermResMainDbwsDao.merge(resourcesMainDbws);
			
			//改solr的history
			try {
				resources_main_dbws_editData(resourcesId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*// 刪除偵測電子資源表內的數據
			IOerm_resources_ckrs resources_ckrs = new IOerm_resources_ckrs();
			bool = resources_ckrs.delete(connFactory, conn, resources_id);
			conn.commit();
			if (bool) {
				// 刪除solr內的數據
				new Resources_ckrs_search().deleteById(resources_id);
				conn.commit();
			}
			// 刪除每月推薦的數據
			IOerm_promo erm_promo = new IOerm_promo();
			erm_promo.setResources_id_sc(resources_id);
			List<IOerm_promo> erm_promo_list = new ArrayList<IOerm_promo>();
			erm_promo_list = erm_promo.search(connFactory, conn);
			for (int i = 0; i < erm_promo_list.size(); i++) {
				bool = erm_promo.delete(connFactory, conn, erm_promo_list.get(i).getPromoid());
				if (bool) {
					// 刪除solr內的數據
					new Promo_promo_search().deleteData(erm_promo_list.get(i).getPromoid());
				}
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 刪除resources_mainfile表及其關聯的表中數據和SOLR中的數據
	@SuppressWarnings("unused")
	public void deteleData2(String resourcesId) {
		resourcesMainDbwsSolrSearch();
		try{
			ErmResourcesMainDbws resourcesMainDbws = ermResMainDbwsDao.getResMainDbwsByResId(resourcesId); 
			resourcesMainDbws.setHistory("Y");
			resourcesMainDbws.setIsDataEffid(0);
			ermResMainDbwsDao.merge(resourcesMainDbws);
			
			//改solr的history
			try {
				resources_main_dbws_editData(resourcesId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/*// 刪除偵測電子資源表內的數據
			IOerm_resources_ckrs resources_ckrs = new IOerm_resources_ckrs();
			bool = resources_ckrs.delete(connFactory, conn, resources_id);
			conn.commit();
			if (bool) {
				// 刪除solr內的數據
				new Resources_ckrs_search().deleteById(resources_id);
				conn.commit();
			}
			// 刪除每月推薦的數據
			IOerm_promo erm_promo = new IOerm_promo();
			erm_promo.setResources_id_sc(resources_id);
			List<IOerm_promo> erm_promo_list = new ArrayList<IOerm_promo>();
			erm_promo_list = erm_promo.search(connFactory, conn);
			for (int i = 0; i < erm_promo_list.size(); i++) {
				bool = erm_promo.delete(connFactory, conn, erm_promo_list.get(i).getPromoid());
				if (bool) {
					// 刪除solr內的數據
					new Promo_promo_search().deleteData(erm_promo_list.get(i).getPromoid());
				}
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根據type_id刪除數據
	 * @param type_id
	 */
	public void deteleSolrType(String typeId){
		resourcesMainDbwsSolrSearch();
		try {
			solrServer.deleteByQuery("type_id:"+typeId+"");
			solrServer.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 根據type_id獲取未刪除的resources_id插入solr中
	 * @param type_id
	 * @return
	 */
	public void addSolrText(String typeId){
		List<String> strlist=new ArrayList<String>();
		String sql="select res.resources_id from (select distinct resources_id from erm_resources_mainfile_v where type_id ='"+typeId+"' and history='N') res";
		try {
			List<Object> suitdepListTemp=ermResMainfileVDao.findObjectListBySql(sql);
			for(int i=0;i<suitdepListTemp.size();i++){
				Object[] objTemp = (Object[])suitdepListTemp.get(i);
				String resourcesIdTemp=objTemp[0].toString();
				strlist.add(resourcesIdTemp);
			}
			for(int i=0;i<strlist.size();i++){
				resources_main_dbws_editData(strlist.get(i));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//IOerm_resources_dbwslog
	//IOerm_resources_main_dbws IOerm_resources_main_dbws
	/**
	 * 判斷該資源是否是新進資源
	 * 
	 * @param time1
	 *            起訂日期
	 * @param time2
	 *            當前日期
	 * @return
	 * @throws ParseException
	 */
	public String getQuot(String time1, String time2) throws ParseException {
		String bool = "newResNo";
		long quot = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = sdf.parse(time1);
		Date date2 = sdf.parse(time2);
		quot = date2.getTime() - date1.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		if (quot <= 30)
			bool = "newResYes";
		return bool;
	}

	/**
	 * 把<{[(改成轉義字元
	 * 
	 * @param title
	 * @return
	 */
	public String transferredStr(String title) {
		title = title.replaceAll("[\\(\\)\\<\\>\\[\\]\\{\\}\\!\\~\\+\\-\\^]",
				"\\\\$0");
		return title;
	}
	public static String encodeXSSString(String data) {
	     if(data == null || "".equals(data)) {
	      return data;
	     }
	     
	     final StringBuffer buf = new StringBuffer();
	        final char[] chars = data.toCharArray();
	        //' & 
	        if((int)chars[0] == 39 ) {
	          buf.append("");
	         }else{
	        	 buf.append((char) chars[0]);
	         }
	        for (int i = 1; i < chars.length; i++) {
	         if((int)chars[i] == 58 ) {
	          buf.append(" : ");
	         }else {
	          buf.append((char) chars[i]);
	         }
      }
       
       return buf.toString();
   }
	public static void main(String args[]) throws SolrServerException, SQLException{
		  String SOLR_URL = "http://192.168.20.39:8080/solr/core0"; 
		  CommonsHttpSolrServer solrServer=null; 
	  try { 
		  solrServer =new CommonsHttpSolrServer(SOLR_URL);
		  solrServer.setMaxTotalConnections(100); 
		  solrServer.setSoTimeout(10000);
		  //socket read time 
		  solrServer.setConnectionTimeout(5000);
		  ResourcesMainDbwsSolrSearch tt=new ResourcesMainDbwsSolrSearch();
		  tt.search("*:*");
	  } catch (MalformedURLException e) { 
		  e.printStackTrace(); }
	}

}
