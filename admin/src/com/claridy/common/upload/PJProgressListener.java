package com.claridy.common.upload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import com.claridy.common.util.ProgressEntity;


/**
 * @author TPWEN 2014-2-24 下午05:20:09
 * 
 * 默認的資訊回饋對象，實現介面中定義的必要方法
 */
public class PJProgressListener  implements ProgressListener
{

	private HttpSession session;
	
	public PJProgressListener() {  
	}

	public PJProgressListener(HttpSession _session) {  
		session=_session;  
		ProgressEntity ps = new ProgressEntity();  
		session.setAttribute("upload_ps", ps);  
	}

	
	public void update(long pBytesRead, long pContentLength, int pItems) {
		// TODO Auto-generated method stub
		ProgressEntity ps = (ProgressEntity) session.getAttribute("upload_ps");  
		ps.itemNum = pItems;  
		ps.readSize = pBytesRead;  
		ps.totalSize = pContentLength;  
		//ps.show = pBytesRead+"/"+pContentLength+" byte"; 
		ps.show=Math.round(new Float(pBytesRead) / new Float(pContentLength)*100)+"%";
		ps.rate = Math.round(new Float(pBytesRead) / new Float(pContentLength)*100);
  
		//更新  
		session.setAttribute("upload_ps", ps);

		
	}

   
}