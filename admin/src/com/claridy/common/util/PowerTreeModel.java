package com.claridy.common.util;
import java.util.List;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.AbstractTreeModel;

import com.claridy.common.mechanism.facase.SysMenuService;
import com.claridy.domain.WebFunction;


@SuppressWarnings("rawtypes")
public class PowerTreeModel extends AbstractTreeModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 449823358151892294L;
	//構造方法，參數為樹根節點
	SysMenuService menuService;	
	@SuppressWarnings("unchecked")
	public PowerTreeModel(Object root,SysMenuService menuService) {
		super(root);		
		this.menuService=menuService;
	}
	//返回parent節點的子節點
	public Object getChild(Object parent, int index) {
		if(parent instanceof WebFunction){
			WebFunction d=(WebFunction)parent;
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			} 
			return menuService.getChildMenu(d,languageType).get(index);
		}else if(parent instanceof List){
			List clist=(List)parent;
			return clist.get(index);
		}
		return null;
	}
    //返回parent節點的子節點數目
	public int getChildCount(Object parent) {
		if(parent instanceof WebFunction){
			WebFunction d=(WebFunction)parent;
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			} 
			return menuService.getChildMenu(d,languageType).size();
		}else if(parent instanceof List){
			List clist=(List)parent;
			return clist.size();
		}
		return 0;
	}
	//判斷某節點是否為子節點
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		if(node instanceof WebFunction){
			WebFunction d=(WebFunction)node;
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			} 
			return Integer.parseInt(menuService.getHasChild(d.getUuid(),languageType).toString())==0?true:false;
		}else if(node instanceof List){
			List clist=(List)node;
			return clist.size()>0?false:true;
		}
		return false;
	}
}


