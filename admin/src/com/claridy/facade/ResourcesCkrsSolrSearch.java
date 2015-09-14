package com.claridy.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.IERMSystemSettingDAO;
import com.claridy.dao.IErmResourcesMainfileVDAO;
import com.claridy.domain.ErmResourcesMainfileV;
import com.claridy.domain.ErmSystemSetting;

@Service
public class ResourcesCkrsSolrSearch {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private IErmResourcesMainfileVDAO ermResMainfileVDao;
	@Autowired
	private IERMSystemSettingDAO ermSysSettingDao;
	
	private String SOLR_URL;
	private CommonsHttpSolrServer solrServer = null;
	/**
	 * 創建solr服務器   
	 */
	public void resourcesCkrsSolrSearch(){
		try {
			ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SOLRIP");
			String ip = ermSysSetting.getFuncValue();
			
			if(ip!=null&&!"".equals(ip)){
				SOLR_URL = "http://"+ip+"/solr/core2";
			}
			solrServer = new CommonsHttpSolrServer(SOLR_URL);
			solrServer.setMaxTotalConnections(100);
			solrServer.setSoTimeout(10000);//socket read time
			solrServer.setConnectionTimeout(5000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 查詢
	 * @param queryStr
	 * @return
	 */
	public List<ErmResourcesMainfileV> search(String queryStr){
		resourcesCkrsSolrSearch();
		List<ErmResourcesMainfileV> resourcesCkrsList = new ArrayList<ErmResourcesMainfileV>();
		SolrQuery query = new SolrQuery();
		QueryResponse response =null;
		query.setQuery(queryStr);
		query.addSortField("type_id",SolrQuery.ORDER.asc);
		query.addSortField("resources_id",SolrQuery.ORDER.asc);
		ErmSystemSetting ermSysSetting=ermSysSettingDao.getSysByFunID("SolrCount");
		query.setRows(Integer.parseInt(ermSysSetting.getFuncValue()));
		SolrDocumentList docs = null;
		try {
			response = solrServer.query(query);
		} catch (SolrServerException e) {
			return resourcesCkrsList;
		}
		docs = response.getResults();
		 // 輸出結果   
	    for(SolrDocument doc:docs) {   
	    	ErmResourcesMainfileV ermResources=new ErmResourcesMainfileV();
	    	ermResources.setResourcesId(doc.getFieldValue("resources_id").toString());
	    	ermResources.setTitle(doc.getFieldValue("title").toString());
	    	ermResources.setTypeId(doc.getFieldValue("type_id").toString());
	    	resourcesCkrsList.add(ermResources);
	    }  
		return resourcesCkrsList;
	}
	
	/**
	 * 更新全部搜尋引擎資料
	 */
	public void addData() {
		resourcesCkrsSolrSearch();
		log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addCkrsSolrData start");
		log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>addCkrsSolrData start");
		//首先刪除索引檔
		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Statement pstmt = null;
		ResultSet rs = null;
		Collection docs = new ArrayList();
		SolrInputDocument doc1 = null;
		try {
			List<Object> mainfileListTemp=ermResMainfileVDao.findObjectListBySql("select m.title,m.resources_id,m.type_id from erm_resources_ckrs k left join erm_resources_mainfile_v m on k.resources_id=m.resources_id");
			for(int i=0;i<mainfileListTemp.size();i++){
				Object[] objTemp = (Object[])mainfileListTemp.get(i);
				doc1 = new SolrInputDocument();
				doc1.addField("resources_id", objTemp[1]);
				doc1.addField("title",objTemp[0]);
				doc1.addField("type_id",objTemp[2]);
				docs.add(doc1);
			}
			solrServer.add(docs);
			solrServer.commit();
		} catch (Exception e) {
			log.error("添加偵測電子資源數據報錯",e);
		} 
		log.error(">>>>>>>>>>>>>>>>>>>>>>>>>>addCkrsSolrData end");
		log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>>addCkrsSolrData end");
	}
	/**
	 * 新增或修改搜索引擎資料
	 * @param type_id  資源類型
	 * @param resoucres_id 資源編號
	 * @param title 題名
	 */
	public void addResourcesCkrs(String type_id,String resoucres_id,String title){
		Collection docs = new ArrayList();
		SolrInputDocument doc1 = null;
		try {
			doc1 = new SolrInputDocument();
			doc1.addField("resources_id", resoucres_id);
			doc1.addField("title",title);
			doc1.addField("type_id",type_id);
			docs.add(doc1);
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 刪除所選偵測電子資源
	 */
	public void deleteById(String resources_id){
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
	 * 刪除所有搜尋引擎資料
	 */
	public void deleteData(){
		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
