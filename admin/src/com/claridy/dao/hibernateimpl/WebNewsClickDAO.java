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
import com.claridy.dao.IWebNewsClickDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebNewsClick;
@Repository
public class WebNewsClickDAO extends BaseDAO implements IWebNewsClickDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebNewsClickDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據開始日期和結束日期查詢最新消息點擊資料
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebNewsClick> findWebNewsClickList(Date startDate, Date endDate){
		List<WebNewsClick> listRet = new ArrayList<WebNewsClick>();
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebNewsClick.class);
			criteria.add(Restrictions.between("visitTime", startDateD,endDateD));
			listRet = (List<WebNewsClick>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			log.error("findWebNewsClickList報錯：",e);
		}
		return listRet;
	}
	/**
	 * 查詢最新消息集合
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ErmNews> findAllWebNews() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ErmNews.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<ErmNews> list = (List<ErmNews>) super.findByCriteria(criteria);
		return list;
	}
}
