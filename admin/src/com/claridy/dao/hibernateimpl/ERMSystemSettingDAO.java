package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IERMSystemSettingDAO;
import com.claridy.domain.ErmSystemSetting;
import com.claridy.domain.WebEmployee;

@Repository
public class ERMSystemSettingDAO extends BaseDAO implements
		IERMSystemSettingDAO {
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ERMSystemSettingDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmSystemSetting> findAll(WebEmployee webEmployee) throws DataAccessException {
		//DetachedCriteria criteria = DetachedCriteria.forClass(ERMSystemSetting.class);
		DetachedCriteria criteria=daoimpl.getCriteris(ErmSystemSetting.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions.eq("isDefault", "N"));
		criteria.addOrder(Order.desc("createDate"));
		List<ErmSystemSetting> tmpERMSystemSettingList = (List<ErmSystemSetting>) super
				.findByCriteria(criteria);
		return tmpERMSystemSettingList;
	}

	@SuppressWarnings("unchecked")
	public List<ErmSystemSetting> search(String funName,WebEmployee webEmployee)
			throws DataAccessException {
		//DetachedCriteria criteria = DetachedCriteria.forClass(ERMSystemSetting.class);
		DetachedCriteria criteria=daoimpl.getCriteris(ErmSystemSetting.class, webEmployee);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions
				.like("funcName", funName, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("isDefault", "N"));
		criteria.addOrder(Order.desc("createDate"));
		return (List<ErmSystemSetting>) super.findByCriteria(criteria);
	}
	@SuppressWarnings("unchecked")
	public List<ErmSystemSetting> findSysSettingList(String term){
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + ErmSystemSetting.class.getSimpleName());
		sbHql.append(" WHERE "+term+"");
		Query query = this.getSession().createQuery(sbHql.toString());
		return (List<ErmSystemSetting>) query.list();
	}
	
	public ErmSystemSetting findByFunID(String funId)
			throws DataAccessException {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + ErmSystemSetting.class.getSimpleName());
		sbHql.append(" WHERE isDefault ='N' and isDataEffid=1 and funcId = ? order by createDate");
		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, funId);

		return (ErmSystemSetting) query.uniqueResult();
	}
	
	public ErmSystemSetting getSysByFunID(String funId){
		try {
			StringBuffer sbHql = new StringBuffer();
			sbHql.append(" FROM " + ErmSystemSetting.class.getSimpleName());
			sbHql.append(" WHERE funcId = ? ");
			Query query = this.getSession().createQuery(sbHql.toString());
			query.setParameter(0, funId);

			return (ErmSystemSetting) query.uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			return new ErmSystemSetting();
		}
	}

}
