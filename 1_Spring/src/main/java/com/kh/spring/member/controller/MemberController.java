package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller // 객체 생성, Controller 성질을 갖게 생성(MemberController를 Controller객체로 등록시키려면 @Controller 어노테이션 추가해야됨, 어노테이션 없을시 객체 등록이 안됨)
public class MemberController {
	
	// private MemberService mService = new MemberService();
	
	@Autowired // Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities.
	private MemberService mService; 
	// 인터페이스를 불러오는 것이 됨 -> MemberService이름 변경해도 영향은 없지만 객체를 생성하고 있진 않음
	// MemberServiceImpl클래스에서 @이용 -> @Service("mService") -> 이렇게 하면 객체 관리를 framework가 함(프레임워크가 객체 생성)
	// private MemberService mService;만 하면 객체는 framework통해 만들어졌으나, 만들어진 객체를 mService에 집어넣는, 주입하는 과정이 없음
	// @Autowired를 붙여주면 됨 : DI 의존성 주입 -> pramework가 만들어놓은 객체를 변수 mService에 주입시킴
	
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login() {
//		System.out.println("로그인 처리합니다.");
//	}
	
	/*********** 파라미터 전송 받기 ***********/
	// 1, HttpServletRequest를 통해 전송받기(JSP/Servlet방식)
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(HttpServletRequest request) {
//		String id1 = request.getParameter("id");
//		String pwd1 = request.getParameter("pwd");
//		
//		System.out.println("id1 : " + id1);
//		System.out.println("pwd1 : " + pwd1);
//		
//	}
	
	// 2. @RequestParam 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@RequestParam(value="id", defaultValue="hello") String userId, 
//					  @RequestParam(value="pwd", defaultValue="world") String userPwd,
//					  @RequestParam(value="test", required=false, defaultValue="spring") String test) {
//		// @RequestParam 
//		// value 		: 뷰에서 받아오는 파라미터의 이름, value가 한 개라면 value=샐략하고 "변수명"만 써도 됨
//		// defaultValue : 값이 들어오지 않았거나 null일때 기본적으로 들어갈 value 설정
//		// required 	: 내가 받아오려는 파라미터가 꼭 필요한 파라미터인지, 필수로 받아와야하는지 설정하는 것
//		//				     값이 들어오는 것이 필수가 아닐때 false 지정 -> 값 넣지 않고 이동시 null로 들어옴, defaultValue설정되어 있을시 그 값으로 들어옴
//		//  			     기본값은 true로 만약 값이 들어오지 않는다면 DefaultHandlerExceptionResolver에러 뜸
//		System.out.println("id2 : " + userId);
//		System.out.println("pwd2 : " + userPwd);
//		System.out.println("test : " + test);
//	}
	
	// 3. @RequestParam 생략
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(String id, String pwd) {
//		// 매개변수 이름을 받아올 파라미터명과 일치시켜줌 -> @RequestParam 생략 가능해짐
//		// 받아올 파라미터명과 매개변수명을 동일하게 하면 @RequestParam 생략해도 자동매칭시켜줌
//		// 생략시 defaultValue와 required속성 사용할 수 없게 됨
//		System.out.println("id3 : " + id);
//		System.out.println("pwd4 : " + pwd);
//	}
	
	// 4. @ModelAttribute 방식
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(@ModelAttribute Member m) {
//		// url의 파라미터명과 객체의 setter의 필드명 일치하면 객체의 그 필드에 setting됨
//		//        id        ->     setId
//		System.out.println("id4 : " + m.getId());
//		System.out.println("pwd4 : " + m.getPwd());
//	}
	
	// 5. @ModelAttribute 생략 -> 지양
//	@RequestMapping(value="login.me", method=RequestMethod.POST)
//	public void login(Member m) {
//		System.out.println("id5 : " + m.getId());
//		System.out.println("pwd5 : " + m.getPwd());
//	}
	
	@RequestMapping(value="login.me", method=RequestMethod.POST)
	public void login(@ModelAttribute Member m) {
		
		// 이전 사용했던 new MemberService()해서 객체를 만드는 방식은 주도권이 나한테 있으며 결합도가 높아짐
		// 결합도가 높다는 것을 확인할 수 있는 두 가지는 1. 클래스 명 변경에 직접적인 영향을 받음, 2. 매 요청마다 새로운 service객체 생성
		// MemberService mService = new MemberService();
		// System.out.println(mService);
		// 여기서 MemberService mService = new MemberService();로 객체 생성했을 때
		// com.kh.spring.member.model.service.MemberService@669e740e
		// com.kh.spring.member.model.service.MemberService@6fa508b4
		// 요청 들어갈 때마다 객체 주소값 바뀌는 것 확인 가능 -> 결합도 높음
		// MemberService 클래스명 변경시 여기서 해당 클래스 찾지 못해 에러 뜨는것 확인 가능 -> 결합도 높음
		
		// 맨 위에서 MemberService객체 생성 코드 필드로 작성
		// -> 요청 여러번 들어와도 객체 주소값 동일
		// com.kh.spring.member.model.service.MemberService@52b3c12d
		// com.kh.spring.member.model.service.MemberService@52b3c12d
		// 클래스명 변경시는 에러 여전함 
		// 해결 방법 : MemberService인터페이스 만들어 implements하고 
		// MemberSerivceImpl클래스에서 @Service어노테이션을 통해 객체 관리를 framework가 하도록 시킴(프레임워크가 객체 생성)
		// 그리고 MemberController 필드 쪽에 아래 작성 -> 클래스명 변경에 영향x, 요청 여러 번 들어와도 객체 주소값 동일 -> 결합도 낮음
		// @Autowired 
		// private MemberService mService; 
//		com.kh.spring.member.model.service.MemberServiceImpl@727332a5
//		com.kh.spring.member.model.service.MemberServiceImpl@727332a5
		
		Member loginMember = mService.memberLogin(m); // 인터페이스의 메소드 호출한 것, MemberServiceImpl클래스에서 추상화 메소드를 오버라이딩(재작성) 강제로 하게 됨
	}
}



