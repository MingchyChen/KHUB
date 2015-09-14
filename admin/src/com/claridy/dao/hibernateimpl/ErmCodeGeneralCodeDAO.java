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
import com.claridy.dao.IErmCodeGeneralCodeDAO;
import com.claridy.domain.ErmCodeGeneralCode;
import com.claridy.domain.WebEmployee;

@Repository
public class ErmCodeGeneralCodeDAO extends BaseDAO implements IErmCodeGeneralCodeDAO{
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public ErmCodeGeneralCodeDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findAll(WebEmployee webEmployee) throws DataAccessException {
		String hql=daoimpl.getHql("ErmCodeGeneralCode", webEmployee);
		hql += " and isDataEffid = 1";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
        return list;
	}
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> search(String itemId, String generalcodeId,
			String name1, String name2, String yesOrNo, WebEmployee webEmployee)
			throws DataAccessException {
		String hql=daoimpl.getHql("ErmCodeGeneralCode", webEmployee);
		hql += " and isDataEffid = 1";
		if(itemId!=null&&!"".equals(itemId)){
			hql+="and ermCodeItem.itemId like '%"+itemId+"%' ";
		}
		if(generalcodeId!=null&&!"".equals(generalcodeId)){
			hql+="and generalcodeId like '%"+generalcodeId+"%' ";
		}
		if(name1!=null&&!"".equals(name1)){
			hql+="and name1 like '%"+name1+"%' ";
		}
		if(name2!=null&&!"".equals(name2)){
			hql+="and name2 like '%"+name2+"%' ";
		}
		if(yesOrNo!=null&&!"".equals(yesOrNo)){
			hql+="and history like '"+yesOrNo+"' ";
		}
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
        return list;
	}
	/**
	 * 根據條件查詢集合
	 * @param openValue
	 * @param openType
	 * @param name
	 * @param code
	 * @param webEmployee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findGeneralList(String openValue,String openType,String name,String code,WebEmployee webEmployee){
		String hql="from ErmCodeGeneralCode where 1=1 ";
		hql += " and isDataEffid = 1";
		if(openValue.equals("language")){
			if(openType.trim().equals("DBLAN")){
				hql+=" and ermCodeItem.itemId='DBLAN' and history='N' ";
			}else{
				hql+=" and ermCodeItem.itemId='RELAN' and history='N' ";
			}
		}
		if(openValue.equals("DBSUB")){
			hql+=" and ermCodeItem.itemId='DBSUB' and history='N' ";
		}
		if(openValue.equals("EJSUB")){
			hql+=" and ermCodeItem.itemId='EJSUB' and history='N' ";
		}
		if(openValue.equals("EBSUB")){
			hql+=" and ermCodeItem.itemId='EBSUB' and history='N' ";
		}
		if(openValue.equals("WSSUB")){
			hql+=" and ermCodeItem.itemId='WSSUB' and history='N' ";
		}
		if(openValue.equals("DBTYPE")){
			hql+=" and ermCodeItem.itemId='DBTYPE' and history='N' ";
		}
		if (!code.equalsIgnoreCase("")) {
			hql+=" and lower(generalcodeId) like '%" + code.toLowerCase() + "%'";
		}
		if (!name.equalsIgnoreCase("")) {
			hql+=" and lower(name1) like '%" + name.toLowerCase() + "%'";
		}
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
        return list;
	}
	@SuppressWarnings("unchecked")
	public ErmCodeGeneralCode findByItemIDAndGeneralcodeId(String itemId,
			String generalcodeId) {
		// TODO Auto-generated method stub
		Session session=this.getSession();
		String hql="";
		Query query=null;
		hql="from ErmCodeGeneralCode where isDataEffid = 1 and ermCodeItem.itemId= '"+itemId+"' and generalcodeId='"+generalcodeId+"'";
		query=session.createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
		ErmCodeGeneralCode codeRet=new ErmCodeGeneralCode();
		if(list.size()>0){
			codeRet=list.get(0);
		}
		return codeRet;	
	}
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findErmCodeGeneralCodeByItemId(String itemId) {
		String hql="from ErmCodeGeneralCode where isDataEffid = 1 and (history='N' or history='n' or history is null or history='') and ermCodeItem.itemId  ='"+itemId+"'";
		Query query=this.getSession().createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
		return list;
	}
	/**
	 * 根據傳過來的sql返回查詢結果
	 */
	public String getNameBySql(String sql){
		try {
			Session session = this.getSession();
			Query query = session.createSQLQuery(sql);
			Object obj = query.uniqueResult();
			return obj.toString();
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	/**
	 * 根據傳過來的sql返回查詢結果
	 */
	public Object getObjectBySql(String sql){
		try {
			Session session = this.getSession();
			Query query = session.createSQLQuery(sql);
			Object obj = query.uniqueResult();
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			return new Object();
		}
	}
	/**
	 * 根據itemid查詢數據
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findByItemId(WebEmployee webEmployee,String itemId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmCodeGeneralCode.class);
		criteria.add(Restrictions.eq("ermCodeItem.itemId", itemId));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	
	/**
	 * 根據generalcodeId查詢數據
	 *
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ErmCodeGeneralCode> findBygeneralId(WebEmployee webEmployee,String generalId){
		DetachedCriteria criteria=DetachedCriteria.forClass(ErmCodeGeneralCode.class);
		criteria.add(Restrictions.eq("generalcodeId", generalId));
		return criteria.getExecutableCriteria(getSession()).list();
	}
	/**
	 * 根據name和itemId查詢集合
	 * @param name
	 * @param itemId
	 * @param history
	 * @return
	 */
	public List<ErmCodeGeneralCode> findGeneralCodeList(String name,String itemId) {
		String hql="from ErmCodeGeneralCode where isDataEffid = 1 and name1='"+name+"' and ermCodeItem.itemId  ='"+itemId+"'";
		Query query=this.getSession().createQuery(hql);
		List<ErmCodeGeneralCode> list=query.list();
		return list;
	}
}
