package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// public class TestInterceptor extends HandlerInterceptorAdapter{ // The type HandlerInterceptorAdapter is deprecated 취소줄 생김(사용 권장
// public class TestInterceptor implements HandlerInterceptor {
public class TestInterceptor implements AsyncHandlerInterceptor { // 둘 중에 하나 쓰면 됨
	
	private Logger logger = LoggerFactory.getLogger(TestInterceptor.class);
	
	// ctrl + space바 클릭 후 메소드 선택
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 전처리
		// DispatcherServlet이 Controller를 호출하기 전(Controller로 요청이 들어가기 전)에 수행
		
		if(logger.isDebugEnabled()) { // if문으로 debug level인지 확인 후 로그 찍음
			logger.debug("========= START =========");
			logger.debug(request.getRequestURI());
		}
		
//		return HandlerInterceptor.super.preHandle(request, response, handler); // HandlerInterceptor을 implements할 경우
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler); // 항상 true로 반환
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		// 후처리
		// Controller에서 DispatcherServlet으로 리턴되는 순간에 수행
		
		if(logger.isDebugEnabled()) {
			logger.debug("-------- view --------");
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		// 뷰단 처리 후
		// 모든 뷰에서 모든 작업(최종 결과를 생성하는 일 포함)이 완료된 후 수행
		
		if(logger.isDebugEnabled()) {
			logger.debug("========= END =========\n");
		}
	}
}
