package com.claridy.common.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class FileUpUtil {

//	public static void main(String args[]){
//		//得到要保存的路徑
//		System.out.println(getFilePath("C:\\Users\\chentao\\workspaceForNTCL\\ntclportal\\src\\main\\webapp\\"));
//		//得到檔保存在伺服器上的名稱,
////		getServerFile("grs","a.doc");
//	}
	public static String getFilePath(String path){
		//得到已存在的資料夾中應該保存的那個子資料夾
		//begin>>
		path=path+"document\\";
		File file = new File(path);
		File[] fileList=file.listFiles();
		if(file.exists()){
			String zFileName="";
			int maxNum=0;
			if(fileList.length==0){
				zFileName="file_0";
				File myFilePath=new File(path+"\\"+zFileName);
				myFilePath.mkdir();
			}else{
				for(int i=0;i<fileList.length;i++){
					//裡面有個檔desktop.ini
					if("desktop.ini".equals(fileList[i].getName())){
						continue;
					}
					zFileName=fileList[i].getName();
					try{
						if(maxNum<Integer.parseInt(zFileName.substring(zFileName.lastIndexOf("_")+1,zFileName.length()))){
							maxNum=Integer.parseInt(zFileName.substring(zFileName.lastIndexOf("_")+1,zFileName.length()));
						}
					}catch (Exception e) {
						zFileName="file_"+maxNum;
					}
				}
			}
		//<<end
		//判斷該資料夾的大小有沒有起過10G
		//獲得子資料夾的路徑
			String mainPath=path;
			path=path+zFileName;
			long fileSize=getFileSize(path);
			//當檔大於10時創建一個資料夾，返回創建的資料夾路徑
			if(fileSize>1*1024*1024){ 
				maxNum++;
				File myFilePath=new File(mainPath+"\\"+zFileName.substring(0,zFileName.lastIndexOf("_")+1)+(maxNum));
				if(myFilePath.exists()){//判斷新增的檔是否存在
					try{
					myFilePath.delete();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				try{
					myFilePath.mkdir();
				}catch(Exception e){
					e.printStackTrace();
				}
				path=zFileName.substring(0,zFileName.lastIndexOf("_")+1)+maxNum;
			}
		}else{
//			System.out.println("檔不存在");
		}
		return path;
	}
	
	public static long getFileSize(String path){
		long size=0;
		File file= new File(path);
		if(file.exists()){
			//得到子資料夾中所有的檔
			File []fileList=file.listFiles();
			
			File softFile;
			
			//遍歷子資料夾中的所有檔獲得檔的大小後加總
			for(int i=0;i<fileList.length;i++){
				if(fileList[i].isFile()){//判斷是否是檔
					//System.out.println(fileList[i].getName());
					softFile=new File(path+"\\"+fileList[i].getName());
					FileInputStream fis = null;
	                try{
	                    fis = new FileInputStream(softFile);  
	                    size=size+fis.available()/1000;
	                }catch(IOException e1){   
//	                    System.out.println("IO出錯！");
	                }  
				}else{
					size=size+getFileSize(path+"\\"+fileList[i].getName());
				}
			}
		}
		return size;
	}
	public static String getServerFile(String loginName,String fileName){
		return loginName+"_"+new Date().getTime()+"_"+Math.random()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
	}
}

