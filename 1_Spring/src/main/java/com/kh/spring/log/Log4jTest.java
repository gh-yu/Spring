package com.kh.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	
	//												다른 클래스 쓰면 그 클래스의 logger를 읽어 들임
	//												어떤 클래스에 있는 로거를 읽을지 쓰면 됨
	private Logger logger = LoggerFactory.getLogger(Log4jTest.class);
	
	public static void main(String[] args) {
		new Log4jTest().test();
	}
	
	public void test() {
//		logger.fatal("fatal 로그"); // fatal은 건드릴 수 없는 영역, 지원하지 않음
		logger.error("error 로그");
		logger.warn("warn 로그");
		logger.info("info 로그");
		logger.debug("debug 로그");
		logger.trace("trace 로그");
	}
}
