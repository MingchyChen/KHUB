package com.claridy.dao.hibernateimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebFarmingNewsClickDAO;
import com.claridy.domain.WebFarmingNews;
import com.claridy.domain.WebFarmingNewsClick;
@Repository
public class WebFarmingNewsClickDAO extends BaseDAO implements IWebFarmingNewsClickDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebFarmingNewsClickDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據開始日期和結束日期查詢農業新聞點擊資料
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebFarmingNewsClick> findWebFarmClickList(Date startDate, Date endDate){
		List<WebFarmingNewsClick> listRet = new ArrayList<WebFarmingNewsClick>();
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebFarmingNewsClick.class);
			criteria.add(Restrictions.between("visitTime", startDateD,endDateD));
			listRet = (List<WebFarmingNewsClick>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			log.error("findWebFarmClickList報錯：",e);
		}
		return listRet;
	}
	/**
	 * 查詢農業新聞集合
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebFarmingNews> findAllWebFarm() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebFarmingNews.class);
		List<WebFarmingNews> list = (List<WebFarmingNews>) super.findByCriteria(criteria);
		return list;
	}
}
