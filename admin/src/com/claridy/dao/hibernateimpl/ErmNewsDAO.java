package com.claridy.dao.hibernateimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmNewsDAO;
import com.claridy.domain.ErmNews;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmNewsDAO extends BaseDAO implements IErmNewsDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmNewsDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmNews> findAllErmNews(WebEmployee webEmployee) {
		Date today = new Date();
		// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		// String hql = daoimpl.getHql("ErmNews", webEmployee);
		// hql += " and isDataEffid=1 and "+sdf.format(today)+"<= closeDate";
		// Query query = this.getSession().createQuery(hql);
		// List<ErmNews> list = query.list();
		// return list;
		DetachedCriteria criteria = daoimpl.getCriteris(ErmNews.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		// criteria.add(Restrictions.ge("closeDate", today));
//		criteria.add(Restrictions.or(Restrictions.ge("closeDate", today),
//				Restrictions.isNull("closeDate")));
		List<ErmNews> list = (List<ErmNews>) super.findByCriteria(criteria);
		return list;

	}

	@SuppressWarnings("unchecked")
	public List<ErmNews> findErmNewsByParam(String title, String onDown,
			WebEmployee webEmployee) {
		Date today = new Date();
		DetachedCriteria criteria = daoimpl.getCriteris(ErmNews.class,
				webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		// if (coding != null && !"".equals(coding)) {
		// criteria.add(Restrictions.like("newsId", coding));
		// }
		if (title != null && !"".equals(title)) {
			criteria.add(Restrictions.like("matterZhTw", title));
		}
		//
		// if (date != null) {
		// criteria.add(Restrictions.le("startDate", date));
		// criteria.add(Restrictions.ge("endDate", date));
		// }
		if (onDown.equals("1")) {
			criteria.add(Restrictions.or(Restrictions.ge("closeDate", today),
					Restrictions.isNull("closeDate")));
		} else if(onDown.equals("0")){
			criteria.add(Restrictions.and(Restrictions.le("closeDate", today),
					Restrictions.isNotNull("closeDate")));
		}
		List<ErmNews> list = (List<ErmNews>) super.findByCriteria(criteria);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<ErmNews> findedtAddList(String searchType, String searchValue) {
		Session session = this.getSession();
		String hql = "";
		Query query = null;
		if (searchType == null && searchValue == null) {
			hql = "from ErmNews where isDataEffid=1";
			query = session.createQuery(hql);
		} else {
			hql = "from ErmNews where isDataEffid=1 and " + searchType + "='"
					+ searchValue + "'";
			query = session.createQuery(hql);
		}
		List<ErmNews> list = query.list();
		return list;
	}
}
