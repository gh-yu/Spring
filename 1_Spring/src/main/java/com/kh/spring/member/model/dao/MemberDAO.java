package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository("mDAO") // DAO성질의 객체를 만들어주는 어노테이션
public class MemberDAO {

	public Member memberLogin(SqlSessionTemplate sqlSession, Member m) {
		Member loginUser = sqlSession.selectOne("memberMapper.memberLogin", m);
		
		return loginUser;
	}

	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		int result = sqlSession.insert("memberMapper.insertMember", m);
		
		return result;
	}

//	public String getEncPwd(SqlSessionTemplate sqlSession, String id) {
//		String encPwd = sqlSession.selectOne("memberMapper.getEncPwd", id);
//		
//		return encPwd;
//	}

	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		int result = sqlSession.update("memberMapper.updateMember", m);
		
		return result;
	}

	public int updatePwd(SqlSessionTemplate sqlSession, Member m) {
		int result = sqlSession.update("memberMapper.updatePwd", m);
		
		return result;
	}

	public int duplicateId(SqlSessionTemplate sqlSession, String id) {
		return sqlSession.selectOne("memberMapper.duplicateId", id);
	}
	
}
