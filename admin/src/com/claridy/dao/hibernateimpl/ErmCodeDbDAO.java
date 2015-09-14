package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmCodeDbDAO;
import com.claridy.domain.ErmCodeDb;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmCodeDbDAO extends BaseDAO implements IErmCodeDbDAO {
	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmCodeDbDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodeDb> findPublisherList(String openValue, String openType,
			String name, String code, WebEmployee webEmployee) {
		String hql = "from ErmCodeDb where isDataEffid = 1";
		if (!code.equalsIgnoreCase("")) {
			hql += " and lower(dbId) like '%" + code.toLowerCase() + "%'";
		}
		if (!name.equalsIgnoreCase("")) {
			hql += " and lower(name) like '%" + name.toLowerCase() + "%'";
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<ErmCodeDb> list = query.list();
		return list;
	}

	public List<ErmCodeDb> findcodeDbByDbId(String dbId, WebEmployee webEmployee) {
		// TODO Auto-generated method stub
		String hql = "from ErmCodeDb where isDataEffid = 1 and dbId='" + dbId + "'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<ErmCodeDb> list = query.list();
		return list;
	}

	public List<ErmCodeDb> findAllCodeDb(WebEmployee webEmployee) {
		// TODO Auto-generated method stub
		String hql = daoimpl.getHql("ErmCodeDb", webEmployee);
		// hql += " and isDataEffid = 1";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<ErmCodeDb> list = query.list();
		return list;
	}

	/**
	 * 根據名稱查詢資料庫集合
	 * 
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodeDb> findErmCodeDbList(String name)
			throws DataAccessException {
		DetachedCriteria criteria = DetachedCriteria.forClass(ErmCodeDb.class);
		criteria.add(Restrictions.eq("isDataEffid", 1));
		criteria.add(Restrictions.eq("name", name));
		return (List<ErmCodeDb>) super.findByCriteria(criteria);
	}

	public List<ErmCodeDb> findAllCodeDbIsUse() {
		// String hql=daoimpl.getHql("ErmCodeDb", webEmployee);
		String hql = "from ErmCodeDb where isDataEffid = 1";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<ErmCodeDb> list = query.list();
		return list;
	}
	public ErmCodeDb getErmDbByDbId(String dbId) {
		String hql = "from ErmCodeDb where dbId='" + dbId + "'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<ErmCodeDb> list = query.list();
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public void updateErmEjebItemByDbId(String dbId) {
		String sql = "update erm_resources_ejeb_item set history='Y' where db_id='"+dbId+"'";
		Session session = this.getSession();
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
	}
}
