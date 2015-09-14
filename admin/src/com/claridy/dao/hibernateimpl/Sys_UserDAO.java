package com.claridy.dao.hibernateimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.claridy.common.mechanism.dao.hibernateimpl.BaseDAO;
import com.claridy.dao.ISys_UserDAO;
import com.claridy.domain.Sys_User;

@Repository
public class Sys_UserDAO extends BaseDAO implements ISys_UserDAO {
	@Autowired
	public Sys_UserDAO(HibernateTemplate hibernateTemplate) {
		setHibernateTemplate(hibernateTemplate);
	}
	// 根據主鍵進行查詢
	public Sys_User findSysUser(String name) {
		String hql = "from Sys_User where user_name='" + name + "'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Sys_User> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return new Sys_User();
		}
	}
	public Sys_User getUser(String username,String password){
		String hql = "from Sys_User where user_name='" + username + "' and user_pwd='" + password + "'";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		List<Sys_User> list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return new Sys_User();
		}
	}
}
