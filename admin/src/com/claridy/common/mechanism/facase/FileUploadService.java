package com.claridy.common.mechanism.facase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.claridy.common.mechanism.dao.ISys_FileDAO;
import com.claridy.common.mechanism.domain.FileUploadModel;
import com.claridy.common.mechanism.domain.Sys_File;
//import com.google.gson.Gson;

@Service
public class FileUploadService {
	
	@Autowired
	private ISys_FileDAO sys_FileDAO;
	
	final String IMG_TYPE = "gif,jpg,png,bmp"; 
	
	final String ICON_FILE = "/images/common/file.gif";  
	
	final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	// 時間+3碼亂碼
	public String getNowTimeString(){
		String str = dateFormat.format(new Date()) + (int)(Math.random()*100) + "-" ;
		return str;
	}
	
	// 上傳檔案
	public List<FileUploadModel> uploadFile(String uploadPath, MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws IOException {
		
		List<FileUploadModel> uploadedFiles = new ArrayList<FileUploadModel>();
		try {			
			String nowTime = this.getNowTimeString();
			String linkURL = multipartRequest.getRequestURL().toString().replaceAll(multipartRequest.getServletPath(), "") + "/";
			String homePath = multipartRequest.getSession().getServletContext().getRealPath("/");
			if (multipartRequest != null) {  
				Iterator<String> iterator = multipartRequest.getFileNames();
	
				while (iterator.hasNext()) {
					MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
	
					if (StringUtils.hasText(multifile.getOriginalFilename())) {
						FileUploadModel u = new FileUploadModel();
						String name = nowTime + multifile.getOriginalFilename();
						name=URLEncoder.encode(name,"utf-8");
						u.setName(uploadPath + name);
						u.setSize(Long.valueOf(multifile.getSize()).intValue());
						u.setUrl(linkURL + uploadPath + name);
						
//						String fileType = this.getFileType(multifile.getOriginalFilename());
//						if (!"".equals(fileType) && IMG_TYPE.indexOf(fileType) != -1) {
							u.setThumbnail_url(linkURL + uploadPath + name);							
//						}else {
//							u.setThumbnail_url(linkURL + ICON_FILE);		
//						}
						uploadedFiles.add(u);
						
						this.saveFileToServer(multifile, homePath + uploadPath, nowTime);
					}
				}
			}
//			this.json_encode(response, uploadedFiles);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadedFiles;
	}

	/**
	 * 描述 : <將檔保存到指定路徑>. <br>
	 * <p>
	 * 
	 * @param multifile
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private String saveFileToServer(MultipartFile multifile, String path, String nowTime) throws IOException {
		
		// 創建目錄	
		this.mkDir(path);

		// 讀取檔流並保持在指定路徑
		InputStream inputStream = multifile.getInputStream();
		OutputStream outputStream = new FileOutputStream(path + nowTime + multifile.getOriginalFilename());
		byte[] buffer = multifile.getBytes();
		
		@SuppressWarnings("unused")
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = inputStream.read(buffer)) != -1) {
			bytesum += byteread;
			outputStream.write(buffer, 0, byteread);
			outputStream.flush();
		}
		outputStream.close();
		inputStream.close();

		return path + multifile.getOriginalFilename();
	}

	public void json_encode(final HttpServletResponse response, Object o) throws IOException {
//		header('Pragma: no-cache');
//		header('Cache-Control: no-store, no-cache, must-revalidate');
//		header('Content-Disposition: inline; filename="files.json"');
//		header('X-Content-Type-Options: nosniff');
//		header('Access-Control-Allow-Origin: *');
//		header('Access-Control-Allow-Methods: OPTIONS, HEAD, GET, POST, PUT, DELETE');
//		header('Access-Control-Allow-Headers: X-File-Name, X-File-Type, X-File-Size');
		
		response.setContentType("application/json");
		
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Content-Disposition", "inline");
		response.setHeader("filename", "files.json");
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setDateHeader("Expires", 0);
		
		//PrintWriter out = response.getWriter();
		//Gson gs = new Gson();
		//out.write(gs.toJson(o));
	}
	
	/**
	 * 建立資料夾(可建多層資料夾)
	 * 
	 * @param path
	 * @param 最後一層的資料夾
	 */
	public String mkDir(String path) {
		String[] pathAry = path.split("[/]|\\\\");

		StringBuffer list = new StringBuffer();
		for (int i = 0; i < pathAry.length; i++) {
			if (!pathAry[i].equals("")) {
				list.append(pathAry[i] + "/");
				File dir = new File(list.toString());
				if (!dir.isDirectory()) {
					dir.mkdir();
				}
			}
		}
		return list.toString();
	}
	
	/**
	 * 取出檔案的副檔名
	 */
	public String getFileType(String fileName){
		String fileType = "";
		try {
			fileType = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileType.toLowerCase();
	}
	
	/**
	 * 根據file_name得到display_file_name
	 */
	public String getDisplay_file_name(String file_name){
		String display_file_name = "";
		try {
			display_file_name = file_name.substring(file_name.lastIndexOf("/")+1, file_name.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return display_file_name;
	}
	
	/**
	 * 刪除sys_file裡該筆資料
	 * @param file_pk
	 */
	public void deleteFile(String file_pk){
		Sys_File sys_File = sys_FileDAO.find(file_pk);
		try {
			this.deleteFiletoServer(sys_File.getFile_name());
			sys_FileDAO.delete(sys_File);
//			boolean hasDelete = this.deleteFiletoServer(null, sys_File.getFile_name());
//			if (hasDelete) {
//				sys_FileDAO.delete(sys_File);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 刪除實體檔案
	 * 
	 */
	public boolean deleteFiletoServer(String file) throws IOException {
		boolean success = Boolean.FALSE;
		String path = FileUploadService.class.getClassLoader().getResource("").getPath();
		path = path.substring(path.indexOf("/")+1);
		path = path.substring(0, path.indexOf("WEB-INF"));

		File f = new File(path + file);
		if (f.exists()) {
			f.delete();
			success = Boolean.TRUE;
		}

		return success;
	}
	
	/**
	 * 複製實體檔案
	 */
	public String copyFile(String path, String file_name, String display_file_name){
		String newFile_name = "";
		try {
			String location = file_name.substring(0, file_name.lastIndexOf("/")+1);
			newFile_name = location + this.getNowTimeString() + display_file_name;
			
			File srcFile = new File(path + file_name);
			File dstFile = new File(path + newFile_name);
	
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFile));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dstFile));
	
			byte[] tmp = new byte[1];  
			while (in.read(tmp) != -1) {
				out.write(tmp);
			}
			in.close();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFile_name;
	}
}