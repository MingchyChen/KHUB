package com.claridy.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Breadcurmbs {
	// 麵包屑顯示的字串
	private StringBuffer breadcurmbs;
	// session
	private HttpSession session;
	// 當前的節點屬於哪一級
	private int noteLocation;
	// 節點集合
	private List<String[]> noteList;
	
	
	public List<String[]> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<String[]> noteList) {
		this.noteList = noteList;
	}

	/**
	 * 構造方法
	 * @param noteLocation 當前的節點屬於哪一級
	 * @param request HttpServletRequest
	 */
	@SuppressWarnings("unchecked")
	public Breadcurmbs(HttpServletRequest request){
		this.breadcurmbs = new StringBuffer();
		this.session = request.getSession();

		// 獲取session中的noteList，沒有就初始化
		this.noteList = (List<String[]>) session.getAttribute("noteList");
		if(this.noteList == null){
			this.noteList = new ArrayList<String[]>();
		}
	}
	
	public StringBuffer getBreadcurmbs() {
		return breadcurmbs;
	}

	/**
	 * 設置麵包屑
	 */
	public void setBreadcurmbs(int noteLocation) {
		
		// 取得noteList中的內容
		for(int i = 0; i < noteList.size(); i++){
			String[] note = noteList.get(i);
			//判斷節點位置是否小於i，是則添加，否則刪除
			// 檢查節點內容是否正確
				if(note.length == 2){
					// 添加節點
					if(note[0].equals("")){
						breadcurmbs.append(note[1]+"&gt;");
					}else if(i==0){
						breadcurmbs.append("&gt;<a href='"+note[0]+"'>"+note[1]+"</a>");   
				    }  
					else{   
				    	breadcurmbs.append("&gt;<a href=\"javascript:loadUrl('"+note[0]+"')\" title='"+note[1]+"'>"+note[1]+"</a>");  
				    }  
				}
		}
		if(breadcurmbs.length() > 0){
			breadcurmbs.insert(0, ">");
		}
		session.setAttribute("noteList", noteList);
		session.setAttribute("breadcurmbs", breadcurmbs.toString());
	}
	
	/**
	 * 設置麵包屑
	 * @param noteChild 要添加的節點,length為2
	 */
	public void setBreadcurmbs(int noteLocation,String[] noteChild) {
		while(noteLocation < noteList.size()){
			noteList.remove(noteList.size() - 1);
		}
		if(noteLocation < noteList.size()){
			noteList.add(noteLocation,noteChild);
		}else{
			noteList.add(noteChild);
		}
		setBreadcurmbs(noteLocation);
	}
	
	public boolean isExist(String childName){
		boolean isExist =false;
		for(int i=0;i<noteList.size();i++){
			String[] child= noteList.get(i);
			if(childName != null && childName.equals(child[1])){
				isExist = true;
			}
		}
		return isExist;
		
	}
}
