package com.claridy.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.claridy.common.util.XSSStringEncoder;
import com.claridy.domain.WebIndexInfo;
import com.claridy.facade.WebIndexInfoService;

@Controller
public class LoginController {
	@Autowired
	private WebIndexInfoService indexInfoService;
	
	@RequestMapping(value = "")
	public String goHome(HttpServletRequest request, Model model,
			HttpSession session) {
		//獲取頁尾資訊
		WebIndexInfo footerinfo = indexInfoService.getFooterInfo();
		session.setAttribute("footerinfo", footerinfo.getNewsContentZhTw());
		if(request.getSession().getAttribute("loginOK")!=null){
			boolean loginok=(Boolean)request.getSession().getAttribute("loginOK");
			if(!loginok){
				return "system/login";
			}else{
				request.setAttribute("linkUrl", "home");
				return "system/template_pattern";
			}
		}else{
			return "system/login";
		}
	}
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String login(HttpServletRequest request,Model model,
			HttpSession session) {
		//獲取頁尾資訊
		WebIndexInfo footerinfo = indexInfoService.getFooterInfo();
		session.setAttribute("footerinfo", footerinfo.getNewsContentZhTw());
		if(request.getSession().getAttribute("loginOK")!=null){
			boolean loginok=(Boolean)request.getSession().getAttribute("loginOK");
			if(!loginok){
				return "system/login";
			}else{
				request.setAttribute("linkUrl", "home");
				return "system/template_pattern";
			}
		}else{
			return "system/login";
		}
	}

}
