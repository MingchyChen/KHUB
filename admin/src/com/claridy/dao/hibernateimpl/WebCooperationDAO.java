package com.claridy.dao.hibernateimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.common.util.DaoImplUtil;
import com.claridy.dao.IWebCooperationDAO;
import com.claridy.domain.WebCooperation;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOrg;

@Repository
public class WebCooperationDAO extends BaseDAO implements IWebCooperationDAO {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	protected DaoImplUtil daoimpl;
	@Autowired
	public WebCooperationDAO(HibernateTemplate hibernateTemplate){
		setHibernateTemplate(hibernateTemplate);
	}
	/**
	 * 查詢方法
	 */
	
	@SuppressWarnings("unchecked")
	public List<WebCooperation> find(WebOrg applyWebOrg,WebEmployee webEmployee,WebOrg acceptWebOrg,String uuid,String acceptPeople,int status,Date startDate,Date endDate) {
		DetachedCriteria criteria = daoimpl.getWebCooperationCriteris(WebCooperation.class,	 webEmployee);
		criteria.createAlias("applyAccount", "appacc");
		criteria.createAlias("acceptEmployee.parentWebOrg", "acceptWeborg");
		if(uuid!=null&&!"".equals(uuid)){
			criteria.add(Restrictions.eq("uuid",uuid));
		}
		if(applyWebOrg!=null&&applyWebOrg.getOrgId()!=null&&!applyWebOrg.getOrgId().equals("")){
			criteria.add(Restrictions.eq("appacc.parentorgid",applyWebOrg.getOrgId()));
		}
		if(acceptWebOrg!=null&&acceptWebOrg.getOrgId()!=null&&!acceptWebOrg.getOrgId().equals("")){
			criteria.add(Restrictions.eq("acceptWeborg.orgId",acceptWebOrg.getOrgId()));
		}
		/*if(acceptPeople!=null&&!acceptPeople.equals("")){
			criteria.add(Restrictions.like("applyAccount.nameZhTw", acceptPeople));
		}*/
		if(status!=-1){
			criteria.add(Restrictions.eq("status",status));
		}
		criteria.add(Restrictions.eq("isDataEffid",1));
		
		if(startDate!=null&&!startDate.equals("")&&endDate!=null&&!"".equals(endDate)){
			criteria.add(Restrictions.between("createDate", startDate,endDate));
		}else if(startDate!=null&&!startDate.equals("")){
			criteria.add(Restrictions.ge("createDate", startDate));
		}else if(endDate!=null&&!"".equals(endDate)){
			criteria.add(Restrictions.le("createDate", endDate));
		}
		criteria.addOrder(Order.asc("status"));
		criteria.addOrder(Order.asc("createDate"));
		List<WebCooperation> webCooperationList=(List<WebCooperation>) criteria.getExecutableCriteria(getSession()).list();
		return webCooperationList;
	}

