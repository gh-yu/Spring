package com.kh.spring.member.model.exception;

public class MemberException extends RuntimeException { // 그냥 Exception상속 받는게 아니라 RuntimeException 상속
	// RuntimeException : Unchecked Exception -> 체크하지 않아도 되는 에러
	// 이렇게 하면  thorws로 예외 강제 발생시켜도 try catch안해도 됨
	
	public MemberException() {}
	public MemberException(String msg) {
		super(msg);
	}
}
