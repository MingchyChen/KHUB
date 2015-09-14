package com.claridy.common.mechanism.facase;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.DataAccessException;
import com.claridy.common.mechanism.dao.ISys_MenuDAO;
import com.claridy.common.mechanism.domain.Sys_Menu;
import com.claridy.domain.WebFunction;


@Service
public class SysMenuService {

   	@Autowired
	private ISys_MenuDAO sys_MenuDAO;

   	//存放系統選單資訊，宣告為static是為了不會每次都要重新查詢資料庫來獲得選單資訊
   	private static List<Sys_Menu> sys_MenuList = null;
   	
   	public List<WebFunction> findMenuList(String languageType){
   		List<WebFunction> menuList = null;
   		menuList=sys_MenuDAO.findAll(languageType);
   		return menuList;
   	}
   	public List<WebFunction> findMenuList2(String languageType){
   		List<WebFunction> menuList = null;
   		menuList=sys_MenuDAO.findAll(languageType);
   		return menuList;
   	}
   	public List<WebFunction> findAllList(String languageType) throws DataAccessException{
   		List<WebFunction> menuList = null;
   		menuList=sys_MenuDAO.findAllList(languageType);
   		return menuList;
   	}
   	public List<WebFunction> getChildMenu(WebFunction webfunction,String languageType){
   		List<WebFunction> menuList = null;
   		menuList=sys_MenuDAO.getChildMenu(webfunction,languageType);
   		return menuList;
   	}
   	public List<WebFunction> getChildMenu2(WebFunction webfunction,String languageType){
   		List<WebFunction> menuList = null;
   		menuList=sys_MenuDAO.getChildMenu(webfunction,languageType);
   		return menuList;
   	}
   	public Object getHasChild(String uuid,String languageType) {
		// TODO Auto-generated method stub
		return sys_MenuDAO.getHasChild(uuid, languageType);
	}
   	public List<Sys_Menu> getSys_MenuList() {
		return sys_MenuList;
	}

	public void setSys_MenuList(List<Sys_Menu> sys_MenuList) {
		this.sys_MenuList = sys_MenuList;
	}

	//產生SysMenu
   	public void genSysMenu() {
   		if( sys_MenuList == null ) {
   			sys_MenuList = new ArrayList<Sys_Menu>();
   			this.getNextLevel("0");
   		}
   	}
   	
   	//找下一層SysMenu
   	private void getNextLevel(String parent) {   		
   		DetachedCriteria criteria = DetachedCriteria.forClass(Sys_Menu.class);
   		criteria.add(Restrictions.eq("parent", parent));
   		criteria.addOrder(Order.asc("seq"));
   		criteria.addOrder(Order.asc("func_no"));
   		List sysMenuListTmp = sys_MenuDAO.findByCriteria(criteria);
   		
   		int i = 0;
   		int size = sysMenuListTmp.size();
   		Sys_Menu sysMenuTmp = null;
   		for(;i < size;i++) {
   			sysMenuTmp = (Sys_Menu)sysMenuListTmp.get(i);
   			sys_MenuList.add(sysMenuTmp);
   			this.getNextLevel(sysMenuTmp.getFunc_no());
   		}
   		sysMenuTmp = null;
   		
   		sysMenuListTmp = null;
   	}
}