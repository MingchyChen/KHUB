package com.claridy.admin.composer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Textbox;

import com.claridy.domain.ErmSystemSetting;
import com.claridy.facade.SystemSettingService;

/**
 * sunchao nj
 * 2014/07/29
 * Ezproxy設定檔維護
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class SystemEzproxyComposer extends GenericForwardComposer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private Textbox txtContent;
	String formAction = "";// 儲存表單action的value

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadEzproxyTxt();
		loadEzproxy();
	}
	/**
	 * 加載ezproxy的內容
	 * @param connFactory
	 * @param conn
	 * @throws IOException 
	 */
	public void loadEzproxyTxt(){
		try {
			ErmSystemSetting sysSetting=((SystemSettingService)SpringUtil.getBean("systemSettingService")).getSysByFunID("EZPROXY_CONFIG_TXT");
			String txtPath=sysSetting.getFuncValue();
			//String txtPath=((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyConfigTxt;
	  		File file=new File(txtPath);
	  		StringBuffer sfBuffer=new StringBuffer();
	  		String encoding = "UTF-8";
	  		if(file.exists() && file.canRead() && (file.getName().lastIndexOf(".txt") > 0)) {
		  		try {
		  			InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
			  		BufferedReader buffer = new BufferedReader(read);
			  		String lineTxt = null;
			  		while ((lineTxt = buffer.readLine()) != null ) {
			  			sfBuffer.append(lineTxt+"\n");
			  		}
			  		if (read != null) {
			  			read.close();
			  		}
				} catch (Exception e) {
					// TODO: handle exception
					log.error("加載ezproxy的內容",e);
				}
	  		}
	  		txtContent.setText(sfBuffer.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 加載ezproxy的數據
	 * 
	 * @param connFactory
	 * @param conn
	 */
	public void loadEzproxy() {
		try{
			String restart_url = "", pass = "", user = "";
			
			ErmSystemSetting sysSetting=new ErmSystemSetting();
			List<ErmSystemSetting> setList = ((SystemSettingService)SpringUtil.getBean("systemSettingService")).findSysSettingList("funcId in ('Ezproxy Login','Ezproxy Restart','EzproxyRestart pass','EzproxyRestart user')");
			for (int i = 0; i < setList.size(); i++) {
				sysSetting = setList.get(i);
				if (sysSetting.getFuncId().equals("Ezproxy Login")) {
					formAction = sysSetting.getFuncValue();
				} else if (sysSetting.getFuncId().equals("Ezproxy Restart")) {
					restart_url = sysSetting.getFuncValue();
				} else if (sysSetting.getFuncId().equals("EzproxyRestart pass")) {
					pass = sysSetting.getFuncValue();
				} else if (sysSetting.getFuncId().equals("EzproxyRestart user")) {
					user = sysSetting.getFuncValue();
				}
			}
			
			/*formAction = ((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyLogin;
			restart_url = ((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyRestart;
			pass = ((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyPass;
			user = ((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyUser;*/
			
			Clients.evalJavaScript("loadData('" + restart_url + "','" + user
					+ "','" + pass + "')");// 給表單賦值
		} catch (Exception e) {
			// TODO: handle exception
			log.error("加載ezproxy的數據",e);
		}
	}
	/**
	 * 更新
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * 
	 * @throws InterruptedException
	 */
	public void onClick$update_button(){
		try{
			String txtCon=txtContent.getText();
			ErmSystemSetting sysSetting=((SystemSettingService)SpringUtil.getBean("systemSettingService")).getSysByFunID("EZPROXY_CONFIG_TXT");
			String txtPath=sysSetting.getFuncValue();
			//String txtPath=((SystemProperties)SpringUtil.getBean("systemProperties")).ezproxyConfigTxt;
	  		if(txtPath==null||"".equals(txtPath)){
	  	  		alert(Labels.getLabel("SystemEzproxy.error"));
	  		}else{
	  			//導出路徑
	  			String webPath = txtPath;
	  			try {
	  				BufferedWriter bufferFileWriter = new BufferedWriter(
	  	  					new OutputStreamWriter(new FileOutputStream(webPath), "UTF-8"));
	  	  			bufferFileWriter.append(txtCon);
	  	  			bufferFileWriter.newLine();
	  	  			bufferFileWriter.close();
	  	  			alert(Labels.getLabel("updateOK"));
		  	  		loadEzproxyTxt();
				} catch (Exception e) {
					// TODO: handle exception
				}
	  	  		
	  		}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("更新",e);
		}
	}
	/**
	 * Ezproxy Restart
	 */
	public void onClick$ezproxy_restart_button() {
		Clients.evalJavaScript("restart('" + formAction + "')");
	}
}
