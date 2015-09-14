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
import com.claridy.dao.IWebEduTrainingClickDAO;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEduTrainingClick;
@Repository
public class WebEduTrainingClickDAO extends BaseDAO implements IWebEduTrainingClickDAO {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebEduTrainingClickDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 根據開始日期和結束日期查詢教育訓練點擊資料
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebEduTrainingClick> findWebEduClickList(Date startDate, Date endDate){
		List<WebEduTrainingClick> listRet = new ArrayList<WebEduTrainingClick>();
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebEduTrainingClick.class);
			criteria.add(Restrictions.between("visitTime", startDateD,endDateD));
			listRet = (List<WebEduTrainingClick>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			log.error("findWebEduClickList報錯：",e);
		}
		return listRet;
	}
	/**
	 * 查詢教育訓練集合
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebEduTraining> findAllWebEdu() {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebEduTraining.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		List<WebEduTraining> list = (List<WebEduTraining>) super.findByCriteria(criteria);
		return list;
	}
}
