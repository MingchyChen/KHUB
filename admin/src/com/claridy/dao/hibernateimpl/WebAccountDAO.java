package com.claridy.dao.hibernateimpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebAccountDAO;
import com.claridy.domain.WebAccount;
import com.claridy.domain.WebEmployee;

@Repository
public class WebAccountDAO extends BaseDAO implements IWebAccountDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebAccountDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	
	@SuppressWarnings("unchecked")
	public List<WebAccount> findWebAccount(WebAccount webAccount,int isregister,int isdataeffid,WebEmployee webEmployee){
		String hql=daoimpl.getWebAccountmpHql("WebAccount", webEmployee);
		hql+=" "+findAccount(webAccount, isdataeffid);
		if(isregister==1){
			hql+=" and isRegister="+isregister;
		}else if(isregister==0){
			hql+=" and (isRegister!=1 or isRegister is null)";
		}
		hql+=" order by createDate desc";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return (List<WebAccount>)query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<WebAccount> findAccount(WebAccount webAccount,WebEmployee webEmployee){
		String hql=daoimpl.getWebAccountmpHql("WebAccount", webEmployee);
		hql+=" and accountId='"+webAccount.getAccountId()+"'";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return (List<WebAccount>)query.list();
	}
	
	public String findAccount(WebAccount webAccount,int isdataeffid){
		String strHQL="";
		if(webAccount!=null){
			if(webAccount.getUuid()!=null&&!webAccount.getUuid().equals("")){
				strHQL+=" and uuid like'"+webAccount.getUuid()+"'";
			}
			if(webAccount.getAccountId()!=null&&!webAccount.getAccountId().equals("")){
				
				strHQL+=" and accountId like'%"+webAccount.getAccountId()+"%'";
			}
			if(webAccount.getNameZhTw()!=null&&!webAccount.getNameZhTw().equals("")){
				
				strHQL+=" and nameZhTw like'%"+webAccount.getNameZhTw()+"%'";
				

				
			}
			if(webAccount.getParentorgid()!=null&&!"".equals(webAccount.getParentorgid())){
				
				strHQL+=" and parentorgId='"+webAccount.getParentorgid()+"'";
				

			}
			if(webAccount.getStatus()!=-1){
				strHQL+=" and (status="+webAccount.getStatus();
			}
			if(webAccount.getType()!=0){
				
				strHQL+=" and type="+webAccount.getType();
			
			}
			
			if(webAccount.getIsCheck()!=-1){
				strHQL+=" and isCheck="+webAccount.getIsCheck();
			}
			
		}
		if(isdataeffid==0){
			strHQL+=" or isDataEffid="+isdataeffid+")";
		}else{
			if(strHQL.indexOf("(")!=-1){
				strHQL+=" and isDataEffid="+isdataeffid+")";
			}else{
				strHQL+=" and isDataEffid="+isdataeffid;
			}
		}
		
		return strHQL;
	}

	public WebAccount findById(String name) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebAccount.class);
		criteria.add(Restrictions.eq("titleZhTw",name));
		return (WebAccount) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}
	/**
	 * 根據uuid查詢account對象
	 * @param accountId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public WebAccount getAccount(String accountId) {
		DetachedCriteria criteria=DetachedCriteria.forClass(WebAccount.class);
		criteria.add(Restrictions.eq("uuid",accountId));
		List<WebAccount> accountListRet=(List<WebAccount>)super.findByCriteria(criteria);
		if(accountListRet.size()>0){
			WebAccount webAccountRet=accountListRet.get(0);
			return webAccountRet;
		}else{
			return null;
		}
	}
	/**
	 * 查詢全部使用者
	 * @return
	 */
	public List<WebAccount> findAccountList(){
		String hql="from WebAccount where isDataEffid=1";
		Session session=this.getSession();
		Query query=session.createQuery(hql);
		return (List<WebAccount>)query.list();
	}
	
	/**
	 * 根據時間查詢申請次數
	 * @param parentOrgId
	 * @param dbId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findAccApplyNumList(String startDate, String endDate){
		List<Object> tmpWebCooperList=new ArrayList<Object>(); 
		try {
			if (startDate != null && !"".equals(startDate)) {
				startDate+=" 00:00:00";
			}
			if (endDate != null && !"".equals(endDate)) {
				endDate+=" 23:59:59";
			}
			Session session=this.getSession();
			String sql="select parentorgid,COUNT(*) as applynumber from webaccount where isDataEffid=1 and CONVERT(varchar(100), createdate, 121)  " +
					"between '"+startDate+"' and '"+endDate+"' group by parentorgid";
			Query query = session.createSQLQuery(sql);
			tmpWebCooperList=query.list();
		} catch (Exception e) {
			log.error("findAccApplyNumList報錯:",e);
		}
		return tmpWebCooperList;
	}
	
	/**
	 * 根據日期和單位查詢申請次數
	 * @param parentOrgId
	 * @param dbId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findAccApplyNumListByParent(String startDate, String endDate,String parentOrgId){
		List<Object> tmpWebCooperList=new ArrayList<Object>(); 
		try {
			if (startDate != null && !"".equals(startDate)) {
				startDate+=" 00:00:00";
			}
			if (endDate != null && !"".equals(endDate)) {
				endDate+=" 23:59:59";
			}
			Session session=this.getSession();
			String sql="select orgid,COUNT(*) as applynumber from webaccount where isDataEffid=1 and CONVERT(varchar(100), createdate, 121)  " +
					"between '"+startDate+"' and '"+endDate+"' and parentorgid='"+parentOrgId+"' group by orgid";
			Query query = session.createSQLQuery(sql);
			tmpWebCooperList=query.list();
		} catch (Exception e) {
			log.error("findAccApplyNumListByParent報錯:",e);
		}
		return tmpWebCooperList;
	}
	/**
	 * 根據開始日期和結束日期提供單位查詢
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebAccount> findWebAccListByParent(String startDate, String endDate,String parentOrgId){
		List<WebAccount> tmpWebAccList=new ArrayList<WebAccount>();
		try {
			if (startDate != null && !"".equals(startDate)) {
				startDate+=" 00:00:00";
			}
			if (endDate != null && !"".equals(endDate)) {
				endDate+=" 23:59:59";
			}
			Session session=this.getSession();
			String hql="from WebAccount where isDataEffid=1 and createDate between '"+startDate+"' and '"+endDate+"' ";
			if(parentOrgId!=null&&!parentOrgId.equals("")){
				hql+=" and parentorgid='"+parentOrgId+"'  ";
			}
			Query query = session.createQuery(hql);
			tmpWebAccList=query.list();
		} catch (Exception e) {
			log.error("findWebAccListByParent報錯:",e);
		}
		return tmpWebAccList;
	}
}
