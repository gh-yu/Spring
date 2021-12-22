package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

//"mService" 이름 붙여주면 조금 더 정확해짐, 바로 찾을 수 있어서
@Service("mService") // 이렇게 하면 객체 관리를 framework가 함(프레임워크가 프로그램 실행시 객체 생성하여 관리)
public class MemberServiceImpl implements MemberService {

	// 인터페이스 끼지 않고 하는 방식(기업마다 사용방식 다름) -> mDAO는 인터페이스  implements하지 않고 만듦
	@Autowired // framework가 만들어놓은 객체를 변수 mDAO에 주입시킴(의존성 주입 DI) 
	private MemberDAO mDAO;
	
	// root-context.xml에서 DB 연결
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	// 인터페이스 끼고 하는 방식
	@Override
	public Member memberLogin(Member m) { // 추상메소드 오버라이딩(강제 재작성) -> 구현되지 않은 메소드 작성
		
		Member loginUser = mDAO.memberLogin(sqlSession, m);
		
		return loginUser;
	}

	@Override
	public int insertMember(Member m) {

		int result = mDAO.insertMember(sqlSession, m);
		
		return result; // 지금은 자동 커밋이라서 나중에 커밋 롤백 조건 추가
	}

//	@Override
//	public String getEncPwd(String id) {
//		
//		String encPwd = mDAO.getEncPwd(sqlSession, id);
//		
//		return encPwd;
//	}

	@Override
	public int updateMember(Member m) {
		
		int result = mDAO.updateMember(sqlSession, m);
		
		return result;
	}

	@Override
	public int updatePwd(Member m) {
		
		int result = mDAO.updatePwd(sqlSession, m);
		
		return result;
	}

	@Override
	public int duplicateId(String id) {
		int result = mDAO.duplicateId(sqlSession, id);
		return result;
	}

}