	/**
	 * 根據開始日期和結束日期查詢館合申請人員
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebCooperation> findWebCooperationList(Date startDate, Date endDate){
		List<WebCooperation> tmpWebCooperList = null;
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
			DetachedCriteria criteria = DetachedCriteria.forClass(WebCooperation.class);
			criteria.add(Restrictions.between("createDate", startDateD,endDateD));
			tmpWebCooperList = (List<WebCooperation>) super
					.findByCriteria(criteria);
		} catch (Exception e) {
			tmpWebCooperList=new ArrayList<WebCooperation>();
			log.error("findWebCooperationList報錯：",e);
		}
		return tmpWebCooperList;
	}
	
	/**
	 * 根據開始日期和結束日期查詢申請館合資料庫統計
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findApplyCooperResList(String startDate, String endDate){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String hql="select acco.parentorgid as parentorgid,coop.dbid as dbid,COUNT(*) as applynumber from WebCooperation  " +
				"coop left join webaccount acco on coop.accountid=acco.uuid " +
				"where CONVERT(varchar(100), coop.createdate, 121) between '"+startDate+"' and '"+endDate+"' and coop.isdataeffid=1 group by  " +
				"parentorgid,dbid";
		Query query = session.createSQLQuery(hql);
		List<Object> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
	/**
	 * 根據單位Id和資料庫Id查詢申請次數
	 * @param parentOrgId
	 * @param dbId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findCooperNumberList(String startDate, String endDate,String parentOrgId,String dbId){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String sql="select acco.parentorgid as parentorgid,coop.dbid as dbid,coop.status,COUNT(*) as applynumber from  " +
				"WebCooperation coop left join webaccount acco on coop.accountid=acco.uuid where CONVERT(varchar(100), coop.createdate, 121) between '"+startDate+"'  " +
				"and '"+endDate+"' and coop.isdataeffid=1 and acco.parentorgid= " +
				"'"+parentOrgId+"' and coop.dbid='"+dbId+"' group by parentorgid,dbid,coop.status";
		Query query = session.createSQLQuery(sql);
		List<Object> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
	
	/**
	 * 根據開始日期和結束日期提供單位資料庫Id以及申請單狀態查詢
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebCooperation> findWebCooperationListByStatus(String startDate, String endDate,String parentOrgId,String dbId,String status){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String hql="from WebCooperation where isDataEffid=1 and createDate between '"+startDate+"' and '"+endDate+"' ";
		if(parentOrgId!=null&&!parentOrgId.equals("")){
			hql+=" and applyAccount.parentorgid='"+parentOrgId+"'  ";
		}
		if(dbId!=null&&!dbId.equals("")){
			hql+=" and dbid='"+dbId+"'  ";
		}
		if(status!=null&&!status.equals("")){
			hql+=" and status='"+status+"'  ";
		}
		Query query = session.createQuery(hql);
		List<WebCooperation> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
	
	/**
	 * 根據開始日期和結束日期查詢館合提供單位統計
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findCooperUnitsList(String startDate, String endDate){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String hql="select emp.parentorgid as parentorgid,COUNT(*) as applynumber from WebCooperation  " +
				"coop left join webemployee emp on coop.employeesn=emp.employeesn  inner join (select uuid from webaccount acc inner join weborg org on acc.parentorgid=org.orgid where org.isdataeffid=1) acc on coop.accountid=acc.uuid " +
				"where CONVERT(varchar(100), coop.createdate, 121) between '"+startDate+"' and '"+endDate+"' and coop.isdataeffid=1 group by  " +
				"parentorgid";
		Query query = session.createSQLQuery(hql);
		List<Object> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
	/**
	 * 根據單位Id和資料庫Id查詢申請次數
	 * @param parentOrgId
	 * @param dbId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findCooperUnitsNumberList(String startDate, String endDate,String parentOrgId){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String sql="select emp.parentorgid as parentorgid,coop.status,COUNT(*) as applynumber from  " +
				"WebCooperation coop left join webemployee emp on coop.employeesn=emp.employeesn   inner join (select uuid from webaccount acc inner join weborg org on acc.parentorgid=org.orgid where org.isdataeffid=1) acc on coop.accountid=acc.uuid where CONVERT(varchar(100), coop.createdate, 121) between '"+startDate+"'  " +
				"and '"+endDate+"' and coop.isdataeffid=1 and emp.parentorgid='"+parentOrgId+"' group by parentorgid,coop.status";
		Query query = session.createSQLQuery(sql);
		List<Object> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
	
	/**
	 * 根據開始日期和結束日期提供單位資料庫Id以及申請單狀態查詢館合提供單位統計
	 * @param startDate
	 * @param endDate
	 * @param nLocate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<WebCooperation> findCooperUnitsListByStatus(String startDate, String endDate,String parentOrgId,String status){
		if (startDate != null && !"".equals(startDate)) {
			startDate+=" 00:00:00";
		}
		if (endDate != null && !"".equals(endDate)) {
			endDate+=" 23:59:59";
		}
		Session session=this.getSession();
		String hql="from WebCooperation where isDataEffid=1 and createDate between '"+startDate+"' and '"+endDate+"' ";
		if(parentOrgId!=null&&!parentOrgId.equals("")){
			hql+=" and acceptEmployee.parentWebOrg.orgId='"+parentOrgId+"'  ";
		}
		if(status!=null&&!status.equals("")){
			hql+=" and status='"+status+"'  ";
		}
		Query query = session.createQuery(hql);
		List<WebCooperation> tmpWebCooperList=query.list();
		return tmpWebCooperList;
	}
}
