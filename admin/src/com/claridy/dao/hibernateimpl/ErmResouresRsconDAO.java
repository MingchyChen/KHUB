package com.claridy.dao.hibernateimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.IErmResouresRsconDAO;
import com.claridy.domain.ErmResourcesRscon;

@Repository
public class ErmResouresRsconDAO extends BaseDAO implements
		IErmResouresRsconDAO {

	
	@Autowired
	public ErmResouresRsconDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<ErmResourcesRscon> findbyDate(Date sdate,Date eDate, String status) {
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmResourcesRscon.class);
		if(sdate!=null&&!sdate.equals("")&&eDate!=null&&!eDate.equals("")){
			criteria.add(Restrictions.between("ckdate", sdate, eDate));
		}
		if(sdate!=null&&!sdate.equals("")){
			criteria.add(Restrictions.ge("ckdate", sdate));
		}
		if(eDate!=null&&!eDate.equals("")){
			criteria.add(Restrictions.le("ckdate", eDate));
		}
		if(status!=null&&!status.equals("0")&&!status.equals("")){
			criteria.add(Restrictions.eq("urlcon",status));
		}
		criteria.addOrder(Order.desc("ckdate"));
		return (List<ErmResourcesRscon>) super.findByCriteria(criteria);
	}

}
