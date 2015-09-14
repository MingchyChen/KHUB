package com.claridy.facade;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.domain.Sys_Pagination;
import com.claridy.dao.ISys_UserDAO;
import com.claridy.domain.Sys_User;

@Service
public class SystemUserService {

	@Autowired
	public ISys_UserDAO systemUserDAO;

	/**
	 * 後臺分頁查詢
	 * @param matter
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Sys_Pagination getSysUserList(String name, String[] sort, int currentPage, int pageSize) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(Sys_User.class);
		// 標題
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("user_name", "%" + name+ "%"));
		}
		// 設定排序條件
		criteria.add(Restrictions.sqlRestriction("1=1 and user_name!='admin' and isdelete=0"));// 預設條件   
		criteria.addOrder(Order.desc("created_date"));
		

	
		Sys_Pagination Sys_Pagination = systemUserDAO.findByCriteria(
				criteria, currentPage, pageSize);
		return Sys_Pagination;
   	}
	/**
	 * 新增方法
	 * @param webopus
	 */
	public void saveSysUser(Sys_User sysuser){
		systemUserDAO.saveOrUpdate(sysuser);
	}
	/**
	 * 修改方法
	 * @param webopus
	 */
	public void updateSysUser(Sys_User sysuser){
		systemUserDAO.saveOrUpdate(sysuser);
	}
	/**
	 * 刪除方法
	 * @param webopus
	 */
	public void delSysUser(Sys_User sysuser){
		systemUserDAO.saveOrUpdate(sysuser);
	}
	/**
	 * 根據主鍵查詢資訊
	 * @param uuid
	 * @return
	 */
	public Sys_User findSysUser(String name){
		return systemUserDAO.findSysUser(name);
	}
 
}
