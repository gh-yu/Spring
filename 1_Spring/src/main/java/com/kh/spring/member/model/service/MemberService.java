package com.kh.spring.member.model.service;

import com.kh.spring.member.model.vo.Member;

public interface MemberService {
	
	Member memberLogin(Member m); // 추상메소드 만듦

	int insertMember(Member m);

//	String getEncPwd(String id);

	int updateMember(Member m);

	int updatePwd(Member m);

	int duplicateId(String id);
}
