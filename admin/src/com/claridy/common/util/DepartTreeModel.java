package com.claridy.common.util;
import java.util.List;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.AbstractTreeModel;

import com.claridy.common.mechanism.facase.SysMenuService;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunction;


@SuppressWarnings("rawtypes")
public class DepartTreeModel extends AbstractTreeModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 449823358151892294L;
	//构造方法，参数为树根节点
	SysMenuService menuService;	
	private WebEmployee webEmployee;
	@SuppressWarnings("unchecked")
	public DepartTreeModel(Object root,SysMenuService menuService) {
		super(root);		
		this.menuService=menuService;
	}
	//有了父节点后，获得子节点，index为第几个孩子
	public Object getChild(Object parent, int index) {
		if(parent instanceof WebFunction){
			WebFunction d=(WebFunction)parent;
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			}
			return menuService.getChildMenu2(d,languageType).get(index);
		}else if(parent instanceof List){
			List clist=(List)parent;
			return clist.get(index);
		}
		return null;
	}
    //返回parent节点的子节点数目
	public int getChildCount(Object parent) {
		if(parent instanceof WebFunction){
			WebFunction d=(WebFunction)parent;
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			}
			return menuService.getChildMenu2(d,languageType).size();
		}else if(parent instanceof List){
			List clist=(List)parent;
			return clist.size();
		}
		return 0;
	}
	//判断某节点是否为子节点
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
	@SuppressWarnings("unused")
	private int getLevel(String data) {
        for (int i = -1, level = 0;; ++level)
            if ((i = data.indexOf('.', i + 1)) < 0)
                return level;
    }
}


