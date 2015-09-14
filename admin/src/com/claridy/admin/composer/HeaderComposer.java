package com.claridy.admin.composer;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.claridy.common.util.ZkUtils;

public class HeaderComposer extends SelectorComposer<Component> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3369391370563245215L;
	
	@Listen("onClick=#toLogot")
	public void toLogot() throws Exception{
		ZkUtils.getSessionAttributes().remove("loginOK");
		ZkUtils.sendRedirect("/login");
	}
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(ZkUtils.getSessionAttribute("loginOK")!=null){
			boolean loginok=(Boolean) ZkUtils.getSessionAttribute("loginOK");
			if(!loginok){
				ZkUtils.sendRedirect("/login");
			}
		}else{
			ZkUtils.sendRedirect("/login");
		}
	}
}
