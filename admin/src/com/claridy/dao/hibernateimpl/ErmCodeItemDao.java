package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IErmCodeItemDao;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.ErmCodeItem;
import com.claridy.domain.WebEmployee;


@Repository
public class ErmCodeItemDao extends BaseDAO implements IErmCodeItemDao {

	@Autowired
	protected DaoImplUtil daoimpl;

	@Autowired
	public ErmCodeItemDao(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodeItem> findErmCodeItem(ErmCodeItem ermCodeItem,
			WebEmployee webemployee) throws DataAccessException {
		String hql=daoimpl.getHql("ErmCodeItem", webemployee);
		
		hql += " and isDataEffid = 1";

		if (ermCodeItem.getItemId()!=null&&!ermCodeItem.getItemId().equals("")) {
			hql += " and itemId like '%"+ermCodeItem.getItemId()+"%'";
		}
		if (ermCodeItem.getName1()!=null&&!ermCodeItem.getName1().equals("")) {
			hql += " and name1 like '%"+ermCodeItem.getName1()+"%'";
		}
		if (ermCodeItem.getName2()!=null&&!ermCodeItem.getName2().equals("")) {
			hql += " and name2 like '%"+ermCodeItem.getName2()+"%'";
		}
		if (ermCodeItem.getHistory()!=null&&!ermCodeItem.getHistory().equals("")) {
			hql += " and history = '"+ermCodeItem.getHistory()+"'";
		}
		
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return query.list();
	}

	public ErmCodeItem findByItemId(String itemId) throws DataAccessException {
		StringBuffer sbHql = new StringBuffer();
		sbHql.append(" FROM " + ErmCodeItem.class.getSimpleName());
		sbHql.append(" WHERE isDataEffid = 1 and itemId = ? order by createDate");
		Query query = this.getSession().createQuery(sbHql.toString());
		query.setParameter(0, itemId);

		return (ErmCodeItem) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodeItem> findByhistory(WebEmployee webemployee)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String hql=daoimpl.getHql("ErmCodeItem", webemployee);
		hql += " and isDataEffid = 1";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return query.list();
	}

	public void deleteItems(String itemId) throws DataAccessException {
		Session session=this.getSession();
		String deleteHql = "update ErmCodeGeneralCode set isDataEffid = 0 where ermCodeItem.itemId = '"+itemId+"'";
		Query query=session.createQuery(deleteHql);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeItemId(String itemId,WebEmployee webemployee)
			throws DataAccessException {
		String hql=daoimpl.getHql("ErmCodeGeneralCode", webemployee);
		hql += " and isDataEffid = 1 and ermCodeItem.itemId = '"+itemId+"'";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return query.list();
	}

}
