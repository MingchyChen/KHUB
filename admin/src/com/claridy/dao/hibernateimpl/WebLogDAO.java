package com.claridy.dao.hibernateimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IWebLogDAO;
import com.claridy.domain.WebLog;
import com.claridy.domain.WebSysLog;

@Repository
public class WebLogDAO extends BaseDAO implements IWebLogDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	public WebLogDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<WebSysLog> search(String nLocate) throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebSysLog.class);
		criteria.add(Restrictions.eq("nlocate", nLocate));
		criteria.addOrder(Order.desc("ndate"));
		List<WebSysLog> tmpWebSysLogList = (List<WebSysLog>) super
				.findByCriteria(criteria);
		return tmpWebSysLogList;
	}
	
	/**
	 * 根據開始日期和結束日期查詢前台登入人員
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebLog> findWebLogList(Date startDate, Date endDate,String nLocate){
		List<WebLog> tmpWebLogList = null;
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebLog.class);
			criteria.add(Restrictions.like("nlocate", nLocate, MatchMode.ANYWHERE));
			criteria.add(Restrictions.between("ndate", startDateD,endDateD));
			tmpWebLogList = (List<WebLog>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			tmpWebLogList=new ArrayList<WebLog>();
			log.error("findWebLogList方法報錯：",e);
		}
		return tmpWebLogList;
	}
}
