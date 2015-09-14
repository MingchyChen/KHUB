package com.claridy.facade;

import java.io.IOException;
import java.net.MalformedURLException;
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

import com.claridy.dao.IERMSystemSettingDAO;
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.dao.IErmResourcesMainEjebItemDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmResourcesEjebItem;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmSystemSetting;

/*****************************************************************************************************************************************************************************
 * 程式名稱：ERM 修改人：顧晨露 修改描述：1.菜單"電子資料庫/電子書/電子資料庫/網絡資料"進入，修改方法findDataList(..)
 * 2.添加"電子資料庫/電子書/電子資料庫/網絡資料"頁面，左邊點選資源瀏覽模組，產生的資源資訊，方法findResList(..)
 * 3.添加方法editCountSolrDate(..)該方法用於修改頁面上做了"推薦，點閱，收藏"操作後，修改solr檔中資源統計欄位的值
 * 4.添加方法addResCountDate()該方法用於把資源統計欄位加入到solr檔 修改時間：2012-3-9 修改人：顧晨露
 * 修改描述：1.添加方法findAfterInfoList
 * (..)該方法用於菜單"電子資料庫/電子書/電子資料庫/網絡資料"進入，點擊資源瀏覽模組資源資訊的顯示
 * 2.添加方法findAfterSpecialInfoList
 * (..)該方法用於菜單"電子資料庫/電子書/電子資料庫/網絡資料"進入，點擊資源瀏覽模組中特殊功能（語言別，所屬資料庫，適用系所）資源資訊的顯示。
 * 3.添加方法findResList(..) 該方法用於"資源瀏覽"模組中的中文資料, 西文資料,全文資料庫, 熱門點閱, 熱門收藏, 熱門推薦點選操作
 * 4.添加方法findItemInfo(..)該方法用於處理語言別資訊的顯示 適用系所資訊的顯示 所屬資料庫資訊的顯示 修改時間：2012-3-11
 * 修改人：顧晨露 修改描述：添加了方法findAfterRefeshInfoList(..)刷新後分類(排除一下情況作的刷新動作：
 * <1>.菜單（電子資料庫，電子期刊，電子書，網絡資源）進入，點擊資源瀏覽出現的所屬資料庫後分類 ;
 * <2>.菜單（電子資料庫，電子期刊，電子書，網絡資源）進入，點擊資源瀏覽中的（語言別，所屬資料庫，適用系所）所屬資料庫後分類 )
 * 此方法被重構了兩次，重構的目的是區分是否點擊過後分類的"更多"按鈕。 修改時間：2012-3-15 修改人：顧晨露
 * 修改描述：1.添加了方法findSItemInfo(..)該方法用於一般查詢或是進階查詢更多按鈕的操作
 * 2.添加了方法findResListBySItemInfo(..)該方法用於一般查詢或是進階查詢進入點選完更多按鈕後，顯示各個項目下的資源資訊
 * 3.添加了方法findResListByItemInfo(..)該方法被重構了兩次 (<1>.讀取語言別資訊下的資源資訊 讀取適用系所下的資源資訊
 * 讀取所屬資料庫下的資源資訊 <2>.菜單（電子資料庫，電子書，電子期刊，網絡資源）進入，後分類點選操作) 修改時間：2012-3-16 修改人：顧晨露
 * 修改描述：修改方法findAfterTypeList(..),findDataList(..)添加了參數用於記錄頁面點選過的後分類資訊
 * 修改時間：2012-3-19 修改人：顧晨露
 * 修改描述：修改了方法findDataList(..),findAfterTypeList(..)..主要涉及到後分類的方法都做了副檔欄位的功能。
 * 修改時間：2012-3-21 修改人：顧晨露 修改時間：2012-5-3
 * 修改描述：添加方法findDataListBySearch(..),該方法是查找用戶搜尋結果資源資訊的
 * ，主要是由於前臺顯示欄位不同（type_id/dbtype_id），所以單獨提取出來
 * **************************************************************************************************************************************************************************/
@Service
public class ResourcesMainfileSolrSearch {
	@Autowired
	private IErmCodeGeneralCodeDAO ermCodeGeneralDao;
	@Autowired
	private IErmResourcesMainfileVDAO ermResMainfileVDao;
	@Autowired
	private IErmResourcesMainEjebItemDAO ermResMainEjebItemDao;
	@Autowired
	private IERMSystemSettingDAO ermSysSettingDao;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static String SOLR_URL;

