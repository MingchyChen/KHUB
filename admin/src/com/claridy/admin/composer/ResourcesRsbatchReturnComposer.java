package com.claridy.admin.composer;

import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import com.claridy.common.util.ZkUtils;

public class ResourcesRsbatchReturnComposer extends SelectorComposer<Component>{
	@Wire
	private Label path_label;//頁面路徑
	@Wire
	private Label success_resources;//上傳成功筆數
	@Wire
	private Button leava_btn;//離開
	@Wire
	private Button return_btn;//要返回的頁面
	@Wire
	private Label warn1;//上傳成功:共
	@Wire
	private Label warn2;//筆
	//多語系
	private String message1,message2;
	private String resources_code_id;
	/**
	 * 加載數據
	 */
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		//得到當前語系
//		Sys sys=new Sys();
//		java.util.Locale locale = Locales.getLocale(sys.getLocale());
//		Locales.setThreadLocal(locale);
		Session session=Sessions.getCurrent();
		//session.setAttribute("comb_resource_type", resources_code_id);
		 resources_code_id=(String) session.getAttribute("comb_resource_type");
		String pageTitle=Labels.getLabel("resources_rsbatch_return.pageTitle");
		message1=Labels.getLabel("resources_rsbatch_return.message1");
		message2=Labels.getLabel("warnMessage");
		String returnMes=Labels.getLabel("resources_rsbatch_return.returnMes");
		String message=Labels.getLabel("resources_rsbatch_return.message");
		path_label.setValue(pageTitle);//電子資源管理>>批次匯入>>比對結果>>上傳成功
		warn1.setValue(Labels.getLabel("resources_rsbatch_return.warn1mess"));
		warn2.setValue(Labels.getLabel("resources_rsbatch_return.warn2mess"));
		 String selectedName=null;
		//combobox中選中的值（定義在Resources_upload_result類中）
		if(session.getAttribute("selectedName")!=null){//如果不為空則清空session
			selectedName=session.getAttribute("selectedName").toString();
		}
		if(selectedName!=null){	
			String content="";
			if(selectedName.equals("exist")){
				content=Labels.getLabel("resources_rsbatch_return.isHere");//已存在
			}else if(selectedName.equals("noExist")){
				content=Labels.getLabel("resources_rsbatch_return.noIsHere");//未存在
			}
				return_btn.setLabel(returnMes+content+message);//"返回"+content+"資料"
		}
		leava_btn.setLabel(Labels.getLabel("resources_rsbatch_return.live"));//離開
		
		String successNumber="";//記錄處理成功的數據筆數
		if(session.getAttribute("successNumber")!=null){
			successNumber=session.getAttribute("successNumber").toString();
		}
		
		success_resources.setValue(successNumber);
	}
	/**
	 * 離開
	 */
	@Listen("onClick=#leava_btn")
	public void onClick$leava_btn(){
		//確認是否需要離開頁面
		int num=Messagebox.show(message1,message2,Messagebox.YES|Messagebox.NO,Messagebox.QUESTION);//"確定離開？","提示"
		if(num==Messagebox.OK){
			//Executions.sendRedirect("/resources_rsbatch.jsf");
			String url="resourcesRsbatch/resourcesRsbatch.zul";
			ZkUtils.refurbishMethod(url);	
		}
		
			
		}

	
	
	/**
	 * 返回
	 */
	@Listen("onClick=#return_btn")
	public void onClick$return_btn(){
		//Executions.sendRedirect("/resources_upload_result.jsf");
		Session session=Sessions.getCurrent();
		session.setAttribute("comb_resource_type", resources_code_id);
		String url="resourcesRsbatch/resourcesUploadResult.zul";
		ZkUtils.refurbishMethod(url);	
	}
}
