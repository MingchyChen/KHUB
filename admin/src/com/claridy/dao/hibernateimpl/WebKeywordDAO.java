package com.claridy.dao.hibernateimpl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebKeywordDAO;
import com.claridy.domain.WebKeyWord;


@Repository
public class WebKeywordDAO extends BaseDAO implements IWebKeywordDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebKeywordDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	public List<WebKeyWord> findkeyword(String accountid) {
		String hql = "from WebKeyWord where keyword is not null and keyword !='' order by createdate desc";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(100);
		List<WebKeyWord> list=query.list();
		List<WebKeyWord> li=new ArrayList<WebKeyWord>();
		for (WebKeyWord key : list) {
			if(li.size()==0){
				li.add(key);
			}
			int i=0;
			if(li.size()<5){
				for (WebKeyWord word : li) {
					if(key.getKeyWord().equals(word.getKeyWord())){
						i++;
						break;
					}else{
						i++;
						if(i==li.size()){
							li.add(key);
							break;
						}else{
							continue;
						}
						
					}
				}
			}
		}
		
		if(li.size()>0){
			return li;
		}else{
			return new ArrayList<WebKeyWord>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<WebKeyWord> getkeybyaccount(String accountid) {
		String hql = "from WebKeyWord where webAccountUuid='"+accountid+"' and keyword is not null and keyword !='' order by createdate desc" ;
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(60);
		List<WebKeyWord> list=query.list();
		List<WebKeyWord> li=new ArrayList<WebKeyWord>();
		for (WebKeyWord key : list) {
			if(li.size()==0){
				li.add(key);
			}
			int i=0;
			if(li.size()<20){
				for (WebKeyWord word : li) {
					if((key.getKeyWord().equals(word.getKeyWord()))&&(key.getTarget()==word.getTarget())){
						i++;
						break;
					}else{
						i++;
						if(i==li.size()){
							li.add(key);
							break;
						}else{
							continue;
						}
						
					}
				}
			}
		}
		if(li.size()>0){
			return li;
		}else{
			return new ArrayList<WebKeyWord>();
		}
	}
	public WebKeyWord getKWByAccountAndKey(String keyword,String accountid,int target) {
		String hql = "from WebKeyWord where webAccountUuid='"+accountid+"' and target="+target+" and lower(keyWord) =lower('"+keyword+"') order by createdate desc"  ;
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<WebKeyWord> webKWlist=query.list();
		if(webKWlist.size()>0){
			return webKWlist.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 根據開始日期和結束日期查詢查詢種類使用量統計集合
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebKeyWord> findSelUseNumberList(Date startDate, Date endDate){
		List<WebKeyWord> tmpWebKeyWordList = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String startDateStr=sdf.format(startDate);
			String endDateStr=sdf.format(endDate);
			SimpleDateFormat smp = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Date startDateD = startDate;
			if (startDateStr != null && !"".equals(startDateStr)) {
				startDateStr+=" 00:00:00";
				startDateD = smp.parse(startDateStr);
			}
			Date endDateD = endDate;
			if (endDateStr != null && !"".equals(endDateStr)) {
				endDateStr+=" 23:59:59";
				endDateD = smp.parse(endDateStr);
			}
			DetachedCriteria criteria = DetachedCriteria.forClass(WebKeyWord.class);
			criteria.add(Restrictions.between("createDate", startDateD,endDateD));
			tmpWebKeyWordList = (List<WebKeyWord>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			tmpWebKeyWordList=new ArrayList<WebKeyWord>();
			log.error("findSelUseNumberList報錯：",e);
		}
		return tmpWebKeyWordList;
	}
}
