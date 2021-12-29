package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 프레임워크가 객체 생성할 수 있도록 어노테이션(Service, Controller, Repository 성질을 가지지 않음)
@Aspect
public class LoggerAspect2 {
	
	private Logger logger = LoggerFactory.getLogger(LoggerAspect2.class);
	
	
//	@Pointcut("execution(* com.kh.spring.board..*(..))")
//	public void myPointcut() {} // 비어있는 메소드여야 함
	
//	@Around("myPointcut()") // Advice메소드 위 @Around 어노테이션, "myPointcut()" -> 참조할 Pointcut메소드의 이름
	@Around("execution(* com.kh.spring.board..*(..))") // Pointcut을 만들기 위해 비어있는 메소드 선언하지 않고, Around 어노테이션에 Pointcut바로 지정할 수 있음
	public /*void*/ Object loggerAdvice(/*JoinPoint*/ ProceedingJoinPoint joinPoint) throws Throwable { // ProceedingJoinPoint : Around Advice에 필요한 파라미터
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
		
//		Object obj = null;
//		try {
//			obj = joinPoint.proceed(); // 계속 진행할 수 있도록 
//		} catch (Throwable e) { // Throwable은 Exception보다 상위 클래스(잡을 수 없는 Error, 잡을 수 있는 Exception도 다 받아줌)
//			e.printStackTrace(); // DAO에서 에러 발생시 여기서 에러 잡음 -> rollback할 수 없음
//		}
		
		Object obj = joinPoint.proceed(); // try-catch 대신 throws하기(예외 넘기기)
		
		logger.debug("[After] " + componentName + type + "." + methodName + "()");
		
		return obj;
	}
}
