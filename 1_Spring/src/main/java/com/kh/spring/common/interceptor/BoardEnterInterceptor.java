package com.kh.spring.common.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.AsyncHandlerInterceptor;

public class BoardEnterInterceptor implements AsyncHandlerInterceptor {
	
//	private Logger logger = LoggerFactory.getLogger(BoardEnterInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			
		// 또는 topList.bo가 아닐 때만? 아니면 .bo에 해당하는 모든 url? /spring/blist.bo일 때만? -> board-context.xml에서 등록설정하면 됨
		if (/* request.getRequestURI().equals("/spring/blist.bo") && */request.getSession().getAttribute("loginUser") == null) {
//			logger.debug("no login");
//			
//			response.setContentType("text/html; charset=UTF-8");
//			PrintWriter script = response.getWriter();
//			script.println("<script>");
//			script.println("alert('로그인 후 이용해주세요.')");
//			script.println("location.href='home.do'");
//			script.println("</script>");
//			return false;
			request.setAttribute("msg", "로그인 후 이용하세요");
			request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
			return false;
		}		
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
	}
}
