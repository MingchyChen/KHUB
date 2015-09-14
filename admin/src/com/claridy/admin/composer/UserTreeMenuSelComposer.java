package com.claridy.admin.composer;


import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.claridy.common.mechanism.facase.SysMenuService;
import com.claridy.common.util.DepartTreeModel;
import com.claridy.common.util.ZkUtils;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebFunction;
import com.claridy.facade.WebOrgListService;

/**
 * 
 * sunchao nj
 * 菜單權限功能
 * 2014/07/18
 */
public class UserTreeMenuSelComposer extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3369391370563245215L;
	@Wire
	private Tree tree;
	DepartTreeModel  btm;
	SysMenuService menuService;
	WebOrgListService orgListService;
	private WebEmployee webEmployee;
	@Listen("onClick=#tree")
	public void showSelectMenu() throws Exception{
		Treeitem item = tree.getSelectedItem();
        if (item.getValue() != null &&!item.getValue().equals("")) {
        	String conLbl="";
        	String lbl=BuildPath(item,conLbl);
        	lbl=lbl.substring(0,lbl.length()-1);
        	Desktop dkp = Executions.getCurrent().getDesktop();
    		Page page = dkp.getPageIfAny("templatePage");
    		Label contentLabel = (Label) page.getFellowIfAny("contentLabel");
    		Include contentInclude = (Include) page.getFellowIfAny("contentInclude");
    		contentLabel.setValue(lbl.toString());
        	contentInclude.setSrc(item.getValue() + ".zul");
        }else{
        	if (item.isOpen())
                item.setOpen(false);
            else
                item.setOpen(true);
        }
	}
	/**
	 * 返回不同菜單的麵包屑
	 * @param item
	 * @param conLbl
	 * @return
	 */
	public String BuildPath(Treeitem item,String conLbl){
		String tmpPath;
		tmpPath=item.getLabel() + ">" + conLbl;
		 if(item.getParentItem()!=null&&!"".equals(item.getParentItem())){
		    tmpPath=BuildPath(item.getParentItem(),tmpPath);
		 }
		return tmpPath;
	}
	public UserTreeMenuSelComposer(){
		if(ZkUtils.getSessionAttribute("loginOK")!=null){
			String languageType=null;
			Session session=Sessions.getCurrent();
			if(session.getAttribute("now_Locale")!=null){
				languageType=session.getAttribute("now_Locale").toString();
			} 
			menuService=(SysMenuService) SpringUtil.getBean("sysMenuService");
			btm=new DepartTreeModel(menuService.findMenuList2(languageType),menuService);	
		}else{
			ZkUtils.sendRedirect("/login");
		}
	}	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		tree.setModel(btm);
		tree.setTreeitemRenderer(new TreeitemRenderer<WebFunction>() {
			public void render(Treeitem item, WebFunction data, int index)
					throws Exception {
				orgListService=(WebOrgListService) SpringUtil.getBean("webOrgListService");	
				//獲取用戶資訊
				webEmployee = (WebEmployee) ZkUtils.getSessionAttribute("webEmployee");
				List<String> funcUUidlist= orgListService.getFunctionEmployList(webEmployee);
				boolean bool = funcUUidlist.contains(data.getUuid());
				if(bool==true){
					item.setOpen(true);
					item.setLabel(Labels.getLabel(data.getMutiFuncId()));
					item.setValue(data.getUrl());
				}else{
					int result=orgListService.getSumFunctionUuid(data.getUuid(),webEmployee);
					if(result>0){
						item.setOpen(true);
						item.setLabel(data.getFuncName());
						item.setValue(data.getUrl());
					}else{
						item.setLabel("");
						item.setValue("");
						item.setVisible(false);
					}
				}
			}
		});
	}
}
