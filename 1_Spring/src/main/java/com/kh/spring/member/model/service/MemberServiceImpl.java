package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

//"mService" 이름 붙여주면 조금 더 정확해짐, 바로 찾을 수 있어서
@Service("mService") // 이렇게 하면 객체 관리를 framework가 함(프레임워크가 프로그램 실행시 객체 생성하여 관리)
public class MemberServiceImpl implements MemberService {

	// 인터페이스 끼지 않고 하는 방식(기업마다 사용방식 다름) -> mDAO는 인터페이스  implements하지 않고 만듦
	@Autowired // pramework가 만들어놓은 객체를 변수 mDAO에 주입시킴(의존성 주입 DI) 
	private MemberDAO mDAO;
	
	// 인터페이스 끼고 하는 방식
	@Override
	public Member memberLogin(Member m) { // 추상메소드 오버라이딩(강제 재작성)
		return null;
	}

}
