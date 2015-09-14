package com.claridy.facade;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.hibernateimpl.WebOpinionDAO;
import com.claridy.domain.WebEmployee;
import com.claridy.domain.WebOpinion;
import com.claridy.domain.WebOpinionReply;

@Service
public class WebOpinionService {
	@Autowired
	private WebOpinionDAO webopiniondao;
	
	public List<WebOpinion> findOpinionInfoAll(WebEmployee webEmployee){
		return webopiniondao.findOpinionInfoAll(webEmployee);
	}
	public List<WebOpinion> findOpinionByTitle(WebEmployee webEmployee,String title){
		return webopiniondao.findOpinionByTitle(webEmployee, title);
	}
	public List<WebOpinion> findedtAddList(String searchType, String searchValue){
		return webopiniondao.findedtAddList(searchType, searchValue);
	}
	public List<WebOpinionReply> findReplyList(String searchType, String searchValue){
		 List<WebOpinionReply> list=webopiniondao.findReplyList(searchType, searchValue);
		 for (WebOpinionReply search : list) {
				String content=this.HtmlText(search.getReplyZhTw());
				search.setReplyZhTw(content);
			}
		return list;
	}
	public void addOpinionReply(WebOpinionReply webOpinionReply){
		webopiniondao.saveOrUpdate(webOpinionReply);
	}
	public void updateOpinionReply(WebOpinionReply webOpinionReply){
		webopiniondao.merge(webOpinionReply);
	}
	public WebOpinionReply findReply(String searchType, String searchValue){
		WebOpinionReply re=webopiniondao.findReply(searchType, searchValue);
		String content=this.HtmlText(re.getReplyZhTw());
		re.setReplyZhTw(content);
		return re;
	}
	
	public static String HtmlText(String inputString) { 
	      String htmlStr = inputString; //含html標籤的字串 
	      String textStr =""; 
	      java.util.regex.Pattern p_script; 
	      java.util.regex.Matcher m_script; 
	      java.util.regex.Pattern p_style; 
	      java.util.regex.Matcher m_style; 
	      java.util.regex.Pattern p_html; 
	      java.util.regex.Matcher m_html; 
	      try { 
	       String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定義script的正則運算式{或<script[^>]*?>[\\s\\S]*?<\\/script> } 
	       String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定義style的正則運算式{或<style[^>]*?>[\\s\\S]*?<\\/style> } 
	          String regEx_html = "<[^>]+>"; //定義HTML標籤的正則運算式 
	      
	          p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	          m_script = p_script.matcher(htmlStr); 
	          htmlStr = m_script.replaceAll(""); //過濾script標籤 
	
	          p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	          m_style = p_style.matcher(htmlStr); 
	          htmlStr = m_style.replaceAll(""); //過濾style標籤 
	      
	          p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	          m_html = p_html.matcher(htmlStr); 
	          htmlStr = m_html.replaceAll(""); //過濾html標籤 
	          
	          /* 空格 ——   */
	         // p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);
	          m_html = p_html.matcher(htmlStr);
	          htmlStr = htmlStr.replaceAll(" "," ");

	          textStr = htmlStr; 
	      
	      }catch(Exception e) { 
	      } 
	      return textStr;
	}
}
