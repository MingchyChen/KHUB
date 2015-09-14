package com.claridy.common.mechanism.facase;

import java.util.ArrayList;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.IWebHomeLinkDAO;
import com.claridy.common.mechanism.domain.WebHomeLink;

@Service
public class WebHomeLinkService {

   	@Autowired
	private IWebHomeLinkDAO sys_MenuDAO;

   	//存放系統選單資訊，宣告為static是為了不會每次都要重新查詢資料庫來獲得選單資訊
   	private static List<WebHomeLink> sys_MenuList = null;
   	
   	public List<WebHomeLink> getSys_MenuList() {
		return sys_MenuList;
	}

	public void setSys_MenuList(List<WebHomeLink> sys_MenuList) {
		this.sys_MenuList = sys_MenuList;
	}

	//產生SysMenu
   	public void genSysMenu() {
   		if( sys_MenuList == null ) {
   			sys_MenuList = new ArrayList<WebHomeLink>();
   			this.getNextLevel("0");
   		}
   	}
   	
   	//找下一層SysMenu
   	private void getNextLevel(String parent) {   		
   		DetachedCriteria criteria = DetachedCriteria.forClass(WebHomeLink.class);
   		criteria.add(Restrictions.eq("parent", parent));
   		criteria.addOrder(Order.asc("seq"));
   		criteria.addOrder(Order.asc("func_no"));
   		List sysMenuListTmp = sys_MenuDAO.findByCriteria(criteria);
   		
   		int i = 0;
   		int size = sysMenuListTmp.size();
   		WebHomeLink sysMenuTmp = null;
   		for(;i < size;i++) {
   			sysMenuTmp = (WebHomeLink)sysMenuListTmp.get(i);
   			sys_MenuList.add(sysMenuTmp);
   			this.getNextLevel(sysMenuTmp.getFunc_no());
   		}
   		sysMenuTmp = null;
   		
   		sysMenuListTmp = null;
   	}
}