	private CommonsHttpSolrServer solrServer = null;

	public void ResourcesMainfileSolrSearch() {
		ErmSystemSetting ermSysSetting = ermSysSettingDao
				.getSysByFunID("SOLRIP");
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
	 * 根據sql查詢集合
	 * @param sql
	 * @return
	 */
	public List<Object> findObjectListBySql(String sql){
		return ermResMainfileVDao.findObjectListBySql(sql);
	}
	/**
	 * 根據sql查詢名稱
	 * @param sql
	 * @return
	 */
	public String getNameBySql(String sql){
		String retName = ermCodeGeneralDao.getNameBySql(sql);
		return retName;
	}
	
	public static void main(String args[]) throws Exception {
		try {
			ResourcesMainfileSolrSearch tt = new ResourcesMainfileSolrSearch();
			// tt.insertSolr();
			tt.addData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2013-06-28 添加索引檔first
	 */
	public void addData() {
		log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addSolrData start");
		log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>addSolrData start");
		ResourcesMainfileSolrSearch();
		// 首先刪除索引檔
		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		SolrInputDocument doc1 = null;
		List<String> reslist = new ArrayList<String>();
		// 後修改
		try {
			String sql = "select mainfile.resources_id,mainfile.resources_id from (select distinct resources_id from erm_resources_mainfile_v where type_id is not null) mainfile";
			List<Object> mainFileListTemp = ermResMainfileVDao
					.findObjectListBySql(sql);
			for (int i = 0; i < mainFileListTemp.size(); i++) {
				Object[] objTemp = (Object[]) mainFileListTemp.get(i);
				reslist.add(objTemp[0].toString());
			}
			for (int i = 0; i < reslist.size(); i++) {
				ErmResourcesMainfileV mainfile = ermResMainfileVDao
						.getErmResMainfileVByResId(reslist.get(i));

				if (encodeXSSString(mainfile.getTitle()) != null
						&& !"".equals(encodeXSSString(mainfile.getTitle()))) {
					Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
					doc1 = new SolrInputDocument();
					doc1.addField("resources_id",
							encodeXSSString(mainfile.getResourcesId()));
					doc1.addField("title", encodeXSSString(mainfile.getTitle()));
					doc1.addField("title_search", encodeXSSString(mainfile
							.getTitle().toLowerCase()));
					doc1.addField("title_sort",
							encodeXSSString(mainfile.getTitle()));
					doc1.addField("type_id", mainfile.getTypeId());
					doc1.addField("brief1",
							encodeXSSString(mainfile.getBrief1()));
					doc1.addField("coverage",
							encodeXSSString(mainfile.getCoverage()));
					doc1.addField("isbnonline", mainfile.getIsbnonline());
					doc1.addField("isbnprinted", mainfile.getIsbnprinted());
					doc1.addField("issnprinted", mainfile.getIssnprinted());
					doc1.addField("issnonline", mainfile.getIssnonline());
					doc1.addField("core", mainfile.getCore());
					doc1.addField("imgurl", mainfile.getImgurl());
					doc1.addField("connect_id", mainfile.getConnectId());

					doc1.addField("publisher_id", mainfile.getPublisherId());
					String publisherName = ermCodeGeneralDao
							.getNameBySql("select name from erm_code_publisher where publisher_id='"
									+ mainfile.getPublisherId() + "'");
					doc1.addField("publisher_name", publisherName);

					doc1.addField("agented_id", mainfile.getAgentedId());
					String agentedName = ermCodeGeneralDao
							.getNameBySql("select name from erm_code_publisher where publisher_id='"
									+ mainfile.getAgentedId() + "'");
					doc1.addField("agented_name", agentedName);

					// 後添加
					doc1.addField("jcr", mainfile.getJcr());
					doc1.addField("jcrreportlink", mainfile.getJcrReportLink());

					if(mainfile.getTypeId()!=null&&(mainfile.getTypeId().equals("EJ")||mainfile.getTypeId().equals("EB"))){
						// 副檔 所屬資料庫
						List<ErmResourcesMainfileV> ermResMainfileVListTemp = ermResMainfileVDao
								.findErmResMainfileVList(mainfile.getResourcesId());
						for (int k = 0; k < ermResMainfileVListTemp.size(); k++) {
							ErmResourcesMainfileV ermResMainfileVTemp = ermResMainfileVListTemp
									.get(k);
							doc1.addField("db_id", ermResMainfileVTemp.getDbId());
							// 副檔 提供單位
							List<Object> suunitListTemp = ermResMainfileVDao
									.findObjectListBySql("select suunit_id,suunit_id from erm_resources_suunit where resources_id='"
											+ ermResMainfileVTemp.getDbId() + "'");
							for (int t = 0; t < suunitListTemp.size(); t++) {
								Object[] objTemp = (Object[]) suunitListTemp.get(t);
								doc1.addField("suunit_id", objTemp[0]);
							}
							//採購註記
							doc1.addField("remark_id", ermResMainfileVTemp.getRemarkId());
						}
						// 插入刪除狀態
						String ejEbSql = "select count(*) from erm_resources_ejeb_item where resources_id = '"
								+ mainfile.getResourcesId()
								+ "' and (history='' or history is null or history='N')";
						String ejebItemCount = ermCodeGeneralDao.getNameBySql(ejEbSql);
						if (ejebItemCount != null && !"".equals(ejebItemCount)
								&& Integer.parseInt(ejebItemCount) > 0) {
							doc1.addField("history", "N");
						} else {
							doc1.addField("history", "Y");
						}
						if(ejebItemCount!=null&&!"".equals(ejebItemCount)&&(Integer.parseInt(ejebItemCount)==1||Integer.parseInt(ejebItemCount)==0)){
							doc1.addField("isonedb", "1");
						}else {
							doc1.addField("isonedb", "0");
						}
						// 插入結束日期
						String endOrderDateSql="select count(*) from erm_resources_ejeb_item where resources_id='"+ mainfile.getResourcesId()+ "' and endorderdate is null";
						String endOrderCount = ermCodeGeneralDao.getNameBySql(endOrderDateSql);
						if (Integer.parseInt(endOrderCount) == 0) {
							ErmResourcesEjebItem ermResEjebItem = ermResMainEjebItemDao.getMainEjebItemByResId(mainfile.getResourcesId());
							if(ermResEjebItem.getEndOrderDate()!=null&&!"".equals(ermResEjebItem.getEndOrderDate())){
								doc1.addField("endorderdate", ermResEjebItem.getEndOrderDate());
							}
						}
					}else if(mainfile.getTypeId()!=null&&(mainfile.getTypeId().equals("DB")||mainfile.getTypeId().equals("WS"))){
						// 副檔 提供單位
						List<Object> suunitListTemp = ermResMainfileVDao
								.findObjectListBySql("select suunit_id,suunit_id from erm_resources_suunit where resources_id='"
										+ mainfile.getResourcesId() + "'");
						for (int t = 0; t < suunitListTemp.size(); t++) {
							Object[] objTemp = (Object[]) suunitListTemp.get(t);
							doc1.addField("suunit_id", objTemp[0]);
						}
						//採購註記
						doc1.addField("remark_id", mainfile.getRemarkId());
						
						doc1.addField("history", mainfile.getHistory());
						doc1.addField("endorderdate", mainfile.getEndOrderDate());
					}

					doc1.addField("url1", mainfile.getUrl1());
					doc1.addField("language_id", mainfile.getLanguageId());
					doc1.addField("starorderdate", mainfile.getStarOrderDate());
					doc1.addField("state", mainfile.getState());
					// 副檔 主題
					List<Object> subjectListTemp = ermResMainfileVDao
							.findObjectListBySql("select subject_id,subject_id from erm_resources_subject where resources_id='"
									+ mainfile.getResourcesId() + "'");
					for (int s = 0; s < subjectListTemp.size(); s++) {
						Object[] objTemp = (Object[]) subjectListTemp.get(s);
						doc1.addField("subject_id", objTemp[0]);
					}
					// 副檔 資料庫類型
					List<Object> dbtypeListTemp = ermResMainfileVDao
							.findObjectListBySql("select dbtype_id,dbtype_id from erm_resources_dbtype where resources_id='"
									+ mainfile.getResourcesId() + "'");
					for (int d = 0; d < dbtypeListTemp.size(); d++) {
						Object[] objTemp = (Object[]) dbtypeListTemp.get(d);
						doc1.addField("dbtype_id", objTemp[0]);
					}
					
					//添加創建人員和創建日期
					String dataowner = ermCodeGeneralDao
							.getNameBySql("select employeename from webemployee where employeesn ='"+mainfile.getDataowner()+"'");
					if(dataowner!=null&&!"".equals(dataowner)){
						doc1.addField("dataowner", dataowner);
					}
					Object createdate = mainfile.getCreateDate();
					if(createdate!=null&&!"".equals(createdate)){
						doc1.addField("createdate", createdate);
					}
					
					String titleStr = encodeXSSString(mainfile.getTitle());
					// A to Z
					String character_cn = "";
					if (titleStr != null && !"".equals(titleStr)) {
						character_cn = titleStr.trim().substring(0, 1);
					}
					String character_cn2 = "";
					if (titleStr != null && !"".equals(titleStr)) {
						if (titleStr.trim().length() >= 2) {
							// Aa to AZ
							character_cn2 = titleStr.trim().substring(0, 2);
						}
					}
					// 驗證數字
					Pattern pattern = Pattern.compile("^\\d$");
					// 驗證字母
					Pattern pattern1 = Pattern.compile("[A-Za-z]");
					// 驗證字母
					Pattern pattern2 = Pattern.compile("[A-Za-z]{2}");

					if (pattern.matcher(character_cn).matches()) {
						doc1.addField("character_cn", "0-9");
					} else if (pattern1.matcher(character_cn).matches()) {
						doc1.addField("character_cn", character_cn);
						if (pattern2.matcher(character_cn2).matches()) {
							doc1.addField("character_cn2",
									character_cn2.toUpperCase());
						}
					} else {
						doc1.addField("character_cn", "0");
						try {
							int index = 0;
							List<Object> campareListTemp = ermResMainfileVDao
									.findObjectListBySql("select character_num,phonetic_one,phonetic_two from erm_sys_phonetic_compare where character_cn='"
											+ character_cn + "'");
							for (int c = 0; c < campareListTemp.size(); c++) {
								Object[] objTemp = (Object[]) campareListTemp
										.get(c);
								if (index == 0) {
									// 筆劃
									String character_num = objTemp[0]
											.toString();
									if (Integer.parseInt(character_num) >= 20) {
										character_num = "20+";
									}
									doc1.addField("character_num",
											character_num);

									index = 1;
								}
								// 拼音（臺灣）
								doc1.addField("phonetic_one", objTemp[1]);
								// 拼音（大陸）
								doc1.addField("phonetic_two", objTemp[2]);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					docs.add(doc1);
					solrServer.add(docs);
					if(i%1000==0){
						solrServer.commit();
					}
				}

			}
			solrServer.commit();
			log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addSolrData end");
		} catch (Exception e) {
			try {
				solrServer.commit();
			} catch (SolrServerException e1) {
				log.error("提交solr數據報錯",e1);
			} catch (IOException e1) {
				log.error("提交solr數據報錯",e1);
			}
			log.error("提交solr數據報錯",e);
		}
		log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>addSolrData end");
	}

	public void resources_mainfile_deleteData(String resources_id) {
		ResourcesMainfileSolrSearch();
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
	 * 類型查詢 2013-06-28
	 * 
	 * @param solrSql
	 * @return
	 * @throws ParseException
	 */
	public List<String[]> findTypeList(String title, String brief1,
			String advanced, String lang_type) {
		ResourcesMainfileSolrSearch();
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
		ErmSystemSetting ermSysSetting = ermSysSettingDao
				.getSysByFunID("SolrCount");
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
						typeName = ermCodeGeneralDao
								.getNameBySql("select name2 from erm_code_generalcode where item_id='RETYPE' and history='N' and generalcode_id='"
										+ count.getName() + "'");
					} else {
						typeName = ermCodeGeneralDao
								.getNameBySql("select name1 from erm_code_generalcode where item_id='RETYPE'  AND history='N' and generalcode_id='"
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
	 * 查詢 2013-06-28
	 * 
	 * @param queryStr
	 * @return
	 */
	public List promo_search(String queryStr) {
		ResourcesMainfileSolrSearch();
		List resourcesListRet = new ArrayList();
		SolrQuery query = new SolrQuery();
		QueryResponse response = null;
		// query.setHighlight(true);
		// query.addHighlightField("title");
		// query.setHighlightSimplePre("<font color=\"red\">");
		// query.setHighlightSimplePost("</font>");
		query.setQuery(queryStr);
		query.addSortField("title_sort", SolrQuery.ORDER.asc);
		// query.setRows(100);
		ErmSystemSetting ermSysSetting = ermSysSettingDao
				.getSysByFunID("SolrCount");
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

	/*
	 * // 刪除resources_mainfile表及其關聯的表中數據和SOLR中的數據 public void deteleData(String
	 * resources_id) { boolean bool = false; // 刪除mainfile內的數據
	 * IOerm_resources_mainfile_v resources_mainfile = new
	 * IOerm_resources_mainfile_v(); resources_mainfile =
	 * resources_mainfile.selectOne(connFactory, conn, resources_id); bool =
	 * resources_mainfile.delete(connFactory, conn, resources_id); if (bool) {
	 * // 刪除solr內的數據 resources_mainfile_deleteData(resources_id); } //
	 * 刪除偵測電子資源表內的數據 IOerm_resources_ckrs resources_ckrs = new
	 * IOerm_resources_ckrs(); bool = resources_ckrs.delete(connFactory, conn,
	 * resources_id); if (bool) { // 刪除solr內的數據 new
	 * Resources_ckrs_search().deleteById(resources_id); } // 刪除每月推薦的數據
	 * IOerm_promo erm_promo = new IOerm_promo();
	 * erm_promo.setResources_id_sc(resources_id); List<IOerm_promo>
	 * erm_promo_list = new ArrayList<IOerm_promo>(); erm_promo_list =
	 * erm_promo.search(connFactory, conn); for (int i = 0; i <
	 * erm_promo_list.size(); i++) { bool = erm_promo.delete(connFactory, conn,
	 * erm_promo_list.get(i) .getPromoid()); if (bool) { // 刪除solr內的數據 new
	 * Promo_promo_search().deleteData(erm_promo_list.get(i) .getPromoid()); } }
	 * // 如果沒有關閉connFactory if (connFactory != null) {
	 * connFactory.closeConn(conn); conn = null; connFactory = null; } }
	 */

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
		if (data == null || "".equals(data)) {
			return data;
		}

		final StringBuffer buf = new StringBuffer();
		final char[] chars = data.toCharArray();
		// ' &
		if ((int) chars[0] == 39) {
			buf.append("");
		} else {
			buf.append((char) chars[0]);
		}
		for (int i = 1; i < chars.length; i++) {
			if ((int) chars[i] == 58) {
				buf.append(" : ");
			} else {
				buf.append((char) chars[i]);
			}
		}

		return buf.toString();
	}
	
	public void addEditEjebItemData(String dbId){
		log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addEditEjebItemData start");
		ResourcesMainfileSolrSearch();
		SolrInputDocument doc1 = null;
		List<String> reslist = new ArrayList<String>();
		// 後修改
		try {
			String sql = "select mainfile.resources_id,mainfile.resources_id from (select distinct resources_id from erm_resources_mainfile_v where db_id = '"+dbId+"') mainfile";
			List<Object> mainFileListTemp = ermResMainfileVDao
					.findObjectListBySql(sql);
			for (int i = 0; i < mainFileListTemp.size(); i++) {
				Object[] objTemp = (Object[]) mainFileListTemp.get(i);
				reslist.add(objTemp[0].toString());
			}
			for (int i = 0; i < reslist.size(); i++) {
				ErmResourcesMainfileV mainfile = ermResMainfileVDao
						.getMainfileVByResId(reslist.get(i));

				if (encodeXSSString(mainfile.getTitle()) != null
						&& !"".equals(encodeXSSString(mainfile.getTitle()))) {
					Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
					doc1 = new SolrInputDocument();
					doc1.addField("resources_id",
							encodeXSSString(mainfile.getResourcesId()));
					doc1.addField("title", encodeXSSString(mainfile.getTitle()));
					doc1.addField("title_search", encodeXSSString(mainfile
							.getTitle().toLowerCase()));
					doc1.addField("title_sort",
							encodeXSSString(mainfile.getTitle()));
					doc1.addField("type_id", mainfile.getTypeId());
					doc1.addField("brief1",
							encodeXSSString(mainfile.getBrief1()));
					doc1.addField("coverage",
							encodeXSSString(mainfile.getCoverage()));
					doc1.addField("isbnonline", mainfile.getIsbnonline());
					doc1.addField("isbnprinted", mainfile.getIsbnprinted());
					doc1.addField("issnprinted", mainfile.getIssnprinted());
					doc1.addField("issnonline", mainfile.getIssnonline());
					doc1.addField("core", mainfile.getCore());
					doc1.addField("imgurl", mainfile.getImgurl());
					doc1.addField("connect_id", mainfile.getConnectId());

					doc1.addField("publisher_id", mainfile.getPublisherId());
					String publisherName = ermCodeGeneralDao
							.getNameBySql("select name from erm_code_publisher where publisher_id='"
									+ mainfile.getPublisherId() + "'");
					doc1.addField("publisher_name", publisherName);

					doc1.addField("agented_id", mainfile.getAgentedId());
					String agentedName = ermCodeGeneralDao
							.getNameBySql("select name from erm_code_publisher where publisher_id='"
									+ mainfile.getAgentedId() + "'");
					doc1.addField("agented_name", agentedName);

					// 後添加
					doc1.addField("jcr", mainfile.getJcr());
					doc1.addField("jcrreportlink", mainfile.getJcrReportLink());

					if(mainfile.getTypeId()!=null&&(mainfile.getTypeId().equals("EJ")||mainfile.getTypeId().equals("EB"))){
						// 副檔 所屬資料庫
						List<ErmResourcesMainfileV> ermResMainfileVListTemp = ermResMainfileVDao
								.findErmResMainfileVList(mainfile.getResourcesId());
						for (int k = 0; k < ermResMainfileVListTemp.size(); k++) {
							ErmResourcesMainfileV ermResMainfileVTemp = ermResMainfileVListTemp
									.get(k);
							doc1.addField("db_id", ermResMainfileVTemp.getDbId());
							// 副檔 提供單位
							List<Object> suunitListTemp = ermResMainfileVDao
									.findObjectListBySql("select suunit_id,suunit_id from erm_resources_suunit where resources_id='"
											+ ermResMainfileVTemp.getDbId() + "'");
							for (int t = 0; t < suunitListTemp.size(); t++) {
								Object[] objTemp = (Object[]) suunitListTemp.get(t);
								doc1.addField("suunit_id", objTemp[0]);
							}
							//採購註記
							doc1.addField("remark_id", ermResMainfileVTemp.getRemarkId());
						}
						// 插入刪除狀態
						String ejEbSql = "select count(*) from erm_resources_ejeb_item where resources_id = '"
								+ mainfile.getResourcesId()
								+ "' and (history='' or history is null or history='N')";
						String ejebItemCount = ermCodeGeneralDao.getNameBySql(ejEbSql);
						if (ejebItemCount != null && !"".equals(ejebItemCount)
								&& Integer.parseInt(ejebItemCount) > 0) {
							doc1.addField("history", "N");
						} else {
							doc1.addField("history", "Y");
						}
						if(ejebItemCount!=null&&!"".equals(ejebItemCount)&&(Integer.parseInt(ejebItemCount)==1||Integer.parseInt(ejebItemCount)==0)){
							doc1.addField("isonedb", "1");
						}else {
							doc1.addField("isonedb", "0");
						}
						// 插入結束日期
						String endOrderDateSql="select count(*) from erm_resources_ejeb_item where resources_id='"+ mainfile.getResourcesId()+ "' and endorderdate is null";
						String endOrderCount = ermCodeGeneralDao.getNameBySql(endOrderDateSql);
						if (Integer.parseInt(endOrderCount) == 0) {
							ErmResourcesEjebItem ermResEjebItem = ermResMainEjebItemDao.getMainEjebItemByResId(mainfile.getResourcesId());
							if(ermResEjebItem.getEndOrderDate()!=null&&!"".equals(ermResEjebItem.getEndOrderDate())){
								doc1.addField("endorderdate", ermResEjebItem.getEndOrderDate());
							}
						}
					}
					doc1.addField("url1", mainfile.getUrl1());
					doc1.addField("language_id", mainfile.getLanguageId());
					doc1.addField("starorderdate", mainfile.getStarOrderDate());
					doc1.addField("state", mainfile.getState());
					// 副檔 主題
					List<Object> subjectListTemp = ermResMainfileVDao
							.findObjectListBySql("select subject_id,subject_id from erm_resources_subject where resources_id='"
									+ mainfile.getResourcesId() + "'");
					for (int s = 0; s < subjectListTemp.size(); s++) {
						Object[] objTemp = (Object[]) subjectListTemp.get(s);
						doc1.addField("subject_id", objTemp[0]);
					}
					// 副檔 資料庫類型
					List<Object> dbtypeListTemp = ermResMainfileVDao
							.findObjectListBySql("select dbtype_id,dbtype_id from erm_resources_dbtype where resources_id='"
									+ mainfile.getResourcesId() + "'");
					for (int d = 0; d < dbtypeListTemp.size(); d++) {
						Object[] objTemp = (Object[]) dbtypeListTemp.get(d);
						doc1.addField("dbtype_id", objTemp[0]);
					}
					//添加創建人員和創建日期
					String dataowner = ermCodeGeneralDao
							.getNameBySql("select employeename from webemployee where employeesn ='"+mainfile.getDataowner()+"'");
					if(dataowner!=null&&!"".equals(dataowner)){
						doc1.addField("dataowner", dataowner);
					}
					Object createdate = mainfile.getCreateDate();
					if(createdate!=null&&!"".equals(createdate)){
						doc1.addField("createdate", createdate);
					}
					String titleStr = encodeXSSString(mainfile.getTitle());
					// A to Z
					String character_cn = "";
					if (titleStr != null && !"".equals(titleStr)) {
						character_cn = titleStr.trim().substring(0, 1);
					}
					String character_cn2 = "";
					if (titleStr != null && !"".equals(titleStr)) {
						if (titleStr.trim().length() >= 2) {
							// Aa to AZ
							character_cn2 = titleStr.trim().substring(0, 2);
						}
					}
					// 驗證數字
					Pattern pattern = Pattern.compile("^\\d$");
					// 驗證字母
					Pattern pattern1 = Pattern.compile("[A-Za-z]");
					// 驗證字母
					Pattern pattern2 = Pattern.compile("[A-Za-z]{2}");

					if (pattern.matcher(character_cn).matches()) {
						doc1.addField("character_cn", "0-9");
					} else if (pattern1.matcher(character_cn).matches()) {
						doc1.addField("character_cn", character_cn);
						if (pattern2.matcher(character_cn2).matches()) {
							doc1.addField("character_cn2",
									character_cn2.toUpperCase());
						}
					} else {
						doc1.addField("character_cn", "0");
						try {
							int index = 0;
							List<Object> campareListTemp = ermResMainfileVDao
									.findObjectListBySql("select character_num,phonetic_one,phonetic_two from erm_sys_phonetic_compare where character_cn='"
											+ character_cn + "'");
							for (int c = 0; c < campareListTemp.size(); c++) {
								Object[] objTemp = (Object[]) campareListTemp
										.get(c);
								if (index == 0) {
									// 筆劃
									String character_num = objTemp[0]
											.toString();
									if (Integer.parseInt(character_num) >= 20) {
										character_num = "20+";
									}
									doc1.addField("character_num",
											character_num);

									index = 1;
								}
								// 拼音（臺灣）
								doc1.addField("phonetic_one", objTemp[1]);
								// 拼音（大陸）
								doc1.addField("phonetic_two", objTemp[2]);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					docs.add(doc1);
					solrServer.add(docs);
					if(i%1000==0){
						solrServer.commit();
					}
				}
			}
			solrServer.commit();
		} catch (Exception e) {
			try {
				solrServer.commit();
			} catch (SolrServerException e1) {
				log.error("提交solr數據報錯",e1);
			} catch (IOException e1) {
				log.error("提交solr數據報錯",e1);
			}
			log.error("提交solr數據報錯",e);
		}
		log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addEditEjebItemData end");
	}
}