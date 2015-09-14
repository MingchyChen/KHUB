package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmSysScheduleDAO;
import com.claridy.domain.ErmSysSchedule;
import com.claridy.domain.WebEduTraining;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmSysScheduleDAO extends BaseDAO implements
		IErmSysScheduleDAO {
	
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ErmSysScheduleDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	
	@SuppressWarnings("unchecked")
	public List<ErmSysSchedule> findAll(WebEmployee webEmployee) {
		DetachedCriteria criteria = daoimpl.getCriteris(ErmSysSchedule.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffId", 1));
		List<ErmSysSchedule> scheduleList=(List<ErmSysSchedule>) super.findByCriteria(criteria);
		return scheduleList;
	}


	public ErmSysSchedule findById(String id,WebEmployee webEmployee) {
		DetachedCriteria criteria = daoimpl.getCriteris(ErmSysSchedule.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffId", 1));
		if(id!=null&&!id.equals("")){
			criteria.add(Restrictions.eq("id",id));
		}
		ErmSysSchedule schedule=(ErmSysSchedule) super.findByCriteria(criteria).get(0);
		return schedule;
	}


	@SuppressWarnings("unchecked")
	public ErmSysSchedule findAllByStatus(String uuid) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmSysSchedule.class);
		criteria.add(Restrictions.eq("status", 0));
		if(uuid!=null&&!"".equals(uuid)){
			criteria.add(Restrictions.eq("id",uuid));
		}	
		ErmSysSchedule schedule=(ErmSysSchedule) criteria.getExecutableCriteria(getSession()).uniqueResult();
		return schedule;
	}
	

}
