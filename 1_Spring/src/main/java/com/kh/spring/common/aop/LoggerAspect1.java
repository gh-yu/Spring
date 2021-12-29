package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerAspect1 {
	
	private Logger logger = LoggerFactory.getLogger(LoggerAspect1.class);
	
	public /*void*/ Object loggerAdvice(/*JoinPoint*/ ProceedingJoinPoint joinPoint) { // ProceedingJoinPoint : Around Advice에 필요한 파라미터
		Signature signature = joinPoint.getSignature();
		logger.debug("signature : " + signature); // 실행하고 있는 메소드 정보가 출력됨
		
		String type = signature.getDeclaringTypeName(); // 메소드가 속해있는 클래스의 이름
		String methodName = signature.getName(); // 메소드 이름
		logger.debug("type : " + type);
		logger.debug("methodName : " + methodName);
		
		String componentName = null;
		if(type.indexOf("Controller") > -1) { // "Controller"를 포함하지 않으면 -1을 반환
			componentName = "Controller  \t:  ";
		} else if(type.indexOf("Service") > - 1) {
			componentName = "ServiceImpl  \t:  ";
		} else if(type.indexOf("DAO") > -1) {
			componentName = "DAO  \t\t:  ";
		}
		
		logger.debug("[Before] " + componentName + type + "." + methodName + "()");
		
		Object obj = null;
		try {
			obj = joinPoint.proceed(); // 진행할 수 있도록 
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		logger.debug("[After] " + componentName + type + "." + methodName + "()");
		
		return obj;
	}
}
