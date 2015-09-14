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
import com.claridy.dao.IWebPublicationsClickDAO;
import com.claridy.domain.WebPublication;
import com.claridy.domain.WebPublicationsClick;
@Repository
public class WebPublicationsClickDAO extends BaseDAO implements IWebPublicationsClickDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebPublicationsClickDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據開始日期和結束日期查詢農業出版品點擊資料
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebPublicationsClick> findWebPubClickList(Date startDate, Date endDate){
		List<WebPublicationsClick> listRet = new ArrayList<WebPublicationsClick>();
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebPublicationsClick.class);
			criteria.add(Restrictions.between("visitTime", startDateD,endDateD));
			listRet = (List<WebPublicationsClick>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			log.error("findWebPubClickList報錯：",e);
		}
		return listRet;
	}
	/**
	 * 查詢農業出版品集合
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebPublication> findAllWebPub() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebPublication.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<WebPublication> list = (List<WebPublication>) super.findByCriteria(criteria);
		return list;
	}
}
